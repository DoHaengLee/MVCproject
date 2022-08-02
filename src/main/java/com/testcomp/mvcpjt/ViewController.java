package com.testcomp.mvcpjt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ViewController {
	
	private static final Logger logger = LoggerFactory.getLogger(ViewController.class);
	
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
}
