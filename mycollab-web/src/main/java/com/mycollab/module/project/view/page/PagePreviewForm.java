package com.mycollab.module.project.view.page;

import com.mycollab.module.page.domain.Page;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.mycollab.vaadin.ui.GenericBeanForm;
import com.mycollab.vaadin.ui.AbstractFormLayoutFactory;
import com.mycollab.vaadin.web.ui.AdvancedPreviewBeanForm;
import com.mycollab.vaadin.ui.field.RichTextViewField;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.2.10
 */
public class PagePreviewForm extends AdvancedPreviewBeanForm<Page> {
    @Override
    public void setBean(Page bean) {
        this.setFormLayoutFactory(new PageReadFormLayout());
        this.setBeanFormFieldFactory(new PageReadFormFieldFactory(this));
        super.setBean(bean);
    }

    private static class PageReadFormLayout extends AbstractFormLayoutFactory {

        private MVerticalLayout layout;

        @Override
        public AbstractComponent getLayout() {
            layout = new MVerticalLayout().withStyleName("border-bottom").withFullWidth();
            return layout;
        }

        @Override
        protected Component onAttachField(java.lang.Object propertyId, Field<?> field) {
            if (propertyId.equals("content")) {
                layout.addComponent(field);
                return field;
            }
            return null;
        }
    }

    private static class PageReadFormFieldFactory extends AbstractBeanFieldGroupViewFieldFactory<Page> {
        private static final long serialVersionUID = 1L;

        PageReadFormFieldFactory(GenericBeanForm<Page> form) {
            super(form);
        }

        @Override
        protected Field<?> onCreateField(java.lang.Object propertyId) {
            if (propertyId.equals("content")) {
                return new RichTextViewField(attachForm.getBean().getContent());
            }
            return null;
        }
    }
}
