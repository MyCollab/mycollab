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
package com.esofthead.mycollab.mobile.module.project.view.message;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.mobile.module.project.ui.form.field.ProjectFormAttachmentUploadField;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * @author MyCollab Ltd.
 *
 * @since 4.5.2
 */
public class MessageAttachmentField extends ProjectFormAttachmentUploadField {
	private static final long serialVersionUID = -6328176923669282211L;

	@Override
	protected void constructUI() {
		content = new VerticalLayout();
		content.setStyleName("attachment-field");

		rowWrap = new VerticalLayout();
		rowWrap.setWidth("100%");
		rowWrap.setStyleName("attachment-row-wrap");

		HorizontalLayout compHeader = new HorizontalLayout();
		compHeader.setWidth("100%");
		compHeader.setStyleName("attachment-field-header");

		Label headerLbl = new Label(
				AppContext.getMessage(GenericI18Enum.M_FORM_ATTACHMENT));
		headerLbl.setStyleName("field-caption");

		compHeader.addComponent(headerLbl);
		compHeader.setExpandRatio(headerLbl, 1.0f);

		compHeader.addComponent(attachmentBtn);

		content.addComponent(compHeader);

		content.addComponent(rowWrap);
	}

}
