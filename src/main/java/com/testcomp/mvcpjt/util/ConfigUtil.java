package com.testcomp.mvcpjt.util;


public class ConfigUtil {

	public static ConfigProp cProp = new ConfigProp();
	
	public static String JWTkey = cProp.JWTkey;
	public static String JWTissuer = cProp.JWTissuer;
	public static String JWTtype = cProp.JWTtype;
	public static String JWTalg = cProp.JWTalg;
	public static long JWTexp = cProp.JWTexp;
    public static long JWTacc = cProp.JWTacc;
    public static long JWTref = cProp.JWTref;
	
	public static int OTPmin = cProp.OTPmin;
    public static long OTPt0 = cProp.OTPt0;
    public static long OTPx = cProp.OTPx;
    public static int OTPdigit = cProp.OTPdigit;
    public static String OTPalg = cProp.OTPalg;
	
	public static String DBdriver = cProp.DBdriver;
	public static String DBurl = cProp.DBurl;
	public static String DBid = cProp.DBid;
	public static String DBpw = cProp.DBpw;

	public static String FILEpath = cProp.FILEpath;
	
	public static String AESkey = cProp.AESkey;
	public static String AESiv = cProp.AESiv;
	public static String AESalg = cProp.AESalg;
	public static String RSAprivfile = cProp.RSAprivfile;
	public static String RSApubfile = cProp.RSApubfile;
	public static String RSAalg = cProp.RSAalg;
}