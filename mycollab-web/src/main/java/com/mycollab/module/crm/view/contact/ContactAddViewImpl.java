/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.view.contact;

import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.SimpleContact;
import com.mycollab.module.crm.i18n.ContactI18nEnum;
import com.mycollab.module.crm.ui.CrmAssetsManager;
import com.mycollab.module.crm.ui.components.AbstractEditItemComp;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.mycollab.vaadin.ui.AdvancedEditBeanForm;
import com.mycollab.vaadin.ui.IFormLayoutFactory;
import com.mycollab.vaadin.web.ui.DefaultDynaFormLayout;
import com.vaadin.server.Resource;
import com.vaadin.ui.ComponentContainer;

import static com.mycollab.vaadin.web.ui.utils.FormControlsGenerator.generateEditFormControls;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
@ViewComponent
public class ContactAddViewImpl extends AbstractEditItemComp<SimpleContact> implements ContactAddView {
    private static final long serialVersionUID = 1L;

    @Override
    protected String initFormTitle() {
        return (beanItem.getId() == null) ? UserUIContext.getMessage(ContactI18nEnum.NEW) : beanItem.getContactName();
    }

    @Override
    protected Resource initFormIconResource() {
        return CrmAssetsManager.getAsset(CrmTypeConstants.CONTACT);
    }

    @Override
    protected ComponentContainer createButtonControls() {
        return generateEditFormControls(editForm);
    }

    @Override
    protected AdvancedEditBeanForm<SimpleContact> initPreviewForm() {
        return new AdvancedEditBeanForm<>();
    }

    @Override
    protected IFormLayoutFactory initFormLayoutFactory() {
        return new DefaultDynaFormLayout(CrmTypeConstants.CONTACT, ContactDefaultDynaFormLayoutFactory.getForm());
    }

    @Override
    protected AbstractBeanFieldGroupEditFieldFactory<SimpleContact> initBeanFormFieldFactory() {
        return new ContactEditFormFieldFactory<>(editForm);
    }
}