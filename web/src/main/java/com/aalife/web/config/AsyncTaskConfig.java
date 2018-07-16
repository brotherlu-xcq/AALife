package com.aalife.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author mosesc
 * @date 2018-07-13
 */
@Configuration
@ConfigurationProperties(prefix = "threadPool")
public class AsyncTaskConfig {
    /**
     * 线程池基本线程数
     */
    @Value(value = "${threadPool.size.core}")
    private int coreSize;
    /**
     * 线程池最大线程数
     */
    @Value(value = "${threadPool.size.max}")
    private int maxSize;
    /**
     * 线程池使用的缓冲队列，多余的任务会放在这里，然后由线程池中空闲线程去处理。
     * 放不下新入的任务时，新建线程加入线程池，并处理请求，如果池子大小撑到了maximumPoolSize就用RejectedExecutionHandler来做拒绝处理.
     */
    @Value(value = "${threadPool.size.capacity}")
    private int capacitySize;
    @Value(value = "${threadPool.name}")
    private String poolName;
    @Value(value = "${threadPool.keepAlive}")
    private int keepAlive;
    /**
     * 是否允许核心线程池超时
     */
    @Value(value = "${threadPool.allowCoreTimeOut}")
    private boolean allowCoreTimeOut;

    @Bean
    public Executor createExecutor(){
        ThreadPoolTaskExecutor threadPoolExecutor = new ThreadPoolTaskExecutor();
        threadPoolExecutor.setCorePoolSize(coreSize);
        threadPoolExecutor.setMaxPoolSize(maxSize);
        threadPoolExecutor.setThreadNamePrefix(poolName);
        threadPoolExecutor.setQueueCapacity(capacitySize);
        threadPoolExecutor.setKeepAliveSeconds(keepAlive);
        threadPoolExecutor.setAllowCoreThreadTimeOut(allowCoreTimeOut);
        // 拒绝策略为：由调用线程处理该任务
        threadPoolExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        threadPoolExecutor.initialize();
        return threadPoolExecutor;
    }
}
