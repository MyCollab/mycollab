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
package com.esofthead.mycollab.module.project.ui.components;

import com.esofthead.mycollab.core.arguments.Order;
import com.vaadin.ui.ComboBox;

public class ItemOrderComboBox extends ComboBox {
	private static final long serialVersionUID = 1L;

	public ItemOrderComboBox() {
		this.setItemCaptionMode(ItemCaptionMode.EXPLICIT);
		this.setNullSelectionAllowed(false);
		this.addItem(Order.ASCENDING);
		this.setItemCaption(Order.ASCENDING, "Ascending");

		this.addItem(Order.DESCENDING);
		this.setItemCaption(Order.DESCENDING, "Descending");
		this.select(Order.ASCENDING);
	}
}
