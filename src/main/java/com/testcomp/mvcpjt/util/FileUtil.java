package com.testcomp.mvcpjt.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;



public class FileUtil {
	
	public static String filepath = "C:/Users/Public/Documents/MVCpjt/";
	public static LocalDateTime now = LocalDateTime.now();     // Java 8부터 가능
	
	
	public void uploadFile(MultipartHttpServletRequest mhsr) throws Exception {
		List<MultipartFile> mf = mhsr.getFiles("uploadFile");
		if (mf.size() == 1 && mf.get(0).getOriginalFilename().equals("")) {
		} else {
			String curDateTime = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
			
			for (int i = 0; i < mf.size(); i++) {
				String originalfileName = mf.get(i).getOriginalFilename();
				String saveFileName = originalfileName + curDateTime;
				String savePath = filepath + saveFileName; 
				// 업로드 (savePath에 저장)
				mf.get(i).transferTo(new File(savePath));

				// DB에 업로드 된 파일 목록 저장해두기
				
				// 파일 파싱하기
				String conttype = mf.get(i).getContentType();
				switch(conttype) {
					case "" :
						break;
					default : 
						break;
				}
			}
		}
	}

	// 폴더 및 파일 생성
	public void createFile(String filename) throws Exception {
        File folder = new File(filepath);
        if (!folder.isDirectory()) {
        	folder.mkdirs();
        }
		File file = new File(filepath+filename);
		if(!file.exists()) {
			file.createNewFile();
		}
	}
	
	// String 파일 읽기
 	public String readFile(String filename) throws Exception {
 		BufferedReader reader = new BufferedReader(new FileReader(filename));
 		String readLine = "";
 		String str;
 		while ((str = reader.readLine()) != null) {
 			readLine+=str;
 		}
 		reader.close();
 		return readLine;
 	}
 	
 	// byte 파일 읽기
 	public byte[] readByteFile(String filename) throws Exception {
 		byte[] result;
 		File file = new File(filepath+filename);
 		return Files.readAllBytes(file.toPath());
 	}
}
