package com.mycollab.module.crm.view.cases;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.CaseWithBLOBs;
import com.mycollab.module.crm.i18n.CaseI18nEnum;
import com.mycollab.module.crm.ui.CrmAssetsManager;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.mycollab.vaadin.ui.AbstractFormLayoutFactory;
import com.mycollab.vaadin.ui.FormContainer;
import com.mycollab.vaadin.web.ui.MassUpdateWindow;
import com.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class MassUpdateCaseWindow extends MassUpdateWindow<CaseWithBLOBs> {
    private static final long serialVersionUID = 1L;

    public MassUpdateCaseWindow(String title, CaseListPresenter presenter) {
        super(title, CrmAssetsManager.getAsset(CrmTypeConstants.CASE), new CaseWithBLOBs(), presenter);
    }

    @Override
    protected AbstractFormLayoutFactory buildFormLayoutFactory() {
        return new MassUpdateContactFormLayoutFactory();
    }

    @Override
    protected AbstractBeanFieldGroupEditFieldFactory<CaseWithBLOBs> buildBeanFormFieldFactory() {
        return new CaseEditFormFieldFactory<>(updateForm, false);
    }

    private class MassUpdateContactFormLayoutFactory extends AbstractFormLayoutFactory {
        private static final long serialVersionUID = 1L;

        private GridFormLayoutHelper informationLayout;

        @Override
        public AbstractComponent getLayout() {
            FormContainer formLayout = new FormContainer();
            informationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(2, 6);
            formLayout.addSection(UserUIContext.getMessage(CaseI18nEnum.SECTION_CASE_INFORMATION), informationLayout.getLayout());
            formLayout.addComponent(buildButtonControls());
            return formLayout;
        }

        // priority, status, account name, origin, type, reason, assignuser
        @Override
        protected Component onAttachField(Object propertyId, final Field<?> field) {
            if (propertyId.equals("priority")) {
                return informationLayout.addComponent(field, "Priority", 0, 0);
            } else if (propertyId.equals("status")) {
                return informationLayout.addComponent(field, "Status", 1, 0);
            } else if (propertyId.equals("accountid")) {
                return informationLayout.addComponent(field, "Account Name", 0, 1);
            } else if (propertyId.equals("origin")) {
                return informationLayout.addComponent(field, "Origin", 1, 1);
            } else if (propertyId.equals("type")) {
                return informationLayout.addComponent(field, "Type", 0, 2);
            } else if (propertyId.equals("reason")) {
                return informationLayout.addComponent(field, "Reason", 1, 2);
            } else if (propertyId.equals("assignuser")) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(GenericI18Enum.FORM_ASSIGNEE), 0, 3, 2, "297px");
            }
            return null;
        }
    }
}