package com.x.wechat;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

/**
 * 类名: NullX509TrustManager </br>
 * 描述: 信任管理器 </br>
 * 开发人员: fatum </br>
 * 创建时间: 16-10-10 </br>
 * 发布版本: V1.0 </br>
 */
public class NullX509TrustManager implements X509TrustManager
{
    // 检查客户端证书
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException
    {
    }

    // 检查服务器端证书
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException
    {
    }

    // 返回受信任的X509证书数组
    public X509Certificate[] getAcceptedIssuers()
    {
        return null;
    }
}
