/**
 * This file is part of mycollab-ui.
 *
 * mycollab-ui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-ui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-ui.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.utils;

import com.esofthead.mycollab.common.domain.AuditChangeItem;
import com.esofthead.mycollab.common.domain.SimpleAuditLog;
import com.esofthead.mycollab.utils.FieldGroupFormatter.FieldDisplayHandler;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.3.3
 */
public class AuditLogPrinter {
    private FieldGroupFormatter groupFormatter;

    public AuditLogPrinter(FieldGroupFormatter groupFormatter) {
        this.groupFormatter = groupFormatter;
    }

    public String generateChangeSet(SimpleAuditLog auditLog) {
        StringBuffer str = new StringBuffer("");
        boolean isAppended = false;
        List<AuditChangeItem> changeItems = auditLog.getChangeItems();
        if (CollectionUtils.isNotEmpty(changeItems)) {
            for (AuditChangeItem item : changeItems) {
                String fieldName = item.getField();
                FieldDisplayHandler fieldDisplayHandler = groupFormatter.getFieldDisplayHandler(fieldName);
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