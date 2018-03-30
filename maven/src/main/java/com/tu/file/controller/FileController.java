package com.tu.file.controller;

import java.io.File;
import java.io.IOException;

import javax.annotation.Resource;
import javax.crypto.SecretKey;
import javax.servlet.http.HttpSession;


import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.tu.file.service.IFileService;
import com.tu.file.utils.AESUtil;


@Controller
@RequestMapping("/file")
public class FileController {
	
	@Resource
	private IFileService userService;
	@RequestMapping("/upload")
	public ModelAndView uploadFile(@RequestParam MultipartFile[] uploadFile,HttpSession session){
		//获取上传文件名
		for (int i = 0; i < uploadFile.length; i++) {
			String filename = uploadFile[i].getOriginalFilename();
			//获取WebRoot下的images文件夹的绝对路径作为前半部分路径
			String leftPath = session.getServletContext().getRealPath("/upload");
			//将文件的前半部分路径与文件名拼接
			File file = new File(leftPath, filename);
			try {
				byte[] generateAESSecretKey = AESUtil.generateAESSecretKey();
				SecretKey key = AESUtil.restoreSecretKey(generateAESSecretKey);
				byte[] aesEcbEncode = AESUtil.AesEcbEncode(uploadFile[i].getBytes(), key);
				System.out.println(aesEcbEncode);
				uploadFile[i].transferTo(file);
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
		}
		ModelAndView mv = new ModelAndView();
		mv.setViewName("index");
		return mv;
	}
	@RequestMapping("/download")
	public ResponseEntity<byte[]> downFile() throws IOException{
		File file = new File("C:\\Users\\TU\\workspace\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\SSM_beta1\\images\\456.png");
		HttpHeaders headers = new HttpHeaders();
		String filename = new String("helloWorld.png".getBytes("UTF-8"),"iso-8859-1");
		//设置文件名
		headers.setContentDispositionFormData("attachment", filename);
		//以文件下载的形式来输出流
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),    
                headers, HttpStatus.CREATED);
	}
}
