package com.testcomp.mvcpjt.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import io.jsonwebtoken.lang.Arrays;



public class FileUtil {
	
	public static String filepath = "C:/Users/Public/Documents/MVCpjt/";
	public static LocalDateTime now = LocalDateTime.now();     // Java 8부터 가능
	
	/*
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
	*/
	public boolean uploadFile(MultipartFile thefile) throws Exception {
		boolean result = false;
        try{
            File folder = new File(filepath);
            if (!folder.isDirectory()) folder.mkdirs();
            File destination = new File(filepath + thefile.getOriginalFilename());
            thefile.transferTo(destination);
            result = true;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return result;
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
 	
 	// csv 파일 읽기
 	public List<List<String>> readCSV(String filename) throws Exception {
 		List<List<String>> result = new ArrayList<List<String>>();
 		try {
 			File csv = new File(filepath+filename);
 	        if (!csv.exists()) {
 	        	result = null;
 	        } else {
 	        	BufferedReader br = null;
 	            String line = "";
 	            br = new BufferedReader(new FileReader(csv));
 	            // 한 줄 씩 읽어, 쉼표로 나눠주기
 	            while ((line = br.readLine()) != null) {
 	                List<String> aLine = new ArrayList<String>();
 	                String[] lineArr = line.split(",");
 	                for(String s : lineArr) {
 	                	aLine.add(s);
 	                }
 	                result.add(aLine);
 	            }
 	            if (br != null) { 
 	                br.close();
 	            }
 	        }
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
        
        return result;
    }
 	// csv 파일 쓰기
 	public void writeCSV(String filename, List<List<String>> dataList) throws Exception {
        File csv = new File(filepath+filename);
        createFile(filename);
        BufferedWriter bw = null;
        bw = new BufferedWriter(new FileWriter(csv));
        
        for(List<String> ls : dataList) {
			for(String s : ls) {
				bw.write(s);
				if(!s.equals(ls.get(ls.size()-1))) {
					bw.write(",");
				}
			}
			if(!ls.equals(dataList.get(dataList.size()-1))) {
				bw.newLine();
			}
		}
        if (bw != null) {
            bw.flush();
            bw.close();
        }
    }
}
