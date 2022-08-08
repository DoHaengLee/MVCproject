package com.testcomp.mvcpjt;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



@Controller
public class ViewController {

	// 메인 페이지
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home() {
		return "home";
	}
	
	// 사용자 등록 페이지 (POST랑 GET 둘 다 가능하게 하려면 아예 method=RequestMethod.POST를 지우면 됨)
	@RequestMapping(value = "/signup_view")
	public String signupView() {	
		return "user/signup_view";
	}
	
	/* JWT */
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
	
	/* OTP */
	// ID/PW로 OTP 획득 페이지
	@RequestMapping(value = "/otpgen_view")
	public String otpgenView() {	
		return "otp/otpgen_view";
	}	
	// OTP 인증 페이지
	@RequestMapping(value = "/otpchk_view")
	public String otpchkView() {	
		return "otp/otpchk_view";
	}
	
	/* CRYPT */
	// AES256 암호화 페이지
	@RequestMapping(value = "/aes256enc_view")
	public String aes256encView() {	
		return "crypt/aes256enc_view";
	}
	// AES256 복호화 페이지
	@RequestMapping(value = "/aes256dec_view")
	public String aes256decView() {	
		return "crypt/aes256dec_view";
	}
}
