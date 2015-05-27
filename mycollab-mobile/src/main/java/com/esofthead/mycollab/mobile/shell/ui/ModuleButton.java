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
package com.esofthead.mycollab.mobile.shell.ui;

import com.vaadin.ui.Button;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
public class ModuleButton extends Button {
	private static final long serialVersionUID = 1197359749888761486L;
	
	public static final String CLASSNAME = "module-button";

    public ModuleButton(String moduleName) {
        super(moduleName);

        this.setStyleName(CLASSNAME);
    }
}
