package com.testcomp.mvcpjt.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServletRequest;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.json.simple.JSONObject;

import com.testcomp.mvcpjt.util.db.UserDTO;



public class ApiUtil {
	
	// ��û ������ - �⺻ : POST, ���� ����
	public String sendReq(String urlStr, JSONObject jObj) {
		String reqStr = "";
		HttpURLConnection conn = null;
		try {
			URL url = new URL(urlStr);
	        conn = (HttpURLConnection) url.openConnection();
	        if(conn != null){
	            conn.setConnectTimeout(10000);
	            conn.setRequestMethod("POST");
	            conn.setDoOutput(true);
	            
	            // ����� ��û �� sub�� ���� ��� Authorization header�� Bearer�� �ֱ�
	            if(jObj.get("sub")!=null) {
	            	conn.setRequestProperty("Authorization","Bearer " + jObj.get(jObj.get("sub").toString()).toString());
	            }

	            int resCode = conn.getResponseCode();
	            if(resCode == HttpURLConnection.HTTP_OK){
	            	// ����� ��û �� body�� ���� ��� json ���·� �־��ֱ�
	            	if(jObj.get("body") != null) {
	            		conn.setDoInput(true);
	            		conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8��");
	            		OutputStream os = conn.getOutputStream();
		                os.write(jObj.get("body").toString().getBytes(StandardCharsets.UTF_8));
		                os.flush();
	            	}

	            	// ���� �о����
	                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	                String line = null;
	                while(true){
	                    line = reader.readLine();
	                    if(line == null)
	                        break;
	                }
	                reqStr = line;
	                reader.close();
	            }
	        }
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(conn != null){
				conn.disconnect();
			}
		}
		return reqStr;
	}
	
	// ��û �� header(��ū) Ȯ��
	public Map<String,Object> chkHeaderToken(HttpServletRequest request, String sub) {
		Map<String,Object> resMap = new HashMap<String,Object>();
		try {
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
		} catch (Exception e) {
			e.printStackTrace();
		}

		return resMap;
	}
	
	// ��û �� body Ȯ��
	public Map<String,Object> readBody(HttpServletRequest request) {
		Map<String,Object> tokenMap = new HashMap<String,Object>();
		try {
			String test = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
			tokenMap.put("result", true);
			tokenMap.put("body", test);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tokenMap;	
	}
	
	// ��û �� header Ȯ�� �� body �� ��ȯ
	public Map<String,Object> readReq(HttpServletRequest request, String sub) {
		Map<String,Object> tokenMap = new HashMap<String,Object>();
		tokenMap = chkHeaderToken(request, sub);
		if((boolean)tokenMap.get("result")) {
			tokenMap = readBody(request);
		}
		return tokenMap;
	}
}