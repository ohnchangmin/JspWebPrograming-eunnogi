<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String id = "";
try{
	id = (String)session.getAttribute("id");
	if(id==null||id.equals(""))
		response.sendRedirect("sessionLoginForm.jsp");
	else{
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>세션을 사용한 간단한 회원인증</title>
</head>
<body>
	<b><%=id%>님이 로그인 하셨습니다.</b>
	<form action="sessionLogout.jsp" action="post">
		<input type="submit" value="로그아웃">
	</form>
</body>
</html>
<%		
	}
}catch(Exception e){
	e.printStackTrace();
}
%>