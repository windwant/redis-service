package com.redis.base;

/**
 * Created by Administrator on 18-3-20.
 */
public interface BaseConstants {
    String REDIS_LIST_KEY = "redis_list";
    String REDIS_HASH_KEY = "redis_hash";
    String REDIS_SET_KEY = "redis_set";
    String REDIS_ZSET_KEY = "redis_zset";
    String REDIS_BIT_KEY = "redis_bit";
    String REDIS_HLL_KEY = "redis_hll";

    String REDIS_VISIT_USER = "user";

    String REDIS_DEFAULT_HOST = "localhost";
    int REDIS_DEFAULT_PORT = 6379;
}
