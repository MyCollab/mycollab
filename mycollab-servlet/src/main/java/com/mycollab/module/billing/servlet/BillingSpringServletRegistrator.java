package com.mycollab.module.billing.servlet;

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
public class BillingSpringServletRegistrator {
    @Bean("confirmEmailServlet")
    public ServletRegistrationBean confirmEmailServlet() {
        return new ServletRegistrationBean(new ConfirmEmailHandler(), "/user/confirm_signup/*");
    }


    @Bean("resetPasswordServlet")
    public ServletRegistrationBean resetPasswordServlet() {
        return new ServletRegistrationBean(new ResetPasswordHandler(), "/user/recoverypassword/action/*");
    }

    @Bean("resetPasswordServlet")
    public ServletRegistrationBean resetPasswordPage() {
        return new ServletRegistrationBean(new ResetPasswordHandler(), "/user/recoverypassword/*");
    }
}
