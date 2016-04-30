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

package com.esofthead.mycollab.module.project.view.settings;

import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.i18n.VersionI18nEnum;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.module.project.ui.components.AbstractEditItemComp;
import com.esofthead.mycollab.module.project.view.settings.component.VersionEditFormFieldFactory;
import com.esofthead.mycollab.module.tracker.domain.Version;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasEditFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.esofthead.mycollab.vaadin.ui.AdvancedEditBeanForm;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.web.ui.DynaFormLayout;
import com.esofthead.mycollab.vaadin.web.ui.EditFormControlsGenerator;
import com.vaadin.server.Resource;
import com.vaadin.ui.ComponentContainer;

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
        return (beanItem.getId() == null) ? null : beanItem.getVersionname();
    }

    @Override
    protected String initFormHeader() {
        return (beanItem.getId() == null) ? AppContext
                .getMessage(VersionI18nEnum.VIEW_NEW_TITLE) : AppContext
                .getMessage(VersionI18nEnum.VIEW_EDIT_TITLE);
    }

    @Override
    protected Resource initFormIconResource() {
        return ProjectAssetsManager.getAsset(ProjectTypeConstants.BUG_VERSION);
    }

    @Override
    protected ComponentContainer createButtonControls() {
        return (new EditFormControlsGenerator<>(editForm)).createButtonControls();
    }

    @Override
    protected AdvancedEditBeanForm<Version> initPreviewForm() {
        return new AdvancedEditBeanForm<>();
    }

    @Override
    protected IFormLayoutFactory initFormLayoutFactory() {
        return new DynaFormLayout(ProjectTypeConstants.BUG_VERSION,
                VersionDefaultFormLayoutFactory.getForm(), "id");
    }

    @Override
    protected AbstractBeanFieldGroupEditFieldFactory<Version> initBeanFormFieldFactory() {
        return new VersionEditFormFieldFactory(editForm);
    }
}
