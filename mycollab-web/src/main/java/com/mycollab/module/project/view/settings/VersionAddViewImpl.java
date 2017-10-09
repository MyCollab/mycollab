package com.mycollab.module.project.view.settings;

import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.i18n.VersionI18nEnum;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.ui.components.AbstractEditItemComp;
import com.mycollab.module.project.view.settings.component.VersionEditFormFieldFactory;
import com.mycollab.module.tracker.domain.Version;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.event.HasEditFormHandlers;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.mycollab.vaadin.ui.AdvancedEditBeanForm;
import com.mycollab.vaadin.ui.IFormLayoutFactory;
import com.mycollab.vaadin.web.ui.DefaultDynaFormLayout;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.ComponentContainer;

import static com.mycollab.vaadin.web.ui.utils.FormControlsGenerator.generateEditFormControls;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class VersionAddViewImpl extends AbstractEditItemComp<Version> implements VersionAddView {
    private static final long serialVersionUID = 1L;

    @Override
    public HasEditFormHandlers<Version> getEditFormHandlers() {
        return this.editForm;
    }

    @Override
    protected String initFormTitle() {
        return (beanItem.getId() == null) ? null : beanItem.getName();
    }

    @Override
    protected String initFormHeader() {
        return (beanItem.getId() == null) ? UserUIContext.getMessage(VersionI18nEnum.NEW) : UserUIContext.getMessage(VersionI18nEnum.DETAIL);
    }

    @Override
    protected FontAwesome initFormIconResource() {
        return ProjectAssetsManager.getAsset(ProjectTypeConstants.BUG_VERSION);
    }

    @Override
    protected ComponentContainer createButtonControls() {
        return generateEditFormControls(editForm);
    }

    @Override
    protected AdvancedEditBeanForm<Version> initPreviewForm() {
        return new AdvancedEditBeanForm<>();
    }

    @Override
    protected IFormLayoutFactory initFormLayoutFactory() {
        return new DefaultDynaFormLayout(ProjectTypeConstants.BUG_VERSION, VersionDefaultFormLayoutFactory.getForm(), "id");
    }

    @Override
    protected AbstractBeanFieldGroupEditFieldFactory<Version> initBeanFormFieldFactory() {
        return new VersionEditFormFieldFactory(editForm);
    }
}
