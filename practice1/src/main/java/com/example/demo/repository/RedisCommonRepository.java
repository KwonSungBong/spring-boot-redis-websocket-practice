package com.example.demo.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class RedisCommonRepository {

    @Autowired
    private StringRedisTemplate redisTemplate;

    // string
    public void addValue(String key, String value){
        redisTemplate.opsForValue().set(key, value);
    }
    public String getValue(String key){
        return redisTemplate.opsForValue().get(key);
    }

    // list
    public Long addToListRight(String key, String value){
        return redisTemplate.opsForList().rightPush(key, value);
    }
    public Long addToListLeft(String key, String value){
        return redisTemplate.opsForList().leftPush(key, value);
    }
    public String getToListRight(String key){
        return redisTemplate.opsForList().rightPop(key);
    }
    public String getToListLeft(String key){
        return redisTemplate.opsForList().leftPop(key);
    }
    public List<String> getList(String key){
        return redisTemplate.opsForList().range(key, 0, -1);
    }
    public long getListSize(String key){
        return redisTemplate.opsForList().size(key);
    }
    public void trim(String key, long start, long end){
        redisTemplate.opsForList().trim(key, start, end);
    }

    // set
    public Long addSet(String key, String value){
        return redisTemplate.opsForSet().add(key, value);
    }
    public Long addSets(String key, String[] values){
        return redisTemplate.opsForSet().add(key, values);
    }
    public Set<String> getSet(String key){
        return redisTemplate.opsForSet().members(key);
    }
    public Set<String> getKeys (String pattern){
        return redisTemplate.keys(pattern);
    }

    // hash
    public void addHash(String key, String field, String value) {
        redisTemplate.opsForHash().put(key, field, value);;
    }
    public Object getHash(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }
    public List<Object> getHashValues(String key) {
        return redisTemplate.opsForHash().values(key);
    }
    public Map<Object,Object> getHashAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    // delete
    public void delKeys(Set<String> keys){
        redisTemplate.delete(keys);
    }
    public void delKey(String key){
        redisTemplate.delete(key);
    }
    public void flushAll() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    public void convertAndSend(String key, String value) {
        redisTemplate.convertAndSend(key, value);
    }

}
