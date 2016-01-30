<%@ page language="java" pageEncoding="gb2312"%>
<jsp:useBean id="mdata11"  class="rx.com.MotorData" scope="application"/>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
 <%
  request.setCharacterEncoding("gb2312");
  String cid=request.getParameter("cid");
  String cmdStr=request.getParameter("cmdStr");
   
  if (session.getAttribute("login")!= null){
	String o_cid = session.getAttribute("login").toString();
	if (o_cid.equals(cid) && cmdStr!=null){
	 	String sql="select f_motor_web_add_task2("+cid+",'CMD="+cmdStr+"','s');"; 
		if (cmdStr.length()<80){ mdata11.dsQuery(sql);}
		out.println("<script language='javaScript'> alert('指令正在发送中！');</script>");
	}
} else
	{
	session.invalidate();
	out.println("<script language='javaScript'> alert('请重新登陆！');</script>");
	return;
	} 


   %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<html>
<head>
    <base href="<%=basePath%>">
    <title>cmd</title>
</head>
  	
<body style="background-color:#6A8ABA;"  >	
	<br/><br/>
	 <table width="420px"  border="0" cellspacing="0" cellpadding="0" align="center"  title="" style="font-size: 16px">

	<tr> 
		<td width="100%"  colspan="2"> 
			<form name="rct_login" action="motor_webCMD.jsp" method="post"  >
			<input type="hidden" name="cid" id="cid" value="<%=cid %>" >
				<p>
				<label for="cmdStr" >请输入指令：</label>
				<input type="text" name="cmdStr" id="cmdStr"  maxlength="80" style="width:400px">
				</p>
				<br/>
				<p align="center"> 
				<input type="submit" name="Submit" value="提交" align="top" style="width:80px">
				</p>				
				</form>
		</td>
 </tr>
</table>
<br/><br/><br/>
<div>断油或者断电指令：stop <br/>
恢复正常通行指令：go <br/>
 取消微信关联指令：reset </div>
	<br/>		
</body>
</html>
