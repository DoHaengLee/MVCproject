package com.testcomp.mvcpjt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewController {
	
	private static final Logger logger = LoggerFactory.getLogger(ViewController.class);
	
	// ����� ��� ������ (POST�� GET �� �� �����ϰ� �Ϸ��� �ƿ� method=RequestMethod.POST�� ����� ��)
	@RequestMapping(value = "/signup_view")
	public String signupView() {	
		return "jwt/signup_view";
	}
	
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
}
