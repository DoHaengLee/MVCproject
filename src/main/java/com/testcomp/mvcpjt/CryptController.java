package com.testcomp.mvcpjt;

import javax.servlet.http.HttpServletRequest;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.testcomp.mvcpjt.util.crypt.AES256util;



@RestController
public class CryptController {
	
	private static final Logger logger = LoggerFactory.getLogger(CryptController.class);
	private static AES256util aes256Util = new AES256util();

	
	// AES256으로 암호화 API
	@RequestMapping(value = "/aes256enc", method = RequestMethod.POST)
	public JSONObject aes256enc(HttpServletRequest request) throws Exception {
		logger.info("aes256enc BEGEIN");
		JSONObject jObj =  new JSONObject(aes256Util.aes256enc(request));
		logger.info("aes256enc Res : "+jObj);
		return jObj;
	}
	// AES256으로 복호화 API
		@RequestMapping(value = "/aes256dec", method = RequestMethod.POST)
		public JSONObject aes256dec(HttpServletRequest request) throws Exception {
			logger.info("aes256dec BEGEIN");
			JSONObject jObj =  new JSONObject(aes256Util.aes256dec(request));
			logger.info("aes256dec Res : "+jObj);
			return jObj;
		}
}
