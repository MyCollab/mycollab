/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.stereotype.Controller;

/**
 * @author MyCollab Ltd.
 * @since 4.6.0
 */
@Configuration
@EnableSpringConfigured
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
@Profile("production")
public class RootConfiguration {
}
