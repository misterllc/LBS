
<%@ page language="java" pageEncoding="gb2312"%>
<jsp:useBean id="mdata11"  class="rx.com.MotorData" scope="application"/>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
 <%
  request.setCharacterEncoding("gb2312");
  String username=request.getParameter("username");
  String password=request.getParameter("password"); 
  String sql="select f_motor_web_login('"+username+"','"+password+"');"; 
  String cid=mdata11.dsQuery(sql);
  
if(cid.charAt(0)=='0')
	{
	session.invalidate();
	out.println("<script language='javaScript'> alert('µÇÂ¼Ê§°Ü');</script>");
	response.setHeader("refresh","1;url=user.html");
	return;
	} 
	session.setAttribute("login",cid);

   %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<html>
<head>
    <base href="<%=basePath%>">
    <title>welcome</title>
</head>
  	
<body style="background-color:#BABABA;"  onload="javascript:window.parent.a(<%=cid%>,0,'<%=username %>');">	
	<br/>
	<div style="font-size: 16px"><%=username %></div>	
	<br/>		
</body>
</html>
