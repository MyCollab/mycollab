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
package com.esofthead.mycollab.module.crm.view.activity;

import com.esofthead.mycollab.module.crm.i18n.MeetingI18nEnum;
import com.esofthead.mycollab.module.crm.ui.components.HistoryLogWindow;
import com.esofthead.mycollab.utils.FieldGroupFomatter;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class MeetingHistoryLogWindow extends HistoryLogWindow {
	private static final long serialVersionUID = 1L;

	public static final FieldGroupFomatter meetingFormatter;

	static {
		meetingFormatter = new FieldGroupFomatter();

		meetingFormatter.generateFieldDisplayHandler("subject",
				MeetingI18nEnum.FORM_SUBJECT);
		meetingFormatter.generateFieldDisplayHandler("status",
				MeetingI18nEnum.FORM_STATUS);
		meetingFormatter.generateFieldDisplayHandler("startdate",
				MeetingI18nEnum.FORM_START_DATE_TIME,
				FieldGroupFomatter.DATETIME_FIELD);
		meetingFormatter.generateFieldDisplayHandler("enddate",
				MeetingI18nEnum.FORM_END_DATE_TIME,
				FieldGroupFomatter.DATETIME_FIELD);
		meetingFormatter.generateFieldDisplayHandler("location",
				MeetingI18nEnum.FORM_LOCATION);
	}

	public MeetingHistoryLogWindow(String module, String type) {
		super(module, type);
	}

	@Override
	protected FieldGroupFomatter buildFormatter() {
		return meetingFormatter;
	}

}
