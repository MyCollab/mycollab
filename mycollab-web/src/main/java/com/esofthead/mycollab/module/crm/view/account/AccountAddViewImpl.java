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
package com.esofthead.mycollab.module.crm.view.account;

import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.SimpleAccount;
import com.esofthead.mycollab.module.crm.i18n.AccountI18nEnum;
import com.esofthead.mycollab.module.crm.ui.CrmAssetsManager;
import com.esofthead.mycollab.module.crm.ui.components.AbstractEditItemComp;
import com.esofthead.mycollab.module.crm.ui.components.DynaFormLayout;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.esofthead.mycollab.vaadin.ui.AdvancedEditBeanForm;
import com.esofthead.mycollab.vaadin.ui.EditFormControlsGenerator;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.vaadin.server.Resource;
import com.vaadin.ui.ComponentContainer;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
@ViewComponent
public class AccountAddViewImpl extends AbstractEditItemComp<SimpleAccount> implements AccountAddView {
    private static final long serialVersionUID = 1L;

    @Override
    protected String initFormTitle() {
        return (beanItem.getId() == null) ? AppContext.getMessage(AccountI18nEnum.VIEW_NEW_TITLE) :
                beanItem.getAccountname();
    }

    @Override
    protected Resource initFormIconResource() {
        return CrmAssetsManager.getAsset(CrmTypeConstants.ACCOUNT);
    }

    @Override
    protected ComponentContainer createButtonControls() {
        return new EditFormControlsGenerator<>(editForm).createButtonControls();
    }

    @Override
    protected AdvancedEditBeanForm<SimpleAccount> initPreviewForm() {
        return new AdvancedEditBeanForm<>();
    }

    @Override
    protected IFormLayoutFactory initFormLayoutFactory() {
        return new DynaFormLayout(CrmTypeConstants.ACCOUNT, AccountDefaultDynaFormLayoutFactory.getForm());
    }

    @Override
    protected AbstractBeanFieldGroupEditFieldFactory<SimpleAccount> initBeanFormFieldFactory() {
        return new AccountEditFormFieldFactory<>(editForm);
    }
}
