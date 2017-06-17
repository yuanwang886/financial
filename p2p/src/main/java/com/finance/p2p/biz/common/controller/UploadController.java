package com.finance.p2p.biz.common.controller;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.finance.p2p.biz.business.service.BuyService;
import com.finance.p2p.biz.common.bean.BaseData;
import com.finance.p2p.biz.common.service.UploadService;
import com.finance.p2p.biz.sys.utils.Const;
import com.finance.p2p.biz.sys.utils.Const.FileKey;
import com.finance.p2p.biz.sys.utils.Const.USERKey;
import com.finance.p2p.biz.sys.utils.Pubfun;
import com.finance.p2p.entity.User;
import com.framework.exception.BusinessException;

/**
 * 文件上传
 * 
 * @author Administrator
 *
 */
@Controller
public class UploadController {

	@Autowired
	private UploadService uploadService;

	@Autowired
	private BuyService buyService;

	/**
	 * 文件上传的入口
	 * 
	 * @param jsonparam
	 * @param files
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("upload")
	@ResponseBody
	public BaseData upload(MultipartFile file, HttpSession session, Long id, Integer type, String password) throws IOException {

		String realPath = session.getServletContext().getRealPath(Const.UploadKey.UPLOAD_PATH);
		
		
		User user = (User) session.getAttribute(USERKey.USER_SESSION);

		// 校验用户用户密码正确性
		password = Pubfun.encryptMD5(user.getPhone(), password);
		if (!StringUtils.equals(password, user.getPayPassword())) {
			throw new BusinessException("支付密码不正确~");
		}
		// 文件保存
		String filePath = uploadService.saveFile(realPath, user.getUserId(), file);

		// 这个理貌似要去处理业务了吧
		if (type.equals(FileKey.FILE_1001)) {
			buyService.updateRecordState(id, filePath);
		}
		return new BaseData("url", Const.UploadKey.SERVER_HTTP_PATH + filePath);
	}
}
