package tech.tuanzi.miaosha;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class MiaoshaApplicationTests {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisScript<Boolean> script;

    @Test
    public void testLock01() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        // 只有设置的 key 之前不存在，才能设置成功
        Boolean isLock = valueOperations.setIfAbsent("k1", "v1");
        // 上锁成功
        if (isLock) {
            valueOperations.set("name", "Patrick");
            String name = (String) valueOperations.get("name");
            System.out.println("name = " + name);
            // 操作结束，删除锁
            // 如果在这一步之前抛出异常，将出现死锁
            // Integer.parseInt("xxx");
            redisTemplate.delete("k1");
        } else {
            System.out.println("有线程在使用，请稍后再试");
        }
    }

    /**
     * 死锁的解决方法：在获取锁的时候，设置一个超时时间
     * 弹幕说：这样会导致当前事务还未完成，就被迫打开锁的问题
     * 老师说：
     * 这样就会导致 a 线程上的锁，5 秒后自己失效了，但是任务还没有执行完毕
     * 此时来了 b 线程，它也上锁成功了，等 a 线程执行完毕后，它会把 b 的锁释放掉
     * 所以可以让 v1 变成一个随机值，在释放锁的时候检查随机值，这样可以保证只删除自己上的锁
     * 释放锁有三个步骤：获取锁，比较锁，删除锁。明显不具备原子性。
     * 此时就可以使用 Lua 脚本。
     */
    @Test
    public void testLock02() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        // 给锁添加一个过期时间，防止应用在运行过程中抛出异常导致锁无法正常释放
        Boolean isLock = valueOperations.setIfAbsent("k1", "v1", 5, TimeUnit.SECONDS);
        if (isLock) {
            valueOperations.set("name", "Patrick");
            String name = (String) valueOperations.get("name");
            System.out.println("name = " + name);
            Integer.parseInt("xxx");
            redisTemplate.delete("k1");
        } else {
            System.out.println("有线程在使用，请稍后再试");
        }
    }

    @Test
    public void testLock03() {
        ValueOperations valueOperations = redisTemplate.opsForValue();

        String value = UUID.randomUUID().toString();
        Boolean isLock = valueOperations.setIfAbsent("k1", value, 120, TimeUnit.SECONDS);
        if (isLock) {
            valueOperations.set("name", "Patrick");
            String name = (String) valueOperations.get("name");
            System.out.println("name = " + name);
            System.out.println(valueOperations.get("k1"));
            Boolean result = (Boolean) redisTemplate.execute(script, Collections.singletonList("k1"), value);
            System.out.println(result);
        } else {
            System.out.println("有线程在使用，请稍后再试");
        }
    }
}
