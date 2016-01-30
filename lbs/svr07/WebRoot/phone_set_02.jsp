<%@ page language="java" pageEncoding="gb2312"%>
<jsp:useBean id="mydata1"  class="rx.com.mydata_old" scope="application"/>
<%
  Integer cid=Integer.valueOf(request.getParameter("c")); 
  Integer operator=Integer.valueOf(request.getParameter("o")); 
  Integer operid=Integer.valueOf(request.getParameter("operid")); 
  String  control_phone=mydata1.get_control_phone2(cid);//request.getParameter("control_phone"); 
  String	teles;
  String[] phones=new String[20];
  Integer ph=mydata1.get_cid_main(cid);
  if((ph-operator)==0) {ph=0; }else{return;}
  if(operid==98)
{
   phones[0]=request.getParameter("tele0"); 
   phones[1]=request.getParameter("tele1");
   phones[2]=request.getParameter("tele2"); 
   phones[3]=request.getParameter("tele3");
   phones[4]=request.getParameter("tele4"); 
   phones[5]=request.getParameter("tele5");
   phones[6]=request.getParameter("tele6"); 
   phones[7]=request.getParameter("tele7");
   phones[8]=request.getParameter("tele8"); 
   phones[9]=request.getParameter("tele9");
   phones[10]=request.getParameter("tele10"); 
   phones[11]=request.getParameter("tele11");
   phones[12]=request.getParameter("tele12"); 
   phones[13]=request.getParameter("tele13");
   phones[14]=request.getParameter("tele14"); 
   phones[15]=request.getParameter("tele15");
   phones[16]=request.getParameter("tele16"); 
   phones[17]=request.getParameter("tele17");
   phones[18]=request.getParameter("tele18");
   phones[18]=request.getParameter("tele19");
  teles=phones[0]+","+phones[1] +","+phones[2] +","+phones[3] +","+phones[4] +","+phones[5] +","+phones[6] +","+phones[7] +
  ","+phones[8] +","+phones[9] +","+phones[10] +","+phones[11] +","+phones[12] +","+phones[13] +","+phones[14] +","+phones[15] +
  ","+phones[16] +","+phones[17] +","+phones[18] +","+phones[19] +",#";
 mydata1.update_card_phone2(cid,teles);
 
 out.println("<script language='javaScript'> alert('设置成功！');</script>");
 response.setHeader("refresh","1;url=i.jsp?c=" +cid +"&o=" +operator);  //===history befor?
 return;
}
  else if(operid==70)
{
	teles=mydata1.get_card_telephone(cid);

}
 else
	  {
	 	teles=" , , , , , , , , , , , , , , , , , , , , , #";
	  return;
	  }
  String result;
  
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<style type="text/css">
.aa
{ background-color:#0000ff; color:#ff0000;filter: alpha(opacity=50)}
.bb 
{ background-color:#3366cc; color:#ffffff}
.fld3
{background-color:#EEEEFF; color:#880000;filter: alpha(opacity=50)}
.fld2
{background-color:#BBBBBB; color:#004400;filter: alpha(opacity=90);}
.fld1
{background-color:#DDDDFF; color:#220000;filter: alpha(opacity=100);}
</style> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312">
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=0.5, maximum-scale=4.0, user-scalable=yes" />  
<meta name="MobileOptimized" content="240"/> 
<title>亲情号码</title>
</head>

<script language="JavaScript">

String.prototype.trim=function()
{
     return this.replace(/(^\s*)|(\s*$)/g,"");
}


function  checkMobile(){ 
for(i=0;i<19;i++)
{
	var tele="tele" +i;
    var sMobile = document.getElementById(tele).value.trim() ;
    if(sMobile.length<5) continue;
    if(!(sMobile.length==8 ||sMobile.length==7 ||sMobile.length==11 )|| isNaN(sMobile))
    {alert(sMobile + " 不是合理的电话号码");
    return false;}
   
   }
//   for(i=0;i<2;i++)
//{
//	var tele="tele" +i;
//    var sMobile = document.getElementById(tele).value.trim() ;
//    if(!(/^1[3|4|5|8|9]\d{9}$/.test(sMobile))){ 
 //       alert(sMobile + " 不是完整的11位手机号"); 
//        return false; 
//    } 
 //   }
    return true;
} 
function  checkdate(){ 
    var datestr = document.all["go_date"].value 
    if(datestr.length<6){ 
        alert(" 请输入合理的日期"); 
        return false; 
    } 
    return true;
} 
</script>

<script language="javascript">
 //substring(start,end),indexOf(s,start)
function init_phones(phones)
{
var j=0;
var k=0;
for(i=0;i<20;i++)
	{
	j=phones.indexOf(",",k);
	var tele="tele" +i;

	if (( j-k)>4)
	{ 
    document.all[tele].value=phones.substring(k,j);
    }
    k=j+1;
    }
}
 
</script>
<body onload="javascript:init_phones('<%=teles%>');">

<form method="Post" name="PeriodicTable" action="phone_set_02.jsp">
<input class="fld3" type="hidden"  name="tele0" id="tele0"  value="<%=control_phone %>"  maxlength="11"  >

<div  align="center" >电话号码设置</div>
<table width="260px"  border="1" align="center" title="" style="font-size:14px">
				<tr><td> 
				<label>主控1:</label>
				<input class="fld1" type="text"  name="tele1" id="tele1"  value="<%=control_phone %>"  readonly maxlength="11"  >				
				</td></tr>	
							
				<tr><td>
				<label>亲情2:</label>
				<input class="fld3" type="text"  name="tele2" id="tele2"  maxlength="11"  >		
				</td></tr>
				
				<tr><td>
				<label>亲情3:</label>
				<input class="fld3" type="text"  name="tele3" id="tele3"  maxlength="11"  >		
				</td></tr>
				
				<tr><td>
				<label>亲情4:</label>
				<input class="fld3" type="text"  name="tele4" id="tele4"  maxlength="11"  >		
				</td></tr>
										
				<tr><td>
				<label>电话1:</label>
				<input class="fld3" type="text"  name="tele5" id="tele5"  maxlength="11"  >		
				</td></tr>
				
				<tr><td>
				<label>电话2:</label>
				<input class="fld3" type="text"  name="tele6" id="tele6"  maxlength="11"  >		
				</td></tr>
				
				<tr><td>
				<label>电话3:</label>
				<input class="fld3" type="text"  name="tele7" id="tele7"  maxlength="11"  >		
				</td></tr>
				
				<tr><td>
				<label>电话4:</label>
				<input class="fld3" type="text"  name="tele8" id="tele8"  maxlength="11"  >		
				</td></tr>
				
				<tr><td>
				<label>电话5:</label>
				<input class="fld3" type="text"  name="tele9" id="tele9"  maxlength="11"  >		
				</td></tr>
				
				<tr><td>
				<label>电话6:</label>
				<input class="fld3" type="text"  name="tele10" id="tele10"  maxlength="11"  >		
				</td></tr>
				
				<tr><td>
				<label>电话7:</label>
				<input class="fld3" type="text"  name="tele11" id="tele11"  maxlength="11"  >		
				</td></tr>
				
				<tr><td>
				<label>电话8:</label>
				<input class="fld3" type="text"  name="tele12" id="tele12"  maxlength="11"  >		
				</td></tr>
				
				<tr><td>
				<label>电话9:</label>
				<input class="fld3" type="text"  name="tele13" id="tele13"  maxlength="11"  >		
				</td></tr>
				
				<tr><td>
				<label>电话10:</label>
				<input class="fld3" type="text"  name="tele14" id="tele14"  maxlength="11"  >		
				</td></tr>
							
				<tr><td>
				<p class="fld1" align="center"/> 
				<input type="submit" name="Submit" value="提交修改" align="top"  onclick="javascript:return checkMobile();">
				</td></tr>
			</table>
			
				<input class="fld3" type="hidden" name="tele15" id="tele15"    maxlength="11"  >
				<input class="fld3" type="hidden" name="tele16" id="tele16"    maxlength="11"  >
				<input class="fld3" type="hidden" name="tele17" id="tele17"    maxlength="11"  >
				<input class="fld3" type="hidden" name="tele18" id="tele18"    maxlength="11"  >
				<input class="fld3" type="hidden" name="tele19" id="tele19"    maxlength="11"  >
											
				<input type="hidden"  name="o" id="operator"  value="<%=operator %>" readonly>
				<input type="hidden"  name="operid" id="operid"  value="098" readonly>
				<input type="hidden"  name="c" id="cid"  value="<%=cid %>" readonly>	
				<input class="fld2" type="hidden"  name="control_phone" id="control_phone"  value="<%=control_phone %>"   readonly>					
	
		</form>

</body>
</html>