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
package com.esofthead.mycollab.module.file.view.components;

import java.util.ArrayList;
import java.util.List;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.module.ecm.ResourceUtils;
import com.esofthead.mycollab.module.ecm.domain.Content;
import com.esofthead.mycollab.module.ecm.domain.Resource;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.resources.StreamDownloadResourceUtil;
import com.esofthead.mycollab.vaadin.ui.GridFormLayoutHelper;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.UiUtils;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class FileDownloadWindow extends Window {
	private static final long serialVersionUID = 1L;
	private final Content content;

	public FileDownloadWindow(final Content content) {
		super(content.getName());
		this.setWidth("400px");
		this.center();
		this.setResizable(false);
		this.setModal(true);

		this.content = content;
		this.constructBody();
	}

	private void constructBody() {
		final VerticalLayout layout = new VerticalLayout();
		final Embedded iconEmbed = new Embedded();
		iconEmbed.setSource(MyCollabResource
				.newResource("icons/page_white.png"));
		UiUtils.addComponent(layout, iconEmbed, Alignment.MIDDLE_CENTER);

		final GridFormLayoutHelper info = new GridFormLayoutHelper(1, 4,
				"100%", "80px", Alignment.TOP_LEFT);
		info.getLayout().setWidth("100%");
		info.getLayout().setMargin(new MarginInfo(false, true, false, true));
		info.getLayout().setSpacing(false);

		if (this.content.getDescription() != null) {

			final Label desvalue = new Label();
			if (!this.content.getDescription().equals("")) {
				desvalue.setData(this.content.getDescription());
			} else {
				desvalue.setValue("&nbsp;");
				desvalue.setContentMode(ContentMode.HTML);
			}
			info.addComponent(desvalue, "Description", 0, 0);
		}
		final Label author = new Label(this.content.getCreatedBy());
		info.addComponent(author, "Created by", 0, 1);

		final Label size = new Label(
				ResourceUtils.getVolumeDisplay(this.content.getSize()));
		info.addComponent(size, "Size", 0, 2);

		final Label dateCreate = new Label(AppContext.formatDate(this.content
				.getCreated().getTime()));
		info.addComponent(dateCreate, "Date created", 0, 3);

		layout.addComponent(info.getLayout());

		final HorizontalLayout buttonControls = new HorizontalLayout();
		buttonControls.setSpacing(true);
		buttonControls.setMargin(new MarginInfo(true, false, true, false));

		final Button downloadBtn = new Button("Download");
		List<Resource> resources = new ArrayList<Resource>();
		resources.add(content);

		StreamResource downloadResource = StreamDownloadResourceUtil
				.getStreamResourceSupportExtDrive(resources);

		FileDownloader fileDownloader = new FileDownloader(downloadResource);
		fileDownloader.extend(downloadBtn);

		downloadBtn.addStyleName(UIConstants.THEME_GREEN_LINK);
		UiUtils.addComponent(buttonControls, downloadBtn,
				Alignment.MIDDLE_CENTER);

		final Button cancelBtn = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL),
				new ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						FileDownloadWindow.this.close();
					}
				});
		cancelBtn.addStyleName(UIConstants.THEME_GRAY_LINK);

		UiUtils.addComponent(buttonControls, cancelBtn, Alignment.MIDDLE_CENTER);
		UiUtils.addComponent(layout, buttonControls, Alignment.MIDDLE_CENTER);
		this.setContent(layout);
	}
}
