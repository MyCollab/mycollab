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

import com.vaadin.server.Resource;
import com.vaadin.ui.Image;
import com.vaadin.ui.Window;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 * 
 */
public class AttachmentPreviewWindow extends Window {
	private static final long serialVersionUID = 1L;

	private final Resource previewResource;

	public AttachmentPreviewWindow(Resource previewResource) {
		this.previewResource = previewResource;
		this.setHeight("600px");
		this.setCaption("Image Preview");
		center();
		initUI();
	}

	private void initUI() {
		Image previewImage = new Image(null, this.previewResource);
		previewImage.setSizeUndefined();
		this.setContent(previewImage);
		this.setResizable(false);
	}
}
