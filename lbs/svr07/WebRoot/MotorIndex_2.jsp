
<%@ page language="java" import="java.util.*" import="rx.com.GsmLBS" pageEncoding="utf-8"%>
<jsp:useBean id="mydata_2"  class="rx.com.MotorData" scope="application"/>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String basePath2 = request.getScheme()+"://"+request.getServerName()+path+"/";

String openId=request.getParameter("openId");

String sql=null;
String cid=null;
sql="select f_motor_oid_latest_loc('"+openId+"')";
String rpts=mydata_2.dsQuery(sql); 

if (rpts.charAt(0)=='-'){//用户不存在
 	request.getRequestDispatcher("motor_IMEI.jsp?IMEI=@#").forward(request, response);
	return;  
}

int i, j=0;
String locs=null;
String timeStr=null;

String result=null;
String sno=null;
String gpsArray=null;

i=rpts.indexOf("GPS=");
if (i<0){
	 out.println("<script language='javaScript'> alert('没有查到记录，请稍后再试！');</script>");
	return;  //还没有定位记录
}else {

i=i +4;
j=rpts.indexOf('&',i);
locs=rpts.substring(i, j); //GPS Data

i=rpts.indexOf('&');
sno=rpts.substring(0,i); //serial No

i=rpts.indexOf("TIME=");
if(i>=0){
	timeStr=rpts.substring(i+5,i+24);
}else{
	timeStr="0";
}
i=rpts.indexOf("CID=");
if(i>=0){
	cid=rpts.substring(i+4);
}
gpsArray="[["+	locs.replaceAll(";", "],[") +"]]";
}

String imei=request.getParameter("IMEI");
String code=null;
if (imei==null){
	code=basePath2+"openIdLogin.jsp?code="+request.getParameter("code")+"&state=123";
	} else {
	code=basePath2+"motor_relation.jsp";
	}
String JSKsignature=rx.com.DogThread.f_sha1(code );

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="initial-scale=1.0, user-scalable=no, width=device-width">
  <meta HTTP-EQUIV="pragma" CONTENT="no-cache">
  <meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
  <meta HTTP-EQUIV="expires" CONTENT="0">
  <title>M</title>
  <link rel="stylesheet" href="http://cache.amap.com/lbs/static/main.css" />
  <script src="http://webapi.amap.com/maps?v=1.3&key=46e31d461e2541d1f1591323b0bce194"></script>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
  <style type="text/css">
	body{
			margin:0;
			height:100%;
			width:100%;
			position:absolute;
		}
	#mapContainer{
			position: absolute;
			top:0;
			left: 0;
			right:0;
			bottom:0;
		}
	#setPage{
			position: absolute;
			top:0;
			left: 0;
			right:0;
			bottom:0;
		}
	#menu_tip{
			height:40px;
			background-color:#fff;
			padding-left:5px;
			padding-right:5px;
			position:absolute;
			bottom:8px;
			font-size:12px;
			right:6px;
			border-radius:3px;
			border:1px solid #ccc;
			line-height: 20px;
		}		
	#menu_tip input[type='button']{
			margin:5px;
			background-color: #0D9BF2;
			height:30px;
			text-align:center;
			line-height:30px;
			color:#fff;
			font-size:12px;
			border-radius:3px;
			outline: none;
			border:0;
		}
	.myPos{
			display: block;
			height:38px;
			width:23px;
			text-align:center;
			background-image:url('http://webapi.amap.com/images/0.png');
			background-repeat:no-repeat;
			color:red;
			font-size:14px;
			line-height:24px;
		}		
	</style>
	
<script language='javaScript'> 
var is_login=false;
var posCount=0;
var posXY=[];
var timeStrA=[];
var markersA=[];
var marked=0;
var tmp_sno;
var tmp_tmstr;
var shareURL;
var converting=0;
var maxCount=0;
var lastNo=0;
var period=33000;

 var createMarker1 = function(posM,count,timeStamp) {
        var div = document.createElement('div');
        div.className = 'myPos';
        div.innerHTML = count;
        var markerX = new AMap.Marker({
           content: div,
           title:timeStamp,
           position: posM,
           offset: new AMap.Pixel(-12, -36),
           zIndex: 1000 - count
        });
 		markersA.push(markerX);
 		if (count==0) {
 			markerX.setLabel({offset: new AMap.Pixel(20, 0), content: timeStamp});
 		}
        markerX.setMap(map);
 };
 
 
 var createMarker2 = function(posM,count,timeStamp) {
        var markerX = new AMap.Marker({
           position: posM,
           icon:'<%=basePath%>images/pos8.gif',
           offset: new AMap.Pixel(-4, -4),
           zIndex: 1000 - count
        });
 		markersA.push(markerX);
        markerX.setMap(map);
 };

function showMarkers(){
var i=0;
var j=markersA.length;
var mk;

	for (i=0;i<j;i++){
		mk=markersA.pop();
		mk && mk.setMap(null);
	}
	for (i=0;i<posCount;i++){
		if ((i%20)==0){
			createMarker1(posXY[i],i,timeStrA[i]);
		}else {
			createMarker2(posXY[i],i,timeStrA[i]);
		}
		
	}
}

function M(id){
    return document.getElementById(id);
}

function clear_points(){
var i=0;
var j=posXY.length;
	maxCount=9999;
	for (i=0;i<j;i++){
		posXY.pop();
		timeStrA.pop();
	}
	posCount=0;
}
function menu_go() { 	
	initDate();
 	M("logTime").style.display="block";              
    return 1; 
  }   
    
function menu_latest() {	
    M("action_h").value=0;
	M("menu_his").submit();
	clear_points();
	maxCount=0;
	period=33000;
    return 1; 
 }   

  function f_showParameter(){
    
 	M("mapContainer").style.display="none";
 	M("statusPage").style.display="none";
  	M("setPage").style.display="block";
  	    M("actionCode").value="read";
   with (M("menu_schedule")) {   
           method = "post";           
           action = "motor_set.jsp"; 
           target="frm_set";  
           submit();   
        }  
  	M("menu_2").style.display="block";
  	M("menu_1").style.display="none";
  	return 2;
  }
  
  function f_getpara(rpts,nm){
	var l=nm.length+1;
	var nm2=nm + "=";
	var i=rpts.indexOf(nm2);
	var j;
	var rslt=null;
	if (i>=0) {
		i=i +l;
		j=rpts.indexOf('&',i);
		if (j<0) j=rpts.length;
		rslt=rpts.substring(i,j);
	}else {rslt=" ";}
	return rslt.toLowerCase();	
}
    
  function f_showStatus(){
    var rpts="<%=rpts %>";
    var alrm1=f_getpara(rpts,"ALRM1");
    var alrm2=f_getpara(rpts,"ALRM2");
    var sta=f_getpara(rpts,"STA");
    var rt="";
    
    if (alrm1.indexOf("vib")>=0) {rt=rt +"异常震动  ";}
    if (alrm1.indexOf("out")>=0) {rt=rt +"异常移动  ";}
    if (rt.length>2) {rt=rt +"<br/>";}
    if (alrm2.indexOf("low")>=0) {rt=rt +"电量偏低  ";}
    if (alrm2.indexOf("out")>=0) {rt=rt +"异常断电  ";}
    M("statusAlrm").innerHTML=rt;
    rt="";
    if (sta.indexOf("run")>=0) {rt=rt +"在行进  ";}
    if (sta.indexOf("park")>=0) {rt=rt +"停车中  ";}
    if (sta.indexOf("prot")>=0) {rt=rt +"防护已开  ";}
    M("statusMotor").innerHTML=rt;
    M("statusAcct").innerHTML=f_getpara(rpts,"ACCT");
	M("statusTime").innerHTML=f_getpara(rpts,"TIME");
	M("statusMile").innerHTML=f_getpara(rpts,"MILE");
	M("statusBat").innerHTML=f_getpara(rpts,"BAT");
  	M("mapContainer").style.display="none";
  	M("setPage").style.display="none";
  	M("statusPage").style.display="block";
  	M("menu_2").style.display="block";
  	M("menu_1").style.display="none";
  	return 2;
  }
  
  function  f_showMap(){
  M("statusPage").style.display="none";
  M("setPage").style.display="none";
  M("mapContainer").style.display="block";
 
  	M("menu_1").style.display="block";
  	M("menu_2").style.display="none";
  }
   
		
  function addPos3(xy){
		posXY.push([xy.lng,xy.lat]);
		timeStrA.push(tmp_tmstr);
		posCount++;		
		return;
	}	
//unshift：将参数添加到原数组开头
function insertPos3(xy){
	posCount++;
	posXY.unshift([xy.lng,xy.lat]);
	timeStrA.unshift(tmp_tmstr);
	return;
};
  
function converted(status,result){
	var mapCenter;
    var x0,y0,xy;
    var i=0;
    var l=0;
    var p1;
 
if(status=='complete' && result.info=='ok'){
	l=result.locations.length;
	x0=result.locations[l -1].lng;
 	y0=result.locations[l -1].lat;
	p1=[x0,y0];

	if (marked==0){
		marked=1;
		map = new AMap.Map('mapContainer', {
      	center: [x0,y0], // 设置中心点
		resizeEnable: true,
       	zoom: 14,    		 // 设置缩放级别
    });
    shareURL="http://m.amap.com/?name=M&q=" +y0+","+x0;
	}
	mapCenter = map.getCenter();
	x0=mapCenter.getLng();
    y0=mapCenter.getLat();
    xy=Math.abs(p1[0]-x0)+Math.abs(p1[1]-y0);
    if (xy>0.01){
 	 	map.panTo(p1);
 	 }
 		
 	if (posCount==0){  //===
		for (i=l -1;i>=0;i--) {
 			addPos3(result.locations[i]);
 		} 
		}	
	else if (timeStrA[posCount -1]>tmp_tmstr){
		for (i=l -1;i>=0;i--) {
 			addPos3(result.locations[i]);
 		} 
		}
	else if (timeStrA[0]<tmp_tmstr){
		for (i=0;i<l;i++) {
 			insertPos3(result.locations[i]);
 		} 
		}
			
   	 showMarkers();
     if (maxCount<2){    	
   		 maxCount++;
    	 setTimeout(refreshTimer,period);
    } 
 }

}
 function f_center3(gpsArray,sno,tmstr){
 var l=gpsArray.length;
 var i;
 var LngLatA=[];
 for (i=0;i<l;i++){
 	LngLatA[i]=new AMap.LngLat(gpsArray[i][0],gpsArray[i][1]);
 }
 tmp_sno=sno;
 tmp_tmstr=tmstr;
 M("currentNo").value=sno; 

  AMap.convertFrom(LngLatA,'gps',converted);
 }
 
 function refreshTimer(){
  	var cno3=parseInt(M("currentNo").value);
  	if (lastNo>=cno3){
  		period=33000;
  	}
  	lastNo=cno3;
 	cno3++; 

 	 M("currentNo").value=cno3;  
      with (M("menu_schedule")) {            
           action = "refresh3.jsp"; 
           target="frm_bottom";  
           submit();   
        }   
      return 1; 
    }   
 
</script>
  
</head>

<body style="background:black;">
  <div id="mapContainer"></div>
  
  <div id="setPage" style="display: none;">
   <iframe name="frm_set"  src="" frameborder=0 scrolling=no width=100% height=100% ></iframe> 
  </div>
  <div id="statusPage" style="display: none; margin:0 auto;color:white;">
   <br/>
  <table    border="0" align="center" title="" style="font-size:14px;color:white;">
    <tr><td>更新时间: </td><td id="statusTime" >.</td> </tr>
    <tr><td>电瓶电量：</td><td id="statusBat" ></td> </tr> 
	<tr><td>行使里程：</td><td id="statusMile"></td> </tr> 
	<tr><td>车辆状态：</td><td id="statusMotor"></td> </tr>
	<tr><td>账户余额：</td><td id="statusAcct"></td> </tr>
	<tr><td>告警信息：</td><td id="statusAlrm"  rowspan=2></td> </tr>
	</table>
  </div>
  
<div id="menu_tip" style="display: block">
	<div id="menu_1" style="display: block">
		<input type="button" value="历史轨迹" id="menu_old"  onClick="javascript:menu_go() ;"/> 
		<input type="button" value="实时追踪" id="menu_refresh"  onClick="javascript:menu_latest() ;"/>  
		<input type="button" value="其它信息" id="menu_set"  onClick="javascript:f_showStatus() ;"/>  			
	</div>
	<div id="menu_2"   style="display: none">
			<input type="button" value="位置查看" id="menu_map"  onClick="javascript:f_showMap() ;"/> 
			<input type="button" value="参数设置" id="menu_set2"  onClick="javascript:f_showParameter() ;"/> 
			<input type="button" value="状态信息" id="menu_info" onClick="javascript:f_showStatus() ;"/>
	</div>
</div>
	
<div style="display: none;heght:0px; ">
    <iframe name="frm_bottom" src="" align=bottom  frameborder=0 scrolling=no width=100%  height=0%></iframe> 
 
	<form name="menu_schedule" id="menu_schedule" action="refresh3.jsp" method="post" target="frm_bottom">	
			<input type="hidden"  name="openid" id="openid" value="<%=openId %>"  readonly>	
			<input type="hidden"  name="cid" id="cid" value="<%=cid %>"  readonly>
			<input type="hidden"  name="currentNo" id="currentNo"  value="<%=sno %>" readonly>
			<input type="hidden"  name="action" id="actionCode"  value="-"  readonly>
			<input type="submit" name="Submit" value="submit" >	
	</form>
</div>
  
	
  <script>
    var map;
 	f_center3(<%=gpsArray %>,0,"<%=timeStr%>");
  </script>
 <script type="text/javascript">

 wx.config({
    debug: false, // 
    appId: '<%=rx.com.DogThread.myAppid%>', // 
    timestamp:<%=rx.com.DogThread.timestamp%>, // 
    nonceStr: '<%=rx.com.DogThread.nonceStr%>', // 
    signature: '<%=JSKsignature%>',// 
    jsApiList: ['onMenuShareTimeline','onMenuShareAppMessage','onMenuShareQQ'] //
});

wx.ready(function(){
  wx.onMenuShareAppMessage({
    title: '我的位置', 
    desc: '详情>>',
    link: shareURL, 
    imgUrl: 'http://webapi.amap.com/images/0.png', 
    type: 'link',
    dataUrl: '',
    success: function () { },
    cancel: function () { }
});

wx.onMenuShareTimeline({
    title: '我的位置', 
    link: shareURL, 
    imgUrl: 'http://webapi.amap.com/images/0.png', 
    success: function () { },
    cancel: function () { }
});

wx.onMenuShareQQ({
    title: '我的位置', 
    desc: '详情>>',
    link: shareURL, 
    imgUrl: 'http://webapi.amap.com/images/0.png', 
    success: function () { },
    cancel: function () { }
});

});
 </script>
<div id="logTime" style="display:none;position:absolute;top:50%;left:50%;margin:-150px 0 0 -150px;width:236px;height:150px;z-index:0;border:solid 3px blue;background-color:white">
<script type="text/javascript">
var regdate;
function initDate(){
	var now = new Date();	
	var rq1 ;
	regdate=now.valueOf();
	regdate=regdate -2*60*60*1000;
	now=new Date(regdate);
	rq1 =(now.getFullYear())+'-'+(now.getMonth() +1)+'-'+now.getDate();
	document.getElementById("rq").innerHTML=rq1;
	document.getElementById("sj").innerHTML=now.getHours()+':00';	
}
function changedate(dd){
	var nw;
	var rq1;
	regdate=regdate +dd*60*60*1000;
	nw=new Date(regdate);
	rq1 =(nw.getFullYear())+'-'+(nw.getMonth() +1)+'-'+nw.getDate();
	document.getElementById("rq").innerHTML=rq1;
	document.getElementById("sj").innerHTML=nw.getHours()+':00';	
}
function timeok(){
	var rq1;
	var nw=new Date(regdate);
	rq1 =(nw.getFullYear())+'-'+(nw.getMonth() +1)+'-'+nw.getDate() +' '+nw.getHours()+':00';
	M("regDate").value=rq1;
	M("action_h").value=1;
	M("menu_his").submit();
	M("logTime").style.display="none";
	clear_points();
	maxCount=0;
	period=200;
}

</script>
输入轨迹起始点：<br>
<table style="width:100%" ><tr>
<td><a href="javascript:changedate(-24);"><img src='images/22.gif'/></a></td>
<td ><div style="border:solid 3px blue;height:30px;text-align:center;font-size:24px;width:140px;" id="rq">2016-01-22</div> </td>
<td><a href="javascript:changedate(24);"><img src='images/22plus.gif'/></a></td>
</tr>
<tr></tr>
<tr>
<td><a href="javascript:changedate(-1);"><img src='images/22.gif'/></a></td>
<td><div style="border:solid 3px blue;height:30px;text-align:center;font-size:24px;width:140px;" id="sj">08:00</div></td>
<td><a href="javascript:changedate(1);"><img src='images/22plus.gif'/></a></td>
</tr>
</table>
<br><div style="width:100%;text-align:center;"><button onclick="javascript:timeok()">确定</button></div> 
<form name="menu_his" id="menu_his" action="refresh3x.jsp" method="post" target="frm_bottom">		
			<input type="hidden"  name="cid" id="cid4" value="<%=cid %>" readonly>
			<input type="hidden"  name="action" id="action_h"  value="1" readonly>
			<input type="hidden"  name="regDate" id="regDate"	value="2016-01-22" readonly>				
		</form>
</div>
</body>
</html>
