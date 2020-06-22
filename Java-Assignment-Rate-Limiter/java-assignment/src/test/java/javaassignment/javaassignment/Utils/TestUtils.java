package javaassignment.javaassignment.Utils;

import java.util.ArrayList;
import java.util.List;
import javaassignment.javaassignment.Enums.ApisIdentifier;
import javaassignment.javaassignment.Enums.RateLimiterTimeUnit;
import javaassignment.javaassignment.Models.RateLimiterRule;

public class TestUtils {


  public static List<RateLimiterRule> createRateLimiterRule()
  {
    RateLimiterRule rateLimiterRule1 = new RateLimiterRule();
    rateLimiterRule1.setApi(ApisIdentifier.API_GET_DEVELOPER_ALL);
    rateLimiterRule1.setActive(Boolean.TRUE);
    rateLimiterRule1.setRateLimit(10);
    rateLimiterRule1.setRateLimiterTimeUnit(RateLimiterTimeUnit.SECOND);
    rateLimiterRule1.setUserId("user113");

    RateLimiterRule rateLimiterRule2 = new RateLimiterRule();
    rateLimiterRule2.setRateLimiterTimeUnit(RateLimiterTimeUnit.MINUTE);
    rateLimiterRule2.setActive(Boolean.FALSE);
    rateLimiterRule2.setApi(ApisIdentifier.API_GET_ORGANIZATION_ALL);
    rateLimiterRule2.setRateLimit(100);
    rateLimiterRule2.setUserId("user116");


    RateLimiterRule rateLimiterRule3 = new RateLimiterRule();
    rateLimiterRule3.setApi(ApisIdentifier.API_GET_DEVELOPER_ALL);
    rateLimiterRule3.setRateLimit(10);
    rateLimiterRule3.setActive(Boolean.TRUE);
    rateLimiterRule3.setRateLimiterTimeUnit(RateLimiterTimeUnit.SECOND);
    rateLimiterRule3.setUserId("user112");

    List<RateLimiterRule> rateLimiterRules = new ArrayList<>();

    rateLimiterRules.add(rateLimiterRule1);
    rateLimiterRules.add(rateLimiterRule2);
    rateLimiterRules.add(rateLimiterRule3);

    return rateLimiterRules;



  }


}
