package com.jonah.vttp5_ssf_project.Repos;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.jonah.vttp5_ssf_project.Constants.*;

@Repository
public class MapRepo {
    @Autowired
    @Qualifier(Constants.template02)
    RedisTemplate<String, String> redisTemplate;

    public void create(String redisKey, String hashKey, String hashValue){
        redisTemplate.opsForHash().put(redisKey, hashKey, hashValue);
    }

    public Object get(String redisKey, String hashKey){
        return redisTemplate.opsForHash().get(redisKey, hashKey);
    }

    public Long delete(String redisKey, String hashKey){
        return redisTemplate.opsForHash().delete(redisKey,hashKey); //this returns the number of records that were deleted

    }

    public Boolean keyExists(String redisKey, String hashKey){
        return redisTemplate.opsForHash().hasKey(redisKey, hashKey);
    }

    public Map<Object, Object> getEntries(String redisKey){
        return redisTemplate.opsForHash().entries(redisKey);
    }

    public Set<Object> getKeys(String redisKey){
        return redisTemplate.opsForHash().keys(redisKey);
    }

    public List<Object> getValues(String redisKey){
        return redisTemplate.opsForHash().values(redisKey);
    }

    public Long size(String redisKey){
        return redisTemplate.opsForHash().size(redisKey);
    }

    public void expire(String redisKey, Integer expireValue){
        redisTemplate.expire(redisKey, expireValue, TimeUnit.SECONDS);
    }

    public void oneHourExpire(String redisKey){
        redisTemplate.expire(redisKey, 3600, TimeUnit.SECONDS);
    }
}
