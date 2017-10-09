package com.mycollab.module.project.view.settings;

import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.i18n.ComponentI18nEnum;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.ui.components.AbstractEditItemComp;
import com.mycollab.module.project.view.settings.component.ComponentEditFormFieldFactory;
import com.mycollab.module.tracker.domain.Component;
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
public class ComponentAddViewImpl extends AbstractEditItemComp<Component> implements ComponentAddView {
    private static final long serialVersionUID = 1L;

    @Override
    protected String initFormHeader() {
        return (beanItem.getId() == null) ? UserUIContext.getMessage(ComponentI18nEnum.NEW) : UserUIContext.getMessage(ComponentI18nEnum.DETAIL);
    }

    @Override
    protected String initFormTitle() {
        return (beanItem.getId() == null) ? null : beanItem.getName();
    }

    @Override
    protected FontAwesome initFormIconResource() {
        return ProjectAssetsManager.getAsset(ProjectTypeConstants.BUG_COMPONENT);
    }

    @Override
    protected ComponentContainer createButtonControls() {
        return generateEditFormControls(editForm);
    }

    @Override
    protected AdvancedEditBeanForm<Component> initPreviewForm() {
        return new AdvancedEditBeanForm<>();
    }

    @Override
    protected IFormLayoutFactory initFormLayoutFactory() {
        return new DefaultDynaFormLayout(ProjectTypeConstants.BUG_COMPONENT, ComponentDefaultFormLayoutFactory.getForm(), "id");
    }

    @Override
    protected AbstractBeanFieldGroupEditFieldFactory<Component> initBeanFormFieldFactory() {
        return new ComponentEditFormFieldFactory(editForm);
    }

    @Override
    public HasEditFormHandlers<Component> getEditFormHandlers() {
        return this.editForm;
    }

}
