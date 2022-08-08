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
    
    <title>[MVCpjt] AES256 복호화</title>
</head>

<body>
<P>복호화 할 문자를 입력하세요.</P>

<ul>
    <li>
        AES256으로 암호화 된 문자 <input id="enctext" name="enctext" type="text" placeholder="AES256 enc string" onkeypress="if(event.keyCode==13){chkWritten();}">
    </li>
</ul>
<button type="button" onclick="javascript:chkWritten()">복호화</button>
<div>
	<p id="resHere"></p>
</div>
<button type="button" onclick="location.href='/'">메인으로</button>
<button type="button" onclick="location.href='aes256enc_view'">암호화</button>

</body>
</html>



<script>
function chkWritten() {
    var enctext = document.getElementById("enctext").value;

    if(enctext == null || enctext == "") {
        document.getElementById("enctext").focus();
        document.getElementById("resHere").innerText = "복호화 할 문자를 입력해주세요";
    } else {
    	let obj={
    	   		"enctext" : enctext
    	   	};
        $.ajax({
            type : "POST",
            url : "aes256dec",
            contentType: 'application/json; charset=utf-8',
            data : JSON.stringify(obj),
            dataType: "json",
            cache : false,
            async: false,
            success : function(data) {
                var res = data.result;
                console.log("res : "+res);
                if(res){
                	document.getElementById("resHere").innerText = data.dec;
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