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


Redis Sentinel provides high availability for Redis. In practical terms this means that using Sentinel you can create a Redis deployment that resists without human intervention to certain kind of failures.
Redis Sentinel also provides other collateral tasks such as monitoring, notifications and acts as a configuration provider for clients.
This is the full list of Sentinel capabilities at a macroscopical level (i.e. the big picture):

	* Monitoring. Sentinel constantly checks if your master and slave instances are working as expected.
	* Notification. Sentinel can notify the system administrator, another computer programs, via an API, that something is wrong with one of the monitored Redis instances.
	* Automatic failover. If a master is not working as expected, Sentinel can start a failover process where a slave is promoted to master, the other additional slaves are reconfigured to use the new master, and the applications using the Redis server informed about the new address to use when connecting.
	* Configuration provider. Sentinel acts as a source of authority for clients service discovery: clients connect to Sentinels in order to ask for the address of the current Redis master responsible for a given service. If a failover occurs, Sentinels will report the new address.

