<%@ page language="java" pageEncoding="gb2312"%>
<jsp:useBean id="mydata1"  class="rx.com.mydata_old" scope="application"/>
<%
  Integer cid=Integer.valueOf(request.getParameter("c")); 
  Integer operator=Integer.valueOf(request.getParameter("o")); 
  Integer operid=Integer.valueOf(request.getParameter("operid")); 
  //String  control_phone=request.getParameter("control_phone"); 
   Integer ph=mydata1.get_cid_main(cid);
   if((ph-operator)==0) {ph=0; }else{return;}
//  Integer[] fld=new Integer[20]; //ring_type,sos_enable,class_enable,am1,am2,pm1,pm2;
  String datas;

  if(operid==98)
{

String tms=	request.getParameter("tms");
 mydata1.update_card_clock2(cid,tms);
 out.println("<script language='javaScript'> alert('设置成功！');</script>");  //history.back(-2);
 response.setHeader("refresh","1;url=i.jsp?c=" +cid +"&o=" +operator);  //===history befor?

 return;
}
  else if(operid==70)
{
	datas=mydata1.get_card_clock(cid);
	
}
 else
	  {
	
	  return;
	  }
  
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312">
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=0.5, maximum-scale=4.0, user-scalable=yes" />  
<meta name="mobileOptimized" content="400"/> 
<title>clock</title>
<style>   
<!--   
.tm
{font-size:14px; width:30px}
-->   
</style>   
</head>
<script language="javascript">

String.prototype.trim=function()
{
     return this.replace(/(^\s*)|(\s*$)/g,"");
}

function  checkTime(){ 
	var times="";
	
for(i=0;i<8;i++)
{
	var Hour="h" +i;
	var Minute="m" +i;
	var tm="time" +i;
	var wmsk="weekmask"+i;
    var tmh = document.getElementById(Hour).value;
  	var tmm = document.getElementById(Minute).value;
  	var hr=parseInt(tmh);
  	var min=parseInt(tmm);
  	var mask=1;
  	var weekmask=0;
    if(hr>=0 && hr<24 && min>=0 && min<60)
    {
    	//document.getElementById(tm).value=hr*100+min;
    	times=times + (hr*100+min)+",";
    }
    else
    {alert("请输入合理的时间！");
    return false;}
    
    for(j=0;j<7;j++)
    {
    	var cb=("cb"+i) +j;
    	if(document.getElementById(cb).checked) {
    		weekmask=weekmask +mask;
    	}
    	mask=mask +mask;
    }
   //	document.getElementById(wmsk).value=weekmask;
   times=times +weekmask +",";
   }
times=times +"0,0,0,0,#";
document.getElementById("tms").value=times;
 	document.getElementById("form3").submit();
    return true;
} 

function init_sets(datas)
{
var j=0;
var k=0;



for(i=0;i<8;i++)	
{
	var Hour="h" +i;
	var Minute="m" +i;

	j=datas.indexOf(",",k);
	if(j==0 || j==k) break;
	
	var tm=datas.substring(k,j);
	var i_tm=parseInt(tm);
	var hr=parseInt(i_tm/100) ;
	var min=i_tm%100;
	
	if(hr>9){
		document.getElementById(Hour).value=hr;
	}
	else{
		document.getElementById(Hour).value='0' +hr;
	}
	if(min>9){
		document.getElementById(Minute).value=min;
	}
	else{
		document.getElementById(Minute).value='0' +min;
	}
	
	
	k=j+1;
	j=datas.indexOf(",",k);
	if(j==0 ||j==k) break;
	var mask=parseInt(datas.substring(k,j));
	var wk=1;
	for(a=0;a<7;a++)
	{
		var cb=("cb"+i)+a;
		if ((wk & mask)>0){
			document.getElementById(cb).checked=true;
		}
		else{
			document.getElementById(cb).checked=false;
		}
		wk=wk +wk;
	}
	
	k=j+1;
	
    }
 
}

</script>

<body  onload="javascript:init_sets('<%=datas%>');"  >

<form>
<div  align="center" >语音报时设置</div>
<table width="260px"  border="1" align="center" title="" style="font-size:14px">

<tr>
    <td width="258" >	
		<table width="210" border="0" align="center">
			<tr>
				<td colspan="7"><label>时间</label><input id="h0" name="h0" type="text" value="07" maxlength="2" class="tm"><label>:</label><input id="m0" name="m0" type="text" value="00" maxlength="2" class="tm">
				</td>
			</tr>
      		<tr> 
        		<td width="30" align="center">一</td>
    			<td width="30" align="center">二</td>
				<td width="30" align="center">三</td>
				<td width="30" align="center">四</td>
				<td width="30" align="center">五</td>
				<td width="30" align="center">六</td>
				<td width="30" align="center">日</td>
      		</tr>
      		<tr>
      			<td align="center"><input id="cb01" name="cb01" type="checkbox" value="0" >  </td>
     			<td align="center"><input id="cb02" name="cb02" type="checkbox" value="0" >  </td>
    			<td align="center"><input id="cb03" name="cb03" type="checkbox" value="0" >  </td>
	 			<td align="center"><input id="cb04" name="cb04" type="checkbox" value="0" >  </td>
	 			<td align="center"><input id="cb05" name="cb05" type="checkbox" value="0" >  </td>
				<td align="center"><input id="cb06" name="cb06" type="checkbox" value="0" >  </td>
				<td align="center"><input id="cb00" name="cb00" type="checkbox" value="0" >  </td>
      		</tr>
    	</table>
     </td>
  </tr>

  <tr>
    <td width="258" >
	
		<table width="210" border="0" align="center">
			<tr>
				<td colspan="7"><label>时间</label><input id="h1" name="h1" type="text" value="07" maxlength="2" class="tm"><label>:</label><input id="m1" name="m1" type="text" value="00" maxlength="2" class="tm">
				</td>
			</tr>
      		<tr> 
        		<td width="30" align="center">一</td>
    			<td width="30" align="center">二</td>
				<td width="30" align="center">三</td>
				<td width="30" align="center">四</td>
				<td width="30" align="center">五</td>
				<td width="30" align="center">六</td>
				<td width="30" align="center">日</td>
      		</tr>
      		<tr>
      			<td align="center"><input id="cb11" name="cb11" type="checkbox" value="0" >  </td>
     			<td align="center"><input id="cb12" name="cb12" type="checkbox" value="0" >  </td>
    			<td align="center"><input id="cb13" name="cb13" type="checkbox" value="0" >  </td>
	 			<td align="center"><input id="cb14" name="cb14" type="checkbox" value="0" >  </td>
	 			<td align="center"><input id="cb15" name="cb15" type="checkbox" value="0" >  </td>
				<td align="center"><input id="cb16" name="cb16" type="checkbox" value="0" >  </td>
				<td align="center"><input id="cb10" name="cb10" type="checkbox" value="0" >  </td>
      		</tr>
    	</table>
     </td>
  </tr>
  
  <tr>
   <td width="258" >
	
		<table width="210" border="0" align="center">
			<tr>
				<td colspan="7"><label>时间</label><input id="h2" name="h2" type="text" value="07" maxlength="2" class="tm"><label>:</label><input id="m2" name="m2" type="text" value="00" maxlength="2" class="tm"> 
				</td>
			</tr>
      		<tr> 
        		<td width="30" align="center">一</td>
    			<td width="30" align="center">二</td>
				<td width="30" align="center">三</td>
				<td width="30" align="center">四</td>
				<td width="30" align="center">五</td>
				<td width="30" align="center">六</td>
				<td width="30" align="center">日</td>
      		</tr>
      		<tr>
      			<td align="center"><input id="cb21" name="cb21" type="checkbox" value="0" >  </td>
     			<td align="center"><input id="cb22" name="cb22" type="checkbox" value="0" >  </td>
    			<td align="center"><input id="cb23" name="cb23" type="checkbox" value="0" >  </td>
	 			<td align="center"><input id="cb24" name="cb24" type="checkbox" value="0" >  </td>
	 			<td align="center"><input id="cb25" name="cb25" type="checkbox" value="0" >  </td>
				<td align="center"><input id="cb26" name="cb26" type="checkbox" value="0" >  </td>
				<td align="center"><input id="cb20" name="cb20" type="checkbox" value="0" >  </td>
      		</tr>
    	</table>
     </td>
  </tr>
  <tr>
  <td width="258" >
	
		<table width="210" border="0" align="center">
			<tr>
				<td colspan="7"><label>时间</label><input id="h3" name="h3" type="text" value="07" maxlength="2" class="tm"><label>:</label><input id="m3" name="m3" type="text" value="00" maxlength="2" class="tm"> 
				</td>
			</tr>
      		<tr> 
        		<td width="30" align="center">一</td>
    			<td width="30" align="center">二</td>
				<td width="30" align="center">三</td>
				<td width="30" align="center">四</td>
				<td width="30" align="center">五</td>
				<td width="30" align="center">六</td>
				<td width="30" align="center">日</td>
      		</tr>
      		<tr>
      			<td align="center"><input id="cb31" name="cb31" type="checkbox" value="0" >  </td>
     			<td align="center"><input id="cb32" name="cb32" type="checkbox" value="0" >  </td>
    			<td align="center"><input id="cb33" name="cb33" type="checkbox" value="0" >  </td>
	 			<td align="center"><input id="cb34" name="cb34" type="checkbox" value="0" >  </td>
	 			<td align="center"><input id="cb35" name="cb35" type="checkbox" value="0" >  </td>
				<td align="center"><input id="cb36" name="cb36" type="checkbox" value="0" >  </td>
				<td align="center"><input id="cb30" name="cb30" type="checkbox" value="0" >  </td>
      		</tr>
    	</table>
     </td>
  </tr>
  
  <tr>
    <td width="258" >
	
		<table width="210" border="0" align="center">
			<tr>
				<td colspan="7"><label>时间</label><input id="h4" name="h4" type="text" value="07" maxlength="2"   class="tm"><label>:</label><input id="m4" name="m4" type="text" value="00" maxlength="2" class="tm"> 
				</td>
			</tr>
      		<tr> 
        		<td width="30" align="center">一</td>
    			<td width="30" align="center">二</td>
				<td width="30" align="center">三</td>
				<td width="30" align="center">四</td>
				<td width="30" align="center">五</td>
				<td width="30" align="center">六</td>
				<td width="30" align="center">日</td>
      		</tr>
      		<tr>
      			<td align="center"><input id="cb41" name="cb41" type="checkbox" value="0" >  </td>
     			<td align="center"><input id="cb42" name="cb42" type="checkbox" value="0" >  </td>
    			<td align="center"><input id="cb43" name="cb43" type="checkbox" value="0" >  </td>
	 			<td align="center"><input id="cb44" name="cb44" type="checkbox" value="0" >  </td>
	 			<td align="center"><input id="cb45" name="cb45" type="checkbox" value="0" >  </td>
				<td align="center"><input id="cb46" name="cb46" type="checkbox" value="0" >  </td>
				<td align="center"><input id="cb40" name="cb40" type="checkbox" value="0" >  </td>
      		</tr>
    	</table>
     </td>
  </tr>
  
  <tr>
    <td width="258" >	
		<table width="210" border="0" align="center">
			<tr>
				<td colspan="7"><label>时间</label><input id="h5" name="h5" type="text" value="07" maxlength="2" class="tm"><label>:</label><input id="m5" name="m5" type="text" value="00" maxlength="2" class="tm">
				</td>
			</tr>
      		<tr> 
        		<td width="30" align="center">一</td>
    			<td width="30" align="center">二</td>
				<td width="30" align="center">三</td>
				<td width="30" align="center">四</td>
				<td width="30" align="center">五</td>
				<td width="30" align="center">六</td>
				<td width="30" align="center">日</td>
      		</tr>
      		<tr>
      			<td align="center"><input id="cb51" name="cb51" type="checkbox" value="0" >  </td>
     			<td align="center"><input id="cb52" name="cb52" type="checkbox" value="0" >  </td>
    			<td align="center"><input id="cb53" name="cb53" type="checkbox" value="0" >  </td>
	 			<td align="center"><input id="cb54" name="cb54" type="checkbox" value="0" >  </td>
	 			<td align="center"><input id="cb55" name="cb55" type="checkbox" value="0" >  </td>
				<td align="center"><input id="cb56" name="cb56" type="checkbox" value="0" >  </td>
				<td align="center"><input id="cb50" name="cb50" type="checkbox" value="0" >  </td>
      		</tr>
    	</table>
     </td>
  </tr>
  
  <tr>
    <td width="258" >
	
		<table width="210" border="0" align="center">
			<tr>
				<td colspan="7"><label>时间</label><input id="h6" name="h6" type="text" value="07" maxlength="2" class="tm"><label>:</label><input id="m6" name="m6" type="text" value="00" maxlength="2" class="tm">
				</td>
			</tr>
      		<tr> 
        		<td width="30" align="center">一</td>
    			<td width="30" align="center">二</td>
				<td width="30" align="center">三</td>
				<td width="30" align="center">四</td>
				<td width="30" align="center">五</td>
				<td width="30" align="center">六</td>
				<td width="30" align="center">日</td>
      		</tr>
      		<tr>
      			<td align="center"><input id="cb61" name="cb61" type="checkbox" value="0" >  </td>
     			<td align="center"><input id="cb62" name="cb62" type="checkbox" value="0" >  </td>
    			<td align="center"><input id="cb63" name="cb63" type="checkbox" value="0" >  </td>
	 			<td align="center"><input id="cb64" name="cb64" type="checkbox" value="0" >  </td>
	 			<td align="center"><input id="cb65" name="cb65" type="checkbox" value="0" >  </td>
				<td align="center"><input id="cb66" name="cb66" type="checkbox" value="0" >  </td>
				<td align="center"><input id="cb60" name="cb60" type="checkbox" value="0" >  </td>
      		</tr>
    	</table>
     </td>
  </tr>
  
  <tr>
    <td width="258" >	
		<table width="210" border="0" align="center">
			<tr>
				<td colspan="7"><label>时间</label><input id="h7" name="h7" type="text" value="07" maxlength="2" class="tm"><label>:</label><input id="m7" name="m7" type="text" value="00" maxlength="2" class="tm">
				</td>
			</tr>
      		<tr> 
        		<td width="30" align="center">一</td>
    			<td width="30" align="center">二</td>
				<td width="30" align="center">三</td>
				<td width="30" align="center">四</td>
				<td width="30" align="center">五</td>
				<td width="30" align="center">六</td>
				<td width="30" align="center">日</td>
      		</tr>
      		<tr>
      			<td align="center"><input id="cb71" name="cb71" type="checkbox" value="0" >  </td>
     			<td align="center"><input id="cb72" name="cb72" type="checkbox" value="0" >  </td>
    			<td align="center"><input id="cb73" name="cb73" type="checkbox" value="0" >  </td>
	 			<td align="center"><input id="cb74" name="cb74" type="checkbox" value="0" >  </td>
	 			<td align="center"><input id="cb75" name="cb75" type="checkbox" value="0" >  </td>
				<td align="center"><input id="cb76" name="cb76" type="checkbox" value="0" >  </td>
				<td align="center"><input id="cb70" name="cb70" type="checkbox" value="0" >  </td>
      		</tr>
    	</table>
     </td>
  </tr>
  
  <tr>  <td align="center" >
	<input type="button" name="Submit" value="提交修改"  onclick="javascript:checkTime();">
</td></tr>
</table>
</form>
<form  id="form3" name="form3" method="post" action="term_alarm_02.jsp">

	<input type="hidden"  name="time1" id="time1"  value="0" readonly>
	<input type="hidden"  name="weekmask1" id="weekmask1"  value="0" readonly>
	<input type="hidden"  name="time2" id="time2"  value="0" readonly>
	<input type="hidden"  name="weekmask2" id="weekmask2"  value="0" readonly>
	<input type="hidden"  name="time3" id="time3"  value="0" readonly>
	<input type="hidden"  name="weekmask3" id="weekmask3"  value="0" readonly>
	<input type="hidden"  name="time4" id="time4"  value="0" readonly>
	<input type="hidden"  name="weekmask4" id="weekmask4"  value="0" readonly>
	
	<input type="hidden"  name="tms" id="tms"  value="0" readonly>
	<input type="hidden"  name="o" id="operator"  value="<%=operator %>" readonly>
	<input type="hidden"  name="operid" id="operid"  value="098" readonly>
	<input type="hidden"  name="c" id="cid"  value="<%=cid %>" readonly>					
</form>

</body>
</html>