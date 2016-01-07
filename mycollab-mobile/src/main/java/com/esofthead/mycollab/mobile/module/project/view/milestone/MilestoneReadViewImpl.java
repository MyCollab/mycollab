/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.module.project.view.milestone;

import com.esofthead.mycollab.mobile.module.project.ui.CommentNavigationButton;
import com.esofthead.mycollab.mobile.module.project.ui.ProjectPreviewFormControlsGenerator;
import com.esofthead.mycollab.mobile.ui.AbstractPreviewItemComp;
import com.esofthead.mycollab.mobile.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.mobile.ui.Section;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleMilestone;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.MilestoneStatus;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.form.field.DateViewField;
import com.esofthead.mycollab.vaadin.ui.form.field.DefaultViewField;
import com.esofthead.mycollab.vaadin.ui.form.field.RichTextViewField;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */

@ViewComponent
public class MilestoneReadViewImpl extends AbstractPreviewItemComp<SimpleMilestone> implements MilestoneReadView {
    private static final long serialVersionUID = -2466318105833801922L;

    private CommentNavigationButton relatedComments;

    @Override
    public HasPreviewFormHandlers<SimpleMilestone> getPreviewFormHandlers() {
        return this.previewForm;
    }

    @Override
    protected void afterPreviewItem() {
        relatedComments.displayTotalComments(beanItem.getId() + "");
    }

    @Override
    protected String initFormTitle() {
        return this.beanItem.getName();
    }

    @Override
    protected AdvancedPreviewBeanForm<SimpleMilestone> initPreviewForm() {
        return new AdvancedPreviewBeanForm<>();
    }

    @Override
    protected void initRelatedComponents() {

    }

    @Override
    protected IFormLayoutFactory initFormLayoutFactory() {
        return new MilestoneFormLayoutFactory();
    }

    @Override
    protected AbstractBeanFieldGroupViewFieldFactory<SimpleMilestone> initBeanFormFieldFactory() {
        return new MilestoneFormFieldFactory(this.previewForm);
    }

    @Override
    protected ComponentContainer createButtonControls() {
        return new ProjectPreviewFormControlsGenerator<>(this.previewForm).createButtonControls(ProjectRolePermissionCollections.MILESTONES);
    }

    @Override
    protected ComponentContainer createBottomPanel() {
        MVerticalLayout toolbarLayout = new MVerticalLayout().withSpacing(false).withMargin(false);
        toolbarLayout.setDefaultComponentAlignment(Alignment.TOP_LEFT);
        relatedComments = new CommentNavigationButton(ProjectTypeConstants.MILESTONE);
        Section section = new Section(FontAwesome.COMMENT, relatedComments);
        toolbarLayout.addComponent(section);

        return toolbarLayout;
    }

    private class MilestoneFormFieldFactory extends AbstractBeanFieldGroupViewFieldFactory<SimpleMilestone> {
        private static final long serialVersionUID = 1L;

        public MilestoneFormFieldFactory(GenericBeanForm<SimpleMilestone> form) {
            super(form);
        }

        @Override
        protected Field<?> onCreateField(final Object propertyId) {
            if (propertyId.equals("startdate")) {
                return new DateViewField(beanItem.getStartdate());
            } else if (propertyId.equals("enddate")) {
                return new DateViewField(beanItem.getEnddate());
            } else if (propertyId.equals("owner")) {
                return new DefaultViewField(beanItem.getOwnerFullName());
            } else if (propertyId.equals("description")) {
                return new RichTextViewField(beanItem.getDescription());
            } else if (propertyId.equals("status")) {
                return new DefaultViewField(AppContext.getMessage(MilestoneStatus.class, beanItem.getStatus()));
            }
            return null;
        }
    }

}
