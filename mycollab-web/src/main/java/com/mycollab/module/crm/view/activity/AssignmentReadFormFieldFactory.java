package com.mycollab.module.crm.view.activity;

import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.CrmLinkBuilder;
import com.mycollab.module.crm.domain.CrmTask;
import com.mycollab.module.crm.domain.SimpleCrmTask;
import com.mycollab.module.crm.i18n.OptionI18nEnum.TaskStatus;
import com.mycollab.module.crm.ui.CrmAssetsManager;
import com.mycollab.module.crm.ui.components.RelatedReadItemField;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.mycollab.vaadin.ui.GenericBeanForm;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.ui.field.DateTimeViewField;
import com.mycollab.vaadin.ui.field.I18nFormViewField;
import com.mycollab.vaadin.ui.field.RichTextViewField;
import com.mycollab.vaadin.web.ui.field.LinkViewField;
import com.mycollab.vaadin.web.ui.field.UserLinkViewField;
import com.vaadin.ui.Field;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
class AssignmentReadFormFieldFactory extends AbstractBeanFieldGroupViewFieldFactory<SimpleCrmTask> {
    private static final long serialVersionUID = 1L;

    public AssignmentReadFormFieldFactory(GenericBeanForm<SimpleCrmTask> form) {
        super(form);
    }

    @Override
    protected Field<?> onCreateField(Object propertyId) {
        SimpleCrmTask task = attachForm.getBean();

        if (propertyId.equals("assignuser")) {
            return new UserLinkViewField(task.getAssignuser(), task.getAssignUserAvatarId(), task.getAssignUserFullName());
        } else if (propertyId.equals("startdate")) {
            return new DateTimeViewField(task.getStartdate());
        } else if (propertyId.equals("duedate")) {
            return new DateTimeViewField(task.getDuedate());
        } else if (propertyId.equals("contactid")) {
            return new LinkViewField(task.getContactName(), CrmLinkBuilder.generateContactPreviewLinkFull(task.getContactid()),
                    CrmAssetsManager.getAsset(CrmTypeConstants.CONTACT));
        } else if (propertyId.equals("type")) {
            return new RelatedReadItemField(task);

        } else if (propertyId.equals("description")) {
            return new RichTextViewField(task.getDescription());
        } else if (CrmTask.Field.status.equalTo(propertyId)) {
            return new I18nFormViewField(task.getStatus(), TaskStatus.class).withStyleName(UIConstants.FIELD_NOTE);
        }

        return null;
    }

}
