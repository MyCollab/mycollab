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

import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.MeetingWithBLOBs;
import com.mycollab.module.crm.i18n.MeetingI18nEnum;
import com.mycollab.module.crm.ui.CrmAssetsManager;
import com.mycollab.module.crm.ui.components.AbstractEditItemComp;
import com.mycollab.module.crm.ui.components.RelatedEditItemField;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.events.HasEditFormHandlers;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.*;
import com.mycollab.vaadin.web.ui.DefaultDynaFormLayout;
import com.mycollab.vaadin.web.ui.ValueComboBox;
import com.mycollab.vaadin.web.ui.field.DateTimeOptionField;
import com.vaadin.server.Resource;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;

import static com.mycollab.vaadin.web.ui.utils.FormControlsGenerator.generateEditFormControls;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
@ViewComponent
public class MeetingAddViewImpl extends AbstractEditItemComp<MeetingWithBLOBs> implements MeetingAddView {
    private static final long serialVersionUID = 1L;

    @Override
    protected String initFormTitle() {
        return (beanItem.getId() == null) ? AppContext.getMessage(MeetingI18nEnum.NEW) : beanItem.getSubject();
    }

    @Override
    protected Resource initFormIconResource() {
        return CrmAssetsManager.getAsset(CrmTypeConstants.MEETING);
    }

    @Override
    protected ComponentContainer createButtonControls() {
        return generateEditFormControls(editForm);
    }

    @Override
    protected AdvancedEditBeanForm<MeetingWithBLOBs> initPreviewForm() {
        return new AdvancedEditBeanForm<>();
    }

    @Override
    protected IFormLayoutFactory initFormLayoutFactory() {
        return new DefaultDynaFormLayout(CrmTypeConstants.MEETING, MeetingDefaultFormLayoutFactory.getForm());
    }

    @Override
    protected AbstractBeanFieldGroupEditFieldFactory<MeetingWithBLOBs> initBeanFormFieldFactory() {
        return new MeetingEditFormFieldFactory(editForm);
    }

    private static class MeetingEditFormFieldFactory extends AbstractBeanFieldGroupEditFieldFactory<MeetingWithBLOBs> {
        private static final long serialVersionUID = 1L;

        MeetingEditFormFieldFactory(GenericBeanForm<MeetingWithBLOBs> form) {
            super(form);
        }

        @Override
        protected Field<?> onCreateField(Object propertyId) {
            if (propertyId.equals("subject")) {
                TextField tf = new TextField();
                if (isValidateForm) {
                    tf.setNullRepresentation("");
                    tf.setRequired(true);
                    tf.setRequiredError("Subject must not be null");
                }

                return tf;
            } else if (propertyId.equals("status")) {
                return new MeetingStatusComboBox();
            } else if (propertyId.equals("startdate")) {
                return new DateTimeOptionField();
            } else if (propertyId.equals("enddate")) {
                return new DateTimeOptionField();
            } else if (propertyId.equals("description")) {
                return new RichTextArea();
            } else if (propertyId.equals("type")) {
                return new RelatedEditItemField(attachForm.getBean());
            } else if (propertyId.equals("typeid")) {
                return new DummyCustomField<String>();
            } else if (propertyId.equals("isrecurrence")) {
                return null;
            }
            return null;
        }
    }

    @Override
    public HasEditFormHandlers<MeetingWithBLOBs> getEditFormHandlers() {
        return editForm;
    }

    private static class MeetingStatusComboBox extends ValueComboBox {

        MeetingStatusComboBox() {
            super();
            setCaption(null);
            this.loadData("Planned", "Held", "Not Held");
        }
    }
}
