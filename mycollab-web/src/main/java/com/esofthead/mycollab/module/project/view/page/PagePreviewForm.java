/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.project.view.page;

import com.esofthead.mycollab.common.i18n.WikiI18nEnum;
import com.esofthead.mycollab.module.page.domain.Page;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.web.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.vaadin.web.ui.field.I18nFormViewField;
import com.esofthead.mycollab.vaadin.web.ui.field.RichTextViewField;
import com.vaadin.ui.ComponentContainer;
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

    private static class PageReadFormLayout implements IFormLayoutFactory {
        private static final long serialVersionUID = 1L;

        private MVerticalLayout layout;

        @Override
        public ComponentContainer getLayout() {
            layout = new MVerticalLayout().withStyleName("border-bottom").withWidth("100%");
            return layout;
        }

        @Override
        public void attachField(java.lang.Object propertyId, Field<?> field) {
            if (propertyId.equals("content")) {
                layout.addComponent(field);
            }
        }
    }

    private static class PageReadFormFieldFactory extends AbstractBeanFieldGroupViewFieldFactory<Page> {
        private static final long serialVersionUID = 1L;

        public PageReadFormFieldFactory(GenericBeanForm<Page> form) {
            super(form);
        }

        @Override
        protected Field<?> onCreateField(java.lang.Object propertyId) {
            if (propertyId.equals("status")) {
                return new I18nFormViewField(attachForm.getBean().getStatus(), WikiI18nEnum.class);
            } else if (propertyId.equals("content")) {
                return new RichTextViewField(attachForm.getBean().getContent());
            }
            return null;
        }
    }
}
