package com.mycollab.servlet;

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
public class GenericSpringServletRegistor {

    @Bean("errorServlet")
    public ServletRegistrationBean errorServlet() {
        return new ServletRegistrationBean(new AppExceptionServletHandler(), "/error");
    }

    @Bean("tooltipServlet")
    public ServletRegistrationBean tooltipServlet() {
        return new ServletRegistrationBean(new TooltipGeneratorServletRequestHandler(), "/tooltip/*");
    }
}
