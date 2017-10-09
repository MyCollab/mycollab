package com.mycollab.module.page.servlet;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author MyCollab Ltd
 * @since 5.5.0
 */
@Configuration
@Profile("production")
public class PageSpringServletRegistor {
    @Bean("fileUploadServlet")
    public ServletRegistrationBean fileUploadServlet() {
        return new ServletRegistrationBean(new FileUploadServlet(), "/page/upload");
    }
}
