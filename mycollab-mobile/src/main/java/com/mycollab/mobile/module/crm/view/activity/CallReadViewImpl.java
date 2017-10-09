package com.mycollab.mobile.module.crm.view.activity;

import com.mycollab.mobile.form.view.DynaFormLayout;
import com.mycollab.mobile.module.crm.ui.CrmPreviewFormControlsGenerator;
import com.mycollab.mobile.ui.AbstractPreviewItemComp;
import com.mycollab.mobile.ui.AdvancedPreviewBeanForm;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.SimpleCall;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.vaadin.event.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.mycollab.vaadin.ui.IFormLayoutFactory;
import com.vaadin.ui.ComponentContainer;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
@ViewComponent
public class CallReadViewImpl extends AbstractPreviewItemComp<SimpleCall> implements CallReadView {
    private static final long serialVersionUID = 3089173379174861809L;

    @Override
    public HasPreviewFormHandlers<SimpleCall> getPreviewFormHandlers() {
        return this.previewForm;
    }


    @Override
    protected void afterPreviewItem() {
    }

    @Override
    protected String initFormHeader() {
        return beanItem.getSubject();
    }

    @Override
    protected AdvancedPreviewBeanForm<SimpleCall> initPreviewForm() {
        return new AdvancedPreviewBeanForm<>();
    }

    @Override
    protected IFormLayoutFactory initFormLayoutFactory() {
        return new DynaFormLayout(CrmTypeConstants.CALL, CallDefaultFormLayoutFactory.getForm());
    }

    @Override
    protected AbstractBeanFieldGroupViewFieldFactory<SimpleCall> initBeanFormFieldFactory() {
        return new CallReadFormFieldFactory(previewForm);
    }

    @Override
    protected ComponentContainer createButtonControls() {
        return new CrmPreviewFormControlsGenerator<>(this.previewForm).createButtonControls(RolePermissionCollections.CRM_CALL);
    }

    @Override
    protected ComponentContainer createBottomPanel() {
        return null;
    }

    @Override
    protected String getType() {
        return CrmTypeConstants.CALL;
    }
}
