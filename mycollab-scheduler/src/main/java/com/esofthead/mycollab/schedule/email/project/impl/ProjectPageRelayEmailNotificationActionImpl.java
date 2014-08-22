/**
 * This file is part of mycollab-scheduler.
 *
 * mycollab-scheduler is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-scheduler is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-scheduler.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.schedule.email.project.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.wiki.domain.Page;
import com.esofthead.mycollab.module.wiki.service.WikiService;
import com.esofthead.mycollab.schedule.email.ItemFieldMapper;
import com.esofthead.mycollab.schedule.email.MailContext;
import com.esofthead.mycollab.schedule.email.project.ProjectPageRelayEmailNotificationAction;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.4.0
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ProjectPageRelayEmailNotificationActionImpl extends
		SendMailToAllMembersAction<Page> implements
		ProjectPageRelayEmailNotificationAction {

	@Autowired
	private WikiService wikiService;

	@Override
	protected Page getBeanInContext(MailContext<Page> context) {
		// return wikiService.getPage(context.getTypeid());
		return null;
	}

	@Override
	protected void buildExtraTemplateVariables(MailContext<Page> context) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getItemName() {
		return StringUtils.trim(bean.getSubject(), 100);
	}

	@Override
	protected String getCreateSubject(MailContext<Page> context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getUpdateSubject(MailContext<Page> context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getCommentSubject(MailContext<Page> context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ItemFieldMapper getItemFieldMapper() {
		// TODO Auto-generated method stub
		return null;
	}

}
