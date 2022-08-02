package com.testcomp.mvcpjt.util.db;


public class OtpDTO {

	//변수
    private String id;
    private int num;
    private String otp;
    private boolean usedyn;
    
    //생성자
    public OtpDTO(){}
    public OtpDTO(String id) {
        this.id = id;
    }
    public OtpDTO(String id, String otp) {
        this.id = id;
        this.otp = otp;
    }
    
    //getter
    public String getId() {
        return id;
    }
    public int getNum() {
        return num;
    }
    public String getOtp() {
        return otp;
    }
    public boolean getUsedyn() {
        return usedyn;
    }
    
    //setter
    public void setId(String id) {
        this.id = id;
    }
    public void setNum(int num) {
        this.num = num;
    }
    public void setOtp(String otp) {
        this.otp = otp;
    }
    public void setUsedyn(boolean usedyn) {
        this.usedyn = usedyn;
    }
	
}
