package com.quick.shelf.config.datasource;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 多数据源配置，多数据源配置因为和单数据源冲突，所以现在默认版本删除了多数据源配置
 *
 * @author quick
 * @Date 2017/5/20 21:58
 */
@Configuration
@ConditionalOnProperty(prefix = "shelf.muti-datasource", name = "open", havingValue = "false", matchIfMissing = true)
@EnableTransactionManagement(proxyTargetClass = true)
@MapperScan(basePackages = {"com.quick.shelf.modular.*.mapper"})
public class SingleDataSourceConfig {

}

