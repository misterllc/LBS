<%@ page language="java" import="java.util.*"  pageEncoding="utf-8"%>
<jsp:useBean id="mydata3"  class="rx.com.MotorData" scope="application"/>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String openid=request.getParameter("openId"); 
String imei=request.getParameter("IMEI");

String rpts=mydata3.save_motor_relation(openid,imei);  
if (rpts.charAt(0)=='1') {
	//
	request.getRequestDispatcher("MotorIndex_2.jsp").forward(request, response);
}else {

	request.getRequestDispatcher("motor_IMEI.jsp").forward(request, response);
}

%>
	
