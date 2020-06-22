package javaassignment.javaassignment.Utils;

import static org.mockito.Mockito.mock;

import java.util.Arrays;
import javaassignment.javaassignment.Constants.UtilityConstants;
import javaassignment.javaassignment.Enums.ApisIdentifier;
import javaassignment.javaassignment.Exceptions.ApplicationException;
import javaassignment.javaassignment.Services.RateLimiterRuleCacheService;
import javaassignment.javaassignment.Services.RateLimiterWindowCacheService;
import javaassignment.javaassignment.Services.impl.InMemoryRateLimiterWindowCacheServiceImpl;
import javaassignment.javaassignment.Services.impl.RateLimiterRuleCacheServiceImpl;
import javaassignment.javaassignment.Services.impl.RedisServerRateLimiterWindowCacheServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataAccessException;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class RateLimiterUtilsTest {

  RateLimiterWindowCacheService rateLimiterWindowCacheService;

  RateLimiterRuleCacheService rateLimiterRuleCacheService;

  RateLimiterUtils rateLimiterUtils;

  @Before
  public void setUp() {
    rateLimiterWindowCacheService = mock(RedisServerRateLimiterWindowCacheServiceImpl.class);
    rateLimiterRuleCacheService = mock(RateLimiterRuleCacheServiceImpl.class);
    rateLimiterUtils = new RateLimiterUtils(rateLimiterWindowCacheService);
    ReflectionTestUtils.setField(rateLimiterUtils, "byPassUsers", Arrays.asList(
        new String[]{"112", "113"}));
    ReflectionTestUtils.setField(rateLimiterUtils, "defaultRateLimit", 10);
    ReflectionTestUtils.setField(rateLimiterUtils, "rateLimiterAlgo", "rateLimiterAlgoSlidingWindow");
    ReflectionTestUtils.setField(rateLimiterUtils,"rateLimiterRuleCacheService",rateLimiterRuleCacheService);
  }

  @Test
  public void isAllowedTest_ByPassUsersWithoutRateLimit() throws ApplicationException {
    Assert.assertTrue(rateLimiterUtils.isAllowed("112", ApisIdentifier.API_GET_DEVELOPER_ALL));
  }

  @Test
  public void isAllowedTest_NoRuleExisted() throws ApplicationException {
    Mockito.when(rateLimiterRuleCacheService.getLimit(Mockito.anyString())).thenReturn(
        UtilityConstants.NoRule);
    Mockito.when(rateLimiterWindowCacheService.isAllowed(Mockito.anyString(), Mockito.any(),Mockito.anyInt(),Mockito.anyString())).thenReturn(Boolean.TRUE);
    Assert.assertTrue(rateLimiterUtils.isAllowed("user113",ApisIdentifier.API_GET_DEVELOPER_ALL));
  }

  @Test
  public void isAllowedTest_RuleExisted() throws ApplicationException {
    Mockito.when(rateLimiterRuleCacheService.getLimit(Mockito.anyString())).thenReturn(
        "HOUR:10");
    Mockito.when(rateLimiterWindowCacheService.isAllowed(Mockito.anyString(), Mockito.any(),Mockito.anyInt(),Mockito.anyString())).thenReturn(Boolean.TRUE);
    Assert.assertTrue(rateLimiterUtils.isAllowed("user113",ApisIdentifier.API_GET_DEVELOPER_ALL));
  }

  @Test
  public void isAllowedTest_DataAccessException(){
    Mockito.when(rateLimiterRuleCacheService.getLimit(Mockito.anyString())).thenReturn(
        "HOUR:10");
    Mockito.when(rateLimiterWindowCacheService.isAllowed(Mockito.anyString(), Mockito.any(),Mockito.anyInt(),Mockito.anyString())).thenThrow(
        new DataAccessException("Redis Failure") {
          @Override
          public String getMessage() {
            return super.getMessage();
          }
        });
    try {
      Assert
          .assertTrue(rateLimiterUtils.isAllowed("user113", ApisIdentifier.API_GET_DEVELOPER_ALL));
    }
    catch (Exception d)
    {
      Assert.assertTrue(d instanceof ApplicationException);
    }
  }

  @Test
  public void isAllowedTest_Exception(){
    Mockito.when(rateLimiterRuleCacheService.getLimit(Mockito.anyString())).thenReturn(
        "HOUR:10");
    Mockito.when(rateLimiterWindowCacheService.isAllowed(Mockito.anyString(), Mockito.any(),Mockito.anyInt(),Mockito.anyString())).thenThrow(new NumberFormatException());
    try {
      Assert
          .assertTrue(rateLimiterUtils.isAllowed("user113", ApisIdentifier.API_GET_DEVELOPER_ALL));
    }
    catch (Exception d)
    {
      Assert.assertTrue(d instanceof ApplicationException);
    }
  }
}
