package com.mycollab.spring;

import com.google.common.eventbus.AsyncEventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.concurrent.Executors;

/**
 * @author MyCollab Ltd
 * @since 5.1.0
 */
@Configuration
@Profile({"production", "test"})
public class AppEventBus {
    private static Logger LOG = LoggerFactory.getLogger(AppEventBus.class);

    @Bean
    public AsyncEventBus asyncEventBus() {
        return new AsyncEventBus(Executors.newCachedThreadPool(),
                (throwable, subscriberExceptionContext) -> LOG.error("Error in event bus execution", throwable));
    }
}
