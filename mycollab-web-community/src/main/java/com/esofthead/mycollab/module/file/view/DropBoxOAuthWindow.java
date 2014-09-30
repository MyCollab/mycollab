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
package com.esofthead.mycollab.module.file.view;

import com.esofthead.mycollab.common.ui.components.AbstractCloudDriveOAuthWindow;
import com.vaadin.ui.Label;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.5.2
 *
 */
public class DropBoxOAuthWindow extends AbstractCloudDriveOAuthWindow {
	private static final long serialVersionUID = 1L;

	public DropBoxOAuthWindow() {
		super();
		this.setWidth("420px");
		this.setResizable(false);
		this.center();
		this.setContent(new Label("Do not support this feature in this edition"));
	}
}
