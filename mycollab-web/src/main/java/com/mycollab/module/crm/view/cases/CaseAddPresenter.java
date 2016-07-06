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

import com.mycollab.common.UrlEncodeDecoder;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.ResourceNotFoundException;
import com.mycollab.core.SecureAccessException;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.CaseWithBLOBs;
import com.mycollab.module.crm.domain.SimpleCase;
import com.mycollab.module.crm.events.CaseEvent;
import com.mycollab.module.crm.i18n.CaseI18nEnum;
import com.mycollab.module.crm.service.CaseService;
import com.mycollab.module.crm.view.CrmGenericPresenter;
import com.mycollab.module.crm.view.CrmModule;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.events.IEditFormHandler;
import com.mycollab.vaadin.mvp.ScreenData;
import com.vaadin.ui.ComponentContainer;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class CaseAddPresenter extends CrmGenericPresenter<CaseAddView> {
    private static final long serialVersionUID = 1L;

    public CaseAddPresenter() {
        super(CaseAddView.class);
    }

    @Override
    protected void postInitView() {
        view.getEditFormHandlers().addFormHandler(new IEditFormHandler<SimpleCase>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSave(final SimpleCase cases) {
                saveCase(cases);
                EventBusFactory.getInstance().post(new CaseEvent.GotoRead(this, cases.getId()));
            }

            @Override
            public void onCancel() {
                EventBusFactory.getInstance().post(new CaseEvent.GotoList(this, null));
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
        CrmModule.navigateItem(CrmTypeConstants.CASE);
        if (AppContext.canWrite(RolePermissionCollections.CRM_CASE)) {
            SimpleCase cases = null;
            if (data.getParams() instanceof SimpleCase) {
                cases = (SimpleCase) data.getParams();
            } else if (data.getParams() instanceof Integer) {
                CaseService caseService = AppContextUtil.getSpringBean(CaseService.class);
                cases = caseService.findById((Integer) data.getParams(), AppContext.getAccountId());
            }
            if (cases == null) {
                throw new ResourceNotFoundException();
            }
            super.onGo(container, data);
            view.editItem(cases);

            if (cases.getId() == null) {
                AppContext.addFragment("crm/cases/add", AppContext.getMessage(GenericI18Enum.BROWSER_ADD_ITEM_TITLE,
                        AppContext.getMessage(CaseI18nEnum.SINGLE)));
            } else {
                AppContext.addFragment("crm/cases/edit/" + UrlEncodeDecoder.encode(cases.getId()),
                        AppContext.getMessage(GenericI18Enum.BROWSER_EDIT_ITEM_TITLE,
                                AppContext.getMessage(CaseI18nEnum.SINGLE), cases.getSubject()));
            }
        } else {
            throw new SecureAccessException();
        }
    }

    private int saveCase(CaseWithBLOBs cases) {
        CaseService caseService = AppContextUtil.getSpringBean(CaseService.class);
        cases.setSaccountid(AppContext.getAccountId());

        if (cases.getId() == null) {
            caseService.saveWithSession(cases, AppContext.getUsername());
        } else {
            caseService.updateWithSession(cases, AppContext.getUsername());
        }
        return cases.getId();
    }
}
