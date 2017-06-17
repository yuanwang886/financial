package com.finance.p2p.biz.sms.service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finance.p2p.biz.common.bean.BaseData;
import com.finance.p2p.biz.common.service.CacheService;
import com.finance.p2p.biz.sms.bean.SmsBean;
import com.finance.p2p.biz.sys.utils.Const;
import com.finance.p2p.biz.sys.utils.Global;
import com.finance.p2p.dao.SmsMapper;
import com.finance.p2p.dao.UserMapper;
import com.finance.p2p.entity.Sms;
import com.finance.p2p.entity.User;
import com.framework.SysConst;
import com.framework.encry.MD5;
import com.framework.exception.BusinessException;
import com.framework.utils.DateUtil;
import com.framework.utils.HttpUtil;
import com.framework.utils.IdWorker;
import com.framework.utils.PubFun;

@Service
public class SMSService {

	@Autowired
	private SmsMapper smsMapper;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private UserMapper userMapper;

	/**
	 * 另外方式调用短信
	 * 
	 * @param phone
	 * @param type
	 * @return
	 */
	public BaseData send(String phone, Integer type) {
		SmsBean sb = new SmsBean();
		sb.setPhone(phone);
		sb.setType(type);
		return send(sb);
	}

	/**
	 * 发送短信验证码
	 * 
	 * @param busstype
	 * @param mobile
	 * @return
	 */
	public BaseData send(SmsBean bean) {

		// 对于发送短信手机号与业务类型必须存在
		String phone = bean.getPhone();
		Integer bussType = bean.getType();

		// 校验手机号码/^(12|13|14|15|17|18)\d{9}$/
		if (!PubFun.checkPhone(phone)) {
			throw new BusinessException("请输入正确的手机号");
		}

		if (bussType == null) {
			throw new BusinessException("参数错误(1013),请校验后重新再试");
		}

		// 校验用户信息
		User user = userMapper.selectByPhone(phone);
		if (bussType.equals(Const.SMSKey.TEMP_1001)) {
			if (user != null) {
				throw new BusinessException("该手机号已注册(1019)");
			}
		} else {
			if (user == null) {
				throw new BusinessException("该手机号未注册(1020)");
			}
		}

		// 随机短信验证码
		String smsCode = PubFun.getRandom(6);
		String tempCode = Const.SMSKey.SMS_TEMP.get(bussType);

		String rep = this.sendSms(phone, smsCode, tempCode);

		Sms smsLog = new Sms();

		// 开始转化
		if (StringUtils.isNoneEmpty(rep)) {
			try {
				rep = java.net.URLDecoder.decode(rep, "gb2312");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			Map<String, String> map = new HashMap<>();
			String[] result = rep.split("&");

			for (String r : result) {

				String[] kv = r.split("=");
				map.put(kv[0], kv[1]);
			}

			if (StringUtils.equals(map.get("result"), "0")) {
				smsLog.setReportStatus(SysConst.YesOrNO.YES);
				cacheService.setValue(tempCode, phone, smsCode, Const.SMSKey.TIMEOUT);
			} else {
				smsLog.setReportStatus(SysConst.YesOrNO.NO);
				smsLog.setErrorCode(map.get("result"));
				smsLog.setRepError(map.get("description"));
			}
		} else {
			throw new BusinessException("发送短信失败(1016),请校验后重新再试");
		}
		smsLog.setRepResult(rep);
		smsLog.setId(IdWorker.getId());
		smsLog.setMobile(phone);
		smsLog.setBusinessType(bussType);

		Date date = new Date();
		smsLog.setCreateTime(date);
		smsLog.setModifyTime(date);
		smsMapper.insert(smsLog);

		BaseData baseData = new BaseData();
		if (smsLog.getReportStatus() == SysConst.YesOrNO.NO) {
			baseData.setError("短信发送失败,请稍后再试.");
		}
		return baseData;
	}

	/**
	 * 短信校验接口处理
	 * 
	 * @param bean
	 * @return
	 */
	public void verify(String mobile, Integer type, String code) {

		// 校验手机号码/^(12|13|14|15|17|18)\d{9}$/
		if (!PubFun.checkPhone(mobile)) {
			throw new BusinessException("请输入正确的手机号");
		}

		if (code == null) {
			throw new BusinessException("参数错误(1015),请校验后重新再试");
		}

		// 从缓存中获取短信验证码进行校验
		this.checkSmsCode(mobile, code, Const.SMSKey.SMS_TEMP.get(type));
	}

	/**
	 * 校验短信正确性
	 * 
	 * @param mobile
	 * @param code
	 * @param type
	 * @return
	 */
	public boolean checkSmsCode(String mobile, String code, String type) {
		String smsCode = cacheService.get(type, mobile);
		if (StringUtils.isEmpty(smsCode)) {
			throw new BusinessException("您的验证码已失效,请重新获取");
		}
		if (!StringUtils.equals(smsCode, code)) {
			throw new BusinessException("您的验证码错误,请重新输入");
		}
		// 说明校验通过，那么我们就删除此条数据
		cacheService.delete(type, mobile);
		return true;
	}

	/**
	 * 短信发送接口
	 * 
	 * @param mobile
	 * @param signName
	 * @param code
	 * @param product
	 * @param tempCode
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private String sendSms(String mobile, String code, String tempCode) {

		StringBuilder sb = new StringBuilder();
		String date = DateUtil.formatDate(new Date());
		sb.append("?username=" + Const.SMSKey.APPKEY);
		sb.append("&password=" + MD5.MD5Encode(Const.SMSKey.APPSECRET + date));
		try {
			sb.append("&content="
					+ java.net.URLEncoder.encode(Global.getConfig(tempCode).replaceAll("code", code), "gb2312"));
			sb.append("&timestamp=" + java.net.URLEncoder.encode(date, "gb2312"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		sb.append("&mobiles=" + mobile);

		String result = HttpUtil.post(Const.SMSKey.URL + sb.toString(), null);

		return result;
	}

}
