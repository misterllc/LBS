<%@ page language="java" pageEncoding="gb2312"%>
<jsp:useBean id="mydata1"  class="rx.com.mydata_old" scope="application"/>
<%
  Integer cid=Integer.valueOf(request.getParameter("c")); 
  Integer operator=Integer.valueOf(request.getParameter("o")); 
  Integer operid=Integer.valueOf(request.getParameter("operid")); 
  //String  control_phone=request.getParameter("control_phone"); 
   Integer ph=mydata1.get_cid_main(cid);
   if((ph-operator)==0) {ph=0; }else{return;}
 // Integer[] fld=new Integer[7]; //ring_type,sos_enable,class_enable,am1,am2,pm1,pm2;
  String datas;

  if(operid==98)
{
	String flgs=request.getParameter("flags");
	String cls=request.getParameter("class_time"); 

	
 mydata1.update_card_set2(cid,flgs,cls);
 out.println("<script language='javaScript'> alert('设置成功！');</script>");  //history.back(-2);
 response.setHeader("refresh","1;url=i.jsp?c=" +cid +"&o=" +operator);  //===history befor?

 return;
}
  else if(operid==70)
{
	datas=mydata1.get_card_set(cid);
	
}
 else
	  {
	
	  return;
	  }
  
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
{background-color:#BBBBBB; color:#004400;filter: alpha(opacity=90);font-size:16px;}
.fld3
{background-color:#DDDDFF; color:#220000;filter: alpha(opacity=100);font-size:16px;}
body,html,{width: 100%;height: 100%;overflow: hidden;	align:center;}
#datamap {width: 400px;height: 90px;overflow: hidden;margin:0;display:none;}
</style>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312">
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=0.5, maximum-scale=4.0, user-scalable=yes" />  
<meta name="MobileOptimized" content="400"/> 
<title>设置</title>
</head>

<script language="JavaScript">

String.prototype.trim=function()
{
     return this.replace(/(^\s*)|(\s*$)/g,"");
}


function  checkTime(){
var flg="";
var times="";

	if(document.getElementById("r01").checked==true){
		flg=flg+"1,";
	}
	else if(document.getElementById("r02").checked==true){
		flg=flg+"2,";
	}
	else {
		flg=flg+"0,";
	}
	
	if(document.getElementById("r11").checked==true){
		flg=flg+"1,";
	}
	else {
		flg=flg+"0,";
	}
	
	if(document.getElementById("r21").checked==true){
		flg=flg+"1,";
	}
	else if(document.getElementById("r22").checked==true){
		flg=flg+"2,";
	}
	else {
		flg=flg+"0,";
	}
	document.getElementById("flags").value=flg;
	
for(i=0;i<12;i++)
{
	var dd="d0" +i;
    var tm = document.getElementById(dd).value.trim() ;
  times=times +tm +",";
    if((!(tm.length==4)) || isNaN(tm))
    {alert("请用4个数子表示时间(如:0830)");
    return false;}
   
   }
times=times +"0,0,0,0,0,0,#";
document.getElementById("class_time").value=times;
 document.getElementById("form2").submit();
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
function init_sets(datas)
{
var j=0;
var k=0;
j=datas.indexOf(",",k);
var rb="r0" +datas.substring(k,j);
document.getElementById(rb).checked=true;
k=j+1;
j=datas.indexOf(",",k);
var rb="r1" +datas.substring(k,j);
document.getElementById(rb).checked=true;

k=j+1;
j=datas.indexOf(",",k);
var rb="r2" +datas.substring(k,j);
document.getElementById(rb).checked=true;

for(i=0;i<12;i++)
	{
	k=j+1;
	j=datas.indexOf(",",k);
	if(j==0) break;
	var rb="d0" +i;  //
	var tm=datas.substring(k,j);
	if ((j-k)<4) tm='0'+tm;
	document.getElementById(rb).value=tm;
    }
 
}
 
</script>
<body onload="javascript:init_sets('<%=datas%>');"  >

<form id="form23" name="form23" method="post" action="term_set_02.jsp">
<table width="300px" height="400px" border="1">
  <tr>
    <td width="237"><p>来电提示</p>    
        <label>
          <input id="r00" type="radio" name="ring_type" value="0"  />
         铃声</label>
        <label>
        <input id="r01" type="radio" name="ring_type" value="1" />
        震动</label>
        <label>
		   <input id="r02" type="radio" name="ring_type"  value="2"  checked/>
        报号</label>
   </td>
  </tr>
  <tr>
    <td><p>SOS功能</p>    
        <label>
          <input id="r11" type="radio" name="sos_enable" value="1"  checked/>
         启用</label>
        <label>
        <input id="r10" type="radio" name="sos_enable" value="0" />
        关闭</label>

   </td>
  </tr>
  <tr>
  <td>
  <p>周一到周五上课时间段</p>
  <label>上午</label><input id="d00" type="text" name="am10" value="0830"  style="width:40px"/>-<input id="d01" type="text" name="am20" value="1130" style="width:40px"/>
  <label>  下午</label><input id="d02" type="text" name="pm10" value="1330"  style="width:40px"/>-<input id="d03" type="text" name="pm20" value="1630" style="width:40px"/>
  </td>
  </tr>
  
  <tr>
  <td>
  <p>周六上课时间段</p>
  <label>上午</label><input id="d04" type="text" name="am11" value="0830"  style="width:40px"/>-<input id="d05" type="text" name="am21" value="1130" style="width:40px"/>
  <label>  下午</label><input id="d06" type="text" name="pm11" value="1330"  style="width:40px"/>-<input id="d07" type="text" name="pm21" value="1630" style="width:40px"/>
  </td>
  </tr>
  
  <tr>
  <td>
  <p>周日上课时间段</p>
  <label>上午</label><input id="d08" type="text" name="am12" value="0830"  style="width:40px"/>-<input id="d09" type="text" name="am22" value="1130" style="width:40px"/>
  <label>  下午</label><input id="d010" type="text" name="pm12" value="1330"  style="width:40px"/>-<input id="d011" type="text" name="pm22" value="1630" style="width:40px"/>
  </td>
  </tr>
  
  
  <tr>
    <td><p>上课时段电话呼叫</p>    
         <label>
          <input id="r22" type="radio" name="class_enable" value="2" checked/>
         禁呼入</label>
        <label>
        <input id="r20" type="radio" name="class_enable" value="0" />
        不限制</label>
        <label>
		   <input id="r21" type="radio" name="class_enable"  value="1"  />
        全禁止</label>

   </td>
  </tr>
  <tr>  <td align="center" >
	<input type="button" name="button_submit" value="提交修改"  onclick="javascript:checkTime();">
</td></tr>
</table>
</form>
<form id="form2" name="form2" method="post" action="term_set_02.jsp">
	<input type="hidden"  name="class_time" id="class_time"  value="0,0,0,0,0,0,#" readonly>
	<input type="hidden"  name="flags" id="flags"  value="0,1,0" readonly>
	<input type="hidden"  name="o" id="operator"  value="<%=operator %>" readonly>
	<input type="hidden"  name="operid" id="operid"  value="098" readonly>
	<input type="hidden"  name="c" id="cid"  value="<%=cid %>" readonly>				
</form>

</body>
</html>