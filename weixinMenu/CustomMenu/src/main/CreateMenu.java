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
		System.out.print("������˵�json��");

		String menujson = Menu.getMenu_2(); //input.next();
		
		//��ȡToken ����ȡ����Token2Сʱ��Ч�����ظ�ʹ�ã�
		AccessToken at = WeChatUtil.getAccessToken(appId, appSecret);
		
		if (at!=null&&at.getToken()!=null) {
			
			System.out.println("��ȡ����TOKEN��\n"+at.getToken());
			//delete menu 
			int delresult=WeChatUtil.deleteMenu(at.getToken());
			if(!(delresult==0)){
				System.out.println("�˵�ɾ��ʧ��");
				return ;
			}
			// ���ýӿڴ����˵�
			int result = WeChatUtil.createMenu(menujson,at.getToken());
			// �жϲ˵��������
			if (0 == result)
				System.out.println("�˵������ɹ�");
			else
				System.out.println("�˵�����ʧ��");
		}else
			System.out.println("�˵�����ʧ��");
	}

}