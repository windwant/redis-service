# redis
java spring redies

1. 结合Spring 简单 订阅/发布系统。
2. redis缓存
   JedisPool jedis 链接连接池 单机
   ShardedJedisPool 切片连接池 分布式（多个redis 运行实例） 根据一致性那个hash算法动态均匀存储及获取key-value
   JedisSentinelPool 哨兵模式
