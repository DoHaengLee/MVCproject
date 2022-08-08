package com.testcomp.mvcpjt.util;

import java.io.FileInputStream;
import java.util.Properties;
import org.springframework.util.ResourceUtils;



public class ConfigProp {
	
	public ConfigProp() {
		getProp();
	}
	
	public static String JWTkey = "";
	public static String JWTissuer = "";
	public static String JWTtype = "";
	public static String JWTalg = "";
	public static long JWTexp = 0;
    public static long JWTacc = 0;
    public static long JWTref = 0;
	
	public static int OTPmin = 0;
    public static long OTPt0 = 0;
    public static long OTPx = 0;
    public static int OTPdigit = 0;
    public static String OTPalg = "";
	
	public static String DBdriver = "";
	public static String DBurl = "";
	public static String DBid = "";
	public static String DBpw = "";
	
	public static String FILEpath = "";
	
	public static String AESkey = "";
	public static String AESiv = "";
	public static String AESalg = "";
	public static String RSAprivfile = "";
	public static String RSApubfile = "";
	public static String RSAalg = "";
	
	public static void getProp() {
		try{
			Properties props = new Properties();
			props.load(new FileInputStream(ResourceUtils.getFile("classpath:config/mvc.properties")));
			
			JWTkey = props.get("jwt.key").toString();
			JWTissuer = props.get("jwt.issuer").toString();
			JWTtype = props.get("jwt.type").toString();
			JWTalg = props.get("jwt.alg").toString();
			JWTexp = Long.parseLong(props.get("jwt.exp.min").toString()) * 1000 * 60;
			JWTacc = Long.parseLong(props.get("jwt.acc.min").toString()) * 1000 * 60;
			JWTref = Long.parseLong(props.get("jwt.ref.min").toString()) * 1000 * 60;
			
			OTPmin = Integer.parseInt(props.get("otp.window").toString());
			OTPt0 = Long.parseLong(props.get("otp.t0").toString());
			OTPx = Long.parseLong(props.get("otp.x").toString());
			OTPdigit = Integer.parseInt(props.get("otp.digit").toString());
			OTPalg = props.get("otp.alg").toString();
			
			DBdriver = props.get("db.driver").toString();
			DBurl = props.get("db.url").toString();
			DBid = props.get("db.id").toString();
			DBpw = props.get("db.pw").toString();
			
			FILEpath = props.get("file.path").toString();
			
			AESkey = props.get("crypt.aes256.secret").toString();
			AESiv = AESkey.substring(0,16);
			AESalg = props.get("crypt.aes256.alg").toString();
			RSAprivfile = props.get("crypt.rsa.filename").toString() + "_private.key";
			RSApubfile = props.get("crypt.rsa.filename").toString() + "_public.key";
			RSAalg = props.get("crypt.rsa.alg").toString();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}