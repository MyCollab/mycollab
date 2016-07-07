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

package com.mycollab.module.crm.view.activity;

import com.mycollab.common.TableViewField;
import com.mycollab.module.crm.data.CrmLinkBuilder;
import com.mycollab.module.crm.domain.SimpleCall;
import com.mycollab.module.crm.domain.criteria.CallSearchCriteria;
import com.mycollab.module.crm.service.CallService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.web.ui.LabelLink;
import com.mycollab.vaadin.web.ui.UIConstants;
import com.mycollab.vaadin.web.ui.table.DefaultPagedBeanTable;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Label;
import org.vaadin.viritin.button.MButton;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0.0
 */
public class CallTableDisplay extends DefaultPagedBeanTable<CallService, CallSearchCriteria, SimpleCall> {
    private static final long serialVersionUID = 1L;

    public CallTableDisplay(TableViewField requireColumn, List<TableViewField> displayColumns) {
        super(AppContextUtil.getSpringBean(CallService.class), SimpleCall.class, requireColumn, displayColumns);

        this.addGeneratedColumn("subject", (source, itemId, columnId) -> {
            final SimpleCall call = getBeanByIndex(itemId);

            LabelLink b = new LabelLink(call.getSubject(), CrmLinkBuilder.generateCallPreviewLinkFul(call.getId()));
            if ("Held".equals(call.getStatus())) {
                b.addStyleName(UIConstants.LINK_COMPLETED);
            }
            return b;
        });

        this.addGeneratedColumn("isClosed", (source, itemId, columnId) -> {
            final SimpleCall call = getBeanByIndex(itemId);
            MButton b = new MButton("", clickEvent -> fireTableEvent(new TableClickEvent(CallTableDisplay.this, call, "isClosed")))
                    .withIcon(FontAwesome.TRASH_O).withStyleName(UIConstants.BUTTON_LINK);
            b.setDescription("Close this call");
            return b;
        });

        this.addGeneratedColumn("startdate", (source, itemId, columnId) -> {
            final SimpleCall call = getBeanByIndex(itemId);
            return new Label(AppContext.formatDateTime(call.getStartdate()));
        });
    }
}
