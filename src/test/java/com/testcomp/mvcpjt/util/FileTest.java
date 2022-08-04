package com.testcomp.mvcpjt.util;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.testcomp.mvcpjt.util.FileUtil;

public class FileTest {
	private static final Logger logger = LoggerFactory.getLogger(FileTest.class);
	private static FileUtil fUtil = new FileUtil();
	
	@Test
	public void testCsv() throws Exception {
		if(fUtil.readCSV("test.csv") != null) {
			List<List<String>> test = fUtil.readCSV("test.csv");
			for(List<String> ls : test) {
				for(String s : ls) {
					logger.info("s : "+s);
				}
				logger.info("line done");
			}
			
			fUtil.writeCSV("test_wr.csv",test);
		}
	}
}