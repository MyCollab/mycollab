/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.module.crm.view.cases;

import com.esofthead.mycollab.common.UrlEncodeDecoder;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.crm.events.CaseEvent;
import com.esofthead.mycollab.mobile.shell.events.ShellEvent;
import com.esofthead.mycollab.mobile.module.crm.view.AbstractCrmPresenter;
import com.esofthead.mycollab.module.crm.domain.CaseWithBLOBs;
import com.esofthead.mycollab.module.crm.domain.SimpleCase;
import com.esofthead.mycollab.module.crm.service.CaseService;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.DefaultEditFormHandler;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.ComponentContainer;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class CaseAddPresenter extends AbstractCrmPresenter<CaseAddView> {
    private static final long serialVersionUID = 8258508255937580306L;

    public CaseAddPresenter() {
        super(CaseAddView.class);
    }

    @Override
    protected void postInitView() {
        view.getEditFormHandlers().addFormHandler(new DefaultEditFormHandler<SimpleCase>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSave(final SimpleCase cases) {
                saveCase(cases);
                EventBusFactory.getInstance().post(new ShellEvent.NavigateBack(this, null));
            }

            @Override
            public void onSaveAndNew(final SimpleCase cases) {
                saveCase(cases);
                EventBusFactory.getInstance().post(new CaseEvent.GotoAdd(this, null));
            }
        });
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        if (AppContext.canWrite(RolePermissionCollections.CRM_CASE)) {

            SimpleCase cases = null;
            if (data.getParams() instanceof SimpleCase) {
                cases = (SimpleCase) data.getParams();
            } else if (data.getParams() instanceof Integer) {
                CaseService caseService = ApplicationContextUtil.getSpringBean(CaseService.class);
                cases = caseService.findById((Integer) data.getParams(), AppContext.getAccountId());
            }
            if (cases == null) {
                NotificationUtil.showRecordNotExistNotification();
                return;
            }
            super.onGo(container, data);
            view.editItem(cases);

            if (cases.getId() == null) {
                AppContext.addFragment("crm/cases/add", AppContext.getMessage(GenericI18Enum.BROWSER_ADD_ITEM_TITLE, "Case"));
            } else {
                AppContext.addFragment("crm/cases/edit/" + UrlEncodeDecoder.encode(cases.getId()),
                        AppContext.getMessage(GenericI18Enum.BROWSER_EDIT_ITEM_TITLE, "Case", cases.getSubject()));
            }
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }

    private void saveCase(CaseWithBLOBs cases) {
        CaseService caseService = ApplicationContextUtil.getSpringBean(CaseService.class);
        cases.setSaccountid(AppContext.getAccountId());
        if (cases.getId() == null) {
            caseService.saveWithSession(cases, AppContext.getUsername());
        } else {
            caseService.updateWithSession(cases, AppContext.getUsername());
        }
    }
}
