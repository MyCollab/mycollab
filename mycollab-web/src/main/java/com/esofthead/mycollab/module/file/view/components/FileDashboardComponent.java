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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.utils.LocalizationHelper;
import com.esofthead.mycollab.module.ecm.domain.ExternalDrive;
import com.esofthead.mycollab.module.ecm.domain.ExternalFolder;
import com.esofthead.mycollab.module.ecm.domain.Folder;
import com.esofthead.mycollab.module.ecm.domain.Resource;
import com.esofthead.mycollab.module.ecm.service.ExternalDriveService;
import com.esofthead.mycollab.module.ecm.service.ExternalResourceService;
import com.esofthead.mycollab.module.ecm.service.ResourceMover;
import com.esofthead.mycollab.module.ecm.service.ResourceService;
import com.esofthead.mycollab.module.file.domain.criteria.FileSearchCriteria;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.SearchHandler;
import com.esofthead.mycollab.vaadin.ui.GenericSearchPanel;
import com.esofthead.mycollab.vaadin.ui.Separator;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.UiUtils;
import com.esofthead.mycollab.web.MyCollabResource;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Tree.CollapseEvent;
import com.vaadin.ui.Tree.ExpandEvent;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 *
 */
public abstract class FileDashboardComponent extends VerticalLayout {
	private static final long serialVersionUID = 1L;
	private String rootPath;
	private String rootFolderName;
	private final FileSearchPanel fileSearchPanel;
	private ResourceHandlerComponent resourceHandlerComponent;
	private Folder baseFolder;
	private HorizontalLayout resourceContainer;

	private final ResourceService resourceService;

	public FileDashboardComponent() {
		this.setWidth("100%");
		this.setSpacing(true);
		this.resourceService = ApplicationContextUtil
				.getSpringBean(ResourceService.class);
		this.fileSearchPanel = new FileSearchPanel(null);

		this.addComponent(this.fileSearchPanel);

		resourceContainer = new HorizontalLayout();
		resourceContainer.setSizeFull();

		this.resourceHandlerComponent = new ResourceHandlerComponent(
				baseFolder, rootPath, null);
		this.resourceHandlerComponent.setSpacing(true);
		resourceContainer.addComponent(resourceHandlerComponent);
		resourceContainer.setComponentAlignment(resourceHandlerComponent,
				Alignment.TOP_LEFT);
		resourceContainer.setExpandRatio(resourceHandlerComponent, 1.0f);

		this.addComponent(resourceContainer);

	}

	abstract protected void doSearch(FileSearchCriteria searchCriteria);

	private void displayResourcesInTable(final Folder folder) {
		resourceHandlerComponent.displayComponent(folder, rootPath,
				rootFolderName, false);
	}

	public void displayResources(String rootPath, String rootFolderName) {
		this.rootPath = rootPath;
		this.rootFolderName = rootFolderName;

		this.baseFolder = new Folder();
		this.baseFolder.setPath(this.rootPath);

		this.displayResourcesInTable(this.baseFolder);

		resourceHandlerComponent
				.addSearchHandlerToBreadCrumb(new SearchHandler<FileSearchCriteria>() {
					@Override
					public void onSearch(FileSearchCriteria criteria) {
						Folder selectedFolder = null;
						selectedFolder = (Folder) FileDashboardComponent.this.resourceService
								.getResource(criteria.getBaseFolder());
						resourceHandlerComponent
								.constructBodyItemContainer(selectedFolder);
						resourceHandlerComponent
								.gotoFolderBreadCumb(selectedFolder);
						FileDashboardComponent.this.baseFolder = selectedFolder;
						resourceHandlerComponent
								.setCurrentBaseFolder(selectedFolder);
					}
				});
	}

	protected class RenameResourceWindow extends Window {
		private static final long serialVersionUID = 1L;
		private final Resource resource;
		private final ResourceService service;

		public RenameResourceWindow(final Resource resource,
				final ResourceService service) {
			super("Rename folder/file");
			this.center();
			this.setWidth("400px");

			this.service = service;
			this.resource = resource;
			this.constructBody();
		}

		private void constructBody() {
			final VerticalLayout contentLayout = new VerticalLayout();
			final HorizontalLayout topRename = new HorizontalLayout();
			topRename.setSpacing(true);
			topRename.setMargin(true);

			final Label label = new Label("Enter new name: ");
			UiUtils.addComponent(topRename, label, Alignment.MIDDLE_LEFT);

			final TextField newName = new TextField();
			newName.setWidth("150px");
			UiUtils.addComponent(topRename, newName, Alignment.MIDDLE_LEFT);

			UiUtils.addComponent(contentLayout, topRename,
					Alignment.MIDDLE_LEFT);

			final HorizontalLayout controlButton = new HorizontalLayout();
			controlButton.setSpacing(true);
			final Button save = new Button("Save", new ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(final ClickEvent event) {
					final String oldPath = RenameResourceWindow.this.resource
							.getPath();
					final String parentPath = oldPath.substring(0,
							oldPath.lastIndexOf("/") + 1);
					final String newNameValue = (String) newName.getValue();
					final String newPath = parentPath + newNameValue;
					try {
						RenameResourceWindow.this.service.rename(oldPath,
								newPath, AppContext.getUsername());
						// reset layout
						FileDashboardComponent.this
								.displayResourcesInTable(FileDashboardComponent.this.baseFolder);

					} finally {
						RenameResourceWindow.this.close();
					}
				}
			});
			save.addStyleName(UIConstants.THEME_BLUE_LINK);

			UiUtils.addComponent(controlButton, save, Alignment.MIDDLE_CENTER);

			final Button cancel = new Button("Cancel", new ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(final ClickEvent event) {
					RenameResourceWindow.this.close();
				}
			});
			cancel.addStyleName(UIConstants.THEME_BLUE_LINK);
			UiUtils.addComponent(controlButton, cancel, Alignment.MIDDLE_CENTER);
			UiUtils.addComponent(contentLayout, controlButton,
					Alignment.MIDDLE_CENTER);

			this.setContent(contentLayout);
		}
	}

	class FileSearchPanel extends GenericSearchPanel<FileSearchCriteria> {
		private static final long serialVersionUID = 1L;
		protected FileSearchCriteria searchCriteria;
		private ComponentContainer menuBar = null;
		private HorizontalLayout basicSearchBody;

		public HorizontalLayout getBasicSearchBody() {
			return basicSearchBody;
		}

		public FileSearchPanel(final ComponentContainer menuBar) {
			this.menuBar = menuBar;
		}

		@Override
		public void attach() {
			super.attach();
			this.createBasicSearchLayout();
		}

		private void createBasicSearchLayout() {

			this.setCompositionRoot(new FileBasicSearchLayout());
		}

		private HorizontalLayout createSearchTopPanel() {
			final HorizontalLayout layout = new HorizontalLayout();
			layout.setWidth("100%");
			layout.setSpacing(true);
			layout.setMargin(true);

			final Image titleIcon = new Image(null, MyCollabResource
					.newResource("icons/24/project/file.png"));
			layout.addComponent(titleIcon);
			layout.setComponentAlignment(titleIcon, Alignment.MIDDLE_LEFT);

			final Label searchtitle = new Label("Files");
			searchtitle.setStyleName(Reindeer.LABEL_H2);
			layout.addComponent(searchtitle);
			layout.setComponentAlignment(searchtitle, Alignment.MIDDLE_LEFT);
			layout.setExpandRatio(searchtitle, 1.0f);

			if (this.menuBar != null) {
				UiUtils.addComponent(layout, this.menuBar,
						Alignment.MIDDLE_RIGHT);
			}

			return layout;
		}

		@SuppressWarnings("rawtypes")
		class FileBasicSearchLayout extends BasicSearchLayout {

			@SuppressWarnings("unchecked")
			public FileBasicSearchLayout() {
				super(FileSearchPanel.this);
			}

			private static final long serialVersionUID = 1L;
			private TextField nameField;
			private CheckBox myItemCheckbox;

			@Override
			public ComponentContainer constructHeader() {
				return FileSearchPanel.this.createSearchTopPanel();
			}

			@Override
			public ComponentContainer constructBody() {
				basicSearchBody = new HorizontalLayout();
				basicSearchBody.setSpacing(false);
				basicSearchBody.setMargin(true);

				this.nameField = this.createSeachSupportTextField(
						new TextField(), "NameFieldOfBasicSearch");

				this.nameField.setWidth(UIConstants.DEFAULT_CONTROL_WIDTH);
				UiUtils.addComponent(basicSearchBody, this.nameField,
						Alignment.MIDDLE_CENTER);

				final Button searchBtn = new Button();
				searchBtn.setStyleName("search-icon-button");
				searchBtn.setIcon(MyCollabResource
						.newResource("icons/16/search_white.png"));
				searchBtn.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						FileDashboardComponent.this
								.doSearch((FileSearchCriteria) fillupSearchCriteria());
					}
				});
				UiUtils.addComponent(basicSearchBody, searchBtn,
						Alignment.MIDDLE_LEFT);

				this.myItemCheckbox = new CheckBox("My Items");
				this.myItemCheckbox.setWidth("75px");
				UiUtils.addComponent(basicSearchBody, this.myItemCheckbox,
						Alignment.MIDDLE_CENTER);

				final Separator separator = new Separator();
				UiUtils.addComponent(basicSearchBody, separator,
						Alignment.MIDDLE_LEFT);

				final Button cancelBtn = new Button(
						LocalizationHelper
								.getMessage(GenericI18Enum.BUTTON_CLEAR));
				cancelBtn.setStyleName(UIConstants.THEME_LINK);
				cancelBtn.addStyleName("cancel-button");
				cancelBtn.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						FileBasicSearchLayout.this.nameField.setValue("");
					}
				});
				UiUtils.addComponent(basicSearchBody, cancelBtn,
						Alignment.MIDDLE_CENTER);
				return basicSearchBody;
			}

			@Override
			protected SearchCriteria fillupSearchCriteria() {
				FileSearchPanel.this.searchCriteria = new FileSearchCriteria();
				FileSearchPanel.this.searchCriteria.setRootFolder(rootPath);
				FileSearchPanel.this.searchCriteria.setFileName(this.nameField
						.getValue().toString().trim());
				FileSearchPanel.this.searchCriteria.setBaseFolder(baseFolder
						.getPath());

				return FileSearchPanel.this.searchCriteria;
			}
		}
	}

	public static abstract class AbstractMoveWindow extends Window {
		private static final long serialVersionUID = 1L;
		protected TreeTable folderTree;
		protected String rootPath;
		protected Folder baseFolder;
		private Resource resourceEditting;
		private final ResourceService resourceService;
		private final ExternalResourceService externalResourceService;
		private final ExternalDriveService externalDriveService;
		protected List<Resource> lstResEditting;
		private final ResourceMover resourceMover;
		private final boolean isNeedLoadExternalDrive;

		public AbstractMoveWindow(Resource resource,
				boolean isNeedLoadExternalDrive) {
			super("Move File/Foler");
			center();
			this.setWidth("600px");
			this.resourceEditting = resource;
			this.resourceService = ApplicationContextUtil
					.getSpringBean(ResourceService.class);
			this.externalResourceService = ApplicationContextUtil
					.getSpringBean(ExternalResourceService.class);
			this.externalDriveService = ApplicationContextUtil
					.getSpringBean(ExternalDriveService.class);
			this.isNeedLoadExternalDrive = isNeedLoadExternalDrive;
			this.resourceMover = ApplicationContextUtil
					.getSpringBean(ResourceMover.class);
			constructBody();
		}

		public AbstractMoveWindow(List<Resource> lstRes,
				boolean isNeedLoadExternalDrive) {
			super("Move File/Foler");
			center();
			this.setWidth("600px");
			this.lstResEditting = lstRes;
			this.resourceService = ApplicationContextUtil
					.getSpringBean(ResourceService.class);
			this.externalResourceService = ApplicationContextUtil
					.getSpringBean(ExternalResourceService.class);
			this.externalDriveService = ApplicationContextUtil
					.getSpringBean(ExternalDriveService.class);
			this.resourceMover = ApplicationContextUtil
					.getSpringBean(ResourceMover.class);
			this.isNeedLoadExternalDrive = isNeedLoadExternalDrive;

			constructBody();
		}

		private void constructBody() {
			VerticalLayout contentLayout = new VerticalLayout();
			contentLayout.setSpacing(true);
            contentLayout.setMargin(true);
			this.setContent(contentLayout);

			final HorizontalLayout resourceContainer = new HorizontalLayout();
			resourceContainer.setSizeFull();

			this.folderTree = new TreeTable();
			this.folderTree.setMultiSelect(false);
			this.folderTree.setSelectable(true);
			this.folderTree.setImmediate(true);
			this.folderTree.addContainerProperty("Name", String.class, "");
			this.folderTree.addContainerProperty("Date Modified", String.class,
					"");
			this.folderTree.setColumnWidth("Date Modified",
					UIConstants.TABLE_DATE_TIME_WIDTH);
			this.folderTree.setColumnExpandRatio("Name", 1.0f);
			this.folderTree.setWidth("100%");

			resourceContainer.addComponent(this.folderTree);

			this.folderTree.addExpandListener(new Tree.ExpandListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void nodeExpand(final ExpandEvent event) {
					final Folder expandFolder = (Folder) event.getItemId();
					// load externalResource if currentExpandFolder is
					// rootFolder
					if (rootPath.equals(expandFolder.getPath())
							&& AbstractMoveWindow.this.isNeedLoadExternalDrive) {
						List<ExternalDrive> externalDrives = externalDriveService
								.getExternalDrivesOfUser(AppContext
										.getUsername());
						for (ExternalDrive externalDrive : externalDrives) {
							ExternalFolder externalMapFolder = new ExternalFolder();
							externalMapFolder.setStorageName(externalDrive
									.getStoragename());
							externalMapFolder.setExternalDrive(externalDrive);
							externalMapFolder.setPath("/");
							externalMapFolder.setName(externalDrive
									.getFoldername());

							Calendar cal = GregorianCalendar.getInstance();
							cal.setTime(externalDrive.getCreatedtime());

							externalMapFolder.setCreated(cal);
							expandFolder.addChild(externalMapFolder);

							AbstractMoveWindow.this.folderTree.addItem(
									new Object[] {
											externalMapFolder.getName(),
											AppContext
													.formatDateTime(externalMapFolder
															.getCreated()
															.getTime()) },
									externalMapFolder);

							AbstractMoveWindow.this.folderTree.setItemIcon(
									externalMapFolder,
									MyCollabResource
											.newResource("icons/16/ecm/dropbox.png"));
							AbstractMoveWindow.this.folderTree.setItemCaption(
									externalMapFolder,
									externalMapFolder.getName());
							AbstractMoveWindow.this.folderTree.setParent(
									externalMapFolder, expandFolder);
						}
					}
					if (expandFolder instanceof ExternalFolder) {
						List<ExternalFolder> subFolders = externalResourceService
								.getSubFolders(((ExternalFolder) expandFolder)
										.getExternalDrive(), expandFolder
										.getPath());
						for (final Folder subFolder : subFolders) {
							expandFolder.addChild(subFolder);
							Date dateTime = ((ExternalFolder) subFolder)
									.getExternalDrive().getCreatedtime();

							AbstractMoveWindow.this.folderTree.addItem(
									new Object[] { subFolder.getName(),
											AppContext.formatDateTime(dateTime) },
									subFolder);

							AbstractMoveWindow.this.folderTree.setItemIcon(
									subFolder,
									MyCollabResource
											.newResource("icons/16/ecm/dropbox_subfolder.png"));
							AbstractMoveWindow.this.folderTree.setItemCaption(
									subFolder, subFolder.getName());
							AbstractMoveWindow.this.folderTree.setParent(
									subFolder, expandFolder);
						}
					} else {
						final List<Folder> subFolders = AbstractMoveWindow.this.resourceService
								.getSubFolders(expandFolder.getPath());

						AbstractMoveWindow.this.folderTree.setItemIcon(
								expandFolder,
								MyCollabResource
										.newResource("icons/16/ecm/folder_open.png"));

						if (subFolders != null) {
							for (final Folder subFolder : subFolders) {
								expandFolder.addChild(subFolder);
								AbstractMoveWindow.this.folderTree.addItem(
										new Object[] {
												subFolder.getName(),
												AppContext
														.formatDateTime(subFolder
																.getCreated()
																.getTime()) },
										subFolder);

								AbstractMoveWindow.this.folderTree.setItemIcon(
										subFolder,
										MyCollabResource
												.newResource("icons/16/ecm/folder_close.png"));
								AbstractMoveWindow.this.folderTree
										.setItemCaption(subFolder,
												subFolder.getName());
								AbstractMoveWindow.this.folderTree.setParent(
										subFolder, expandFolder);
							}
						}
					}
				}
			});

			this.folderTree.addCollapseListener(new Tree.CollapseListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void nodeCollapse(final CollapseEvent event) {
					final Folder collapseFolder = (Folder) event.getItemId();
					if (collapseFolder instanceof ExternalFolder) {
						if (collapseFolder.getPath().equals("/"))
							AbstractMoveWindow.this.folderTree.setItemIcon(
									collapseFolder,
									MyCollabResource
											.newResource("icons/16/ecm/dropbox.png"));
						else
							AbstractMoveWindow.this.folderTree.setItemIcon(
									collapseFolder,
									MyCollabResource
											.newResource("icons/16/ecm/dropbox_subfolder.png"));
					} else {
						AbstractMoveWindow.this.folderTree.setItemIcon(
								collapseFolder,
								MyCollabResource
										.newResource("icons/16/ecm/folder_close.png"));
					}
					for (Folder folder : collapseFolder.getChilds()) {
						recursiveRemoveSubItem(folder);
					}
				}

				private void recursiveRemoveSubItem(Folder collapseFolder) {
					List<Folder> childs = collapseFolder.getChilds();
					if (childs.size() > 0) {
						for (final Folder subFolder : childs) {
							recursiveRemoveSubItem(subFolder);
						}
						AbstractMoveWindow.this.folderTree
								.removeItem(collapseFolder);
					} else {
						AbstractMoveWindow.this.folderTree
								.removeItem(collapseFolder);
					}
				}
			});

			this.folderTree
					.addItemClickListener(new ItemClickEvent.ItemClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void itemClick(final ItemClickEvent event) {
							AbstractMoveWindow.this.baseFolder = (Folder) event
									.getItemId();
						}
					});

			contentLayout.addComponent(resourceContainer);
			displayFiles();

			HorizontalLayout controlGroupBtnLayout = new HorizontalLayout();
			controlGroupBtnLayout.setSpacing(true);

			Button moveBtn = new Button("Move", new ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					if (resourceEditting != null) {
						try {
							resourceMover.moveResource(resourceEditting,
									baseFolder, AppContext.getUsername(),
									AppContext.getAccountId());

							displayAfterMoveSuccess(
									AbstractMoveWindow.this.baseFolder, false);
						} finally {
							AbstractMoveWindow.this.close();
						}
					} else if (lstResEditting != null
							&& lstResEditting.size() > 0) {
						boolean checkingFail = false;
						for (Resource res : lstResEditting) {
							try {
								resourceMover.moveResource(res, baseFolder,
										AppContext.getUsername(),
										AppContext.getAccountId());
							} catch (Exception e) {
								checkingFail = true;
							}
						}
						AbstractMoveWindow.this.close();
						displayAfterMoveSuccess(
								AbstractMoveWindow.this.baseFolder,
								checkingFail);
					}
				}

			});
			moveBtn.addStyleName(UIConstants.THEME_BLUE_LINK);
			controlGroupBtnLayout.addComponent(moveBtn);
			Button cancelBtn = new Button("Cancel", new ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					AbstractMoveWindow.this.close();
				}
			});
			cancelBtn.addStyleName(UIConstants.THEME_BLUE_LINK);
			controlGroupBtnLayout.addComponent(cancelBtn);

			UiUtils.addComponent(contentLayout, controlGroupBtnLayout,
					Alignment.MIDDLE_CENTER);
		}

		public abstract void displayAfterMoveSuccess(Folder folder,
				boolean checking);

		protected abstract void displayFiles();
	}

	protected class MoveResourceWindow extends AbstractMoveWindow {
		private static final long serialVersionUID = 1L;

		public MoveResourceWindow(Resource resource,
				ResourceService resourceService) {
			super(resource, false);
		}

		@Override
		public void displayAfterMoveSuccess(Folder folder, boolean checking) {
			FileDashboardComponent.this
					.displayResourcesInTable(FileDashboardComponent.this.baseFolder);
		}

		@Override
		protected void displayFiles() {
			String rootPath = FileDashboardComponent.this.rootPath;
			this.folderTree.removeAllItems();
			this.rootPath = rootPath;

			this.baseFolder = new Folder();
			baseFolder.setPath(this.rootPath);
			this.folderTree.addItem(new Object[] {
					FileDashboardComponent.this.rootFolderName, "" },
					baseFolder);
			this.folderTree.setItemCaption(baseFolder,
					FileDashboardComponent.this.rootFolderName);

			this.folderTree.setCollapsed(baseFolder, false);
		}
	}
}
