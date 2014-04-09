/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.project.view.bug;

import java.util.GregorianCalendar;

import com.esofthead.mycollab.common.domain.MonitorItem;
import com.esofthead.mycollab.common.domain.criteria.MonitorSearchCriteria;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.ui.components.CompFollowersSheet;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.vaadin.AppContext;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
class BugFollowersSheet extends CompFollowersSheet<SimpleBug> {
	private static final long serialVersionUID = 1L;

	public BugFollowersSheet(SimpleBug bug) {
		super(bug);
	}

	@Override
	protected void loadMonitorItems() {
		MonitorSearchCriteria searchCriteria = new MonitorSearchCriteria();
		searchCriteria.setTypeId(new NumberSearchField(bean.getId()));
		searchCriteria.setType(new StringSearchField(ProjectTypeConstants.BUG));
		tableItem.setSearchCriteria(searchCriteria);
	}

	@Override
	protected boolean saveMonitorItem(String username) {

		if (!monitorItemService.isUserWatchingItem(username,
				ProjectTypeConstants.BUG, bean.getId())) {

			MonitorItem monitorItem = new MonitorItem();
			monitorItem.setMonitorDate(new GregorianCalendar().getTime());
			monitorItem.setType(ProjectTypeConstants.BUG);
			monitorItem.setTypeid(bean.getId());
			monitorItem.setUser(username);
			monitorItem.setSaccountid(AppContext.getAccountId());
			monitorItemService.saveWithSession(monitorItem,
					AppContext.getUsername());
			return true;

		}
		return false;
	}

	@Override
	protected void saveRelayNotification() {

	}

	@Override
	protected boolean isEnableAdd() {
		return CurrentProjectVariables
				.canWrite(ProjectRolePermissionCollections.BUGS);
	}
}