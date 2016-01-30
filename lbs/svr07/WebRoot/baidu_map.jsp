<%@ page language="java" import="java.util.* " pageEncoding="utf-8"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
Integer  cid=Integer.valueOf(request.getParameter("c"));
Integer  sno=Integer.valueOf(request.getParameter("sno"));  
String loc_s=""; //mydata1.get_card_location(cid,sno);
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=16Q3MQONrEdSIkrB0dkRgp2q"></script>
<script type="text/javascript" src="http://developer.baidu.com/map/jsdemo/demo/changeMore.js"></script>
<title>地图</title>
<style type="text/css">
body,html,#allmap {width:100%;height: 100%;overflow: hidden;margin:0;}
#datamap {width: 400px;height: 100px;overflow: hidden;margin:0;display:none;}
</style>

<script type="text/javascript">
function latest()
{
//	alert(datas);
	//map.clearOverlays();
	//showmap(datas);
	BMap.Convertor.transMore(linesPoints,0,callback);
//	add_label();
}
</script>

</head>

<body>
<div id="allmap"></div>


<script type="text/javascript">
var p_str="<%=loc_s%>"; //
var lat,lon,st;
var linesPoints = new Array();
var st_points=new Array();
var myIcon = new BMap.Icon("./images/icon/women.gif", new BMap.Size(12,12));
var label_time ;
var lat0=0,lon0=0;
function showmap(data_str)
{
var fld=data_str.split(",",91);
if(linesPoints.length>0) {linesPoints.splice(0,linesPoints.length);st_points.splice(0,linesPoints.length);}
label_time = fld[0].toString().substr(5,11); //new BMap.Label(fld[0],{offset:new BMap.Size(20,0)});
var i=1;
while(i<fld.length)
{
	st=parseInt(fld[i++]);
	if(st<64)
	{
		lat=parseInt(fld[i++])/360000;
		lon=parseInt(fld[i++])/360000;
	}
	else
	{
		lat=parseFloat(fld[i++]);
		lon=parseFloat(fld[i++]);
		if((lat0 -lat)<0.015  && (lat0 -lat)>-0.015){
			lat=(lat+lat0)/2;
			lon=(lon+lon0)/2;
		}
	}
	if(st>0){
	linesPoints.push(new BMap.Point(lon,lat));
	st_points.push(st);
	lat0=lat;
	lon0=lon;
	}
}
}
showmap(p_str);
// 百度地图API功能
var map = new BMap.Map("allmap");            // 创建Map实例
//var point = new BMap.Point(lon, lat);    // 创建点坐标
map.centerAndZoom(linesPoints[linesPoints.length -1],15);                     // 初始化地图,设置中心点坐标和地图级别。point
map.enableScrollWheelZoom();                            //启用滚轮放大缩小
map.addControl(new BMap.NavigationControl());  //添加默认缩放平移控件
//map.addControl(new BMap.NavigationControl({anchor: BMAP_ANCHOR_TOP_RIGHT, type: BMAP_NAVIGATION_CONTROL_SMALL}));  //右上角，仅包含平移和缩放按钮
map.addControl(new BMap.MapTypeControl({anchor: BMAP_ANCHOR_TOP_RIGHT}));    //左上角，默认地图控件
//var marker1 = new BMap.Marker(new BMap.Point(110.284, 25.260));  // 创建标注
///map.addOverlay(marker1);              // 将标注添加到地图中

//创建信息窗口
//var infoWindow1 = new BMap.InfoWindow("普通标注");
//marker1.addEventListener("click", function(){this.openInfoWindow(infoWindow1);});

//创建自定义图标
//var pt = new BMap.Point(lon, lat);
//==var myIcon = new BMap.Icon("./images/icon/master.gif", new BMap.Size(12,12));
//==var marker2 = new BMap.Marker(linesPoints[linesPoints.length -1],{icon:myIcon});  // 创建标注
//==map.addOverlay(marker2);              // 将标注添加到地图中

//让小狐狸说话（创建信息窗口）
//==var infoWindow2 = new BMap.InfoWindow(lat+"d");
//==marker2.addEventListener("click", function(){this.openInfoWindow(infoWindow2);});
//添加圆
//var circle = new BMap.Circle(point,500);
//map.addOverlay(circle);

var opts = {
  width : 100,     // 信息窗口宽度
  height: 30     // 信息窗口高度
 // title : "王府井" , // 信息窗口标题
 // enableMessage:true,//设置允许信息窗发送短息
 // message:"..."
};
//==var infoWindow = new BMap.InfoWindow(fld[0], opts);  // 创建信息窗口对象
//==map.openInfoWindow(infoWindow,linesPoints[linesPoints.length -1]); //开启信息窗口

 //==  var carMk = new BMap.Marker(linesPoints[0],{icon:myIcon});
 //==       map.addOverlay(carMk);
    //==    resetMkPoint(1,carMk);
    
    function resetMkPoint(i,carMk){
        carMk.setPosition(linesPoints[i]);
        addMarker(linesPoints[i],50);
        
        if(i < linesPoints.length){
            setTimeout(function(){
                i++;
                resetMkPoint(i,carMk);
            },100);
        }
    }

    function addMarker(point,r){
  var marker = new BMap.Marker(point);
  map.addOverlay(marker);
 //添加圆//strokeStyle  String  圆形边线的样式，solid或dashed;fillColor fillOpacity 圆形填充的透明度，取值范围0 - 1
}
  

// 坐标转换
 var xyResult = null;
var pointA;
var point_count=0;
function callback(xyResults){

 var j=0;
 for(var index in xyResults){
  xyResult = xyResults[index];
  if(xyResult.error != 0){continue;}//出错就直接返回;
  pointA = new BMap.Point(xyResult.x, xyResult.y);
  
  if(st_points[j]<64){
     var marker = new BMap.Marker(pointA,{icon:myIcon});
     map.addOverlay(marker);
     map.setCenter(pointA);  // 每一个被设置的点都是中心点的过程
 // if (index==(xyResults.length -1))	marker.setLabel(label_time); //添加GPS标注
  }
  else
	  {
	  var circle = new BMap.Circle(pointA,600,{strokeColor:"blue", strokeWeight:2, strokeOpacity:0.2,fillOpacity:0.04,strokeStyle:"dashed",enableMassClear:false});
		map.addOverlay(circle);
	  }
  j++; 
  point_count++;
    }
 if(point_count==linesPoints.length){add_label();}
}
//setTimeout(function(){
    BMap.Convertor.transMore(linesPoints,0,callback);        //一秒之后开始进行坐标转换。参数2，表示是从GCJ-02坐标到百度坐标。参数0，表示是从GPS到百度坐标
//}, 1000);
function add_label()
{
	var opts = {position : pointA, 	offset   : new BMap.Size(0, 0) 	};
	var label = new BMap.Label(label_time, opts);  // 创建文本标注对象

		map.addOverlay(label);
}

</script>


</body>

</html>
