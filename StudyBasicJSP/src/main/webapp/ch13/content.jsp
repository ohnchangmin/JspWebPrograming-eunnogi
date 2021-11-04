<%@page import="ch13.board.BoardDataBean"%>
<%@page import="ch13.board.BoardDBBean"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="color.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판</title>
<link href="style.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="<%=bodyback_c%>">
<%
	int num = Integer.parseInt(request.getParameter("num"));
	String pageNum = request.getParameter("pageNum");
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	try{
		BoardDBBean dbPro = BoardDBBean.getInstance();
		BoardDataBean article = dbPro.getArticle(num);
		
		int ref = article.getRef();
		int re_step = article.getRe_step();
		int re_level = article.getRe_level();
%>
<p>글내용 보기</p>

<form>
	<table>
		<tr height="30">
			<td align="center" width="125" bgcolor="<%=value_c%>">글번호</td>
			<td align="center" width="125" align="center"><%=article.getNum()%></td>
			<td align="center" width="125" bgcolor="<%=value_c%>">조회수</td>
			<td align="center" width="125" align="center"><%=article.getReadcount()%></td>
		</tr>
		<tr height="30">
			<td align="center" width="125" bgcolor="<%=value_c%>">작성자</td>
			<td align="center" width="125" align="center"><%=article.getWriter()%></td>
			<td align="center" width="125" bgcolor="<%=value_c%>">작성일</td>
			<td align="center" width="125" align="center"><%=sdf.format(article.getReg_date())%></td>
		</tr>
		<tr height="30">
			<td align="center" width="125" bgcolor="<%=value_c%>">글제목</td>
			<td align="center" width="375" align="center" colspan="3"><%=article.getSubject()%></td>
		</tr>
		<tr>
			<td align="center" width="125" bgcolor="<%=value_c%>">글내용</td>
			<td align="left" width="375" colspan="3"><pre><%=article.getContent()%></pre></td>
		</tr>
		<tr height="30">
			<td colspan="4" align="right" bgcolor="<%=value_c%>">
				<input type="button" value="글수정" onclick="document.location.href='updateForm.jsp?num=<%=article.getNum()%>&pageNum=<%=pageNum%>'">
				&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="button" value="글삭제" onclick="document.location.href='deleteForm.jsp?num=<%=article.getNum()%>&pageNum=<%=pageNum%>'">
				&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="button" value="댓글쓰기" onclick="document.location.href='writeForm.jsp?num=<%=num%>&ref=<%=ref%>&re_step=<%=re_step%>&re_level=<%=re_level%>'">
				&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="button" value="글목록" onclick="document.location.href='list.jsp?pageNum=<%=pageNum%>'">
			</td>
		</tr>
	</table>
	<%}catch(Exception e){}%>
</form>
</body>
</html>