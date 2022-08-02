# MVCproject

**Spring (MVC2) - JWT, TOTP**
- JDK 1.8
- Tomcat 8.5
- h2

---
**USER**
1. signup_view : 사용자 등록 페이지 (필수)
- bcrypt로 pw 암호화 하여 저장

#
**JWT**
1. tokenpw_ivew : ID/PW로 토큰 발급 페이지
2. tokenref_view : Refresh Token으로 재발급 페이지
3. testapi_view : Access Token을 필요로 하는 테스트페이지
- jjwt를 활용해 토큰 발급 및 검증
- 재발급 이전 토큰 사용 방지 로직 추가
- 필요 토큰 자리에 다른 토큰 (예: 3번에서 refresh token) 사용 방지 로직 추가

#
**OTP**
1. otpgen_view : ID/PW로 OTP 발급 페이지
2. otpchk_view : ID/PW/OTP 인증 페이지
- OTP 재사용 방지 로직 추가
