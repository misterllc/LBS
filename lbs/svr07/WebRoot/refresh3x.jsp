<%@ page language="java" import="java.util.*" import="rx.com.GsmLBS" pageEncoding="utf-8"%>
<jsp:useBean id="mdataR3x"  class="rx.com.MotorData" scope="application"/>

<%
String cid=request.getParameter("cid"); 
String cno=request.getParameter("currentNo");
String rq=request.getParameter("regDate");
String  action=request.getParameter("action");
String sql=null;
if (action.charAt(0)=='1'){
	sql="select f_motor_cid_tm_loc("+cid+",'"+rq+"');";
} else {
	sql="select f_motor_cid_latest_loc("+cid+")";
}
String rpts=mdataR3x.dsQuery(sql);  

if (rpts.charAt(0)=='-'){
	return;  //用户不存在
}

int k, j=0;
String locs=null;
String timeStr=null;
String gps=null;
String sno=null;
String gpsArray=null;

k=rpts.indexOf("GPS=");
if (k<0){
	return;
}

k=k +4;
j=rpts.indexOf('&',k);

locs=rpts.substring(k, j); //GPS Data

k=rpts.indexOf('&');
sno=rpts.substring(0,k);//serial No

k=rpts.indexOf("TIME=");
if(k>=0){
	timeStr=rpts.substring(k+5,k+24);
}else{
	timeStr="0";
}
	
	gpsArray="[["+	locs.replaceAll(";", "],[") +"]]";
%>
	
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  <meta HTTP-EQUIV="pragma" CONTENT="no-cache">
  <meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
  <meta HTTP-EQUIV="expires" CONTENT="0">      
    <title>M</title> 
  </head>
  
<body onload="javascript:window.parent.f_center3(<%=gpsArray%>,<%=sno%>,'<%=timeStr %>');"  > 

<p>data</p>
</body>
</html>

