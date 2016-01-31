package helloJavaPackage;

import java.util.ArrayList;

public class MyVerify {
	private ArrayList<Verify> userList;
	private long  timeout_value;
	private int rand_seed=921;
	private MySms mySms=new MySms();
	
	public MyVerify(int timeout_minutes){
		userList=new ArrayList<Verify>();
		timeout_value=timeout_minutes *60*1000;
	}

	public String addUser(String mUser,boolean sms_verify){
		long tm=System.currentTimeMillis();
		int rand=(int) (tm&0x03FF);
		rand_seed=(rand<<10) +rand_seed;
		if(rand_seed>1000000) rand_seed=(rand_seed>>1)+301645;
		String mCode=new String(String.format("%1$06d", rand_seed));
		rand_seed=rand;
		int i=0;
		delete_timeout();
		i=getIndexByName(mUser);
		if(i<0){
		userList.add(new Verify(mUser,mCode,tm));
		}
		else{
			userList.get(i).code=Integer.parseInt(mCode);
			userList.get(i).timestamp=tm;
		}
			
		if(sms_verify) send_sms(mUser,mCode);  //发送短信验证码
		return mCode;
	}
	
	public int check(String mUser,String mCode){  //验证码不正确返回-2，否则返回0
		int sz=userList.size();		
		int i=0;
		int  ret= -1; 
		int cookie=Integer.parseInt(mCode);
		long tm=System.currentTimeMillis();
		for (i=0;i<sz;i++){
			if(mUser.compareTo(userList.get(i).userNo)==0){
				if(cookie==userList.get(i).code){
					ret=0; //(int)(tm -userList.get(i).timestamp)/1000; 
					userList.get(i).timestamp=tm;  //最近访问时间
				}
				else{
					ret =-2;
				}
				break;
			}
		}
			
		return ret;
	}
	
	private int getIndexByName(String mUser){
		int sz=userList.size();	
		int i=0;
		for(i=0;i<sz;i++){
			if(mUser.compareTo(userList.get(i).userNo)==0){
				break;
			}
		}
			if(i>=sz) i=-1;
			return i;			
	}
	
	private void delete_timeout(){
		int sz=userList.size();		
		long tm=System.currentTimeMillis()-timeout_value;
		for(int i=0;i<sz;i++){
			if(userList.get(i).timestamp<tm){
				userList.remove(i);
				break;
			}
		}
	}
	
	private void send_sms(String tele,String msg){
		mySms.put_sms(tele, "你的验证码为："+msg+"，两江四湖景区售票处");
	}
	
}
