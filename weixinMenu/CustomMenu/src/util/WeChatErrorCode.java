package util;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class WeChatErrorCode {


	public static final Map ERRORCODE=new HashMap<Integer,String>();
	
	static{
		ERRORCODE.put(-1,"系统繁忙");
		ERRORCODE.put(0,"请求成功");
		ERRORCODE.put(40001,"获取access_token时AppSecret错误，或者access_token无效");
		ERRORCODE.put(40002,"不合法的凭证类型");
		ERRORCODE.put(40003,"不合法的OpenID");
		ERRORCODE.put(40004,"不合法的媒体文件类型");
		ERRORCODE.put(40005,"不合法的文件类型");
		ERRORCODE.put(40006,"不合法的文件大小");
		ERRORCODE.put(40007,"不合法的媒体文件id");
		ERRORCODE.put(40008,"不合法的消息类型");
		ERRORCODE.put(40009,"不合法的图片文件大小");
		ERRORCODE.put(40010,"不合法的语音文件大小");
		ERRORCODE.put(40011,"不合法的视频文件大小");
		ERRORCODE.put(40012,"不合法的缩略图文件大小");
		ERRORCODE.put(40013,"不合法的APPID");
		ERRORCODE.put(40014,"不合法的access_token");
		ERRORCODE.put(40015,"不合法的菜单类型");
		ERRORCODE.put(40016,"不合法的按钮个数");
		ERRORCODE.put(40017,"不合法的按钮个数");
		ERRORCODE.put(40018,"不合法的按钮名字长度");
		ERRORCODE.put(40019,"不合法的按钮KEY长度");
		ERRORCODE.put(40020,"不合法的按钮URL长度");
		ERRORCODE.put(40021,"不合法的菜单版本号");
		ERRORCODE.put(40022,"不合法的子菜单级数");
		ERRORCODE.put(40023,"不合法的子菜单按钮个数");
		ERRORCODE.put(40024,"不合法的子菜单按钮类型");
		ERRORCODE.put(40025,"不合法的子菜单按钮名字长度");
		ERRORCODE.put(40026,"不合法的子菜单按钮KEY长度");
		ERRORCODE.put(40027,"不合法的子菜单按钮URL长度");
		ERRORCODE.put(40028,"不合法的自定义菜单使用用户");
		ERRORCODE.put(40029,"不合法的oauth_code");
		ERRORCODE.put(40030,"不合法的refresh_token");
		ERRORCODE.put(40031,"不合法的openid列表");
		ERRORCODE.put(40032,"不合法的openid列表长度");
		ERRORCODE.put(40033,"不合法的请求字符，不能包含\\uxxxx格式的字符");
		ERRORCODE.put(40035,"不合法的参数");
		ERRORCODE.put(40038,"不合法的请求格式");
		ERRORCODE.put(40039,"不合法的URL长度");
		ERRORCODE.put(40050,"不合法的分组id");
		ERRORCODE.put(40051,"分组名字不合法");
		ERRORCODE.put(41001,"缺少access_token参数");
		ERRORCODE.put(41002,"缺少appid参数");
		ERRORCODE.put(41003,"缺少refresh_token参数");
		ERRORCODE.put(41004,"缺少secret参数");
		ERRORCODE.put(41005,"缺少多媒体文件数据");
		ERRORCODE.put(41006,"缺少media_id参数");
		ERRORCODE.put(41007,"缺少子菜单数据");
		ERRORCODE.put(41008,"缺少oauth code");
		ERRORCODE.put(41009,"缺少openid");
		ERRORCODE.put(42001,"access_token超时");
		ERRORCODE.put(42002,"refresh_token超时");
		ERRORCODE.put(42003,"oauth_code超时");
		ERRORCODE.put(43001,"需要GET请求");
		ERRORCODE.put(43002,"需要POST请求");
		ERRORCODE.put(43003,"需要HTTPS请求");
		ERRORCODE.put(43004,"需要接收者关注");
		ERRORCODE.put(43005,"需要好友关系");
		ERRORCODE.put(44001,"多媒体文件为空");
		ERRORCODE.put(44002,"POST的数据包为空");
		ERRORCODE.put(44003,"图文消息内容为空");
		ERRORCODE.put(44004,"文本消息内容为空");
		ERRORCODE.put(45001,"多媒体文件大小超过限制");
		ERRORCODE.put(45002,"消息内容超过限制");
		ERRORCODE.put(45003,"标题字段超过限制");
		ERRORCODE.put(45004,"描述字段超过限制");
		ERRORCODE.put(45005,"链接字段超过限制");
		ERRORCODE.put(45006,"图片链接字段超过限制");
		ERRORCODE.put(45007,"语音播放时间超过限制");
		ERRORCODE.put(45008,"图文消息超过限制");
		ERRORCODE.put(45009,"接口调用超过限制");
		ERRORCODE.put(45010,"创建菜单个数超过限制");
		ERRORCODE.put(45015,"回复时间超过限制");
		ERRORCODE.put(45016,"系统分组，不允许修改");
		ERRORCODE.put(45017,"分组名字过长");
		ERRORCODE.put(45018,"分组数量超过上限");
		ERRORCODE.put(46001,"不存在媒体数据");
		ERRORCODE.put(46002,"不存在的菜单版本");
		ERRORCODE.put(46003,"不存在的菜单数据");
		ERRORCODE.put(46004,"不存在的用户");
		ERRORCODE.put(47001,"解析JSON/XML内容错误");
		ERRORCODE.put(48001,"api功能未授权");
		ERRORCODE.put(50001,"用户未授权该api");
	}

}