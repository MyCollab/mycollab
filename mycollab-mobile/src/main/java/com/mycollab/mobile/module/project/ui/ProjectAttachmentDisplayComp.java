/**
 * mycollab-mobile - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.mobile.module.project.ui;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.mobile.ui.FormSectionBuilder;
import com.mycollab.mobile.ui.MobileAttachmentUtils;
import com.mycollab.module.ecm.domain.Content;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.VerticalLayout;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
public class ProjectAttachmentDisplayComp extends CssLayout {
    private static final long serialVersionUID = -3401160588430768707L;

    private List<Content> attachments;

    public ProjectAttachmentDisplayComp(List<Content> attachments) {
        this.attachments = attachments;
        this.constructUI();
    }

    private void constructUI() {
        this.setStyleName("attachment-display-comp");
        this.addComponent(FormSectionBuilder.build(FontAwesome.FILE, GenericI18Enum.FORM_ATTACHMENTS));
        VerticalLayout comp = new VerticalLayout();
        comp.setStyleName("attachment-view-panel");
        comp.setWidth("100%");

        for (final Content attachment : attachments) {
            Component attachmentRow = MobileAttachmentUtils.renderAttachmentRow(attachment);
            comp.addComponent(attachmentRow);
        }
        this.addComponent(comp);
    }
}
