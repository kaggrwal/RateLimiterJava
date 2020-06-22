package javaassignment.javaassignment;

import javaassignment.javaassignment.Enums.ApisIdentifier;
import javaassignment.javaassignment.Enums.RateLimiterTimeUnit;
import javaassignment.javaassignment.Models.RateLimiterRule;
import javaassignment.javaassignment.Repositories.RateLimiterRuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RateLimiterRepositoryCommandRunner implements CommandLineRunner {

  @Autowired
  RateLimiterRuleRepository rateLimiterRuleRepository;

  @Override
  public void run(String... args) throws Exception {
    RateLimiterRule rateLimiterRule = new RateLimiterRule();
    rateLimiterRule.setUserId("user112");
    rateLimiterRule.setApi(ApisIdentifier.API_GET_DEVELOPER_ALL);
    rateLimiterRule.setRateLimit(10);
    rateLimiterRule.setRateLimiterTimeUnit(RateLimiterTimeUnit.MINUTE);
    rateLimiterRule.setActive(Boolean.TRUE);

    RateLimiterRule rateLimiterRule2 = new RateLimiterRule();
    rateLimiterRule2.setUserId("user114");
    rateLimiterRule2.setApi(ApisIdentifier.API_GET_ORGANIZATION_ALL);
    rateLimiterRule2.setRateLimit(10);
    rateLimiterRule2.setRateLimiterTimeUnit(RateLimiterTimeUnit.SECOND);
    rateLimiterRule2.setActive(Boolean.FALSE);

    RateLimiterRule rateLimiterRule3 = new RateLimiterRule();
    rateLimiterRule3.setUserId("user116");
    rateLimiterRule3.setApi(ApisIdentifier.API_GET_DEVELOPER_ONE);
    rateLimiterRule3.setRateLimit(10);
    rateLimiterRule3.setRateLimiterTimeUnit(RateLimiterTimeUnit.SECOND);
    rateLimiterRule3.setActive(Boolean.TRUE);


    rateLimiterRuleRepository.save(rateLimiterRule);
    rateLimiterRuleRepository.save(rateLimiterRule2);
    rateLimiterRuleRepository.save(rateLimiterRule3);


  }
}
