package javaassignment.javaassignment.Services;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import javaassignment.javaassignment.Constants.UtilityConstants;
import javaassignment.javaassignment.Enums.RateLimiterTimeUnit;
import javaassignment.javaassignment.Services.impl.InMemoryRateLimiterWindowCacheServiceImpl;
import javax.rmi.CORBA.Util;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class InMemoryRateLimiterWindowCacheServiceImplTest {

  InMemoryRateLimiterWindowCacheServiceImpl inMemoryRateLimiterWindowCacheService = new InMemoryRateLimiterWindowCacheServiceImpl();

  @Test
  public void fixedWindowRateLimiterTest_newWindow()
  {
    Assert.assertTrue(inMemoryRateLimiterWindowCacheService.fixedWindowRateLimiter("key1",10));
  }

  @Test
  public void fixedWindowRateLimiterTest_WithinLimit()
  {
    for(int i =0;i<3;i++) {
      Assert.assertTrue(inMemoryRateLimiterWindowCacheService.fixedWindowRateLimiter("key1", 10));
    }
  }

  @Test
  public void fixedWindowRateLimiterTest_LimitExhausted()
  {
    for(int i =0;i<10;i++) {
     inMemoryRateLimiterWindowCacheService.fixedWindowRateLimiter("key1", 10);
    }
    Assert.assertFalse(inMemoryRateLimiterWindowCacheService.fixedWindowRateLimiter("key1",10));
  }

  @Test
  public void slidingWindowRateLimiterTest_newWindow()
  {
    Assert.assertTrue(inMemoryRateLimiterWindowCacheService.slidingWindowRateLimiter("key1",
        RateLimiterTimeUnit.SECOND,10));
  }

  @Test
  public void slidingWindowRateLimiterTest_WithinLimit()
  {
    for(int i =0;i<3;i++) {
      Assert.assertTrue(inMemoryRateLimiterWindowCacheService.slidingWindowRateLimiter("key1",
          RateLimiterTimeUnit.SECOND,10));    }
  }

  @Test
  public void slidingWindowRaterTest_LimitExhausted()
  {
    for(int i =0;i<10;i++) {
      inMemoryRateLimiterWindowCacheService.slidingWindowRateLimiter("key1",
          RateLimiterTimeUnit.SECOND,10);
    }
    Assert.assertFalse(inMemoryRateLimiterWindowCacheService.slidingWindowRateLimiter("key1",
        RateLimiterTimeUnit.SECOND,10));
  }

  @Test
  public void slidingWindowRaterTest_LimitTwoValueInQueue() throws InterruptedException {
    for(int i =0;i<10;i++) {
      inMemoryRateLimiterWindowCacheService.slidingWindowRateLimiter("key1",
          RateLimiterTimeUnit.SECOND,10);
    }
    Thread.sleep(1000);
    Assert.assertTrue(inMemoryRateLimiterWindowCacheService.slidingWindowRateLimiter("key1",
        RateLimiterTimeUnit.SECOND,10));
  }

  @Test
  public void isAllowedTest_FixedWindow() {
    Assert.assertTrue(inMemoryRateLimiterWindowCacheService.isAllowed("key1",RateLimiterTimeUnit.SECOND,10,
        UtilityConstants.RATE_LIMITER_ALGO_FIXED_WINDOW));
  }

  @Test
  public void isAllowedTest_SlidingWindow() {
    Assert.assertTrue(inMemoryRateLimiterWindowCacheService.isAllowed("key1",RateLimiterTimeUnit.SECOND,10,
        UtilityConstants.RATE_LIMITER_ALGO_SLIDING_WINDOW));
  }

  @Test
  public void isAllowedTest_DefaultFixedWindow() {
    Assert.assertTrue(inMemoryRateLimiterWindowCacheService.isAllowed("key1",RateLimiterTimeUnit.SECOND,10,
        UtilityConstants.Colon));
  }
}
