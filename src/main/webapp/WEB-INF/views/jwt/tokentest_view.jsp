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
    
    <title>[MVCpjt] Access Token 테스트</title>
</head>
<body>
<P>AccessToken을 입력하여 작동을 테스트하세요.</P>

<ul>
    <li>
        AccessToken <input id="acc" name="acc" type="text" placeholder="eyJ0eXAi..." onkeypress="if(event.keyCode==13){chkWritten();}">
    </li>
</ul>
<button type="button" onclick="javascript:chkWritten()">테스트</button>
<p id="resHere"></p>
<button type="button" onclick="location.href='/'">메인으로</button>

</body>
</html>



<script>
function chkWritten() {
    var acc = document.getElementById("acc").value;

    if(acc == null || acc == "") {
        document.getElementById("acc").focus();
        document.getElementById("resHere").innerText = "Access Token을 입력해주세요";
    } else {
    	let obj={
    			"jic" : "jiccc"
    	   	};
        $.ajax({
            type : "POST",
            url : "testapi",
            headers: {
                "Authorization": "Bearer "+acc,
            },
            contentType: 'application/json; charset=utf-8',
            data : JSON.stringify(obj),
            dataType: "json",
            cache : false,
            async: false,
            success : function(data) {
                var res = data.result;
                if(res){
                	document.getElementById("resHere").innerText = "SUCCESS!";
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