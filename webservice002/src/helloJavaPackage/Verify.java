package helloJavaPackage;
//生成验证码
public class Verify {
public String userNo;
public int code;
public long timestamp;

public Verify(String tele,String mcode,long tm){
	userNo=new String(tele);
	timestamp=tm;
	code=Integer.parseInt(mcode);	
}

}
