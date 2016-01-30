package util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class WeChatUtil {

	public static JSONObject httpRequest(String requestUrl,
			String requestMethod, String outputStr) {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			// ����SSLContext���󣬲�ʹ������ָ�������ι�������ʼ��
			TrustManager[] tm = { new TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// ������SSLContext�����еõ�SSLSocketFactory����
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url
					.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// ��������ʽ��GET/POST��
			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod))
				httpUrlConn.connect();

			// ����������Ҫ�ύʱ
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// ע������ʽ����ֹ��������
				outputStream.write(outputStr.getBytes("UTF-8"));
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
			jsonObject = JSONObject.fromObject(buffer.toString());
		} catch (ConnectException ce) {
			System.out.println("΢�ŷ��������ӳ�ʱ��");
		} catch (Exception e) {
			System.out.println("HTTPS������󣬴�����Ϣ��\n" + e.getMessage());
		}
		return jsonObject;
	}

	// ��ȡaccess_token�Ľӿڵ�ַ��GET�� ��200����/�죩
	public final static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

	/**
	 * ��ȡaccess_token
	 * 
	 * @param appid
	 *            ƾ֤
	 * @param appsecret
	 *            ��Կ
	 * @return
	 */
	public static AccessToken getAccessToken(String appid, String appsecret) {
		AccessToken accessToken = null;

		String requestUrl = access_token_url.replace("APPID", appid).replace(
				"APPSECRET", appsecret);
		JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
		// �������ɹ�
		if (null != jsonObject) {
			try {
				accessToken = new AccessToken();
				accessToken.setToken(jsonObject.getString("access_token"));
				accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
			} catch (JSONException e) {
				accessToken = null;
				// ��ȡtokenʧ��
				System.out.println("��ȡTOKENʧ��("+jsonObject.getInt("errcode")+")��"+WeChatErrorCode.ERRORCODE.get(jsonObject.getInt("errcode")));
			}
		}
		return accessToken;
	}

	// �˵�������POST�� ��100����/�죩

	public static String menu_create_url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";


	
	/**
	 * �����˵�
	 * 
	 * @param menu
	 *            �˵�ʵ��
	 * @param accessToken
	 *            ��Ч��access_token
	 * @return 0��ʾ�ɹ�������ֵ��ʾʧ��
	 */
	public static int createMenu(String jsonMenu, String accessToken) {
		int result = 0;

		// ƴװ�����˵���url
		String url = menu_create_url.replace("ACCESS_TOKEN", accessToken);
		// ���ýӿڴ����˵�
		JSONObject jsonObject = httpRequest(url, "POST", jsonMenu);

		if (null != jsonObject) {
			if (0 != jsonObject.getInt("errcode")) {
				result = jsonObject.getInt("errcode");
				System.out.println("�����˵�ʧ��("+result+")��"+WeChatErrorCode.ERRORCODE.get(result));
			}
		}
		return result;
	}

//ʹ�ýӿڴ����Զ���˵��󣬿����߻���ʹ�ýӿ�ɾ����ǰʹ�õ��Զ���˵��� 
//����˵�� 
//http����ʽ��GET
public static String menu_delete_url ="https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
	public static int deleteMenu(String accessToken) {
		int result = 0;

		// ƴװdelete�˵���url
		String url = menu_delete_url.replace("ACCESS_TOKEN", accessToken);
		// ���ýӿ�
		JSONObject jsonObject = httpRequest(url, "GET",null);

		if (null != jsonObject) {
			if (0 != jsonObject.getInt("errcode")) {
				result = jsonObject.getInt("errcode");
				System.out.println("delete�˵�ʧ��("+result+")��"+WeChatErrorCode.ERRORCODE.get(result));
			}
		}
		return result;
	}
	
}