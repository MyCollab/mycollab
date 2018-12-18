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
package com.mycollab.module.user.view.component;

import com.mycollab.common.i18n.SecurityI18nEnum;
import com.mycollab.security.AccessPermissionFlag;
import com.mycollab.vaadin.web.ui.KeyCaptionComboBox;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class AccessPermissionComboBox extends KeyCaptionComboBox {
    private static final long serialVersionUID = 1L;

    public AccessPermissionComboBox() {
        super(false, new Entry(AccessPermissionFlag.NO_ACCESS, SecurityI18nEnum.NO_ACCESS),
                new Entry(AccessPermissionFlag.READ_ONLY, SecurityI18nEnum.READONLY),
                new Entry(AccessPermissionFlag.READ_WRITE, SecurityI18nEnum.READ_WRITE),
                new Entry(AccessPermissionFlag.ACCESS, SecurityI18nEnum.ACCESS));
        this.setValue(AccessPermissionFlag.READ_ONLY);
    }
}
