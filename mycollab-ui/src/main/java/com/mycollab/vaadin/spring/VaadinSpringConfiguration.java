package com.mycollab.vaadin.spring;

import com.mycollab.vaadin.AppServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
@Configuration
@Profile("production")
public class VaadinSpringConfiguration {

    @Bean("mainServlet")
    public ServletRegistrationBean mainServlet() {
        ServletRegistrationBean bean = new ServletRegistrationBean(new AppServlet(), "/*");
        bean.setLoadOnStartup(1);
        return bean;
    }
}
