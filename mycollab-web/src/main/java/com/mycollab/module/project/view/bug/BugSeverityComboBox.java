/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view.bug;

import com.mycollab.module.project.i18n.OptionI18nEnum;
import com.mycollab.module.project.i18n.OptionI18nEnum.BugSeverity;
import com.mycollab.vaadin.web.ui.I18nValueComboBox;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.IconGenerator;
import com.vaadin.ui.StyleGenerator;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class BugSeverityComboBox extends I18nValueComboBox {
    private static final long serialVersionUID = 1L;

    public BugSeverityComboBox() {
        super(BugSeverity.class, OptionI18nEnum.bug_severities);
//        setItemIconGenerator((IconGenerator<BugSeverity>) severity -> VaadinIcons.STAR);
//        setStyleGenerator((StyleGenerator<BugSeverity>) severity -> {
//            if (severity != null) {
//                return "bug-severity-" + severity.toString().toLowerCase();
//            } else {
//                return null;
//            }
//        });
    }
}
