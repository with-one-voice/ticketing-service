package com.onevoice.payment.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class EventConfig {

    //  org.springframework.context.ApplicationEvent;
    //  ApplicationEventMulticaster 빈을 등록하면 이벤트 리스너는 이벤트 퍼블리셔와 다른 스레드에서 비동기로 실행된다.
    //  기본적으로 SimpleAsyncTaskExecutor 는 스레드를 무한정 생성하며,
    //  이는 리소스 관리에 부담을 줄 수 있다.
    //  따라서 실제 애플리케이션에서는 ThreadPoolTaskExecutor 등으로
    //  스레드 풀 크기를 제한해야 한다.
    @Bean(name = "applicationEventMulticaster")
    public ApplicationEventMulticaster simpleApplicationEventMulticaster() {
        SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();

        // ThreadPoolTaskExecutor 를 사용하여 더 나은 스레드 풀 관리
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(10);
        taskExecutor.setMaxPoolSize(20);
        taskExecutor.setQueueCapacity(50);
        taskExecutor.initialize();

        eventMulticaster.setTaskExecutor(taskExecutor);

        return eventMulticaster;
    }
}
