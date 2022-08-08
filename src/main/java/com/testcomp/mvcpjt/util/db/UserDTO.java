package com.testcomp.mvcpjt.util.db;

import java.sql.Timestamp;



public class UserDTO {
	/* 변수 */
    private String id;
    private String pw;
    private String seed;
    private Timestamp refexp;
    private Timestamp accexp;
    
    /* 생성자 */
    public UserDTO(){}
    public UserDTO(String id, String pw) {
        this.id = id;
        this.pw = pw;
    }
    
    /* getter */
    public String getId() {
        return id;
    }
    public String getPw() {
        return pw;
    }
    public Timestamp getRefexp() {
        return refexp;
    }
    public Timestamp getAccexp() {
        return accexp;
    }
    public String getSeed() {
        return seed;
    }
    
    /* setter */
    public void setId(String id) {
        this.id = id;
    }
    public void setPw(String pw) {
        this.pw = pw;
    }
    public void setRefexp(Timestamp refexp) {
        this.refexp = refexp;
    }
    public void setAccexp(Timestamp accexp) {
        this.accexp = accexp;
    }
    public void setSeed(String seed) {
        this.seed = seed;
    }
}