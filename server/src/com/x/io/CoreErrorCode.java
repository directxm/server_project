package com.x.io;

/**
 * @author yanfengbing
 * @since 13-12-7 PM5:24
 */
public enum CoreErrorCode implements ErrorCode
{
	UNKNOWN(-1, "未知错误，一般是什么都没有干就发生了"),
	FATAL(1, "严重错误，本来不可能发生，请一定联系开发者"),
	ERROR(2, "一般错误，请看详细信息"),
	TIMEOUT(3, "操作超时！"),
	MARSHAL(4, "程式解码错误"),
	CONNECTION(5, "连接错误"),
	UNSUPPORTED(6, "不支持此功能"),
	PARAMETER_INVALID(7, "参数错误"),
	ENCODING(8, "编码错误，请查看详细信息"),
	SIGNATURE(9, "签名错误"),
	DUPLICATED(10, "重复登录"),
	BUSY(11, "服务器繁忙，请稍候重试"),
	ILLEGAL(12, "非法的访问"),
	IDLE(13, "无响应断开连接"),
	NO_RESPONSE(14, "服务器未返回,请联系程序员处理相关错误"),
	SAVE(15, "暂时无法存储数据"),
	NOT_READY(16, "服务器还没有准备好, 稍等片刻"),
	KICK_OUT(17, "您的账号在另外一个地方登陆了, 把你踢了下去, 哦耶!"),
	MAINTENANCE(18, "服务器正在维护中, 请稍后"),

	TRANSACTION_EXIST(101, "已经在处理中，不要重复发送，稍候！"),
	TRANSACTION_NOT_EXIST(102, "通信无效，因为没有发现任何要处理的事情！"),
	;

	private int value;
	private String msg;

	CoreErrorCode(int value, String msg)
	{
		this.value = value;
		this.msg = msg;
	}

	@Override
	public int code()
	{
		return value;
	}

	@Override
	public String message()
	{
		return msg;
	}

	public static ErrorCode parseErrorCode(int retcode)
	{
		ErrorCode errorCode = AllCodes.get(retcode);
		if(errorCode == null)
		{
			return new ErrorCode()
			{
				@Override
				public int code()
				{
					return retcode;
				}

				@Override
				public String message()
				{
					return "";
				}
			};
		}
		return errorCode;
	}

	static
	{
		for(CoreErrorCode coreErrorCode : CoreErrorCode.values())
		{
			AllCodes.put(coreErrorCode.code(), coreErrorCode);
		}
	}
}
