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

package com.esofthead.mycollab.module.user.view.component;

import com.esofthead.mycollab.security.AccessPermissionFlag;
import com.esofthead.mycollab.vaadin.ui.KeyCaptionComboBox;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class AccessPermissionComboBox extends KeyCaptionComboBox {
	private static final long serialVersionUID = 1L;

	public AccessPermissionComboBox() {
		super(false);

		this.addItem(AccessPermissionFlag.NO_ACCESS, "No Access");
		this.addItem(AccessPermissionFlag.READ_ONLY, "Read Only");
		this.addItem(AccessPermissionFlag.READ_WRITE, "Read & Write");
		this.addItem(AccessPermissionFlag.ACCESS, "Access");

		this.setValue(AccessPermissionFlag.READ_ONLY);
	}
}
