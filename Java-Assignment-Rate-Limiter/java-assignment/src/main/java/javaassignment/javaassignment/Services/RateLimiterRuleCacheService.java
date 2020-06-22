package javaassignment.javaassignment.Services;

public interface RateLimiterRuleCacheService {

  void scheduleRefreshRuleCache();

  String getLimit(String key);

}
