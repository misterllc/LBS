package rx.com;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/*  高德地图定位 服务接口 (REST)功能介绍 
	根据 手机客户端上传终的基站 /wifi等信息，定位服务返回终端的置。
	HTTP/GET http://apilocate.amap.com/position  编码格式utf-8
	bts 接入基站信息 ,非CDMA格式为：mcc,mnc,lac,cellid,signal ;nearbts邻近基站
	serverip 非必填,设备接入基站时对应的网关IP 
	network 无线网络类型 GSM/GPRS/EDGE/HSUPA/HSDPA/WCDMA accesstype=0时，必填 
	smac 手机mac码 非必填
	mmac 已连热点mac信息 mac,signal,ssid。 如：f0:7d:68:9e:7d:18,-41,TPLink ;macs热点列表
	
联网方式为移动接入且手机卡为非cdma卡，即accesstype参数为0且cdma=0时： 
http://apilocate.amap.com/position?accesstype=0&imei=352315052834187
&cdma=0&bts=460,01,40977,2205409,-65
&nearbts=460,01,40977,2205409,-65|460,01,40977,2205409,-65|460,01,40977,2205409,-65
&serverip=10.2.166.4&output=xml&key=<用户Key> 

联网方式为wifi接入，即accesstype参数为1时： BC:F6:85:DA:73:44
http://apilocate.amap.com/position?accesstype=1&imei=352315052834187&mmac=22:27:1d:20:08:d5,-55,CMCC-EDU
&macs=22:27:1d:20:08:d5,-55,CMCC-EDU|5c:63:bf:a4:bf:56,-86,TP-LINK|d8:c7:c8:a8:1a:13,-42,TP-LINK &serverip=10.2.166.4&output=xml
&key=<用户Key>
结果: <type>4</type>type=0：没有得到定位结果；
<response><status>1</status><info>OK</info><result><type>4</type>
<location>116.4810549,39.9899077</location><radius>550</radius>
<desc>北京市 朝阳区 阜荣街 靠近锦绣餐厅(北京方恒假日酒店北)</desc>
<country>中国</country><province>北京市</province><city>北京市</city><citycode>010</citycode><adcode>110105</adcode><road>阜荣街</road><street>阜荣街</street><poi>锦绣餐厅(北京方恒假日酒店北)</poi></result></response>
坐标转换:
http://restapi.amap.com/v3/assistant/coordinate/convert?locations=116.481499,39.990475&coordsys=gps&output=xml&key=您申请的key
经度和纬度用","分割，经度在前，纬度在后，经纬度小数点后不得超过6位。多个坐标点间用”;”进行分隔
 * */

public class GsmLBS {
	String httpUrl = "http://apilocate.amap.com/position?key=9335b840a4ba56aae2d6b5769b68cc41&output=xml";// 填入apikey到HTTP header
	String apiUrlCoord="http://restapi.amap.com/v3/assistant/coordinate/convert?key=c43bfdee723664db60ef7181db45c4e0&output=xml&coordsys=gps";
	
	//String httpArg = "phone=18007311810&content=%E6%82%A8%E6%9";
	//String jsonResult = request(httpUrl, httpArg);
	//System.out.println(jsonResult);

	public String getLocFromGSM(String httpArg) {//根据 手机客户端上传终的基站 /wifi等信息，定位服务返回终端的置。
	    BufferedReader reader = null;
	    String result = null;
	    StringBuffer sbf = new StringBuffer();
	    String httpUrl2 = httpUrl + "&" + httpArg;

	    try {
	        URL url = new URL(httpUrl2);
	        HttpURLConnection connection = (HttpURLConnection) url
	                .openConnection();
	        connection.setRequestMethod("GET");

	        connection.connect();
	        InputStream is = connection.getInputStream();
	        reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
	        String strRead = null;
	        while ((strRead = reader.readLine()) != null) {
	            sbf.append(strRead);
	            sbf.append("\r\n");
	        }
	        reader.close();
	        result = sbf.toString();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return result;
	}

	public String changeCoordate(String httpArg) {  //坐标转换:
	
	    BufferedReader reader = null;
	    String result = null;
	    StringBuffer sbf = new StringBuffer();
	    String httpUrl2 =apiUrlCoord + "&" + httpArg;
	    int i,j;
	    try {
	        URL url = new URL(httpUrl2);
	        HttpURLConnection connection = (HttpURLConnection) url
	                .openConnection();
	        connection.setRequestMethod("GET");

	        connection.connect();
	        InputStream is = connection.getInputStream();
	        reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
	        String strRead = null;
	        while ((strRead = reader.readLine()) != null) {
	            sbf.append(strRead);
	            sbf.append("\r\n");
	        }
	        reader.close();
	        result = sbf.toString();
	        i=result.indexOf("tions>");
	        j=result.indexOf("<", i);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
	    //<response><status>1</status><info>ok</info><locations>110.28408628,25.24886223</locations></response>
	    i=result.indexOf("tions>");
	    i=i +6;
        j=result.indexOf("<", i);
	    return result.substring(i, j);
	}


}
