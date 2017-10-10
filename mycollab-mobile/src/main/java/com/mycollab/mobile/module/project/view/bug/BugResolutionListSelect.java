/**
 * mycollab-mobile - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.mobile.module.project.view.bug;

import com.mycollab.mobile.ui.I18NValueListSelect;
import com.mycollab.module.project.i18n.OptionI18nEnum.BugResolution;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
public class BugResolutionListSelect extends I18NValueListSelect {
    private static final long serialVersionUID = 1L;

    private BugResolutionListSelect(boolean nullIsAllowable, Enum<?>... values) {
        super(nullIsAllowable, values);
    }

    public static BugResolutionListSelect getInstanceForWontFixWindow() {
        return new BugResolutionListSelect(false, BugResolution.CannotReproduce, BugResolution.Duplicate, BugResolution.Invalid);
    }

    public static BugResolutionListSelect getInstanceForResolvedBugWindow() {
        return new BugResolutionListSelect(false, BugResolution.Fixed);
    }
}
