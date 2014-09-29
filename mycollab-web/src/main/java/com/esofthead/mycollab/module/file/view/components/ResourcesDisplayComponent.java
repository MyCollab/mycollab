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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.easyuploads.MultiFileUploadExt;
import org.vaadin.hene.popupbutton.PopupButton;
import org.vaadin.peter.buttongroup.ButtonGroup;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.eventmanager.ApplicationEvent;
import com.esofthead.mycollab.module.ecm.ResourceUtils;
import com.esofthead.mycollab.module.ecm.StorageNames;
import com.esofthead.mycollab.module.ecm.domain.Content;
import com.esofthead.mycollab.module.ecm.domain.ExternalContent;
import com.esofthead.mycollab.module.ecm.domain.ExternalDrive;
import com.esofthead.mycollab.module.ecm.domain.ExternalFolder;
import com.esofthead.mycollab.module.ecm.domain.Folder;
import com.esofthead.mycollab.module.ecm.domain.Resource;
import com.esofthead.mycollab.module.ecm.service.ExternalDriveService;
import com.esofthead.mycollab.module.ecm.service.ExternalResourceService;
import com.esofthead.mycollab.module.ecm.service.ResourceService;
import com.esofthead.mycollab.module.file.domain.criteria.FileSearchCriteria;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.SearchHandler;
import com.esofthead.mycollab.vaadin.resources.LazyStreamSource;
import com.esofthead.mycollab.vaadin.resources.OnDemandFileDownloader;
import com.esofthead.mycollab.vaadin.resources.StreamDownloadResourceUtil;
import com.esofthead.mycollab.vaadin.ui.AttachmentPanel;
import com.esofthead.mycollab.vaadin.ui.ButtonLink;
import com.esofthead.mycollab.vaadin.ui.ConfirmDialogExt;
import com.esofthead.mycollab.vaadin.ui.GridFormLayoutHelper;
import com.esofthead.mycollab.vaadin.ui.Hr;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.esofthead.mycollab.vaadin.ui.Separator;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.UiUtils;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.util.ReflectTools;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class ResourcesDisplayComponent extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	private static Logger log = LoggerFactory
			.getLogger(ResourcesDisplayComponent.class);

	private static final String illegalFileNamePattern = "[<>:&/\\|?*&]";

	private ResourceService resourceService;
	private ExternalResourceService externalResourceService;
	private ExternalDriveService externalDriveService;

	private HorizontalLayout controllGroupBtn;

	private FileBreadcrumb fileBreadCrumb;
	private ResourcesContainer resourcesContainer;
	private ResourcePagingNavigator pagingResourceWapper;

	private Folder baseFolder;
	private Folder rootFolder;
	private String rootFolderName;
	private String rootPath;

	private List<Resource> selectedResourcesList;

	public ResourcesDisplayComponent(final String rootPath,
			final Folder baseFolder) {
		this.baseFolder = baseFolder;
		this.rootPath = rootPath;
		this.rootFolder = baseFolder;
		externalResourceService = ApplicationContextUtil
				.getSpringBean(ExternalResourceService.class);
		externalDriveService = ApplicationContextUtil
				.getSpringBean(ExternalDriveService.class);
		resourceService = ApplicationContextUtil
				.getSpringBean(ResourceService.class);

		VerticalLayout mainBodyLayout = new VerticalLayout();
		mainBodyLayout.setSpacing(true);
		mainBodyLayout.addStyleName("box-no-border-left");

		// file bread Crum ---------------------
		fileBreadCrumb = new FileBreadcrumb(rootPath);
		mainBodyLayout.addComponent(fileBreadCrumb);

		// Construct controllGroupBtn
		controllGroupBtn = new HorizontalLayout();
		controllGroupBtn.setMargin(new MarginInfo(false, false, false, true));
		controllGroupBtn.setSpacing(true);
		controllGroupBtn.addStyleName(UIConstants.THEME_SMALL_PADDING);

		final Button selectAllBtn = new Button();
		selectAllBtn.addStyleName(UIConstants.THEME_BROWN_LINK);
		selectAllBtn.setIcon(MyCollabResource
				.newResource("icons/16/checkbox_empty.png"));
		selectAllBtn.setData(false);
		selectAllBtn.setImmediate(true);
		selectAllBtn.setDescription("Select all");

		selectAllBtn.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (!(Boolean) selectAllBtn.getData()) {
					selectAllBtn.setIcon(MyCollabResource
							.newResource("icons/16/checkbox.png"));
					selectAllBtn.setData(true);
					resourcesContainer.setAllValues(true);
				} else {
					selectAllBtn.setData(false);
					selectAllBtn.setIcon(MyCollabResource
							.newResource("icons/16/checkbox_empty.png"));
					resourcesContainer.setAllValues(false);
				}
			}
		});
		UiUtils.addComponent(controllGroupBtn, selectAllBtn,
				Alignment.MIDDLE_LEFT);

		Button goUpBtn = new Button("Up");
		goUpBtn.setIcon(MyCollabResource
				.newResource("icons/16/ecm/up_to_root.png"));

		goUpBtn.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				Folder parentFolder = null;
				if (ResourcesDisplayComponent.this.baseFolder instanceof ExternalFolder) {
					if (ResourcesDisplayComponent.this.baseFolder.getPath()
							.equals("/")) {
						parentFolder = rootFolder;
					} else {
						parentFolder = externalResourceService
								.getParentResourceFolder(
										((ExternalFolder) ResourcesDisplayComponent.this.baseFolder)
												.getExternalDrive(),
										ResourcesDisplayComponent.this.baseFolder
												.getPath());
					}
				} else if (!ResourcesDisplayComponent.this.baseFolder.getPath()
						.equals(ResourcesDisplayComponent.this.rootPath)) {
					parentFolder = resourceService
							.getParentFolder(ResourcesDisplayComponent.this.baseFolder
									.getPath());
				} else {
					parentFolder = rootFolder;
				}
				selectedResourcesList = new ArrayList<Resource>();
				resourcesContainer.constructBody(parentFolder);
				ResourcesDisplayComponent.this.baseFolder = parentFolder;
				fileBreadCrumb
						.gotoFolder(ResourcesDisplayComponent.this.baseFolder);
			}
		});
		goUpBtn.setDescription("Back to parent folder");
		goUpBtn.setStyleName(UIConstants.THEME_BROWN_LINK);
		goUpBtn.setDescription("Go up");
		UiUtils.addComponent(controllGroupBtn, goUpBtn, Alignment.MIDDLE_LEFT);

		ButtonGroup navButton = new ButtonGroup();
		navButton.addStyleName(UIConstants.THEME_BROWN_LINK);
		Button createBtn = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_CREATE_LABEL),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						AddNewFolderWindow addnewFolderWindow = new AddNewFolderWindow();
						UI.getCurrent().addWindow(addnewFolderWindow);
					}
				});
		createBtn.setIcon(MyCollabResource.newResource("icons/16/ecm/add.png"));
		createBtn.addStyleName(UIConstants.THEME_BROWN_LINK);
		createBtn.setDescription("Create new folder");
		createBtn.setEnabled(AppContext
				.canWrite(RolePermissionCollections.PUBLIC_DOCUMENT_ACCESS));
		navButton.addButton(createBtn);

		Button uploadBtn = new Button("Upload", new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				MultiUploadContentWindow multiUploadWindow = new MultiUploadContentWindow();
				UI.getCurrent().addWindow(multiUploadWindow);
			}
		});
		uploadBtn.setIcon(MyCollabResource
				.newResource("icons/16/ecm/upload.png"));
		uploadBtn.addStyleName(UIConstants.THEME_BROWN_LINK);
		uploadBtn.setDescription("Upload");

		uploadBtn.setEnabled(AppContext
				.canWrite(RolePermissionCollections.PUBLIC_DOCUMENT_ACCESS));
		navButton.addButton(uploadBtn);

		Button downloadBtn = new Button("Download");

		LazyStreamSource streamSource = new LazyStreamSource() {
			private static final long serialVersionUID = 1L;

			@Override
			protected StreamSource buildStreamSource() {
				return StreamDownloadResourceUtil
						.getStreamSourceSupportExtDrive(selectedResourcesList);
			}

			@Override
			public String getFilename() {
				return StreamDownloadResourceUtil
						.getDownloadFileName(selectedResourcesList);
			}
		};
		OnDemandFileDownloader downloaderExt = new OnDemandFileDownloader(
				streamSource);
		downloaderExt.extend(downloadBtn);

		downloadBtn.setIcon(MyCollabResource
				.newResource("icons/16/ecm/download.png"));
		downloadBtn.addStyleName(UIConstants.THEME_BROWN_LINK);
		downloadBtn.setDescription("Download");
		downloadBtn.setEnabled(AppContext
				.canRead(RolePermissionCollections.PUBLIC_DOCUMENT_ACCESS));
		navButton.addButton(downloadBtn);

		Button moveToBtn = new Button("Move", new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (selectedResourcesList.size() > 0) {
					MoveResourceWindow moveResourceWindow = new MoveResourceWindow(
							selectedResourcesList);
					UI.getCurrent().addWindow(moveResourceWindow);
				} else {
					NotificationUtil
							.showWarningNotification("Please select item to move");
				}
			}
		});
		moveToBtn.setIcon(MyCollabResource
				.newResource("icons/16/ecm/move_up.png"));
		moveToBtn.addStyleName(UIConstants.THEME_BROWN_LINK);
		moveToBtn.setEnabled(AppContext
				.canWrite(RolePermissionCollections.PUBLIC_DOCUMENT_ACCESS));
		moveToBtn.setDescription("Move to");
		navButton.addButton(moveToBtn);

		Button deleteBtn = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_DELETE_LABEL),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						if (selectedResourcesList.size() == 0) {
							NotificationUtil
									.showWarningNotification("Please select at least one item to delete");
						} else {
							deleteResourceAction();
						}
					}
				});
		deleteBtn.setIcon(MyCollabResource
				.newResource("icons/16/ecm/delete.png"));
		deleteBtn.addStyleName(UIConstants.THEME_RED_LINK);
		deleteBtn.setImmediate(true);
		deleteBtn.setDescription("Delele");
		deleteBtn.setEnabled(AppContext
				.canAccess(RolePermissionCollections.PUBLIC_DOCUMENT_ACCESS));

		navButton.addButton(deleteBtn);
		controllGroupBtn.addComponent(navButton);

		mainBodyLayout.addComponent(controllGroupBtn);

		resourcesContainer = new ResourcesContainer(baseFolder, resourceService);
		resourcesContainer.setWidth("100%");

		mainBodyLayout.addComponent(resourcesContainer);
		this.addComponent(mainBodyLayout);
	}

	/**
	 * this method show Component when start loading
	 * 
	 * @param baseFolder
	 */
	public void displayComponent(Folder baseFolder, String rootPath,
			String rootFolderName) {
		this.baseFolder = baseFolder;
		this.rootFolder = baseFolder;
		this.rootPath = rootPath;
		this.rootFolderName = rootFolderName;
		this.fileBreadCrumb.initBreadcrumb();
		this.resourcesContainer.constructBody(this.baseFolder);
	}

	private void deleteResourceAction() {
		ConfirmDialogExt.show(UI.getCurrent(), AppContext.getMessage(
				GenericI18Enum.DIALOG_DELETE_TITLE,
				SiteConfiguration.getSiteName()), AppContext
				.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
				AppContext.getMessage(GenericI18Enum.BUTTON_YES_LABEL),
				AppContext.getMessage(GenericI18Enum.BUTTON_NO_LABEL),
				new ConfirmDialog.Listener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClose(final ConfirmDialog dialog) {
						if (dialog.isConfirmed()) {
							if (CollectionUtils
									.isNotEmpty(selectedResourcesList)) {
								for (Resource res : selectedResourcesList) {
									if (res instanceof ExternalFolder
											|| res instanceof ExternalContent) {
										if (res instanceof ExternalFolder) {
											ResourcesDisplayComponent.this.externalResourceService
													.deleteResource(
															((ExternalFolder) res)
																	.getExternalDrive(),
															res.getPath());
										} else
											ResourcesDisplayComponent.this.externalResourceService
													.deleteResource(
															((ExternalContent) res)
																	.getExternalDrive(),
															res.getPath());
									} else {
										if (res instanceof Folder) {
											fireEvent(new ResourceRemovedEvent(
													ResourcesDisplayComponent.this,
													res));
										}
										ResourcesDisplayComponent.this.resourceService.removeResource(
												res.getPath(),
												AppContext.getUsername(),
												AppContext.getAccountId());
									}
								}

								resourcesContainer.constructBody(baseFolder);

								NotificationUtil
										.showNotification("Delete content successfully.");
								ResourcesDisplayComponent.this.selectedResourcesList = new ArrayList<Resource>();
							}
						}
					}
				});
	}

	public void constructBodyItemContainer(Folder folder) {
		this.selectedResourcesList.clear();
		this.baseFolder = folder;
		fileBreadCrumb.gotoFolder(folder);
		resourcesContainer.constructBody(folder);
	}

	public void constructBodyItemContainerSearchActionResult(
			List<Resource> lst, String criteria) {
		this.selectedResourcesList.clear();
	}

	public void addSearchHandlerToBreadCrumb(
			final SearchHandler<FileSearchCriteria> handler) {
		fileBreadCrumb.addSearchHandler(handler);
	}

	public void initBreadCrumb() {
		fileBreadCrumb.initBreadcrumb();
	}

	private class ResourcesContainer extends VerticalLayout {
		private static final long serialVersionUID = 1L;

		private final List<CheckBox> checkboxes;

		public ResourcesContainer(Folder folder, ResourceService resourceService) {
			selectedResourcesList = new ArrayList<Resource>();
			checkboxes = new ArrayList<CheckBox>();
			this.setMargin(true);
			this.setSpacing(false);
		}

		private void setAllValues(boolean value) {
			for (CheckBox checkbox : checkboxes) {
				checkbox.setValue(value);
			}
		}

		private void constructBody(Folder currentFolder) {
			this.removeAllComponents();
			this.addComponent(new Hr());

			List<Resource> lstResource;
			if (currentFolder instanceof ExternalFolder) {
				lstResource = externalResourceService.getResources(
						((ExternalFolder) currentFolder).getExternalDrive(),
						currentFolder.getPath());
			} else {
				lstResource = resourceService.getResources(currentFolder
						.getPath());
			}

			if (currentFolder.getPath().equals(rootPath)) {
				List<ExternalDrive> externalDrives = externalDriveService
						.getExternalDrivesOfUser(AppContext.getUsername());
				if (CollectionUtils.isNotEmpty(externalDrives)) {
					for (ExternalDrive drive : externalDrives) {
						if (StorageNames.DROPBOX.equals(drive.getStoragename())) {
							try {
								Resource res = externalResourceService
										.getCurrentResourceByPath(drive, "/");
								res.setName(drive.getFoldername());
								lstResource.add(0, res);
							} catch (Exception e) {
								log.error("Error while query resource", e);
							}
						} else {
							throw new MyCollabException(
									"Do not support any external drive different than Dropbox");
						}
					}
				}
			}

			if (CollectionUtils.isNotEmpty(lstResource)) {
				if (lstResource.size() <= ResourcePagingNavigator.pageItemNum) {
					for (Resource res : lstResource) {
						this.addComponent(buildResourceRowComp(res, false));
						this.addComponent(new Hr());
					}
				} else if (lstResource.size() > ResourcePagingNavigator.pageItemNum) {
					for (int i = 0; i < ResourcePagingNavigator.pageItemNum; i++) {
						Resource res = lstResource.get(i);
						this.addComponent(buildResourceRowComp(res, false));
						this.addComponent(new Hr());
					}
					pagingResourceWapper = new ResourcePagingNavigator(
							lstResource);
					pagingResourceWapper.setWidth("100%");
					this.addComponent(pagingResourceWapper);
					this.setComponentAlignment(pagingResourceWapper,
							Alignment.MIDDLE_CENTER);
				}
			}
		}

		// private void constructBodySearchActionResult(
		// List<Resource> lstResource, String criteria) {
		// isSearchAction = true;
		//
		// if (CollectionUtils.isEmpty(lstResource)) {
		// VerticalLayout bodyLayout = new VerticalLayout();
		// bodyLayout.setSpacing(true);
		// bodyLayout.setMargin(true);
		// bodyLayout.setWidth("100%");
		//
		// HorizontalLayout messageLayout = new HorizontalLayout();
		// messageLayout.setSpacing(true);
		// messageLayout.addComponent(new Label("Your search- "));
		// Label strSearchLabel = new Label(criteria);
		// strSearchLabel.addStyleName("h2");
		// messageLayout.addComponent(strSearchLabel);
		// messageLayout.addComponent(new Label(
		// " -did not match any documents."));
		// bodyLayout.addComponent(messageLayout);
		// bodyLayout.addComponent(new Label("Suggesstion:"));
		// bodyLayout.addComponent(new Label(
		// "-Make sure that all words are spelled correctly."));
		// bodyLayout.addComponent(new Label("-Try different keywords."));
		// bodyLayout
		// .addComponent(new Label("-Try more general keywords."));
		// bodyLayout.addComponent(new Label("-Try fewer keywords."));
		// this.addComponent(bodyLayout);
		// this.addComponent(new Hr());
		// return;
		// }
		//
		// HorizontalLayout messageSearchLayout = new HorizontalLayout();
		// messageSearchLayout.setWidth("100%");
		// Label titleLabel = new Label("Search result: ");
		// titleLabel.setWidth("115px");
		// titleLabel.addStyleName("h3");
		// messageSearchLayout.addComponent(titleLabel);
		//
		// Label nameLabel = new Label("Name");
		// nameLabel.addStyleName("h3");
		// nameLabel.setWidth("350px");
		// messageSearchLayout.addComponent(nameLabel);
		// Label pathLabel = new Label("Path");
		// pathLabel.addStyleName("h3");
		// messageSearchLayout.addComponent(pathLabel);
		// messageSearchLayout.setExpandRatio(pathLabel, 1.0f);
		//
		// this.addComponent(messageSearchLayout);
		// this.addComponent(new Hr());
		// if (lstResource != null && lstResource.size() > 0) {
		// for (Resource res : lstResource) {
		// this.addComponent(buildResourceRowComp(res, true));
		// this.addComponent(new Hr());
		// }
		// }
		// }

		private HorizontalLayout buildResourceRowComp(final Resource res,
				final boolean isSearchAction) {
			final HorizontalLayout layout = new HorizontalLayout();
			layout.setWidth("100%");
			layout.setSpacing(true);
			layout.addStyleName("resourceItem");
			layout.setHeight("44px");

			final CheckBox checkbox = new CheckBox();
			checkbox.setWidth("25px");
			checkbox.setImmediate(true);
			checkbox.setStyleName(UIConstants.THEME_ROUND_BUTTON);
			checkboxes.add(checkbox);

			checkbox.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					if (checkbox.getValue()) {
						selectedResourcesList.add(res);
					} else {
						selectedResourcesList.remove(res);
					}
				}
			});
			layout.addComponent(checkbox);
			layout.setComponentAlignment(checkbox, Alignment.MIDDLE_LEFT);

			layout.addLayoutClickListener(new LayoutClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void layoutClick(LayoutClickEvent event) {
					if (checkbox.getValue())
						checkbox.setValue(false);
					else
						checkbox.setValue(true);
				}
			});

			CssLayout resIconWapper = new CssLayout();
			final Embedded resourceIcon = new Embedded();
			if (res instanceof Folder)
				if (res instanceof ExternalFolder)
					resourceIcon.setSource(MyCollabResource
							.newResource("icons/32/ecm/folder_dropbox.png"));
				else
					resourceIcon.setSource(MyCollabResource
							.newResource("icons/32/ecm/folder.png"));
			else {
				if (res instanceof ExternalContent)
					resourceIcon.setSource(MyCollabResource
							.newResource("icons/32/ecm/file_dropbox.png"));
				else
					resourceIcon.setSource(MyCollabResource
							.newResource("icons/32/ecm/file.png"));
			}

			resIconWapper.setWidth("40px");
			resIconWapper.addComponent(resourceIcon);

			layout.addComponent(resIconWapper);
			layout.setComponentAlignment(resIconWapper, Alignment.MIDDLE_LEFT);

			VerticalLayout informationLayout = new VerticalLayout();
			informationLayout.setWidth("345px");

			Button resourceLinkBtn = new Button(res.getName(),
					new ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(ClickEvent event) {
							if (res instanceof Folder) {
								ResourcesDisplayComponent.this.baseFolder = (Folder) res;
								resourcesContainer.constructBody((Folder) res);
								ResourcesDisplayComponent.this.fileBreadCrumb
										.gotoFolder((Folder) res);
							} else {
								FileDownloadWindow fileDownloadWindow = new FileDownloadWindow(
										(Content) res);
								UI.getCurrent().addWindow(fileDownloadWindow);
							}
						}
					});
			resourceLinkBtn.addStyleName("link");
			resourceLinkBtn.addStyleName("h3");
			informationLayout.addComponent(resourceLinkBtn);

			HorizontalLayout moreInfoAboutResLayout = new HorizontalLayout();
			moreInfoAboutResLayout.setSpacing(true);

			// If resource is dropbox resource then we can not define the
			// created user so we do not need to display, then we assume the
			// current user is created user
			if (res.getCreatedBy() == null
					|| res.getCreatedBy().trim().equals("")) {
				Label usernameLbl = new Label(AppContext.getUsername());
				usernameLbl.addStyleName("grayLabel");
				moreInfoAboutResLayout.addComponent(usernameLbl);
			} else {
				Label usernameLbl = new Label(res.getCreatedBy());
				usernameLbl.addStyleName("grayLabel");
				moreInfoAboutResLayout.addComponent(usernameLbl);
			}
			moreInfoAboutResLayout.addComponent(new Separator());

			// If resource is dropbox resource then we can not define the
			// created date so we do not need to display\
			if (res.getCreated() != null) {
				Label createdTimeLbl = new Label((AppContext.formatDate(res
						.getCreated().getTime())));
				createdTimeLbl.addStyleName("grayLabel");
				moreInfoAboutResLayout.addComponent(createdTimeLbl);
			} else {
				Label createdTimeLbl = new Label("Undefined");
				createdTimeLbl.addStyleName("grayLabel");
				moreInfoAboutResLayout.addComponent(createdTimeLbl);
			}

			if (res instanceof Content) {
				moreInfoAboutResLayout.addComponent(new Separator());

				Label lbl = new Label(ResourceUtils.getVolumeDisplay(res
						.getSize()));
				lbl.addStyleName("grayLabel");
				moreInfoAboutResLayout.addComponent(lbl);
			}
			informationLayout.addComponent(moreInfoAboutResLayout);

			layout.addComponent(informationLayout);
			layout.setComponentAlignment(informationLayout,
					Alignment.MIDDLE_LEFT);

			layout.setExpandRatio(informationLayout, 1.0f);

			final PopupButton resourceSettingPopupBtn = new PopupButton();

			final VerticalLayout filterBtnLayout = new VerticalLayout();

			final Button renameBtn = new Button("Rename",
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(ClickEvent event) {
							resourceSettingPopupBtn.setPopupVisible(false);
							final RenameResourceWindow renameWindow = new RenameResourceWindow(
									res, resourceService);
							UI.getCurrent().addWindow(renameWindow);
						}
					});
			renameBtn.addStyleName("link");
			filterBtnLayout.addComponent(renameBtn);

			final Button downloadBtn = new Button("Download");

			LazyStreamSource streamsource = new LazyStreamSource() {
				private static final long serialVersionUID = 1L;

				@Override
				protected StreamSource buildStreamSource() {
					List<Resource> lstRes = new ArrayList<Resource>();
					lstRes.add(res);
					return StreamDownloadResourceUtil
							.getStreamSourceSupportExtDrive(lstRes);
				}

				@Override
				public String getFilename() {
					return res.getName();
				}
			};

			OnDemandFileDownloader downloaderExt = new OnDemandFileDownloader(
					streamsource);
			downloaderExt.extend(downloadBtn);

			downloadBtn.addStyleName("link");
			filterBtnLayout.addComponent(downloadBtn);

			final Button moveBtn = new Button("Move to",
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(ClickEvent event) {
							MoveResourceWindow moveResourceWindow = new MoveResourceWindow(
									res);
							UI.getCurrent().addWindow(moveResourceWindow);
						}
					});
			moveBtn.addStyleName("link");
			filterBtnLayout.addComponent(moveBtn);

			final Button deleteBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_DELETE_LABEL),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(ClickEvent event) {
							selectedResourcesList.clear();
							selectedResourcesList.add(res);

							ResourcesDisplayComponent.this
									.deleteResourceAction();
						}
					});
			deleteBtn.addStyleName("link");
			filterBtnLayout.addComponent(deleteBtn);
			// ------------------------------------------------------------------------

			filterBtnLayout.setMargin(true);
			filterBtnLayout.setSpacing(true);
			filterBtnLayout.setWidth("100px");
			resourceSettingPopupBtn.setIcon(MyCollabResource
					.newResource("icons/16/item_settings_big.png"));
			resourceSettingPopupBtn.setStyleName(UIConstants.THEME_BLANK_LINK);
			resourceSettingPopupBtn.setContent(filterBtnLayout);

			layout.addComponent(resourceSettingPopupBtn);
			layout.setComponentAlignment(resourceSettingPopupBtn,
					Alignment.MIDDLE_RIGHT);
			return layout;
		}
	}

	private class RenameResourceWindow extends Window {
		private static final long serialVersionUID = 1L;
		private final Resource resource;
		private final ResourceService service;

		public RenameResourceWindow(final Resource resource,
				final ResourceService service) {
			super("Rename folder/file");
			this.center();
			this.setResizable(false);
			this.setWidth("400px");

			this.service = service;
			this.resource = resource;
			this.constructBody();
		}

		private void constructBody() {
			final VerticalLayout layout = new VerticalLayout();
			layout.setMargin(new MarginInfo(false, true, true, true));
			final HorizontalLayout topRename = new HorizontalLayout();
			topRename.setSpacing(true);
			topRename.setMargin(true);

			final Label label = new Label("Enter new name: ");
			UiUtils.addComponent(topRename, label, Alignment.MIDDLE_CENTER);

			final TextField newName = new TextField();
			newName.setWidth("150px");
			UiUtils.addComponent(topRename, newName, Alignment.MIDDLE_CENTER);

			UiUtils.addComponent(layout, topRename, Alignment.MIDDLE_CENTER);

			final HorizontalLayout controlButtons = new HorizontalLayout();
			controlButtons.setSpacing(true);
			final Button saveBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_SAVE_LABEL),
					new ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							final String oldPath = RenameResourceWindow.this.resource
									.getPath();
							String parentPath = oldPath.substring(0,
									oldPath.lastIndexOf("/") + 1);
							if (resource instanceof ExternalFolder
									|| resource instanceof ExternalContent) {
								parentPath = (parentPath.length() == 0) ? "/"
										: parentPath;
							}
							String newNameValue = newName.getValue();
							String newPath = parentPath + newNameValue;

							if (resource instanceof ExternalFolder
									|| resource instanceof ExternalContent) {
								if (resource instanceof ExternalFolder)
									externalResourceService.rename(
											((ExternalFolder) resource)
													.getExternalDrive(),
											oldPath, newPath);
								else
									externalResourceService.rename(
											((ExternalContent) resource)
													.getExternalDrive(),
											oldPath, newPath);
							} else {

								RenameResourceWindow.this.service.rename(
										oldPath, newPath,
										AppContext.getUsername());
							}
							resourcesContainer.constructBody(baseFolder);

							if ((resource instanceof ExternalFolder || resource instanceof ExternalContent)
									&& (pagingResourceWapper != null && pagingResourceWapper
											.getCurrentPage() != 1))
								pagingResourceWapper
										.pageChange(pagingResourceWapper
												.getCurrentPage());
							RenameResourceWindow.this.close();
						}
					});
			saveBtn.addStyleName(UIConstants.THEME_GREEN_LINK);

			UiUtils.addComponent(controlButtons, saveBtn,
					Alignment.MIDDLE_CENTER);

			final Button cancel = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL_LABEL),
					new ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							RenameResourceWindow.this.close();
						}
					});
			cancel.addStyleName(UIConstants.THEME_GRAY_LINK);
			UiUtils.addComponent(controlButtons, cancel,
					Alignment.MIDDLE_CENTER);
			UiUtils.addComponent(layout, controlButtons,
					Alignment.MIDDLE_CENTER);

			this.setContent(layout);
		}
	}

	private class AddNewFolderWindow extends Window {
		private static final long serialVersionUID = 1L;

		private final TextField folderName;

		public AddNewFolderWindow() {
			this.setModal(true);
			this.setCaption("New Folder");
			this.setResizable(false);
			this.center();

			VerticalLayout contentLayout = new VerticalLayout();
			contentLayout.setMargin(true);
			this.setContent(contentLayout);

			final HorizontalLayout fileLayout = new HorizontalLayout();
			fileLayout.setSpacing(true);
			fileLayout.setSizeUndefined();
			final Label captionLbl = new Label("Enter folder name: ");
			fileLayout.addComponent(captionLbl);
			fileLayout.setComponentAlignment(captionLbl, Alignment.MIDDLE_LEFT);

			this.folderName = new TextField();
			fileLayout.addComponent(this.folderName);
			fileLayout.setComponentAlignment(this.folderName,
					Alignment.MIDDLE_LEFT);
			fileLayout.setExpandRatio(this.folderName, 1.0f);

			contentLayout.addComponent(fileLayout);
			contentLayout.setComponentAlignment(fileLayout,
					Alignment.MIDDLE_CENTER);

			final HorizontalLayout controlsLayout = new HorizontalLayout();
			controlsLayout.setSpacing(true);
			controlsLayout.setMargin(new MarginInfo(true, false, false, false));

			final Button saveBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_SAVE_LABEL),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {

							final String folderVal = AddNewFolderWindow.this.folderName
									.getValue();

							if (folderVal != null
									&& !folderVal.trim().equals("")) {
								Pattern pattern = Pattern
										.compile(illegalFileNamePattern);
								Matcher matcher = pattern.matcher(folderVal);
								if (matcher.find()) {
									NotificationUtil
											.showWarningNotification("Please enter valid folder name except any follow characters : <>:&/\\|?*&");
									return;
								}

								String baseFolderPath = (baseFolder == null) ? rootPath
										: baseFolder.getPath();

								if (baseFolder instanceof ExternalFolder) {
									String path = baseFolder.getPath() + "/"
											+ folderVal;
									externalResourceService.createFolder(
											((ExternalFolder) baseFolder)
													.getExternalDrive(), path);
								} else {
									resourceService.createNewFolder(
											baseFolderPath, folderVal,
											AppContext.getUsername());
								}
								resourcesContainer.constructBody(baseFolder);
								AddNewFolderWindow.this.close();
							}
						}
					});
			saveBtn.addStyleName(UIConstants.THEME_GREEN_LINK);
			saveBtn.setIcon(MyCollabResource.newResource("icons/16/save.png"));
			controlsLayout.addComponent(saveBtn);

			final Button cancelBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL_LABEL),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							AddNewFolderWindow.this.close();
						}
					});
			cancelBtn.addStyleName(UIConstants.THEME_GRAY_LINK);
			controlsLayout.addComponent(cancelBtn);
			controlsLayout.setComponentAlignment(cancelBtn,
					Alignment.MIDDLE_RIGHT);

			contentLayout.addComponent(controlsLayout);
			contentLayout.setComponentAlignment(controlsLayout,
					Alignment.MIDDLE_CENTER);
		}
	}

	private class MultiUploadContentWindow extends Window {
		private static final long serialVersionUID = 1L;

		private final GridFormLayoutHelper layoutHelper;
		private final MultiFileUploadExt multiFileUploadExt;

		public MultiUploadContentWindow() {
			super("Multi Upload Content");
			this.setWidth("500px");
			this.setResizable(false);

			VerticalLayout contentLayout = new VerticalLayout();
			contentLayout.setMargin(new MarginInfo(false, false, true, false));
			this.setContent(contentLayout);
			this.setModal(true);
			final AttachmentPanel attachments = new AttachmentPanel();

			this.layoutHelper = new GridFormLayoutHelper(1, 2, "100%", "167px",
					Alignment.TOP_LEFT);

			multiFileUploadExt = new MultiFileUploadExt(attachments);
			multiFileUploadExt.addComponent(attachments);
			multiFileUploadExt.setWidth("100%");

			this.layoutHelper.addComponent(multiFileUploadExt, "File", 0, 0);

			this.layoutHelper.getLayout().setWidth("100%");
			this.layoutHelper.getLayout().setMargin(false);
			this.layoutHelper.getLayout().addStyleName("colored-gridlayout");
			contentLayout.addComponent(this.layoutHelper.getLayout());

			final HorizontalLayout controlsLayout = new HorizontalLayout();
			controlsLayout.setSpacing(true);
			controlsLayout.setMargin(new MarginInfo(true, false, false, false));

			final Button uploadBtn = new Button("Upload",
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							List<File> lstFileAttachments = attachments
									.getListFile();
							if (lstFileAttachments != null
									&& lstFileAttachments.size() > 0) {
								for (File file : lstFileAttachments) {
									try {
										if (StringUtils.isNotBlank(file
												.getName())) {
											Pattern pattern = Pattern
													.compile(illegalFileNamePattern);
											Matcher matcher = pattern
													.matcher(file.getName());
											if (matcher.find()) {
												NotificationUtil
														.showWarningNotification("Please upload valid file-name except any follow characters : <>:&/\\|?*&");
												return;
											}
										}
										final Content content = new Content(
												baseFolder.getPath() + "/"
														+ file.getName());
										content.setSize(file.length());
										FileInputStream fileInputStream = new FileInputStream(
												file);

										if (baseFolder instanceof ExternalFolder) {
											externalResourceService
													.saveContent(
															((ExternalFolder) baseFolder)
																	.getExternalDrive(),
															content,
															fileInputStream);
										} else
											resourceService.saveContent(
													content,
													AppContext.getUsername(),
													fileInputStream,
													AppContext.getAccountId());
									} catch (IOException e) {
										throw new MyCollabException(e);
									}
								}
								resourcesContainer.constructBody(baseFolder);
								MultiUploadContentWindow.this.close();
								NotificationUtil
										.showNotification("Upload successfully.");
							} else {
								NotificationUtil
										.showNotification("It seems you did not attach file yet!");
							}
						}
					});
			uploadBtn.addStyleName(UIConstants.THEME_GREEN_LINK);
			controlsLayout.addComponent(uploadBtn);

			final Button cancelBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL_LABEL),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							MultiUploadContentWindow.this.close();
						}
					});
			cancelBtn.addStyleName(UIConstants.THEME_GRAY_LINK);
			controlsLayout.addComponent(cancelBtn);
			controlsLayout.setComponentAlignment(cancelBtn,
					Alignment.MIDDLE_RIGHT);

			contentLayout.addComponent(controlsLayout);
			contentLayout.setComponentAlignment(controlsLayout,
					Alignment.MIDDLE_CENTER);
		}

	}

	private class ResourcePagingNavigator extends CssLayout {
		private static final long serialVersionUID = 1L;
		private final int totalItem;
		public static final int pageItemNum = 15;
		private int currentPage;
		private final CssLayout controlBarWrapper;
		private final HorizontalLayout pageManagement;
		private final int totalPage;
		private final List<Resource> lstResource;
		private Button currentBtn;
		private final HorizontalLayout controlBar;

		public ResourcePagingNavigator(List<Resource> lstResource) {
			this.totalItem = lstResource.size();
			this.currentPage = 1;
			this.totalPage = (totalItem / pageItemNum) + 1;
			this.lstResource = lstResource;

			// defined layout here ---------------------------
			this.controlBarWrapper = new CssLayout();
			this.controlBarWrapper.setStyleName("listControl");
			this.controlBarWrapper.setWidth("100%");

			controlBar = new HorizontalLayout();
			controlBar.setWidth("100%");
			this.controlBarWrapper.addComponent(controlBar);

			this.pageManagement = new HorizontalLayout();
			createPageControls();
		}

		private void createPageControls() {
			this.pageManagement.removeAllComponents();
			if (this.currentPage > 1) {
				final Button firstLink = new ButtonLink("1",
						new ClickListener() {
							private static final long serialVersionUID = 1L;

							@Override
							public void buttonClick(final ClickEvent event) {
								pageChange(1);
							}
						}, false);
				firstLink.addStyleName("buttonPaging");
				this.pageManagement.addComponent(firstLink);
			}
			if (this.currentPage >= 5) {
				final Label ss1 = new Label("...");
				ss1.addStyleName("buttonPaging");
				this.pageManagement.addComponent(ss1);
			}
			if (this.currentPage > 3) {
				final Button previous2 = new ButtonLink(""
						+ (this.currentPage - 2), new ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						pageChange(currentPage - 2);
					}
				}, false);
				previous2.addStyleName("buttonPaging");
				this.pageManagement.addComponent(previous2);
			}
			if (this.currentPage > 2) {
				final Button previous1 = new ButtonLink(""
						+ (this.currentPage - 1), new ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						pageChange(currentPage - 1);
					}
				}, false);
				previous1.addStyleName("buttonPaging");
				this.pageManagement.addComponent(previous1);
			}
			// Here add current ButtonLink
			currentBtn = new ButtonLink("" + this.currentPage,
					new ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							// pageChange(currentPage);
						}
					}, false);
			currentBtn.addStyleName("buttonPaging");
			currentBtn.addStyleName("buttonPagingcurrent");

			this.pageManagement.addComponent(currentBtn);
			final int range = this.totalPage - this.currentPage;
			if (range >= 1) {
				final Button next1 = new ButtonLink(
						"" + (this.currentPage + 1), new ClickListener() {
							private static final long serialVersionUID = 1L;

							@Override
							public void buttonClick(final ClickEvent event) {
								pageChange(currentPage + 1);
							}
						}, false);
				next1.addStyleName("buttonPaging");
				this.pageManagement.addComponent(next1);
			}
			if (range >= 2) {
				final Button next2 = new ButtonLink(
						"" + (this.currentPage + 2), new ClickListener() {
							private static final long serialVersionUID = 1L;

							@Override
							public void buttonClick(final ClickEvent event) {
								pageChange(currentPage + 2);
							}
						}, false);
				next2.addStyleName("buttonPaging");
				this.pageManagement.addComponent(next2);
			}
			if (range >= 4) {
				final Label ss2 = new Label("...");
				ss2.addStyleName("buttonPaging");
				this.pageManagement.addComponent(ss2);
			}
			if (range >= 3) {
				final Button last = new ButtonLink("" + this.totalPage,
						new ClickListener() {
							private static final long serialVersionUID = 1L;

							@Override
							public void buttonClick(final ClickEvent event) {
								pageChange(totalPage);
							}
						}, false);
				last.addStyleName("buttonPaging");
				this.pageManagement.addComponent(last);
			}

			this.pageManagement.setWidth(null);
			this.pageManagement.setSpacing(true);
			controlBar.addComponent(this.pageManagement);
			controlBar.setComponentAlignment(this.pageManagement,
					Alignment.MIDDLE_RIGHT);
			this.addComponent(controlBarWrapper);
		}

		public void pageChange(int currentPage) {
			this.currentPage = currentPage;
			resourcesContainer.removeAllComponents();
			int index = currentPage - 1;
			int start = (index == 0) ? index : index * pageItemNum;
			int end = ((start + pageItemNum) > lstResource.size()) ? lstResource
					.size() : start + pageItemNum;

			for (int i = start; i < end; i++) {
				Resource res = lstResource.get(i);
				resourcesContainer.addComponent(resourcesContainer
						.buildResourceRowComp(res, false));
				resourcesContainer.addComponent(new Hr());
			}
			createPageControls();
			resourcesContainer.addComponent(this);
		}

		public int getCurrentPage() {
			return currentPage;
		}
	}

	private class MoveResourceWindow extends AbstractResourceMovingWindow {
		private static final long serialVersionUID = 1L;

		public MoveResourceWindow(Resource resource) {
			super(resource);
		}

		public MoveResourceWindow(List<Resource> lstResource) {
			super(lstResource);
		}

		@Override
		public void displayAfterMoveSuccess(Folder folder, boolean checking) {
			fileBreadCrumb.gotoFolder(folder);
			resourcesContainer.constructBody(folder);
			if (!checking) {
				NotificationUtil
						.showNotification("Move asset(s) successfully.");
			} else {
				NotificationUtil
						.showNotification("Move finish, some items can't move to destination. Please check duplicated file-name and try again.");
			}
			ResourcesDisplayComponent.this.selectedResourcesList = new ArrayList<Resource>();
		}

		@Override
		protected void displayFiles() {
			this.folderTree.removeAllItems();

			this.baseFolder = new Folder(
					ResourcesDisplayComponent.this.rootPath);
			this.rootPath = ResourcesDisplayComponent.this.rootPath;
			this.folderTree.addItem(new Object[] {
					ResourcesDisplayComponent.this.rootFolderName, "" },
					this.baseFolder);
			this.folderTree.setItemCaption(this.baseFolder,
					ResourcesDisplayComponent.this.rootFolderName);

			this.folderTree.setCollapsed(this.baseFolder, false);
		}
	}

	public void addResourceRemovedListener(ResourceRemovedListener listener) {
		this.addListener(ResourceRemovedEvent.VIEW_IDENTIFIER,
				ResourceRemovedEvent.class, listener,
				ResourceRemovedListener.viewInitMethod);
	}

	public static interface ResourceRemovedListener extends EventListener,
			Serializable {
		public static final Method viewInitMethod = ReflectTools.findMethod(
				ResourceRemovedListener.class, "removedResource",
				ResourceRemovedEvent.class);

		void removedResource(ResourceRemovedEvent event);
	}

	public static class ResourceRemovedEvent extends ApplicationEvent {
		private static final long serialVersionUID = 1L;

		public static final String VIEW_IDENTIFIER = "resourceRemoved";

		public ResourceRemovedEvent(Object source, Resource data) {
			super(source, data);
		}
	}

}
