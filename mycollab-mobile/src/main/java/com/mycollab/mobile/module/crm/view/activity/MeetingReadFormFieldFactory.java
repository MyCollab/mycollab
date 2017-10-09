package com.mycollab.mobile.module.crm.view.activity;

import com.mycollab.mobile.module.crm.ui.RelatedReadItemField;
import com.mycollab.module.crm.domain.SimpleMeeting;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.mycollab.vaadin.ui.GenericBeanForm;
import com.mycollab.vaadin.ui.field.DateTimeViewField;
import com.vaadin.ui.Field;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
class MeetingReadFormFieldFactory extends AbstractBeanFieldGroupViewFieldFactory<SimpleMeeting> {
    private static final long serialVersionUID = 1L;

    public MeetingReadFormFieldFactory(GenericBeanForm<SimpleMeeting> form) {
        super(form);
    }

    @Override
    protected Field<?> onCreateField(Object propertyId) {
        if (propertyId.equals("typeid")) {
            return new RelatedReadItemField(attachForm.getBean());
        } else if (propertyId.equals("startdate")) {
            if (attachForm.getBean().getStartdate() == null)
                return null;
            return new DateTimeViewField(attachForm.getBean().getStartdate());
        } else if (propertyId.equals("enddate")) {
            if (attachForm.getBean().getEnddate() == null)
                return null;
            return new DateTimeViewField(attachForm.getBean().getEnddate());
        } else if (propertyId.equals("isrecurrence")) {
            return null;
        }
        return null;
    }

}
