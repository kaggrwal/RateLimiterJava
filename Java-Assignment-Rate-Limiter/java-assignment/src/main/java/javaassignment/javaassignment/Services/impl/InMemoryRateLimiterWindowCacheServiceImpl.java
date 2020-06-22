package javaassignment.javaassignment.Services.impl;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import javaassignment.javaassignment.Enums.RateLimiterTimeUnit;
import javaassignment.javaassignment.Services.RateLimiterWindowCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service("InMemory")
public class InMemoryRateLimiterWindowCacheServiceImpl implements
    RateLimiterWindowCacheService {

  Logger logger = LoggerFactory.getLogger(InMemoryRateLimiterWindowCacheServiceImpl.class);

  private Map<String, Object> WindowRateLimits = new ConcurrentHashMap<>();


  @Override
  public boolean fixedWindowRateLimiter(String key, Integer rateLimit) {
    if (this.WindowRateLimits.containsKey(key)) {
      if ((int) this.WindowRateLimits.get(key) < rateLimit) {
        this.WindowRateLimits.put(key, (int) this.WindowRateLimits.get(key) + 1);
        return true;
      } else {
        logger.info(String.format("Rate limit exhausted for key: %s", key));
        return false;
      }
    } else {
      logger.debug(String.format("Creating a new Window of Rate limit for key: %s", key));
      this.WindowRateLimits.put(key, 1);
      return true;
    }
  }

  @Override
  public boolean slidingWindowRateLimiter(String key, RateLimiterTimeUnit rateLimiterTimeUnit,
      Integer rateLimit) {
    long currentTime = System.currentTimeMillis();
    long boundary = currentTime - rateLimiterTimeUnit.getValue();
    Queue<Long> slidingWindow;
    if (this.WindowRateLimits.containsKey(key)) {
      slidingWindow = (Queue<Long>) this.WindowRateLimits.get(key);
      if (!checkForSlidingWindow(currentTime, rateLimit, boundary, slidingWindow)) {
        logger.info(String.format("Rate limit exhausted for key: %s", key));
        return false;
      } else {
        return true;
      }
    } else {
      logger.debug(String.format("Creating a new Window of Rate limit for key: %s", key));
      slidingWindow = new ConcurrentLinkedQueue<>();
      slidingWindow.add(currentTime);
      this.WindowRateLimits.put(key, slidingWindow);
      return true;
    }

  }

  private boolean checkForSlidingWindow(long currentTime, Integer rateLimit, long boundary,
      Queue<Long> slidingWindow) {
    while (!slidingWindow.isEmpty() && slidingWindow.element() <= boundary) {
      slidingWindow.poll();
    }
    slidingWindow.add(currentTime);
    return slidingWindow.size() <= rateLimit;
  }
}




