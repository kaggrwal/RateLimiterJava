package javaassignment.javaassignment.Services.impl;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import javaassignment.javaassignment.Constants.UtilityConstants;
import javaassignment.javaassignment.Enums.RateLimiterTimeUnit;
import javaassignment.javaassignment.Services.RateLimiterWindowCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

@Service("RedisServer")
public class RedisServerRateLimiterWindowCacheServiceImpl implements
    RateLimiterWindowCacheService {

  Logger logger = LoggerFactory.getLogger(RedisServerRateLimiterWindowCacheServiceImpl.class);

  @Autowired
  private RedisTemplate<String, String> redisTemplate;


  @Override
  public boolean fixedWindowRateLimiter(String key, Integer rateLimit) {
    List<Object> commandResponseList = (List<Object>) redisTemplate
        .execute(new SessionCallback<Object>() {
          @Override
          public <K, V> Object execute(RedisOperations<K, V> redisOperations)
              throws DataAccessException {
            redisOperations.watch((K) key);
            Integer count = Integer.parseInt(
                Optional.ofNullable((String) redisOperations.opsForValue().get(key)).orElse("0"));
            if (count != null && count >= rateLimit) {
              logger.info(String.format("Rate limit exhausted for key: %s", key));
              return null;
            } else {
              redisOperations.multi();
              redisOperations.opsForValue().increment((K) key, 1);
              redisOperations.expire((K)key, Duration.ofMillis(RateLimiterTimeUnit.HOUR.getValue()));
              return redisOperations.exec();
            }
          }
        });

    if (commandResponseList != null) {
      return true;
    }
    return false;
  }

  @Override
  public boolean slidingWindowRateLimiter(String key, RateLimiterTimeUnit rateLimiterTimeUnit,
      Integer rateLimit) {
    String redisKey =
        key + UtilityConstants.Colon + System.currentTimeMillis() / rateLimiterTimeUnit.getValue();
      List<Object> commandResponseList =  (List<Object>)redisTemplate.execute(new SessionCallback<Object>() {
      @Override
      public <K, V> Object execute(RedisOperations<K, V> redisOperations)
          throws DataAccessException {
        if (redisOperations.hasKey((K) redisKey)) {
          redisOperations.watch((K) redisKey);
          Integer count = Integer.parseInt(
              Optional.ofNullable((String) redisOperations.opsForValue().get(redisKey)).orElse("0"));
          if (count != null && count >= rateLimit) {
            logger.info(String.format("Rate limit exhausted for key: %s", key));
            return null;}
          redisOperations.multi();
          redisOperations.opsForValue().increment((K) redisKey, 1);
          return redisOperations.exec();
        }
        else {
          redisOperations.multi();
          //redisOperations.opsForValue().setIfAbsent((K)redisKey,(V)"1");
          redisOperations.opsForValue()
              .setIfAbsent((K) redisKey, (V) "1", rateLimiterTimeUnit.getValue(),
                  TimeUnit.MILLISECONDS);
          return redisOperations.exec();
        }
      }});

    if(commandResponseList == null) return false;
    if (redisTemplate.keys(key + "*").size() > rateLimit) {
      return false;
    } else {
      return true;
    }
  }
}