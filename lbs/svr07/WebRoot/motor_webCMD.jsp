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
		out.println("<script language='javaScript'> alert('ָ�����ڷ����У�');</script>");
	}
} else
	{
	session.invalidate();
	out.println("<script language='javaScript'> alert('�����µ�½��');</script>");
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
				<label for="cmdStr" >������ָ�</label>
				<input type="text" name="cmdStr" id="cmdStr"  maxlength="80" style="width:400px">
				</p>
				<br/>
				<p align="center"> 
				<input type="submit" name="Submit" value="�ύ" align="top" style="width:80px">
				</p>				
				</form>
		</td>
 </tr>
</table>
<br/><br/><br/>
<div>���ͻ��߶ϵ�ָ�stop <br/>
�ָ�����ͨ��ָ�go <br/>
 ȡ��΢�Ź���ָ�reset </div>
	<br/>		
</body>
</html>
