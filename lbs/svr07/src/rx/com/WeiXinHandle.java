package rx.com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class WeiXinHandle{//---������ʱ����
	
	private HttpServletRequest final_request;
	private HttpServletResponse final_response;
	private Document document=null;
	private Element root=null;
    private String fromUsername = null;
    private String toUsername = null;
    private String msgType=null;
    private String MediaId=null;
    
   
    public WeiXinHandle(HttpServletRequest request,HttpServletResponse response){
    	final_request=request;
    	final_response=response;
    }
	
	
	//�Զ��ظ�����
	public  void responseMsg(){
		String postStr=null;
		try{
			postStr=readStreamParameter(final_request.getInputStream());
		}catch(Exception e){
			e.printStackTrace();
		}
		//System.out.println(postStr);
		if (null!=postStr&&!postStr.isEmpty()){
			
			try{
				document = DocumentHelper.parseText(postStr);
			}catch(Exception e){
				e.printStackTrace();
			}
			if(null==document){
				wechatMsg("");
				return;
			}
			root=document.getRootElement();
            fromUsername = root.elementText("FromUserName");
            toUsername = root.elementText("ToUserName");
            msgType=root.elementText("MsgType");
          
            if(msgType.compareTo("text")==0) {
            	answerMsg();
            }
            else if(msgType.compareTo("event")==0) {
            	answerEvent();
            }
            else if(msgType.compareTo("voice")==0) {
            	MediaId=root.elementText("MediaId");
            	answerVoice();
            }
		}else {
			wechatMsg("");
	    }
           
	}
	
	//
	private void answerVoice(){
		rx.com.httpClientUtil.downloadFile(MediaId);
		 wechatMsg("");
	}
	
	//�����û����͵���ͨ��Ϣ���ı�....
	private void answerMsg(){
				
		 String keyword = root.elementTextTrim("Content");
         
			if(null!=keyword&&!keyword.equals(""))
         {
				 wechatMsg("wait a moment,please!");
         }else{
        	 wechatMsg("");
         }

	    
	}
	
	//�����¼����ͣ���ע���˵���ɨ��......
	private void answerEvent(){
		String eventKind = root.elementText("Event");
//		String keyValue = root.elementText("EventKey");
//�û�δ��עʱ�����й�ע����¼�����  �¼�����subscribe , EventKey�¼�KEYֵ��qrscene_Ϊǰ׺������Ϊ��ά��Ĳ���ֵ  
//�û��ѹ�עʱ���¼������¼����ͣ�SCAN  ;EventKey�¼�KEYֵ����һ��32λ�޷�����������������ά��ʱ�Ķ�ά��scene_id  
//Event  �¼����ͣ�subscribe(����)��unsubscribe(ȡ������)  
//����˵���ȡ��Ϣʱ���¼�����,����˵������Ӳ˵������������.
//�¼����ͣ�CLICK  		EventKey  �¼�KEYֵ�����Զ���˵��ӿ���KEYֵ��Ӧ 
//�˵���ת����ʱ���¼����� �¼����ͣ�VIEW  		EventKey  �¼�KEYֵ�����õ���תURL  
		 if(eventKind.compareTo("subscribe")==0) {
	         wechatMsg("Welcome ...");
         }
         else if(eventKind.compareTo("SCAN")==0) {
        	 wechatMsg("");
         }
         else if(eventKind.compareTo("CLICK")==0) {
        	 wechatMsg("Can I help you?");
          }
         else if(eventKind.compareTo("VIEW")==0) {
        	 wechatMsg("");
          }
         else if(eventKind.compareTo("unsubscribe")==0) {
        	 wechatMsg("");
             }
		else {
			 wechatMsg("");
	    }
		
	}
	
	
	//����������ȡpost����
	private String readStreamParameter(ServletInputStream in){
		StringBuilder buffer = new StringBuilder();
		BufferedReader reader=null;
		try{
			reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
			String line=null;
			while((line = reader.readLine())!=null){
				buffer.append(line);
	        }
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=reader){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return buffer.toString();
	}
	
private void wechatMsg(String msgText){
		String time = null;
		
		String textTpl=null;
		if (msgText.length()<2){
			textTpl = "";
		}
		else {
		time = new Date().getTime()+"";
		textTpl = 	"<xml><ToUserName><![CDATA[" + fromUsername +
		"]]></ToUserName><FromUserName><![CDATA[" + toUsername +
		"]]></FromUserName><CreateTime>" + time +
		"</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[" + msgText +
		"]]></Content><FuncFlag>0</FuncFlag></xml>";
		}
		
		try{
			final_response.getWriter().print(textTpl);
			final_response.getWriter().flush();
			final_response.getWriter().close();
		}catch(Exception e){
			
		}
		return ;
}

}