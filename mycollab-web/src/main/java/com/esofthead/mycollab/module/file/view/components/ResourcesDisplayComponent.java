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

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.configuration.StorageFactory;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.utils.FileUtils;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.ecm.StorageNames;
import com.esofthead.mycollab.module.ecm.domain.*;
import com.esofthead.mycollab.module.ecm.service.ExternalDriveService;
import com.esofthead.mycollab.module.ecm.service.ExternalResourceService;
import com.esofthead.mycollab.module.ecm.service.ResourceService;
import com.esofthead.mycollab.module.file.domain.criteria.FileSearchCriteria;
import com.esofthead.mycollab.module.file.events.FileEvent;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.SearchHandler;
import com.esofthead.mycollab.vaadin.resources.LazyStreamSource;
import com.esofthead.mycollab.vaadin.resources.OnDemandFileDownloader;
import com.esofthead.mycollab.vaadin.resources.StreamDownloadResourceUtil;
import com.esofthead.mycollab.vaadin.resources.file.FileAssetsUtil;
import com.esofthead.mycollab.vaadin.ui.*;
import com.esofthead.mycollab.vaadin.web.ui.*;
import com.esofthead.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.easyuploads.MultiFileUploadExt;
import org.vaadin.hene.popupbutton.PopupButton;
import org.vaadin.peter.buttongroup.ButtonGroup;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ResourcesDisplayComponent extends MVerticalLayout {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(ResourcesDisplayComponent.class);

    private ResourceService resourceService;
    private ExternalResourceService externalResourceService;
    private ExternalDriveService externalDriveService;

    private FileBreadcrumb fileBreadCrumb;
    private ResourcesContainer resourcesContainer;
    private ResourcePagingNavigator pageNavigator;

    private Folder baseFolder;
    private String rootFolderName;
    private String rootPath;

    public ResourcesDisplayComponent(final Folder rootFolder) {
        this.withMargin(new MarginInfo(false, false, true, false));
        this.baseFolder = rootFolder;
        this.rootPath = rootFolder.getPath();
        externalResourceService = ApplicationContextUtil.getSpringBean(ExternalResourceService.class);
        externalDriveService = ApplicationContextUtil.getSpringBean(ExternalDriveService.class);
        resourceService = ApplicationContextUtil.getSpringBean(ResourceService.class);

        VerticalLayout mainBodyLayout = new VerticalLayout();
        mainBodyLayout.setSpacing(true);

        // file breadcrum ---------------------
        fileBreadCrumb = new FileBreadcrumb(rootPath);
        mainBodyLayout.addComponent(fileBreadCrumb);

        // Construct controllGroupBtn
        MHorizontalLayout groupBtns = new MHorizontalLayout();

        final Button selectAllBtn = new Button();
        selectAllBtn.addStyleName(UIConstants.BUTTON_ACTION);
        selectAllBtn.setIcon(FontAwesome.SQUARE_O);
        selectAllBtn.setData(false);
        selectAllBtn.setImmediate(true);
        selectAllBtn.setDescription("Select all");

        selectAllBtn.addClickListener(new ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                if (!(Boolean) selectAllBtn.getData()) {
                    selectAllBtn.setIcon(FontAwesome.CHECK_SQUARE_O);
                    selectAllBtn.setData(true);
                    resourcesContainer.setAllValues(true);
                } else {
                    selectAllBtn.setData(false);
                    selectAllBtn.setIcon(FontAwesome.SQUARE_O);
                    resourcesContainer.setAllValues(false);
                }
            }
        });
        groupBtns.with(selectAllBtn).withAlign(selectAllBtn, Alignment.MIDDLE_LEFT);

        Button goUpBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_UP));
        goUpBtn.setIcon(FontAwesome.ARROW_UP);

        goUpBtn.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                Folder parentFolder;
                if (baseFolder instanceof ExternalFolder) {
                    if (baseFolder.getPath().equals("/")) {
                        parentFolder = baseFolder;
                    } else {
                        parentFolder = externalResourceService.getParentResourceFolder(
                                ((ExternalFolder) baseFolder).getExternalDrive(), baseFolder.getPath());
                    }
                } else if (!baseFolder.getPath().equals(rootPath)) {
                    parentFolder = resourceService.getParentFolder(baseFolder.getPath());
                } else {
                    parentFolder = baseFolder;
                }

                if (parentFolder != null) {
                    resourcesContainer.constructBody(parentFolder);
                    baseFolder = parentFolder;
                    fileBreadCrumb.gotoFolder(baseFolder);
                } else {
                    LOG.error("Can not get parent folder", baseFolder.getPath());
                }
            }
        });
        goUpBtn.setDescription("Back to parent folder");
        goUpBtn.setStyleName(UIConstants.BUTTON_ACTION);
        goUpBtn.setDescription("Go up");

        groupBtns.with(goUpBtn).withAlign(goUpBtn, Alignment.MIDDLE_LEFT);

        ButtonGroup navButton = new ButtonGroup();
        navButton.addStyleName(UIConstants.BUTTON_ACTION);
        Button createBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CREATE), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                AddNewFolderWindow addnewFolderWindow = new AddNewFolderWindow();
                UI.getCurrent().addWindow(addnewFolderWindow);
            }
        });
        createBtn.setIcon(FontAwesome.PLUS);
        createBtn.addStyleName(UIConstants.BUTTON_ACTION);
        createBtn.setWidth("90px");
        createBtn.setDescription("Create new folder");
        createBtn.setEnabled(AppContext.canWrite(RolePermissionCollections.PUBLIC_DOCUMENT_ACCESS));
        navButton.addButton(createBtn);

        Button uploadBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_UPLOAD), new ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                MultiUploadContentWindow multiUploadWindow = new MultiUploadContentWindow();
                UI.getCurrent().addWindow(multiUploadWindow);
            }
        });
        uploadBtn.setWidth("90px");
        uploadBtn.setIcon(FontAwesome.UPLOAD);
        uploadBtn.addStyleName(UIConstants.BUTTON_ACTION);
        uploadBtn.setDescription("Upload");

        uploadBtn.setEnabled(AppContext.canWrite(RolePermissionCollections.PUBLIC_DOCUMENT_ACCESS));
        navButton.addButton(uploadBtn);

        Button downloadBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_DOWNLOAD));

        LazyStreamSource streamSource = new LazyStreamSource() {
            private static final long serialVersionUID = 1L;

            @Override
            protected StreamSource buildStreamSource() {
                Collection<Resource> selectedResources = getSelectedResources();
                return StreamDownloadResourceUtil.getStreamSourceSupportExtDrive(selectedResources);
            }

            @Override
            public String getFilename() {
                Collection<Resource> selectedResources = getSelectedResources();
                return StreamDownloadResourceUtil.getDownloadFileName(selectedResources);
            }
        };
        OnDemandFileDownloader downloaderExt = new OnDemandFileDownloader(streamSource);
        downloaderExt.extend(downloadBtn);

        downloadBtn.setWidth("110px");
        downloadBtn.setIcon(FontAwesome.DOWNLOAD);
        downloadBtn.addStyleName(UIConstants.BUTTON_ACTION);
        downloadBtn.setEnabled(AppContext.canRead(RolePermissionCollections.PUBLIC_DOCUMENT_ACCESS));
        navButton.addButton(downloadBtn);

        Button moveToBtn = new Button("Move", new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                Collection<Resource> selectedResources = getSelectedResources();
                if (CollectionUtils.isNotEmpty(selectedResources)) {
                    MoveResourceWindow moveResourceWindow = new MoveResourceWindow(selectedResources);
                    UI.getCurrent().addWindow(moveResourceWindow);
                } else {
                    NotificationUtil.showWarningNotification("Please select at least one item to move");
                }
            }
        });
        moveToBtn.setWidth("90px");
        moveToBtn.setIcon(FontAwesome.ARROWS);
        moveToBtn.addStyleName(UIConstants.BUTTON_ACTION);
        moveToBtn.setEnabled(AppContext.canWrite(RolePermissionCollections.PUBLIC_DOCUMENT_ACCESS));
        moveToBtn.setDescription("Move to");
        navButton.addButton(moveToBtn);

        Button deleteBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_DELETE), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                Collection<Resource> selectedResources = getSelectedResources();
                if (CollectionUtils.isEmpty(selectedResources)) {
                    NotificationUtil.showWarningNotification("Please select at least one item to delete");
                } else {
                    deleteResourceAction();
                }
            }
        });
        deleteBtn.setWidth("90px");
        deleteBtn.setIcon(FontAwesome.TRASH_O);
        deleteBtn.addStyleName(UIConstants.BUTTON_DANGER);
        deleteBtn.setDescription("Delete resource");
        deleteBtn.setEnabled(AppContext.canAccess(RolePermissionCollections.PUBLIC_DOCUMENT_ACCESS));

        navButton.addButton(deleteBtn);
        groupBtns.addComponent(navButton);

        mainBodyLayout.addComponent(groupBtns);
        resourcesContainer = new ResourcesContainer();

        mainBodyLayout.addComponent(resourcesContainer);
        this.addComponent(mainBodyLayout);
    }

    /**
     * this method show Component when start loading
     *
     * @param baseFolder
     */
    public void displayComponent(Folder baseFolder, String rootFolderName) {
        this.baseFolder = baseFolder;
        this.rootPath = baseFolder.getPath();
        this.rootFolderName = rootFolderName;
        this.fileBreadCrumb.initBreadcrumb();
        this.resourcesContainer.constructBody(this.baseFolder);
    }

    private void deleteResourceAction() {
        ConfirmDialogExt.show(UI.getCurrent(), AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, AppContext.getSiteName()),
                AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                AppContext.getMessage(GenericI18Enum.BUTTON_YES),
                AppContext.getMessage(GenericI18Enum.BUTTON_NO),
                new ConfirmDialog.Listener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onClose(final ConfirmDialog dialog) {
                        Collection<Resource> selectedResources = getSelectedResources();
                        if (CollectionUtils.isNotEmpty(selectedResources)) {
                            if (dialog.isConfirmed()) {
                                for (Resource res : selectedResources) {
                                    if (res.isExternalResource()) {
                                        externalResourceService.deleteResource(
                                                ((ExternalFolder) res).getExternalDrive(), res.getPath());
                                    } else {
                                        if (res instanceof Folder) {
                                            EventBusFactory.getInstance().post(new FileEvent.ResourceRemovedEvent
                                                    (ResourcesDisplayComponent.this, res));
                                        }
                                        resourceService.removeResource(res.getPath(), AppContext.getUsername(),
                                                AppContext.getAccountId());
                                    }
                                }

                                resourcesContainer.constructBody(baseFolder);
                                NotificationUtil.showNotification("Congrats", "Deleted content successfully.");
                            }
                        }
                    }
                });
    }

    public void constructBodyItemContainer(Folder folder) {
        this.baseFolder = folder;
        fileBreadCrumb.gotoFolder(folder);
        resourcesContainer.constructBody(folder);
    }

    public void addSearchHandlerToBreadCrumb(final SearchHandler<FileSearchCriteria> handler) {
        fileBreadCrumb.addSearchHandler(handler);
    }

    public Collection<Resource> getSelectedResources() {
        return resourcesContainer.getSelectedResourceCollection();
    }

    private class ResourcesContainer extends MVerticalLayout {
        private static final long serialVersionUID = 1L;

        private List<CheckBox> checkboxes;
        private List<Resource> resources;

        public ResourcesContainer() {
            withMargin(false).withWidth("100%");
            checkboxes = new ArrayList<>();
        }

        private void setAllValues(boolean value) {
            for (CheckBox checkbox : checkboxes) {
                checkbox.setValue(value);
            }
        }

        private void constructBody(Folder currentFolder) {
            this.removeAllComponents();
            this.addComponent(new Hr());

            if (currentFolder instanceof ExternalFolder) {
                resources = externalResourceService.getResources(((ExternalFolder) currentFolder).getExternalDrive(),
                        currentFolder.getPath());
            } else {
                resources = resourceService.getResources(currentFolder.getPath());
            }

            if (currentFolder.getPath().equals(rootPath)) {
                List<ExternalDrive> externalDrives = externalDriveService.getExternalDrivesOfUser(AppContext.getUsername());
                if (CollectionUtils.isNotEmpty(externalDrives)) {
                    for (ExternalDrive drive : externalDrives) {
                        if (StorageNames.DROPBOX.equals(drive.getStoragename())) {
                            try {
                                Resource res = externalResourceService.getCurrentResourceByPath(drive, "/");
                                res.setName(drive.getFoldername());
                                resources.add(0, res);
                            } catch (Exception e) {
                                LOG.error("Error while query renameResource", e);
                            }
                        } else {
                            throw new MyCollabException("Do not support any external drive different than Dropbox");
                        }
                    }
                }
            }

            if (CollectionUtils.isNotEmpty(resources)) {
                pageNavigator = new ResourcePagingNavigator(resources);
                pageNavigator.setWidth("100%");
                if (resources.size() <= pageNavigator.pageItemNum) {
                    for (Resource res : resources) {
                        ComponentContainer resContainer = buildResourceRowComp(res);
                        if (resContainer != null) {
                            this.addComponent(buildResourceRowComp(res));
                            this.addComponent(new Hr());
                        }
                    }
                } else if (resources.size() > pageNavigator.pageItemNum) {
                    for (int i = 0; i < pageNavigator.pageItemNum; i++) {
                        Resource res = resources.get(i);
                        ComponentContainer resContainer = buildResourceRowComp(res);
                        if (resContainer != null) {
                            this.addComponent(buildResourceRowComp(res));
                            this.addComponent(new Hr());
                        }
                    }
                    this.with(pageNavigator).withAlign(pageNavigator, Alignment.MIDDLE_CENTER);
                }
            }
        }

        private HorizontalLayout buildResourceRowComp(final Resource resource) {
            if (resource.getName().startsWith(".")) {
                return null;
            }
            MHorizontalLayout layout = new MHorizontalLayout().withWidth("100%").withHeight("60px");
            layout.addStyleName("resourceItem");

            final CheckBox checkbox = new CheckBox();
            checkbox.setWidth("25px");
            checkboxes.add(checkbox);

            checkbox.addValueChangeListener(new ValueChangeListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void valueChange(ValueChangeEvent event) {
                    resource.setSelected(checkbox.getValue());
                }
            });
            layout.with(checkbox).withAlign(checkbox, Alignment.MIDDLE_LEFT);

            CssLayout resIconWrapper = new CssLayout();
            Component resourceIcon = null;
            if (resource instanceof Folder) {
                resourceIcon = (resource instanceof ExternalFolder) ? new ELabel(FontAwesome.DROPBOX) : new
                        ELabel(FontAwesome.FOLDER);
                resourceIcon.addStyleName("icon-38px");
            } else if (resource instanceof Content) {
                Content content = (Content) resource;
                if (StringUtils.isNotBlank(content.getThumbnail())) {
                    resourceIcon = new Embedded(null, new ExternalResource(StorageFactory.getInstance().getResourcePath(content.getThumbnail())));
                    resourceIcon.setWidth("38px");
                    resourceIcon.setHeight("38px");
                } else {
                    if (content instanceof ExternalContent) {
                        final byte[] thumbnailBytes = ((ExternalContent) content).getThumbnailBytes();
                        if (thumbnailBytes != null) {
                            resourceIcon = new Embedded(null, new StreamResource(new StreamSource() {
                                @Override
                                public InputStream getStream() {
                                    return new ByteArrayInputStream(thumbnailBytes);
                                }
                            }, String.format("thumbnail%s.%s", content.getPath(), "png")));
                            resourceIcon.setWidth("38px");
                            resourceIcon.setHeight("38px");
                        }
                    }
                }
            } else {
                throw new MyCollabException("Do not support resource file " + resource.getClass());
            }
            if (resourceIcon == null) {
                resourceIcon = new ELabel(FileAssetsUtil.getFileIconResource(resource.getName()));
                resourceIcon.addStyleName("icon-38px");
            }
            resIconWrapper.addComponent(resourceIcon);

            layout.addComponent(resIconWrapper);
            layout.setComponentAlignment(resIconWrapper, Alignment.MIDDLE_LEFT);

            MVerticalLayout informationLayout = new MVerticalLayout().withMargin(false);

            Button resourceLinkBtn = new Button(resource.getName(), new ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(ClickEvent event) {
                    if (resource instanceof Folder) {
                        baseFolder = (Folder) resource;
                        resourcesContainer.constructBody((Folder) resource);
                        fileBreadCrumb.gotoFolder((Folder) resource);
                    } else {
                        FileDownloadWindow fileDownloadWindow = new FileDownloadWindow((Content) resource);
                        UI.getCurrent().addWindow(fileDownloadWindow);
                    }
                }
            });
            resourceLinkBtn.addStyleName(UIConstants.BUTTON_LINK);
            informationLayout.addComponent(resourceLinkBtn);

            MHorizontalLayout moreInfoAboutResLayout = new MHorizontalLayout();

            // If renameResource is dropbox renameResource then we can not
            // define the
            // created user so we do not need to display, then we assume the
            // current user is created user
            if (StringUtils.isEmpty(resource.getCreatedUser())) {
                UserLink usernameLbl = new UserLink(AppContext.getUsername(), AppContext.getUserAvatarId(),
                        AppContext.getUser().getDisplayName());
                usernameLbl.addStyleName(UIConstants.LABEL_META_INFO);
                moreInfoAboutResLayout.addComponent(usernameLbl);
            } else {
                UserService userService = ApplicationContextUtil.getSpringBean(UserService.class);
                SimpleUser user = userService.findUserByUserNameInAccount(resource.getCreatedUser(), AppContext.getAccountId());
                if (user != null) {
                    UserLink userLink = new UserLink(user.getUsername(), user.getAvatarid(), user.getDisplayName());
                    userLink.addStyleName(UIConstants.LABEL_META_INFO);
                    moreInfoAboutResLayout.addComponent(userLink);
                } else {
                    Label usernameLbl = new Label(resource.getCreatedBy());
                    usernameLbl.addStyleName(UIConstants.LABEL_META_INFO);
                    moreInfoAboutResLayout.addComponent(usernameLbl);
                }
            }
            moreInfoAboutResLayout.addComponent(new Separator());

            // If renameResource is dropbox renameResource then we can not
            // define the
            // created date so we do not need to display\
            if (resource.getCreated() != null) {
                Label createdTimeLbl = new Label((AppContext.formatPrettyTime(resource.getCreated().getTime())));
                createdTimeLbl.setDescription(AppContext.formatDateTime(resource.getCreated().getTime()));
                createdTimeLbl.addStyleName(UIConstants.LABEL_META_INFO);
                moreInfoAboutResLayout.addComponent(createdTimeLbl);
            } else {
                Label createdTimeLbl = new Label("Undefined");
                createdTimeLbl.addStyleName(UIConstants.LABEL_META_INFO);
                moreInfoAboutResLayout.addComponent(createdTimeLbl);
            }

            if (resource instanceof Content) {
                moreInfoAboutResLayout.addComponent(new Separator());
                Label lbl = new Label(FileUtils.getVolumeDisplay(resource.getSize()));
                lbl.addStyleName(UIConstants.LABEL_META_INFO);
                moreInfoAboutResLayout.addComponent(lbl);
            }
            informationLayout.addComponent(moreInfoAboutResLayout);
            layout.with(informationLayout).withAlign(informationLayout, Alignment.MIDDLE_LEFT).expand(informationLayout);

            final PopupButton resourceSettingPopupBtn = new PopupButton();

            OptionPopupContent filterBtnLayout = new OptionPopupContent();
            Button renameBtn = new Button("Rename", new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(ClickEvent event) {
                    resourceSettingPopupBtn.setPopupVisible(false);
                    UI.getCurrent().addWindow(new RenameResourceWindow(resource));
                }
            });
            renameBtn.setIcon(FontAwesome.EDIT);
            filterBtnLayout.addOption(renameBtn);

            Button downloadBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_DOWNLOAD));

            LazyStreamSource streamSource = new LazyStreamSource() {
                private static final long serialVersionUID = 1L;

                @Override
                protected StreamSource buildStreamSource() {
                    List<Resource> lstRes = new ArrayList<>();
                    lstRes.add(resource);
                    return StreamDownloadResourceUtil.getStreamSourceSupportExtDrive(lstRes);
                }

                @Override
                public String getFilename() {
                    return resource.getName();
                }
            };

            OnDemandFileDownloader downloaderExt = new OnDemandFileDownloader(streamSource);
            downloaderExt.extend(downloadBtn);

            downloadBtn.setIcon(FontAwesome.DOWNLOAD);
            filterBtnLayout.addOption(downloadBtn);

            Button moveBtn = new Button("Move to", new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(ClickEvent event) {
                    UI.getCurrent().addWindow(new MoveResourceWindow(resource));
                }
            });
            moveBtn.setIcon(FontAwesome.ARROWS);
            filterBtnLayout.addOption(moveBtn);

            Button deleteBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_DELETE), new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(ClickEvent event) {
                    ResourcesDisplayComponent.this.deleteResourceAction();
                }
            });
            deleteBtn.setIcon(FontAwesome.TRASH_O);
            filterBtnLayout.addDangerOption(deleteBtn);

            resourceSettingPopupBtn.setIcon(FontAwesome.ELLIPSIS_H);
            resourceSettingPopupBtn.addStyleName(UIConstants.BUTTON_ACTION);
            resourceSettingPopupBtn.setContent(filterBtnLayout);

            layout.addComponent(resourceSettingPopupBtn);
            layout.setComponentAlignment(resourceSettingPopupBtn, Alignment.MIDDLE_RIGHT);
            return layout;
        }

        Collection<Resource> getSelectedResourceCollection() {
            if (CollectionUtils.isNotEmpty(resources)) {
                Collection<Resource> selectedResources = Collections2.filter(resources, new Predicate<Resource>() {
                    @Override
                    public boolean apply(Resource input) {
                        return input.isSelected();
                    }
                });
                return selectedResources;
            } else {
                return new ArrayList<>();
            }

        }
    }

    private class RenameResourceWindow extends Window {
        private static final long serialVersionUID = 1L;
        private Resource renameResource;

        public RenameResourceWindow(Resource resource) {
            super("Rename folder/file");
            this.center();
            this.setResizable(false);
            this.setModal(true);
            this.setWidth("400px");
            this.renameResource = resource;
            this.constructBody();
        }

        private void constructBody() {
            VerticalLayout contentLayout = new VerticalLayout();
            GridFormLayoutHelper layoutHelper = GridFormLayoutHelper.defaultFormLayoutHelper(1, 1);
            final TextField folderName = new TextField();
            layoutHelper.addComponent(folderName, "Folder Name", 0, 0);
            contentLayout.addComponent(layoutHelper.getLayout());

            final MHorizontalLayout controlButtons = new MHorizontalLayout().withMargin(true);
            final Button saveBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_SAVE), new ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final ClickEvent event) {
                    String oldPath = renameResource.getPath();
                    String parentOldPath = oldPath.substring(0, oldPath.lastIndexOf("/") + 1);

                    String newNameValue = folderName.getValue();
                    String newPath = parentOldPath + newNameValue;

                    if (renameResource.isExternalResource()) {
                        externalResourceService.rename(((ExternalFolder) renameResource).getExternalDrive(), oldPath, newPath);
                    } else {
                        resourceService.rename(oldPath, newPath, AppContext.getUsername());
                    }
                    resourcesContainer.constructBody(baseFolder);

                    RenameResourceWindow.this.close();
                }
            });
            saveBtn.setIcon(FontAwesome.SAVE);
            saveBtn.addStyleName(UIConstants.BUTTON_ACTION);

            Button cancelBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL), new ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final ClickEvent event) {
                    RenameResourceWindow.this.close();
                }
            });
            cancelBtn.addStyleName(UIConstants.BUTTON_OPTION);
            controlButtons.with(saveBtn, cancelBtn).alignAll(Alignment.MIDDLE_CENTER);
            contentLayout.addComponent(controlButtons);
            contentLayout.setComponentAlignment(controlButtons, Alignment.MIDDLE_CENTER);

            this.setContent(contentLayout);
        }
    }

    private class AddNewFolderWindow extends Window {
        private static final long serialVersionUID = 1L;

        private TextField folderName;
        private TextArea descriptionArea;

        public AddNewFolderWindow() {
            this.setModal(true);
            this.setResizable(false);
            this.setWidth("500px");
            this.setCaption("New Folder");
            this.center();

            MVerticalLayout contentLayout = new MVerticalLayout().withSpacing(false).withMargin(new MarginInfo(false, false, true, false));
            this.setContent(contentLayout);

            GridFormLayoutHelper layoutHelper = GridFormLayoutHelper.defaultFormLayoutHelper(1, 1);
            this.folderName = new TextField();
            layoutHelper.addComponent(folderName, "Folder Name", 0, 0);
            contentLayout.addComponent(layoutHelper.getLayout());
            MHorizontalLayout controlsLayout = new MHorizontalLayout().withMargin(new MarginInfo(true, false, false, false));
            Button saveBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_SAVE), new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final ClickEvent event) {
                    String folderVal = folderName.getValue();

                    if (StringUtils.isNotBlank(folderVal)) {
                        FileUtils.assertValidFolderName(folderVal);
                        String baseFolderPath = baseFolder.getPath();

                        if (baseFolder instanceof ExternalFolder) {
                            String path = baseFolder.getPath() + "/" + folderVal;
                            externalResourceService.createNewFolder(((ExternalFolder) baseFolder).getExternalDrive(), path);
                        } else {
                            resourceService.createNewFolder(baseFolderPath, folderVal, AppContext.getUsername());
                        }
                        resourcesContainer.constructBody(baseFolder);
                        AddNewFolderWindow.this.close();
                    }
                }
            });
            saveBtn.addStyleName(UIConstants.BUTTON_ACTION);
            saveBtn.setIcon(FontAwesome.SAVE);
            controlsLayout.addComponent(saveBtn);

            Button cancelBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL), new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final ClickEvent event) {
                    AddNewFolderWindow.this.close();
                }
            });
            cancelBtn.addStyleName(UIConstants.BUTTON_OPTION);
            controlsLayout.addComponent(cancelBtn);
            controlsLayout.setComponentAlignment(cancelBtn, Alignment.MIDDLE_RIGHT);

            contentLayout.with(controlsLayout).withAlign(controlsLayout, Alignment.MIDDLE_CENTER);
        }
    }

    private class MultiUploadContentWindow extends Window {
        private static final long serialVersionUID = 1L;

        private final GridFormLayoutHelper layoutHelper;
        private final MultiFileUploadExt multiFileUploadExt;

        public MultiUploadContentWindow() {
            super("Upload");
            this.setWidth("500px");
            this.setResizable(false);
            this.setModal(true);
            center();

            VerticalLayout contentLayout = new VerticalLayout();
            contentLayout.setMargin(new MarginInfo(false, false, true, false));
            this.setContent(contentLayout);
            final AttachmentPanel attachmentPanel = new AttachmentPanel();

            this.layoutHelper = GridFormLayoutHelper.defaultFormLayoutHelper(1, 1);

            multiFileUploadExt = new MultiFileUploadExt(attachmentPanel);
            multiFileUploadExt.addComponent(attachmentPanel);
            multiFileUploadExt.setWidth("100%");

            this.layoutHelper.addComponent(multiFileUploadExt, "File", 0, 0);
            contentLayout.addComponent(this.layoutHelper.getLayout());

            MHorizontalLayout controlsLayout = new MHorizontalLayout().withMargin(new MarginInfo(true, false, false, false));

            final Button uploadBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_UPLOAD), new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final ClickEvent event) {
                    List<File> attachments = attachmentPanel.files();
                    if (CollectionUtils.isNotEmpty(attachments)) {
                        for (File attachment : attachments) {
                            try {
                                if (!FileUtils.isValidFileName(attachment.getName())) {
                                    NotificationUtil.showWarningNotification("Please upload valid file-name except any follow characters : <>:&/\\|?*&");
                                    return;
                                }
                                Content content = new Content(String.format("%s/%s", baseFolder.getPath(), attachment.getName()));
                                content.setSize(attachment.length());
                                FileInputStream fileInputStream = new FileInputStream(attachment);

                                if (baseFolder instanceof ExternalFolder) {
                                    externalResourceService.saveContent(((ExternalFolder) baseFolder)
                                            .getExternalDrive(), content, fileInputStream);
                                } else
                                    resourceService.saveContent(content, AppContext.getUsername(),
                                            fileInputStream, AppContext.getAccountId());
                            } catch (IOException e) {
                                throw new MyCollabException(e);
                            }
                        }
                        resourcesContainer.constructBody(baseFolder);
                        MultiUploadContentWindow.this.close();
                        NotificationUtil.showNotification("Congrats", "Upload successfully.");
                    } else {
                        NotificationUtil.showWarningNotification("It seems you did not attach file yet!");
                    }
                }
            });
            uploadBtn.addStyleName(UIConstants.BUTTON_ACTION);
            uploadBtn.setIcon(FontAwesome.UPLOAD);
            controlsLayout.addComponent(uploadBtn);

            Button cancelBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL), new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final ClickEvent event) {
                    MultiUploadContentWindow.this.close();
                }
            });
            cancelBtn.addStyleName(UIConstants.BUTTON_OPTION);
            controlsLayout.addComponent(cancelBtn);
            controlsLayout.setComponentAlignment(cancelBtn, Alignment.MIDDLE_RIGHT);

            contentLayout.addComponent(controlsLayout);
            contentLayout.setComponentAlignment(controlsLayout, Alignment.MIDDLE_CENTER);
        }
    }

    private class ResourcePagingNavigator extends CssLayout {
        private static final long serialVersionUID = 1L;
        private int totalItem;
        private int pageItemNum = 15;
        private int currentPage;
        private CssLayout controlBarWrapper;
        private MHorizontalLayout navigator;
        private int totalPage;
        private List<Resource> lstResource;
        private Button currentBtn;
        private HorizontalLayout controlBar;

        public ResourcePagingNavigator(List<Resource> lstResource) {
            this.totalItem = lstResource.size();
            this.currentPage = 1;
            this.totalPage = (totalItem / pageItemNum) + 1;
            this.lstResource = lstResource;

            // defined layout here ---------------------------
            this.controlBarWrapper = new CssLayout();
            this.controlBarWrapper.setWidth("100%");

            controlBar = new MHorizontalLayout().withMargin(false).withWidth("100%");
            this.controlBarWrapper.addComponent(controlBar);

            navigator = new MHorizontalLayout();
            navigator.setWidthUndefined();
            createPageControls();
        }

        private void createPageControls() {
            navigator.removeAllComponents();
            if (this.currentPage > 1) {
                final Button firstLink = new ButtonLink("1", new ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(final ClickEvent event) {
                        pageChange(1);
                    }
                });
                firstLink.addStyleName("buttonPaging");
                navigator.addComponent(firstLink);
            }
            if (this.currentPage >= 5) {
                final Label ss1 = new Label("...");
                ss1.addStyleName("buttonPaging");
                navigator.addComponent(ss1);
            }
            if (this.currentPage > 3) {
                final Button previous2 = new ButtonLink("" + (this.currentPage - 2), new ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(final ClickEvent event) {
                        pageChange(currentPage - 2);
                    }
                });
                previous2.addStyleName("buttonPaging");
                navigator.addComponent(previous2);
            }
            if (this.currentPage > 2) {
                final Button previous1 = new ButtonLink("" + (this.currentPage - 1), new ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(final ClickEvent event) {
                        pageChange(currentPage - 1);
                    }
                });
                previous1.addStyleName("buttonPaging");
                navigator.addComponent(previous1);
            }
            // Here add current ButtonLinkLegacy
            currentBtn = new ButtonLink("" + this.currentPage, new ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final ClickEvent event) {
                    // pageChange(currentPage);
                }
            });
            currentBtn.addStyleName("buttonPaging");
            currentBtn.addStyleName("current");

            navigator.addComponent(currentBtn);
            final int range = this.totalPage - this.currentPage;
            if (range >= 1) {
                final Button next1 = new ButtonLink(
                        "" + (this.currentPage + 1), new ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(final ClickEvent event) {
                        pageChange(currentPage + 1);
                    }
                });
                next1.addStyleName("buttonPaging");
                navigator.addComponent(next1);
            }
            if (range >= 2) {
                final Button next2 = new ButtonLink("" + (this.currentPage + 2), new ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(final ClickEvent event) {
                        pageChange(currentPage + 2);
                    }
                });
                next2.addStyleName("buttonPaging");
                navigator.addComponent(next2);
            }
            if (range >= 4) {
                final Label ss2 = new Label("...");
                ss2.addStyleName("buttonPaging");
                navigator.addComponent(ss2);
            }
            if (range >= 3) {
                final Button last = new ButtonLink("" + this.totalPage, new ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(final ClickEvent event) {
                        pageChange(totalPage);
                    }
                });
                last.addStyleName("buttonPaging");
                navigator.addComponent(last);
            }


            controlBar.addComponent(navigator);
            controlBar.setComponentAlignment(navigator, Alignment.MIDDLE_RIGHT);
            this.addComponent(controlBarWrapper);
        }

        public void pageChange(int currentPage) {
            this.currentPage = currentPage;
            resourcesContainer.removeAllComponents();
            int index = currentPage - 1;
            int start = (index == 0) ? index : index * pageItemNum;
            int end = ((start + pageItemNum) > lstResource.size()) ? lstResource.size() : start + pageItemNum;

            for (int i = start; i < end; i++) {
                Resource res = lstResource.get(i);
                ComponentContainer resContainer = resourcesContainer.buildResourceRowComp(res);
                if (resContainer != null) {
                    resourcesContainer.addComponent(resContainer);
                    resourcesContainer.addComponent(new Hr());
                }
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

        public MoveResourceWindow(Collection<Resource> lstResource) {
            super(lstResource);
        }

        @Override
        public void displayAfterMoveSuccess(Folder folder, boolean checking) {
            fileBreadCrumb.gotoFolder(folder);
            resourcesContainer.constructBody(folder);
            if (!checking) {
                NotificationUtil.showNotification("Congrats", "Moved asset(s) successfully.");
            } else {
                NotificationUtil
                        .showWarningNotification("Moving assets is finished, some items can't move to destination. Please " +
                                "check duplicated file-name and try again.");
            }
        }

        @Override
        protected void displayFiles() {
            this.folderTree.removeAllItems();

            this.baseFolder = new Folder(rootPath);
            this.folderTree.addItem(new Object[]{ResourcesDisplayComponent.this.rootFolderName, ""}, this.baseFolder);
            this.folderTree.setItemCaption(this.baseFolder, ResourcesDisplayComponent.this.rootFolderName);

            this.folderTree.setCollapsed(this.baseFolder, false);
        }
    }

}
