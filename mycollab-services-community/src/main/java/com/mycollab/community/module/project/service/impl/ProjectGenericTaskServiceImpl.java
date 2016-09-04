/**
 * This file is part of mycollab-services-community.
 *
 * mycollab-services-community is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services-community is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services-community.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.community.module.project.service.impl;

import com.mycollab.core.MyCollabException;
import com.mycollab.module.project.domain.ProjectGenericTask;
import com.mycollab.module.project.service.impl.AbstractProjectGenericTaskServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author MyCollab Ltd
 * @since 5.4.2
 */
@Service
public class ProjectGenericTaskServiceImpl extends AbstractProjectGenericTaskServiceImpl{
    @Override
    public void updateAssignmentValue(ProjectGenericTask assignment) {
        throw new MyCollabException("Not support this operation in the community edition");
    }
}
