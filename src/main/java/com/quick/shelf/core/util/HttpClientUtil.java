package com.quick.shelf.core.util;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName HttpClientUtil
 * @Description TODO
 * @Author ken
 * @Date 2019/7/14 14:10
 * @Version 1.0
 */
public class HttpClientUtil {

    public static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    /**
     * 发送GET请求
     *
     * @param path
     * @param parametersBody
     * @return
     * @throws URISyntaxException
     */
    public static String doGet(String path, List<NameValuePair> parametersBody) {
        HttpGet get = null;
        try {
            URIBuilder uriBuilder = new URIBuilder(path);
            uriBuilder.setParameters(parametersBody);
                get = new HttpGet(uriBuilder.build());
            HttpClient client = HttpClientBuilder.create().build();
            HttpResponse response = client.execute(get);
            int code = response.getStatusLine().getStatusCode();
            if (code >= 400)
                throw new RuntimeException("Could not access protected resource. Server returned http code: " + code);
            return EntityUtils.toString(response.getEntity());
        } catch (ClientProtocolException e) {
            logger.error("doPost 发生 连接协议 异常，异常信息：{}", e.getMessage());
        } catch (IOException e) {
            logger.error("doPost 发生 IO 异常，异常信息：{}", e.getMessage());
        }  catch (URISyntaxException e) {
            logger.error("doPost 发生异常，异常信息：{}", e.getMessage());
        } finally {
            assert get != null;
            get.releaseConnection();
        }
        return null;
    }

    // 发送POST请求（普通表单形式）
    public static String doPostForm(String path, List<NameValuePair> parametersBody) {
        HttpEntity entity = new UrlEncodedFormEntity(parametersBody, Charsets.UTF_8);
        return doPost(path, "application/x-www-form-urlencoded", entity);
    }

    /**
     * 发送POST 请求
     * @param url 地址
     * @param map Map<String, String> 类型的参数
     * @return
     */
    public static String doPostMap(String url, Map<String, String> map) {
        Gson gson = new Gson();
        String json = gson.toJson(map, new TypeToken<Map<String, String>>() {
        }.getType());
        StringEntity entity = new StringEntity(json, Charsets.UTF_8);
        return doPost(url, "application/json", entity);
    }

    /**
     * 发送
     *
     * @param path
     * @param mediaType
     * @param entity
     * @return
     */
    private static String doPost(String url, String mediaType, HttpEntity entity) {
        logger.debug("[postRequest] resourceUrl: {}", url);
        HttpPost post = new HttpPost(url);
        post.addHeader("Content-Type", mediaType);
        post.addHeader("Accept", "application/json");
        post.setEntity(entity);
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpResponse response = client.execute(post);
            int code = response.getStatusLine().getStatusCode();
            if (code >= 400)
                throw new RuntimeException(EntityUtils.toString(response.getEntity()));
            logger.error("doPost 发生异常，CODE={}", code);
            return EntityUtils.toString(response.getEntity());
        } catch (ClientProtocolException e) {
            logger.error("doPost 发生 连接协议 异常，异常信息：{}", e.getMessage());
        } catch (IOException e) {
            logger.error("doPost 发生 IO 异常，异常信息：{}", e.getMessage());
        } finally {
            post.releaseConnection();
        }
        return null;
    }

    public static void main(String[] args) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("ip", "218.192.3.42"));
        System.out.println(doGet("http://ip.taobao.com/service/getIpInfo.php", params));
    }
}
