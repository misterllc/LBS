package rx.com;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;  
import java.io.InputStream;
import java.net.SocketTimeoutException;  
import java.security.GeneralSecurityException;  
import java.security.cert.CertificateException;  
import java.security.cert.X509Certificate;  
import java.util.ArrayList;  
import java.util.List;  
import java.util.Map;  
import java.util.Map.Entry;  
import java.util.Set;  
  
import javax.net.ssl.SSLContext;  
import javax.net.ssl.SSLException;  
import javax.net.ssl.SSLSession;  
import javax.net.ssl.SSLSocket;  
  
//import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;  
import org.apache.commons.lang.StringUtils;  
import org.apache.http.Consts;  
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;  
import org.apache.http.HttpResponse;  
import org.apache.http.NameValuePair;  
import org.apache.http.client.HttpClient;  
import org.apache.http.client.config.RequestConfig;  
import org.apache.http.client.config.RequestConfig.Builder;  
import org.apache.http.client.entity.UrlEncodedFormEntity;  
import org.apache.http.client.methods.HttpGet;  
import org.apache.http.client.methods.HttpPost;  
import org.apache.http.conn.ConnectTimeoutException;  
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;  
import org.apache.http.conn.ssl.SSLContextBuilder;  
import org.apache.http.conn.ssl.TrustStrategy;  
import org.apache.http.conn.ssl.X509HostnameVerifier;  
import org.apache.http.entity.ContentType;  
import org.apache.http.entity.StringEntity;  
import org.apache.http.impl.client.CloseableHttpClient;  
import org.apache.http.impl.client.HttpClients;  
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;  
import org.apache.http.message.BasicNameValuePair;  

/** 
 * HttpClientUtils, 使用 HttpClient 4.x<br> 
 *  
 */  
public final class httpClientUtil {  
  
	private static String APPID="wxeb6cf6348c8d4aa0";
	private static String SECRET="8ca36c057f2b51baad70f6d87bcfec3d";
	private static StringBuffer TOCKEN=new StringBuffer();
    private static HttpClient client = null;  
    static {  
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();  
        cm.setMaxTotal(128);  
        cm.setDefaultMaxPerRoute(128);  
        client = HttpClients.custom().setConnectionManager(cm).build();  
    }  
  //  private JSONObject jsonObject = null;
    /** 
     * 发送一个 Post 请求, 使用指定的字符集编码. 
     *  
     * @param url 
     * @param body 
     *            RequestBody 
     * @param mimeType 
     *            例如 application/xml 
     * @param charset 
     *            编码 
     * @param connTimeout 
     *            建立链接超时时间,毫秒. 
     * @param readTimeout 
     *            响应超时时间,毫秒. 
     * @return ResponseBody, 使用指定的字符集编码. 
     *  
     * @throws ConnectTimeoutException 
     *             建立链接超时异常 
     * @throws SocketTimeoutException 
     *             响应超时 
     * @throws Exception 
     */  
    public static String post(String url, String body, String mimeType,  
            String charset, Integer connTimeout, Integer readTimeout)  
            throws ConnectTimeoutException, SocketTimeoutException, Exception {  
        HttpClient client = null;  
        HttpPost post = new HttpPost(url);  
        String result = "";  
        try {  
            if (StringUtils.isNotBlank(body)) {  
                HttpEntity entity = new StringEntity(body, ContentType.create(  
                        mimeType, charset));  
                post.setEntity(entity);  
            }  
            // 设置参数  
            Builder customReqConf = RequestConfig.custom();  
            if (connTimeout != null) {  
                customReqConf.setConnectTimeout(connTimeout);  
            }  
            if (readTimeout != null) {  
                customReqConf.setSocketTimeout(readTimeout);  
            }  
            post.setConfig(customReqConf.build());  
  
            HttpResponse res;  
            if (url.startsWith("https")) {  
                // 执行 Https 请求.  
                client = createSSLInsecureClient();  
                res = client.execute(post);  
            } else {  
                // 执行 Http 请求.  
                client = httpClientUtil.client;  
                res = client.execute(post);  
            }  
            result = IOUtils.toString(res.getEntity().getContent(), charset);  
        } finally {  
            post.releaseConnection();  
            if (url.startsWith("https") && client != null  
                    && client instanceof CloseableHttpClient) {  
                ((CloseableHttpClient) client).close();  
            }  
        }  
        return result;  
    }  
  
    /** 
     * 提交form表单 
     *  
     * @param url 
     * @param params 
     * @param connTimeout 
     * @param readTimeout 
     * @return 
     * @throws ConnectTimeoutException 
     * @throws SocketTimeoutException 
     * @throws Exception 
     */  
    public static String postForm(String url, Map<String, String> params,  
            Map<String, String> headers, Integer connTimeout,  
            Integer readTimeout) throws ConnectTimeoutException,  
            SocketTimeoutException, Exception {  
  
        HttpClient client = null;  
  
        HttpPost post = new HttpPost(url);  
        try {  
            if (params != null && !params.isEmpty()) {  
                List<NameValuePair> formParams = new ArrayList<org.apache.http.NameValuePair>();  
                Set<Entry<String, String>> entrySet = params.entrySet();  
                for (Entry<String, String> entry : entrySet) {  
                    formParams.add(new BasicNameValuePair(entry.getKey(), entry  
                            .getValue()));  
                }  
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(  
                        formParams, Consts.UTF_8);  
                post.setEntity(entity);  
            }  
            if (headers != null && !headers.isEmpty()) {  
                for (Entry<String, String> entry : headers.entrySet()) {  
                    post.addHeader(entry.getKey(), entry.getValue());  
                }  
            }  
            // 设置参数  
            Builder customReqConf = RequestConfig.custom();  
            if (connTimeout != null) {  
                customReqConf.setConnectTimeout(connTimeout);  
            }  
            if (readTimeout != null) {  
                customReqConf.setSocketTimeout(readTimeout);  
            }  
            post.setConfig(customReqConf.build());  
            HttpResponse res = null;  
            if (url.startsWith("https")) {  
                // 执行 Https 请求.  
                client = createSSLInsecureClient();  
                res = client.execute(post);  
            } else {  
                // 执行 Http 请求.  
                client = httpClientUtil.client;  
                res = client.execute(post);  
            }  
            return IOUtils.toString(res.getEntity().getContent(), "UTF-8");  
        } finally {  
            post.releaseConnection();  
            if (url.startsWith("https") && client != null  
                    && client instanceof CloseableHttpClient) {  
                ((CloseableHttpClient) client).close();  
            }  
        }  
    }  
  
    /** 
     * 发送一个 GET 请求 
     *  
     * @param url 
     * @param charset 
     * @return 
     * @throws Exception 
     */  
    public static String get(String url, String charset) throws Exception {  
        return get(url, charset, null, null);  
    }  
  
    /** 
     * 发送一个 GET 请求 
     *  
     * @param url 
     * @param charset 
     * @param connTimeout 
     *            建立链接超时时间,毫秒. 
     * @param readTimeout 
     *            响应超时时间,毫秒. 
     * @return 
     * @throws ConnectTimeoutException 
     *             建立链接超时 
     * @throws SocketTimeoutException 
     *             响应超时 
     * @throws Exception 
     */  
    public static String get(String url, String charset, Integer connTimeout,  
            Integer readTimeout) throws ConnectTimeoutException,  
            SocketTimeoutException, Exception {  
        HttpClient client = null;  
  
        HttpGet get = new HttpGet(url);  
        String result = "";  
        try {  
            // 设置参数  
            Builder customReqConf = RequestConfig.custom();  
            if (connTimeout != null) {  
                customReqConf.setConnectTimeout(connTimeout);  
            }  
            if (readTimeout != null) {  
                customReqConf.setSocketTimeout(readTimeout);  
            }  
            get.setConfig(customReqConf.build());  
  
            HttpResponse res = null;  
  
            if (url.startsWith("https")) {  
                // 执行 Https 请求.  
                client = createSSLInsecureClient();  
                res = client.execute(get);  
            } else {  
                // 执行 Http 请求.  
                client = httpClientUtil.client;  
                res = client.execute(get);  
            }  
  
            result = IOUtils.toString(res.getEntity().getContent(), charset);  
        } finally {  
            get.releaseConnection();  
            if (url.startsWith("https") && client != null  
                    && client instanceof CloseableHttpClient) {  
                ((CloseableHttpClient) client).close();  
            }  
        }  
        return result;  
    }  
  
    /** 
     * 从 response 里获取 charset 
     *  
     * @param ressponse 
     * @return 
     */  
    @SuppressWarnings("unused")  
    private static String getCharsetFromResponse(HttpResponse ressponse) {  
        // Content-Type:text/html; charset=GBK  
        if (ressponse.getEntity() != null  
                && ressponse.getEntity().getContentType() != null  
                && ressponse.getEntity().getContentType().getValue() != null) {  
            String contentType = ressponse.getEntity().getContentType()  
                    .getValue();  
            if (contentType.contains("charset=")) {  
                return contentType  
                        .substring(contentType.indexOf("charset=") + 8);  
            }  
        }  
        return null;  
    }  
  
    private static CloseableHttpClient createSSLInsecureClient()  
            throws GeneralSecurityException {  
        try {  
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(  
                    null, new TrustStrategy() {  
                        public boolean isTrusted(X509Certificate[] chain,  
                                String authType) throws CertificateException {  
                            return true;  
                        }  
                    }).build();  
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(  
                    sslContext, new X509HostnameVerifier() {  
  
                        public boolean verify(String arg0, SSLSession arg1) {  
                            return true;  
                        }  
  
                        public void verify(String host, SSLSocket ssl)  
                                throws IOException {  
                        }  
  
                        public void verify(String host, X509Certificate cert)  
                                throws SSLException {  
                        }  
                        public void verify(String host, String[] cns,  
                                String[] subjectAlts) throws SSLException {  
                        }  
  
                    });  
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();  
        } catch (GeneralSecurityException e) {  
            throw e;  
        }  
    }  
  
    //在微信网页中，查询用户ID
  public static String getOpenId(String CODE){
    String xml;
    try { 
  	    	String tkStr="https://api.weixin.qq.com/sns/oauth2/access_token?appid="+
  	    	APPID+"&secret="+SECRET+ "&code="+CODE+"&grant_type=authorization_code";
  	    	
  	    	xml=get(tkStr,  "utf-8", 15000, 15000);  
  	    	int i=xml.indexOf("openid");
  	    	int j=0;
  	    	if (i>0){
  	    		i=i+9;
  	    		j=xml.indexOf('"',i);
  	    		return xml.substring(i,j);
  	    		//System.out.println(xml); 
  	    	}else{
  	    		xml="0";
  	    	}
    	  } catch (Exception e) {  
    		  xml="error";
  			System.out.println(e.toString()); 
  	    } 
  	     	
		return xml;   	
    }
  
  //在返回数据头中提取文件名，然后保存文件，返回文件名
  public static String saveFile(HttpResponse res) {  
		
	    String filename=getFileName(res); 
	   
	    System.out.println(filename);
	try {
		InputStream ins=res.getEntity().getContent();
		File f=new File(filename);
		FileOutputStream fileout = new FileOutputStream(f);
		byte[] buffer=new byte[1024];
		int ch = 0;
		while ((ch=ins.read(buffer))!=-1){
			fileout.write(buffer,0,ch);
		}
		ins.close();
		fileout.flush();
		fileout.close();
		
			} catch (IllegalStateException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
			} catch (IOException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
			}
	        return filename;
	    }

  //根据MEDIA_ID下载文件，返回文件名
	public static String downloadFile(String MediaId ){  
	    
	    String filename=null;
	    HttpResponse res = null;  
	    if (TOCKEN.length()<4){
	    	getToken();
	    }
	    
	    String url="https://api.weixin.qq.com/cgi-bin/media/get?access_token="+TOCKEN.toString()+
	    "&media_id="+MediaId;
	    
	    try {
			res=getURL(url,"utf-8",15000,15000);
			filename=saveFile(res);
		} catch (ConnectTimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SocketTimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     	 	  
	    return filename;  
	}  
	
	 public static void getToken(){
		    String xml;
		    try { 
		  	    	String tkStr="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" +
		  	    				APPID+"&secret=" +SECRET; 
		  	    	xml=get(tkStr,  "utf-8", 15000, 15000);  
		  	    	int i=xml.indexOf("token");
		  	    	int j=0;
		  	    	if (i>0){
		  	    		i=i+8;
		  	    		j=xml.indexOf('"',i);
		  	    		TOCKEN.delete(0, TOCKEN.length());  	    	
		  	    		TOCKEN.append(xml.substring(i, j));
		  	    		System.out.println(xml); 

		  	    	}
		    	  } catch (Exception e) {  
		  			System.out.println(e.toString()); 
		 
		  	    } 
		  	    	
				return ;   	
		    }

	 
	 public static HttpResponse getURL(String url, String charset, Integer connTimeout,  
	            Integer readTimeout) throws ConnectTimeoutException,  
	            SocketTimeoutException, Exception {  
	        HttpClient client = null;  
	  
	        HttpGet get = new HttpGet(url);  
	        HttpResponse res = null;    
	        try {  
	            // 设置参数  
	            Builder customReqConf = RequestConfig.custom();  
	            if (connTimeout != null) {  
	                customReqConf.setConnectTimeout(connTimeout);  
	            }  
	            if (readTimeout != null) {  
	                customReqConf.setSocketTimeout(readTimeout);  
	            }  
	            get.setConfig(customReqConf.build());  
	  	  
	            if (url.startsWith("https")) {  
	                // 执行 Https 请求.  
	                client = createSSLInsecureClient();  
	                res = client.execute(get);  
	            } else {  
	                // 执行 Http 请求.  
	                client = httpClientUtil.client;  
	                res = client.execute(get);  
	            }  
	  
	           
	        } finally {  
	            get.releaseConnection();  
	            if (url.startsWith("https") && client != null  
	                    && client instanceof CloseableHttpClient) {  
	                ((CloseableHttpClient) client).close();  
	            }  
	        }  
	        return res;  
	    }  
	  
	 public static String getFileName(HttpResponse response) { 
		 org.apache.http.Header contentHeader = response.getFirstHeader("Content-disposition"); 
		 String filename = null;
		 String fname=null;
		 if (contentHeader != null) { 
		 HeaderElement[] values = contentHeader.getElements(); 
		 if (values.length == 1) { 
		 NameValuePair param = values[0].getParameterByName("filename"); 
		 if (param != null) { 
		 try { 
		 filename = param.getValue(); 
		 } catch (Exception e) { 
		 e.printStackTrace(); 
		 } 
		 } 
		 } 
		 } 
		 if (filename==null){
			 fname="c:\\downMedia\\none1";
		 }else{
			fname="c:\\downMedia\\" +filename; 
		 }
		 return fname; 
		 } 


	 
}