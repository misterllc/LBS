package rx.com;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/*  �ߵµ�ͼ��λ ����ӿ� (REST)���ܽ��� 
	���� �ֻ��ͻ����ϴ��յĻ�վ /wifi����Ϣ����λ���񷵻��ն˵��á�
	HTTP/GET http://apilocate.amap.com/position  �����ʽutf-8
	bts �����վ��Ϣ ,��CDMA��ʽΪ��mcc,mnc,lac,cellid,signal ;nearbts�ڽ���վ
	serverip �Ǳ���,�豸�����վʱ��Ӧ������IP 
	network ������������ GSM/GPRS/EDGE/HSUPA/HSDPA/WCDMA accesstype=0ʱ������ 
	smac �ֻ�mac�� �Ǳ���
	mmac �����ȵ�mac��Ϣ mac,signal,ssid�� �磺f0:7d:68:9e:7d:18,-41,TPLink ;macs�ȵ��б�
	
������ʽΪ�ƶ��������ֻ���Ϊ��cdma������accesstype����Ϊ0��cdma=0ʱ�� 
http://apilocate.amap.com/position?accesstype=0&imei=352315052834187
&cdma=0&bts=460,01,40977,2205409,-65
&nearbts=460,01,40977,2205409,-65|460,01,40977,2205409,-65|460,01,40977,2205409,-65
&serverip=10.2.166.4&output=xml&key=<�û�Key> 

������ʽΪwifi���룬��accesstype����Ϊ1ʱ�� BC:F6:85:DA:73:44
http://apilocate.amap.com/position?accesstype=1&imei=352315052834187&mmac=22:27:1d:20:08:d5,-55,CMCC-EDU
&macs=22:27:1d:20:08:d5,-55,CMCC-EDU|5c:63:bf:a4:bf:56,-86,TP-LINK|d8:c7:c8:a8:1a:13,-42,TP-LINK &serverip=10.2.166.4&output=xml
&key=<�û�Key>
���: <type>4</type>type=0��û�еõ���λ�����
<response><status>1</status><info>OK</info><result><type>4</type>
<location>116.4810549,39.9899077</location><radius>550</radius>
<desc>������ ������ ���ٽ� �����������(����������վƵ걱)</desc>
<country>�й�</country><province>������</province><city>������</city><citycode>010</citycode><adcode>110105</adcode><road>���ٽ�</road><street>���ٽ�</street><poi>�������(����������վƵ걱)</poi></result></response>
����ת��:
http://restapi.amap.com/v3/assistant/coordinate/convert?locations=116.481499,39.990475&coordsys=gps&output=xml&key=�������key
���Ⱥ�γ����","�ָ������ǰ��γ���ں󣬾�γ��С����󲻵ó���6λ������������á�;�����зָ�
 * */

public class GsmLBS {
	String httpUrl = "http://apilocate.amap.com/position?key=9335b840a4ba56aae2d6b5769b68cc41&output=xml";// ����apikey��HTTP header
	String apiUrlCoord="http://restapi.amap.com/v3/assistant/coordinate/convert?key=c43bfdee723664db60ef7181db45c4e0&output=xml&coordsys=gps";
	
	//String httpArg = "phone=18007311810&content=%E6%82%A8%E6%9";
	//String jsonResult = request(httpUrl, httpArg);
	//System.out.println(jsonResult);

	public String getLocFromGSM(String httpArg) {//���� �ֻ��ͻ����ϴ��յĻ�վ /wifi����Ϣ����λ���񷵻��ն˵��á�
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

	public String changeCoordate(String httpArg) {  //����ת��:
	
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
