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
package com.esofthead.mycollab.module.crm.view.activity;

import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.CallWithBLOBs;
import com.esofthead.mycollab.module.crm.ui.CrmAssetsManager;
import com.esofthead.mycollab.module.crm.ui.components.AbstractEditItemComp;
import com.esofthead.mycollab.module.crm.ui.components.DynaFormLayout;
import com.esofthead.mycollab.module.crm.ui.components.RelatedEditItemField;
import com.esofthead.mycollab.module.user.ui.components.ActiveUserComboBox;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.*;
import com.esofthead.mycollab.vaadin.ui.form.field.RichTextEditField;
import com.vaadin.data.Property;
import com.vaadin.server.Resource;
import com.vaadin.ui.*;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
@ViewComponent
public class CallAddViewImpl extends AbstractEditItemComp<CallWithBLOBs>
        implements CallAddView {
    private static final long serialVersionUID = 1L;

    @Override
    protected String initFormTitle() {
        return (beanItem.getId() == null) ? "New Call" : beanItem.getSubject();
    }

    @Override
    protected Resource initFormIconResource() {
        return CrmAssetsManager.getAsset(CrmTypeConstants.CALL);
    }

    @Override
    protected ComponentContainer createButtonControls() {
        return new EditFormControlsGenerator<>(editForm)
                .createButtonControls();
    }

    @Override
    protected AdvancedEditBeanForm<CallWithBLOBs> initPreviewForm() {
        return new AdvancedEditBeanForm<>();
    }

    @Override
    protected IFormLayoutFactory initFormLayoutFactory() {
        return new DynaFormLayout(CrmTypeConstants.CALL,
                CallDefaultFormLayoutFactory.getForm());
    }

    @Override
    protected AbstractBeanFieldGroupEditFieldFactory<CallWithBLOBs> initBeanFormFieldFactory() {
        return new CallEditFormFieldFactory(editForm);
    }

    private class CallEditFormFieldFactory extends
            AbstractBeanFieldGroupEditFieldFactory<CallWithBLOBs> {
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
                return new RichTextEditField();
            } else if (propertyId.equals("result")) {
                return new RichTextEditField();
            } else if (propertyId.equals("durationinseconds")) {
                return new CallDurationField();
            } else if (propertyId.equals("purpose")) {
                return new CallPurposeComboBox();
            } else if (propertyId.equals("status")
                    || propertyId.equals("calltype")) {
                return callStatusField;
            } else if (propertyId.equals("type")) {
                return new RelatedEditItemField(attachForm.getBean());
            } else if (propertyId.equals("typeid")) {
                return new DummyCustomField<String>();
            } else if (propertyId.equals("startdate")) {
                return new DateTimePickerField();
            }
            return null;
        }

        @SuppressWarnings("rawtypes")
        private class CallStatusTypeField extends CompoundCustomField {
            private static final long serialVersionUID = 1L;

            @Override
            public Class<?> getType() {
                return Object.class;
            }

            @Override
            protected Component initContent() {
                HorizontalLayout layout = new HorizontalLayout();
                layout.setSpacing(true);

                CallTypeComboBox typeField = new CallTypeComboBox();
                layout.addComponent(typeField);
                typeField.select(beanItem.getCalltype());

                CallStatusComboBox statusField = new CallStatusComboBox();
                layout.addComponent(statusField);
                statusField.select(beanItem.getStatus());

                // binding field group
                fieldGroup.bind(typeField, "calltype");
                fieldGroup.bind(statusField, "status");
                return layout;
            }
        }
    }

    private class CallPurposeComboBox extends ValueComboBox {

        private static final long serialVersionUID = 1L;

        public CallPurposeComboBox() {
            super();
            setCaption(null);
            this.loadData("Prospecting", "Administrative",
                    "Negotiation", "Project", "Support");
        }
    }

    private class CallDurationField extends CustomField<Integer> {

        private static final long serialVersionUID = 1L;
        private TextField hourField;
        private ValueComboBox minutesField;

        public CallDurationField() {
            hourField = new TextField();
            hourField.setWidth("30px");

            minutesField = new ValueComboBox();
            minutesField.loadData("0", "15", "30", "45");
            minutesField.setWidth("40px");
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
            int hourVal, minutesVal;
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
                return (minutesVal * 60 + hourVal * 3600);
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
                hourField.setValue("" + hours);
                minutesField.select("" + minutes);
            }
            super.setPropertyDataSource(newDataSource);
        }

        @Override
        protected Component initContent() {
            HorizontalLayout layout = new HorizontalLayout();
            layout.setSpacing(true);

            layout.addComponent(hourField);

            layout.addComponent(minutesField);

            layout.addComponent(new Label("(hours/minutes)"));

            return layout;
        }

        @Override
        public Class<Integer> getType() {
            return Integer.class;
        }
    }

    private class CallTypeComboBox extends ValueComboBox {

        private static final long serialVersionUID = 1L;

        public CallTypeComboBox() {
            super();
            setCaption(null);
            this.setWidth("80px");
            this.loadData("Inbound", "Outbound");
            this.addValueChangeListener(new Property.ValueChangeListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void valueChange(
                        com.vaadin.data.Property.ValueChangeEvent event) {
                    beanItem.setCalltype((String) CallTypeComboBox.this
                            .getValue());
                }
            });
        }
    }

    private class CallStatusComboBox extends ValueComboBox {

        private static final long serialVersionUID = 1L;

        public CallStatusComboBox() {
            super();
            setCaption(null);
            this.setWidth("100px");
            this.loadData("Planned", "Held", "Not Held");
            this.addValueChangeListener(new Property.ValueChangeListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void valueChange(
                        com.vaadin.data.Property.ValueChangeEvent event) {
                    beanItem.setStatus((String) CallStatusComboBox.this
                            .getValue());
                }
            });
        }
    }
}
