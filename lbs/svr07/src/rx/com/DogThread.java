package rx.com;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Formatter;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;




//import net.sf.json.JSONException;
import rx.com.MotorData;

public final class DogThread {
	public  static boolean dog_is_run=false;
    private  static  Dog dog=null;
	private  static  MotorData mData=null;
   	public  static   String token=null;// ��ȡ����ƾ֤
   	public  static   String jsapi_ticket=null;  //jsapiƾ֤����Ч��ͬtoken
   	public static String timestamp=null;// �������ǩ����ʱ���
   	public static String nonceStr="wxeb6cf6348c8d4aa0"; // �������ǩ���������
	public   static  int expiresIn=0;// ƾ֤��Чʱ�䣬��λ����
	public  static  String myAppid="wxeb6cf6348c8d4aa0";
	private  static  String myAppSecret="8ca36c057f2b51baad70f6d87bcfec3d";
	private  static  String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
//	private String post_message_url="https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";
	
	//rx   appid=wx4006cb3c7c1154b7
	//test appid=wxeb6cf6348c8d4aa0  appSecret ="8ca36c057f2b51baad70f6d87bcfec3d";
  
	public  static  void DogStart(){ 
		if (dog_is_run){return;}	
		dog=new Dog("Dog_thread");
		mData=new MotorData();
        dog.start(); 
        dog_is_run=true;
        System.out.println("dog  start........");  //test=====
    } 

private  static  class Dog extends Thread{ 

   public Dog(String name){ 
        super(name);//���ø���������Ĺ��췽�� 
    } 
    
    public  void run(){ 
    	int timer=0;
    	int token_timer=0;
    	int test_t=10;
    	
        while(true){ 

			if(timer>=test_t)
			{
				String urgentStr=mData.get_urgent_task();
			
				if(urgentStr.length()>10 ){
					int i=urgentStr.indexOf(" &"); //�ָ�openId,report body
					if (i>0 && expiresIn>0){
						String oid=urgentStr.substring(0,i);
						i=i +2;
						String msg=urgentStr.substring(i);
					//	postMessage(oid,msg);
						postTemplateMessage(oid,msg);
					}
					test_t=0;
				}else {
					if (urgentStr.charAt(0)=='0') {
						test_t=10;
					}
				}
				timer=0;
			}
			if(token_timer>=expiresIn)
			{
				token_timer=0;
				getAccessToken();
				if(token!=null){
					getJsapiTicket();
				}
				timestamp=""+(System.currentTimeMillis()/1000);
			}
			
			timer++;
        	try {
				sleep(500);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			token_timer++;
			
        } 
    } 
}
/*��ȡaccess token ����ʽ: GET
  https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
    ��������JSON���ݰ� {"access_token":"ACCESS_TOKEN","expires_in":7200}
*/
private  static  void getAccessToken() {
	String requestUrl = access_token_url.replace("APPID", myAppid).replace(
			"APPSECRET", myAppSecret);
	String jsonObject = httpRequest(requestUrl, "GET", null);
	// �������ɹ�
	if (null != jsonObject) {
			token=getElement(jsonObject,"access_token");   
			expiresIn=getElementInt(jsonObject,"expires_in");  
			System.out.println(jsonObject);
	}
	return ;
}
/*
 * access_token�� ��GET��ʽ����jsapi_ticket��https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi
 */
private  static  void getJsapiTicket() {
	String requestUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi&access_token=" +token;
	String jsonObject = httpRequest(requestUrl, "GET", null);
	// �������ɹ�
	if (null != jsonObject) {
			jsapi_ticket=getElement(jsonObject,"ticket");   
			//expiresIn=getElementInt(jsonObject,"expires_in");  
			System.out.println(jsonObject);
	}
	return ;
}
/*�����ı���Ϣ post
https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN
{
    "touser":"OPENID",
    "msgtype":"text",
    "text":{"content":"Hello World"}
}*/
//
//private  static  int postMessage(String openId,String msg){
//	int result = 0;	
//	String url ="https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" +token;
//	StringBuilder  msgBody=new StringBuilder();
//	msgBody.append("{\"touser\":\"");
//	msgBody.append(openId);
//	msgBody.append("\",\"msgtype\":\"text\",\"text\":{\"content\":\"");
//	msgBody.append(msg);
//	msgBody.append("\"}}");
//	
//	String jsonObject = httpRequest(url, "POST", msgBody.toString());// ���ýӿ�
//
//if (null != jsonObject) {
//			result = getElementInt(jsonObject,"errcode");
//	}
//	return result;
//}

//����ģ����Ϣ,https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN
private  static  int postTemplateMessage(String openId,String rpt){
	int result = 0;	
	String url ="https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" +token;
	StringBuilder  msgBody=f_getAlarmInfo(openId,rpt);
	
	String jsonObject = httpRequest(url, "POST", msgBody.toString());// ���ýӿ�

if (null != jsonObject) {
			result = getElementInt(jsonObject,"errcode");
	}
	return result;
}

public  static  String  httpRequest(String requestUrl,String requestMethod, String outputStr) {
	String jsonObject = null;
	StringBuffer buffer = new StringBuffer();
	try {
		// ����SSLContext���󣬲�ʹ������ָ�������ι�������ʼ��
		TrustManager[] tm = { new TrustManager() };
		SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
		sslContext.init(null, tm, new java.security.SecureRandom());
		// ������SSLContext�����еõ�SSLSocketFactory����
		SSLSocketFactory ssf = sslContext.getSocketFactory();

		URL url = new URL(requestUrl);
		HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
		httpUrlConn.setSSLSocketFactory(ssf);

		httpUrlConn.setDoOutput(true);
		httpUrlConn.setDoInput(true);
		httpUrlConn.setUseCaches(false);
		
		httpUrlConn.setRequestMethod(requestMethod);// ��������ʽ��GET/POST��

		if ("GET".equalsIgnoreCase(requestMethod))
			httpUrlConn.connect();

		// ����������Ҫ�ύʱ
		if (null != outputStr) {
			OutputStream outputStream = httpUrlConn.getOutputStream();
			// ע������ʽ����ֹ��������
			outputStream.write(outputStr.getBytes("utf-8"));
			outputStream.close();
		}

		// �����ص�������ת�����ַ���
		InputStream inputStream = httpUrlConn.getInputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(
				inputStream, "utf-8");
		BufferedReader bufferedReader = new BufferedReader(
				inputStreamReader);

		String str = null;
		while ((str = bufferedReader.readLine()) != null) {
			buffer.append(str);
		}
		bufferedReader.close();
		inputStreamReader.close();
		// �ͷ���Դ
		inputStream.close();
		inputStream = null;
		httpUrlConn.disconnect();
		jsonObject =buffer.toString(); //JSONObject.fromObject(buffer.toString());
	} catch (ConnectException ce) {
		System.out.println("΢�ŷ��������ӳ�ʱ��");
	} catch (Exception e) {
		System.out.println("HTTPS������󣬴�����Ϣ��\n" + e.getMessage());
	}
	return jsonObject;
}



 private  static    class TrustManager implements X509TrustManager {

	public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	}

	public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	}

	public X509Certificate[] getAcceptedIssuers() {
		return null;
	}
}
 
 private  static String getElement(String src,String eName){
		int i,j,l;
		String rslt=null;
		l=eName.length();
		i=src.indexOf(eName);
		if(i>=0){
			i=i+l+3;
			j=src.indexOf('"', i);
			if (j>0) {
				rslt=src.substring(i,j);
			}else{
				rslt=""; //src.substring(i,i+3);
			}
		}else{
			rslt="";
		}
		return rslt;
	}
 
 private  static int getElementInt(String src,String eName){
		int i,j,l;
		String rslt=null;
		l=eName.length();
		i=src.indexOf(eName);
		if(i>=0){
			i=i+l+2;
			j=i;
			while (src.charAt(j)>='0' && src.charAt(j)<='9') {j++;}
			if (j>i) {
				rslt=src.substring(i,j);
				l= Integer.parseInt(rslt);
			}else{
				l= 0;
			}
		}else {l=0;}
		return l;
 }
 
 private static String f_getpara(String rpts,String nm){
			int l=nm.length();
			int i=rpts.indexOf(nm);
			int j;
			String rslt=null;
			if (i>=0) {
				i=i +l;
				j=rpts.indexOf('&',i);
				if (j<0) j=rpts.length();
				rslt=rpts.substring(i,j);
			}else {rslt=" ";}
			return rslt.toLowerCase();	
		}

 /** ģ����ϢΪһ��json��
 {"touser":"OPENID",
 "template_id":"nEmAyLVZK19XPVySeikHrJXYF9Fohl0eHBohOXe0hxo",
 "url":"http://weixin.qq.com/download",
 "topcolor":"#FF0000",
 "data":{
 		"first": {"value":"2015-12-21 12:00:00", "color":"#173177"},
 		"remark":{"value":"�Ƿ��ƶ�","color":"#173177"}
     }
 }
 */
 private static StringBuilder  f_getAlarmInfo(String oid,String rpts){
	 String alrm1=f_getpara(rpts,"ALRM1=");
	 String alrm2=f_getpara(rpts,"ALRM2=");
	 StringBuilder rt=new StringBuilder();
    
	 rt.append("{\"touser\":\"");
	 rt.append(oid);
	 rt.append("\",\"template_id\":\"nEmAyLVZK19XPVySeikHrJXYF9Fohl0eHBohOXe0hxo\",\"url\":\"\",\"topcolor\":\"#FF0000\",");
	 rt.append("\"data\":{\"first\": {\"value\":\"");	
	 rt.append(f_getpara(rpts,"TIME="));
	 rt.append("\", \"color\":\"#173177\"},\"remark\":{\"value\":\"");
	 
	    if (alrm1.indexOf("vib")>=0) {rt.append("�쳣��  ");}
	    if (alrm1.indexOf("out")>=0) {rt.append("�쳣�ƶ�  ");}
	   // if (rt.length()>2) {rt.append("\n");}
	    if (alrm2.indexOf("low")>=0) {rt.append("����ƫ��  ");}
	    if (alrm2.indexOf("out")>=0) {rt.append("�쳣�ϵ�  ");}
	  rt.append("\",\"color\":\"#173177\"} } }");

	  	return rt;
	  }
	  
 public static String f_sha1(String myStr){
	 String rtn=null;
	 StringBuilder srcStr=new StringBuilder();
	 srcStr.append("jsapi_ticket=");
	 srcStr.append(jsapi_ticket);
	 srcStr.append("&noncestr=wxeb6cf6348c8d4aa0&timestamp=");
	 srcStr.append(timestamp);
	 srcStr.append("&url=");
	 srcStr.append(myStr);
	 
	 try {
		java.security.MessageDigest alga=java.security.MessageDigest.getInstance("SHA-1");
		alga.reset();
		alga.update(srcStr.toString().getBytes("UTF-8"));
		rtn=byteToHex(alga.digest());  // new String(alga.digest());
	} catch (NoSuchAlgorithmException e) {
		e.printStackTrace();
		rtn="";
	}
	 catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			rtn="";
		}
	 return rtn;
 }
 
 private static String byteToHex(final byte[] hash) {
     Formatter formatter = new Formatter();
     for (byte b : hash)
     {
         formatter.format("%02x", b);
     }
     String result = formatter.toString();
     formatter.close();
     return result;
 }
 
}
