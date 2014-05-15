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

import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.hene.popupbutton.PopupButton;

import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.module.ecm.ResourceUtils;
import com.esofthead.mycollab.module.ecm.domain.Content;
import com.esofthead.mycollab.module.ecm.domain.Resource;
import com.esofthead.mycollab.module.ecm.service.ResourceService;
import com.esofthead.mycollab.module.file.resource.StreamDownloadResourceUtil;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.ButtonLink;
import com.esofthead.mycollab.vaadin.ui.ConfirmDialogExt;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.UiUtils;
import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public abstract class FileSearchResultComponent extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	private final Label searchHeader;
	private final VerticalLayout bodyLayout;
	private final ResourceService resourceService;
	private final ResourceTableDisplay resourceTable;

	private String rootFolder;
	private String basePath;
	private String searchString;

	public FileSearchResultComponent() {
		this.resourceService = ApplicationContextUtil
				.getSpringBean(ResourceService.class);

		final CssLayout headerWrapper = new CssLayout();
		headerWrapper.setWidth("100%");
		headerWrapper.addStyleName(UIConstants.THEME_COMP_HEADER);

		final HorizontalLayout headerLayout = new HorizontalLayout();
		headerLayout.setWidth("100%");
		headerLayout.setSpacing(true);

		headerWrapper.addComponent(headerLayout);
		this.addComponent(headerWrapper);

		final Embedded headerIcon = new Embedded();
		headerIcon.setSource(MyCollabResource
				.newResource("icons/16/search.png"));
		headerLayout.addComponent(headerIcon);
		headerLayout.setComponentAlignment(headerIcon, Alignment.MIDDLE_LEFT);

		this.searchHeader = new Label();
		headerLayout.addComponent(this.searchHeader);
		headerLayout.setComponentAlignment(this.searchHeader,
				Alignment.MIDDLE_LEFT);
		headerLayout.setExpandRatio(this.searchHeader, 1.0f);

		final Button backButton = new Button("Back to dashboard",
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						backView();
					}
				});
		backButton.addStyleName(UIConstants.THEME_GREEN_LINK);
		headerLayout.addComponent(backButton);

		this.resourceTable = new ResourceTableDisplay();
		this.resourceTable.setWidth("100%");

		this.bodyLayout = new VerticalLayout();
		this.bodyLayout.addComponent(this.resourceTable);
		this.addComponent(this.bodyLayout);

	}

	abstract protected void backView();

	public void displaySearchResult(final String rootFolder,
			final String basePath, final String name) {
		this.rootFolder = rootFolder;
		this.basePath = basePath;
		this.searchString = name;

		final String header = "Search result of '%s'";
		this.searchHeader.setValue(String.format(header, name));

		final List<Resource> resourceList = this.resourceService
				.searchResourcesByName(basePath, name);

		this.resourceTable.refreshRowCache();

		this.resourceTable
				.setContainerDataSource(new BeanItemContainer<Resource>(
						Resource.class, resourceList));
		this.resourceTable.setVisibleColumns(new String[] { "uuid",
				"createdUser", "path", "size", "created" });
		this.resourceTable.setColumnHeaders(new String[] { "", "Name",
				"Enclosing Folder", "Size (Kb)", "Created" });
	}

	@SuppressWarnings("serial")
	private class ResourceTableDisplay extends Table {
		public ResourceTableDisplay() {

			this.addGeneratedColumn("uuid", new Table.ColumnGenerator() {
				private static final long serialVersionUID = 1L;

				@Override
				public Object generateCell(final Table source,
						final Object itemId, final Object columnId) {

					final Resource resource = ResourceTableDisplay.this
							.getResource(itemId);

					final PopupButton resourceSettingPopupBtn = new PopupButton();
					final VerticalLayout filterBtnLayout = new VerticalLayout();

					final Button renameBtn = new Button("Rename",
							new Button.ClickListener() {

								@Override
								public void buttonClick(final ClickEvent event) {
									resourceSettingPopupBtn
											.setPopupVisible(false);
									final RenameResourceWindow renameWindow = new RenameResourceWindow(
											resource,
											FileSearchResultComponent.this.resourceService);
									UI.getCurrent().addWindow(renameWindow);
								}
							});
					renameBtn.setStyleName("link");
					filterBtnLayout.addComponent(renameBtn);

					final Button downloadBtn = new Button("Download");
					List<Resource> lstRes = new ArrayList<Resource>();
					lstRes.add(resource);

					final StreamResource downloadResource = StreamDownloadResourceUtil
							.getStreamResourceSupportExtDrive(lstRes, false);
					FileDownloader fileDownloader = new FileDownloader(
							downloadResource);
					fileDownloader.extend(downloadBtn);

					downloadBtn.setStyleName("link");
					filterBtnLayout.addComponent(downloadBtn);

					final Button deleteBtn = new Button(AppContext
							.getMessage(GenericI18Enum.BUTTON_DELETE_LABEL),
							new Button.ClickListener() {

								@Override
								public void buttonClick(final ClickEvent event) {
									resourceSettingPopupBtn
											.setPopupVisible(false);
									ConfirmDialogExt.show(
											UI.getCurrent(),
											AppContext
													.getMessage(
															GenericI18Enum.DELETE_DIALOG_TITLE,
															SiteConfiguration
																	.getSiteName()),
											AppContext
													.getMessage(GenericI18Enum.DELETE_SINGLE_ITEM_DIALOG_MESSAGE),
											AppContext
													.getMessage(GenericI18Enum.BUTTON_YES_LABEL),
											AppContext
													.getMessage(GenericI18Enum.BUTTON_NO_LABEL),
											new ConfirmDialog.Listener() {
												private static final long serialVersionUID = 1L;

												@Override
												public void onClose(
														final ConfirmDialog dialog) {
													if (dialog.isConfirmed()) {
														FileSearchResultComponent.this.resourceService
																.removeResource(
																		resource.getPath(),
																		AppContext
																				.getUsername(),
																		AppContext
																				.getAccountId());

														FileSearchResultComponent.this
																.displaySearchResult(
																		FileSearchResultComponent.this.rootFolder,
																		FileSearchResultComponent.this.basePath,
																		FileSearchResultComponent.this.searchString);
													}
												}
											});

								}
							});
					deleteBtn.setStyleName("link");
					filterBtnLayout.addComponent(deleteBtn);

					filterBtnLayout.setMargin(true);
					filterBtnLayout.setSpacing(true);
					filterBtnLayout.setWidth("100px");
					resourceSettingPopupBtn.setIcon(MyCollabResource
							.newResource("icons/16/item_settings.png"));
					resourceSettingPopupBtn.setStyleName("link");
					resourceSettingPopupBtn.setContent(filterBtnLayout);
					return resourceSettingPopupBtn;
				}
			});

			this.addGeneratedColumn("createdUser", new Table.ColumnGenerator() {
				private static final long serialVersionUID = 1L;

				@Override
				public Object generateCell(final Table source,
						final Object itemId, final Object columnId) {
					final Resource resource = ResourceTableDisplay.this
							.getResource(itemId);
					String path = resource.getPath();
					final int pathIndex = path.lastIndexOf("/");
					if (pathIndex > -1) {
						path = path.substring(pathIndex + 1);
					}
					final HorizontalLayout resourceLabel = new HorizontalLayout();

					com.vaadin.server.Resource iconResource = null;
					if (resource instanceof Content) {
						iconResource = UiUtils
								.getFileIconResource(((Content) resource)
										.getPath());
					} else {
						iconResource = MyCollabResource
								.newResource("icons/16/ecm/folder_close.png");
					}
					final Embedded iconEmbed = new Embedded(null, iconResource);

					resourceLabel.addComponent(iconEmbed);
					resourceLabel.setComponentAlignment(iconEmbed,
							Alignment.MIDDLE_CENTER);

					final ButtonLink resourceLink = new ButtonLink(path,
							new Button.ClickListener() {

								@Override
								public void buttonClick(final ClickEvent event) {

									final FileDownloadWindow downloadFileWindow = new FileDownloadWindow(
											(Content) resource);
									UI.getCurrent().addWindow(
											downloadFileWindow);

								}
							});

					resourceLink.setWidth("100%");
					resourceLabel.addComponent(resourceLink);
					resourceLabel.setExpandRatio(resourceLink, 1.0f);
					resourceLabel.setWidth("100%");
					return resourceLabel;
				}
			});

			this.addGeneratedColumn("path", new Table.ColumnGenerator() {
				private static final long serialVersionUID = 1L;

				@Override
				public Object generateCell(final Table source,
						final Object itemId, final Object columnId) {
					final Resource resource = ResourceTableDisplay.this
							.getResource(itemId);
					String path = resource.getPath();
					int index = path.lastIndexOf("/");
					if (index >= 0) {
						path = path.substring(0, index);
					}

					if (rootFolder != null && path.startsWith(rootFolder)) {
						path = path.substring(rootFolder.length());
					}

					Button pathLink = new Button(path,
							new Button.ClickListener() {

								@Override
								public void buttonClick(ClickEvent event) {
									// TODO: implement click path link

								}
							});
					pathLink.setStyleName("link");
					return pathLink;
				}
			});

			this.addGeneratedColumn("created", new Table.ColumnGenerator() {

				@Override
				public Object generateCell(final Table source,
						final Object itemId, final Object columnId) {
					final Resource resource = ResourceTableDisplay.this
							.getResource(itemId);
					return new Label(AppContext.formatDateTime(resource
							.getCreated().getTime()));
				}
			});

			this.addGeneratedColumn("size", new Table.ColumnGenerator() {

				@Override
				public Object generateCell(final Table source,
						final Object itemId, final Object columnId) {
					final Resource resource = ResourceTableDisplay.this
							.getResource(itemId);
					return new Label(ResourceUtils.getVolumeDisplay(resource
							.getSize()));
				}
			});

			this.setColumnExpandRatio("createdUser", 1);
			this.setColumnWidth("uuid", 22);
			this.setColumnWidth("size", UIConstants.TABLE_S_LABEL_WIDTH);
			this.setColumnWidth("path", UIConstants.TABLE_EX_LABEL_WIDTH);
			this.setColumnWidth("created", UIConstants.TABLE_DATE_TIME_WIDTH);
		}

		private Resource getResource(final Object itemId) {
			final Container container = this.getContainerDataSource();
			final BeanItem<Resource> item = (BeanItem<Resource>) container
					.getItem(itemId);
			return (item != null) ? item.getBean() : null;
		}
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
			final VerticalLayout layout = new VerticalLayout();
			final HorizontalLayout topRename = new HorizontalLayout();
			topRename.setSpacing(true);
			topRename.setMargin(true);

			final Label label = new Label("Enter new name: ");
			UiUtils.addComponent(topRename, label, Alignment.MIDDLE_LEFT);

			final TextField newName = new TextField();
			newName.setWidth("150px");
			UiUtils.addComponent(topRename, newName, Alignment.MIDDLE_LEFT);

			UiUtils.addComponent(layout, topRename, Alignment.MIDDLE_LEFT);

			final HorizontalLayout controlButton = new HorizontalLayout();
			controlButton.setSpacing(true);
			final Button save = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_SAVE_LABEL),
					new ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							final String oldPath = RenameResourceWindow.this.resource
									.getPath();
							final String parentPath = oldPath.substring(0,
									oldPath.lastIndexOf("/") + 1);
							final String newNameValue = (String) newName
									.getValue();
							final String newPath = parentPath + newNameValue;
							try {
								RenameResourceWindow.this.service.rename(
										oldPath, newPath,
										AppContext.getUsername());
								// reset layout
								FileSearchResultComponent.this
										.displaySearchResult(
												FileSearchResultComponent.this.rootFolder,
												FileSearchResultComponent.this.basePath,
												FileSearchResultComponent.this.searchString);

							} finally {
								RenameResourceWindow.this.close();
							}
						}
					});
			save.addStyleName(UIConstants.THEME_GREEN_LINK);

			UiUtils.addComponent(controlButton, save, Alignment.MIDDLE_CENTER);

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
			UiUtils.addComponent(controlButton, cancel, Alignment.MIDDLE_CENTER);
			UiUtils.addComponent(layout, controlButton, Alignment.MIDDLE_CENTER);
			this.setContent(layout);
		}
	}

}
