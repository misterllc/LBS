package rx.com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rx.com.MotorData;

public class WxHandler{
	private MotorData motorData2 =new MotorData();
		
	//�Զ��ظ�����
	public  void responseMsg(HttpServletRequest request,HttpServletResponse response){

	     String msgType=null;
		 String postStr=null;

		try{
			postStr=readStreamParameter(request.getInputStream());
			
			if (null==postStr||postStr.isEmpty()){
				wxResponseNULL(response);
				return;
			}
	
				msgType=getWxElement(postStr,"MsgType");
				
				if(msgType.compareTo("text")==0) {
	            	answerMsg(response,postStr);
	            }
	            else if(msgType.compareTo("event")==0) {
	            	answerEvent(response,postStr);
	            }
	            else if(msgType.compareTo("voice")==0) {
	            	answerVoice(response,postStr);
	            }
	            else {
	            	wxResponseNULL(response);
	            }				
		
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return;          
	}
	
	//
	private void answerVoice(HttpServletResponse final_response,String root){
//		String MediaId=root.elementText("MediaId");
//		rx.com.httpClientUtil.downloadFile(MediaId);
		//String test=DogThread.token;
		//System.out.print(test);    //test=======
		wxResponseNULL(final_response);
	}
	
	//�����û����͵���ͨ��Ϣ���ı�....
	private void answerMsg(HttpServletResponse final_response,String root){
				
		 String keyword =getWxElement(root,"Content");
		 String openId = getWxElement(root,"FromUserName");
         String cmdStr="CMD=" +keyword;
			if(null!=keyword&&!keyword.equals(""))
         {
				wechatMsg(final_response,root,"ָ�����ڷ��͵��豸...");
				if(keyword.length()<60) {motorData2.save_motor_cmd(openId,cmdStr);}				 
         }else{
        	 wxResponseNULL(final_response);
         }

	    
	}
	
	//�����¼����ͣ���ע���˵���ɨ��......
	private void answerEvent(HttpServletResponse final_response,String root){
		String eventKind = getWxElement(root,"Event");
//		String keyValue = root.elementText("EventKey");
//�û�δ��עʱ�����й�ע����¼�����  �¼�����subscribe , EventKey�¼�KEYֵ��qrscene_Ϊǰ׺������Ϊ��ά��Ĳ���ֵ  
//�û��ѹ�עʱ���¼������¼����ͣ�SCAN  ;EventKey�¼�KEYֵ����һ��32λ�޷�����������������ά��ʱ�Ķ�ά��scene_id  
//Event  �¼����ͣ�subscribe(����)��unsubscribe(ȡ������)  
//����˵���ȡ��Ϣʱ���¼�����,����˵������Ӳ˵������������.
//�¼����ͣ�CLICK  		EventKey  �¼�KEYֵ�����Զ���˵��ӿ���KEYֵ��Ӧ 
//�˵���ת����ʱ���¼����� �¼����ͣ�VIEW  		EventKey  �¼�KEYֵ�����õ���תURL  
		 if(eventKind.compareTo("subscribe")==0) {
	         wechatMsg(final_response,root,"Welcome ...");
         }
         else if(eventKind.compareTo("SCAN")==0) {
        	 wxResponseNULL(final_response);
         }
         else if(eventKind.compareTo("CLICK")==0) {
        	// wechatMsg(final_response,root,"������ָ�ϡ����ڱ�����...");
        	 answerClick(final_response,root);
          }
         else if(eventKind.compareTo("VIEW")==0) {
        	 wxResponseNULL(final_response);
          }
         else if(eventKind.compareTo("unsubscribe")==0) {
        	 wxResponseNULL(final_response);
        	 String openId = getWxElement(root,"FromUserName");
        	 String delSQL="delete from motor_openid where openid='"+openId +"';";
        	 motorData2.dsUpdate(delSQL);
        	 
             }
		else {
			wxResponseNULL(final_response);
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
	
private void wechatMsg(HttpServletResponse final_response,String root,String msgText){
		String time = null;		
		String textTpl=null;
        String fromUsername = getWxElement(root,"FromUserName");
        String toUsername = getWxElement(root,"ToUserName");
        	
		time = new Date().getTime()+"";
		textTpl = 	"<xml><ToUserName><![CDATA[" + fromUsername +
		"]]></ToUserName><FromUserName><![CDATA[" + toUsername +
		"]]></FromUserName><CreateTime>" + time +
		"</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[" + msgText +
		"]]></Content><FuncFlag>0</FuncFlag></xml>";
	
		try{
			final_response.getWriter().print(textTpl);
			final_response.getWriter().flush();
			final_response.getWriter().close();
		}catch(Exception e){
			
		}
		return ;
}

private void wxResponseNULL(HttpServletResponse final_response){
	try{
		final_response.getWriter().print("");
		final_response.getWriter().flush();
		final_response.getWriter().close();
	}catch(Exception e){
		
	}
	return ;
}

/*
<xml>
 <ToUserName><![CDATA[toUser]]></ToUserName>
 <FromUserName><![CDATA[fromUser]]></FromUserName> 
 <CreateTime>1348831860</CreateTime>
 <MsgType><![CDATA[text]]></MsgType>
 <Content><![CDATA[this is a test]]></Content>
 <MsgId>1234567890123456</MsgId>
 </xml>*/
private String getWxElement(String src,String eName){
	int i,j,l;
	String rslt=null;
	l=eName.length();
	i=src.indexOf(eName);
	if(i>=0){
		i=i+l+10;
		j=src.indexOf("]]>", i);
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

//�¼����ͣ�CLICK  		EventKey  �¼�KEYֵ�����Զ���˵��ӿ���KEYֵ��Ӧ 
private void answerClick(HttpServletResponse final_response,String root){

	String keyValue = getWxElement(root,"EventKey");
	 String openId = getWxElement(root,"FromUserName");
     String cmdStr=null ;  //"CMD=";
     String echoStr=null ; 
     String rslt=null;

     
	 if(keyValue.compareTo("V_stop")==0) {
         echoStr="ǿ������ָ��͵��豸...";
         cmdStr="CMD=stop";
     }
     else if(keyValue.compareTo("V_go")==0) {
    	 echoStr="�ָ�ָ��͵��豸...";
    	 cmdStr="CMD=go";
     }
     else if(keyValue.compareTo("V_reset")==0) {
    	 echoStr="�Ͽ�΢�ź��豸�Ĺ���ָ�����..."; 
    	 cmdStr="CMD=reset";
      }
     else if(keyValue.compareTo("V_acct")==0) {
    	 echoStr="���ڲ�ѯ��������ڲ鿴����ʾ..."; 
    	 cmdStr="CMD=cxye";
     }
     else if(keyValue.compareTo("V_help")==0) {
    	 echoStr="������ָ�ϡ����ڱ�����...";
      }
     else {
    	 wxResponseNULL(final_response);
    	 return;
         }
	 
	 if (cmdStr!=null){
		 rslt=motorData2.save_motor_cmd(openId,cmdStr);
	 } else {
		 rslt="1";
	 }
	 
	 if (rslt.charAt(0)=='1'){
		 wechatMsg(final_response,root,echoStr);
	 }else {
		 wechatMsg(final_response,root,"δ�����豸���߷����أ�ָ����Ч��"); 
	 }
}

}