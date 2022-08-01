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
    
    <title>[MVCpjt] 리프레시토큰으로 토큰 재획득</title>
</head>

<!-- <style>
div
{
    -webkit-box-sizing: border-box;
    -moz-box-sizing: border-box;  
    box-sizing: border-box; 
} 
</style> -->

<body>
<P>RefreshToken을 입력하여 토큰을 재발급 받으세요.</P>
<P>재발급 할 경우, 이전에 받은 토큰은 더이상 사용이 불가합니다.</P>

<ul>
    <li>
        RefreshToken <input id="ref" name="ref" type="text" placeholder="eyJ0eXAi..." onkeypress="if(event.keyCode==13){chkWritten();}">
    </li>
</ul>
<button type="button" onclick="javascript:chkWritten()">재발급</button>
<div>
	<p id="resHere" style="white-space:normal"></p>
</div>
<button type="button" onclick="location.href='/'">메인으로</button>
<button type="button" onclick="location.href='tokentest_view'">토큰 테스트</button>

</body>
</html>



<script>
function chkWritten() {
    var ref = document.getElementById("ref").value;

    if(ref == null || ref == "") {
        document.getElementById("ref").focus();
        document.getElementById("resHere").innerText = "Refresh Token을 입력해주세요";
    } else {
    	let obj={
    	   		"jic" : "jiccc"
    	   	};
        $.ajax({
            type : "POST",
            url : "tokenref",
            headers: {
                "Authorization": "Bearer "+ref,
            },
            contentType: 'application/json; charset=utf-8',
            data : JSON.stringify(obj),
            dataType: "json",
            cache : false,
            async: false,
            success : function(data) {
                var res = data.result;
                if(res){
                	document.getElementById("resHere").innerText = JSON.stringify(data.token_info);
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