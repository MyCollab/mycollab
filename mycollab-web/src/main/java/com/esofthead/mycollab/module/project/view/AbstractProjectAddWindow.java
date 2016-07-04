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
package com.esofthead.mycollab.module.project.view;

import com.esofthead.mycollab.module.project.domain.Project;
import com.esofthead.mycollab.module.project.i18n.ProjectI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.CacheableComponent;
import com.esofthead.mycollab.vaadin.mvp.LoadPolicy;
import com.esofthead.mycollab.vaadin.mvp.ViewScope;
import com.vaadin.ui.Window;
import org.vaadin.teemu.wizards.WizardStep;

/**
 * @author MyCollab Ltd
 * @since 5.3.5
 */
@LoadPolicy(scope = ViewScope.PROTOTYPE)
public abstract class AbstractProjectAddWindow extends Window implements CacheableComponent {
    protected Project project;

    public AbstractProjectAddWindow(Project valuePrj) {
        setCaption(AppContext.getMessage(ProjectI18nEnum.NEW));
        this.setWidth("900px");
        this.center();
        this.setResizable(false);
        this.setModal(true);
        this.project = valuePrj;
    }

    public interface FormWizardStep extends WizardStep {
        boolean commit();
    }
}
