package com.xue.secondkill;

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
class SecondKillApplicationTests {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisScript<Boolean> script;

    @Test
    public void testLock() {
        ValueOperations valueOperations=redisTemplate.opsForValue();
        String value = UUID.randomUUID().toString();
        Boolean isLock = valueOperations.setIfAbsent("k1", value, 5, TimeUnit.SECONDS);
        if(isLock){
            valueOperations.set("name","wxc");
            String name=(String) valueOperations.get("name");
            System.out.println("name:"+name);
            System.out.println(valueOperations.get("k1"));
            Boolean result = (Boolean)redisTemplate.execute(script, Collections.singletonList("k1"), value);
            System.out.println(result);
        }else{
            System.out.println("有线程在使用,请稍后");
        }
    }

}
