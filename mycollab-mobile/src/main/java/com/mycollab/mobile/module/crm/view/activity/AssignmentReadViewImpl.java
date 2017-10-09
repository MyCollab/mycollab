package com.mycollab.mobile.module.crm.view.activity;

import com.mycollab.mobile.form.view.DynaFormLayout;
import com.mycollab.mobile.module.crm.ui.CrmPreviewFormControlsGenerator;
import com.mycollab.mobile.ui.AbstractPreviewItemComp;
import com.mycollab.mobile.ui.AdvancedPreviewBeanForm;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.SimpleCrmTask;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.vaadin.event.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.mycollab.vaadin.ui.IFormLayoutFactory;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComponentContainer;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
@ViewComponent
public class AssignmentReadViewImpl extends AbstractPreviewItemComp<SimpleCrmTask> implements AssignmentReadView {
    private static final long serialVersionUID = 5248815234910784599L;

    @Override
    public HasPreviewFormHandlers<SimpleCrmTask> getPreviewFormHandlers() {
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
    protected AdvancedPreviewBeanForm<SimpleCrmTask> initPreviewForm() {
        return new AdvancedPreviewBeanForm<>();
    }

    @Override
    protected IFormLayoutFactory initFormLayoutFactory() {
        return new DynaFormLayout(CrmTypeConstants.TASK, AssignmentDefaultFormLayoutFactory.getForm());
    }

    @Override
    protected AbstractBeanFieldGroupViewFieldFactory<SimpleCrmTask> initBeanFormFieldFactory() {
        return new AssignmentReadFormFieldFactory(previewForm);
    }

    @Override
    protected ComponentContainer createButtonControls() {
        return new CrmPreviewFormControlsGenerator<>(previewForm).createButtonControls(RolePermissionCollections.CRM_TASK);
    }

    @Override
    protected ComponentContainer createBottomPanel() {
        MHorizontalLayout toolbarLayout = new MHorizontalLayout();
        toolbarLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        return toolbarLayout;
    }

    @Override
    protected String getType() {
        return CrmTypeConstants.TASK;
    }
}
