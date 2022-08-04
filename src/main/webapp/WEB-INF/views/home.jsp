<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<%@ page session="false" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge, chrome=1">
    <meta name="format-detection" content="telephone=no" />
    <%-- <script type="text/javascript" src="${path}/resources/js/jquery-3.5.1.min.js"></script>
    <script type="text/javascript" src="/resources/js/common.js"></script> --%>
    <script src="./resources/js/jquery-3.5.1.min.js"></script>
    <script src="./resources/js/common.js"></script>
    
    <title>[MVCpjt] HOME</title>
</head>
<body>
<div>
	<h1>사용자</h1>
	<P>1. 사용자 등록 (필수)</P>
	<button type="button" onclick="location.href='signup_view'">등록</button>
</div>
<div>
	<h1>JWT</h1>
	<P>1. ID/PW로 토큰 발급</P>
	<button type="button" onclick="location.href='tokenpw_view'">발급</button>
	<P>2. Refresh Token으로 토큰 재발급</P>
	<button type="button" onclick="location.href='tokenref_view'">재발급</button>
	<P>3. Access Token 이용</P>
	<button type="button" onclick="location.href='tokentest_view'">토큰 테스트</button>
</div>
<div>
	<h1>OTP</h1>
	<P>1. ID/PW로 OTP 발급</P>
	<button type="button" onclick="location.href='otpgen_view'">발급</button>
	<P>2. ID/PW/OTP 인증</P>
	<button type="button" onclick="location.href='otpchk_view'">인증</button>
</div>

</body>
</html>