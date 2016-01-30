<%@ page language="java" pageEncoding="gb2312"%>
<%@ page language="java" import="java.util.*" import="rx.com.GsmLBS" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
//GsmLBS gsmLBS=new GsmLBS();
//String result=gsmLBS.getLocFromGSM("accesstype=1&imei=352315052834888&mmac=bc:f6:85:da:73:43,-55,lgy");
//String locs=mydata1.get_motor_location(2,0);
//String gps=null;
//String lineA=null;
//int i, j=0;

//String result=gsmLBS.changeCoordate("locations="+locs);
//i=locs.indexOf("GPS=", i);
//if (i>0) {
//j=result.indexOf(';');
//	if (j>0) {
//		gps=result.substring(0,j);
//	}
//}
//lineA='['+result+']';
//String lineB=lineA.replaceAll(";", "],[");
%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>status</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body style="background:black;" >

<div style="width:320px; margin:0 auto;color:white;">
<br/>
更新时间: 2015-11-1 12:30<br/>
电池电量：100%<br/>
行使里程：120公里<br/>
车辆状态：保护中<br/>
其他信息：<br/>

</div>

  </body>
</html>
