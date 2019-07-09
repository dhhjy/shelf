package com.quick.shelf;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Guns Web程序启动类
 *
 * @author ken
 * @date 2017-05-21 9:43
 */
public class ShelfServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ShelfApplication.class);
    }
}
