package com.mycollab.module.crm.ui.components;

import com.mycollab.vaadin.event.HasEditFormHandlers;
import com.mycollab.vaadin.mvp.AbstractVerticalPageView;
import com.mycollab.vaadin.mvp.IFormAddView;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.mycollab.vaadin.ui.AdvancedEditBeanForm;
import com.mycollab.vaadin.ui.IFormLayoutFactory;
import com.mycollab.vaadin.ui.WrappedFormLayoutFactory;
import com.mycollab.vaadin.web.ui.AddViewLayout2;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.ComponentContainer;

/**
 * @param <B>
 * @author MyCollab Ltd.
 * @since 3.0
 */
public abstract class AbstractEditItemComp<B> extends AbstractVerticalPageView implements IFormAddView<B> {
    private static final long serialVersionUID = 1L;

    protected B beanItem;
    protected AdvancedEditBeanForm<B> editForm;

    public AbstractEditItemComp() {
        setMargin(new MarginInfo(false, true, true, true));
        editForm = new AdvancedEditBeanForm<>();
        addComponent(editForm);
    }

    @Override
    public void editItem(final B item) {
        this.beanItem = item;
        this.editForm.setFormLayoutFactory(new FormLayoutFactory());
        this.editForm.setBeanFormFieldFactory(initBeanFormFieldFactory());
        this.editForm.setBean(item);
    }

    @Override
    public HasEditFormHandlers<B> getEditFormHandlers() {
        return editForm;
    }

    class FormLayoutFactory extends WrappedFormLayoutFactory {

        @Override
        public AbstractComponent getLayout() {
            AddViewLayout2 formAddLayout = new AddViewLayout2(initFormTitle(), initFormIconResource());

            ComponentContainer buttonControls = createButtonControls();
            if (buttonControls != null) {
                formAddLayout.addHeaderRight(buttonControls);
            }

            wrappedLayoutFactory = initFormLayoutFactory();
            formAddLayout.addBody(wrappedLayoutFactory.getLayout());
            return formAddLayout;
        }
    }

    abstract protected String initFormTitle();

    abstract protected Resource initFormIconResource();

    abstract protected ComponentContainer createButtonControls();

    abstract protected AdvancedEditBeanForm<B> initPreviewForm();

    abstract protected IFormLayoutFactory initFormLayoutFactory();

    abstract protected AbstractBeanFieldGroupEditFieldFactory<B> initBeanFormFieldFactory();
}
