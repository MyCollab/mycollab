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

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.vaadin.peter.buttongroup.ButtonGroup;

import com.esofthead.mycollab.vaadin.events.HasMassItemActionHandlers;
import com.esofthead.mycollab.vaadin.events.MassItemActionHandler;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 3.0
 * 
 */
public class DefaultMassItemActionHandlersContainer extends HorizontalLayout
		implements HasMassItemActionHandlers {

	private static final long serialVersionUID = 1L;

	private Set<MassItemActionHandler> handlers;

	private Map<String, ButtonGroup> groupMap = new HashMap<>();

	public DefaultMassItemActionHandlersContainer() {
		super();
		this.setSpacing(true);
	}

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

		optionBtn.setDescription(description);

		group.addButton(optionBtn);
	}

	public void addDownloadActionItem(final String id, Resource resource,
			String groupId, String downloadFileName, String description) {
		ButtonGroup group = groupMap.get(groupId);

		if (group == null) {
			group = new ButtonGroup();
			groupMap.put(groupId, group);
			this.addComponent(group);
		}

		Button optionBtn = new Button("");

		FileDownloader fileDownler = new FileDownloader(new StreamResource(
				new LazyStreamSource(id), downloadFileName));
		fileDownler.extend(optionBtn);
		optionBtn.setIcon(resource);
		optionBtn.addStyleName(UIConstants.THEME_BLUE_LINK);
		optionBtn.setDescription(description);
		group.addButton(optionBtn);
	}

	private void changeOption(String id) {
		if (handlers != null) {
			for (MassItemActionHandler handler : handlers) {
				handler.onSelect(id);
			}
		}
	}

	private class LazyStreamSource implements StreamSource {
		private static final long serialVersionUID = 1L;
		private String id;

		public LazyStreamSource(String id) {
			this.id = id;
		}

		@Override
		public InputStream getStream() {
			return buildStreamResource(id).getStreamSource().getStream();
		}
	}

	protected StreamResource buildStreamResource(String id) {
		if (handlers != null) {
			for (MassItemActionHandler handler : handlers) {
				StreamResource streamResource = handler.buildStreamResource(id);
				if (streamResource != null) {
					return streamResource;
				}
			}
		}

		return null;
	}

	@Override
	public void addMassItemActionHandler(MassItemActionHandler handler) {
		if (handlers == null) {
			handlers = new HashSet<>();
		}
		handlers.add(handler);

	}
}
