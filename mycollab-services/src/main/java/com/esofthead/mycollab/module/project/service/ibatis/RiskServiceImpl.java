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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esofthead.mycollab.cache.CacheUtils;
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
import com.esofthead.mycollab.module.project.dao.RiskMapper;
import com.esofthead.mycollab.module.project.dao.RiskMapperExt;
import com.esofthead.mycollab.module.project.domain.Risk;
import com.esofthead.mycollab.module.project.domain.SimpleRisk;
import com.esofthead.mycollab.module.project.domain.criteria.RiskSearchCriteria;
import com.esofthead.mycollab.module.project.service.ProjectActivityStreamService;
import com.esofthead.mycollab.module.project.service.ProjectGenericTaskService;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.project.service.RiskService;
import com.esofthead.mycollab.schedule.email.project.ProjectRiskRelayEmailNotificationAction;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Service
@Transactional
@Traceable(module = ModuleNameConstants.PRJ, nameField = "riskname", type = ProjectTypeConstants.RISK, extraFieldName = "projectid")
@Auditable(module = ModuleNameConstants.PRJ, type = ProjectTypeConstants.RISK)
public class RiskServiceImpl extends
		DefaultService<Integer, Risk, RiskSearchCriteria> implements
		RiskService {

	@Autowired
	private RiskMapper riskMapper;

	@Autowired
	private RiskMapperExt riskMapperExt;

	@Autowired
	private RelayEmailNotificationService relayEmailNotificationService;

	@Override
	public ICrudGenericDAO<Integer, Risk> getCrudMapper() {
		return riskMapper;
	}

	@Override
	public ISearchableDAO<RiskSearchCriteria> getSearchMapper() {
		return riskMapperExt;
	}

	@Override
	public SimpleRisk findById(int riskId, int sAccountId) {
		return riskMapperExt.findRiskById(riskId);
	}

	@Override
	public int saveWithSession(Risk record, String username) {
		int recordId = super.saveWithSession(record, username);
		relayEmailNotificationService.saveWithSession(
				createNotification(record, username, recordId,
						MonitorTypeConstants.CREATE_ACTION), username);
		CacheUtils.cleanCaches(record.getSaccountid(), ProjectService.class,
				ProjectGenericTaskService.class,
				ProjectActivityStreamService.class);
		return recordId;
	}

	@Override
	public int updateWithSession(Risk record, String username) {
		relayEmailNotificationService.saveWithSession(
				createNotification(record, username, record.getId(),
						MonitorTypeConstants.UPDATE_ACTION), username);
		CacheUtils.cleanCaches(record.getSaccountid(),
				ProjectActivityStreamService.class);
		return super.updateWithSession(record, username);
	}

	@Override
	public int removeWithSession(Integer primaryKey, String username,
			int accountId) {
		CacheUtils.cleanCaches(accountId, ProjectService.class,
				ProjectGenericTaskService.class,
				ProjectActivityStreamService.class);
		return super.removeWithSession(primaryKey, username, accountId);
	}

	@Override
	public void removeByCriteria(RiskSearchCriteria criteria, int accountId) {
		CacheUtils.cleanCaches(accountId, ProjectService.class,
				ProjectGenericTaskService.class,
				ProjectActivityStreamService.class);
		super.removeByCriteria(criteria, accountId);
	}

	@Override
	public void massRemoveWithSession(List<Integer> primaryKeys,
			String username, int accountId) {
		CacheUtils.cleanCaches(accountId, ProjectService.class,
				ProjectGenericTaskService.class,
				ProjectActivityStreamService.class);
		super.massRemoveWithSession(primaryKeys, username, accountId);
	}

	private RelayEmailNotification createNotification(Risk record,
			String username, int recordId, String action) {
		RelayEmailNotification relayNotification = new RelayEmailNotification();
		relayNotification.setChangeby(username);
		relayNotification.setChangecomment("");
		int sAccountId = record.getSaccountid();
		relayNotification.setSaccountid(sAccountId);
		relayNotification.setType(ProjectTypeConstants.RISK);
		relayNotification.setAction(action);
		relayNotification
				.setEmailhandlerbean(ProjectRiskRelayEmailNotificationAction.class
						.getName());
		relayNotification.setTypeid("" + recordId);
		relayNotification.setExtratypeid(record.getProjectid());
		return relayNotification;
	}
}
