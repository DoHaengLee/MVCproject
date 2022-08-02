package com.testcomp.mvcpjt.util;

import java.lang.reflect.UndeclaredThrowableException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.testcomp.mvcpjt.util.db.OtpDTO;
import com.testcomp.mvcpjt.util.db.UserDTO;
import com.testcomp.mvcpjt.util.UserUtil;



public class OtpUtil {
	
	//����
	private static int setMin = 1;              // �յڷ� �� �о� ����� ���� (1 : ����-����-����)
    private static long T0 = 0;                 // default=0   ->  T�� unix �ð����� �������� �̴�� 0
    private static long X = 60;                 // default=30
    private static int digit = 8;               // default=6
    private static String alg = "HmacSHA256";   // default=SHA1
    
    private static UserUtil uUtil = new UserUtil();

    
    //������
    public OtpUtil(){}
    
    //�޼���
    public String genSeed(){
        String result = "";
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public ArrayList<String> genOtp(String seed){
        ArrayList<String> result = new ArrayList<String>();

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
    
    private String generateTOTP(String sec32, long T, int digit, String alg){
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

    private byte[] hexStr2Bytes(String hex){
        byte[] bArray = new BigInteger("10" + hex,16).toByteArray();    
        byte[] ret = new byte[bArray.length - 1];
        for (int i = 0; i < ret.length ; i++) {
            ret[i] = bArray[i+1];
        }
        return ret;
    }

    private byte[] hmac_sha(String crypto, byte[] keyBytes, byte[] text){
        try {
            Mac hmac;
            hmac = Mac.getInstance(crypto);
            SecretKeySpec macKey = new SecretKeySpec(keyBytes, "RAW");
            hmac.init(macKey);
            return hmac.doFinal(text);
        } catch (GeneralSecurityException gse) {
            throw new UndeclaredThrowableException(gse);
        }
    }

    public ArrayList<String> genOtpNupdate(UserDTO uDTO){
        String seed = genSeed();
        ArrayList<String> result = genOtp(seed);
        try {
            uDTO.setSeed(seed);
            uUtil.updateUser(uDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    
    public Map<String,Object> checkOTPnUpdate(UserDTO uDTO, String typed) {
        Map<String,Object> result = new HashMap<String,Object>();
        boolean flagCorrect = false;
        String msg = "";
        
        try {
        	OtpDTO oDTO1 = new OtpDTO(uDTO.getId(), typed);
        	if(uUtil.usedOtp(oDTO1)) {
        		msg = "Used OTP";
        	} else {
        		String seed = uUtil.getUser(uDTO).get("seed").toString();
        		ArrayList<String> curList =  genOtp(seed);

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
        } catch (Exception e) {
            e.printStackTrace();
        }

        result.put("result", flagCorrect);
        if(!flagCorrect) {
            result.put("msg", msg);
        }
        return result;
    }
}
