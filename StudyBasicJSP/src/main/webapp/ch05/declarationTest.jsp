<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>선언문 예제 - 변수선언</title>
</head>
<body>
	<h2>선언문 예제 - 변수선언</h2>
	<%
	String str1 = str2 + " Server Page";
	%>
	
	<%!
	String str2 = "Java";
	%>
	
	출력결과 : <%=str1 %>
</body>
</html>