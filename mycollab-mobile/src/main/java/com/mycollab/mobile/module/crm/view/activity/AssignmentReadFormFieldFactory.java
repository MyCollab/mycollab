package com.mycollab.mobile.module.crm.view.activity;

import com.mycollab.mobile.module.crm.ui.RelatedReadItemField;
import com.mycollab.module.crm.domain.SimpleCrmTask;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.mycollab.vaadin.ui.GenericBeanForm;
import com.mycollab.vaadin.ui.field.DateTimeViewField;
import com.mycollab.vaadin.ui.field.DefaultViewField;
import com.vaadin.ui.Field;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
class AssignmentReadFormFieldFactory extends AbstractBeanFieldGroupViewFieldFactory<SimpleCrmTask> {
    private static final long serialVersionUID = 1L;

    public AssignmentReadFormFieldFactory(GenericBeanForm<SimpleCrmTask> form) {
        super(form);
    }

    @Override
    protected Field<?> onCreateField(Object propertyId) {
        if (propertyId.equals("assignuser")) {
            return new DefaultViewField(attachForm.getBean().getAssignUserFullName());
        } else if (propertyId.equals("startdate")) {
            if (attachForm.getBean().getStartdate() == null)
                return null;
            return new DateTimeViewField(attachForm.getBean().getStartdate());
        } else if (propertyId.equals("duedate")) {
            if (attachForm.getBean().getDuedate() == null)
                return null;
            return new DateTimeViewField(attachForm.getBean().getDuedate());
        } else if (propertyId.equals("contactid")) {
            return new DefaultViewField(attachForm.getBean().getContactName());
        } else if (propertyId.equals("typeid")) {
            return new RelatedReadItemField(attachForm.getBean());

        }

        return null;
    }

}
