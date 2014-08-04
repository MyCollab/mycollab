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
