package com.testcomp.mvcpjt.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;



public class ApiUtil {
	
	public String sendReq(String urlStr, JSONObject jObj) {
		System.out.println("sendReq");
		String reqStr = "";
		try {
			URL url = new URL(urlStr);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        if(conn != null){
	            conn.setConnectTimeout(10000);
	            conn.setRequestMethod("POST");
	            conn.setDoInput(true);
	            conn.setDoOutput(true);
	            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8¡±");

	            if(jObj.get("sub")!=null) {
	            	conn.setRequestProperty("Authorization","Bearer " + jObj.get(jObj.get("sub").toString()).toString());
	            }

	            int resCode = conn.getResponseCode();
	            if(resCode == HttpURLConnection.HTTP_OK){
	            	OutputStream os = conn.getOutputStream();
	            	System.out.println("Before OutputStream");
	                os.write(jObj.get("body").toString().getBytes(StandardCharsets.UTF_8));
	                System.out.println("After OutputStream");
	                os.flush();
	            	
	                System.out.println("Before reader");
	                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	                System.out.println("After reader");
	                String line = null;
	                while(true){
	                    line = reader.readLine();
	                    if(line == null)
	                        break;
	                }
	                reqStr = line;
	                System.out.println("reqStr : "+reqStr);
	                reader.close();
	            }
	            conn.disconnect();
	        }
		} catch(Exception e) {
			e.printStackTrace();
		}
		return reqStr;
	}
	
	public Map<String,Object> chkHeaderToken(HttpServletRequest request, String sub) {
		Map<String,Object> resMap = new HashMap<String,Object>();
		boolean res = false;
		String msg = "";
		
		String token = request.getHeader("Authorization");
		if(token!=null) {
			String jwt = token.substring("Bearer ".length());
			JwtUtil jUtil = new JwtUtil();
			if(jUtil.validateToken(jwt)) {
				Map<String,Object> jMap = jUtil.getEach(jUtil.getClaims(jwt));
				if(jMap.get(sub.substring(0,3)+"exp") == null && !jMap.get("sub").toString().equals(sub)) {
					msg = "Incorrect Token! Use "+sub;
				} else {
					UserDTO uDTO = new UserDTO();
					uDTO.setId(jMap.get("userid").toString());
					res= jUtil.validateTokenExp(jwt, uDTO);
					if(!res) {
						msg = "Token Before Reissued";
					} else {
						resMap.put("userid", jMap.get("userid").toString());
					}
				}
			} else {
				msg = "Invalid Token";
			}
		} else {
			msg = "Token Missing";
		}
		
		resMap.put("result", res);
		if(!res) {
			resMap.put("msg", msg);
		}
		return resMap;
	}
	
	public Map<String,Object> readReq(HttpServletRequest request, String sub) {
		Map<String,Object> tokenMap = chkHeaderToken(request, sub);
		if((boolean)tokenMap.get("result")) {
			try {
				tokenMap = readBody(request);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return tokenMap;
	}
	
	public Map<String,Object> readBody(HttpServletRequest request) {
		Map<String,Object> tokenMap = new HashMap<String,Object>();
			try {
				String test = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
				//System.out.println("test : "+test);
				tokenMap.put("result", true);
				tokenMap.put("body", test);
			} catch (Exception e) {
				e.printStackTrace();
			}
		return tokenMap;	
	}
}