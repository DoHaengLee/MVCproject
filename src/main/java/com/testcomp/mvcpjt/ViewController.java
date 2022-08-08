package com.testcomp.mvcpjt;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



@Controller
public class ViewController {

	// ���� ������
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home() {
		return "home";
	}
	
	// ����� ��� ������ (POST�� GET �� �� �����ϰ� �Ϸ��� �ƿ� method=RequestMethod.POST�� ����� ��)
	@RequestMapping(value = "/signup_view")
	public String signupView() {	
		return "user/signup_view";
	}
	
	/* JWT */
	// ID/PW�� ��ū ȹ�� ������
	@RequestMapping(value = "/tokenpw_view")
	public String tokenpwView() {	
		return "jwt/tokenpw_view";
	}	
	// �������� ��ū���� ��ū ��ȹ�� ������
	@RequestMapping(value = "/tokenref_view")
	public String tokenrefView() {	
		return "jwt/tokenref_view";
	}
	// �׼��� ��ū�� �׽�Ʈ�غ��� ������
	@RequestMapping(value = "/tokentest_view")
	public String tokentestView() {	
		return "jwt/tokentest_view";
	}
	
	/* OTP */
	// ID/PW�� OTP ȹ�� ������
	@RequestMapping(value = "/otpgen_view")
	public String otpgenView() {	
		return "otp/otpgen_view";
	}	
	// OTP ���� ������
	@RequestMapping(value = "/otpchk_view")
	public String otpchkView() {	
		return "otp/otpchk_view";
	}
	
	/* CRYPT */
	// AES256 ��ȣȭ ������
	@RequestMapping(value = "/aes256enc_view")
	public String aes256encView() {	
		return "crypt/aes256enc_view";
	}
	// AES256 ��ȣȭ ������
	@RequestMapping(value = "/aes256dec_view")
	public String aes256decView() {	
		return "crypt/aes256dec_view";
	}
}
