package rx.com;
import java.sql.*;
import java.util.ResourceBundle;

public class mydata_old {	
//	private String url ="jdbc:mysql://113.107.219.97:3306/rxdb?";//useUnicode=true&characterEncoding=gb2312";//?user=root&password=monday"; 
	private Statement stmt = null;
    private Connection conn=null;
    private ResultSet rs = null;


    
    public String example(String message) {
		  return "hello, "+message; 		  
	   } 
    //openid_test_2==2
    //f_motor_openid_loc`(oid char(64),dest_serial integer) RETURNS text
    //f_motor_openid_add_task2`(oid char(64),cmdstr char(252),m_kind char) RETURNS int(11)准备下发报文
  //String result=gsmLBS.getLocFromGSM("accesstype=1&imei=352315052834888&mmac=bc:f6:85:da:73:43,-55,lgy");
 
public String get_motor_openid_location2(String oid,Integer sno) {  //只取一条记录
    	
		String sql="select f_motor_openid_loc2('" +oid +"'," +sno +");";

		init_db();
		   rs=query(sql);
		   try {
				rs.next();	
				return rs.getString(1);		
		   } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				db_close();
				return "0";
			}
} 
    
 public String get_motor_openid_location(String oid,Integer sno) {
    	
		String sql="select f_motor_openid_loc('" +oid +"'," +sno +");";
		String rslt;
		StringBuilder gps=new StringBuilder();
		int i,j=0;
		init_db();
		   rs=query(sql);
		   try {
				if (rs.next()) {
					rslt=rs.getString(1);
					if ((rslt.charAt(0)=='-')||(rslt.charAt(0)=='0')){
						return rslt;
					}
					i=rslt.indexOf("GPS=");
					while (i>0){
						i=i+4;
						j=rslt.indexOf('&',i);
						gps.append(rslt.substring(i,j));
						i=rslt.indexOf("GPS=",j);
						if(i>0) {gps.append(';');}
					}
					gps.append(rslt.substring(j));
					i=rslt.indexOf("GPS=");
					gps.append(rslt.substring(0,i));
					return gps.toString();
				}
		   } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "none";
			}
		   
	return "nothing";
} 
//f_motor_openid_read`(oid char(64)) RETURNS char(254)读出参数表
 public  String get_motor_set(String oid)
	{
		String sql="select f_motor_openid_read('" +oid +"');";
		init_db();
		   rs=query(sql);
		   try {
				if (rs.next()) {
					return rs.getString(1);
				}
		   } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   
			return "MASTER=1";
	}
  
//f_motor_openid_set`(oid char(64),parastr char(252)) RETURNS int(11)写入参数表
 public  String save_motor_set(String oid,String parastr)
	{
		String sql="select f_motor_openid_set('" +oid +"','" +parastr+ "');";
		init_db();
		   rs=query(sql);
		   try {
				if (rs.next()) {
					return rs.getString(1);
				}
		   } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   
			return "0=error";
	}

 //f_motor_openid_add_task2`(oid char(64),cmdstr char(252),m_kind char) RETURNS int(11)准备下发报文
 public  String save_motor_cmd(String oid,String parastr)
	{
		String sql="select f_motor_openid_add_task2('" +oid +"','" +parastr+ "','s');";
		init_db();
		   rs=query(sql);
		   try {
				if (rs.next()) {
					return rs.getString(1);
				}
		   } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   
			return "0=error";
	}
 
    public String get_motor_location(Integer cid,Integer sno) {
    	
		String sql="select f_motor_get_loc(" +cid +"," +sno +");";
		String rslt;
		StringBuilder gps=new StringBuilder();
		int i,j;
		init_db();
		   rs=query(sql);
		   try {
				if (rs.next()) {
					rslt=rs.getString(1);
					i=rslt.indexOf("GPS=");
					while (i>0){
						i=i+4;
						j=rslt.indexOf('&',i);
						gps.append(rslt.substring(i,j));
						i=rslt.indexOf("GPS=",j);
						if(i>0) {gps.append(';');}
					}
					return gps.toString();
				}
		   } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "none";
			}
		   
	return "nothing";
} 
	
    public  String get_cid(String phone,String passwd)
	{
		String sql="select card_id from card_login where phone='"+phone +"'and password='"+passwd+"';";
		init_db();
		   rs=query(sql);
		   try {
				if (rs.next()) {
					return rs.getString(1);
				}
		   } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   
			return "no";
	}
    
    public  Integer get_cid_main(long cid)//取主控电话后5位，作为WAP登录凭证
	{
		String sql="select phone from card_login where card_id="+cid +"  limit 1;";
		init_db();
		   rs=query(sql);
		   try {
				if (rs.next()) {
					return Integer.valueOf(rs.getString(1).substring(6, 11));
				}
		   } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   
			return 0;
	}
    public  String get_control_phone(long cid)
	{
		String sql="select phone from card_login where card_id="+cid +"  limit 1;";
		init_db();
		   rs=query(sql);
		   try {
				if (rs.next()) {
					return rs.getString(1)+";"+rs.getString(1)+";;;;;;;;;;;;;;;;;;;;;";
				}
		   } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   
			return ";;;;;;;;;;;;;;;;;;;;;;";
	}
    
    public  String get_control_phone2(long cid)
	{
		String sql="select phone from card_login where card_id="+cid +"  limit 1;";
		init_db();
		   rs=query(sql);
		   try {
				if (rs.next()) {
					return rs.getString(1);
				}
		   } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   
			return " ";
	}
    
	public String get_card_location(Integer cid,Integer sno) {
	
			String sql="select f_get_location(" +cid +"," +sno +");";
			init_db();
			   rs=query(sql);
			   try {
					if (rs.next()) {
						return rs.getString(1);
					}
			   } catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return "none";
				}
			   
		return "nothing";
	} 
	
	public String get_card_telephone(long cid) {
		
		String sql="select tele from card_parameter where card_id=" +cid +" ;";
		init_db();
		   rs=query(sql);
		   try {
				if (rs.next()) {
					return rs.getString(1);
				}
				else
				{
					return get_control_phone(cid);
				}
		   } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   
	return ",,,,,,,,,,,,,,,,,,,,,,,,,,,nothing";
} 

public String get_card_set(long cid) {
//		flags=ring_type,sos_enable,class_enable
	String sql="select flags,class  from card_parameter where card_id=" +cid +";";
		init_db();
		   rs=query(sql);
		   try {
				if (rs.next()) {
					return rs.getString(1)+rs.getString(2);
				}
		   } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "none";
			}
		   
	return ",,,,,,,,,,,,,,,,,nothing";
} 
	
public String get_card_clock(long cid) {
	
	String sql="select clock from card_parameter where card_id=" +cid +";";
	init_db();
	   rs=query(sql);
	   try {
			if (rs.next()) {
				return rs.getString(1); 
			}
	   } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "none";
		}
	   
return ",,,,,,,,,,,,,,,,,,,nothing";
} 

	public String broadcast_message(String userid, String msg) {
		String sql="SELECT f_insert_broadcast('" +userid+ "','" +msg +"');" ;
		init_db();
	//	DataSource ds;
		
		   rs=query(sql);
		   try {
				if (rs.next()) {
					return "ok";
				}
		   } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return "error";
	}




	public String send_message(String userid, String receiver, String msg) {
		String sql="SELECT f_insert_receive_box('"+receiver +"','" +msg +"','"+userid +"','-');";
		init_db();
		   rs=query(sql);
		   try {
				if (rs.next()) {
					return "ok";
				}
		   } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return "error";
	}



	
	private void init_db(){
			//if(dogThread==null){
			//	dog_thread();
		//	}

		if(conn!=null) return;
		ResourceBundle my_resources; 
		my_resources = ResourceBundle.getBundle("connect"); 
		String host=my_resources.getString("host");
		String db_url="jdbc:mysql://"+host +":3306/rxdb?";
		String db_user=my_resources.getString("user");
		String db_passwd=my_resources.getString("passwd");
		
		try{
		       Class.forName("com.mysql.jdbc.Driver").newInstance();//装载数据库
		       }
		       catch(ClassNotFoundException error){
		        System.err.println("Unable to load the JDBC/MYSQL driver."+error.getMessage());
		       System.exit(1);
		       } catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try{
				conn = DriverManager.getConnection(db_url,db_user,db_passwd); 
		       }
		       catch(SQLException error)
		       {
		           System.err.println("Cannot connect to the database."+error);
		           System.exit(2);
		       }
		
		try {
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	
	private void db_close(){
		try {
			stmt.close();
			conn.close();
			conn=null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	 private  ResultSet query(String sql) {// 查询数据库
		 
		 try {
				rs = stmt.executeQuery(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} //
		  return rs;
		 }
	 
	 private int update_database(String sql) {// 更新或插入数据库
		  int num = 0;
		  try {
		   num = stmt.executeUpdate(sql);
		  } catch (SQLException ex) {
		   System.err.println("执行插入有错误:" + ex.getMessage());
		   System.out.print("执行插入有错误:" + ex.getMessage());// 输出到客户端
		  }
		  return num;
		 }
	
	 private String set_term_task(long cid, int code) {
			String sql="SELECT f_add_task(" +cid+ "," +code +");" ;
			init_db();
			   rs=query(sql);
			   try {
					if (rs.next()) {
						return "ok";
					}
			   } catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			return "error";
		}


	
	
	 public int update_card_set(int cid,Integer[] phones )
	 {
	 	String sql="REPLACE INTO card_set VALUES("+cid+","+
	 phones[0]+","+	phones[1]+","+	phones[2]+",'"+	phones[3]+";"+	phones[4]+"','"+	
	 phones[5]+";"+	phones[6]+"');";
	 	int num=0;
	 	
	 	num=update_database(sql);
	 	set_term_task(cid,2);
	 	return num;
	 }
	 
	 public int update_card_clock(int cid,Integer[] tm)
	 {
	 	String sql="REPLACE INTO card_clock VALUES("+cid+","+
	 tm[0]+","+	tm[1]+","+	tm[2]+","+	tm[3]+","+	tm[4]+","+	
	 tm[5]+","+	tm[6]+","+tm[7]+");";
	 	int num=0;
	 	
	 	num=update_database(sql);
	 	set_term_task(cid,3);
	 	return num;
	 }
	 
	 public int update_card_phone2(int cid,String phones )
	 {
	 	String sql="update card_parameter set tele='" +phones + "'  where  card_id=" +cid+";";
	 	int num=0;
	 	
	 	num=update_database(sql);
	 	set_term_task(cid,1);
	 	return num;
	 }
	 
	 public int update_card_set2(int cid,String flg,String timestr )
	 {
	 	String sql="update card_parameter set flags='"+flg+ "' ,class='" +timestr + "'  where  card_id=" +cid+";";
	 	int num=0;
	 	
	 	num=update_database(sql);
	 	set_term_task(cid,2);
	 	return num;
	 }
	 
	 public int update_card_clock2(int cid,String tm )
	 {
	 	String sql="update card_parameter set clock='" +tm + "'  where  card_id=" +cid+";";
	 	int num=0;
	 	
	 	num=update_database(sql);
	 	set_term_task(cid,3);
	 	return num;
	 }
	 	
	
}// EOF class


