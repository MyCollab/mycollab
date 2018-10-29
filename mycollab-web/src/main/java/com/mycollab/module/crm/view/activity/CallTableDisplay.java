/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.view.activity;

import com.mycollab.common.GridFieldMeta;
import com.mycollab.module.crm.domain.SimpleCall;
import com.mycollab.module.crm.domain.criteria.CallSearchCriteria;
import com.mycollab.module.crm.service.CallService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.web.ui.table.DefaultPagedGrid;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0.0
 */
// TODO
public class CallTableDisplay extends DefaultPagedGrid<CallService, CallSearchCriteria, SimpleCall> {
    private static final long serialVersionUID = 1L;

    public CallTableDisplay(GridFieldMeta requireColumn, List<GridFieldMeta> displayColumns) {
        super(AppContextUtil.getSpringBean(CallService.class), SimpleCall.class, requireColumn, displayColumns);

//        this.addGeneratedColumn("subject", (source, itemId, columnId) -> {
//            final SimpleCall call = getBeanByIndex(itemId);
//
//            LabelLink b = new LabelLink(call.getSubject(), CrmLinkGenerator.generateCallPreviewLink(call.getId()));
//            if (CallStatus.Held.name().equals(call.getStatus())) {
//                b.addStyleName(WebThemes.LINK_COMPLETED);
//            }
//            return b;
//        });
//
//        this.addGeneratedColumn("isClosed", (source, itemId, columnId) -> {
//            final SimpleCall call = getBeanByIndex(itemId);
//            MButton b = new MButton("", clickEvent -> fireTableEvent(new TableClickEvent(CallTableDisplay.this, call, "isClosed")))
//                    .withIcon(VaadinIcons.TRASH).withStyleName(WebThemes.BUTTON_LINK);
//            b.setDescription(UserUIContext.getMessage(CallI18nEnum.OPT_CLOSE_THIS_CALL));
//            return b;
//        });
//
//        this.addGeneratedColumn("startdate", (source, itemId, columnId) -> {
//            final SimpleCall call = getBeanByIndex(itemId);
//            return new Label(UserUIContext.formatDateTime(call.getStartdate()));
//        });
//
//        this.addGeneratedColumn("status", (source, itemId, columnId) -> {
//            final SimpleCall call = getBeanByIndex(itemId);
//            return ELabel.i18n(call.getStatus(), CallStatus.class);
//        });
    }
}
