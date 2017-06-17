package com.finance.p2p.biz.common.service;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.finance.p2p.biz.sys.utils.Const;
import com.framework.utils.UUIDGenerator;

@Service
public class UploadService {

	/**
	 * 处理文件上传
	 * 
	 * @param userId
	 * @param realPath
	 * @param files
	 * @return
	 * @throws IOException
	 */
	public String saveFile(String realPath, Long userId, MultipartFile file) throws IOException {
		String filePath = "";

		if (file != null) {
			if (!file.isEmpty()) {
				// 给上传人创建文件夹
				File userUploadDir = new File(realPath, userId.toString());
				if (!userUploadDir.exists()) {
					userUploadDir.mkdir();
				}
				// 这里不必处理IO流关闭的问题，因为FileUtils.copyInputStreamToFile()方法内部会自动把用到的IO流关掉
				String fileNameStr = file.getOriginalFilename();
				String[] str = fileNameStr.split("\\.");
				String fileExtName = "";
				if (0 < str.length) {
					fileExtName = str[str.length - 1];
				}
				// 创建服务器上传文件名（当前毫秒数+文件后缀）
				String fileName = UUIDGenerator.getUUID() + "." + fileExtName;
				FileUtils.copyInputStreamToFile(file.getInputStream(),
						new File(userUploadDir.getAbsolutePath(), fileName));
				filePath = Const.UploadKey.UPLOAD_PATH + "/" + userId + "/" + fileName;
			}
		}
		return filePath;
	}
}