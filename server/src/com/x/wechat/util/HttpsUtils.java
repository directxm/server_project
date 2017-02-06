package com.x.wechat.util;

import com.alibaba.fastjson.JSONObject;
import com.x.logging.Logger;
import com.x.util.JSONUtils;
import com.x.wechat.NullX509TrustManager;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by fatum on 2016/11/7.
 */
public class HttpsUtils
{
	/**
     * 发送https请求
     *
     * @param requestUrl 请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr 提交的数据
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
     */
    public static JSONObject httpsRequest(String requestUrl, String requestMethod, String outputStr)
    {
        JSONObject jsonObject = null;
        try
        {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new NullX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
            conn.setSSLSocketFactory(ssf);

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            conn.setRequestMethod(requestMethod);

            // 当outputStr不为null时向输出流写数据
            if (null != outputStr)
            {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null)
            {
                buffer.append(str);
            }

            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            jsonObject = (JSONObject)JSONUtils.parse(buffer.toString());
        }
        catch (ConnectException ce)
        {
            Logger.error("连接超时：{}", ce);
        }
        catch (Exception e)
        {
            Logger.error("https请求异常：{}", e);
        }
        return jsonObject;
    }

    /**
    * 图片下载到本地服务器
    */
    public static String getImageByUrl(String imageUrl, String savePath, String name)
    {
        try
        {
            // 构造URL
            URL url = new URL(imageUrl);
            // 打开连接
            URLConnection con = url.openConnection();
            // 输入流
            InputStream is = con.getInputStream();
            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // Map<String, Object> property =
            // getProperties("/gbtags.properties");
            File file = new File(savePath);// (String) property.get("ewmPath"));
            if(!file.exists())
            {
                file.mkdirs();
            }
            // 输出的文件流
            OutputStream os = new FileOutputStream(savePath + name);
            // 开始读取
            while((len = is.read(bs)) != -1)
            {
                os.write(bs, 0, len);
            }
            // 完毕，关闭所有链接
            os.close();
            is.close();
            return "success";
        }
        catch (Exception e)
        {
            return "error";
        }
    }

}
