package cn.bulletjet.headline.util;

import cn.bulletjet.headline.service.QiniuService;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;

import java.util.List;

@Service
public class JedisAdapter implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);


    public static void print(int index, Object objet) {
        System.out.println(String.format("%d,%s", index, objet.toString()));
    }

    public static void main(String[] args) {
        Jedis jedis = new Jedis();
        jedis.flushAll();

        jedis.set("hello", "world");
        print(1, jedis.get("hello"));
        jedis.rename("hello", "newhello");
        print(1, jedis.get("newhello"));
        jedis.setex("yhl", 15, "wzj");
        jedis.set("pv", "100");
        jedis.incr("pv");
        print(2, jedis.get("pv"));
        jedis.incrBy("pv", 12);
        print(2, jedis.get("pv"));
        String listname = "listA";
        for (int i = 0; i < 10; i++) {
            jedis.lpush(listname, String.valueOf(i));
        }
        print(3, jedis.lrange(listname, 0, -1));
        print(4, jedis.lindex(listname, 3));
        jedis.linsert(listname, BinaryClient.LIST_POSITION.AFTER, "9", "10");
        jedis.linsert(listname, BinaryClient.LIST_POSITION.BEFORE, "0", "nn");
        print(5, jedis.lrange(listname, 0, -1));
        String userKey = "user1";
        jedis.hset(userKey, "name", "yhl");
        jedis.hset(userKey, "password", "123456");

        print(6, jedis.hget(userKey, "name"));
        print(7, jedis.hgetAll(userKey));
        print(8, jedis.hkeys(userKey));
        print(9, jedis.hvals(userKey));
        print(10, jedis.hexists(userKey, "email"));
        print(10, jedis.hexists(userKey, "name"));

        String likekeys = "likes";
        String likekeys1 = "likes2";
        for (int i = 0; i < 10; i++) {
            jedis.sadd(likekeys, String.valueOf(i));
            jedis.sadd(likekeys1, String.valueOf(i * 2));
        }
        print(11, jedis.smembers(likekeys));
        print(12, jedis.smembers(likekeys1));
        print(13, jedis.sinter(likekeys, likekeys1));
        print(14, jedis.sunion(likekeys, likekeys1));
        print(15, jedis.sdiff(likekeys, likekeys1));
        jedis.srem(likekeys, "5");
        print(12, jedis.smembers(likekeys));
        jedis.scard(likekeys);
        jedis.smove(likekeys1, likekeys, "14");
        print(16, jedis.scard(likekeys));
        print(16, jedis.smembers(likekeys));


        String rankkey = "rankkey";
        jedis.zadd(rankkey, 15, "jim");
        jedis.zadd(rankkey, 55, "tom");
        jedis.zadd(rankkey, 100, "yhl");
        jedis.zadd(rankkey, 95, "wzj");
        print(17, jedis.zcard(rankkey));
        print(18, jedis.zcount(rankkey, 15, 95));
        print(19, jedis.zscore(rankkey, "yhl"));
        jedis.zincrby(rankkey, 2, "wzj");
        jedis.zincrby(rankkey, 80, "www");
        print(20, jedis.zcount(rankkey, 0, 100));
        print(21, jedis.zrange(rankkey, 0, 4));
        print(22, jedis.zrevrange(rankkey, 2, 4));
        for (Tuple tuple : jedis.zrangeByScoreWithScores(rankkey, "0", "100")) {
            print(23, tuple.getElement() + ";" + String.valueOf(tuple.getScore()));
        }

        print(23, jedis.zrank(rankkey, "yhl"));
        print(23, jedis.zrevrank(rankkey, "yhl"));
        JedisPool jedisPool = new JedisPool();
        for (int i = 0; i < 100; i++) {
            Jedis j = jedisPool.getResource();
            j.get("hello");
            System.out.println("pool" + i);
            j.close();
        }
    }

    private Jedis jedis = null;
    private JedisPool pool = null;

    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool();
        System.out.println();
    }

    private Jedis getJedis() {
        return pool.getResource();
    }

    public String get(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return getJedis().get(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.set(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public long sadd(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sadd(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    public long srem(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.srem(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    public boolean sismember(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sismember(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return false;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    public long scard(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.scard(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    public void setex(String key, String value) {
        // 验证码, 防机器注册，记录上次注册时间，有效期3天
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.setex(key, 10, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public long lpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public List<String> brpop(int timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.brpop(timeout, key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void setObject(String key, Object obj) {
        set(key, JSON.toJSONString(obj));
    }

    public <T> T getObject(String key, Class<T> clazz) {
        String value = get(key);
        if (value != null) {
            return JSON.parseObject(value, clazz);
        }
        return null;
    }
}
