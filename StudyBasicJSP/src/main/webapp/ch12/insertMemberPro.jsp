<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%@ page import="ch12.member.LogonDBBean"%>

<%request.setCharacterEncoding("utf-8");%>

<jsp:useBean id="member" class="ch12.member.LogonDataBean">
	<jsp:setProperty name="member" property="*"/>
</jsp:useBean>

<%
member.setReg_date(new Timestamp(System.currentTimeMillis()));
LogonDBBean logon = LogonDBBean.getInstance();
logon.insertMember(member);
%>

<jsp:getProperty property="id" name="member"/>님 회원가입을 축하합니다.