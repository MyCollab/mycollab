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

import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.TextField;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class NumberField extends TextField implements TextChangeListener {
	private static final long serialVersionUID = 1L;

	private String lastValue;

	public NumberField() {
		setImmediate(true);
		setTextChangeEventMode(TextChangeEventMode.EAGER);
		addTextChangeListener(this);
	}

	@Override
	public void textChange(TextChangeEvent event) {
		String text = event.getText();
		try {
			new Double(text);
			lastValue = text;
		} catch (NumberFormatException e) {
			setValue(lastValue);
		}
	}
}
