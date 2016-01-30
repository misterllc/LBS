
<%@ page language="java" pageEncoding="gb2312"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
 <%
  request.setCharacterEncoding("gb2312");
 if (!rx.com.DogThread.dog_is_run) {rx.com.DogThread.DogStart();}//=
 	
  String CODE=request.getParameter("code");  
  String openid=rx.com.httpClientUtil.getOpenId(CODE);

  request.getRequestDispatcher("MotorIndex_2.jsp?openId="+openid).forward(request, response);//×ª·¢µ½i.jsp?c=9&o=98601
   %>
<html>
	<head>
    	<title>w</title>
  	</head>
 	
	<body>	
		<p>sorry,system busy.</p>
	</body>
</html>
