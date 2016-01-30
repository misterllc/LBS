<%@ page language="java" pageEncoding="gb2312"%>
<jsp:useBean id="mydata1"  class="rx.com.mydata_old" scope="application"/>
<%
  Integer cid=Integer.valueOf(request.getParameter("c")); 
  Integer operator=Integer.valueOf(request.getParameter("o")); 
  //Integer operid=Integer.valueOf(request.getParameter("operid")); 
 Integer ph=mydata1.get_cid_main(cid);
 if((ph-operator)==0) {ph=0; }else{return;}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=0.5, maximum-scale=4.0, user-scalable=yes" />  
<meta name="MobileOptimized" content="400"/>  
<title>关爱</title>
<style type="text/css">
body,html{bgcolor:#BABABA;width: 100%;height: 100%;overflow: hidden;margin:0;}
#div12 {float: center; margin-left:auto;width:100%; height:100%;} 
#div13 {float: center; margin-left:auto;width:10%; height:1%;} 
</style>
</head>
<script language='javaScript'> 
 function menu_go() {   

          with (document.getElementById("menu_schedule")) {   
           method = "post";          
           action = "wap.jsp";   
            submit();   
        }   
    }   

 function menu_phone() {   

          with (document.getElementById("menu_schedule")) {   
           method = "post"; 
           action = "phone_set_02.jsp";   
            submit();   
        }   
    }   

function menu_term() {   

          with (document.getElementById("menu_schedule")) {   
           method = "post"; 
           action = "term_set_02.jsp";   
            submit();   
        }   
    }   

function menu_clock() {   

          with (document.getElementById("menu_schedule")) {   
           method = "post"; 
           action = "term_alarm_02.jsp";   
            submit();   
        }   
    }   


</script>
<body >



	<div id="div12" align="center">
	<table bgcolor=#BABABA width=100%>
	<tr><td><br/></td></tr>
	<tr><td><br/></td></tr>
	<tr><td><br/></td></tr>	
	<tr><td>
		<center> <a href="javascript:menu_go();">最近位置</a></center>
		<br/>
		<br/>
		<center><a href="javascript:menu_phone();">号码设置 </a></center>
		<br/>
		<br/>
		<center><a href="javascript:menu_clock();">定时报时 </a></center>
		<br/>
		<br/>
		<center><a href="javascript:menu_term();">终端设置 </a></center>
	</td></tr>
	<tr><td><br/></td></tr>
	<tr><td><br/></td></tr>
	<tr><td><br/></td></tr>

	</table>
	</div>


<div id="div12" style="display: none">
		<form name="menu_schedule" id="menu_schedule" action="map.jsp" method="post" >		
			<input type="hidden"  name="o" id="operator" value="<%=operator %>" readonly>
			<input type="hidden"  name="operid" id="operid"  value="70" readonly>
			<input type="hidden"  name="c" id="cid" value="<%=cid%>" readonly>
			<input type="hidden"  name="control_phone" id="control_phone"  readonly>
			<input type="hidden"  name="sno" id="sno"	value="0" readonly>
			<input type="submit" name="Submit" value="确定" align="top" >	
		</form>
</div>
</body>


</html>