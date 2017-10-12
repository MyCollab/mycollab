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
package com.mycollab.module.crm.view.activity;

import com.mycollab.common.i18n.ErrorI18nEnum;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.CallWithBLOBs;
import com.mycollab.module.crm.i18n.CallI18nEnum;
import com.mycollab.module.crm.i18n.OptionI18nEnum.CallPurpose;
import com.mycollab.module.crm.i18n.OptionI18nEnum.CallStatus;
import com.mycollab.module.crm.i18n.OptionI18nEnum.CallType;
import com.mycollab.module.crm.ui.CrmAssetsManager;
import com.mycollab.module.crm.ui.components.AbstractEditItemComp;
import com.mycollab.module.crm.ui.components.RelatedEditItemField;
import com.mycollab.module.user.ui.components.ActiveUserComboBox;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.*;
import com.mycollab.vaadin.web.ui.DefaultDynaFormLayout;
import com.mycollab.vaadin.web.ui.I18nValueComboBox;
import com.mycollab.vaadin.web.ui.IntegerField;
import com.mycollab.vaadin.web.ui.ValueComboBox;
import com.mycollab.vaadin.web.ui.field.DateTimeOptionField;
import com.vaadin.data.Property;
import com.vaadin.server.Resource;
import com.vaadin.ui.*;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.Arrays;

import static com.mycollab.vaadin.web.ui.utils.FormControlsGenerator.generateEditFormControls;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
@ViewComponent
public class CallAddViewImpl extends AbstractEditItemComp<CallWithBLOBs> implements CallAddView {
    private static final long serialVersionUID = 1L;

    @Override
    protected String initFormTitle() {
        return (beanItem.getId() == null) ? UserUIContext.getMessage(CallI18nEnum.NEW) : beanItem.getSubject();
    }

    @Override
    protected Resource initFormIconResource() {
        return CrmAssetsManager.getAsset(CrmTypeConstants.CALL);
    }

    @Override
    protected ComponentContainer createButtonControls() {
        return generateEditFormControls(editForm);
    }

    @Override
    protected AdvancedEditBeanForm<CallWithBLOBs> initPreviewForm() {
        return new AdvancedEditBeanForm<>();
    }

    @Override
    protected IFormLayoutFactory initFormLayoutFactory() {
        return new DefaultDynaFormLayout(CrmTypeConstants.CALL, CallDefaultFormLayoutFactory.getForm());
    }

    @Override
    protected AbstractBeanFieldGroupEditFieldFactory<CallWithBLOBs> initBeanFormFieldFactory() {
        return new CallEditFormFieldFactory(editForm);
    }

    private class CallEditFormFieldFactory extends AbstractBeanFieldGroupEditFieldFactory<CallWithBLOBs> {
        private static final long serialVersionUID = 1L;

        private CallStatusTypeField callStatusField;

        CallEditFormFieldFactory(GenericBeanForm<CallWithBLOBs> form) {
            super(form);
            callStatusField = new CallStatusTypeField();
        }

        @Override
        protected Field<?> onCreateField(Object propertyId) {
            if (propertyId.equals("subject")) {
                MTextField tf = new MTextField();
                if (isValidateForm) {
                    tf.withNullRepresentation("").withRequired(true)
                            .withRequiredError(UserUIContext.getMessage(ErrorI18nEnum.FIELD_MUST_NOT_NULL,
                                    UserUIContext.getMessage(CallI18nEnum.FORM_SUBJECT)));
                }
                return tf;
            } else if (propertyId.equals("assignuser")) {
                return new ActiveUserComboBox();
            } else if (propertyId.equals("description")) {
                return new RichTextArea();
            } else if (propertyId.equals("result")) {
                return new RichTextArea();
            } else if (propertyId.equals("durationinseconds")) {
                CallDurationField callDurationField = new CallDurationField();
                callDurationField.setRequired(true);
                return callDurationField;
            } else if (propertyId.equals("purpose")) {
                return new CallPurposeComboBox();
            } else if (propertyId.equals("status") || propertyId.equals("calltype")) {
                return callStatusField;
            } else if (propertyId.equals("type")) {
                return new RelatedEditItemField(attachForm.getBean());
            } else if (propertyId.equals("typeid")) {
                return new DummyCustomField<String>();
            } else if (propertyId.equals("startdate")) {
                return new DateTimeOptionField();
            }
            return null;
        }

        private class CallStatusTypeField extends CompoundCustomField {
            private static final long serialVersionUID = 1L;

            @Override
            public Class<?> getType() {
                return Object.class;
            }

            @Override
            protected Component initContent() {
                CallTypeComboBox typeField = new CallTypeComboBox();
                typeField.select(beanItem.getCalltype());

                CallStatusComboBox statusField = new CallStatusComboBox();
                statusField.select(beanItem.getStatus());

                // binding field group
                fieldGroup.bind(typeField, "calltype");
                fieldGroup.bind(statusField, "status");
                return new MHorizontalLayout(typeField, statusField);
            }
        }
    }

    private static class CallPurposeComboBox extends I18nValueComboBox {
        private static final long serialVersionUID = 1L;

        CallPurposeComboBox() {
            setCaption(null);
            this.loadData(Arrays.asList(CallPurpose.Administrative, CallPurpose.Negotiation, CallPurpose.Project,
                    CallPurpose.Prospecting, CallPurpose.Support));
        }
    }

    private static class CallDurationField extends CustomField<Integer> {
        private static final long serialVersionUID = 1L;
        private IntegerField hourField;
        private ValueComboBox minutesField;

        CallDurationField() {
            hourField = new IntegerField();
            hourField.setWidth("50px");

            minutesField = new ValueComboBox();
            minutesField.loadData("0", "15", "30", "45");
            minutesField.setWidth("70px");
        }

        @Override
        public void commit() {
            Integer durationInSeconds = calculateDurationInSeconds();
            this.setInternalValue(durationInSeconds);
            super.commit();
        }

        private Integer calculateDurationInSeconds() {
            Integer hourValue = hourField.getValue();
            String minuteValue = (String) minutesField.getValue();
            int minutesVal;

            try {
                minutesVal = Integer.parseInt(minuteValue);
            } catch (NumberFormatException e) {
                minutesField.select(null);
                minutesVal = 0;
            }

            if (minutesVal != 0 || hourValue != 0) {
                return (minutesVal * 60 + hourValue * 3600);
            }

            return 0;
        }

        @Override
        public void setPropertyDataSource(Property newDataSource) {
            Object value = newDataSource.getValue();
            if (value instanceof Integer) {
                Integer duration = (Integer) value;
                int hours = duration / 3600;
                int minutes = (duration % 3600) / 60;
                hourField.setValue(hours);
                minutesField.select("" + minutes);
            }
            super.setPropertyDataSource(newDataSource);
        }

        @Override
        protected Component initContent() {
            return new MHorizontalLayout(hourField, minutesField, new Label("(hours/minutes)"));
        }

        @Override
        public Class<Integer> getType() {
            return Integer.class;
        }
    }

    private class CallTypeComboBox extends I18nValueComboBox {

        CallTypeComboBox() {
            setCaption(null);
            this.setWidth("80px");
            this.loadData(Arrays.asList(CallType.Inbound, CallType.Outbound));
            this.addValueChangeListener(valueChangeEvent -> beanItem.setCalltype((String) CallTypeComboBox.this.getValue()));
        }
    }

    private class CallStatusComboBox extends I18nValueComboBox {

        CallStatusComboBox() {
            setCaption(null);
            this.setWidth("100px");
            this.loadData(Arrays.asList(CallStatus.Planned, CallStatus.Held, CallStatus.Not_Held));
            this.addValueChangeListener(valueChangeEvent -> beanItem.setStatus((String) CallStatusComboBox.this.getValue()));
        }
    }
}
