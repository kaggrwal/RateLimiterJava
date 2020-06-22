package javaassignment.javaassignment.Utils;

import java.util.List;
import java.util.Set;
import javaassignment.javaassignment.Constants.UtilityConstants;
import javaassignment.javaassignment.Enums.ApisIdentifier;
import javaassignment.javaassignment.Enums.RateLimiterTimeUnit;
import javaassignment.javaassignment.Exceptions.ApplicationException;
import javaassignment.javaassignment.Services.RateLimiterRuleCacheService;
import javaassignment.javaassignment.Services.RateLimiterWindowCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

@Component
public class RateLimiterUtils {

  Logger logger = LoggerFactory.getLogger(RateLimiterUtils.class);

  RateLimiterWindowCacheService rateLimiterWindowCacheService;

  @Autowired
  public RateLimiterUtils(
      @Qualifier("rateLimiterWindowCacheService") RateLimiterWindowCacheService rateLimiterWindowCacheService) {
    this.rateLimiterWindowCacheService = rateLimiterWindowCacheService;
  }

  @Autowired
  RateLimiterRuleCacheService rateLimiterRuleCacheService;

  @Value("${rateLimiterDefaultRateLimitPerSecond}")
  private Integer defaultRateLimit;

  @Value("#{'${rateLimitByPassTestPerformanceUsers}'.split(',')}")
  private List<String> byPassUsers;

  @Value("${rateLimiterAlgo:rateLimiterAlgoSlidingWindow}")
  private String rateLimiterAlgo;

  private static final RateLimiterTimeUnit defaultRateLimiterTimeUnit = RateLimiterTimeUnit.SECOND;


  public boolean isAllowed(String userId, ApisIdentifier Api) throws ApplicationException {

    if (byPassUsers.contains(userId)) {
      logger.info(String.format(
          "current user has been bypassed for rate limits, it is present in bypass list; userId: %s ",
          userId));
      return true;
    }
    try {
      String key = userId + UtilityConstants.Colon + Api;
      String ruleValue = rateLimiterRuleCacheService.getLimit(key);
      if (ruleValue.equalsIgnoreCase(UtilityConstants.NoRule)) {
        logger.debug(String.format("No rate limiter rule provided for key: %s", key));

        return rateLimiterWindowCacheService.isAllowed(
            key,defaultRateLimiterTimeUnit,
            defaultRateLimit,rateLimiterAlgo);
      } else {
        String[] rateLimits = ruleValue.split(UtilityConstants.Colon);
        logger.debug(String
            .format("Rule applied for userId: %s, Rate Limit: %s, Rate Limit Time Unit: %s", userId,
                rateLimits[1], rateLimits[0]));
        return rateLimiterWindowCacheService.isAllowed(
            key, RateLimiterTimeUnit
                .valueOf(rateLimits[0]), Integer.parseInt(rateLimits[1]),rateLimiterAlgo);
      }
    } catch (DataAccessException e) {
      throw new ApplicationException("Problem in fetching data from Redis", e.getCause());
    } catch (Exception e) {
      throw new ApplicationException("Exception occurred", e.getCause());
    }
  }


}
