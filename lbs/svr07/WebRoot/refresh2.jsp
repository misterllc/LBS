<%@ page language="java" import="java.util.*" import="rx.com.GsmLBS" pageEncoding="utf-8"%>
<jsp:useBean id="mdata11"  class="rx.com.MotorData" scope="application"/>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String cid=request.getParameter("cid"); 
String  action=request.getParameter("action");
String currentNo=request.getParameter("currentNo");
int cno=Integer.valueOf( currentNo);
if (action.indexOf('-')==0){
	cno=cno - 1;
}else{
	cno=0;
}
String sql="select f_motor_web_loc2("+cid+","+cno+");";
String rpts=mdata11.dsQuery(sql);  

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
    <title>M</title> 
  </head>
  
<body onload="javascript:window.parent.f_center3(<%=gpsArray%>,<%=sno%>,'<%=timeStr %>');"  > 

<p>data</p>
</body>
</html>

