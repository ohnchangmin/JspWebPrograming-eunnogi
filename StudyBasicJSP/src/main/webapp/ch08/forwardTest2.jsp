<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%request.setCharacterEncoding("utf-8"); %>

<%
String id = "kingdora";
String hobby = "만화보기";
%>

포워딩하는 페이지 forwardTest2.jsp입니다.<br>

<jsp:forward page="forwardToTest.jsp">
	<jsp:param value="<%=id %>" name="id"/>
	<jsp:param value="<%=hobby %>" name="hobby"/>
</jsp:forward>