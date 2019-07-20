package com.quick.shelf.core.util.xinYanUtils.util;


import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * HTTP 请求工具类
 */
public class HttpUtils {

	private static PoolingHttpClientConnectionManager connMgr;

	private static RequestConfig requestConfig;

    private static final int CONNECTION_TIMEOUT = 500000;// 连接超时时间

    private static final int CONNECTION_REQUEST_TIMEOUT = 500000;// 请求超时时间

    private static final int SOCKET_TIMEOUT = 10000;// 数据读取等待超时

    public static final String HTTP = "http";// http

    private static final String DEFAULT_ENCODING = "UTF-8";// 默认编码

	static {
		// 设置连接池
		connMgr = new PoolingHttpClientConnectionManager();
		// 设置连接池大小
		connMgr.setMaxTotal(100);
		connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());

		RequestConfig.Builder configBuilder = RequestConfig.custom();
		// 设置连接超时
		configBuilder.setConnectTimeout(CONNECTION_TIMEOUT);
		// 设置读取超时
		configBuilder.setSocketTimeout(SOCKET_TIMEOUT);
		// 设置从连接池获取连接实例的超时
		configBuilder.setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT);
		// 在提交请求之前 测试连接是否可用
		configBuilder.setStaleConnectionCheckEnabled(true);
		requestConfig = configBuilder.build();
	}

	/**
	 * 发送 GET 请求（HTTP），不带输入数据
	 *
	 * @param url
	 * @param headers
	 *            需要添加的httpheader参数
	 * @return
	 */
	public static String doGet(String url, Map<String, String> headers, Map<String, Object> params) {

		if (url.startsWith("https://")) {
			return doGetSSL(url, headers, params);
		} else {
			return doGetHttp(url, headers, params);
		}
	}

	/**
	 * 发送 GET 请求（HTTP），不带输入数据
	 *
	 * @param url
	 *            需要添加的httpheader参数
	 * @return
	 */
	public static String doGet(String url, Map<String, Object> params) {

		if (url.startsWith("https://")) {
			return doGetSSL(url, null, params);
		} else {
			return doGetHttp(url, null, params);
		}
	}

	/**
	 * 发送 GET 请求（HTTP），K-V形式
	 *
	 * @param url
	 * @param headers
	 *            需要添加的httpheader参数
	 * @param params
	 * @return
	 */
	private static String doGetHttp(String url, Map<String, String> headers, Map<String, Object> params) {
		HttpClient httpclient = new DefaultHttpClient();
		String apiUrl = url;
		StringBuffer param = new StringBuffer();
		int i = 0;
		for (String key : params.keySet()) {
			if (i == 0)
				param.append("?");
			else
				param.append("&");
			param.append(key).append("=").append(params.get(key));
			i++;
		}
		apiUrl += param;
		String result = null;
		log("url: " + apiUrl);
		try {
			HttpGet httpGet = new HttpGet(apiUrl);
			// 添加http headers
			if (headers != null && headers.size() > 0) {
				for (String key : headers.keySet()) {
					httpGet.addHeader(key, headers.get(key));
				}
			}

			HttpResponse response = httpclient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();

			log("code : " + statusCode);

			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				result = IOUtils.toString(instream, DEFAULT_ENCODING);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 发送 SSL Get 请求（HTTPS），K-V形式
	 *
	 * @param apiUrl
	 *            API接口URL
	 * @param headers
	 * @param params
	 *            参数map
	 * @return
	 */
	private static String doGetSSL(String url, Map<String, String> headers, Map<String, Object> params) {
		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory())
				.setConnectionManager(connMgr)
                .setDefaultRequestConfig(requestConfig)
                .build();
		CloseableHttpResponse response = null;
		String apiUrl = url;
		String httpStr = null;
		StringBuffer param = new StringBuffer();
		int i = 0;
		for (String key : params.keySet()) {
			if (i == 0)
				param.append("?");
			else
				param.append("&");
			param.append(key).append("=").append(params.get(key));
			i++;
		}
		apiUrl += param;
		log("url: " + apiUrl);

		try {
			HttpGet httpGet = new HttpGet(apiUrl);
			httpGet.setConfig(requestConfig);
			// 添加http headers
			if (headers != null && headers.size() > 0) {
				for (String key : headers.keySet()) {
					httpGet.addHeader(key, headers.get(key));
				}
			}
			response = httpClient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			log("code : " + statusCode);
			if (statusCode != HttpStatus.SC_OK) {
				return null;
			}
			HttpEntity entity = response.getEntity();
			if (entity == null) {
				return null;
			}
			httpStr = EntityUtils.toString(entity, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					EntityUtils.consume(response.getEntity());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return httpStr;

	}

	/**
	 * 发送 POST 请求（HTTP），K-V形式
	 *
	 * @param url
	 * @param headers
	 *            需要添加的httpheader参数
	 * @return
	 */
	public static String doPostByForm(String url, Map<String, String> headers, Map<String, Object> params) {
		if (url.startsWith("https://")) {
			return doPostSSL(url, headers, params);
		} else {
			return doPostHttp(url, headers, params);
		}
	}

	/**
	 * 发送 POST 请求（HTTP），K-V形式
	 *
	 * @param url
	 * @param headers
	 *            需要添加的httpheader参数
	 * @return
	 */
	public static String doPost(String url, Map<String, Object> params) {
		if (url.startsWith("https://")) {
			return doPostSSL(url, null, params);
		} else {
			return doPostHttp(url, null, params);
		}
	}

	/**
	 * 发送 POST 请求（HTTP），JSON形式
	 *
	 * @param url
	 * @param headers
	 *            需要添加的httpheader参数
	 * @return
	 */
	public static String doPostByJson(String url, Map<String, String> headers, Object json) {
		if (url.startsWith("https://")) {
			return doPostSSL(url, headers, json);
		} else {
			return doPostHttp(url, headers, json);
		}

	}

	/**
	 * 发送 POST 请求（HTTP），K-V形式
	 *
	 * @param apiUrl
	 *            API接口URL
	 * @param headers
	 *            需要添加的httpheader参数
	 * @param params
	 *            参数map
	 * @return
	 */
	private static String doPostHttp(String apiUrl, Map<String, String> headers, Map<String, Object> params) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String httpStr = null;
		HttpPost httpPost = new HttpPost(apiUrl);
		CloseableHttpResponse response = null;
		if (headers != null) {
			Set<String> keys = headers.keySet();
            for (String key : keys) {
                httpPost.addHeader(key, headers.get(key));
            }
		}
		try {
			httpPost.setConfig(requestConfig);
			List<NameValuePair> pairList = new ArrayList<>(params.size());
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
				pairList.add(pair);
			}
			httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName(DEFAULT_ENCODING)));
			response = httpClient.execute(httpPost);
			log(response.toString());
			HttpEntity entity = response.getEntity();
			httpStr = EntityUtils.toString(entity, DEFAULT_ENCODING);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					EntityUtils.consume(response.getEntity());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return httpStr;
	}

	/**
	 * 发送 POST 请求（HTTP），JSON形式
	 *
	 * @param apiUrl
	 * @param headers
	 *            需要添加的httpheader参数
	 * @param json
	 *            json对象
	 * @return
	 */
	private static String doPostHttp(String apiUrl, Map<String, String> headers, Object json) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String httpStr = null;
		HttpPost httpPost = new HttpPost(apiUrl);
		CloseableHttpResponse response = null;
		if (headers != null) {
			Set<String> keys = headers.keySet();
            for (String key : keys) {
                httpPost.addHeader(key, headers.get(key));

            }
		}
		try {
			httpPost.setConfig(requestConfig);
			StringEntity stringEntity = new StringEntity(json.toString(), DEFAULT_ENCODING);// 解决中文乱码问题
			stringEntity.setContentEncoding(DEFAULT_ENCODING);
			stringEntity.setContentType("application/json");
			httpPost.setEntity(stringEntity);
			response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			log(response.getStatusLine().getStatusCode() + "");
			httpStr = EntityUtils.toString(entity, DEFAULT_ENCODING);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					EntityUtils.consume(response.getEntity());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return httpStr;
	}

	/**
	 * 发送 SSL POST 请求（HTTPS），K-V形式
	 *
	 * @param apiUrl
	 *            API接口URL
	 * @param params
	 *            参数map
	 * @return
	 */
	private static String doPostSSL(String apiUrl, Map<String, String> headers, Map<String, Object> params) {
		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory())
				.setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();
		HttpPost httpPost = new HttpPost(apiUrl);
		CloseableHttpResponse response = null;
		String httpStr = null;
		if (headers != null) {
			Set<String> keys = headers.keySet();
            for (String key : keys) {
                httpPost.addHeader(key, headers.get(key));

            }
		}
		try {
			httpPost.setConfig(requestConfig);
			List<NameValuePair> pairList = new ArrayList<>(params.size());
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
				pairList.add(pair);
			}
			httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName(DEFAULT_ENCODING)));
			response = httpClient.execute(httpPost);
			log("response:"+response);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				return null;
			}
			HttpEntity entity = response.getEntity();
			if (entity == null) {
				return null;
			}
			httpStr = EntityUtils.toString(entity, DEFAULT_ENCODING);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					EntityUtils.consume(response.getEntity());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return httpStr;
	}

	/**
	 * 发送 SSL POST 请求（HTTPS），JSON形式
	 *
	 * @param apiUrl
	 *            API接口URL
	 * @param json
	 *            JSON对象
	 * @return
	 */
	private static String doPostSSL(String apiUrl, Map<String, String> headers, Object json) {
		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory())
				.setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();
		HttpPost httpPost = new HttpPost(apiUrl);
		CloseableHttpResponse response = null;
		String httpStr = null;
		if (headers != null) {
			Set<String> keys = headers.keySet();
            for (String key : keys) {
                httpPost.addHeader(key, headers.get(key));

            }
		}
		try {
			httpPost.setConfig(requestConfig);
			StringEntity stringEntity = new StringEntity(json.toString(), DEFAULT_ENCODING);// 解决中文乱码问题
			stringEntity.setContentEncoding(DEFAULT_ENCODING);
			stringEntity.setContentType("application/json");
			httpPost.setEntity(stringEntity);
			response = httpClient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				return null;
			}
			HttpEntity entity = response.getEntity();
			if (entity == null) {
				return null;
			}
			httpStr = EntityUtils.toString(entity, DEFAULT_ENCODING);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					EntityUtils.consume(response.getEntity());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return httpStr;
	}

	/**
	 * 创建SSL安全连接
	 *
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	private static SSLConnectionSocketFactory createSSLConnSocketFactory() {
		// 创建TrustManager
		X509TrustManager xtm = new X509TrustManager() {
			public void checkClientTrusted(X509Certificate[] chain, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] chain, String authType) {
			}

			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};
		// TLS1.0与SSL3.0基本上没有太大的差别，可粗略理解为TLS是SSL的继承者，但它们使用的是相同的SSLContext
		SSLContext ctx;
		try {
			ctx = SSLContext.getInstance("TLS");
			// 使用TrustManager来初始化该上下文，TrustManager只是被SSL的Socket所使用
			ctx.init(null, new TrustManager[] { xtm }, null);
            return new SSLConnectionSocketFactory(ctx);
		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
	}

	public static void log(String msg) {
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "\t: " + msg);
	}

	/**
	 * 测试方法
	 *
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

	}
}