package com.testcomp.mvcpjt.util;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
    ////////// REST API�� body ���� �� UserDTO�� //////////
    public UserDTO getUserFromStr(String body) {
		UserDTO uDTO = new UserDTO();
		try {
			JSONParser parser = new JSONParser();
			JSONObject jObj2 = (JSONObject) parser.parse(body);
			String userid = (String) jObj2.get("userid");
			uDTO.setId(userid);
			if(jObj2.get("password") != null) {
				uDTO.setPw(jObj2.get("password").toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return uDTO;
	}
    
    ////////// �⺻ ��������̺� ���� //////////
    public boolean existUser(UserDTO dto) {
        boolean res = false;
        if(uDAO.getUser(dto) != null) {
        	res = true;
        }
        return res;
    }
    
	public Map<String,Object> regUserIfNeeded(UserDTO dto) {
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
	
	public boolean correctUser(UserDTO dto) {
		boolean result = false;
		if(existUser(dto)) {
			Map<String,Object> dbGet = uDAO.getUser(dto);
			result = BCrypt.checkpw(dto.getPw(), dbGet.get("pw").toString());
		}
		return result;
	}

	public Map<String,Object> getUser(UserDTO dto) {
		Map<String,Object> res = uDAO.getUser(dto);
		if(res.get("seed") != null) {
			String encseed = res.get("seed").toString();
			try {
				res.put("seed", aes256Cipher.AES256_Decode(encseed));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return res;
	}
	
	public boolean updateUser(UserDTO dto) {
		boolean res = false;
		if(dto.getSeed() != null) {
			try {
				String encseed = aes256Cipher.AES256_Encode(dto.getSeed());
				dto.setSeed(encseed);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		res = uDAO.updateUser(dto);
        return res;
    }
	
	
	
	////////// OTP ���� //////////
	public boolean existOtptbl(OtpDTO dto) {
		boolean res = false;
        if(oDAO.getOtptbl(dto) != null) {
        	res = true;
        }
        return res;
    }
	
	public List<Map<String,Object>> getOtptbl(OtpDTO dto) {
        return oDAO.getOtptbl(dto);
    }
	
	// OTP ���������� �õ�� ����� �� �ϳ����� �ο��ϱ� ������ �⺻ ��������̺� update
	public boolean updateOtpgen(UserDTO uDTO) {
		boolean res = false;
		if(uDTO.getSeed() != null) {
			try {
				String encseed = aes256Cipher.AES256_Encode(uDTO.getSeed());
				uDTO.setSeed(encseed);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		res = uDAO.updateUser(uDTO);
        return res;
    }
	
	public boolean updateOtpchk(List<OtpDTO> oDTOlist) {
		boolean res = false;
		
		OtpDTO dto = oDTOlist.get(0);
		
		oDAO.deleteOtptbl(dto);
		for(OtpDTO oDTO : oDTOlist) {
			res = oDAO.insertOtptbl(oDTO);
		}

        return res;
    }
	
	public boolean usedOtp(OtpDTO dto) {
        return oDAO.usedOtp(dto);
    }
}