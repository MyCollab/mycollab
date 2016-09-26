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
package com.mycollab.module.crm.view.cases;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.iexporter.CSVObjectEntityConverter.FieldMapperDef;
import com.mycollab.module.crm.domain.SimpleCase;
import com.mycollab.module.crm.domain.criteria.CaseSearchCriteria;
import com.mycollab.module.crm.event.CaseEvent;
import com.mycollab.module.crm.service.CaseService;
import com.mycollab.module.crm.ui.components.EntityImportWindow;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;

import java.util.Arrays;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class CaseImportWindow extends EntityImportWindow<SimpleCase> {
    private static final long serialVersionUID = 1L;

    public CaseImportWindow() {
        super(false, "Import Cases", AppContextUtil.getSpringBean(CaseService.class), SimpleCase.class);
    }

    @Override
    protected List<FieldMapperDef> constructCSVFieldMapper() {
        FieldMapperDef[] fields = {
                new FieldMapperDef("priority", "Priority"),
                new FieldMapperDef("status", "Status"),
                new FieldMapperDef("type", "Type"),
                new FieldMapperDef("subject", "Subject"),
                new FieldMapperDef("assignuser", UserUIContext.getMessage(GenericI18Enum.FORM_ASSIGNEE)),
                new FieldMapperDef("reason", "Reason"),
                new FieldMapperDef("origin", "Origin"),
                new FieldMapperDef("email", "Email"),
                new FieldMapperDef("phonenumber", "Phone Number")};
        return Arrays.asList(fields);
    }

    @Override
    protected void reloadWhenBackToListView() {
        EventBusFactory.getInstance().post(new CaseEvent.GotoList(CaseListView.class, new CaseSearchCriteria()));
    }
}
