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

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.ecm.StorageNames;
import com.esofthead.mycollab.module.ecm.domain.ExternalFolder;
import com.esofthead.mycollab.module.ecm.domain.Folder;
import com.esofthead.mycollab.module.file.domain.criteria.FileSearchCriteria;
import com.esofthead.mycollab.vaadin.events.HasSearchHandlers;
import com.esofthead.mycollab.vaadin.events.SearchHandler;
import com.esofthead.mycollab.vaadin.mvp.CacheableComponent;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.CommonUIFactory;
import com.lexaden.breadcrumb.Breadcrumb;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@ViewComponent
public class FileBreadcrumb extends Breadcrumb implements CacheableComponent,
		HasSearchHandlers<FileSearchCriteria> {

	private static final long serialVersionUID = 1L;
	private List<SearchHandler<FileSearchCriteria>> handers;

	private String rootFolderPath;

	public FileBreadcrumb(String rootFolderPath) {
		if (org.apache.commons.lang3.StringUtils.isEmpty(rootFolderPath)) {
			throw new MyCollabException("Root folder path can not be empty");
		}
		this.rootFolderPath = rootFolderPath;
		this.setShowAnimationSpeed(Breadcrumb.AnimSpeed.SLOW);
		this.setHideAnimationSpeed(Breadcrumb.AnimSpeed.SLOW);
		this.setUseDefaultClickBehaviour(false);
	}

	void initBreadcrumb() {
		// home Btn ----------------
		this.addLink(new Button(null, new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				FileSearchCriteria criteria = new FileSearchCriteria();
				criteria.setBaseFolder(rootFolderPath);
				criteria.setRootFolder(rootFolderPath);
				notifySearchHandler(criteria);
			}
		}));
		this.setHeight(25, Unit.PIXELS);

		this.select(0);
		Button documentBtnLink = generateBreadcrumbLink("My Documents",
				new ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						FileSearchCriteria criteria = new FileSearchCriteria();
						criteria.setBaseFolder(rootFolderPath);
						criteria.setRootFolder(rootFolderPath);
						notifySearchHandler(criteria);
					}
				});
		documentBtnLink.addStyleName("link");
		this.addLink(documentBtnLink);
		this.setLinkEnabled(true, 1);
	}

	void gotoFolder(final Folder folder) {
		initBreadcrumb();

		if (folder instanceof ExternalFolder) {
			displayExternalFolder((ExternalFolder) folder);
		} else {
			displayMyCollabFolder(folder);
		}
	}

	private void displayMyCollabFolder(final Folder folder) {
		String folderPath = folder.getPath();
		if (!folderPath.startsWith(rootFolderPath)) {
			throw new MyCollabException("Invalid path " + rootFolderPath
					+ "---" + folderPath);
		}

		String remainPath = folderPath.substring(rootFolderPath.length());
		if (remainPath.startsWith("/")) {
			remainPath = remainPath.substring(1);
		}

		StringBuffer curPath = new StringBuffer("");
		String[] path = remainPath.split("/");
		for (int i = 0; i < path.length; i++) {
			String pathName = path[i];
			curPath.append(pathName);

			final Button btn = new Button();
			btn.setCaption(StringUtils.trim(pathName, 25, true));
			btn.setDescription(pathName);
			final String currentResourcePath = curPath.toString();
			btn.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					FileSearchCriteria criteria = new FileSearchCriteria();
					criteria.setBaseFolder(rootFolderPath + "/"
							+ currentResourcePath);
					criteria.setRootFolder(rootFolderPath);
					notifySearchHandler(criteria);
				}
			});

			this.select(i + 1);
			this.addLink(btn);
			this.setLinkEnabled(true, i + 1);

			if (i < path.length - 1) {
				curPath.append("/");
			}
		}
	}

	private void displayExternalFolder(final ExternalFolder folder) {
		String folderPath = folder.getPath();

		final StringBuffer curPath = new StringBuffer("");
		String[] path = folderPath.split("/");
		if (path.length == 0) {
			final Button btn = new Button();
			btn.setCaption(StringUtils.trim(folder.getExternalDrive()
					.getFoldername(), 25, true));
			this.addLink(btn);
			this.select(2);
			return;
		}

		for (int i = 0; i < path.length; i++) {
			String pathName = path[i];

			if (i == 0) {
				pathName = folder.getExternalDrive().getFoldername();
				curPath.append("/");
			} else {
				curPath.append(pathName);
			}

			final Button btn = new Button();
			btn.setCaption(StringUtils.trim(pathName, 25, true));
			btn.setDescription(pathName);

			final String currentFolderPath = curPath.toString();

			btn.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					FileSearchCriteria criteria = new FileSearchCriteria();
					criteria.setBaseFolder(currentFolderPath);

					criteria.setRootFolder("/");
					criteria.setStorageName(StorageNames.DROPBOX);
					criteria.setExternalDrive(folder.getExternalDrive());
					notifySearchHandler(criteria);
				}
			});

			this.select(i + 1);
			this.addLink(btn);
			this.setLinkEnabled(true, i + 1);

			if (i < path.length - 1) {
				curPath.append("/");
			}
		}
	}

	private static Button generateBreadcrumbLink(String linkname,
			Button.ClickListener listener) {
		return CommonUIFactory.createButtonTooltip(
				StringUtils.trim(linkname, 25, true), linkname, listener);
	}

	@Override
	public void addSearchHandler(final SearchHandler<FileSearchCriteria> handler) {
		if (this.handers == null) {
			this.handers = new ArrayList<SearchHandler<FileSearchCriteria>>();
		}
		this.handers.add(handler);
	}

	private void notifySearchHandler(final FileSearchCriteria criteria) {
		if (this.handers != null) {
			for (final SearchHandler<FileSearchCriteria> handler : this.handers) {
				handler.onSearch(criteria);
			}
		}
	}

}
