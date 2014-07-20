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

package com.esofthead.mycollab.common.logging;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.ErrorCode;
import org.apache.log4j.spi.LoggingEvent;

import com.esofthead.mycollab.common.dao.ReportBugIssueMapper;
import com.esofthead.mycollab.common.domain.ReportBugIssueWithBLOBs;
import com.esofthead.mycollab.spring.ApplicationContextUtil;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 *
 */
public class DbLoggingAppender extends AppenderSkeleton {

	@Override
	protected void append(LoggingEvent event) {
		if (this.layout == null) {
			errorHandler.error("No layout for appender " + name, null,
					ErrorCode.MISSING_LAYOUT);
			return;
		}

		StringBuilder message = new StringBuilder(this.layout.format(event));

		if (layout.ignoresThrowable()) {
			String[] messages = event.getThrowableStrRep();
			if (messages != null) {
				for (int j = 0; j < messages.length; ++j) {
					message.append(messages[j], 0, messages[j].length());
					message.append("\r\n");
				}
			}
		}

		ReportBugIssueWithBLOBs record = new ReportBugIssueWithBLOBs();
		record.setErrortrace(message.toString());

		commitLog(record);
	}

	@Override
	public void close() {
	}

	@Override
	public boolean requiresLayout() {
		return true;
	}

	private static final void commitLog(ReportBugIssueWithBLOBs record) {
		try {
			ReportBugIssueMapper mapper = ApplicationContextUtil
					.getSpringBean(ReportBugIssueMapper.class);
			if (mapper != null) {
				mapper.insertSelective(record);
			}

		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println(record.getErrortrace());
		}
	}
}
