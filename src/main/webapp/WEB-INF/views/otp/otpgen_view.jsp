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
    
    <title>[MVCpjt] ID/PW로 OTP 발급</title>
</head>



<body>
<P>ID/PW를 이용해 OTP를 발급받으세요.</P>
<P>이전, 현재, 다음 OTP가 나옵니다.</P>

<ul>
    <li>
        ID <input id="uid" name="uid" type="text" placeholder="ID" onkeypress="if(event.keyCode==13){chkWritten();}">
    </li>
    <li>
        PW <input id="pwd" name="pwd" type="password" placeholder="PW" onkeypress="if(event.keyCode==13){chkWritten();}">
    </li>
</ul>
<button type="button" onclick="javascript:chkWritten()">발급</button>
<div>
	<p id="resHere"></p>
</div>
<button type="button" onclick="location.href='/'">메인으로</button>
<button type="button" onclick="location.href='otpchk_view'">OTP 인증</button>

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
            url : "otpgen",
            contentType: 'application/json; charset=utf-8',
            data : JSON.stringify(obj),
            dataType: "json",
            cache : false,
            async: false,
            success : function(data) {
                var res = data.result;
                console.log("res : "+res);
                if(res){
                	document.getElementById("resHere").innerText = data.otp_info;
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