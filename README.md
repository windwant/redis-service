# redis
java spring redis

1. 结合Spring 简单 订阅/发布系统。

2. redis 缓存应用

   JedisPool jedis 链接连接池 单机

   ShardedJedisPool 切片连接池 分布式（多个redis 运行实例） 根据一致性那个hash算法动态均匀存储及获取key-value

   JedisSentinelPool 哨兵模式

3. redis 统一配置管理

4. lua脚本实现分布式锁

    EVAL script numkeys key [key ...] arg [arg ...]


    起始版本：2.6.0

    时间复杂度：取决于脚本本身的执行的时间复杂度。
