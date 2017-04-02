/**
 * This file is part of mycollab-web-community.
 *
 * mycollab-web-community is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web-community is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web-community.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.community.module.user.accountsettings.customize.view;

import com.mycollab.module.user.accountsettings.customize.view.IThemeCustomizeView;
import com.mycollab.module.user.domain.AccountTheme;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.web.ui.NotPresentedView;

/**
 * @author MyCollab Ltd
 * @since 5.3.1
 */
@ViewComponent
public class ThemeCustomizeView extends NotPresentedView implements IThemeCustomizeView {
    @Override
    public void customizeTheme(AccountTheme accountTheme) {

    }
}
