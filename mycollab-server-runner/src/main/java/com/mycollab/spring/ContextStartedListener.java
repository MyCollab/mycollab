package com.mycollab.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
@Configuration
@Profile("production")
public class ContextStartedListener implements ApplicationListener<ContextRefreshedEvent> {
    private static Logger LOG = LoggerFactory.getLogger(ContextStartedListener.class);

    private boolean isStarted = false;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        LOG.info("MyCollab is ready for usage");
        isStarted = true;
    }

    @Bean
    public ServletRegistrationBean checkStartedServlet() {
        return new ServletRegistrationBean(new CheckAppStartedServlet(), "/checkStarted");
    }

    class CheckAppStartedServlet extends HttpServlet {
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            if (isStarted) {
                response.getWriter().write("Started");
            } else {
                response.getWriter().write("NotStarted");
            }
        }
    }
}
