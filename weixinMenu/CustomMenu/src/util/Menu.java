package util;

public final class Menu {
	public static String getMenu_1(){
		StringBuilder menuStr=new StringBuilder();
		menuStr.append("{\"button\":[");
		menuStr.append("{\"type\":\"view\",\"name\":\"位置查看\",");
		menuStr.append("\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxeb6cf6348c8d4aa0&redirect_uri=http%3a%2f%2f113.107.219.97%2flbs%2fopenIdLogin.jsp&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect\"},");
		menuStr.append("{\"type\":\"click\",\"name\":\"操作说明\",\"key\":\"V1002_love\"}]  } ");
		
		return menuStr.toString();
	};
	
	public static String getMenu_2(){
		StringBuilder menuStr=new StringBuilder();
		menuStr.append("{\"button\":[");
		
		menuStr.append("{\"type\":\"view\",\"name\":\"查看\",");
		menuStr.append("\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxeb6cf6348c8d4aa0&redirect_uri=http%3a%2f%2f113.107.219.97%2flbs%2fopenIdLogin.jsp&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect\"},");
		
		menuStr.append("{\"name\":\"操控\",");  //包含二级菜单
		menuStr.append("\"sub_button\":[");
		menuStr.append("{\"type\":\"click\",\"name\":\"强锁电机\",\"key\":\"V_stop\"},");
		menuStr.append("{\"type\":\"click\",\"name\":\"恢复电机\",\"key\":\"V_go\"},");
		menuStr.append("{\"type\":\"click\",\"name\":\"余额查询\",\"key\":\"V_acct\"}");
	//	menuStr.append("{\"type\":\"click\",\"name\":\"退出关联\",\"key\":\"V_reset\"}");
		menuStr.append("]},");  
		
		menuStr.append("{\"type\":\"click\",\"name\":\"说明\",\"key\":\"V_help\"} ");
		
		menuStr.append("]}"); 
		
		return menuStr.toString();
	};

}
