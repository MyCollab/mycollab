package com.mycollab.mobile.module.crm.view.activity;

import com.mycollab.mobile.module.crm.ui.RelatedReadItemField;
import com.mycollab.module.crm.domain.SimpleCall;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.mycollab.vaadin.ui.GenericBeanForm;
import com.mycollab.vaadin.ui.field.DateTimeViewField;
import com.mycollab.vaadin.ui.field.DefaultViewField;
import com.vaadin.ui.Field;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
class CallReadFormFieldFactory extends AbstractBeanFieldGroupViewFieldFactory<SimpleCall> {
    private static final long serialVersionUID = 1L;

    public CallReadFormFieldFactory(GenericBeanForm<SimpleCall> form) {
        super(form);
    }

    @Override
    protected Field<?> onCreateField(Object propertyId) {
        if (propertyId.equals("assignuser")) {
            return new DefaultViewField(attachForm.getBean().getAssignUserFullName());
        } else if (propertyId.equals("typeid")) {
            return new RelatedReadItemField(attachForm.getBean());
        } else if (propertyId.equals("status")) {
            String value = "";
            value += attachForm.getBean().getStatus() != null ? attachForm.getBean().getStatus() + " " : "";
            value += attachForm.getBean().getCalltype() != null ? attachForm.getBean().getCalltype() : "";
            return new DefaultViewField(value);
        } else if (propertyId.equals("durationinseconds")) {
            final Integer duration = attachForm.getBean().getDurationinseconds();
            if (duration != null && duration != 0) {
                final int hours = duration / 3600;
                final int minutes = (duration % 3600) / 60;
                final StringBuffer value = new StringBuffer();
                if (hours == 1) {
                    value.append("1 hour ");
                } else if (hours >= 2) {
                    value.append(hours + " hours ");
                }

                if (minutes > 0) {
                    value.append(minutes + " minutes");
                }

                return new DefaultViewField(value.toString());
            } else {
                return new DefaultViewField("");
            }
        } else if (propertyId.equals("startdate")) {
            if (attachForm.getBean().getStartdate() == null) {
                return new DefaultViewField("");
            } else {
                return new DateTimeViewField(attachForm.getBean().getStartdate());
            }
        }

        return null;
    }

}
