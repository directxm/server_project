package com.x.wechat;

/**
 * 类名: Constant </br>
 * 描述: 常量定义 </br>
 * 开发人员: fatum </br>
 * 创建时间: 16-10-10 </br>
 * 发布版本: V1.0 </br>
 */
public class Constant
{
	public static class MessageType
	{
		// 请求消息类型：文本
	    public static final String REQ_MESSAGE_TYPE_TEXT = "text";
	    // 请求消息类型：图片
	    public static final String REQ_MESSAGE_TYPE_IMAGE = "image";
	    // 请求消息类型：语音
	    public static final String REQ_MESSAGE_TYPE_VOICE = "voice";
	    // 请求消息类型：视频
	    public static final String REQ_MESSAGE_TYPE_VIDEO = "video";
	    // 请求消息类型：小视频
	    public static final String REQ_MESSAGE_TYPE_SHORTVIDEO = "shortvideo";
	    // 请求消息类型：地理位置
	    public static final String REQ_MESSAGE_TYPE_LOCATION = "location";
	    // 请求消息类型：链接
	    public static final String REQ_MESSAGE_TYPE_LINK = "link";

	    // 请求消息类型：事件推送
	    public static final String REQ_MESSAGE_TYPE_EVENT = "event";

	    // 事件类型：subscribe(订阅)
	    public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";
	    // 事件类型：unsubscribe(取消订阅)
	    public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";
	    // 事件类型：scan(用户已关注时的扫描带参数二维码)
	    public static final String EVENT_TYPE_SCAN = "scan";
	    // 事件类型：LOCATION(上报地理位置)
	    public static final String EVENT_TYPE_LOCATION = "LOCATION";
	    // 事件类型：CLICK(自定义菜单)
	    public static final String EVENT_TYPE_CLICK = "CLICK";

	    // 响应消息类型：文本
	    public static final String RESP_MESSAGE_TYPE_TEXT = "text";
	    // 响应消息类型：图片
	    public static final String RESP_MESSAGE_TYPE_IMAGE = "image";
	    // 响应消息类型：语音
	    public static final String RESP_MESSAGE_TYPE_VOICE = "voice";
	    // 响应消息类型：视频
	    public static final String RESP_MESSAGE_TYPE_VIDEO = "video";
	    // 响应消息类型：音乐
	    public static final String RESP_MESSAGE_TYPE_MUSIC = "music";
	    // 响应消息类型：图文
	    public static final String RESP_MESSAGE_TYPE_NEWS = "news";
	}

	public static class UrlPost
	{
		// 自定义菜单创建接口
		// http://mp.weixin.qq.com/wiki/10/0234e39a2025342c17a7d23595c6b40a.html

		// 自定义菜单创建
		public static final String CUSTOM_MENU_CREATE = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token={}";
		// 自定义菜单查询
		public static final String CUSTOM_MENU_GET = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token={}";
		// 自定义菜单删除
		public static final String CUSTOM_MENU_DELETE = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token={}";
		// 自定义个性化菜单
		// 参照 http://www.cnblogs.com/txw1958/p/weixin-menu-conditional.html
		public static final String CUSTOM_MENU_ADDCONDITIONAL = "https://api.weixin.qq.com/cgi-bin/menu/addconditional?access_token={}";

		// 获取的access_token
		public static final String ACCESS_TOKEN_GET = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={0}&secret={1}";

		// 网页授权access_token页面
		public static final String OAUTH_TOKEN_GET = "https://api.weixin.qq.com/sns/oauth2/access_token?appid={0}&secret={1}&code={2}&grant_type=authorization_code";


		// 创建分组
		public static final String USER_GROUP_CREATE = "https://api.weixin.qq.com/cgi-bin/groups/create?access_token={}";
		// 查询所有分组
		public static final String USER_GROUP_GET = "https://api.weixin.qq.com/cgi-bin/groups/get?access_token={}";
		// 查询用户所在分组
		public static final String USER_GROUP_GET_ID = "https://api.weixin.qq.com/cgi-bin/groups/getid?access_token={}";
		// 修改分组名
		public static final String USER_GROUP_UPDATE = "https://api.weixin.qq.com/cgi-bin/groups/update?access_token={}";
		// 移动用户分组
		public static final String USER_GROUP_MEMBER_UPDATE = "https://api.weixin.qq.com/cgi-bin/groups/members/update?access_token={}";

		// 获取关注者列表
		// 一次拉取调用最多拉取10000个关注者的OpenID , next_openid 第一个拉取的OPENID，不填默认从头开始拉取
		// 参照 http://www.cnblogs.com/txw1958/p/weixin-get-follower-list.html
		public static final String USER_ID_FRIST_GET = "https://api.weixin.qq.com/cgi-bin/user/get?access_token={}";
		public static final String USER_ID_NEXT_GET = "https://api.weixin.qq.com/cgi-bin/user/get?access_token={}&next_openid={}";
		// 获取用户基本信息
		// 参照 http://www.cnblogs.com/txw1958/p/weixin-get-user-basic-info.html
		public static final String USER_BASIC_GET = "https://api.weixin.qq.com/cgi-bin/user/info?access_token={0}&openid={1}&lang=zh_CN";
		// 获取用户SNS信息
		public static final String USER_SNS_GET = "https://api.weixin.qq.com/sns/userinfo?access_token={0}&openid={1}";

	}
}
