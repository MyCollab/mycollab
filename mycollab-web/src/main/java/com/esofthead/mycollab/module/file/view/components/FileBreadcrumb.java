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

import com.esofthead.mycollab.module.ecm.StorageNames;
import com.esofthead.mycollab.module.ecm.domain.ExternalFolder;
import com.esofthead.mycollab.module.ecm.domain.Folder;
import com.esofthead.mycollab.module.file.domain.criteria.FileSearchCriteria;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasSearchHandlers;
import com.esofthead.mycollab.vaadin.events.SearchHandler;
import com.esofthead.mycollab.vaadin.mvp.CacheableComponent;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.CommonUIFactory;
import com.esofthead.mycollab.vaadin.ui.utils.LabelStringGenerator;
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
	private static LabelStringGenerator menuLinkGenerator = new BreadcrumbLabelStringGenerator();
	private List<SearchHandler<FileSearchCriteria>> handers;
	private Folder currentBreadCrumbFolder;
	private String rootFolderPath;

	public void setRootFolderPath(String rootPath) {
		this.rootFolderPath = rootPath;
	}

	public FileBreadcrumb(String rootFolderPath) {
		this.setShowAnimationSpeed(Breadcrumb.AnimSpeed.SLOW);
		this.setHideAnimationSpeed(Breadcrumb.AnimSpeed.SLOW);
		this.setUseDefaultClickBehaviour(false);
	}

	public void initBreadcrumb() {
		// home Btn ----------------
		this.addLink(new Button(null, new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				FileSearchCriteria criteria = new FileSearchCriteria();
				criteria.setBaseFolder(rootFolderPath);
				criteria.setRootFolder(rootFolderPath);
				notifySelectHandler(criteria);
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
						notifySelectHandler(criteria);
					}
				});
		documentBtnLink.addStyleName("link");
		this.addLink(documentBtnLink);
		this.setLinkEnabled(true, 1);
	}

	public void gotoFolder(final Folder folder) {
		initBreadcrumb();
		currentBreadCrumbFolder = folder;
		String[] path;
		String headPath = "";

		// --- get path for algrothim ----
		if (rootFolderPath.split("/").length >= 3) {
			String folderPath = folder.getPath();
			headPath = folderPath.substring(0, folderPath.indexOf("/"));
			folderPath = folderPath.substring(folderPath.indexOf("/") + 1);
			path = folderPath.split("/");
		} else
			path = folder.getPath().split("/");

		final StringBuffer curPath = new StringBuffer("");
		curPath.append(headPath);

		boolean isNeedAdd3dot = (path.length > 6) ? true : false;
		int holder = 0;
		if (folder instanceof ExternalFolder && path.length == 0) { // home
																	// folder
			Button btn = new Button(((ExternalFolder) folder)
					.getExternalDrive().getFoldername());
			btn.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					FileSearchCriteria criteria = new FileSearchCriteria();
					criteria.setBaseFolder("/");
					criteria.setRootFolder("/");
					criteria.setStorageName(StorageNames.DROPBOX);
					criteria.setExternalDrive(((ExternalFolder) folder)
							.getExternalDrive());
					notifySelectHandler(criteria);
				}
			});
			this.select(1);
			this.addLink(btn);
			this.setLinkEnabled(true, 2);
			return;
		}
		for (int i = 0; i < path.length; i++) {
			String pathName = path[i];
			if (i == 0) {
				if (folder instanceof ExternalFolder) {
					pathName = ((ExternalFolder) folder).getExternalDrive()
							.getFoldername();
					curPath.append("");
				} else {
					if (curPath.toString().length() > 0) {
						curPath.append("/").append(pathName);
					} else {
						curPath.append(pathName);
					}
				}
			} else {
				curPath.append("/").append(pathName);
			}
			if (!pathName.equals(AppContext.getAccountId().toString())
					|| folder instanceof ExternalFolder) {
				final Button btn = new Button();
				if (pathName.length() > 25) {
					btn.setCaption(pathName.substring(0, 20) + "...");
				} else {

					btn.setCaption(pathName);
				}
				btn.setDescription(pathName);
				final String currentResourcePath = curPath.toString();
				btn.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						FileSearchCriteria criteria = new FileSearchCriteria();
						if (currentResourcePath.length() == 0
								&& folder instanceof ExternalFolder) {
							criteria.setBaseFolder("/");
						} else {
							criteria.setBaseFolder(currentResourcePath);
						}
						if (folder instanceof ExternalFolder) {
							criteria.setRootFolder("/");
							criteria.setStorageName(StorageNames.DROPBOX);
							criteria.setExternalDrive(((ExternalFolder) folder)
									.getExternalDrive());
						} else {
							criteria.setRootFolder(rootFolderPath);
						}
						notifySelectHandler(criteria);
					}
				});
				if (i > 1 || folder instanceof ExternalFolder) {
					int index = (folder instanceof ExternalFolder) ? i + 2 : i;
					if (path.length <= 6) {
						this.select(index - 1);
						this.addLink(btn);
						this.setLinkEnabled(true, index);
					} else if (i == path.length - 1 || i == path.length - 2) {
						this.select(holder - 1);
						this.addLink(btn);
						this.setLinkEnabled(true, holder);
						holder++;
					} else {
						if (i > 2 && i < path.length - 2 && isNeedAdd3dot) {
							this.select(index - 1);
							this.addLink(new Button("..."));
							this.setLinkEnabled(true, index);
							isNeedAdd3dot = false;
							holder = index + 1;
						} else if (i <= 2) {
							this.select(index - 1);
							this.addLink(btn);
							this.setLinkEnabled(true, index);
						}
					}
				}
			}
		}
	}

	private static Button generateBreadcrumbLink(String linkname,
			Button.ClickListener listener) {
		return CommonUIFactory.createButtonTooltip(
				menuLinkGenerator.handleText(linkname), linkname, listener);
	}

	@Override
	public void addSearchHandler(final SearchHandler<FileSearchCriteria> handler) {
		if (this.handers == null) {
			this.handers = new ArrayList<SearchHandler<FileSearchCriteria>>();
		}
		this.handers.add(handler);
	}

	public void notifySelectHandler(final FileSearchCriteria criteria) {
		if (this.handers != null) {
			for (final SearchHandler<FileSearchCriteria> handler : this.handers) {
				handler.onSearch(criteria);
			}
		}
	}

	public Folder getCurrentBreadCrumbFolder() {
		return currentBreadCrumbFolder;
	}

	public void setCurrentBreadCrumbFolder(Folder currentBreamCrumbFolder) {
		this.currentBreadCrumbFolder = currentBreamCrumbFolder;
	}

	private static class BreadcrumbLabelStringGenerator implements
			LabelStringGenerator {

		@Override
		public String handleText(String value) {
			if (value.length() > 35) {
				return value.substring(0, 35) + "...";
			}
			return value;
		}

	}

}
