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
package com.esofthead.mycollab.vaadin.ui;

import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.dialogs.DefaultConfirmDialogFactory;

import com.vaadin.server.Sizeable;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 * 
 */
public class ConfirmDialogFactory extends DefaultConfirmDialogFactory {
	private static final long serialVersionUID = 1L;

	@Override
	public ConfirmDialog create(final String caption, final String message,
			final String okCaption, final String cancelCaption) {
		final ConfirmDialog d = super.create(caption, message, okCaption,
				cancelCaption);

		d.getContent().setStyleName("custom-dialog");

		final Button ok = d.getOkButton();
		ok.setStyleName(UIConstants.THEME_GREEN_LINK);

		HorizontalLayout buttons = (HorizontalLayout) ok.getParent();
		buttons.setHeight(Sizeable.SIZE_UNDEFINED, Unit.PIXELS);

		final Button cancelBtn = d.getCancelButton();
		cancelBtn.setStyleName(UIConstants.THEME_GRAY_LINK);
		cancelBtn.focus();

		return d;
	}

}
