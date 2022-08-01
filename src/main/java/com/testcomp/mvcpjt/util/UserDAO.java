package com.testcomp.mvcpjt.util;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.HashMap;
import java.util.Map;



public class UserDAO {
    // 변수
    private static String driver = "org.h2.Driver";
    private static String url = "jdbc:h2:~/test";
    //private static String url = "jdbc:h2:tcp://localhost/~/test";
    private static String id = "sa";
    private static String pw = "";
    
    private static Connection conn;
    private static PreparedStatement pstmt;
    private static ResultSet rs;
    
    
    
    // 생성자
    public UserDAO(){}
    
    
    
    // DB 연결
    private static Connection getConn() {
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url,id,pw);
        }catch(Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    // 연결 해제
    private static void closeConn() {
        try {
            if(rs != null) rs.close();
            if(pstmt !=null) pstmt.close();
            if(conn !=null) conn.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    // 테이블 생성 - static인데도 바로 생성이 안 되네...existUser 쪽에 넣기로
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
    
    // 사용자 등록
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

    // 사용자 존재 여부 확인
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
    
    // 사용자 정보 조회
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
    
    // 사용자 업데이트 - refresh토큰, access토큰
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