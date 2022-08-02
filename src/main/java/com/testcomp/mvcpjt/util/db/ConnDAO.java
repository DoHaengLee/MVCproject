package com.testcomp.mvcpjt.util.db;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;

public class ConnDAO {
	// ����
    private static String driver = "org.h2.Driver";
    private static String url = "jdbc:h2:~/test";					// ��⵿ ������ ���Ӱ�
    //private static String url = "jdbc:h2:tcp://localhost/~/test"; // ��⵿ �ص� ������ ������
    private static String id = "sa";
    private static String pw = "";
    
    public static Connection conn;
    public static PreparedStatement pstmt;
    public static ResultSet rs;
    
    
    // ������
    public ConnDAO(){}

    // DB ����
    public static Connection getConn() {
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url,id,pw);
        }catch(Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    // ���� ����
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
