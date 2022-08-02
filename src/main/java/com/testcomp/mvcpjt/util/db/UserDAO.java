package com.testcomp.mvcpjt.util.db;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;





public class UserDAO {
	// ����
		private static ConnDAO cDAO = new ConnDAO();

    // ������
    public UserDAO(){}
    

    // ���̺� ���� - static�ε��� �ٷ� ������ �� �ǳ�...getUser �ʿ� �ֱ��
    private static void createTblUsers() {
    	String sql = "CREATE TABLE IF NOT EXISTS users(id VARCHAR(100) NOT NULL PRIMARY KEY, pw VARCHAR(500) NOT NULL, refexp TIMESTAMP, accexp TIMESTAMP, seed VARCHAR(500));";
        try {
            cDAO.getConn();
            ConnDAO.pstmt = ConnDAO.conn.prepareStatement(sql);
            int succ = ConnDAO.pstmt.executeUpdate();
            //if(succ==1) System.out.println("CREATE DONE");
            //else System.out.println("TABLE EXISTS");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cDAO.closeConn();
        }
    }
    
    // ����� ���
    public boolean insertUser(UserDTO dto) {
        boolean res = false;
        String sql = "INSERT INTO users VALUES(?,?,?,?,?)";
        try {
            cDAO.getConn();
            ConnDAO.pstmt = ConnDAO.conn.prepareStatement(sql);
            ConnDAO.pstmt.setString(1, dto.getId());
            ConnDAO.pstmt.setString(2, dto.getPw());
            ConnDAO.pstmt.setTimestamp(3, dto.getRefexp());
            ConnDAO.pstmt.setTimestamp(4, dto.getAccexp());
            ConnDAO.pstmt.setString(5, dto.getSeed());
            int succ = ConnDAO.pstmt.executeUpdate();
            if(succ==1) res = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cDAO.closeConn();
        }
        return res;
    }

    // ����� ���� ��ȸ
    public Map<String,Object> getUser(UserDTO dto) {
        Map<String,Object> res = new HashMap<>();
        String sql = "SELECT pw, refexp, accexp, seed FROM users WHERE id=?";
        try {
        	createTblUsers();
            cDAO.getConn();
            ConnDAO.pstmt = ConnDAO.conn.prepareStatement(sql);
            ConnDAO.pstmt.setString(1, dto.getId());
            ConnDAO.rs = ConnDAO.pstmt.executeQuery();
            if(ConnDAO.rs.next()){
                res.put("pw", ConnDAO.rs.getString("pw"));
                if(ConnDAO.rs.getTimestamp("refexp") != null) {
                	res.put("refexp", ConnDAO.rs.getTimestamp("refexp"));
                }
                if(ConnDAO.rs.getTimestamp("accexp") != null) {
                	res.put("accexp", ConnDAO.rs.getTimestamp("accexp"));
                }
                if(ConnDAO.rs.getString("seed") != null) {
                	res.put("seed", ConnDAO.rs.getString("seed"));
                }
            } else {
                res = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cDAO.closeConn();
        }
        return res;
    }
    
    // ����� ������Ʈ - ��ū(refresh + access) / OTP�õ�
    public boolean updateUser(UserDTO dto) {
        boolean res = false;
        
        String sql = "UPDATE users SET ";
        int i = 0;
        if(dto.getRefexp() != null) {
        	sql += "refexp=?, accexp=?";
        	i = 2;
        }
        if(dto.getSeed() != null) {
        	if(i==2) {
        		sql += ", seed=?";
        	} else {
        		sql += "seed=?";
        	}
        	i++;
        }
        sql += " WHERE id=?";

        try {
            cDAO.getConn();
            ConnDAO.pstmt = ConnDAO.conn.prepareStatement(sql);
            if(dto.getRefexp() != null) {
            	ConnDAO.pstmt.setTimestamp(1, dto.getRefexp());
            	ConnDAO.pstmt.setTimestamp(2, dto.getAccexp());
            }
            if (dto.getSeed() != null) {
            	if(i==1) {
            		ConnDAO.pstmt.setString(1, dto.getSeed());
            	} else {
            		ConnDAO.pstmt.setString(3, dto.getSeed());
            	}
            }
            ConnDAO.pstmt.setString(i+1, dto.getId());
            int succ = ConnDAO.pstmt.executeUpdate();
            if(succ==1) res = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cDAO.closeConn();
        }
        return res;
    }
}