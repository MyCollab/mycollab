package com.mycollab.cache.spring;

import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.spring.starter.embedded.InfinispanCacheConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"production"})
public class CacheConfiguration {

    @Bean
    public InfinispanCacheConfigurer cacheConfigurer() {
        return manager -> {
            final org.infinispan.configuration.cache.Configuration ispnConfig = new ConfigurationBuilder()
                    .clustering()
                    .cacheMode(CacheMode.LOCAL)
                    .build();

            manager.defineConfiguration("local-sync-config", ispnConfig);
        };
    }
}
