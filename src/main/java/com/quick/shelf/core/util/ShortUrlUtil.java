package com.quick.shelf.core.util;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName shortUrl
 * @Description TODO
 * @Author ken
 * @Date 2019/7/26 12:44
 * @Version 1.0
 */
public class ShortUrlUtil {
    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(ShortUrlUtil.class);

    /**
     * 短链接网址
     */
    private static final String SUO_URL = "http://api.suolink.cn/api.htm";

    /**
     * key
     */
    private static final String SUO_KEY = "5d16c9168e676d19747853ec@ea2cbffba24aba46e69f897dff284113";

    public static String getShortUrl(String oldUrl) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("url", oldUrl));
        params.add(new BasicNameValuePair("key", ShortUrlUtil.SUO_KEY));
        params.add(new BasicNameValuePair("expireDate", DateUtil.formatDateToDateString(new Date())));
        String newUrl = HttpClientUtil.doGet(ShortUrlUtil.SUO_URL, params);
        logger.info("原始网址：{} 转换后的网址：{}", oldUrl, newUrl);
        return newUrl;
    }
}
