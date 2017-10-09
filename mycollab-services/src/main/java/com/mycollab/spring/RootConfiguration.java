package com.mycollab.spring;

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
@Profile("production")
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {
        "com.mycollab.aspect",
        "com.mycollab.cache.service",
        "com.mycollab.common.service",
        "com.mycollab.form.service",
        "com.mycollab.db.migration.service",
        "com.mycollab.module.billing.esb",
        "com.mycollab.module.common.esb",
        "com.mycollab.module.crm.service",
        "com.mycollab.module.crm.schedule.email.service",
        "com.mycollab.module.ecm.esb",
        "com.mycollab.module.ecm.service",
        "com.mycollab.module.file.service",
        "com.mycollab.module.mail.service",
        "com.mycollab.module.page.service",
        "com.mycollab.module.project.esb",
        "com.mycollab.module.project.service",
        "com.mycollab.module.project.schedule.email.service",
        "com.mycollab.module.tracker.service",
        "com.mycollab.module.user.esb",
        "com.mycollab.module.user.service",
        "com.mycollab.reporting.configuration",
        "com.mycollab.schedule.spring",
        "com.mycollab.schedule.jobs",
        "com.mycollab.spring",
        "com.mycollab.vaadin.mvp.service",
        "com.mycollab.vaadin.ui.registry",
        "com.mycollab.community.configuration",
        "com.mycollab.premium.configuration",
        "com.mycollab.ondemand.configuration"},
        excludeFilters = {@ComponentScan.Filter(classes = {Controller.class})})
public class RootConfiguration {
}
