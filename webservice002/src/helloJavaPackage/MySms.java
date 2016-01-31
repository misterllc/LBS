package helloJavaPackage;

import java.util.Hashtable;

import com.todaynic.client.mobile.SMS;

public class MySms {
	
//	private ArrayList<Msg> msgList=new ArrayList<Msg>();
//	private String lock="0";
	
	public MySms(){
		smsThreadStart();
	}
	
	class Msg {
		public String tele ;
		public String txt;
		public Msg(String m_tele,String m_txt ){
			tele=new String(m_tele);
			txt=new String(m_txt);
		}
	}
	
	private void sms(Msg m)
	{			
			
	//if (msgList.size()>0) 	
	//{
	
		String SendTime="0";	 //即时发送			
		String type="3";     //通道选择: 0：默认通道； 2：通道2； 3：即时通道
		//m=msgList.get(0);
		SMS smsSender=new SMS(configTable);
		
	//	System.out.println("sending a msg...");  //==
		
		try {
			smsSender.sendSMS(m.tele,m.txt,SendTime,type);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		synchronized(lock) {
//			msgList.remove(0);
//		 }
	//}
		
		 return;
	}
	
	  public void put_sms(String m_tele,String m_txt)
	   {
		  if ((m_tele.length()==11) && (m_tele.charAt(0)=='1'))
//		   synchronized(lock) 
//		   {
//			   msgList.add(new Msg(m_tele,m_txt));
//		   }
		   //if(workThread==null)
		   {
			   WorkThread   workThread=new WorkThread(new Msg(m_tele,m_txt));  // "SMS_send_thread"); 		  
		        workThread.start(); 
			}
		//   System.out.println(" a msg want to send...");  //==
	   }
	  
	//private WorkThread workThread=null;
	Hashtable<String, String> configTable=new Hashtable<String, String>();
	private  void smsThreadStart(){
		
		configTable.put("VCPSERVER","sms.todaynic.com");
		configTable.put("VCPSVPORT","20002");
		configTable.put("VCPUSERID","ms95393");
		configTable.put("VCPPASSWD","mtc0mt");
	
		
//		if(workThread==null){
//	        workThread=new WorkThread("SMS_send_thread"); 		  
//	        workThread.start(); 
//		}
		
	    } 

	class WorkThread extends Thread{ 
		private Msg m;
	   WorkThread(Msg msg) { //String name){ 
	        super("sms_thread");//调用父类带参数的构造方法 
	        m=msg;
	    } 
	    
	    public void run(){ 
		
				//sms(m_msg);
	    	String SendTime="0";	 //即时发送			
			String type="3";     //通道选择: 0：默认通道； 2：通道2； 3：即时通道
			//m=msgList.get(0);
			SMS smsSender=new SMS(configTable);
			
		//	System.out.println("sending a msg...");  //==
			
			try {
				smsSender.sendSMS(m.tele,m.txt,SendTime,type);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    } 
	} 


	
	   
	   	
}
