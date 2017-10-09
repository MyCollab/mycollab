package com.mycollab.module.project.view.settings;

import com.mycollab.module.project.domain.ProjectMember;
import com.mycollab.module.project.i18n.ProjectI18nEnum;
import com.mycollab.module.project.i18n.ProjectMemberI18nEnum;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.AbstractFormLayoutFactory;
import com.mycollab.vaadin.ui.FormContainer;
import com.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectMemberFormLayoutFactory extends AbstractFormLayoutFactory {
    private static final long serialVersionUID = 1L;
    private GridFormLayoutHelper informationLayout;

    @Override
    public AbstractComponent getLayout() {
        final FormContainer layout = new FormContainer();
        informationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(2, 3);
        layout.addSection(UserUIContext.getMessage(ProjectMemberI18nEnum.FORM_INFORMATION_SECTION), informationLayout.getLayout());
        return layout;
    }

    @Override
    protected Component onAttachField(Object propertyId, final Field<?> field) {
        if (propertyId.equals("memberFullName")) {
            return informationLayout.addComponent(field, UserUIContext.getMessage(ProjectMemberI18nEnum.FORM_USER), 0, 0, 2, "100%");
        } else if (propertyId.equals("projectroleid")) {
            return informationLayout.addComponent(field, UserUIContext.getMessage(ProjectMemberI18nEnum.FORM_ROLE), 0, 1, 2, "100%");
        } else if (ProjectMember.Field.billingrate.equalTo(propertyId)) {
            return informationLayout.addComponent(field, UserUIContext.getMessage(ProjectI18nEnum.FORM_BILLING_RATE), 0, 2);
        } else if (ProjectMember.Field.overtimebillingrate.equalTo(propertyId)) {
            return informationLayout.addComponent(field, UserUIContext.getMessage(ProjectI18nEnum.FORM_OVERTIME_BILLING_RATE), 1, 2);
        }
        return null;
    }
}
