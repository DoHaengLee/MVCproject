package com.testcomp.mvcpjt.util.db;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;

public class ConnDAO {
	// 변수
    private static String driver = "org.h2.Driver";
    private static String url = "jdbc:h2:~/test";					// 재기동 때마다 새롭게
    //private static String url = "jdbc:h2:tcp://localhost/~/test"; // 재기동 해도 데이터 남도록
    private static String id = "sa";
    private static String pw = "";
    
    public static Connection conn;
    public static PreparedStatement pstmt;
    public static ResultSet rs;
    
    
    // 생성자
    public ConnDAO(){}

    // DB 연결
    public static Connection getConn() {
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url,id,pw);
        }catch(Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    // 연결 해제
    public static void closeConn() {
        try {
            if(rs != null) rs.close();
            if(pstmt !=null) pstmt.close();
            if(conn !=null) conn.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
