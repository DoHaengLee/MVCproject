package com.testcomp.mvcpjt.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.security.crypto.bcrypt.BCrypt;

import com.testcomp.mvcpjt.util.crypt.AES256util;
import com.testcomp.mvcpjt.util.db.UserDAO;
import com.testcomp.mvcpjt.util.db.UserDTO;
import com.testcomp.mvcpjt.util.db.OtpDAO;
import com.testcomp.mvcpjt.util.db.OtpDTO;



public class UserUtil {
	/* ���� */
    private static AES256util aes256Util = new AES256util();
    // mvc.properties > ConfigProp > ConfigUtil
    private static UserDAO uDAO = new UserDAO();
    private static OtpDAO oDAO = new OtpDAO();
    private static ApiUtil aUtil = new ApiUtil();
	
    /* ������ */
	// �⺻ - ����

	/* �Լ� */
    ////////// �⺻ ��������̺� ���� //////////
    // ����� ��Ͽ��� Ȯ��
    public boolean existUser(UserDTO dto) {
        boolean res = false;
        if(uDAO.getUser(dto) != null) {
        	res = true;
        }
        return res;
    }
    // �ʿ� �� ����� ���
	public Map<String,Object> regUserIfNeeded(UserDTO dto) {
		Map<String,Object> resObj = new HashMap<String,Object>();
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
		resObj.put("result",res);
		if(!res) {
			resObj.put("msg",msg);
		}
		return resObj;
	}
	// ID/PW Ȯ��
	public boolean correctUser(UserDTO dto) {
		boolean result = false;
		if(existUser(dto)) {
			Map<String,Object> dbGet = uDAO.getUser(dto);
			result = BCrypt.checkpw(dto.getPw(), dbGet.get("pw").toString());
		}
		return result;
	}
	// ����� ���� ��ȸ
	public Map<String,Object> getUser(UserDTO dto) {
		Map<String,Object> result = new HashMap<String,Object>();
		result = uDAO.getUser(dto);
		if(result.get("seed") != null) {
			String encseed = result.get("seed").toString();
			result.put("seed", aes256Util.decode(encseed));
		}
		return result;
	}
	// ����� ���� ������Ʈ
	public boolean updateUser(UserDTO dto) {
		boolean res = false;
		if(dto.getSeed() != null) {
			String encseed = aes256Util.encode(dto.getSeed());
			dto.setSeed(encseed);
		}
		res = uDAO.updateUser(dto);
        return res;
    }

	////////// OTP ���� //////////
	// OTPTBL �� �ش� ����� ���� ���翩�� Ȯ��
	public boolean existOtptbl(OtpDTO dto) {
		boolean res = false;
		if(oDAO.getOtptbl(dto) != null) {
        	res = true;
        }
        return res;
    }
	// OTPTBL �� �ش� ����� ���� ��ü ��ȸ
	public List<Map<String,Object>> getOtptbl(OtpDTO dto) {
        return oDAO.getOtptbl(dto);
    }
	// OTP ���� ���� Ȯ��
	public boolean usedOtp(OtpDTO dto) {
        return oDAO.usedOtp(dto);
    }
	// ����� seed ������Ʈ - OTP ���������� seed�� ����� �� �ϳ����� �ο��ϱ� ������ �⺻ ��������̺� update
	public boolean updateOtpgen(UserDTO uDTO) throws Exception {
		boolean res = false;
		if(uDTO.getSeed() != null) {
			String encseed = aes256Util.encode(uDTO.getSeed());
			uDTO.setSeed(encseed);
		}
		res = uDAO.updateUser(uDTO);
        return res;
    }
	// OTPTBL �� ��� ���� �� �ű� ���� ����
	public boolean updateOtpchk(List<OtpDTO> oDTOlist) {
		boolean res = false;
		OtpDTO dto = oDTOlist.get(0);
		oDAO.deleteOtptbl(dto);
		for(OtpDTO oDTO : oDTOlist) {
			res = oDAO.insertOtptbl(oDTO);
		}
        return res;
    }
	
	////////// Controller ���� ��� //////////
	// ��û �� body �о�ͼ� ����� ���
	public Map<String,Object> signup(HttpServletRequest request) {
    	Map<String,Object> result = new HashMap<String,Object>();
		Map<String,Object> jic = aUtil.readBody(request);
		if((boolean)jic.get("result")) {
			String body = jic.get("body").toString();
			UserDTO uDTO = getUserFromStr(body);
			result = regUserIfNeeded(uDTO);
		}
		if(result.isEmpty()) {
			result.put("result", false);
			result.put("msg", "Registration Failed");
		}
		return result;
    }
	// ��û �� body���� ID/PW ���� �� UserDTO �����Ͽ� ��ȯ
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
}