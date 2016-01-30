
<%@ page language="java" import="java.util.*" import="rx.com.GsmLBS" pageEncoding="utf-8"%>
<jsp:useBean id="mydata1"  class="rx.com.MotorData" scope="application"/>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String basePath2 = request.getScheme()+"://"+request.getServerName()+path+"/";

 String openId=request.getParameter("openId");

String rpts=mydata1.get_motor_openid_location2(openId, 0);  

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

function callbackA(sno,locStr)
{
	is_login=true;
    M("currentNo").value=sno; 
    return 0;
}

function menu_go() { 
 		M("actionCode").value="-";      
          with (M("menu_schedule")) {   
           method = "post";           
           action = "refresh.jsp"; 
           target="frm_bottom";  
           submit();   
        }  
        return 1; 
    }   
    
    function menu_latest() {
           M("currentNo").value="0"; 
           M("actionCode").value="0"; 
          with (M("menu_schedule")) {   
           method = "post";           
           action = "refresh.jsp"; 
           target="frm_bottom";  
           submit();   
        }  
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
		<input type="button" value="上一位置" id="menu_old"  onClick="javascript:menu_go() ;"/> 
		<input type="button" value="位置刷新" id="menu_refresh"  onClick="javascript:menu_latest() ;"/>  
		<input type="button" value="其它信息" id="menu_set"  onClick="javascript:f_showStatus() ;"/>  			
	</div>
	<div id="menu_2"   style="display: none">
			<input type="button" value="位置查看" id="menu_map"  onClick="javascript:f_showMap() ;"/> 
			<input type="button" value="参数设置" id="menu_set2"  onClick="javascript:f_showParameter() ;"/> 
			<input type="button" value="状态信息" id="menu_info" onClick="javascript:f_showStatus() ;"/>
	</div>
</div>
	
<div style="display: none;heght:0px; position:absolute; ">
    <iframe name="frm_bottom" src="" align=bottom  frameborder=0 scrolling=no width=100%  height=0%></iframe> 
 
	<form name="menu_schedule" id="menu_schedule" action="schedule.jsp" method="post" target="frm_bottom">		
			<input type="hidden"  name="openid" id="openid" value="<%=openId %>"  readonly>
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

</body>
</html>
