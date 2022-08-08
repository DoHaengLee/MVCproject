package com.testcomp.mvcpjt.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.testcomp.mvcpjt.util.db.OtpDTO;
import com.testcomp.mvcpjt.util.db.UserDTO;



public class OtpUtil {
	/* ���� */
	private static UserUtil uUtil = new UserUtil();
    private static ApiUtil aUtil = new ApiUtil();
    // mvc.properties > ConfigProp > ConfigUtil
	private static int setMin = ConfigUtil.OTPmin;           // window:�յڷ� �� �о� ����� ���� (1 : ����-����-����)
    private static long T0 = ConfigUtil.OTPt0;               // default=0   ->  T�� unix �ð����� �������� �̴�� 0
    private static long X = ConfigUtil.OTPx;                 // default=30
    private static int digit = ConfigUtil.OTPdigit;          // default=6
    private static String alg = ConfigUtil.OTPalg;   		// default=SHA1

	/* ������ */
	// �⺻ - ����
    
    /* �Լ� */
    // seed ����
    private String genSeed() throws Exception {
        String result = "";
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789"; 
        int n = 32; // ���� either 20 or 32
        StringBuilder sb = new StringBuilder(n); 
        for (int i = 0; i < n; i++) { 
            int index = (int)(AlphaNumericString.length() * Math.random()); 
            sb.append(AlphaNumericString.charAt(index)); 
        } 
        String s = sb.toString(); // ���⼭ hexlify���� ����� otpkey �ϼ�
        String otpkey = "";
        for (int i = 0; i < s.length(); i++) {
            otpkey += String.format("%02X", (int) s.charAt(i));
        }
        result = otpkey.toLowerCase();
        return result;
    }
    // window�� �°� OTP ����
    private List<String> genOtp(String seed) throws Exception {
        List<String> result = new ArrayList<String>();

        long curMs = System.currentTimeMillis();
        long current = curMs / 1000L;   // ms ���� s��
        
        int length = setMin * 2 + 1;
        long timeArr[] = new long[length];
        int negMin = setMin * (-1);
        for (int i=0; i<length; i++){
            timeArr[i] = current + (negMin * 60);
            negMin++;
        }
        
        for(int i=0; i<timeArr.length; i++) {
            long T = (timeArr[i] - T0)/X;
            result.add(generateTOTP(seed, T, digit, alg));
         }
        
        return result;
    }
    // OTP ����
    private String generateTOTP(String sec32, long T, int digit, String alg) throws Exception {
        String result = "";

        String cntr = Long.toHexString(T).toUpperCase();
        while (cntr.length() < 16) cntr = "0" + cntr; // ������ 16�ڸ� �̻��̶� ��ǻ� ����� �ǳʶ�

        byte[] sec = hexStr2Bytes(sec32);
        byte[] msg = hexStr2Bytes(cntr);

        byte[] hash = hmac_sha(alg, sec, msg);

        int offset = hash[hash.length - 1] & 0xf;
        int binary =
            ((hash[offset] & 0x7f) << 24) | 
            ((hash[offset + 1] & 0xff) << 16) | 
            ((hash[offset + 2] & 0xff) << 8) | 
            (hash[offset + 3] & 0xff);
        
        String digPwrStr = "1";
        for(int i=0; i<digit; i++){
            digPwrStr += "0";
        }
        int digPwr = Integer.parseInt(digPwrStr);
        int otp = binary % digPwr;

        result = Integer.toString(otp);
        while (result.length() < digit) {
            result = "0" + result;
        }

        return result;
    }
    // hex���� byte��
    private byte[] hexStr2Bytes(String hex) throws Exception {
        byte[] bArray = new BigInteger("10" + hex,16).toByteArray();    
        byte[] ret = new byte[bArray.length - 1];
        for (int i = 0; i < ret.length ; i++) {
            ret[i] = bArray[i+1];
        }
        return ret;
    }
    // SHA256 ��ȣȭ
    private byte[] hmac_sha(String crypto, byte[] keyBytes, byte[] text) throws Exception {
        Mac hmac;
        hmac = Mac.getInstance(crypto);
        SecretKeySpec macKey = new SecretKeySpec(keyBytes, "RAW");
        hmac.init(macKey);
        return hmac.doFinal(text);
    }
    // OTP ���� �� USERS ���̺� ������Ʈ
    public List<String> genOtpNupdate(UserDTO uDTO) {
    	List<String> result = new ArrayList<String>();
		String seed = "";
		try {
			seed = genSeed();
            result = genOtp(seed);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(seed != null && !seed.equals("")) {
			uDTO.setSeed(seed);
	        uUtil.updateUser(uDTO);
		}
        return result;
    }
    // OTP Ȯ�� �� OTPTBL ���̺� ������Ʈ
    public Map<String,Object> checkOTPnUpdate(UserDTO uDTO, String typed) {
        Map<String,Object> result = new HashMap<String,Object>();
        boolean flagCorrect = false;
        String msg = "";
        
    	OtpDTO oDTO1 = new OtpDTO(uDTO.getId(), typed);
    	if(uUtil.usedOtp(oDTO1)) {
    		msg = "Used OTP";
    	} else {
    		String seed = uUtil.getUser(uDTO).get("seed").toString();
    		List<String> curList =  new ArrayList<>();
    		try {
    			curList = genOtp(seed);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		
    		if(curList!=null && curList.size()>0) {
    			OtpDTO oDTODb = new OtpDTO(uDTO.getId());
                List<Map<String,Object>> dbList = uUtil.getOtptbl(oDTODb);

        		List<OtpDTO> oDTOlist = new ArrayList<OtpDTO>();
        		int i = 1;
        		for(String curOtp : curList) {
        			OtpDTO oDTO = new OtpDTO(uDTO.getId(), curOtp);
        			oDTO.setNum(i);
        			oDTO.setUsedyn(false);
        			if(curOtp.equals(typed)) {
        				flagCorrect = true;
        				oDTO.setUsedyn(true);
        			} else {
        				if(!dbList.isEmpty()) {
        					for(Map<String,Object> dbMap : dbList) {
                    			if(dbMap.get("otp").toString().equals(curOtp)) {
                    				oDTO.setUsedyn((boolean)dbMap.get("usedyn"));
                    				
                    				break;
                    			}
                    		}
        				}
        			}
        			i++;
        			oDTOlist.add(oDTO);
        		}
        		if(flagCorrect) {
        			uUtil.updateOtpchk(oDTOlist);
        		} else {
        			msg = "Wrong OTP";
        		}
    		}
    	}
    	 result.put("result", flagCorrect);
         if(!flagCorrect) {
             result.put("msg", msg);
         }
        return result;
    }
    
    
    ////////// Controller ���� ��� //////////
    // ����� Ȯ�� �� OTP �߱�
    public Map<String,Object> otpgen(HttpServletRequest request) {
    	Map<String,Object> result = new HashMap<String,Object>();
	    boolean resBool = false;
		String msg = "";
	
		Map<String,Object> jic = aUtil.readBody(request);
		if((boolean)jic.get("result")) {
			String body = jic.get("body").toString();
			UserDTO uDTO = uUtil.getUserFromStr(body);
			if(uUtil.correctUser(uDTO)) {
				List<String> usrOtpList = genOtpNupdate(uDTO);
				resBool = true;
				result.put("otp_info", usrOtpList);
			} else {
				msg = "Wrong PW / Unregistered User";
			}
		} else {
			msg = jic.get("msg").toString();
		}
		
		result.put("result", resBool);
		if(!resBool) {
			result.put("msg", msg);
		}
		return result;
    }
    // ����� Ȯ�� �� OTP ����
    public Map<String,Object> otpchk(HttpServletRequest request) {
    	Map<String,Object> result = new HashMap<String,Object>();
    	boolean resBool = false;
		String msg = "";

		Map<String,Object> jic = aUtil.readBody(request);
		if((boolean)jic.get("result")) {
			String body = jic.get("body").toString();
			UserDTO uDTO = uUtil.getUserFromStr(body);
			if(uUtil.correctUser(uDTO)) {
				JSONParser parser = new JSONParser();
				try {
					JSONObject jObj2 = (JSONObject) parser.parse(body);
					String typed = (String) jObj2.get("otp");
					Map<String,Object> chkCorrectRes = checkOTPnUpdate(uDTO, typed);
					resBool = (boolean) chkCorrectRes.get("result");
					if(!resBool) {
						msg = chkCorrectRes.get("msg").toString();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				msg = "Wrong PW / Unregistered User";
			}
		} else {
			msg = jic.get("msg").toString();
		}
		
		result.put("result",resBool);
		if(!resBool) {
			result.put("msg",msg);
		}

    	return result;
    }
}
