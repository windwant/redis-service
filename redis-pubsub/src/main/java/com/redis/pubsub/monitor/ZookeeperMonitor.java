package com.redis.pubsub.monitor;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent.Type;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * zookeeper服务监控
 */
@Component
public class ZookeeperMonitor {
    private static final Logger logger = LoggerFactory.getLogger(ZookeeperMonitor.class);

    @Value("${zookeeper.host}")
    private String host;
    @Value("${zookeeper.path}")
    private String path;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    @Value("${zookeeper.parent.path}")
    private String parentPath; //注册父路径

    private CuratorFramework curatorFramework; //high-level API that greatly simplifies using ZooKeeper

    public ZookeeperMonitor() {

    }

    @PostConstruct //method that needs to be executed after dependency injection is done to perform any initialization
    public void start(){
        //重试策略 ExponentialBackoffRetry：重试指定的次数, 且每一次重试之间停顿的时间逐渐增加
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        //Factory methods for creating framework-style clients
        curatorFramework = CuratorFrameworkFactory.builder().connectString(host).retryPolicy(retryPolicy).build();
        curatorFramework.start();

        //utility that attempts to keep all data from all children of a ZK path locally cached watch and response
        final PathChildrenCache pathChildrenCache = new PathChildrenCache(curatorFramework, parentPath, true);
        try {
            //cached之后分发事件
            pathChildrenCache.start(StartMode.POST_INITIALIZED_EVENT);
            //添加监听
            pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
                public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                    if(event.getType() == Type.CHILD_REMOVED){
                        if((parentPath + path).equals(event.getData().getPath())){
                            logger.info("path: {}, removed, begin recreate!", path);
                            createNode();
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        createNode();
    }

    /**
     * 创建节点
     * @return
     */
    protected boolean createNode(){
        try {
            String nodePath = parentPath + path;
            if(curatorFramework.checkExists().forPath(nodePath) == null){
                curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(nodePath);
                logger.info("create node path: {}", nodePath);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  false;
    }
}
