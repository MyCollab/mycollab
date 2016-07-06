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
package com.mycollab.vaadin.ui.registry;

import com.mycollab.common.domain.AuditChangeItem;
import com.mycollab.common.domain.SimpleActivityStream;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.ui.formatter.DefaultFieldDisplayHandler;
import com.mycollab.vaadin.ui.formatter.FieldGroupFormatter;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author MyCollab Ltd
 * @since 5.2.11
 */
@Component
public class AuditLogRegistry implements InitializingBean {
    private Map<String, FieldGroupFormatter> auditPrinters;

    @Override
    public void afterPropertiesSet() throws Exception {
        auditPrinters = new HashMap<>();
    }

    public void registerAuditLogHandler(String type, FieldGroupFormatter fieldGroupFormatter) {
        auditPrinters.put(type, fieldGroupFormatter);
    }

    public static FieldGroupFormatter getFieldGroupFormatter(String type) {
        AuditLogRegistry auditLogRegistry = AppContextUtil.getSpringBean(AuditLogRegistry.class);
        return auditLogRegistry.auditPrinters.get(type);
    }

    public String generatorDetailChangeOfActivity(SimpleActivityStream activityStream) {
        if (activityStream.getAssoAuditLog() != null) {
            FieldGroupFormatter groupFormatter = auditPrinters.get(activityStream.getType());
            if (groupFormatter != null) {
                StringBuffer str = new StringBuffer("");
                boolean isAppended = false;
                List<AuditChangeItem> changeItems = activityStream.getAssoAuditLog().getChangeItems();
                if (CollectionUtils.isNotEmpty(changeItems)) {
                    for (AuditChangeItem item : changeItems) {
                        String fieldName = item.getField();
                        DefaultFieldDisplayHandler fieldDisplayHandler = groupFormatter.getFieldDisplayHandler(fieldName);
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
        return "";
    }
}
