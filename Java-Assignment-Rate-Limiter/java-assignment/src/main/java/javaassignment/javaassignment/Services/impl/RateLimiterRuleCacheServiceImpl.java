package javaassignment.javaassignment.Services.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javaassignment.javaassignment.Constants.UtilityConstants;
import javaassignment.javaassignment.Models.RateLimiterRule;
import javaassignment.javaassignment.Repositories.RateLimiterRuleRepository;
import javaassignment.javaassignment.Services.RateLimiterRuleCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RateLimiterRuleCacheServiceImpl implements RateLimiterRuleCacheService {

  Logger logger = LoggerFactory.getLogger(RateLimiterRuleCacheServiceImpl.class);

  @Autowired
  private RateLimiterRuleRepository rateLimiterRuleRepository;

  private Map<String, String> RuleCache = new ConcurrentHashMap<>();


  @Override
  @Scheduled(fixedDelayString = "${rateLimiterRuleCache.interval:120000}", initialDelayString = "${rateLimiterRuleCache.startupDelay:3000}")
  public void scheduleRefreshRuleCache(){

    logger.debug("Refreshing the Rule Cache");
    List<RateLimiterRule> rateLimiterRules = rateLimiterRuleRepository
        .findAllByIsActive(Boolean.TRUE);
    for (RateLimiterRule rule : rateLimiterRules
    ) {

      logger.debug(String.format("Putting the rule: %s",rule.toString()));
      this.RuleCache.put(rule.getUserId() +UtilityConstants.Colon+ rule.getApi().name(),
          rule.getRateLimiterTimeUnit().name() + UtilityConstants.Colon + rule.getRateLimit());
    }
  }

  @Override
  public String getLimit(String key) {
    if (this.RuleCache.containsKey(key)) {
      return this.RuleCache.get(key);
    } else {
      return UtilityConstants.NoRule;
    }
  }
}
