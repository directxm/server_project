package com.x.wechat;

import com.x.logging.Logger;
import com.x.wechat.common.AccessToken;

/**
 * 类名: AccessTokenThread </br>
 * 描述: 定时获取微信access_token的线程 </br>
 * 开发人员: fatum </br>
 * 创建时间: 16-10-10 </br>
 * 发布版本: V1.0 </br>
 *           V1.1 Token 统一为 AccessToken
 */
@Deprecated
public class AccessTokenThread implements Runnable
{
	// 第三方用户唯一凭证
    public final String appid = "";
    // 第三方用户唯一凭证密钥
    public final String appsecret = "";
    public volatile AccessToken accessToken = null;

    public void run()
    {
        while (true)
        {
            try
            {
                //accessToken = CommonUtil.getToken(appid, appsecret);
                if (null != accessToken)
                {
                    // 调用存储到数据库
                    //TokenUtil.saveToken(accessToken);
                    Logger.info("获取access_token成功，有效时长{}秒 token:{}", accessToken.getExpiresIn(), accessToken.getToken());
                    // 休眠7000秒
                    Thread.sleep((accessToken.getExpiresIn() - 200)*1000);
                }
                else
                {
                    // 如果access_token为null，60秒后再获取
                    Thread.sleep(60 * 1000);
                }
            }
            catch (InterruptedException e)
            {
                try
                {
                    Thread.sleep(60 * 1000);
                }
                catch (InterruptedException e1)
                {
                    Logger.error("{}", e1);
                }
                Logger.error("{}", e);
            }
        }
    }
}
