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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by windwant on 2017/2/4.
 */
@Component
public class ZookeeperMonitor {
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
    private String parentPath;

    private CuratorFramework curatorFramework;

    public ZookeeperMonitor() {

    }

    @PostConstruct
    public void start(){
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        curatorFramework = CuratorFrameworkFactory.builder().connectString(host).retryPolicy(retryPolicy).build();
        curatorFramework.start();

        final PathChildrenCache pathChildrenCache = new PathChildrenCache(curatorFramework, parentPath, true);
        try {
            pathChildrenCache.start(StartMode.POST_INITIALIZED_EVENT);
            pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
                public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                    if(event.getType() == Type.CHILD_REMOVED){
                        if((parentPath + path).equals(event.getData().getPath())){
                            System.out.println("path " + path + " removed, begin recreate!");
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

    protected boolean createNode(){
        try {
            String nodePath = parentPath + path;
            if(curatorFramework.checkExists().forPath(nodePath) == null){
                curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(nodePath);
                System.out.println("create node path: " + nodePath);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  false;
    }
}
