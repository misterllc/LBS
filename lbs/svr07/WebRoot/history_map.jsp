<%@ page language="java" import="java.util.*" import="rx.com.GsmLBS" pageEncoding="utf-8"%>
<jsp:useBean id="mdataHistory"  class="rx.com.MotorData" scope="application"/>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

String cid=request.getParameter("cid");
String rq=request.getParameter("regDate");
String  action=request.getParameter("action");
String sql=null;
int timerPeriod=200;

if (cid==null) return;
if (action.charAt(0)=='1'){
	sql="select f_motor_cid_tm_loc("+cid+",'"+rq+"');";
} else {
	sql="select f_motor_cid_latest_loc("+cid+")";
	timerPeriod=30000;
}
String rpts=mdataHistory.dsQuery(sql);  

int i, j=0;
String locs=null;
String timeStr=null;
String gps=null;
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

  <style type="text/css">
	body{margin:0;height:100%;width:100%;position:absolute;}
	#mapContainer{position: absolute;top:0;left: 0;right:0;bottom:0;}
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
var posCount=0;
var posXY=[];
var timeStrA=[];
var markerA=[];

var marked=0;
var tmp_sno;
var tmp_tmstr;
var converting=0;
var maxCount=0;
var lastNo=0;
var period=<%=timerPeriod%>;
var realTime=1;

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
 		markerA.push(markerX);
 		if (count==0) {
 			markerX.setLabel({offset: new AMap.Pixel(20, 0), content: timeStamp});
 		}
        markerX.setMap(map);
 };
 
 
 var createMarker2 = function(posM,count,timeStamp) {
        var markerX = new AMap.Marker({
           position: posM,
           icon:'images/pos8.gif',
           offset: new AMap.Pixel(-4, -4),
           zIndex: 1000 - count
        });
 		markerA.push(markerX);
        markerX.setMap(map);
 };

function showMarkers(){
var i=0;
var j=markerA.length;
var mk;

	for (i=0;i<j;i++){
		mk=markerA.pop();
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
 
 converting=0;
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
    if (maxCount<2||realTime<50){
   		 maxCount++;
   		 realTime++;
    	 setTimeout(refreshTimer,period);
    }
 }

};

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
  	if (period<9000){
  		realTime=999;
  	}
  	lastNo=cno3;
 	cno3++; 

 	 M("currentNo").value=cno3;  
     M("menu_schedule3").submit();   
      return 1; 
    }   
 
</script>
  
</head>

<body style="background:black;" onload="javascript:window.parent.f_showStatus('<%=rpts%>');">
  <div id="mapContainer"></div>
  	
<div style="display: none;heght:0px; position:absolute; ">
    <iframe name="frm_bottom3" src="" align=bottom  frameborder=0 scrolling=no width=100%  height=0%></iframe> 
	<form name="menu_schedule3" id="menu_schedule3" action="refresh3.jsp" method="post" target="frm_bottom3">		
			<input type="hidden"  name="cid" id="cid" value="<%=cid %>"  readonly>
			<input type="hidden"  name="currentNo" id="currentNo"  value="<%=sno %>" readonly>
			<input type="hidden"  name="action" id="actionCode"  value="-"  readonly>
			<input type="submit" name="Submit" value="submit" >	
	</form>
</div> 
<script>
var map;
 f_center3(<%=gpsArray %>,<%=sno%>,"<%=timeStr%>");
 </script>
</body>
</html>
