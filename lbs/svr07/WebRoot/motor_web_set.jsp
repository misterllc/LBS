<%@ page language="java" pageEncoding="gb2312"%>
<jsp:useBean id="mdata11"  class="rx.com.MotorData" scope="application"/>
<%
  String cid=request.getParameter("cid"); 
  String  action=request.getParameter("action"); 
  StringBuilder cmd= new StringBuilder(); 
   String sql=null;
   if (session.getAttribute("login")== null){
	session.invalidate();
	out.println("<script language='javaScript'> alert('请重新登陆！');</script>");
	return;
	} 
  
  if(action.indexOf('w')==0){
 	String teleA=request.getParameter("teleA");
 	String teleB=request.getParameter("teleB");
 	String[] alrm = request.getParameterValues("alrm");
 	String Sex=request.getParameter("Sex");
 	String userMain=request.getParameter("userMain");
 	
 	cmd.append("USR=");
 	
 	if (teleA!=null && teleA.length()>0){
 		cmd.append(teleA);
 	}else{
 		cmd.append('1');
 	}
 	cmd.append(',');
 
 	if (teleB!=null && teleB.length()>0){
 		cmd.append(teleB);
 	}else{
 		cmd.append('1');
 	}
  	cmd.append("&ALRM=");
  
 	int i;
 	if (alrm.length<1){
 		cmd.append("none");
 		} else {
 			cmd.append(alrm[0]);
 			for (i=1;i<alrm.length;i++){
 			cmd.append(',');
 			cmd.append(alrm[i]);
 			}
 		}
 	 cmd.append("&SENS=");
 	 cmd.append(Sex);
 sql="select f_motor_web_set(" + cid + ",'" + cmd.toString() + "');"; 
 mdata11.dsQuery(sql);
 cmd.append("&ID=1");
 out.println("<script language='javaScript'> alert('设置已保存 ');</script>"); 
}
  else 
{
	sql="select f_motor_web_read("+cid+");";
	cmd.append(mdata11.dsQuery(sql));
}
  
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<style type="text/css">
.fld1
{background-color:#DDDDFF; color:#110000;font-weight:bold}
</style> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312">
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=0.5, maximum-scale=4.0, user-scalable=yes" />  
<meta name="MobileOptimized" content="240"/> 
<title></title>
<script type="text/javascript">
var userMain='2';
function myParameter(){
	var paraStr="<%=cmd.toString()%>";
	var i=0,j=0;
	var b=false;
	
	i=paraStr.indexOf("USR=");
	if (i>=0){
		i=i+4;
		j=paraStr.indexOf(",", i);
		document.getElementById("teleA").value=paraStr.substring(i,j);
		i=j+1;
		j=paraStr.indexOf("&", i);
		document.getElementById("teleB").value=paraStr.substring(i,j);
	}
	i=paraStr.indexOf("ALRM=");
	if (i>=0){
		i=i+5;
		j=paraStr.indexOf("batLow", i);
		if (j>0){b=true;}else{b=false;}		 
		document.getElementById("batLow").checked=b;
		j=paraStr.indexOf("batOut", i);
		if (j>0){b=true;}else{b=false;}
		document.getElementById("batOut").checked=b;
		j=paraStr.indexOf("vibrate", i);
		if (j>0){b=true;}else{b=false;}
		document.getElementById("vibrate").checked=b;
		j=paraStr.indexOf("outRange", i);
		if (j>0){b=true;}else{b=false;}
		document.getElementById("outRange").checked=b;
	}
	i=paraStr.indexOf("SENS=");
	if (i>=0){
		i=i+5;
		if (paraStr.charAt(i)=='1'){
			document.getElementById("vib1").checked=true;
		}
		else if (paraStr.charAt(i)=='3'){
			document.getElementById("vib3").checked=true;
		} else {
			document.getElementById("vib2").checked=true;
		}
	}
	i=paraStr.indexOf("ID=");
	if (i>=0){
		i=i+3;
		if (paraStr.charAt(i)=='1'){
			userMain='1';
		}
	}
	
	document.getElementById("userMain").value=userMain;
	return 1 ;
};

function checkUser(){
	if (userMain=='1') {
		return true;
	}
	
	alert("非主控用户,不能修改设置!");
	return false;
}

</script>

</head>

<body style="background:black;color:white" onload ="javascript:myParameter();">

<form method="Post" name="PeriodicTable" action="motor_web_set.jsp">
<input type="hidden"  name="cid" id="cid"  value="<%=cid %>"  >
<input type="hidden"  name="action" id="action"  value="w" readonly>
<input type="hidden"  name="userMain" id="userMain"  value="" readonly>

<div  align="center" >参数设置</div>
<table width="252px"  border="1" align="center" title="" style="font-size:14px">
				<tr><td height=32px> 
				<label>主控号码</label>
				<input class="fld1" type="text"  name="teleA" id="teleA"  value="1"   maxlength="11"  >				
				</td></tr>	
							
				<tr><td height=32px >
				<label>第二号码</label>
				<input class="fld1" type="text"  name="teleB" id="teleB"  value="1"  maxlength="11"  >		
				</td></tr>
								
				<tr><td height=56px>
				<div  style="float:left;height:40px;line-height:24px;">报警短信</div>
				<div  style="float:right;line-height:26px;">
					电池低量<input type="checkbox" name="alrm" value="batLow" id="batLow" >
					电池断开<input type="checkbox" name="alrm" value="batOut" id="batOut" >	<br/>
					意外震动<input type="checkbox" name="alrm" value="vibrate" id="vibrate" >	
					车辆越界<input type="checkbox" name="alrm" value="outRange" id="outRange" checked>	
				</div>	
				</td></tr>
				
				<tr><td height=32px>
				<div  style="float:left">震动敏度</div>
				<div  style="float:right;">
				低<input type="radio" name="Sex" value="1"  id="vib1">	
				&nbsp;中<input type="radio" name="Sex" value="2" id="vib2" checked>
				&nbsp;高<input type="radio" name="Sex" value="3" id="vib3" >&nbsp;	
				</div>
				</td></tr>				
			</table>															
			<p class="fld1" align="center"> 
				<input type="submit" name="Submit" value="提交修改" align="top" onclick="javascript:return checkUser();" >
			</p>
</form>

</body>
</html>