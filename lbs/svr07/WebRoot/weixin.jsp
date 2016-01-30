<%@page import="rx.com.WeiXinHandle"%>

<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="rx.com.weiXinSignature"%>

<%

	String echostr=request.getParameter("echostr");
	if(null==echostr||echostr.isEmpty()){
			WeiXinHandle handler=new WeiXinHandle(request,response);
			handler.responseMsg();
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