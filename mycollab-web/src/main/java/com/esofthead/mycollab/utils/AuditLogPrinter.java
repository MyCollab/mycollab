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
package com.esofthead.mycollab.utils;

import java.util.List;

import com.esofthead.mycollab.common.domain.AuditChangeItem;
import com.esofthead.mycollab.common.domain.SimpleAuditLog;
import com.esofthead.mycollab.utils.FieldGroupFomatter.FieldDisplayHandler;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.3.3
 *
 */
public class AuditLogPrinter {

	private FieldGroupFomatter groupFormatter;

	public AuditLogPrinter(FieldGroupFomatter groupFormatter) {
		this.groupFormatter = groupFormatter;
	}

	public String generateChangeSet(SimpleAuditLog auditLog) {
		StringBuffer str = new StringBuffer("");
		boolean isAppended = false;
		List<AuditChangeItem> changeItems = auditLog.getChangeItems();
		if (changeItems != null && changeItems.size() > 0) {
			for (int i = 0; i < changeItems.size(); i++) {
				AuditChangeItem item = changeItems.get(i);
				String fieldName = item.getField();
				FieldDisplayHandler fieldDisplayHandler = groupFormatter
						.getFieldDisplayHandler(fieldName);
				if (fieldDisplayHandler != null) {
					isAppended = true;
					str.append(fieldDisplayHandler.generateLogItem(item));
				}
			}

		}
		if (isAppended) {
			str.insert(0, "<p>").insert(0, "<ul>");
			str.append("</ul>").append("</p>");
		}
		return str.toString();
	}
}
