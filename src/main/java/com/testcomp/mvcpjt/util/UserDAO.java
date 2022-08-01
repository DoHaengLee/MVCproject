package com.testcomp.mvcpjt.util;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.HashMap;
import java.util.Map;



public class UserDAO {
    // ����
    private static String driver = "org.h2.Driver";
    private static String url = "jdbc:h2:~/test";
    //private static String url = "jdbc:h2:tcp://localhost/~/test";
    private static String id = "sa";
    private static String pw = "";
    
    private static Connection conn;
    private static PreparedStatement pstmt;
    private static ResultSet rs;
    
    
    
    // ������
    public UserDAO(){}
    
    
    
    // DB ����
    private static Connection getConn() {
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url,id,pw);
        }catch(Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    // ���� ����
    private static void closeConn() {
        try {
            if(rs != null) rs.close();
            if(pstmt !=null) pstmt.close();
            if(conn !=null) conn.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    // ���̺� ���� - static�ε��� �ٷ� ������ �� �ǳ�...existUser �ʿ� �ֱ��
    private static void createTableJWT() {
    	String sql = "CREATE TABLE IF NOT EXISTS users(id VARCHAR(100) NOT NULL PRIMARY KEY, pw VARCHAR(500) NOT NULL, refexp TIMESTAMP, accexp TIMESTAMP);";
        try {
            getConn();
            pstmt = conn.prepareStatement(sql);
            int succ = pstmt.executeUpdate();
            //if(succ==1) System.out.println("CREATE DONE");
            //else System.out.println("TABLE EXISTS");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConn();
        }
    }
    
    // ����� ���
    public boolean insertUser(UserDTO dto) {
        boolean res = false;
        String sql = "INSERT INTO users VALUES(?,?,?,?)";
        try {
            getConn();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dto.getId());
            pstmt.setString(2, dto.getPw());
            pstmt.setTimestamp(3, dto.getRefexp());
            pstmt.setTimestamp(4, dto.getAccexp());
            int succ = pstmt.executeUpdate();
            if(succ==1) res = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConn();
        }
        return res;
    }

    // ����� ���� ���� Ȯ��
    public boolean existUser(UserDTO dto) {
        boolean res = false;
        String sql = "SELECT pw FROM users WHERE id=?";
        try {
        	createTableJWT();
            getConn();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dto.getId());
            rs = pstmt.executeQuery();
            if(rs.next()){
                res = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConn();
        }
        return res;
    }
    
    // ����� ���� ��ȸ
    public Map<String,Object> getUser(UserDTO dto) {
        Map<String,Object> res = new HashMap<>();
        String sql = "SELECT pw, refexp, accexp FROM users WHERE id=?";
        try {
            getConn();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dto.getId());
            rs = pstmt.executeQuery();
            if(rs.next()){
                res.put("pw", rs.getString("pw"));
                if(rs.getTimestamp("refexp") != null) {
                	res.put("refexp", rs.getTimestamp("refexp"));
                }
                if(rs.getTimestamp("accexp") != null) {
                	res.put("accexp", rs.getTimestamp("accexp"));
                }
            } else {
                res = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConn();
        }
        return res;
    }
    
    // ����� ������Ʈ - refresh��ū, access��ū
    public boolean updateUser(UserDTO dto) {
        boolean res = false;
        String sql = "UPDATE users SET refexp=?, accexp=? WHERE id=?";
        try {
            getConn();
            pstmt = conn.prepareStatement(sql);
            pstmt.setTimestamp(1, dto.getRefexp());
            pstmt.setTimestamp(2, dto.getAccexp());
            pstmt.setString(3, dto.getId());
            int succ = pstmt.executeUpdate();
            if(succ==1) res = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConn();
        }
        return res;
    }
}