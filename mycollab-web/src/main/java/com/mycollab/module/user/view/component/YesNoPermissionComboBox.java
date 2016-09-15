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
package com.mycollab.module.user.view.component;

import com.mycollab.common.i18n.SecurityI18nEnum;
import com.mycollab.security.BooleanPermissionFlag;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.web.ui.KeyCaptionComboBox;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class YesNoPermissionComboBox extends KeyCaptionComboBox {
    private static final long serialVersionUID = 1L;

    public YesNoPermissionComboBox() {
        super(false);
        this.addItem(BooleanPermissionFlag.TRUE, UserUIContext.getMessage(SecurityI18nEnum.YES));
        this.addItem(BooleanPermissionFlag.FALSE, UserUIContext.getMessage(SecurityI18nEnum.NO));
        this.setValue(BooleanPermissionFlag.FALSE);
    }
}
