<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>include 액션태그</title>
</head>
<body>
	<h2>include 액션태그</h2>
	
	<form action="includeTest.jsp" method="post">
		이름: <input type="text" name="name"><br>
		페이지이름: <input type="text" name="pageName" value="includedTest"><br>
		<input type="submit" value="입력완료">
	</form>
</body>
</html>