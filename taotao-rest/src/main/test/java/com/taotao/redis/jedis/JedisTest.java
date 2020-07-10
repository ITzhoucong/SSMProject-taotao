package com.taotao.redis.jedis;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;

/**
 * @author: ZhouCong
 * @date: Create in 2019/8/21 17:45
 * @description:
 */
public class JedisTest {

    @Test
    public void testJedisSingle() {
//        创建Jedis对象
        Jedis jedis = new Jedis("192.168.174.128", 6379);
//        调用Jedis对象的方法，方法名称和redis的名称一致
        jedis.set("key1", "jedis test");
        String key1 = jedis.get("key1");
        System.out.println(key1);
//        关闭jeids
        jedis.close();
    }

    /**
     * 功能描述:使用连接池
     *
     * @Param:
     * @Return:
     * @Author: ZhouCong
     * @Date: 2019/8/21 17:52
     */
    @Test
    public void testJedisPool() {
//        创建Jedis连接池
        JedisPool pool = new JedisPool("192.168.174.128", 6379);
//        从连接池中获取Jedis对象
        Jedis jedis = pool.getResource();
        String key1 = jedis.get("key1");
        System.out.println(key1);
//      关闭连接
        jedis.close();
        pool.close();
    }

    @Test
    public void testJedisCluster() {
        HashSet<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("192.168.174.128", 7001));
        nodes.add(new HostAndPort("192.168.174.128", 7002));
        nodes.add(new HostAndPort("192.168.174.128", 7003));
        nodes.add(new HostAndPort("192.168.174.128", 7004));
        nodes.add(new HostAndPort("192.168.174.128", 7005));
        nodes.add(new HostAndPort("192.168.174.128", 7006));
        JedisCluster cluster = new JedisCluster(nodes);

        cluster.set("key1", "1000");
        String key1 = cluster.get("key1");
        System.out.println(key1);
        cluster.close();
    }

    @Test
    public void testSpringJedisSingle() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-jedis.xml");
        JedisPool jedisPool = (JedisPool) applicationContext.getBean("redisClient");
        Jedis jedis = jedisPool.getResource();
        String key1 = jedis.get("key1");
        System.out.println(key1);
        jedis.close();
        jedisPool.close();
    }

    @Test
    public void testSpringJedisCluster() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-jedis.xml");
        JedisCluster jedisCluster = (JedisCluster) applicationContext.getBean("redisClient");
        String key1 = jedisCluster.get("key1");
        System.out.println(key1);
        jedisCluster.close();
    }
}
