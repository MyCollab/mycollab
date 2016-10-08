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
package com.mycollab.mobile.module.crm.view.activity;

import com.mycollab.mobile.form.view.DynaFormLayout;
import com.mycollab.mobile.module.user.ui.components.ActiveUserComboBox;
import com.mycollab.mobile.ui.AbstractEditItemComp;
import com.mycollab.mobile.ui.ValueListSelect;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.CallWithBLOBs;
import com.mycollab.module.crm.i18n.CallI18nEnum;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.*;
import com.vaadin.addon.touchkit.ui.DatePicker;
import com.vaadin.data.Property;
import com.vaadin.ui.*;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
@ViewComponent
public class CallAddViewImpl extends AbstractEditItemComp<CallWithBLOBs> implements CallAddView {
    private static final long serialVersionUID = -7038760697823160315L;

    @Override
    protected String initFormTitle() {
        return beanItem.getSubject() != null ? beanItem.getSubject() : UserUIContext.getMessage(CallI18nEnum.NEW);
    }

    @Override
    protected IFormLayoutFactory initFormLayoutFactory() {
        return new DynaFormLayout(CrmTypeConstants.CALL, CallDefaultFormLayoutFactory.getForm());
    }

    @Override
    protected AbstractBeanFieldGroupEditFieldFactory<CallWithBLOBs> initBeanFormFieldFactory() {
        return new CallEditFormFieldFactory(this.editForm);
    }

    private class CallEditFormFieldFactory extends AbstractBeanFieldGroupEditFieldFactory<CallWithBLOBs> {
        private static final long serialVersionUID = 1L;

        private CallStatusTypeField callStatusField;

        public CallEditFormFieldFactory(GenericBeanForm<CallWithBLOBs> form) {
            super(form);
            callStatusField = new CallStatusTypeField();
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
            } else if (propertyId.equals("assignuser")) {
                return new ActiveUserComboBox();
            } else if (propertyId.equals("description")) {
                TextArea descArea = new TextArea();
                descArea.setNullRepresentation("");
                return descArea;
            } else if (propertyId.equals("result")) {
                TextArea resultArea = new TextArea();
                resultArea.setNullRepresentation("");
                return resultArea;
            } else if (propertyId.equals("durationinseconds")) {
                return new CallDurationField();
            } else if (propertyId.equals("purpose")) {
                return new CallPurposeListSelect();
            } else if (propertyId.equals("status")
                    || propertyId.equals("calltype")) {
                return callStatusField;
            } else if (propertyId.equals("typeid")) {
                return new RelatedItemSelectionField(attachForm.getBean());
            } else if (propertyId.equals("type")) {
                return new DummyCustomField<String>();
            } else if (propertyId.equals("startdate")) {
                return new DatePicker();
            }
            return null;
        }

        @SuppressWarnings({"rawtypes"})
        private class CallStatusTypeField extends CompoundCustomField {
            private static final long serialVersionUID = 1L;

            @Override
            public Class<?> getType() {
                return Object.class;
            }

            @Override
            protected Component initContent() {
                VerticalLayout layout = new VerticalLayout();
                layout.setSpacing(true);
                layout.setWidth("100%");

                CallTypeListSelect typeField = new CallTypeListSelect();
                typeField.setWidth("100%");
                layout.addComponent(typeField);
                typeField.select(beanItem.getCalltype());

                CallStatusListSelect statusField = new CallStatusListSelect();
                statusField.setWidth("100%");
                layout.addComponent(statusField);
                statusField.select(beanItem.getStatus());

                // binding field group
                fieldGroup.bind(typeField, "calltype");
                fieldGroup.bind(statusField, "status");
                return layout;
            }
        }
    }

    private static class CallPurposeListSelect extends ValueListSelect {
        private static final long serialVersionUID = 1L;

        public CallPurposeListSelect() {
            super();
            setCaption(null);
            this.loadData("Prospecting", "Administrative", "Negotiation", "Project", "Support");
        }
    }

    private static class CallDurationField extends CustomField<Integer> {

        private static final long serialVersionUID = 1L;
        private TextField hourField;
        private ValueListSelect minutesField;

        public CallDurationField() {
            hourField = new TextField();
            hourField.setWidth("30px");

            minutesField = new ValueListSelect();
            minutesField.setWidth("100%");
            minutesField.loadData("0", "15", "30", "45");
        }

        @Override
        public void commit() {
            Integer durationInSeconds = calculateDurationInSeconds();
            this.setInternalValue(durationInSeconds);
            super.commit();
        }

        private Integer calculateDurationInSeconds() {
            String hourValue = hourField.getValue();
            String minuteValue = (String) minutesField.getValue();
            int hourVal = 0, minutesVal = 0;
            try {
                hourVal = Integer.parseInt(hourValue);
            } catch (NumberFormatException e) {
                hourField.setValue("");
                hourVal = 0;
            }

            try {
                minutesVal = Integer.parseInt(minuteValue);
            } catch (NumberFormatException e) {
                minutesField.select(null);
                minutesVal = 0;
            }

            if (hourVal != 0 || minutesVal != 0) {
                return minutesVal * 60 + hourVal * 3600;
            }

            return 0;
        }

        @SuppressWarnings("rawtypes")
        @Override
        public void setPropertyDataSource(Property newDataSource) {
            Object value = newDataSource.getValue();
            if (value instanceof Integer) {
                Integer duration = (Integer) value;
                if (duration != 0) {
                    int hours = duration / 3600;
                    int minutes = duration % 3600 / 60;
                    hourField.setValue("" + hours);
                    minutesField.select("" + minutes);
                }
            }
            super.setPropertyDataSource(newDataSource);
        }

        @Override
        protected Component initContent() {
            VerticalLayout layout = new VerticalLayout();
            layout.setSpacing(true);
            layout.setWidth("100%");

            HorizontalLayout fieldLayout = new HorizontalLayout();
            fieldLayout.setWidth("100%");
            fieldLayout.setSpacing(true);

            fieldLayout.addComponent(hourField);

            fieldLayout.addComponent(minutesField);
            fieldLayout.setExpandRatio(minutesField, 1.0f);
            layout.addComponent(fieldLayout);

            layout.addComponent(new Label("(hours/minutes)"));

            return layout;
        }

        @Override
        public Class<Integer> getType() {
            return Integer.class;
        }
    }

    private class CallTypeListSelect extends ValueListSelect {

        private static final long serialVersionUID = 1L;

        public CallTypeListSelect() {
            super();
            setCaption(null);
            this.setWidth("80px");
            this.loadData("Inbound", "Outbound");
            this.addValueChangeListener(new Property.ValueChangeListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void valueChange(
                        com.vaadin.data.Property.ValueChangeEvent event) {
                    beanItem.setCalltype((String) CallTypeListSelect.this.getValue());
                }
            });
        }
    }

    private class CallStatusListSelect extends ValueListSelect {
        private static final long serialVersionUID = 1L;

        public CallStatusListSelect() {
            super();
            setCaption(null);
            this.setWidth("100px");
            this.loadData("Planned", "Held", "Not Held");
            this.addValueChangeListener(new Property.ValueChangeListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void valueChange(
                        com.vaadin.data.Property.ValueChangeEvent event) {
                    beanItem.setStatus((String) CallStatusListSelect.this.getValue());
                }
            });
        }
    }

}
