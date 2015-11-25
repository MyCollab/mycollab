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
package com.esofthead.mycollab.mobile.module.project.view.settings;

import com.esofthead.mycollab.mobile.module.project.ui.ProjectPreviewFormControlsGenerator;
import com.esofthead.mycollab.mobile.ui.AbstractPreviewItemComp;
import com.esofthead.mycollab.mobile.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.i18n.ProjectMemberI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.form.field.DefaultViewField;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
@ViewComponent
public class ProjectMemberReadViewImpl extends AbstractPreviewItemComp<SimpleProjectMember> implements ProjectMemberReadView {
    private static final long serialVersionUID = 364308373821870384L;

    @Override
    protected void afterPreviewItem() {
    }

    @Override
    protected String initFormTitle() {
        return this.beanItem.getDisplayName();
    }

    @Override
    protected AdvancedPreviewBeanForm<SimpleProjectMember> initPreviewForm() {
        return new AdvancedPreviewBeanForm<>();
    }

    @Override
    protected void initRelatedComponents() {
    }

    @Override
    protected IFormLayoutFactory initFormLayoutFactory() {
        return new ProjectMemberFormLayoutFactory();
    }

    @Override
    protected AbstractBeanFieldGroupViewFieldFactory<SimpleProjectMember> initBeanFormFieldFactory() {
        return new ProjectMemberBeanFormFieldFactory(this.previewForm);
    }

    @Override
    protected ComponentContainer createButtonControls() {
        return new ProjectPreviewFormControlsGenerator<>(this.previewForm).createButtonControls(
                ProjectPreviewFormControlsGenerator.EDIT_BTN_PRESENTED | ProjectPreviewFormControlsGenerator.DELETE_BTN_PRESENTED,
                ProjectRolePermissionCollections.USERS);
    }

    @Override
    protected ComponentContainer createBottomPanel() {
        return null;
    }

    private class ProjectMemberBeanFormFieldFactory extends AbstractBeanFieldGroupViewFieldFactory<SimpleProjectMember> {
        private static final long serialVersionUID = 5269043189285551214L;

        public ProjectMemberBeanFormFieldFactory(GenericBeanForm<SimpleProjectMember> form) {
            super(form);
        }

        @Override
        protected Field<?> onCreateField(Object propertyId) {
            if (propertyId.equals("memberFullName")) {
                return new DefaultViewField(beanItem.getDisplayName());
            } else if (propertyId.equals("roleName")) {
                String memberRole;
                if (Boolean.TRUE.equals(beanItem.getIsadmin())) {
                    memberRole = AppContext.getMessage(ProjectMemberI18nEnum.M_FORM_PROJECT_ADMIN);
                } else {
                    memberRole = beanItem.getRoleName();
                }
                return new DefaultViewField(memberRole);
            } else if (propertyId.equals("email")) {
                return new DefaultViewField(beanItem.getEmail());
            }
            return null;
        }

    }

    @Override
    public HasPreviewFormHandlers<SimpleProjectMember> getPreviewFormHandlers() {
        return this.previewForm;
    }

}
