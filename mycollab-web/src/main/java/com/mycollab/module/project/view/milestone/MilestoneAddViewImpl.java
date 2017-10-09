package com.mycollab.module.project.view.milestone;

import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.Milestone;
import com.mycollab.module.project.domain.SimpleMilestone;
import com.mycollab.module.project.i18n.MilestoneI18nEnum;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.ui.components.AbstractEditItemComp;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.mycollab.vaadin.ui.AdvancedEditBeanForm;
import com.mycollab.vaadin.ui.IFormLayoutFactory;
import com.mycollab.vaadin.web.ui.DefaultDynaFormLayout;
import com.mycollab.vaadin.web.ui.field.AttachmentUploadField;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.ComponentContainer;

import static com.mycollab.vaadin.web.ui.utils.FormControlsGenerator.generateEditFormControls;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class MilestoneAddViewImpl extends AbstractEditItemComp<SimpleMilestone> implements MilestoneAddView {
    private static final long serialVersionUID = 1L;

    @Override
    protected String initFormTitle() {
        return (beanItem.getId() == null) ? null : beanItem.getName();
    }

    @Override
    protected String initFormHeader() {
        return (beanItem.getId() == null) ? UserUIContext.getMessage(MilestoneI18nEnum.NEW) :
                UserUIContext.getMessage(MilestoneI18nEnum.DETAIL);
    }

    @Override
    protected FontAwesome initFormIconResource() {
        return ProjectAssetsManager.getAsset(ProjectTypeConstants.MILESTONE);
    }

    @Override
    protected ComponentContainer createButtonControls() {
        return generateEditFormControls(editForm);
    }

    @Override
    public AttachmentUploadField getAttachUploadField() {
        return ((MilestoneEditFormFieldFactory) editForm.getFieldFactory()).getAttachmentUploadField();
    }

    @Override
    protected AdvancedEditBeanForm<SimpleMilestone> initPreviewForm() {
        return new AdvancedEditBeanForm<>();
    }

    @Override
    protected IFormLayoutFactory initFormLayoutFactory() {
        return new DefaultDynaFormLayout(ProjectTypeConstants.MILESTONE, MilestoneDefaultFormLayoutFactory.getForm(), Milestone.Field.id.name());
    }

    @Override
    protected AbstractBeanFieldGroupEditFieldFactory<SimpleMilestone> initBeanFormFieldFactory() {
        return new MilestoneEditFormFieldFactory(editForm);
    }
}
