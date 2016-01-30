package main;

import util.AccessToken;
import util.Menu;
import util.WeChatUtil;

public class CreateMenu {

	public static void main(String[] args) {		
		String appId ="wxeb6cf6348c8d4aa0"; 
		String appSecret ="8ca36c057f2b51baad70f6d87bcfec3d";

		/*test open
		 * myAppid="wxeb6cf6348c8d4aa0";
		 * myAppSecret="8ca36c057f2b51baad70f6d87bcfec3d";
		 * ---rx:
		 * appId ="wx4006cb3c7c1154b7"
		 * appSecret ="071b2aa21417c3c8899fa5f203db3ccd"
		 */
		System.out.print("请输入菜单json：");

		String menujson = Menu.getMenu_2(); //input.next();
		
		//获取Token （获取到的Token2小时有效，可重复使用）
		AccessToken at = WeChatUtil.getAccessToken(appId, appSecret);
		
		if (at!=null&&at.getToken()!=null) {
			
			System.out.println("获取到的TOKEN：\n"+at.getToken());
			//delete menu 
			int delresult=WeChatUtil.deleteMenu(at.getToken());
			if(!(delresult==0)){
				System.out.println("菜单删除失败");
				return ;
			}
			// 调用接口创建菜单
			int result = WeChatUtil.createMenu(menujson,at.getToken());
			// 判断菜单创建结果
			if (0 == result)
				System.out.println("菜单创建成功");
			else
				System.out.println("菜单创建失败");
		}else
			System.out.println("菜单创建失败");
	}

}