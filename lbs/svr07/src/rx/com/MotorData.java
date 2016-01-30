package rx.com;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class MotorData {
    private Connection dsConn = null;
    private Statement dsStmt = null;
    private DataSource ds=null;
    
	private void init_ds(){
		Context initCtx;
		try {
			initCtx = new InitialContext();
			ds =(DataSource)initCtx.lookup("java:comp/env/jdbc/mysqlds");					
		} catch (NamingException e1) {
			e1.printStackTrace();
		} 
		return;		
	}
//	int result = stmt.executeUpdate(sql);//语句会返回一个受影响的行数，如果返回-1就没有成功
	public  int dsUpdate(String sql){
		int rslt=0;
		
		if (ds==null){init_ds();}
		
		try {
			dsConn=ds.getConnection();		
			dsStmt=dsConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (dsConn==null){
			return 0;
		}
		   try {
				rslt=dsStmt.executeUpdate(sql);
				dsStmt.close();
				dsConn.close();
				dsConn=null;
				dsStmt=null;	
		   } catch (SQLException e) {
				e.printStackTrace();
				rslt=0;
			}
		   
		   return rslt;
	}
		
public  String dsQuery(String sql){
	String rslt=null;
	
	if (ds==null){init_ds();}
	
	try {
		dsConn=ds.getConnection();		
		dsStmt=dsConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
	} catch (SQLException e) {
		e.printStackTrace();
	}
	if (dsConn==null){
		return "0";
	}
	   try {
			ResultSet rs1=dsStmt.executeQuery(sql);
			if (rs1.next()) {
				rslt= rs1.getString(1);
			}else{
				rslt="0";
			}
			rs1.close();
			dsStmt.close();
			dsConn.close();
			dsConn=null;
			dsStmt=null;	
	   } catch (SQLException e) {
			e.printStackTrace();
			rslt="0";
		}
	   
	   return rslt;
}
	
public String get_motor_openid_location2(String oid,Integer sno) {  //只取一条记录
    	
		String sql="select f_motor_openid_loc2('" +oid +"'," +sno +");";
		String rslt=null;
		
		rslt=dsQuery(sql);		
		return rslt;
} 

//按照时间读取位置SELECT f_motor_oid_tm_loc(openId,regDay); string,string
//f_motor_cid_tm_loc`(cid int(10),reg_day char(20)) RETURNS text
//f_motor_web_loc2`(cid int(10),dest_serial int(11)) RETURNS text
//最近位置，两条记录f_motor_oid_latest_loc`(oid char(64)) RETURNS text
//f_motor_cid_latest_loc`(cid int(10)) RETURNS text

//f_motor_openid_read`(oid char(64)) RETURNS char(254)读出参数表
public  String get_motor_set(String oid)
	{
		String sql="select f_motor_openid_read('" +oid +"');";
		String rslt=null;
		
		rslt=dsQuery(sql);		
		return rslt;
	}

//f_motor_openid_set`(oid char(64),parastr char(252)) RETURNS int(11)写入参数表
public  String save_motor_set(String oid,String parastr)
	{
		String sql="select f_motor_openid_set('" +oid +"','" +parastr+ "');";
		String rslt=null;
		
		rslt=dsQuery(sql);		
		return rslt;
	}

//f_motor_openid_add_task2`(oid char(64),cmdstr char(252),m_kind char) RETURNS int(11)准备下发报文
public  String save_motor_cmd(String oid,String parastr)
	{
		String sql="select f_motor_openid_add_task2('" +oid +"','" +parastr+ "','s');";
		String rslt=null;
		
		rslt=dsQuery(sql);		
		return rslt;
	}

//f_motor_openid_relation`(oid char(64),imeistr char(20)) RETURNS char(1)
public  String save_motor_relation(String oid,String IMEIstr)
{
	String sql="select f_motor_openid_relation('" +oid +"','" +IMEIstr+ "');";
	String rslt=null;
	if (oid.length()<12 || oid.length()>36 ||IMEIstr.length()!=15) {
		rslt="0";
	} else {
		rslt=dsQuery(sql);	
	}
		
	return rslt;
}

public String qry_time()
	{
		String sql="select current_timestamp;";
		String rslt=null;
		
		rslt=dsQuery(sql);
		return rslt;
	}

//f_motor_urgent_task`() RETURNS text
public String get_urgent_task()
{
	String sql="select f_motor_urgent_task();";
	String rslt=null;
	
	rslt=dsQuery(sql);
	return rslt;
}

}
