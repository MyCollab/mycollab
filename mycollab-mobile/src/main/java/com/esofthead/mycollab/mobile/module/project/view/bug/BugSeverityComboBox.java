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
package com.esofthead.mycollab.mobile.module.project.view.bug;

import com.esofthead.mycollab.module.project.ProjectResources;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugSeverity;
import com.esofthead.mycollab.vaadin.ui.AssetResource;
import com.esofthead.mycollab.vaadin.ui.I18nValueComboBox;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
public class BugSeverityComboBox extends I18nValueComboBox {

    private static final long serialVersionUID = 1L;

    public BugSeverityComboBox() {
        super();
        this.setNullSelectionAllowed(false);
        this.setCaption(null);
        this.loadData(Arrays.asList(OptionI18nEnum.bug_severities));

        this.setItemIcon(BugSeverity.Critical.name(), new AssetResource(ProjectResources.B_SEVERITY_CRITICAL_IMG_12));
        this.setItemIcon(BugSeverity.Major.name(), new AssetResource(ProjectResources.B_SEVERITY_MAJOR_IMG_12));
        this.setItemIcon(BugSeverity.Minor.name(), new AssetResource(ProjectResources.B_SEVERITY_MINOR_IMG_12));
        this.setItemIcon(BugSeverity.Trivial.name(), new AssetResource(ProjectResources.B_SEVERITY_TRIVIAL_IMG_12));
    }
}
