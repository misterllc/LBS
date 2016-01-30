package util;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class WeChatErrorCode {


	public static final Map ERRORCODE=new HashMap<Integer,String>();
	
	static{
		ERRORCODE.put(-1,"ϵͳ��æ");
		ERRORCODE.put(0,"����ɹ�");
		ERRORCODE.put(40001,"��ȡaccess_tokenʱAppSecret���󣬻���access_token��Ч");
		ERRORCODE.put(40002,"���Ϸ���ƾ֤����");
		ERRORCODE.put(40003,"���Ϸ���OpenID");
		ERRORCODE.put(40004,"���Ϸ���ý���ļ�����");
		ERRORCODE.put(40005,"���Ϸ����ļ�����");
		ERRORCODE.put(40006,"���Ϸ����ļ���С");
		ERRORCODE.put(40007,"���Ϸ���ý���ļ�id");
		ERRORCODE.put(40008,"���Ϸ�����Ϣ����");
		ERRORCODE.put(40009,"���Ϸ���ͼƬ�ļ���С");
		ERRORCODE.put(40010,"���Ϸ��������ļ���С");
		ERRORCODE.put(40011,"���Ϸ�����Ƶ�ļ���С");
		ERRORCODE.put(40012,"���Ϸ�������ͼ�ļ���С");
		ERRORCODE.put(40013,"���Ϸ���APPID");
		ERRORCODE.put(40014,"���Ϸ���access_token");
		ERRORCODE.put(40015,"���Ϸ��Ĳ˵�����");
		ERRORCODE.put(40016,"���Ϸ��İ�ť����");
		ERRORCODE.put(40017,"���Ϸ��İ�ť����");
		ERRORCODE.put(40018,"���Ϸ��İ�ť���ֳ���");
		ERRORCODE.put(40019,"���Ϸ��İ�ťKEY����");
		ERRORCODE.put(40020,"���Ϸ��İ�ťURL����");
		ERRORCODE.put(40021,"���Ϸ��Ĳ˵��汾��");
		ERRORCODE.put(40022,"���Ϸ����Ӳ˵�����");
		ERRORCODE.put(40023,"���Ϸ����Ӳ˵���ť����");
		ERRORCODE.put(40024,"���Ϸ����Ӳ˵���ť����");
		ERRORCODE.put(40025,"���Ϸ����Ӳ˵���ť���ֳ���");
		ERRORCODE.put(40026,"���Ϸ����Ӳ˵���ťKEY����");
		ERRORCODE.put(40027,"���Ϸ����Ӳ˵���ťURL����");
		ERRORCODE.put(40028,"���Ϸ����Զ���˵�ʹ���û�");
		ERRORCODE.put(40029,"���Ϸ���oauth_code");
		ERRORCODE.put(40030,"���Ϸ���refresh_token");
		ERRORCODE.put(40031,"���Ϸ���openid�б�");
		ERRORCODE.put(40032,"���Ϸ���openid�б���");
		ERRORCODE.put(40033,"���Ϸ��������ַ������ܰ���\\uxxxx��ʽ���ַ�");
		ERRORCODE.put(40035,"���Ϸ��Ĳ���");
		ERRORCODE.put(40038,"���Ϸ��������ʽ");
		ERRORCODE.put(40039,"���Ϸ���URL����");
		ERRORCODE.put(40050,"���Ϸ��ķ���id");
		ERRORCODE.put(40051,"�������ֲ��Ϸ�");
		ERRORCODE.put(41001,"ȱ��access_token����");
		ERRORCODE.put(41002,"ȱ��appid����");
		ERRORCODE.put(41003,"ȱ��refresh_token����");
		ERRORCODE.put(41004,"ȱ��secret����");
		ERRORCODE.put(41005,"ȱ�ٶ�ý���ļ�����");
		ERRORCODE.put(41006,"ȱ��media_id����");
		ERRORCODE.put(41007,"ȱ���Ӳ˵�����");
		ERRORCODE.put(41008,"ȱ��oauth code");
		ERRORCODE.put(41009,"ȱ��openid");
		ERRORCODE.put(42001,"access_token��ʱ");
		ERRORCODE.put(42002,"refresh_token��ʱ");
		ERRORCODE.put(42003,"oauth_code��ʱ");
		ERRORCODE.put(43001,"��ҪGET����");
		ERRORCODE.put(43002,"��ҪPOST����");
		ERRORCODE.put(43003,"��ҪHTTPS����");
		ERRORCODE.put(43004,"��Ҫ�����߹�ע");
		ERRORCODE.put(43005,"��Ҫ���ѹ�ϵ");
		ERRORCODE.put(44001,"��ý���ļ�Ϊ��");
		ERRORCODE.put(44002,"POST�����ݰ�Ϊ��");
		ERRORCODE.put(44003,"ͼ����Ϣ����Ϊ��");
		ERRORCODE.put(44004,"�ı���Ϣ����Ϊ��");
		ERRORCODE.put(45001,"��ý���ļ���С��������");
		ERRORCODE.put(45002,"��Ϣ���ݳ�������");
		ERRORCODE.put(45003,"�����ֶγ�������");
		ERRORCODE.put(45004,"�����ֶγ�������");
		ERRORCODE.put(45005,"�����ֶγ�������");
		ERRORCODE.put(45006,"ͼƬ�����ֶγ�������");
		ERRORCODE.put(45007,"��������ʱ�䳬������");
		ERRORCODE.put(45008,"ͼ����Ϣ��������");
		ERRORCODE.put(45009,"�ӿڵ��ó�������");
		ERRORCODE.put(45010,"�����˵�������������");
		ERRORCODE.put(45015,"�ظ�ʱ�䳬������");
		ERRORCODE.put(45016,"ϵͳ���飬�������޸�");
		ERRORCODE.put(45017,"�������ֹ���");
		ERRORCODE.put(45018,"����������������");
		ERRORCODE.put(46001,"������ý������");
		ERRORCODE.put(46002,"�����ڵĲ˵��汾");
		ERRORCODE.put(46003,"�����ڵĲ˵�����");
		ERRORCODE.put(46004,"�����ڵ��û�");
		ERRORCODE.put(47001,"����JSON/XML���ݴ���");
		ERRORCODE.put(48001,"api����δ��Ȩ");
		ERRORCODE.put(50001,"�û�δ��Ȩ��api");
	}

}