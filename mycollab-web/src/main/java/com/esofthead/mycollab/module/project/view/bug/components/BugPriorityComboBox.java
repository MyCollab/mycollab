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
package com.esofthead.mycollab.module.project.view.bug.components;

import com.esofthead.mycollab.module.project.ProjectResources;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugPriority;
import com.esofthead.mycollab.vaadin.ui.AssetResource;
import com.esofthead.mycollab.vaadin.ui.I18nValueComboBox;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class BugPriorityComboBox extends I18nValueComboBox {
    private static final long serialVersionUID = 1L;

    public BugPriorityComboBox() {
        super();
        this.setNullSelectionAllowed(false);
        this.setCaption(null);
        this.loadData(Arrays.asList(OptionI18nEnum.bug_priorities));
        this.setItemIcon(BugPriority.Blocker.name(), new AssetResource(ProjectResources.B_PRIORITY_BLOCKER_IMG_12));
        this.setItemIcon(BugPriority.Critical.name(), new AssetResource(ProjectResources.B_PRIORITY_CRITICAL_IMG_12));
        this.setItemIcon(BugPriority.Major.name(), new AssetResource(ProjectResources.B_PRIORITY_MAJOR_IMG_12));
        this.setItemIcon(BugPriority.Minor.name(), new AssetResource(ProjectResources.B_PRIORITY_MINOR_IMG_12));
        this.setItemIcon(BugPriority.Trivial.name(), new AssetResource(ProjectResources.B_PRIORITY_TRIVIAL_IMG_12));
    }
}
