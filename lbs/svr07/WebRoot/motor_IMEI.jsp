<%@ page language="java" pageEncoding="gb2312"%>
<%
  String openid=request.getParameter("openId"); 
   String imei=request.getParameter("IMEI");
   String code=null; 
   String path = request.getContextPath();
   String basePath2 = request.getScheme()+"://"+request.getServerName()+path+"/";
   
   if (imei.indexOf("@#")!=0){
   	out.println("<script language='javaScript'> alert('�豸���벻��ȷ�������豸��û��������');</script>"); 
   	code=basePath2+"motor_relation.jsp";
   	}
   	else{
   		out.println("<script language='javaScript'> alert('�������豸�ţ���΢�ź��豸������');</script>"); 
   		code=basePath2+"openIdLogin.jsp?code="+request.getParameter("code")+"&state=123";
   	}
  
   	
String JSKsignature=rx.com.DogThread.f_sha1(code );
   	
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312">
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=0.5, maximum-scale=4.0, user-scalable=yes" />  
<meta name="MobileOptimized" content="240"/> 
<style type="text/css">
.fld3
{background-color:#EEEEFF; color:#880000;filter: alpha(opacity=50)}
</style> 
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript">

function M(id){
    return document.getElementById(id);
}

function scan_imei(){
	wx.scanQRCode({
    needResult: 1, // 1��ֱ�ӷ���ɨ������
    scanType: ["qrCode","barCode"], // ɨ��ά�뻹��һά�룬Ĭ�϶��߶���
    success: function (res) {
    var result = res.resultStr; 
    var i=0;
    i=result.indexOf(",");
    if (i>0)
    {i++;}
    else if (i<0)
    {i=0;}
    M("imei").value=result.substring(i);
}
});
}
</script>
<title>M</title>
</head>


<body>
<br/>
<form method="Post" name="PeriodicTable" action="motor_relation.jsp">
<input class="fld3" type="hidden"  name="openId" id="openid"  value="<%=openid %>"  >
<div  align="center" >
	ɨ���豸IMEI��<br/> <a href="javascript:scan_imei();"><img src="images/scan12.gif"/></a> <br/><br/>
	������IMEI����:<br/>
	<input class="fld3" type="text"  name="IMEI" id="imei"  maxlength="15"  >		
	<br/><br/>
	<input type="submit" name="Submit" value="�ύ" align="top"  >
</div>
</form>
 <script type="text/javascript">

 wx.config({
    debug: false, // 
    appId: '<%=rx.com.DogThread.myAppid%>', // 
    timestamp:<%=rx.com.DogThread.timestamp%>, // 
    nonceStr: '<%=rx.com.DogThread.nonceStr%>', // 
    signature: '<%=JSKsignature%>',// 
    jsApiList: ['scanQRCode'] //
});

wx.ready(function(){
 
});

</script>
</body>
</html>