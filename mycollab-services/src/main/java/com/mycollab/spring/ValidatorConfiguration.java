package com.mycollab.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * @author MyCollab Ltd.
 * @since 4.6.0
 */
@Configuration
@Profile("production")
public class ValidatorConfiguration {

    @Bean
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setMappingLocations(new ClassPathResource("validator/crm-constraints.xml"),
                new ClassPathResource("validator/user-constraints.xml"),
                new ClassPathResource("validator/project-constraints.xml"),
                new ClassPathResource("validator/tracker-constraints.xml"));
        return bean;
    }
}
