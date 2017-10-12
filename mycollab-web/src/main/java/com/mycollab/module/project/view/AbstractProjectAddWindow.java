/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view;

import com.mycollab.module.project.domain.Project;
import com.mycollab.module.project.i18n.ProjectI18nEnum;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.CacheableComponent;
import com.mycollab.vaadin.mvp.LoadPolicy;
import com.mycollab.vaadin.mvp.ViewScope;
import org.vaadin.teemu.wizards.WizardStep;
import org.vaadin.viritin.layouts.MWindow;

import static com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;

/**
 * @author MyCollab Ltd
 * @since 5.3.5
 */
@LoadPolicy(scope = ViewScope.PROTOTYPE)
public abstract class AbstractProjectAddWindow extends MWindow implements CacheableComponent {
    protected Project project;

    public AbstractProjectAddWindow(Project valuePrj) {
        super(UserUIContext.getMessage(ProjectI18nEnum.NEW));
        this.withWidth("900px").withModal(true).withResizable(false).withCenter();
        this.project = valuePrj;
        if (project.getProjectstatus() == null) {
            project.setProjectstatus(StatusI18nEnum.Open.name());
        }
    }

    public interface FormWizardStep extends WizardStep {
        boolean commit();
    }
}
