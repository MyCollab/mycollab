package com.mycollab.module.project.view.settings.component;

import com.mycollab.common.i18n.ErrorI18nEnum;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.project.i18n.ComponentI18nEnum;
import com.mycollab.module.tracker.domain.Component;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.mycollab.vaadin.ui.GenericBeanForm;
import com.vaadin.ui.Field;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;

/**
 * @author MyCollab Ltd
 * @since 5.3.0
 */
public class ComponentEditFormFieldFactory extends AbstractBeanFieldGroupEditFieldFactory<Component> {
    private static final long serialVersionUID = 1L;

    public ComponentEditFormFieldFactory(GenericBeanForm<Component> form) {
        super(form);
    }

    @Override
    protected Field<?> onCreateField(final Object propertyId) {
        if (Component.Field.name.equalTo(propertyId)) {
            final TextField tf = new TextField();
            if (isValidateForm) {
                tf.setNullRepresentation("");
                tf.setRequired(true);
                tf.setRequiredError(UserUIContext.getMessage(ErrorI18nEnum.FIELD_MUST_NOT_NULL,
                        UserUIContext.getMessage(GenericI18Enum.FORM_NAME)));
            }
            return tf;
        } else if (Component.Field.description.equalTo(propertyId)) {
            return new RichTextArea();
        } else if (Component.Field.userlead.equalTo(propertyId)) {
            return new ProjectMemberSelectionField();
        }

        return null;
    }
}
