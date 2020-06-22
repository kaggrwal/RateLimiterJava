package javaassignment.javaassignment.Services;

import java.util.Map;
import java.util.stream.Collectors;
import javaassignment.javaassignment.Constants.UtilityConstants;
import javaassignment.javaassignment.Enums.ApisIdentifier;
import javaassignment.javaassignment.Enums.RateLimiterTimeUnit;
import javaassignment.javaassignment.Repositories.RateLimiterRuleRepository;
import javaassignment.javaassignment.Services.impl.RateLimiterRuleCacheServiceImpl;
import javaassignment.javaassignment.Utils.TestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class RateLimiterRuleCacheServiceImplTest {

  @Mock
  RateLimiterRuleRepository rateLimiterRuleRepository;

  @InjectMocks
  RateLimiterRuleCacheServiceImpl rateLimiterRuleCacheService;

  @Test
  public void scheduleRefreshCacheTest()
  {
    Mockito.when(rateLimiterRuleRepository.findAllByIsActive(Boolean.TRUE)).thenReturn(TestUtils.createRateLimiterRule().stream().filter(rule->rule.isActive()).collect(
        Collectors.toList()));
    rateLimiterRuleCacheService.scheduleRefreshRuleCache();
    Assert.assertEquals(((Map)ReflectionTestUtils.getField(rateLimiterRuleCacheService,"RuleCache")).size(),2);
  }

  @Test
  public void getLimitTest_NoRule()
  {
    Assert.assertEquals(rateLimiterRuleCacheService.getLimit("userTest"), UtilityConstants.NoRule);
  }

  @Test
  public void getLimitTest_RulePresent()
  {
    Mockito.when(rateLimiterRuleRepository.findAllByIsActive(Boolean.TRUE)).thenReturn(TestUtils.createRateLimiterRule().stream().filter(rule->rule.isActive()).collect(
        Collectors.toList()));
    rateLimiterRuleCacheService.scheduleRefreshRuleCache();
    Assert.assertEquals(rateLimiterRuleCacheService.getLimit("user113"+UtilityConstants.Colon+ ApisIdentifier.API_GET_DEVELOPER_ALL),
        RateLimiterTimeUnit.SECOND.name()+UtilityConstants.Colon+10);
  }



}
