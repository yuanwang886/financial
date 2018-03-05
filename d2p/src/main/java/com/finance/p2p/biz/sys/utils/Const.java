package com.finance.p2p.biz.sys.utils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Const {

	/**
	 * 密码加密秘钥
	 * 
	 * @author Administrator
	 *
	 */
	public interface MD5Key {
		String KEY = "TC2017SJT";
	}

	/**
	 * 用户相关参数
	 * 
	 * @author Administrator
	 *
	 */
	public interface USERKey {
		String USER_SESSION = "user";

		String USER_TOKEN = "token";

		Integer UESR = 1; // 手机端操作用户
		Integer ADMIN = 2; // 用户角色 2为后台管理用户

		Integer MAN = 1; // 男人
		Integer FEMALE = 2; // 女人

		Integer LEVEL_0 = 0; // 用户等级普通用户
		Integer LEVEL_1 = 1; // 1级超过1人 小于 5人
		Integer LEVEL_2 = 2; // 1级超过5人
		Integer LEVEL_3 = 3; // 1级超过10人, 团队超过100
		Integer LEVEL_1_U = 1;
		Integer LEVEL_2_U = 5;
		Integer LEVEL_3_U = 10;
		Integer LEVEL_3_T = 100;

		Integer LOCK = 1; // 锁定用户
		Integer UNLOCK = 0; // 未锁定用户

		Integer OPEN = 1; // 消息提醒打开
		Integer CLOSE = 0; // 消息提醒关闭

		String RelationSplit = "_";
	}

	/**
	 * 短信相关参数
	 * 
	 * @author Administrator
	 *
	 */
	public interface SMSKey {

		String URL = Global.getConfig("sms_url");
		String APPKEY = Global.getConfig("sms_appkey");
		String APPSECRET = Global.getConfig("sms_appsecret");

		String SIGNNAME = Global.getConfig("sms_signname");

		Integer TIMEOUT = 5 * 60;

		Integer MAXNUMBER = 6;
		// 以下为对应类型的短信末班
		Integer TEMP_1001 = 1001; // 注册短信
		Integer TEMP_1002 = 1002; // 修改密码
		Integer TEMP_1003 = 1003; // 信息变更
		Integer TEMP_1004 = 1004; // 订单匹配
		Integer TEMP_1005 = 1005; // 确认订单

		Map<Integer, String> SMS_TEMP = new HashMap<Integer, String>() {
			private static final long serialVersionUID = 1L;

			{
				put(TEMP_1001, "SMS_68435076");
				put(TEMP_1002, "SMS_68270129");
				put(TEMP_1003, "SMS_68450052");
				put(TEMP_1004, "SMS_68450054");
				put(TEMP_1005, "SMS_68450055");
			}
		};
	}

	/**
	 * 文件保存路径
	 * 
	 * @author Administrator
	 *
	 */
	public interface UploadKey {

		String UPLOAD_PATH = Global.getConfig("upload_path"); // 文件上传路径

		String SERVER_HTTP_PATH = Global.getConfig("server_http_path"); // http访问路径

	}

	public interface FileKey {

		Integer FILE_1001 = 1001; // 打款图片

	}

	/**
	 * 卖家状态0匹配中 4匹配完成 5 交易完成 9 冻结
	 * 
	 * @author Administrator
	 *
	 */
	public interface StateKey {

		Integer STATE_0 = 0;

		Integer STATE_4 = 4;

		Integer STATE_5 = 5;

		Integer STATE_9 = 9;

		Map<Integer, String> MAP = new HashMap<Integer, String>() {
			private static final long serialVersionUID = 1L;

			{
				put(STATE_0, "等待匹配");
				put(STATE_4, "匹配完成");
				put(STATE_5, "交易完成");
				put(STATE_9, "冻结");
			}
		};
	}

	public interface TimeKey {

		/** 等待年份 */
		Integer USER_PAYTIME = 10;

		Integer HOUR_48 = 48;

		Integer HOUR_12 = 12;
		
		Integer HOUR_0 = 0;

		/** 可以卖出的天数, 经过如下天数后可以卖出 */
		Integer DAY_5 = 5;

		/** 从目前2天没有处理的直接锁定 */
		Integer DAY_2 = 2;

		/** 全款7天后匹配 */
		Integer DAY_7 = 7;
	}

	public interface RateKey {
		/** 日利息 */
		Double DAY_RATE = 0.02;

		/** 经过如下天后不计算利息 */
		Integer DAY_MAX = 8;

		/** 奖金值 */
		BigDecimal RATE1 = new BigDecimal(0.05);
		BigDecimal RATE2 = new BigDecimal(0.03);
		BigDecimal RATE1_T = new BigDecimal(0.06);
		BigDecimal RATE2_T = new BigDecimal(0.04);;
	}

	public interface TypeKey {
		/** 余下款 */
		Integer TYPE_2 = 2;
	}

	/**
	 * 0匹配未打款 1 已打款 2 对方已确认 9 冻结
	 * 
	 * @author Administrator
	 *
	 */
	public interface RecordKey {

		Integer STATE_0 = 0;

		Integer STATE_1 = 1;

		Integer STATE_2 = 2;

		Integer STATE_9 = 9;
	}

	/**
	 * 1 买入 2 卖出 3 利息 4 下级奖金 5 二级奖金 6 内置金额
	 * 
	 * @author Administrator 用户钱包轨迹
	 *
	 */
	public interface TrackKey {

		Integer TYPE_1 = 1;

		Integer TYPE_2 = 2;

		Integer TYPE_3 = 3;

		Integer TYPE_4 = 4;

		Integer TYPE_5 = 5;
		
		Integer TYPE_6 = 6;
	}

	/**
	 * 激活码历史记录 1:激活 2 出售
	 * 
	 * @author Administrator
	 *
	 */
	public interface ReleaseKey {
		Integer TYPE_1 = 1;

		Integer TYPE_2 = 2;
	}
}
