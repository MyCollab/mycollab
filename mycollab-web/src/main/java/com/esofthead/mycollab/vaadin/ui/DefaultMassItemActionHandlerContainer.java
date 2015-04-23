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

import com.esofthead.mycollab.reporting.ReportExportType;
import com.esofthead.mycollab.vaadin.events.HasMassItemActionHandler;
import com.esofthead.mycollab.vaadin.events.MassItemActionHandler;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.peter.buttongroup.ButtonGroup;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author MyCollab Ltd.
 * @since 3.0
 * 
 */
public class DefaultMassItemActionHandlerContainer extends MHorizontalLayout
		implements HasMassItemActionHandler {
	private static final long serialVersionUID = 1L;

	private MassItemActionHandler actionHandler;
	private Map<String, ButtonGroup> groupMap = new HashMap<>();

	public void addActionItem(final String id, Resource resource,
			String groupId, String description) {
		ButtonGroup group = groupMap.get(groupId);

		if (group == null) {
			group = new ButtonGroup();
			groupMap.put(groupId, group);
			this.addComponent(group);
		}

		Button optionBtn = new Button(null, new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				changeOption(id);
			}
		});
		optionBtn.setIcon(resource);
		if ("delete".equals(groupId)) {
			optionBtn.addStyleName(UIConstants.THEME_RED_LINK);
		} else {
			optionBtn.addStyleName(UIConstants.THEME_BLUE_LINK);
		}
        optionBtn.addStyleName(UIConstants.BUTTON_SMALL_PADDING);
		optionBtn.setDescription(description);

		group.addButton(optionBtn);
	}

	public void addDownloadActionItem(ReportExportType exportType, Resource resource,
			String groupId, String downloadFileName, String description) {
		ButtonGroup group = groupMap.get(groupId);

		if (group == null) {
			group = new ButtonGroup();
			groupMap.put(groupId, group);
			this.addComponent(group);
		}

		Button optionBtn = new Button("");
		FileDownloader fileDownloader = new FileDownloader(new StreamResource(
				new DownloadStreamSource(exportType), downloadFileName));
		fileDownloader.extend(optionBtn);
		optionBtn.setIcon(resource);
		optionBtn.addStyleName(UIConstants.THEME_BLUE_LINK);
        optionBtn.addStyleName(UIConstants.BUTTON_SMALL_PADDING);
		optionBtn.setDescription(description);
		group.addButton(optionBtn);
	}

	private void changeOption(String id) {
		if (actionHandler != null) {
			actionHandler.onSelect(id);
		}
	}

	private class DownloadStreamSource implements StreamSource {
		private static final long serialVersionUID = 1L;
		private ReportExportType exportType;

		public DownloadStreamSource(ReportExportType exportType) {
			this.exportType = exportType;
		}

		@Override
		public InputStream getStream() {
			return buildStreamResource(exportType).getStreamSource().getStream();
		}
	}

	protected StreamResource buildStreamResource(ReportExportType id) {
		if (actionHandler != null) {
			StreamResource streamResource = actionHandler.buildStreamResource(id);
			if (streamResource != null) {
				return streamResource;
			}
		}

		return null;
	}

	@Override
	public void setMassActionHandler(MassItemActionHandler handler) {
		actionHandler = handler;
	}
}
