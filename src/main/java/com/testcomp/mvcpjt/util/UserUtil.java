package com.testcomp.mvcpjt.util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import org.springframework.security.crypto.bcrypt.BCrypt;
import com.testcomp.mvcpjt.util.crypt.AES256Cipher;

import com.testcomp.mvcpjt.util.db.UserDAO;
import com.testcomp.mvcpjt.util.db.UserDTO;
import com.testcomp.mvcpjt.util.db.OtpDAO;
import com.testcomp.mvcpjt.util.db.OtpDTO;



public class UserUtil {
	private static String aeskey = "5616a7a50603a05c87ec82f53423fba7f50631235ea92454138d5ed987209091007a21d3d38f0b402602bc751b05e6da";
    private static AES256Cipher aes256Cipher = new AES256Cipher(aeskey);
    private static UserDAO uDAO = new UserDAO();
    private static OtpDAO oDAO = new OtpDAO();
    private static ApiUtil aUtil = new ApiUtil();
	
    ////////// REST API의 body 추출 후 UserDTO로 //////////
    public UserDTO getUserFromStr(String body) throws Exception {
		UserDTO uDTO = new UserDTO();
		JSONParser parser = new JSONParser();
		JSONObject jObj2 = (JSONObject) parser.parse(body);
		String userid = (String) jObj2.get("userid");
		uDTO.setId(userid);
		if(jObj2.get("password") != null) {
			uDTO.setPw(jObj2.get("password").toString());
		}
		return uDTO;
	}
    
    ////////// 기본 사용자테이블 관련 //////////
    public boolean existUser(UserDTO dto) throws Exception {
        boolean res = false;
        if(uDAO.getUser(dto) != null) {
        	res = true;
        }
        return res;
    }
    
	public Map<String,Object> regUserIfNeeded(UserDTO dto) throws Exception {
		boolean res = false;
		String msg = "";
		String encPW = BCrypt.hashpw(dto.getPw(), BCrypt.gensalt());
		dto.setPw(encPW);
		if(existUser(dto)) {
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
	
	public boolean correctUser(UserDTO dto) throws Exception {
		boolean result = false;
		if(existUser(dto)) {
			Map<String,Object> dbGet = uDAO.getUser(dto);
			result = BCrypt.checkpw(dto.getPw(), dbGet.get("pw").toString());
		}
		return result;
	}

	public Map<String,Object> getUser(UserDTO dto) throws Exception {
		Map<String,Object> res = uDAO.getUser(dto);
		if(res.get("seed") != null) {
			String encseed = res.get("seed").toString();
			res.put("seed", aes256Cipher.AES256_Decode(encseed));
		}
		return res;
	}
	
	public boolean updateUser(UserDTO dto) throws Exception {
		boolean res = false;
		if(dto.getSeed() != null) {
			String encseed = aes256Cipher.AES256_Encode(dto.getSeed());
			dto.setSeed(encseed);
		}
		res = uDAO.updateUser(dto);
        return res;
    }
	
	
	
	////////// OTP 관련 //////////
	public boolean existOtptbl(OtpDTO dto) throws Exception {
		boolean res = false;
        if(oDAO.getOtptbl(dto) != null) {
        	res = true;
        }
        return res;
    }
	
	public List<Map<String,Object>> getOtptbl(OtpDTO dto) {
        return oDAO.getOtptbl(dto);
    }
	
	// OTP 관련이지만 시드는 사용자 당 하나씩만 부여하기 때문에 기본 사용자테이블 update
	public boolean updateOtpgen(UserDTO uDTO) throws Exception {
		boolean res = false;
		if(uDTO.getSeed() != null) {
			String encseed = aes256Cipher.AES256_Encode(uDTO.getSeed());
			uDTO.setSeed(encseed);
		}
		res = uDAO.updateUser(uDTO);
        return res;
    }
	
	public boolean updateOtpchk(List<OtpDTO> oDTOlist) throws Exception {
		boolean res = false;
		
		OtpDTO dto = oDTOlist.get(0);
		
		oDAO.deleteOtptbl(dto);
		for(OtpDTO oDTO : oDTOlist) {
			res = oDAO.insertOtptbl(oDTO);
		}

        return res;
    }
	
	public boolean usedOtp(OtpDTO dto) throws Exception {
        return oDAO.usedOtp(dto);
    }
	
	
	 ////////// Controller의 역할을 최대한 줄이도록 //////////
	
	 // 등록
	public Map<String,Object> signup(HttpServletRequest request) throws Exception {
    	Map<String,Object> result = new HashMap<String,Object>();

		Map<String,Object> jic = aUtil.readBody(request);
		if((boolean)jic.get("result")) {
			String body = jic.get("body").toString();
			JSONParser parser = new JSONParser();
			JSONObject jObj2 = (JSONObject) parser.parse(body);
			String userid = (String) jObj2.get("userid");
			String password = (String) jObj2.get("password");
			
			UserDTO uDTO = new UserDTO(userid,password);
			result = regUserIfNeeded(uDTO);
		}
		
		if(result==null) {
			result.put("result", false);
			result.put("msg", "Registration Failed");
		}
		return result;
    }
}