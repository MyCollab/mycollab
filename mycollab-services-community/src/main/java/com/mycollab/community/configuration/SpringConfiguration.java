package com.mycollab.community.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author MyCollab Ltd
 * @since 5.4.0
 */
@Configuration
@Profile("production")
@ComponentScan(basePackages = {
        "com.mycollab.community.common.service",
        "com.mycollab.community.module.billing.service",
        "com.mycollab.community.module.common.service",
        "com.mycollab.community.module.ecm.service",
        "com.mycollab.community.module.file.service",
        "com.mycollab.community.module.project.service",
        "com.mycollab.community.module.user.service",
        "com.mycollab.community.schedule.jobs",
        "com.mycollab.community.schedule.spring",
        "com.mycollab.community.vaadin.ui.service",
        "com.mycollab.community.module.project.view.service"
})
public class SpringConfiguration {
}
