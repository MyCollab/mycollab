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
package com.esofthead.mycollab.mobile.module.project.ui.form.field;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.esofthead.mycollab.module.ecm.domain.Content;
import com.esofthead.mycollab.module.ecm.service.ResourceService;
import com.esofthead.mycollab.module.file.AttachmentType;
import com.esofthead.mycollab.module.file.AttachmentUtils;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.5.3
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ProjectFormAttachmentDisplayField extends CustomField {
	private static final long serialVersionUID = 1L;

	private int projectid;
	private AttachmentType type;
	private int typeid;

	public ProjectFormAttachmentDisplayField(final int projectid,
			final AttachmentType type, final int typeid) {
		this.projectid = projectid;
		this.type = type;
		this.typeid = typeid;
	}

	@Override
	public Class<?> getType() {
		return Object.class;
	}

	@Override
	protected Component initContent() {
		ResourceService resourceService = ApplicationContextUtil
				.getSpringBean(ResourceService.class);
		List<Content> attachments = resourceService.getContents(AttachmentUtils
				.getProjectEntityAttachmentPath(AppContext.getAccountId(),
						projectid, type, typeid));
		if (CollectionUtils.isNotEmpty(attachments)) {
			VerticalLayout comp = new VerticalLayout();
			comp.setStyleName("attachment-view-panel");

			for (Content attachment : attachments) {
				Label l = new Label(attachment.getTitle());
				l.setWidth("100%");
				comp.addComponent(l);
			}

			return comp;
		}
		return new Label("&nbsp;", ContentMode.HTML);
	}
}
