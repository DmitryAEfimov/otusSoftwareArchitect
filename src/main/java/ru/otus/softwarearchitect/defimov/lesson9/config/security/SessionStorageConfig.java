package ru.otus.softwarearchitect.defimov.lesson9.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

@Configuration
@EnableRedisHttpSession
public class SessionStorageConfig extends AbstractHttpSessionApplicationInitializer {
	@Value("${spring.redis.password}")
	private String redisPassword;
	@Value("${spring.redis.port}")
	private int redisPort;
	@Value("${spring.redis.host}")
	private String redisHost;

	@Bean
	public RedisConnectionFactory connectionFactory() {

		RedisStandaloneConfiguration redisConfiguration = new RedisStandaloneConfiguration();
		redisConfiguration.setHostName(redisHost);
		redisConfiguration.setPort(redisPort);
		redisConfiguration.setPassword(RedisPassword.of(redisPassword));

		return new JedisConnectionFactory(redisConfiguration);
	}
}
