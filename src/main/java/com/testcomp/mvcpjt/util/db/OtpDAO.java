package com.testcomp.mvcpjt.util.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class OtpDAO {
	/* ���� */
	private static ConnDAO cDAO = new ConnDAO();
	
	/* ������ */
    public OtpDAO(){
    	createOtptblTable();
    }
    
    /* �Լ� */
    // OTPTBL ���̺� ���� - OTP ���� ���� �뵵
    private static void createOtptblTable() {
    	String sql = "CREATE TABLE IF NOT EXISTS otptbl(id VARCHAR(100) NOT NULL, numgen INT NOT NULL, otp VARCHAR(100) NOT NULL, usedyn BOOLEAN NOT NULL);";
        try {
        	cDAO.getConn();
        	ConnDAO.pstmt = ConnDAO.conn.prepareStatement(sql);
        	ConnDAO.pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	cDAO.closeConn();
        }
    }
    
    // OTPTBL �� �ش� ����� ���� ����
    public void deleteOtptbl(OtpDTO dto) {
        String sql = "DELETE FROM otptbl WHERE id=?";
        try {
            cDAO.getConn();
            ConnDAO.pstmt = ConnDAO.conn.prepareStatement(sql);
            ConnDAO.pstmt.setString(1, dto.getId());
            ConnDAO.pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	cDAO.closeConn();
        }
    }
    
    // OTPTBL ���
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

    // OTPTBL ��ȸ - ��ü
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
    
    // OTPTBL ��ȸ - �ش� OTP�� ����ߴ���
    public boolean usedOtp(OtpDTO dto) {
    	boolean res = false;
        String sql = "SELECT usedyn FROM otptbl WHERE id=? AND otp=?";
        try {
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