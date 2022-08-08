package com.testcomp.mvcpjt.util.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.testcomp.mvcpjt.util.ConfigUtil;



public class ConnDAO {
	/* ���� */
	public static Connection conn;
    public static PreparedStatement pstmt;
    public static ResultSet rs;
	// mvc.properties > ConfigProp > ConfigUtil
    private static String driver = ConfigUtil.DBdriver;
    private static String url = ConfigUtil.DBurl;
    private static String id = ConfigUtil.DBid;
    private static String pw = ConfigUtil.DBpw;

    /* ������ */
    public ConnDAO(){}

    /* �Լ� */
    // DB ����
    public Connection getConn() {
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url,id,pw);
        }catch(Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    // ���� ����
    public void closeConn() {
        try {
            if(rs != null) rs.close();
            if(pstmt !=null) pstmt.close();
            if(conn !=null) conn.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
