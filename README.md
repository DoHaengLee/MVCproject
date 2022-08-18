# Spring Boot Project

https://github.com/DoHaengLee/BootProject

---

# MVCproject - JWT, TOTP / RSA, AES256

**Spring (MVC2)**
- JDK 1.8
- Tomcat 8.5
- h2
- path : C:\Users\Public\Documents\MVCpjt

#
**USER**
1. signup_view : 사용자 등록 페이지 (필수)
- bcrypt로 암호화 하여 pw 저장

#
**JWT**
1. tokenpw_view : ID/PW로 토큰 발급 페이지
2. tokenref_view : Refresh Token으로 재발급 페이지
3. testapi_view : Access Token을 필요로 하는 테스트페이지
- jjwt를 활용해 토큰 발급 및 검증
- 재발급 이전 토큰 사용 방지 로직 추가
- 필요 토큰 자리에 다른 토큰 (예: 3번에서 refresh token) 사용 방지 로직 추가

#
**OTP**
1. otpgen_view : ID/PW로 OTP 발급 페이지
2. otpchk_view : ID/PW/OTP 인증 페이지
- AES256으로 암호화 하여 seed 저장
- OTP 재사용 방지 로직 추가

#
**CRYPT**
1. aes256enc_view : AES256으로 암호화 페이지
2. aes256ede_view : AES256으로 복호화 페이지
- RSA 암복호화 : 생성 후 암호화, 파일 저장 및 읽어온 후 복호화 단위테스트 완료

#
**FILE**

-- 단위테스트 내 test.csv 내용 시작 --

some,kind,of,test

1,Y,2019,63479283

-- 단위테스트 내 test.csv 내용 끝 --
