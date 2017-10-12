/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
