package com.testcomp.mvcpjt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewController {
	
	private static final Logger logger = LoggerFactory.getLogger(ViewController.class);
	
	// 사용자 등록 페이지 (POST랑 GET 둘 다 가능하게 하려면 아예 method=RequestMethod.POST를 지우면 됨)
	@RequestMapping(value = "/signup_view")
	public String signupView() {	
		return "jwt/signup_view";
	}
	
	// ID/PW로 토큰 획득 페이지
	@RequestMapping(value = "/tokenpw_view")
	public String tokenpwView() {	
		return "jwt/tokenpw_view";
	}
	
	// 리프레시 토큰으로 토큰 재획득 페이지
	@RequestMapping(value = "/tokenref_view")
	public String tokenrefView() {	
		return "jwt/tokenref_view";
	}

	// 액세스 토큰을 테스트해보는 페이지
	@RequestMapping(value = "/tokentest_view")
	public String tokentestView() {	
		return "jwt/tokentest_view";
	}
}
