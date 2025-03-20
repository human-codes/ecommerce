package uz.pdp.salemartpro.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;


@Service
public class RedisService {
    private final StringRedisTemplate redisTemplate;

    public RedisService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveLocation(Long userId, String location) {
        redisTemplate.opsForValue().set("location:" + userId, location, 30, TimeUnit.MINUTES);
    }

    public String getLocation(Long userId) {
        return redisTemplate.opsForValue().get("location:" + userId);
    }

    public void deleteLocation(String userId) {
        redisTemplate.delete("location:" + userId);
    }


    public void saveVerificationCode(String email, String code, long expirationMinutes) {
        System.out.println("✅ Storing verification code: " + code + " for email: " + email);
        redisTemplate.opsForValue().set("verify:" + email, code, expirationMinutes, TimeUnit.MINUTES);
    }


    public String getVerificationCode(String email) {
        String key = "verify:" + email;
        String code = redisTemplate.opsForValue().get(key);
        System.out.println("🔍 Retrieving verification code from Redis with key: " + key + " → " + code);
        return code;
    }


    public void deleteVerificationCode(String email) {
        redisTemplate.delete("verify:" + email);
    }
}
