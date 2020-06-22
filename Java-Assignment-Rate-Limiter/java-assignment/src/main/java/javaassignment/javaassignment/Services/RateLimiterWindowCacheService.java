package javaassignment.javaassignment.Services;

import javaassignment.javaassignment.Constants.UtilityConstants;
import javaassignment.javaassignment.Enums.RateLimiterTimeUnit;

public interface RateLimiterWindowCacheService {

  default boolean isAllowed(String key, RateLimiterTimeUnit rateLimiterTimeUnit, Integer rateLimit, String rateLimiterAlgo ) {
    if(rateLimiterAlgo.equalsIgnoreCase(UtilityConstants.RATE_LIMITER_ALGO_FIXED_WINDOW)){
      key = createKeyForFixedWindow(key,rateLimiterTimeUnit);
      return fixedWindowRateLimiter(key,rateLimit);
    }
    else if(rateLimiterAlgo.equalsIgnoreCase(UtilityConstants.RATE_LIMITER_ALGO_SLIDING_WINDOW))
    {

      return slidingWindowRateLimiter(key,rateLimiterTimeUnit,rateLimit);
    }
    else return fixedWindowRateLimiter(key,rateLimit);
  }

  default  String createKeyForFixedWindow(String key, RateLimiterTimeUnit rateLimiterTimeUnit){
    return key+UtilityConstants.Colon+System.currentTimeMillis() / rateLimiterTimeUnit.getValue();
  }

  boolean fixedWindowRateLimiter(String key,Integer rateLimit);

  boolean slidingWindowRateLimiter(String key,RateLimiterTimeUnit rateLimiterTimeUnit,Integer rateLimit);


}
