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
package com.esofthead.mycollab.module.project.service.ibatis;

import com.esofthead.mycollab.common.NotificationType;
import com.esofthead.mycollab.core.cache.CacheKey;
import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultCrudService;
import com.esofthead.mycollab.module.project.dao.ProjectNotificationSettingMapper;
import com.esofthead.mycollab.module.project.domain.ProjectNotificationSetting;
import com.esofthead.mycollab.module.project.domain.ProjectNotificationSettingExample;
import com.esofthead.mycollab.module.project.service.ProjectNotificationSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
public class ProjectNotificationSettingServiceImpl extends DefaultCrudService<Integer, ProjectNotificationSetting> implements
        ProjectNotificationSettingService {

    @Autowired
    private ProjectNotificationSettingMapper projectNotificationSettingMapper;

    @Override
    public ICrudGenericDAO<Integer, ProjectNotificationSetting> getCrudMapper() {
        return projectNotificationSettingMapper;
    }

    @Override
    public ProjectNotificationSetting findNotification(String username, Integer projectId, @CacheKey Integer sAccountId) {
        ProjectNotificationSettingExample ex = new ProjectNotificationSettingExample();
        ex.createCriteria().andUsernameEqualTo(username).andProjectidEqualTo(projectId).andSaccountidEqualTo(sAccountId);
        List<ProjectNotificationSetting> settings = projectNotificationSettingMapper.selectByExample(ex);
        if (settings.size() > 0) {
            return settings.get(0);
        } else {
            ProjectNotificationSetting setting = new ProjectNotificationSetting();
            setting.setLevel(NotificationType.Default.name());
            setting.setProjectid(projectId);
            setting.setSaccountid(sAccountId);
            setting.setUsername(username);
            return setting;
        }
    }

    @Override
    public List<ProjectNotificationSetting> findNotifications(Integer projectId, @CacheKey Integer sAccountId) {
        ProjectNotificationSettingExample ex = new ProjectNotificationSettingExample();
        ex.createCriteria().andProjectidEqualTo(projectId).andSaccountidEqualTo(sAccountId);
        return projectNotificationSettingMapper.selectByExample(ex);
    }

}
