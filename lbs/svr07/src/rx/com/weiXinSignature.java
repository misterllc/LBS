package rx.com;

import java.security.MessageDigest;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;

public final class weiXinSignature {//微信接口验证

	//微信接口验证
	public static boolean checkSignature(HttpServletRequest final_request){
		String signature = final_request.getParameter("signature");
        String timestamp = final_request.getParameter("timestamp");
        String nonce = final_request.getParameter("nonce");
        String token="weixin";  //和微信网页设置一直
        String[] tmpArr={token,timestamp,nonce};
        Arrays.sort(tmpArr);
        String tmpStr=ArrayToString(tmpArr);
        tmpStr=SHA1Encode(tmpStr);
        if(tmpStr.equalsIgnoreCase(signature)){
			return true;
		}else{
			return false;
		}
	}

	//数0--xc 组转字符串
	public static String ArrayToString(String [] arr){
		StringBuffer bf = new StringBuffer();
		for(int i = 0; i < arr.length; i++){
		 bf.append(arr[i]);
		}
		return bf.toString();
	}
	//sha1加密
	public static String SHA1Encode(String sourceString) {
		String resultString = null;
		try {
		   resultString = new String(sourceString);
		   MessageDigest md = MessageDigest.getInstance("SHA-1");
		   resultString = byte2hexString(md.digest(resultString.getBytes()));
		} catch (Exception ex) {
		}
		return resultString;
	}
	public static final String byte2hexString(byte[] bytes) {
		StringBuffer buf = new StringBuffer(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			if (((int) bytes[i] & 0xff) < 0x10) {
		    	buf.append("0");
		   	}
			buf.append(Long.toString((int) bytes[i] & 0xff, 16));
		}
		return buf.toString().toUpperCase();
	}
	
		
}
