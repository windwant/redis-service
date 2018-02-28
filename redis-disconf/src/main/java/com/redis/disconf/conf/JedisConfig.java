package com.redis.disconf.conf;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;
import com.baidu.disconf.client.common.update.IDisconfUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * Redis配置文件 获取不到则使用本地
 *
 * @author windwant
 * @version 2017-6-8
 */
@Service
@Scope("singleton")
@DisconfFile(filename = "redis.properties") //配置中心 配置文件名
public class JedisConfig implements IDisconfUpdate {

    private static final Logger logger = LoggerFactory.getLogger(JedisConfig.class);

    // 代表连接地址
    private String host;

    // 代表连接port
    private int port;

    /**
     * 地址
     *
     * @return
     */
    @DisconfFileItem(name = "redis.host", associateField = "host") //对应配置item
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    /**
     * 端口
     *
     * @return
     */
    @DisconfFileItem(name = "redis.port", associateField = "port")
    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    //监听重载
    public void reload() throws Exception {
        logger.info("properties reload: host {}, port {}", host, port);
    }
}