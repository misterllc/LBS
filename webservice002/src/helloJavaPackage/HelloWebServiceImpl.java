package helloJavaPackage;
import java.sql.*;
import java.text.SimpleDateFormat;  
import java.util.Date;
import java.util.ResourceBundle;


public class HelloWebServiceImpl implements IHelloWebService {
	
	//private String url ="jdbc:mysql://172.16.1.51:3306/mylife?";//useUnicode=true&characterEncoding=gb2312";//?user=root&password=monday"; 
	private Statement stmt = null;
    private Connection conn=null;
    private ResultSet rs = null;
    private DogThread dogThread=null;
    private StringBuffer userCookies=new StringBuffer();
  
	private static ResourceBundle my_resources; 
	 
	private  MyVerify mobileVerify=new MyVerify(60); 
	
	public String example(String message) {
		  return "hello, "+message; 		  
	   } 
	
	public String day_schedule(String agent_id,String date,String cookie)
	{
		//if (check_cookie(agent_id,cookie)){
		return getSchedules( date);
		//}
		//return "login_error";
	}
	
	public String make_order(String scheduleNo,int ticket_num,int child_num,String agent_id,String cookie,String cust,String tele,String price1,String price2,String amount)
	{
		double amt=Double.parseDouble(amount);
		double credit_amt=qry_empno_credit(agent_id);
		
		if (amt >credit_amt) return "credit:" +credit_amt;
		if (check_cookie(agent_id,cookie)){
			return makeOrder(scheduleNo,ticket_num,agent_id,child_num,cust,tele,price1,price2,amount);
		}
		return "login_error";
	}
	
	public String login(String agent_id,String password)
	{
		String operNo;
		String rt;
		
		if(agent_id.startsWith("ljsh0")){
			operNo=agent_id.substring(4);
			rt=qry_empno2(operNo, password);
		}
		else{
			rt=qry_empno(agent_id, password);
		}
		if(rt.charAt(0)=='o'){
			String current=""+System.currentTimeMillis();
			String cookie=current.substring(6);
			setCookie(agent_id,cookie );
			return rt +"," +cookie;  //ok,name,A,0000000  (return data like this)
		}
		
		return rt;
	}
	public String change_password(String agent_id,String old_password,String new_password)
	{
		int num=upd_password(agent_id,old_password,new_password);
		if (num==1)	return "ok";
		return "error,please try agin";
	}
	
	public String pay_result(String agent_id,String orderNo,String result,String cookie)
	{
		if (check_cookie(agent_id,cookie)){
		return  pay_ticket(orderNo);
		}
		return "login_error";
	}
	
	public String my_orders(String agent_id,String cookie)
	{
		if (check_cookie(agent_id,cookie)){
		 return  qry_orders(agent_id);
		}
		return "login_error";
	}
	
	public String cancel_order(String agent_id,String orderNo,String cookie)
	{
		if (check_cookie(agent_id,cookie)){
		return cancel_order(agent_id,orderNo);
		}
		return "login_error";
	}
	
	public String get_schedulePrice(String scheduleNo,String status)
	{
		return qry_travelLinePrice(scheduleNo,status);  //status=A,B,C
	}
	
	private void init_db(){
	
		if(dogThread==null){
			dog_thread();
		}
		if(conn!=null) return;
		my_resources = ResourceBundle.getBundle("connect"); 
		String db_url=my_resources.getString("host") +":3306/mylife?";
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
				conn = DriverManager.getConnection(db_url,"root","monday"); 
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
	
	 private String parent_agent(String childId){
		 String parentId=childId;
		 int pos=childId.indexOf('-');
		 if(pos>0) {
			 parentId=childId.substring(0, pos);
		 }
		 return parentId;
	 }
	 
	 private int insert_orderSpec(String orderno,String special_info,String specila_name)
	 {
		 int num=0;
		 String sql="INSERT INTO order_spec (orderno,spec,regdate,operid ) values ('"
			+orderno +"','" +special_info +"',CURRENT_TIMESTAMP,'" +specila_name+ "');";
		 
		 num=update_database(sql);
		 
		 return num;
	 }

private String makeOrder(String scheduleNo,int num,String agent_id,int child_num,String cust,String tele,String price1,String price2,String amount)
{
	StringBuffer s_info=new StringBuffer();
	String sql="select f_orderseats('" +scheduleNo +"'," + 
	num + ",1,'" +parent_agent(agent_id) +"','02800') from empno where empno='02800';" ; 
	//2,1,'00002','02800')  from empno where empno ='02088' ";
	String orderNo=null;
	 init_db(); //===
	rs=query(sql);
	  try {
			if(rs.next()) {
				char orderN=rs.getString(1).charAt(0); //.substring(0,13);
				s_info.append(rs.getString(1));
				s_info.append("\r\n");
				if (orderN>='0' && orderN<='9')
				{
				orderNo=rs.getString(1).substring(0,13);			
				insert_orderSpec(orderNo,agent_id,"name");
				upd_ticket(orderNo,agent_id,num,child_num,cust,tele,price1,price2,amount);
				upd_order_status(orderNo,"6");
				}
			
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//if(orderNo!=null){
		//put_sms(tele,"订票成功，订单号是："+orderNo + ",金额：" +amount +",人数："+num +"。【桂林两江四湖景区】");
		//}
 return s_info.toString(); 
}
	 
//f_web_formal_order`(sno char(15),agentid char(15),mseat_want int,
//madult int,mchild int,mpricea decimal,mpricec decimal,mamount decimal,
//mname char(44),mtele char(24)) RETURNS char(100)
//
private String makeOrder_web(String scheduleNo,int num,String agent_id,int child_num,
		String cust,String tele,String price1,String price2,String amount,String specText)
{
	int adult_num=num -child_num;
	StringBuffer s_info=new StringBuffer();
	String sql="select f_web_formal_order('" +scheduleNo +"','" + parent_agent(agent_id) +"',"+
	num +","+adult_num+","+child_num+","+price1+","+price2+","+amount+",'"+cust+
	"','" +tele+"');" ; 
	
	String orderNo=null;
	 init_db(); //===
	rs=query(sql);
	  try {
			if(rs.next()) {
				char orderN=rs.getString(1).charAt(0); //.substring(0,13);
				s_info.append(rs.getString(1));
				s_info.append("\r\n");
				if (orderN>='0' && orderN<='9' && specText.length()>2)
				{
				orderNo=rs.getString(1).substring(0,13);			
				insert_orderSpec(orderNo,specText,"02800");
			
				}
			
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//if(orderNo!=null){
		//put_sms(tele,"订票成功，订单号是："+orderNo + ",金额：" +amount +",人数："+num +"。【桂林两江四湖景区】");
		//}
 return s_info.toString(); 
}
	 



	   private String getSchedules(String str_date)   //查询航班信息
	   { 
		   StringBuffer s_info=new StringBuffer();  // //
		   String sql="SELECT s.scheduleno,s.dockon,s.dockoff,s.seatsoft,s.gotime,s.price,s.seattotal- s.seatused ,"+
		   " t.dockon , t.dockoff  FROM schedule s ,travelline t "+
		   " where s.scheduleno like '" +str_date.substring(2) +"%' and s.seattotal>s.seatused and s.status>='A' and s.dockon=t.lineno   " +
		   "  order by s.gotime ;";

		   init_db();
		   if(str_date.length()!=8) return "date string must like 20130630";
		   
		   rs=query(sql);
		   
		    try {
				while(rs.next()) {
					s_info.append(rs.getString(1));
					s_info.append(", ");
					s_info.append(rs.getString(2));
					s_info.append(", ");
					s_info.append(rs.getString(3));
					s_info.append(", ");
					s_info.append(rs.getString(4));
					s_info.append(", ");
					s_info.append(rs.getString(5));
					s_info.append(", ");
					s_info.append(rs.getString(6));
					s_info.append(", ");
					s_info.append(rs.getString(7));	
					s_info.append(", ");
					s_info.append(rs.getString(8));
					s_info.append(", ");
					s_info.append(rs.getString(9));	
					s_info.append("; \r\n");
   
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	   return s_info.toString(); 
	  
	   } 

	   private String qry_empno(String empno,String password)
	   {
		   String sql="select e.empname,a.status,a.telephone,e.tele  from empno e,agents a where empno='" +empno +"' and password='" +password +
		   "' and a.agentno='" +parent_agent(empno)+"';";  //=e.empno
		   init_db();
		   rs=query(sql);
		   try {
				if (rs.next()) {
					String tele;
					if (rs.getString(4).toString().length()>4) {
						tele=rs.getString(4).toString();
						}
					else
					{
						tele=rs.getString(3).toString();
					}
					return "ok, " + rs.getString(1) +"," +rs.getString(2)+"," +tele;
				}
		   } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   
			return "error userId or password";
	   }
	   
	   private String qry_empno2(String empno,String password)
	   {
		   String sql="select empname,'T','18807738800' from empno where empno='" +empno +"' and password='" +password +"';";  
		   init_db();
		   rs=query(sql);
		   try {
				if (rs.next()) {
					return "ok, " + rs.getString(1) +"," +rs.getString(2)+"," +rs.getString(3);
				}
		   } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   
			return "error userId or password";
	   }
	   
//	private String qry_agent(String teleNo)
//	{
//		String sql="select agentno from agents where agentno like '8%' and telephone='" +teleNo +"'  limit 1;";
//		init_db();
//		   rs=query(sql);
//		   try {
//				if (rs.next()) {
//					return rs.getString(1);
//				}
//		   } catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		   
//			return "no";
//	}
	
//	 private String login_tele(String teleNo,String password)
//	   {
//		 String empno=qry_agent(teleNo);
//		 String sql="select e.empname,a.status,a.telephone from empno e,agents a where empno='" +empno +"' and password='" +password +
//		   "' and a.agentno=e.empno;";
//		   init_db();
//		   rs=query(sql);
//		   try {
//				if (rs.next()) {
//					return "ok, " + rs.getString(1) +"," +rs.getString(2)+"," +rs.getString(3)+","+ empno;
//				}
//		   } catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		   
//			return "error userId or password";
//	   }
	
	private Integer test_connect() throws SQLException
	{
		Integer ret=0;
		String sql="select empname from empno where empno>'0'  limit 1";
		init_db();
		   rs=query(sql);
		   try {
				if (rs.next()) {
					ret= 1;
				}
		   } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		   
		   if(ret<1){conn.close();conn=null;}
			return ret;
	}
	
	private int upd_ticket(String orderno,String agent_id,int ticket_num,int child_num,String cust,String tele,String price1,String price2,String amount)
	   {
		 
		   String sql="INSERT INTO tickects(orderno, customer,telephone,adult,child,cicerone,price,childprice,amount," +
		   		"status, regdate, operid,pageserial)VALUES ('" +
		   		orderno +"','" +cust+"','"+tele+
		   		"',"+(ticket_num - child_num)+
		   		","+ child_num +",0," +price1+","+price2+"," + amount +
		   		",6,CURRENT_TIMESTAMP,'02800',0);";
		   init_db(); //===
		   int num=update_database(sql);
		   return num;
	   }

	   private String qry_travelLinePrice(String scheduleNo,String status)  //status 客户协议类ABC
	   {
		   String sql2="SELECT LEFT(dockon,8),seatsoft FROM schedule where scheduleno='"+
		   scheduleNo + "';";
		   String lineNo=null;
		   int seatsoft=0;
		   init_db();
		   rs=query(sql2);
		   try {
				if (rs.next()) {
					lineNo= rs.getString(1);
					seatsoft= rs.getString(2).charAt(0) -'0';
				}
		   } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   
		   
		   StringBuffer rt=new StringBuffer();
		   String sql="SELECT spec FROM order_spec  where orderno like 'line" + status +"%' and operid='"+
		   lineNo+"';";
		
		   rs=query(sql);
		   try {
				while (rs.next()) {
				String[] prices=rs.getString(1).split("\\|");
				rt.append( prices[seatsoft]+",");
				}
		   } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				rt.append("0,0,");
			}
		//   System.out.println(rt.toString());//====
		   return rt.toString();
	   }
	   
	   private int upd_password(String agent_id,String old_passwd,String new_passwd)
	   {
		   int num=0;
		   String sql="update empno set password='" +new_passwd +"' where empno='"+agent_id +
		   "' and password='" +old_passwd +"';";
		   
		   init_db(); //===
		    num=update_database(sql);
		    
		   return num;
	   }
	   
	   private String cancel_order(String agent_id,String orderNo)  //取消订单
	   {
		String sql=" select f_cancelorder('" +orderNo+"','02800') from empno where empno='02800'" ;
	 	
		   init_db(); //===
			rs=query(sql);
			  try {
					if(rs.next()) {
						int amt=rs.getInt(1);
						
						if (amt==1)
						{
							cncel_spec(agent_id,orderNo);
							del_ticket(orderNo);
							upd_order_status(orderNo,"0");
						}
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		 
		   return "......";
	   }
	   
	   private int del_ticket(String orderNo)
	   {
		   String sql="delete from tickects where orderno='" +orderNo +"' and status='6'";
		   int num=0;
		   init_db(); //===
		   num=update_database(sql);
		   return num;
	   }
	   
	   private int upd_order_status(String orderNo,String st)
	   {
		   String sql="update orders set status='"+st+"' where orderno='" +orderNo +"';";
		   int num=0;
		   init_db(); //===
		   num=update_database(sql);
		   return num;
	   }
	   
	   private String qry_orders(String agent_id)   //查询所有订单
	   {
		   SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyMMdd");        
		   Date date = new Date();  
		   String scheduleNo=(bartDateFormat.format(date));    
		   StringBuffer s_info=new StringBuffer();
		   String sql="select o.orderno, s.dockon,s.gotime,o.seatused,o.price ,o.status,o.seatsoft,t.customer,t.telephone,t.child,t.amount "+
		   " , v.dockon, v.dockoff  from orders o,schedule s ,tickects t ,travelline v "+
		   " where o.orderno >'"+ scheduleNo +"' and o.agentno='"+parent_agent(agent_id) +"' and o.status>'2' and o.scheduleno=s.scheduleno " +
		   "  and o.orderno=t.orderno and s.dockon=v.lineno;";

	//	   s_info.append(scheduleNo);
	    	init_db(); //===
		   rs=query(sql);

		    try {
				while(rs.next()) {
					s_info.append(rs.getString(1));
					s_info.append(", ");
					s_info.append(rs.getString(2));
					s_info.append(", ");
					s_info.append(rs.getString(3));
					s_info.append(", ");
					s_info.append(rs.getString(4));
					s_info.append(", ");
					s_info.append(rs.getString(5));	
					s_info.append(", ");
					s_info.append(rs.getString(7));
					s_info.append(", ");
					s_info.append(rs.getString(8));
					s_info.append(", ");
					s_info.append(rs.getString(9));
					s_info.append(", ");
					s_info.append(rs.getString(10));	
					s_info.append(", ");
					s_info.append(rs.getString(11));	
					s_info.append(", ");
					s_info.append(rs.getString(12));	
					s_info.append(", ");
					s_info.append(rs.getString(13));	
					s_info.append(", ");
					char st=(rs.getString(6)).charAt(0);
					if(st=='6')
						{s_info.append("order; \r\n");}	
					else if (st=='8')
						{s_info.append("ticket; \r\n");}	
					else if (st=='0')
						{s_info.append("cancel; \r\n");}	
					else
						{s_info.append(" ; \r\n");}	
								 
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				s_info.append("error,no , , , , , , , , , , , , , , ; ");
			}  
		   
		   return s_info.toString();
	   }
	
	   
	   private String qry_an_order(String agent_id,String oNo)   //查询订单
	   {       
		   StringBuffer s_info=new StringBuffer();
		   String sql="select o.orderno, s.dockon,s.gotime,o.seatused,o.price ,o.status,o.seatsoft,t.customer,t.telephone,t.child,t.amount "+
		   " , v.dockon, v.dockoff,t.regdate  from orders o,schedule s ,tickects t ,travelline v "+
		   " where o.orderno ='"+ oNo +"' and o.agentno='"+parent_agent(agent_id) +"' and o.status>'2' and o.scheduleno=s.scheduleno " +
		   "  and o.orderno=t.orderno and s.dockon=v.lineno;";

	    	init_db(); //===
		   rs=query(sql);

		    try {
				while(rs.next()) {
					s_info.append(rs.getString(1));
					s_info.append(", ");
					s_info.append(rs.getString(2));
					s_info.append(", ");
					s_info.append(rs.getString(3));
					s_info.append(", ");
					s_info.append(rs.getString(4));
					s_info.append(", ");
					s_info.append(rs.getString(5));	
					s_info.append(", ");
					s_info.append(rs.getString(7));
					s_info.append(", ");
					s_info.append(rs.getString(8));
					s_info.append(", ");
					s_info.append(rs.getString(9));
					s_info.append(", ");
					s_info.append(rs.getString(10));	
					s_info.append(", ");
					s_info.append(rs.getString(11));	
					s_info.append(", ");
					s_info.append(rs.getString(12));	
					s_info.append(", ");
					s_info.append(rs.getString(13));	
					s_info.append(", ");
					char st=(rs.getString(6)).charAt(0);
					if(st=='6')
						{s_info.append("order,");}	
					else if (st=='8')
						{s_info.append("ticket,");}	
					else if (st=='0')
						{s_info.append("cancel,");}	
					else
						{s_info.append("-,");}	
					
					s_info.append(rs.getString(14));	
					s_info.append(",end");
								 
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				s_info.append("error,no , , , , , , , , , , , , , , ,; ");
			}  
		   
		   return s_info.toString();
	   }
	   
	   
	   private double qry_orders_amount(String agent_id) //查询订单总金额
	   {
		   SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyMMdd");        
		   Date date = new Date();  
		   String scheduleNo=(bartDateFormat.format(date));    
		   String sql="select sum(t.amount) from orders o,tickects t "+
		   " where o.orderno >'"+ scheduleNo +"' and o.agentno='"+parent_agent(agent_id) +"' and o.status='6'  and t.orderno=o.orderno;";
		   double all_amount=0.0;
		   
	    	init_db(); //===
		   rs=query(sql);

		    try {
				if(rs.next()) {
					all_amount =rs.getDouble(1);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		//   System.out.println("all_amount:" +all_amount);  //===
		   return all_amount;
	   }
	   
	   private double qry_empno_credit(String empno)  //剩余信用额度,20140215账户余额
	   {
		  // String sql="select depart from empno  where empno='" +empno +"';";//信用额度
		   String sql="select (amtadd - amtused) from cn_balance  where agentno='" +parent_agent(empno) +"';";  //账户余额
		   double credit_amount=0.0;
		   double all_amount=0.0;
		   init_db();
		   rs=query(sql);
		   try {
				if (rs.next()) {
					credit_amount=Double.parseDouble(rs.getString(1));
				}
		   } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   all_amount=qry_orders_amount(empno);
		 //  System.out.println("credit_amount:" +credit_amount);  //===
			return credit_amount -all_amount;
	   }
	   
	   
	   private String pay_ticket(String orderNo)
	   {
		   String sql="update tickects set status='8' where orderno='" +orderNo +"' and status='6';";

		   int num=update_database(sql);
		   if (num==1)
		   {
		   upd_order_status(orderNo,"8");
		   return "ok";
		   }
		   return "error";
	   }
	   
	   private String cncel_spec(String agent_id,String orderNo)   //when cncel,orderspec 插入修改记录del,name
		{
			String sql="select agentname,telephone from agents where agentno='" +parent_agent(agent_id) +"';";
			String sql2="select regdate from order_spec where orderno='" +orderNo +"' and operid='name';";
			init_db();
			   
			   
			   rs=query(sql2);
			   try {
					if (rs.next()) {
						String spec2= agent_id +"="+ rs.getString(1).substring(0,16) ;
						insert_orderSpec(orderNo,spec2,"del");
					}
			   } catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 
			   rs=query(sql);
			   try {
					if (rs.next()) {
						String spec1= "<x>" + rs.getString(1) +"="+ rs.getString(2) ;
						  String sql3="update order_spec set spec= '"+spec1 +"',regdate=CURRENT_TIMESTAMP where orderno='"+
						   orderNo +"' and operid='name';";
						  update_database(sql3);
					}
			   } catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			   }
				return "'ok'";
		}
	   

		private  void dog_thread(){ 
			
		        dogThread=new DogThread("SMS_window"); 		  
		        dogThread.start(); 
		    } 

		class DogThread extends Thread{ 
	
		   DogThread(String name){ 
		        super(name);//调用父类带参数的构造方法 
		    } 
		   
		 
		    public void run(){ 
		    	int timer=0;
		        while(true){ 
		        	//sms();
		        	try {
						sleep(100);
		
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(timer==3000)
					{
					try {
						test_connect();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					timer=0;
					}
					timer++;
		        //    System.out.println(Thread.currentThread().getName()); 
		        } 
		    } 
		} 


		
		
		private void setCookie(String agent_id,String agentCookie)
		{
			int a=userCookies.indexOf(agent_id +",");
			if(a>=0)
			{
				int i=agent_id.length();
				a=a +i +1;
				userCookies.replace(a, a+agentCookie.length(), agentCookie);
			}
			else
			{
				userCookies.append(agent_id +",");
				userCookies.append(agentCookie +";");
			}
		//	System.out.println(agent_id +"" +"-" + agentCookie); //==
			return ;
		}
		
		private boolean check_cookie(String agent_id,String cookie)
		{
			
			int a=userCookies.indexOf(agent_id+","+cookie);
			if(a>=0) return true;
			return false;
		}
		
		
		public String qry_order(String agentId, String orderNo) {
			// TODO Auto-generated method stub
			return qry_an_order(agentId,orderNo);   //查询订单;
		}

		public String get_message(String msg) {
			// String sql="select message from messages ;";
			String sql="select orderno,spec from order_spec where orderno like 'news%'  order by orderno ;";
			 StringBuffer s_info=new StringBuffer();
			   init_db();
			   rs=query(sql);
			   s_info.append("<p>");
			   try {
				   
					while (rs.next()) {
						
						s_info.append(rs.getString(2));
						
					}
			   } catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					s_info.append(msg);
				}
			   s_info.append("</p><br/>");
				return s_info.toString();
		}

		public String get_credit(String agent_id) {
			double credit_amt= qry_empno_credit(agent_id);
			return credit_amt+"";
			
		}
		
/*普通客户，通过手机登录系统订票*/

		 private String f_mobile_verify(String phoneNum,String verifyCode)  //手机验证码登录
		   {
			String sql=" select f_mobile_verify('"+phoneNum+"','"+verifyCode+"');" ;
		 	
			   init_db(); //===
				rs=query(sql);
				  try {
						if(rs.next()) {
							int amt=rs.getInt(1);
							
							if (amt==0)//0==success
							{
								return "ok";
							}
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			 
			   return "error";
		   }
		
		 private String f_mobile_login(String phoneNum,String passwd)  //手机登录
		   {
			String sql=" select f_mobile_login('"+phoneNum+"','"+passwd+"');" ;
		 	
			   init_db(); //===
				rs=query(sql);
				  try {
						if(rs.next()) {
							int amt=rs.getInt(1);
							
							if (amt==0)//0==success
							{
								return "ok";
							}
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			 
			   return "error";
		   }

//		 f_mobile_add_order`(sno char(15),uno char(15),mseat_want int,
//					madult int,mchild int,mpricea decimal,mpricec decimal,mamount decimal,mcardid char(24),
//					mname char(44))
		 private String f_mobile_add_order(String sno,String uno,int m_all,int m_adult,int m_child,float mpricea,
				 float mpricec,float mamount,String cardid,String mname,String tele2)  //意向订单
		   {
			String sql="select f_mobile_add_order('"+sno+"','"+uno+"'," +m_all+","+m_adult+","+m_child+","+
			mpricea+","+mpricec+","+mamount+",'"+cardid+"','"+mname+"','"+tele2+ "');" ;
		 	
			   init_db(); //===
				rs=query(sql);
				  try {
						if(rs.next()) {
							int ono=rs.getInt(1);
					
							return ""+ono;
	
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			 
			   return "error db";
		   }
		
//			FUNCTION `f_mobile_pay_order`(tmp_ono int,actno char(24),actname char(64),act_amt decimal(8,2)) RETURNS char(100)
		 private String f_mobile_pay_order(String tmp_ono,String actno,String actname,float act_amt)  //付费后，正式申请订单
		   {
			String sql=" select f_mobile_pay_order("+tmp_ono+",'"+actno+"','"+actname+"',"+act_amt+");" ;
		 	
			   init_db(); //===
				rs=query(sql);
				  try {
						if(rs.next()) {					
								return rs.getString(1).toString();
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			 
			   return "error db";
		   }
	 
		//FUNCTION `f_mobile_formal_order`(sno char(15),uno char(15),mseat_want int,
//			madult int,mchild int,mpricea decimal,mpricec decimal,mamount decimal,mcardid char(24),
//			mname char(44)) RETURNS char(100)	
		 private String f_mobile_formal_order(String sno,String uno,int m_all,int m_adult,int m_child,float mpricea,
				 float mpricec,float mamount,String cardid,String mname,String tele2)  //直接生成正式订单
		   {
			String sql="select f_mobile_formal_order('"+sno+"','"+uno+"'," +m_all+","+m_adult+","+m_child+","+
			mpricea+","+mpricec+","+mamount+",'"+cardid+"','"+mname+ "','"+tele2+ "');" ;
		 	
			   init_db(); //===
				rs=query(sql);
				  try {
						if(rs.next()) {
					
								return rs.getString(1).toString();
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			 
			   return "error db";
		   }

	
		public String mobile_login(String phoneNum, String passwd) {
			// TODO Auto-generated method stub
			String ret=f_mobile_login(phoneNum, passwd);
			if (ret.charAt(0)=='o') return mobileVerify.addUser(phoneNum, false);
			return ret;
		}


		public String mobile_verify(String phoneNum, String verifyCode) {
			// TODO Auto-generated method stub
			String m_code=mobileVerify.addUser(phoneNum, true);
			f_mobile_verify(phoneNum, m_code);
			return m_code;
		}

		public String mobile_add_order(String sno, String uno, int mAll,
				int mAdult, int mChild, float mpricea, float mpricec,
				float mamount, String cardid, String mname, String cookie,String tele2) {
			// TODO Auto-generated method stub
			if (mobile_user_check(uno,cookie)<0) return "login error";
			
			return f_mobile_add_order(sno, uno, mAll,mAdult, mChild, mpricea, mpricec,
				mamount,cardid,mname,tele2);
		}

		public String mobile_formal_order(String sno, String uno, int mAll,
				int mAdult, int mChild, float mpricea, float mpricec,
				float mamount, String cardid, String mname, String cookie,String tele2) {
			// TODO Auto-generated method stub
			if (mobile_user_check(uno,cookie)<0) return "login error";
			
			return f_mobile_formal_order(sno, uno, mAll,mAdult, mChild, mpricea, mpricec,
					mamount,cardid,mname,tele2);
			
		}

		public String mobile_pay_order(String tmpOno, String actno,
				String actname, float actAmt, String uno, String cookie) {
			// TODO Auto-generated method stub
			if (mobile_user_check(uno,cookie)<0) return "login error";
			
			return f_mobile_pay_order(tmpOno,actno,actname, actAmt);
	
		}	
				
		private int mobile_user_check(String uno,String cookie){//no -1,error -2，ok 0
			return mobileVerify.check(uno, cookie);
		}
		 
		
		   private String getOneSchedules(String sno)   //查询航班信息
		   { 
			   StringBuffer s_info=new StringBuffer();  // //
			   String sql="SELECT s.scheduleno,s.dockon,s.dockoff,s.seatsoft,s.gotime,s.price,s.seattotal- s.seatused ,"+
			   " t.dockon , t.dockoff  FROM schedule s ,travelline t "+
			   " where s.scheduleno = '" +sno +"' and s.dockon=t.lineno   " +
			   "  order by s.gotime ;";

			   init_db();
			   
			   rs=query(sql);
			   
			    try {
					while(rs.next()) {
						s_info.append(rs.getString(1));
						s_info.append(",");
						s_info.append(rs.getString(2));
						s_info.append(", ");
						s_info.append(rs.getString(3));
						s_info.append(",");
						s_info.append(rs.getString(4));
						s_info.append(",");
						s_info.append(rs.getString(5));
						s_info.append(",");
						s_info.append(rs.getString(6));
						s_info.append(",");
						s_info.append(rs.getString(7));	
						s_info.append(",");
						s_info.append(rs.getString(8));
						s_info.append(",");
						s_info.append(rs.getString(9));	
						s_info.append("; ");
	   
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		   return s_info.toString(); 
		  
		   }

		public String one_schedule(String sno) {
			// TODO Auto-generated method stub
			return getOneSchedules(sno);
		} 
		
		  private String qry_morbile_orders(String userId)   //查询mobile订单
		   {
			   SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyMMdd");        
			   Date date = new Date();  
			   String scheduleNo=(bartDateFormat.format(date));    
			   StringBuffer s_info=new StringBuffer();
			   String sql="select o.id, s.dockon,s.gotime,o.seat_want,o.amount ,o.status,s.seatsoft,o.name,o.tele"+
			   " from mobile_orders o,schedule s "+
			   " where o.userid='"+userId +"' and o.scheduleno >'"+ scheduleNo +"' and o.scheduleno=s.scheduleno ;";

		//	   s_info.append(scheduleNo);
		    	init_db(); //===
			   rs=query(sql);

			    try {
					while(rs.next()) {
						s_info.append(rs.getString(1));
						s_info.append(",");
						s_info.append(rs.getString(2));
						s_info.append(",");
						s_info.append(rs.getString(3));
						s_info.append(",");
						s_info.append(rs.getString(4));
						s_info.append(",");
						s_info.append(rs.getString(5));	
						s_info.append(",");
						s_info.append(rs.getString(6));
						s_info.append(",");
						s_info.append(rs.getString(7));
						s_info.append(",");
						s_info.append(rs.getString(8));
						s_info.append(",");
						s_info.append(rs.getString(9));	
						s_info.append(";   ");	
									 
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					s_info.append("error,no , , , , , , , , , , , , , , ; ");
				}  
			   
			   return s_info.toString();
		   }

		public String my_mobile_orders(String userId, String cookie) {
			// TODO Auto-generated method stub
			return qry_morbile_orders(userId);
		}

		public String mobile_change_password(String userId, String oldPassword,
				String newPassword) {
			int num=0;
			   String sql="update mobile_user set password='" +newPassword +"'  where userid='"+userId +
			   "' and password='" +oldPassword +"';";
			   
			   init_db(); //===
			   num=update_database(sql);
			    
			if(num>0) return "ok";
			return "error";
		}

		public String mobile_get_credit(String userId) {
			 String sql="select amount_canUse from mobile_user  where userid='" +userId +"';";  //账户余额
			   double credit_amount=0.0;

			   init_db();
			   rs=query(sql);
			   try {
					if (rs.next()) {
						credit_amount=Double.parseDouble(rs.getString(1));
					}
			   } catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 

				return credit_amount+"";
		}
		
		private String qry_history_orders(String agent_id,String date1,String date2)   //查询历史订单 日期格式yyyy-MM-dd
		   {
			   
			   String scheduleNo=date1.substring(2, 4)+date1.substring(5, 7) +date1.substring(8); 
			   
			   StringBuffer s_info=new StringBuffer();
			   String sql="select o.orderno,o.seatused,o.seatsoft,t.customer,t.child,t.amount,o.status,o.operid,t.regdate "+
			   " from orders o,tickects t "+
			   " where o.orderno >'"+ scheduleNo +"' and o.agentno='"+parent_agent(agent_id) +"' and o.status>'2' " +
			   "  and t.orderno=o.orderno and t.regdate between '"+date1+" 0:0:0' and '" +date2 +" 23:59:59';";

		//	   s_info.append(scheduleNo);
		    	init_db(); //===
			   rs=query(sql);

			    try {
					while(rs.next()) {
						s_info.append(rs.getString(1));
						s_info.append(",");
						s_info.append(rs.getString(2));
						s_info.append(",");
						s_info.append(rs.getString(3));
						s_info.append(",");
						s_info.append(rs.getString(4));
						s_info.append(",");
						s_info.append(rs.getString(5));	
						s_info.append(",");
						s_info.append(rs.getString(6));
						s_info.append(",");
						s_info.append(rs.getString(7));
						s_info.append(",");
						s_info.append(rs.getString(8));
						s_info.append(",");
						s_info.append(rs.getString(9));							
						s_info.append(";");	
												 
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					s_info.append("error,no , , , , , , , , , , , , , , ; ");
				}  
			   
			   return s_info.toString();
		   }

		public String my_history_orders(String agentId, String cookie,
				String date1, String date2) {
			if (check_cookie(agentId,cookie)){
				return qry_history_orders(agentId,date1,date2);
				}
				return "login_error";
		}
				
private String qry_agent_water(String agent_id,String date1,String date2)   //查询,日期格式yyyy-MM-dd
		   {			   
			  StringBuffer s_info=new StringBuffer();
			   
			   
		String sql="SELECT  amtadd,amtused,seatused,sec_kind,sec_spec,regdate " +   
    		" FROM cn_securities  " +  
    		" WHERE ( regdate between '"+date1+" 0:0:0' and '" +date2 +" 23:59:59') AND ( agentno='"+parent_agent(agent_id)+"') ;";

		    init_db(); //===
			rs=query(sql);

			 try {
					while(rs.next()) {
						s_info.append(rs.getString(1));
						s_info.append(",");
						s_info.append(rs.getString(2));
						s_info.append(",");
						s_info.append(rs.getString(3));
						s_info.append(",");
						s_info.append(rs.getString(4));
						s_info.append(",");
						s_info.append(rs.getString(5));	
						s_info.append(",");
						s_info.append(rs.getString(6));						
						s_info.append("<>");	
												 
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					s_info.append("none");
				}  
			   
			   return s_info.toString();
		   }

public String get_agent_water(String agentId, String date1, String date2) {
	// TODO Auto-generated method stub
	return qry_agent_water(agentId,date1,date2);
}		

private String qry_agent_recharge(String agent_id,String date1,String date2)   //查询,日期格式yyyy-MM-dd
{			   
	  StringBuffer s_info=new StringBuffer();
	   
	   
String sql="SELECT  amtadd,amtused,setcode,regdate " +   
	" FROM cn_water  " +  
	" WHERE ( regdate between '"+date1+" 0:0:0' and '" +date2 +
	" 23:59:59') AND ( agentno='"+parent_agent(agent_id)+"') and (amtused=0) and (amtadd>0);";

 init_db(); //===
	rs=query(sql);

	 try {
			while(rs.next()) {
				s_info.append(rs.getString(1));
				s_info.append(",");
				s_info.append(rs.getString(2));
				s_info.append(",");
				s_info.append(rs.getString(3));
				s_info.append(",");
				s_info.append(rs.getString(4));					
				s_info.append(";");	
										 
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			s_info.append("none");
		}  
	   
	   return s_info.toString();
}

public String get_agent_recharge(String agentId, String date1, String date2) {
	// TODO Auto-generated method stub
	return qry_agent_recharge(agentId,date1,date2);
}

public String get_agent_balance(String agentId) {
	// TODO Auto-generated method stub
	return ""+qry_empno_credit(agentId);
}

public String get_agent_order_amount(String agentId) {
	// TODO Auto-generated method stub
	return ""+qry_orders_amount(agentId);
}

public String b2c_pay(String orderNo, String agentId, String phoneNum,
		float amt, String bankText, String operTimeStr) {
	
	String tm=operTimeStr.substring(0,4)+'-'+operTimeStr.substring(4,6)+'-'+
	operTimeStr.substring(6,8) +' ' +operTimeStr.substring(8,10) +':' +
	operTimeStr.substring(10,12)+':'+operTimeStr.substring(12,14);
	
	String sql="select f_b2c_water('"+orderNo+"','"+parent_agent(agentId)+"','" +phoneNum+"',"+amt+",'"+
	bankText+"','"+tm+ "');" ;
 	
	   init_db(); //===
		rs=query(sql);
		  try {
				if(rs.next()) {
			
						return rs.getString(1).toString();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 
	
	
	return "error";
}

public String b2c_list(String agentId, String refundAgentId,String date1, String date2, String cookie) {
	
	if (!check_cookie(agentId,cookie)){
		return "login_error";
		}
		
	  StringBuffer s_info=new StringBuffer();
	   	   
	  String sql="SELECT  a.agentno,a.orderno,a.amtadd,a.regdate,b.agentname  " +   
	  	"  FROM cn_securities a,agents b   " +  
	  	"  WHERE ( a.regdate between '"+date1+" 0:0:0' and '" +date2 +
	  	" 23:59:59') AND a.agentno like '"+refundAgentId+"%' and a.amtadd>0 and a.sec_spec='b2c' and b.agentno=a.agentno;" ;

	   init_db(); //===
	  	rs=query(sql);

	  	 try {
	  			while(rs.next()) {
	  				s_info.append(rs.getString(1));
	  				s_info.append(",");
	  				s_info.append(rs.getString(2));
	  				s_info.append(",");
	  				s_info.append(rs.getString(3));
	  				s_info.append(",");
	  				s_info.append(rs.getString(4));	
	  				s_info.append(",");
	  				s_info.append(rs.getString(5));	
	  				s_info.append(";");	
	  										 
	  			}
	  		} catch (SQLException e) {
	  			// TODO Auto-generated catch block
	  			e.printStackTrace();
	  			s_info.append("none");
	  		}  
	  	   
	  	   return s_info.toString();
	
}

public String b2c_refund(String agentId,String paytime, String orderNo, float amt,
		String cookie) {
	
	//if (!check_cookie(agentId,cookie)){
	//	return "login_error";
	//	}
	
	
	String sql="select f_b2c_refund('"+orderNo+"','"+agentId+"',"+amt+",'"+paytime+ "');" ;
 	
	   init_db(); //===
		rs=query(sql);
		  try {
				if(rs.next()) {
			
						return rs.getString(1).toString();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 
	
	
	return "error";
}

public String make_order_web(String scheduleNo, int ticketNum, int childNum,
		String agentId, String cookie, String cust, String tele, String price1,
		String price2, String amount, String specText) {
	
	double amt=Double.parseDouble(amount);
	double credit_amt=qry_empno_credit(agentId);
	
	if (amt >credit_amt) return "credit:" +credit_amt;
	if (check_cookie(agentId,cookie)){
		return makeOrder_web(scheduleNo,ticketNum,agentId,childNum,cust,tele,price1,price2,amount,specText);
	}
	return "login_error";
}		

		
}// EOF class


