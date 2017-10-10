/**
 * mycollab-ui - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.ui.formatter;

import com.mycollab.common.domain.AuditChangeItem;
import com.mycollab.vaadin.UserUIContext;
import com.hp.gagawa.java.elements.Li;

/**
 * @author MyCollab Ltd
 * @since 5.3.4
 */
public class DefaultFieldDisplayHandler {
    private Enum displayName;
    private HistoryFieldFormat format;

    public DefaultFieldDisplayHandler(Enum displayName) {
        this(displayName, new DefaultHistoryFieldFormat());
    }

    public DefaultFieldDisplayHandler(Enum displayName, HistoryFieldFormat format) {
        this.displayName = displayName;
        this.format = format;
    }

    public Enum getDisplayName() {
        return displayName;
    }

    public HistoryFieldFormat getFormat() {
        return format;
    }

    public String generateLogItem(AuditChangeItem item) {
        Li li = new Li().appendText(UserUIContext.getMessage(displayName) + ": ")
                .appendText(format.toString(item.getOldvalue()))
                .appendText("&nbsp; &rarr; &nbsp; ")
                .appendText(format.toString(item.getNewvalue()));
        return li.write();
    }
}
