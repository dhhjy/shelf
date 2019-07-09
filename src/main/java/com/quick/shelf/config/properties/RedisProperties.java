package com.quick.shelf.config.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;

/**
 * beetl配置(如果需要配置别的配置可参照这个形式自己添加)
 *
 * @author fengshuonan
 * @date 2017-05-24 20:37
 */
@Data
@Configuration
@ConfigurationProperties(prefix = RedisProperties.REDIS_PREFIX)
public class RedisProperties {
    static final String REDIS_PREFIX = "spring.redis";

    private int database = 0;

    private String url;

    private String host = "localhost";

    private String password;

    private int port = 6379;

    private boolean ssl;

    private int timeout;

    private org.springframework.boot.autoconfigure.data.redis.RedisProperties.Sentinel sentinel;

    private org.springframework.boot.autoconfigure.data.redis.RedisProperties.Cluster cluster;

    private final org.springframework.boot.autoconfigure.data.redis.RedisProperties.Jedis jedis = new org.springframework.boot.autoconfigure.data.redis.RedisProperties.Jedis();

    private final org.springframework.boot.autoconfigure.data.redis.RedisProperties.Lettuce lettuce = new org.springframework.boot.autoconfigure.data.redis.RedisProperties.Lettuce();

    public RedisProperties() {
    }

    public int getDatabase() {
        return this.database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isSsl() {
        return this.ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getTimeout() {
        return this.timeout;
    }

    public org.springframework.boot.autoconfigure.data.redis.RedisProperties.Sentinel getSentinel() {
        return this.sentinel;
    }

    public void setSentinel(org.springframework.boot.autoconfigure.data.redis.RedisProperties.Sentinel sentinel) {
        this.sentinel = sentinel;
    }

    public org.springframework.boot.autoconfigure.data.redis.RedisProperties.Cluster getCluster() {
        return this.cluster;
    }

    public void setCluster(org.springframework.boot.autoconfigure.data.redis.RedisProperties.Cluster cluster) {
        this.cluster = cluster;
    }

    public org.springframework.boot.autoconfigure.data.redis.RedisProperties.Jedis getJedis() {
        return this.jedis;
    }

    public org.springframework.boot.autoconfigure.data.redis.RedisProperties.Lettuce getLettuce() {
        return this.lettuce;
    }

    public static class Lettuce {
        private Duration shutdownTimeout = Duration.ofMillis(100L);
        private org.springframework.boot.autoconfigure.data.redis.RedisProperties.Pool pool;

        public Lettuce() {
        }

        public Duration getShutdownTimeout() {
            return this.shutdownTimeout;
        }

        public void setShutdownTimeout(Duration shutdownTimeout) {
            this.shutdownTimeout = shutdownTimeout;
        }

        public org.springframework.boot.autoconfigure.data.redis.RedisProperties.Pool getPool() {
            return this.pool;
        }

        public void setPool(org.springframework.boot.autoconfigure.data.redis.RedisProperties.Pool pool) {
            this.pool = pool;
        }
    }

    public static class Jedis {
        private org.springframework.boot.autoconfigure.data.redis.RedisProperties.Pool pool;

        public Jedis() {
        }

        public org.springframework.boot.autoconfigure.data.redis.RedisProperties.Pool getPool() {
            return this.pool;
        }

        public void setPool(org.springframework.boot.autoconfigure.data.redis.RedisProperties.Pool pool) {
            this.pool = pool;
        }
    }

    public static class Sentinel {
        private String master;
        private List<String> nodes;

        public Sentinel() {
        }

        public String getMaster() {
            return this.master;
        }

        public void setMaster(String master) {
            this.master = master;
        }

        public List<String> getNodes() {
            return this.nodes;
        }

        public void setNodes(List<String> nodes) {
            this.nodes = nodes;
        }
    }

    public static class Cluster {
        private List<String> nodes;
        private Integer maxRedirects;

        public Cluster() {
        }

        public List<String> getNodes() {
            return this.nodes;
        }

        public void setNodes(List<String> nodes) {
            this.nodes = nodes;
        }

        public Integer getMaxRedirects() {
            return this.maxRedirects;
        }

        public void setMaxRedirects(Integer maxRedirects) {
            this.maxRedirects = maxRedirects;
        }
    }

    public static class Pool {
        private int maxIdle = 8;
        private int minIdle = 0;
        private int maxActive = 8;
        private Duration maxWait = Duration.ofMillis(-1L);

        public Pool() {
        }

        public int getMaxIdle() {
            return this.maxIdle;
        }

        public void setMaxIdle(int maxIdle) {
            this.maxIdle = maxIdle;
        }

        public int getMinIdle() {
            return this.minIdle;
        }

        public void setMinIdle(int minIdle) {
            this.minIdle = minIdle;
        }

        public int getMaxActive() {
            return this.maxActive;
        }

        public void setMaxActive(int maxActive) {
            this.maxActive = maxActive;
        }

        public Duration getMaxWait() {
            return this.maxWait;
        }

        public void setMaxWait(Duration maxWait) {
            this.maxWait = maxWait;
        }
    }
}