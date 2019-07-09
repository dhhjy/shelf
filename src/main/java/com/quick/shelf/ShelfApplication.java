package com.quick.shelf;

import cn.stylefeng.roses.core.config.WebAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * SpringBoot方式启动类
 *
 * @author quick
 * @Date 2017/5/21 12:06
 */
@SpringBootApplication(exclude = {WebAutoConfiguration.class})
@EnableScheduling
@EnableCaching
public class ShelfApplication {

    private final static Logger logger = LoggerFactory.getLogger(ShelfApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ShelfApplication.class, args);
        logger.info(ShelfApplication.class.getSimpleName() + " is success!");
    }
}
