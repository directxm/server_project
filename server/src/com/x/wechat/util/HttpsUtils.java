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
     * ����https����
     *
     * @param requestUrl �����ַ
     * @param requestMethod ����ʽ��GET��POST��
     * @param outputStr �ύ������
     * @return JSONObject(ͨ��JSONObject.get(key)�ķ�ʽ��ȡjson���������ֵ)
     */
    public static JSONObject httpsRequest(String requestUrl, String requestMethod, String outputStr)
    {
        JSONObject jsonObject = null;
        try
        {
            // ����SSLContext���󣬲�ʹ������ָ�������ι�������ʼ��
            TrustManager[] tm = { new NullX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // ������SSLContext�����еõ�SSLSocketFactory����
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
            conn.setSSLSocketFactory(ssf);

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // ��������ʽ��GET/POST��
            conn.setRequestMethod(requestMethod);

            // ��outputStr��Ϊnullʱ�������д����
            if (null != outputStr)
            {
                OutputStream outputStream = conn.getOutputStream();
                // ע������ʽ
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // ����������ȡ��������
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null)
            {
                buffer.append(str);
            }

            // �ͷ���Դ
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            jsonObject = (JSONObject)JSONUtils.parse(buffer.toString());
        }
        catch (ConnectException ce)
        {
            Logger.error("���ӳ�ʱ��{}", ce);
        }
        catch (Exception e)
        {
            Logger.error("https�����쳣��{}", e);
        }
        return jsonObject;
    }

    /**
    * ͼƬ���ص����ط�����
    */
    public static String getImageByUrl(String imageUrl, String savePath, String name)
    {
        try
        {
            // ����URL
            URL url = new URL(imageUrl);
            // ������
            URLConnection con = url.openConnection();
            // ������
            InputStream is = con.getInputStream();
            // 1K�����ݻ���
            byte[] bs = new byte[1024];
            // ��ȡ�������ݳ���
            int len;
            // Map<String, Object> property =
            // getProperties("/gbtags.properties");
            File file = new File(savePath);// (String) property.get("ewmPath"));
            if(!file.exists())
            {
                file.mkdirs();
            }
            // ������ļ���
            OutputStream os = new FileOutputStream(savePath + name);
            // ��ʼ��ȡ
            while((len = is.read(bs)) != -1)
            {
                os.write(bs, 0, len);
            }
            // ��ϣ��ر���������
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
