package com.mycollab.module.file.servlet;

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
public class FileSpringServletRegistrator {
    @Bean("assetServlet")
    public ServletRegistrationBean assetServlet() {
        return new ServletRegistrationBean(new AssetHandler(), "/assets/*");
    }

    @Bean("resourceGetServlet")
    public ServletRegistrationBean resourceGetServlet() {
        return new ServletRegistrationBean(new ResourceGetHandler(), "/file/*");
    }

    @Bean("userAvatarServlet")
    public ServletRegistrationBean userAvatarServlet() {
        return new ServletRegistrationBean(new UserAvatarHttpServletRequestHandler(), "/file/avatar/*");
    }
}
