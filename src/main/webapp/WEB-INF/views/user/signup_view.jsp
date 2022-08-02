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
    <script src="./resources/js/jquery-3.5.1.min.js"></script>
    <script src="./resources/js/common.js"></script>
    
    <title>[MVCpjt] 신규 등록</title>
</head>
<body>
<P>AccessToken and/or OTP 를 획득하려면 ID/PW를 등록해주세요.</P>
<P>한 번 등록 된 정보는 바꿀 수 없습니다.</P>

<ul>
    <li>
        ID <input id="uid" name="uid" type="text" placeholder="ID" onkeypress="if(event.keyCode==13){chkWritten();}">
    </li>
    <li>
        PW <input id="pwd" name="pwd" type="password" placeholder="PW" onkeypress="if(event.keyCode==13){chkWritten();}">
    </li>
</ul>
<button type="button" onclick="javascript:chkWritten()">등록</button>
<p id="resHere"></p>
<button type="button" onclick="location.href='/'">메인으로</button>
<button type="button" onclick="location.href='tokenpw_view'">토큰 발급</button>
<button type="button" onclick="location.href='otpgen_view'">OTP 발급</button>

</body>
</html>



<script>
function chkWritten() {
    var uid = document.getElementById("uid").value;
    var pwd = document.getElementById("pwd").value;

    if(uid == null || uid == "") {
        document.getElementById("uid").focus();
        document.getElementById("resHere").innerText = "ID를 입력해주세요";
    } else if (pwd == null || pwd == ""){
        document.getElementById("pwd").focus();
        document.getElementById("resHere").innerText = "PW를 입력해주세요";
    } else {
    	let obj={
    	   		"userid" : uid,
    	   		"password" : pwd
    	   	};
        $.ajax({
            type : "POST",
            url : "signup",
            contentType: 'application/json; charset=utf-8',
            data : JSON.stringify(obj),
            dataType: "json",
            cache : false,
            async: false,
            success : function(data) {
                var res = data.result;
                if(res){
                	document.getElementById("resHere").innerText = "Registration Success";
                } else {
                	document.getElementById("resHere").innerText = data.msg;
                }
            },
            error : function(xhr, status, error) {
                document.getElementById("resHere").innerText = error;
            }
        });
    }
}
</script>