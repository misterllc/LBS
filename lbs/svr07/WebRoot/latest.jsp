<%@ page language="java" import="java.util.* " pageEncoding="utf-8"%>
<jsp:useBean id="mydata1"  class="rx.com.mydata_old" scope="application"/>
<%
Integer  cid=Integer.valueOf(request.getParameter("cid"));
Integer  sno=0; //Integer.valueOf(request.getParameter("sno"));  
String loc_s2=mydata1.get_card_location(cid,sno);
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="refresh" content="50"> 
<title>地图</title>
</head>

<body onload="javascript:window.parent.latest('<%=loc_s2 %>');">
<div id="mapdata"> <form id="get"><input id="cid",name="cid" type="text"/> </form></div>
</body>
</html>
