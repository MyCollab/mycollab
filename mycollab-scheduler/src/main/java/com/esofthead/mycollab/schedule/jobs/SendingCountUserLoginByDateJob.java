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
package com.esofthead.mycollab.schedule.jobs;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.esofthead.mycollab.common.domain.MailRecipientField;
import com.esofthead.mycollab.core.arguments.DateSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.module.mail.TemplateGenerator;
import com.esofthead.mycollab.module.mail.service.ExtMailService;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.domain.criteria.UserSearchCriteria;
import com.esofthead.mycollab.module.user.service.UserService;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SendingCountUserLoginByDateJob extends QuartzJobBean {
	private static Logger log = LoggerFactory
			.getLogger(SendingCountUserLoginByDateJob.class);

	@Autowired
	private UserService userService;

	@Autowired
	private ExtMailService extMailService;

	private static final String countUserLoginByDateTemplate = "templates/email/user/countUserLoginByDate.mt";

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		UserSearchCriteria criteria = new UserSearchCriteria();
		criteria.setSaccountid(null);
		criteria.setLastAccessedTime(new DateSearchField(SearchField.AND,
				new Date()));
		List<SimpleUser> lstSimpleUsers = userService
				.findPagableListByCriteria(new SearchRequest<UserSearchCriteria>(
						criteria, 0, Integer.MAX_VALUE));
		if (lstSimpleUsers != null && lstSimpleUsers.size() > 0) {
			TemplateGenerator templateGenerator = new TemplateGenerator(
					"Today system-logins count", countUserLoginByDateTemplate);
			templateGenerator.putVariable("lstUser", lstSimpleUsers);
			templateGenerator.putVariable("count", lstSimpleUsers.size());

			try {
				extMailService.sendHTMLMail("noreply@mycollab.com",
						"noreply@mycollab.com",
						Arrays.asList(new MailRecipientField(
								"hainguyen@esofthead.com", "Hai Nguyen")),
						null, null, templateGenerator.generateSubjectContent(),
						templateGenerator.generateBodyContent(), null);
			} catch (Exception e) {
				log.error("Error whle generate template", e);
			}
		}
	}
}
