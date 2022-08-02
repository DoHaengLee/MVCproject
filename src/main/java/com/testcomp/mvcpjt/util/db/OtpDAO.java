package com.testcomp.mvcpjt.util.db;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class OtpDAO {
	// 변수
	private static ConnDAO cDAO = new ConnDAO();
	
    // 생성자
    public OtpDAO(){}
    

    // 테이블 생성 - static인데도 바로 생성이 안 되네...usedOtp 쪽에 넣기로
    private static void createOtptblTable() {
    	String sql = "CREATE TABLE IF NOT EXISTS otptbl(id VARCHAR(100) NOT NULL, numgen INT NOT NULL, otp VARCHAR(100) NOT NULL, usedyn BOOLEAN NOT NULL);";
        try {
        	cDAO.getConn();
        	ConnDAO.pstmt = ConnDAO.conn.prepareStatement(sql);
            int succ = ConnDAO.pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	cDAO.closeConn();
        }
    }
    
    // OTPtbl 삭제
    //public boolean deleteOtptbl(OtpDTO dto) {
    public void deleteOtptbl(OtpDTO dto) {
        boolean res = false;
        String sql = "DELETE FROM otptbl WHERE id=?";
        try {
            cDAO.getConn();
            ConnDAO.pstmt = ConnDAO.conn.prepareStatement(sql);
            ConnDAO.pstmt.setString(1, dto.getId());
            int succ = ConnDAO.pstmt.executeUpdate();
            //if(succ==1) res = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	cDAO.closeConn();
        }
        //return res;
    }
    
    // OTPtbl 등록
    public boolean insertOtptbl(OtpDTO dto) {
        boolean res = false;
        String sql = "INSERT INTO otptbl VALUES (?,?,?,?)";
        try {
            cDAO.getConn();
            ConnDAO.pstmt = ConnDAO.conn.prepareStatement(sql);
            ConnDAO.pstmt.setString(1, dto.getId());
            ConnDAO.pstmt.setInt(2, dto.getNum());
            ConnDAO.pstmt.setString(3, dto.getOtp());
            ConnDAO.pstmt.setBoolean(4, dto.getUsedyn());
            int succ = ConnDAO.pstmt.executeUpdate();
            if(succ==1) res = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	cDAO.closeConn();
        }
        return res;
    }

    // OTPtbl 정보 조회
    public List<Map<String,Object>> getOtptbl(OtpDTO dto) {
    	List<Map<String,Object>> res = new ArrayList<Map<String,Object>>();
    	
        String sql = "SELECT numgen, otp, usedyn FROM otptbl WHERE id=?";
        if(dto.getOtp() != null) {
        	sql += " and otp=?";
        }
        try {
            cDAO.getConn();
            ConnDAO.pstmt = ConnDAO.conn.prepareStatement(sql);
            ConnDAO.pstmt.setString(1, dto.getId());
            if(dto.getOtp() != null) {
            	ConnDAO.pstmt.setString(2, dto.getOtp());
            }
            ConnDAO.rs = ConnDAO.pstmt.executeQuery();
            while(ConnDAO.rs.next()) {
            	Map<String,Object> map = new HashMap<>();
            	map.put("num", Integer.parseInt(ConnDAO.rs.getString("numgen")));
            	map.put("otp", ConnDAO.rs.getString("otp"));
            	map.put("usedyn", ConnDAO.rs.getBoolean("usedyn"));
            	res.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	cDAO.closeConn();
        }
        return res;
    }
    
    // OTPtbl 정보 조회 - 해당 OTP를 사용했는지
    public boolean usedOtp(OtpDTO dto) {
    	boolean res = false;
        String sql = "SELECT usedyn FROM otptbl WHERE id=? AND otp=?";
        try {
        	createOtptblTable();
            cDAO.getConn();
            ConnDAO.pstmt = ConnDAO.conn.prepareStatement(sql);
            ConnDAO.pstmt.setString(1, dto.getId());
            ConnDAO.pstmt.setString(2, dto.getOtp());
            ConnDAO.rs = ConnDAO.pstmt.executeQuery();
            if(ConnDAO.rs.next()) {
            	if(ConnDAO.rs.getString("usedyn") != null) {
            		res = ConnDAO.rs.getBoolean("usedyn");
            	}
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	cDAO.closeConn();
        }
        return res;
    }
}