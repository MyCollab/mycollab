/**
 * mycollab-services-community - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.community.module.project.service.impl;

import com.mycollab.core.MyCollabException;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.service.impl.AbstractProjectTicketServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author MyCollab Ltd
 * @since 5.4.2
 */
@Service
public class ProjectTicketServiceImpl extends AbstractProjectTicketServiceImpl {
    @Override
    public void updateAssignmentValue(ProjectTicket assignment, String username) {
        throw new MyCollabException("Not support this operation in the community edition");
    }

    @Override
    public void closeSubAssignmentOfMilestone(Integer milestoneId) {
        throw new MyCollabException("Not support this operation in the community edition");
    }
}
