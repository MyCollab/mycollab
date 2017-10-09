package com.mycollab.test.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;

/**
 * @author MyCollab Ltd.
 * @since 4.6.0
 */
@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"com.mycollab.**.service", "com.mycollab.**.spring",
        "com.mycollab.**.jobs", "com.mycollab.**.aspect", "com.mycollab.**.esb"},
        excludeFilters = {@ComponentScan.Filter(classes = {Controller.class})})
@Profile("test")
public class RootConfigurationTest {

}
