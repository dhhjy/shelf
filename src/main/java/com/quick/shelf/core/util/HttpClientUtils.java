package com.quick.shelf.core.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * HttpClient工具类
 */
public class HttpClientUtils {
    private static Log log = LogFactory.getLog(HttpClientUtils.class);

    private static final int CONNECTION_TIMEOUT = 500000;// 连接超时时间

    private static final int CONNECTION_REQUEST_TIMEOUT = 500000;// 请求超时时间

    private static final int SOCKET_TIMEOUT = 10000;// 数据读取等待超时

    public static final String HTTP = "http";// http

    private static final String HTTPS = "https";// https

    private static final String DEFAULT_ENCODING = "UTF-8";// 默认编码

    /**
     * get请求(1.处理http请求;2.处理https请求,信任所有证书)[默认编码:UTF-8]
     *
     * @param url (参数直接拼接到URL后面，即http://test.com?a=1&b=2的形式)
     * @return
     */
    public static String get(String url) {
        List<BasicNameValuePair> list = new ArrayList<>();
        return get(url, list, DEFAULT_ENCODING);
    }

    /**
     * get请求(1.处理http请求;2.处理https请求,信任所有证书)[默认编码:UTF-8]
     *
     * @param url    (url不带参数，例：http://test.com)
     * @param reqMap (参数放置到一个map中)
     * @return
     */
    public static String doGet(String url, List<BasicNameValuePair> params) {
        return get(url, params, DEFAULT_ENCODING);
    }

    /**
     * get请求(1.处理http请求;2.处理https请求,信任所有证书)
     *
     * @param url      (只能是http或https请求)
     * @param encoding
     * @return
     */
    public static String get(String url, List<BasicNameValuePair> params, String encoding) {
        String result = "";
        if (StringUtils.isBlank(url)) {
            log.info("----->url为空");
            return result;
        }

        CloseableHttpClient httpClient;
        if (url.startsWith(HTTPS)) {
            // 创建一个SSL信任所有证书的httpClient对象
            httpClient = HttpClientUtils.createSSLInsecureClient();
        } else {
            httpClient = HttpClients.createDefault();
        }

        CloseableHttpResponse response = null;
        HttpGet httpGet;
        List<NameValuePair> pa;
        try {
            if (params.size() > 0) {
                URIBuilder builder = new URIBuilder(url);
                if (params.size() > 0) {
                    pa = new ArrayList<>(params);
                    builder.setParameters(pa);
                }
                httpGet = new HttpGet(builder.build());
            } else {
                httpGet = new HttpGet(url);
            }

            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(CONNECTION_TIMEOUT)   //设置连接超时时间
                    .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT) // 设置请求超时时间
                    .setSocketTimeout(SOCKET_TIMEOUT)
                    .setRedirectsEnabled(true)//默认允许自动重定向
                    .build();
            httpGet.setConfig(requestConfig);

            // 发送请求，并接收响应
            response = httpClient.execute(httpGet);

            result = handleResponse(url, encoding, response);
        } catch (Exception e) {
            log.error("----->url:" + url + ", get请求异常:" + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResource(httpClient, response);
        }

        return result;
    }


    /**
     * post请求(1.处理http请求;2.处理https请求,信任所有证书)[默认编码:UTF-8]
     *
     * @param url
     * @param reqMap
     * @return
     */
    public static String doPost(String url, List<BasicNameValuePair> params) {
        return post(url, params, DEFAULT_ENCODING);
    }

    /**
     * post请求(1.处理http请求;2.处理https请求,信任所有证书)
     *
     * @param url
     * @param reqMap   入参是个map
     * @param encoding
     * @return
     */
    public static String post(String url, List<BasicNameValuePair> params, String encoding) {
        String result = "";
        if (StringUtils.isBlank(url)) {
            log.info("----->url为空");
            return result;
        }

        CloseableHttpClient httpClient;
        if (url.startsWith(HTTPS)) {
            // 创建一个SSL信任所有证书的httpClient对象
            httpClient = HttpClientUtils.createSSLInsecureClient();
        } else {
            httpClient = HttpClients.createDefault();
        }
        CloseableHttpResponse response = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(CONNECTION_TIMEOUT)   //设置连接超时时间
                    .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT) // 设置请求超时时间
                    .setSocketTimeout(SOCKET_TIMEOUT)
                    .setRedirectsEnabled(true)//默认允许自动重定向
                    .build();
            httpPost.setConfig(requestConfig);
            httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
            httpPost.setEntity(new UrlEncodedFormEntity(params, encoding));

            // 发送请求，并接收响应
            response = httpClient.execute(httpPost);
            result = handleResponse(url, encoding, response);
        } catch (IOException e) {
            log.error("-----> url:" + url + ",post请求异常:" + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResource(httpClient, response);
        }

        return result;
    }

    /**
     * post请求(1.处理http请求;2.处理https请求,信任所有证书)
     *
     * @param url
     * @param jsonParams 入参是个json字符串
     * @param encoding
     * @return
     */
    public static String post(String url, String jsonParams, String encoding) {
        String result = "";
        if (StringUtils.isBlank(url)) {
            log.info("----->url为空");
            return result;
        }

        CloseableHttpClient httpClient;
        if (url.startsWith(HTTPS)) {
            // 创建一个SSL信任所有证书的httpClient对象
            httpClient = HttpClientUtils.createSSLInsecureClient();
        } else {
            httpClient = HttpClients.createDefault();
        }
        CloseableHttpResponse response = null;

        try {
            HttpPost httpPost = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(CONNECTION_TIMEOUT)   //设置连接超时时间
                    .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT) // 设置请求超时时间
                    .setSocketTimeout(SOCKET_TIMEOUT)
                    .setRedirectsEnabled(true)//默认允许自动重定向
                    .build();
            httpPost.setConfig(requestConfig);

            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setEntity(new StringEntity(jsonParams, ContentType.create("application/json", encoding)));

            // 发送请求，并接收响应
            response = httpClient.execute(httpPost);
            result = handleResponse(url, encoding, response);
        } catch (IOException e) {
            log.error("-----> url:" + url + ",post请求异常:" + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResource(httpClient, response);
        }

        return result;
    }

    /**
     * 创建一个SSL信任所有证书的httpClient对象
     *
     * @return
     */
    private static CloseableHttpClient createSSLInsecureClient() {
        try {
            // 默认信任所有证书
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, (TrustStrategy) (chain, authType) -> true).build();

            SSLConnectionSocketFactory sslcsf = new SSLConnectionSocketFactory(sslContext);

            return HttpClients.custom().setSSLSocketFactory(sslcsf).build();
        } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
            e.printStackTrace();
        }

        return HttpClients.createDefault();
    }

    /**
     * 处理响应，获取响应报文
     *
     * @param url
     * @param encoding
     * @param response
     * @return
     * @throws IOException
     */
    private static String handleResponse(String url, String encoding, CloseableHttpResponse response) {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;

        try {
            if (response != null) {
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    // 获取响应实体
                    HttpEntity entity = response.getEntity();

                    if (entity != null) {
                        br = new BufferedReader(new InputStreamReader(entity.getContent(), encoding));
                        String s;
                        while ((s = br.readLine()) != null) {
                            sb.append(s);
                        }
                    }

                    // 释放entity
                    EntityUtils.consume(entity);
                } else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
                    log.info("-----> get请求404,未找到资源. url:" + url);
                } else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                    log.info("-----> get请求500,服务器端异常. url:" + url);
                }
            }
        } catch (Exception e) {
            log.error("----->url:" + url + ",处理响应，获取响应报文异常：" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
    }

    /**
     * 释放资源
     *
     * @param httpClient
     * @param response
     */
    private static void closeResource(CloseableHttpClient httpClient, CloseableHttpResponse response) {
        if (response != null) {
            try {
                response.close();
            } catch (IOException e) {
                log.error("-----> 释放response资源异常:" + e.getMessage());
                e.printStackTrace();
            }
        }

        if (httpClient != null) {
            try {
                httpClient.close();
            } catch (Exception e) {
                log.error("-----> 释放httpclient资源异常:" + e.getMessage());
                e.printStackTrace();
            }
        }
    }


    /**
     * 采用绕过验证的方式处理https请求
     *
     * @param url
     * @param reqMap
     * @param encoding
     * @return
     */
    public static String postSSLUrl(String url, Map<String, String> reqMap, String encoding) {
        String result = "";
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        // 添加参数
        List<NameValuePair> params = new ArrayList<>();
        if (reqMap != null && reqMap.keySet().size() > 0) {
            for (Map.Entry<String, String> entity : reqMap.entrySet()) {
                params.add(new BasicNameValuePair(entity.getKey(), entity.getValue()));
            }
        }

        try {
            //采用绕过验证的方式处理https请求
            SSLContext sslcontext = createIgnoreVerifySSL();
            //设置协议http和https对应的处理socket链接工厂的对象
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", new SSLConnectionSocketFactory(sslcontext))
                    .build();
            PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            HttpClients.custom().setConnectionManager(connManager);

            //创建自定义的httpclient对象
            httpClient = HttpClients.custom().setConnectionManager(connManager).build();

            //创建post方式请求对象
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params, encoding));

            //指定报文头Content-type、User-Agent
            httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
            //httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:6.0.2) Gecko/20100101 Firefox/6.0.2");

            //执行请求操作，并拿到结果（同步阻塞）
            response = httpClient.execute(httpPost);
            result = handleResponse(url, encoding, response);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResource(httpClient, response);
        }

        return result;
    }

    /**
     * 绕过验证
     *
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    private static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("SSLv3");

        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sc.init(null, new TrustManager[]{trustManager}, null);
        return sc;
    }

    public static void main(String[] args) {
        System.out.println(get("http://page.limuzhengxin.com/page/web/task?apiKey=1807072515702764&version=1.2.0&token=f352e29218e74802a0f5f2a783005098&bizType=lifangupgradecheck&sign=953b65f6fe2beef065d2c562850a707c0b77cbbb"));
    }

}

