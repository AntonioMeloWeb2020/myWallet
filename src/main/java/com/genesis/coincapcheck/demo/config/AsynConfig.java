package com.genesis.coincapcheck.demo.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.genesis.coincapcheck.demo.utils.Constants;

@Configuration
@EnableAsync
public class AsynConfig {
	@Bean
	public Executor asyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(Constants.ASYNC_THREAD_MAX_POOL);
	    executor.setMaxPoolSize(Constants.ASYNC_THREAD_MAX_POOL);
		return executor;
	}
}
