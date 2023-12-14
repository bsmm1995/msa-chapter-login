package com.bsmm.login;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import redis.embedded.RedisServer;

import java.io.IOException;

@SpringBootApplication
public class SpringBootLoginApplication {
	private RedisServer redisServer;
	public static void main(String[] args) {
		SpringApplication.run(SpringBootLoginApplication.class, args);
	}

	@PostConstruct
	public void startRedis() throws IOException {
		redisServer = new RedisServer(6379);
		redisServer.start();
	}

	@PreDestroy
	public void stopRedis() {
		redisServer.stop();
	}
}
