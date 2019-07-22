package com.quick.shelf.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * spring session配置
 *  60s *
 * @author fengshuonan
 * @date 2017-07-13 21:05
 */
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 18000)  //session过期时间  如果部署多机环境,需要打开注释
@ConditionalOnProperty(prefix = "shelf", name = "spring-session-open", havingValue = "true")
public class SpringSessionConfig {

}
