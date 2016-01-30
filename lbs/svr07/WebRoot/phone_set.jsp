<%@ page language="java" pageEncoding="gb2312"%>
<jsp:useBean id="mydata1"  class="rx.com.mydata_old" scope="application"/>
<%
  Integer cid=Integer.valueOf(request.getParameter("c")); 
  Integer operator=Integer.valueOf(request.getParameter("o")); 
  Integer operid=Integer.valueOf(request.getParameter("operid")); 
  String  control_phone=request.getParameter("control_phone"); 
  
  String teles;
  String[] phones=new String[19];
  
  Integer ph=mydata1.get_cid_main(cid);
  Integer ph2=Integer.valueOf(control_phone.substring(6,11));
  if((ph-ph2)==0) {ph=0; }else{return;}
  
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
  
 //mydata1.update_card_phone(cid,phones);
 out.println("<script language='javaScript'> alert('设置成功！');</script>");
 return;
}
  else if(operid==70)
{
	teles=mydata1.get_card_telephone(cid);

}
 else
	  {
	
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
.fld1
{background-color:#EEEEFF; color:#880000;filter: alpha(opacity=50)}
.fld2
{background-color:#BBBBBB; color:#004400;filter: alpha(opacity=90);font-size:16px;style="width:60px"}
.fld3
{background-color:#DDDDFF; color:#220000;filter: alpha(opacity=100);font-size:16px;style="width:60px"}
</style> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312">
<title>phone</title>
</head>

<script language="JavaScript">

String.prototype.trim=function()
{
     return this.replace(/(^\s*)|(\s*$)/g,"");
}


function  checkMobile(){
	var str=document.getElementById(control_phone).value;
	document.getElementById(tele1).value=str;
for(i=0;i<19;i++)
{
	var tele="tele" +i;
    var sMobile = document.getElementById(tele).value.trim() ;
    if(sMobile.length<1) continue;
    if(!(sMobile.length==8 ||sMobile.length==7 ||sMobile.length==11 )|| isNaN(sMobile))
    {alert(sMobile + " 不是合理的电话号码");
    return false;}
   
   }
   for(i=0;i<2;i++)
{
	var tele="tele" +i;
    var sMobile = document.getElementById(tele).value.trim() ;
    if(!(/^1[3|4|5|8|9]\d{9}$/.test(sMobile))){ 
        alert(sMobile + " 不是完整的11位手机号"); 
        return false; 
    } 
    }
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
for(i=0;i<19;i++)
	{
	j=phones.indexOf(";",k);
	var tele="tele" +i;

	if (( j-k)>1)
	{ 
   document.getElementById(tele).value=phones.substring(k,j);
    }
    k=j+1;
    }
}
 
</script>
<body onload="javascript:init_phones('<%=teles%>');">

<table id="panel_form" width="80%" >
<tr><td>
<p align="center" class="fld1"><label >电话号码设置</label>	</p> 
<form method="Post" name="PeriodicTable" action="phone_set.jsp">				 
<table align="center">
				<tr><td> <label>卡机号码.</label>
				<input class="fld3" type="text" name="tele0" id="tele0"   maxlength="11"  >
				</td><td><label>友情号码 7</label>
				<input class="fld3" type="text" name="tele10" id="tele10"   maxlength="11"  >				
				</td></tr>				
				<tr><td>
				<label>主控号码1</label>
				<input class="fld2" type="text" name="tele25" id="tele25" value="<%=control_phone %>"  maxlength="11"   disabled>		
				</td><td><label>友情号码 8</label>
				<input class="fld3" type="text" name="tele11" id="tele11"    maxlength="11"  >		
				</td></tr>
				<tr><td> <label>亲情号码2</label>
				<input class="fld3" type="text" name="tele2" id="tele2"   maxlength="11"  >
				</td><td><label>友情号码 9</label>
				<input class="fld3" type="text" name="tele12" id="tele12"    maxlength="11"  >		
				</td></tr>
				<tr><td>
				<label>亲情号码3</label>
				<input class="fld3" type="text" name="tele3" id="tele3"    maxlength="11"  >		
				</td><td><label>友情号码10</label>
				<input class="fld3" type="text" name="tele13" id="tele13"    maxlength="11"  >		
				</td></tr>
				<tr><td> <label>友情号码1</label>
				<input class="fld3" type="text" name="tele4" id="tele4"    maxlength="11"  >
				</td><td><label>友情号码11</label>
				<input class="fld3" type="text" name="tele14" id="tele14"    maxlength="11"  >		
				</td></tr>
				<tr><td>
				<label>友情号码2</label>
				<input class="fld3" type="text" name="tele5" id="tele5"    maxlength="11"  >		
				</td><td><label>友情号码12</label>
				<input class="fld3" type="text" name="tele15" id="tele15"    maxlength="11"  >		
 				</td></tr>
 				<tr><td> <label>友情号码3</label>
				<input class="fld3" type="text" name="tele6" id="tele6"    maxlength="11"  >
				</td><td><label>友情号码13</label>
				<input class="fld3" type="text" name="tele16" id="tele16"    maxlength="11"  >		
				</td></tr><tr><td>
				<label> 友情号码4</label>
				<input class="fld3" type="text" name="tele7" id="tele7"    maxlength="11"  >
				</td><td><label>友情号码14</label>
				<input class="fld3" type="text" name="tele17" id="tele17"    maxlength="11"  >					
				</td></tr>
				<tr><td> <label>友情号码5</label>
				<input class="fld3" type="text" name="tele8" id="tele8"  maxlength="11"  >
				</td><td><label>友情号码15</label>
				<input class="fld3" type="text" name="tele18" id="tele18"    maxlength="11"  >		
				</td></tr>
				<tr><td>
				<label>友情号码6</label>
				<input class="fld3" type="text" name="tele9" id="tele9"   maxlength="11"  >	
				</td><td><label></label>
				<input class="fld2" type="hidden" name="tele1" id="tele1"  value="<%=control_phone %>" maxlength="11"  readonly>				
					<input class="fld2" type="hidden" name="control_phone" id="control_phone"  value="<%=control_phone %>" maxlength="11"  readonly>
				</td></tr>
				
				<tr><td>				
				<input type="hidden"  name="o" id="operator"  value="097" readonly>
				<input type="hidden"  name="operid" id="operid"  value="098" readonly>
				<input type="hidden"  name="c" id="cid"  value="<%=cid %>" readonly>
				
				<p class="fld1" align="right"/> 
				<input type="button" value="重新输入" onclick="javascript:checkMobile()">
				</td><td>
				<p class="fld1" align="left"/> 
				<input type="submit" name="Submit" value="提交修改" align="top"  onclick="javascript:return checkMobile();">
				</td></tr>
			</table>	
		</form>
</td>
</tr>
</table>

</body>
</html>