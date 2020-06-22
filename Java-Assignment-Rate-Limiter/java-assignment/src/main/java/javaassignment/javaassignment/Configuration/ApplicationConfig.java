package javaassignment.javaassignment.Configuration;

import javaassignment.javaassignment.Services.RateLimiterWindowCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

  @Autowired
  private ApplicationContext context;

  @Bean
  public RateLimiterWindowCacheService rateLimiterWindowCacheService(@Value("${rateLimiterWindowCacheServiceImpl:InMemory}") String qualifier) {
    return (RateLimiterWindowCacheService) context.getBean(qualifier);
  }

}
