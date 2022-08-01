package com.testcomp.mvcpjt.util;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.security.crypto.bcrypt.BCrypt;


public class UserUtil {
	private String userid = "";
	private String password = "";
	
	public Map<String,Object> regUserIfNeeded(UserDTO dto) {
		boolean res = false;
		String msg = "";
		String encPW = BCrypt.hashpw(dto.getPw(), BCrypt.gensalt());
		dto.setPw(encPW);
		UserDAO uDAO = new UserDAO();
		if(uDAO.existUser(dto)) {
			msg = "Already Registered";
		} else {
			res = uDAO.insertUser(dto);
			if(!res) {
				msg = "Could Not Insert";
			}
		}
		Map<String,Object> resObj = new HashMap<String,Object>();
		resObj.put("result",res);
		if(!res) {
			resObj.put("msg",msg);
		}
		return resObj;
	}
	
	public boolean correctUser(UserDTO dto) {
		boolean result = false;
		UserDAO uDAO = new UserDAO();
		if(uDAO.existUser(dto)) {
			Map<String,Object> dbGet = uDAO.getUser(dto);
			result = BCrypt.checkpw(dto.getPw(), dbGet.get("pw").toString());
		}
		return result;
	}
	
	public UserDTO getUserFromStr(String body) {
		String userid = "";
		String password = "";
		try {
			JSONParser parser = new JSONParser();
			JSONObject jObj2 = (JSONObject) parser.parse(body);
			userid = (String) jObj2.get("userid");
			password = (String) jObj2.get("password");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new UserDTO(userid,password);
	}
}