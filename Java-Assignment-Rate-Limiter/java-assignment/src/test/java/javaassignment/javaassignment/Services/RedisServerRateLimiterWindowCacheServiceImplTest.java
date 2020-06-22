package javaassignment.javaassignment.Services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javaassignment.javaassignment.Enums.RateLimiterTimeUnit;
import javaassignment.javaassignment.Services.impl.RedisServerRateLimiterWindowCacheServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.autoconfigure.cache.CacheProperties.Redis;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

@RunWith(MockitoJUnitRunner.class)
public class RedisServerRateLimiterWindowCacheServiceImplTest {

  @Mock
  RedisTemplate redisTemplate;

  @InjectMocks
  RedisServerRateLimiterWindowCacheServiceImpl redisServerRateLimiterWindowCacheService;

  @Test
  public void fixedWindowRateLimiterTest_WithinLimit()
  {

    Mockito.when(redisTemplate.execute((SessionCallback)Mockito.any())).thenReturn(new ArrayList<Object>());
    Assert.assertTrue(redisServerRateLimiterWindowCacheService.fixedWindowRateLimiter("key1",10));
  }

  @Test
  public void fixedWindowRateLimiterTest_LimitExhaustedRedisMultiOperationsFailed()
  {

    Mockito.when(redisTemplate.execute((SessionCallback)Mockito.any())).thenReturn(null);
    Assert.assertFalse(redisServerRateLimiterWindowCacheService.fixedWindowRateLimiter("key1",10));
  }

  @Test
  public void slidingWindowRateLimiterTest_LimitExhaustedRedisMultiOperationsFailed()
  {

    Mockito.when(redisTemplate.execute((SessionCallback)Mockito.any())).thenReturn(null);
    Assert.assertFalse(redisServerRateLimiterWindowCacheService.slidingWindowRateLimiter("key1",
        RateLimiterTimeUnit.SECOND,10));
  }
  @Test
  public void slidingWindowRateLimiterTest_LimitExhausted()
  {
    Set<String> keys = new HashSet<>();
    keys.add("key1");
    keys.add("key2");
    Mockito.when(redisTemplate.execute((SessionCallback)Mockito.any())).thenReturn(new ArrayList<Object>());
    Mockito.when(redisTemplate.keys(Mockito.anyString())).thenReturn(keys);
    Assert.assertFalse(redisServerRateLimiterWindowCacheService.slidingWindowRateLimiter("key1",
        RateLimiterTimeUnit.SECOND,1));
  }

  @Test
  public void slidingWindowRateLimiterTest_WithinLimit()
  {
    Set<String> keys = new HashSet<>();
    keys.add("key1");
    keys.add("key2");
    Mockito.when(redisTemplate.execute((SessionCallback)Mockito.any())).thenReturn(new ArrayList<Object>());
    Mockito.when(redisTemplate.keys(Mockito.anyString())).thenReturn(keys);
    Assert.assertTrue(redisServerRateLimiterWindowCacheService.slidingWindowRateLimiter("key1",
        RateLimiterTimeUnit.SECOND,3));
  }
}
