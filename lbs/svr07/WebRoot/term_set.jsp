<%@ page language="java" pageEncoding="gb2312"%>
<jsp:useBean id="mydata1"  class="rx.com.mydata_old" scope="application"/>
<%
  Integer cid=Integer.valueOf(request.getParameter("c")); 
  Integer operator=Integer.valueOf(request.getParameter("o")); 
  Integer operid=Integer.valueOf(request.getParameter("operid")); 
  String  control_phone=request.getParameter("control_phone"); 
  
  Integer[] fld=new Integer[7]; //ring_type,sos_enable,class_enable,am1,am2,pm1,pm2;
  String datas;

  if(operid==98)
{
	fld[0]=Integer.valueOf(request.getParameter("ring_type"));
	fld[1]=Integer.valueOf(request.getParameter("sos_enable")); 
	fld[2]=Integer.valueOf(request.getParameter("class_enable"));
	fld[3]=Integer.valueOf(request.getParameter("am1"));
	fld[4]=Integer.valueOf(request.getParameter("am2"));
	fld[5]=Integer.valueOf(request.getParameter("pm1"));
	fld[6]=Integer.valueOf(request.getParameter("pm2"));
	
 mydata1.update_card_set(cid,fld);
 out.println("<script language='javaScript'> alert('���óɹ���');</script>");  //history.back(-2);
 //response.setHeader("refresh","1;url=i.jsp?cid=" +cid );  //===history befor?

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
{background-color:#BBBBBB; color:#004400;filter: alpha(opacity=90);font-size:16px;style="width:60px"}
.fld3
{background-color:#DDDDFF; color:#220000;filter: alpha(opacity=100);font-size:16px;style="width:60px"}
body,html,{width: 400px;height: 600px;overflow: hidden;	align:center;}
#datamap {width: 400px;height: 100px;overflow: hidden;margin:0;display:none;}
</style>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312">
<title>Insert title here</title>
</head>

<script language="JavaScript">

String.prototype.trim=function()
{
     return this.replace(/(^\s*)|(\s*$)/g,"");
}


function  checkTime(){ 
for(i=0;i<4;i++)
{
	var dd="d0" +i;
    var tm = document.getElementById(dd).value.trim() ;
  
    if((!(tm.length==4)) || isNaN(tm))
    {alert("����4�����ӱ�ʾʱ��(��:0830)");
    return false;}
   
   }
 
    return true;
} 
function  checkdate(){ 
    var datestr = document.all["go_date"].value 
    if(datestr.length<6){ 
        alert(" ��������������"); 
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
j=datas.indexOf(";",k);
var rb="r0" +datas.substring(k,j);
document.getElementById(rb).checked=true;
k=j+1;
j=datas.indexOf(";",k);
var rb="r1" +datas.substring(k,j);
document.getElementById(rb).checked=true;

k=j+1;
j=datas.indexOf(";",k);
var rb="r2" +datas.substring(k,j);
document.getElementById(rb).checked=true;

for(i=0;i<4;i++)
	{
	k=j+1;
	j=datas.indexOf(";",k);
	if(j==0) break;
	var rb="d0" +i;  //
	var tm=datas.substring(k,j);
	if ((j-k)<4) tm='0'+tm;
	document.getElementById(rb).value=tm;
    }
 
}
 
</script>
<body onload="javascript:init_sets('<%=datas%>');"  >

<form id="form2" name="form2" method="post" action="term_set.jsp">
<table width="300px" height="400px" border="1">
  <tr>
    <td width="237"><p>������ʾ</p>    
        <label>
          <input id="r00" type="radio" name="ring_type" value="0"  />
         ����</label>
        <label>
        <input id="r01" type="radio" name="ring_type" value="1" />
        ��</label>
        <label>
		   <input id="r02" type="radio" name="ring_type"  value="2" />
        ����</label>
   </td>
  </tr>
  <tr>
    <td><p>SOS����</p>    
        <label>
          <input id="r11" type="radio" name="sos_enable" value="1"  />
         ����</label>
        <label>
        <input id="r10" type="radio" name="sos_enable" value="0" />
        �ر�</label>

   </td>
  </tr>
  <tr>
  <td>
  <p>�Ͽ�ʱ���</p>
  <label>����</label><input id="d00" type="text" name="am1" value="0830"  style="width:36px"/>-<input id="d01" type="text" name="am2" value="1130" style="width:36px"/>
  <label>  ����</label><input id="d02" type="text" name="pm1" value="1330"  style="width:36px"/>-<input id="d03" type="text" name="pm2" value="1630" style="width:36px"/>
  </td>
  </tr>
  
  <tr>
    <td><p>�Ͽ�ʱ�ε绰����</p>    
         <label>
          <input id="r22" type="radio" name="class_enable" value="2" />
         ������</label>
        <label>
        <input id="r20" type="radio" name="class_enable" value="0" />
        ������</label>
        <label>
		   <input id="r21" type="radio" name="class_enable"  value="1"  />
        ȫ��ֹ</label>

   </td>
  </tr>
  <tr>  <td align="center" >
	<input type="submit" name="Submit" value="�ύ�޸�"  onclick="javascript:return checkTime();">
</td></tr>
</table>
	<input type="hidden"  name="o" id="operator"  value="097" readonly>
	<input type="hidden"  name="operid" id="operid"  value="098" readonly>
	<input type="hidden"  name="c" id="cid"  value="<%=cid %>" readonly>				
</form>

</body>
</html>