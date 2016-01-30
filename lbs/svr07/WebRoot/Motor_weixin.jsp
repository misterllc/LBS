<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="rx.com.weiXinSignature"%>
<jsp:useBean id="myWX"  class="rx.com.WxHandler" scope="application"/>

<%
	String echostr=request.getParameter("echostr");
	
	if(null==echostr||echostr.isEmpty()){
			myWX.responseMsg(request, response);
	}else{
			String echo=null;	
			if(rx.com.weiXinSignature.checkSignature(request)){
				echo=echostr;
			}else{
				echo="error";                                                                                                                                                                                                                                                                                                                                         
			}
			try{
				response.getWriter().print(echo);
				response.getWriter().flush();
				response.getWriter().close();
			}catch(Exception e){
				
			}
		}

	
	
%>