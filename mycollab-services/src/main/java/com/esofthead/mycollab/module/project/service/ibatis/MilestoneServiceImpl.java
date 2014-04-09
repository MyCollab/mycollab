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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esofthead.mycollab.common.ModuleNameConstants;
import com.esofthead.mycollab.common.MonitorTypeConstants;
import com.esofthead.mycollab.common.domain.RelayEmailNotification;
import com.esofthead.mycollab.common.interceptor.aspect.Auditable;
import com.esofthead.mycollab.common.interceptor.aspect.Traceable;
import com.esofthead.mycollab.common.service.RelayEmailNotificationService;
import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.ISearchableDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultService;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.dao.MilestoneMapper;
import com.esofthead.mycollab.module.project.dao.MilestoneMapperExt;
import com.esofthead.mycollab.module.project.domain.Milestone;
import com.esofthead.mycollab.module.project.domain.SimpleMilestone;
import com.esofthead.mycollab.module.project.domain.criteria.MilestoneSearchCriteria;
import com.esofthead.mycollab.module.project.service.MilestoneService;
import com.esofthead.mycollab.schedule.email.project.ProjectMilestoneRelayEmailNotificationAction;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
@Transactional
@Traceable(module = ModuleNameConstants.PRJ, type = ProjectTypeConstants.MILESTONE, nameField = "name", extraFieldName = "projectid")
@Auditable(module = ModuleNameConstants.PRJ, type = ProjectTypeConstants.MILESTONE)
public class MilestoneServiceImpl extends
		DefaultService<Integer, Milestone, MilestoneSearchCriteria> implements
		MilestoneService {

	@Autowired
	protected MilestoneMapper milestoneMapper;

	@Autowired
	protected MilestoneMapperExt milestoneMapperExt;

	@Autowired
	private RelayEmailNotificationService relayEmailNotificationService;

	@Override
	public ICrudGenericDAO<Integer, Milestone> getCrudMapper() {
		return milestoneMapper;
	}

	@Override
	public ISearchableDAO<MilestoneSearchCriteria> getSearchMapper() {
		return milestoneMapperExt;
	}

	@Override
	public SimpleMilestone findById(int milestoneId, int sAccountId) {
		return milestoneMapperExt.findById(milestoneId);
	}

	@Override
	public int saveWithSession(Milestone record, String username) {
		int recordId = super.saveWithSession(record, username);
		relayEmailNotificationService.saveWithSession(
				createNotification(record, username, recordId,
						MonitorTypeConstants.CREATE_ACTION), username);
		return recordId;
	}

	@Override
	public int updateWithSession(Milestone record, String username) {
		relayEmailNotificationService.saveWithSession(
				createNotification(record, username, record.getId(),
						MonitorTypeConstants.UPDATE_ACTION), username);
		return super.updateWithSession(record, username);
	}

	private RelayEmailNotification createNotification(Milestone record,
			String username, int recordId, String action) {
		RelayEmailNotification relayNotification = new RelayEmailNotification();
		relayNotification.setChangeby(username);
		relayNotification.setChangecomment("");
		relayNotification.setAction(action);
		relayNotification.setSaccountid(record.getSaccountid());
		relayNotification.setType(ProjectTypeConstants.MILESTONE);
		relayNotification
				.setEmailhandlerbean(ProjectMilestoneRelayEmailNotificationAction.class
						.getName());
		relayNotification.setTypeid(recordId);
		relayNotification.setExtratypeid(record.getProjectid());
		return relayNotification;
	}
}
