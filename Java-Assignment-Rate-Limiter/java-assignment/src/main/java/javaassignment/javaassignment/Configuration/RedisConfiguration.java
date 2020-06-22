package javaassignment.javaassignment.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

@Configuration
public class RedisConfiguration {

    @Value("${redis.host:localhost}")
    private String redisHost;
    @Value("${redis.port:6379}")
    private String redisPort;
    @Value("${redis.connection.pool:10}")
    private String redisConnectionPool;

    @Bean
    public JedisPoolConfig poolConfig() {
      final JedisPoolConfig jedisPoolConfig =  new JedisPoolConfig();
      jedisPoolConfig.setTestOnBorrow( true );
      jedisPoolConfig.setMaxTotal( Integer.parseInt(redisConnectionPool) );
      return jedisPoolConfig;
    }

    @Bean
    public JedisConnectionFactory connectionFactory() {
      final JedisConnectionFactory connectionFactory =
          new JedisConnectionFactory( poolConfig() );
      connectionFactory.setHostName(redisHost);
      connectionFactory.setDatabase( Protocol.DEFAULT_DATABASE );
      connectionFactory.setPort( Integer.parseInt(redisPort) );
      return connectionFactory;
    }

  @Bean
  @Autowired
  public RedisTemplate< String, String > redisTemplate(
      final JedisConnectionFactory connectionFactory ) {
    final RedisTemplate< String, String > template =
        new RedisTemplate< String, String>();
    template.setConnectionFactory( connectionFactory );
    template.setKeySerializer( new StringRedisSerializer() );
    template.setHashValueSerializer( new StringRedisSerializer() );
    template.setHashKeySerializer( new StringRedisSerializer() );
    template.setValueSerializer( new StringRedisSerializer() );
    template.setStringSerializer( new StringRedisSerializer() );
    return template;
  }
 
}
