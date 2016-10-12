#cache

JedisPool jedis 链接连接池 单机
ShardedJedisPool 切片连接池 分布式（多个redis 运行实例） 根据一致性那个hash算法动态均匀存储及获取key-value
JedisSentinelPool 哨兵模式
运行多个redis实例 配置主从配置 slaveof host port

配置sentinel实例
port 6376 //sentinel端口
sentinel monitor mymaster 127.0.0.1 6379 2  //master配置
sentinel down-after-milliseconds mymaster 60000
sentinel failover-timeout mymaster 180000
sentinel parallel-syncs mymaster 1