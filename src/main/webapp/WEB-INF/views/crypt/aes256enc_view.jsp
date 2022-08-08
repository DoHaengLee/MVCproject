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
    
    <title>[MVCpjt] AES256 암호화</title>
</head>

<body>
<P>암호화 할 문자를 입력하세요.</P>

<ul>
    <li>
        Plain Text <input id="plaintext" name="plaintext" type="text" placeholder="Some kind of string" onkeypress="if(event.keyCode==13){chkWritten();}">
    </li>
</ul>
<button type="button" onclick="javascript:chkWritten()">암호화</button>
<div>
	<p id="resHere"></p>
</div>
<button type="button" onclick="location.href='/'">메인으로</button>
<button type="button" onclick="location.href='aes256dec_view'">복호화</button>

</body>
</html>



<script>
function chkWritten() {
    var plaintext = document.getElementById("plaintext").value;

    if(plaintext == null || plaintext == "") {
        document.getElementById("plaintext").focus();
        document.getElementById("resHere").innerText = "암호화 할 문자를 입력해주세요";
    } else {
    	let obj={
    	   		"plaintext" : plaintext
    	   	};
        $.ajax({
            type : "POST",
            url : "aes256enc",
            contentType: 'application/json; charset=utf-8',
            data : JSON.stringify(obj),
            dataType: "json",
            cache : false,
            async: false,
            success : function(data) {
                var res = data.result;
                console.log("res : "+res);
                if(res){
                	document.getElementById("resHere").innerText = data.enc;
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