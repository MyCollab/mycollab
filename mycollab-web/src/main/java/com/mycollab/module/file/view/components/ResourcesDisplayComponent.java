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
package com.mycollab.module.file.view.components;

import com.esofthead.vaadin.floatingcomponent.FloatingComponent;
import com.google.common.collect.Collections2;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.configuration.StorageFactory;
import com.mycollab.core.MyCollabException;
import com.mycollab.core.utils.FileUtils;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.ecm.StorageNames;
import com.mycollab.module.ecm.domain.*;
import com.mycollab.module.ecm.service.ExternalDriveService;
import com.mycollab.module.ecm.service.ExternalResourceService;
import com.mycollab.module.ecm.service.ResourceService;
import com.mycollab.module.file.events.FileEvent;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.user.domain.SimpleUser;
import com.mycollab.module.user.service.UserService;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.resources.LazyStreamSource;
import com.mycollab.vaadin.resources.OnDemandFileDownloader;
import com.mycollab.vaadin.resources.StreamDownloadResourceUtil;
import com.mycollab.vaadin.resources.file.FileAssetsUtil;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.web.ui.AttachmentPanel;
import com.mycollab.vaadin.web.ui.ConfirmDialogExt;
import com.mycollab.vaadin.web.ui.UIConstants;
import com.mycollab.vaadin.web.ui.UserLink;
import com.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.easyuploads.MultiFileUploadExt;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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

    private Folder baseFolder;
    private final String rootPath;

    public ResourcesDisplayComponent(final Folder rootFolder) {
        this.baseFolder = rootFolder;
        this.rootPath = rootFolder.getPath();
        externalResourceService = AppContextUtil.getSpringBean(ExternalResourceService.class);
        externalDriveService = AppContextUtil.getSpringBean(ExternalDriveService.class);
        resourceService = AppContextUtil.getSpringBean(ResourceService.class);

        withSpacing(false).withMargin(new MarginInfo(true, false, true, false));
        fileBreadCrumb = new FileBreadcrumb(rootPath);
        fileBreadCrumb.addSearchHandler(criteria -> {
            Resource selectedFolder;
            if (StorageNames.DROPBOX.equals(criteria.getStorageName())) {
                selectedFolder = externalResourceService.getCurrentResourceByPath(
                        criteria.getExternalDrive(), criteria.getBaseFolder());
            } else {
                selectedFolder = resourceService.getResource(criteria.getBaseFolder());
            }

            if (selectedFolder == null) {
                LOG.error(String.format("Can not find folder with path %s--%s", criteria.getBaseFolder(),
                        criteria.getRootFolder()));
            } else if (!(selectedFolder instanceof Folder)) {
                LOG.error(String.format("Expect folder but the result is file %s--%s", criteria.getBaseFolder(),
                        criteria.getRootFolder()));
            } else {
                Folder resultFolder = (Folder) selectedFolder;
                constructBodyItemContainer(resultFolder);
                baseFolder = resultFolder;
            }
        });
        ELabel headerLbl = ELabel.h2(ProjectAssetsManager.getAsset(ProjectTypeConstants.FILE).getHtml() + " Files");

        MButton createBtn = new MButton("New folder", clickEvent -> UI.getCurrent().addWindow(new AddNewFolderWindow()))
                .withIcon(FontAwesome.PLUS).withStyleName(UIConstants.BUTTON_ACTION)
                .withVisible(AppContext.canWrite(RolePermissionCollections.PUBLIC_DOCUMENT_ACCESS));
        createBtn.setDescription("Create new folder");

        MButton uploadBtn = new MButton(AppContext.getMessage(GenericI18Enum.BUTTON_UPLOAD), clickEvent -> {
            MultiUploadContentWindow multiUploadWindow = new MultiUploadContentWindow();
            UI.getCurrent().addWindow(multiUploadWindow);
        }).withIcon(FontAwesome.UPLOAD).withStyleName(UIConstants.BUTTON_ACTION)
                .withVisible(AppContext.canWrite(RolePermissionCollections.PUBLIC_DOCUMENT_ACCESS));
        uploadBtn.setDescription("Upload");

        MHorizontalLayout headerLayout = new MHorizontalLayout(headerLbl, new MHorizontalLayout(createBtn, uploadBtn)).expand(headerLbl);
        resourcesContainer = new ResourcesContainer();
        MVerticalLayout floatControl = new MVerticalLayout(headerLayout, fileBreadCrumb).withMargin(new MarginInfo
                (false, false, true, false)).withStyleName("floatControl");
        this.with(floatControl, resourcesContainer);

        fileBreadCrumb.initBreadcrumb();
        resourcesContainer.constructBody(baseFolder);
    }

    private void deleteResourceAction(final Collection<Resource> deletedResources) {
        ConfirmDialogExt.show(UI.getCurrent(), AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, AppContext.getSiteName()),
                AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                AppContext.getMessage(GenericI18Enum.BUTTON_YES),
                AppContext.getMessage(GenericI18Enum.BUTTON_NO),
                confirmDialog -> {
                    if (confirmDialog.isConfirmed()) {
                        for (Resource res : deletedResources) {
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
                });
    }

    public void constructBodyItemContainer(Folder folder) {
        this.baseFolder = folder;
        fileBreadCrumb.gotoFolder(folder);
        resourcesContainer.constructBody(folder);
    }

    public Collection<Resource> getSelectedResources() {
        return resourcesContainer.getSelectedResourceCollection();
    }

    private class ResourcesContainer extends MHorizontalLayout {

        private Resource selectedResource;
        private MVerticalLayout bodyContainer;
        private MVerticalLayout selectedResourceControlLayout;
        private List<Resource> resources;

        ResourcesContainer() {
            this.setId("resource-container");
            withSpacing(true).withFullWidth();
        }

        private void constructBody(Folder currentFolder) {
            this.removeAllComponents();

            bodyContainer = new MVerticalLayout().withSpacing(false).withMargin(false);
            selectedResourceControlLayout = new MVerticalLayout().withSpacing(false).withMargin(false).withWidth("400px")
                    .withStyleName("margin-top", "margin-left");

            FloatingComponent.floatThis(selectedResourceControlLayout).setContainerId("main-body");
            with(bodyContainer, selectedResourceControlLayout).expand(bodyContainer);
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
                for (Resource res : resources) {
                    ComponentContainer resContainer = buildResourceRowComp(res);
                    if (resContainer != null) {
                        bodyContainer.addComponent(buildResourceRowComp(res));
                    }
                }
            }
        }

        private void displaySelectedResourceControls() {
            if (selectedResource != null) {
                selectedResourceControlLayout.removeAllComponents();
                ELabel resourceHeaderLbl = ELabel.h3(selectedResource.getName()).withStyleName(UIConstants.TEXT_ELLIPSIS);
                MHorizontalLayout headerLayout = new MHorizontalLayout(resourceHeaderLbl).withMargin(new MarginInfo
                        (false, true, false, true)).withStyleName("panel-header").withFullWidth().alignAll(Alignment.MIDDLE_LEFT);
                selectedResourceControlLayout.with(headerLayout);

                MButton renameBtn = new MButton("Rename", clickEvent -> UI.getCurrent().addWindow(new
                        RenameResourceWindow(selectedResource)))
                        .withIcon(FontAwesome.EDIT).withStyleName(UIConstants.BUTTON_LINK);

                Button downloadBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_DOWNLOAD));

                LazyStreamSource streamSource = new LazyStreamSource() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    protected StreamSource buildStreamSource() {
                        List<Resource> lstRes = new ArrayList<>();
                        lstRes.add(selectedResource);
                        return StreamDownloadResourceUtil.getStreamSourceSupportExtDrive(lstRes);
                    }

                    @Override
                    public String getFilename() {
                        return (selectedResource instanceof Folder) ? "out.zip" : selectedResource.getName();
                    }
                };

                OnDemandFileDownloader downloaderExt = new OnDemandFileDownloader(streamSource);
                downloaderExt.extend(downloadBtn);

                downloadBtn.addStyleName(UIConstants.BUTTON_LINK);
                downloadBtn.setIcon(FontAwesome.DOWNLOAD);

                MButton moveBtn = new MButton("Move to", clickEvent -> UI.getCurrent().addWindow(new MoveResourceWindow(selectedResource)))
                        .withIcon(FontAwesome.ARROWS).withStyleName(UIConstants.BUTTON_LINK);

                Button deleteBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_DELETE), new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(ClickEvent event) {
                        deleteResourceAction(Collections.singletonList(selectedResource));
                    }
                });
                deleteBtn.addStyleName(UIConstants.BUTTON_LINK);
                deleteBtn.setIcon(FontAwesome.TRASH_O);
                selectedResourceControlLayout.with(new MVerticalLayout(renameBtn, downloadBtn, moveBtn, deleteBtn)
                        .withStyleName("panel-body"));
            } else {
                selectedResourceControlLayout.removeAllComponents();
                selectedResourceControlLayout.removeStyleName(UIConstants.BOX);
            }
        }

        private HorizontalLayout buildResourceRowComp(final Resource resource) {
            if (resource.getName().startsWith(".")) {
                return null;
            }
            final MHorizontalLayout layout = new MHorizontalLayout().withMargin(new MarginInfo(true, false, true, false))
                    .withFullWidth().withStyleName(UIConstants.HOVER_EFFECT_NOT_BOX, "border-bottom");
            layout.addLayoutClickListener(layoutClickEvent -> {
                selectedResource = resource;
                displaySelectedResourceControls();
                for (int i = 0; i < bodyContainer.getComponentCount(); i++) {
                    bodyContainer.getComponent(i).removeStyleName("selected");
                }
                layout.addStyleName("selected");
            });

            CssLayout resIconWrapper = new CssLayout();
            Component resourceIcon = null;
            if (resource instanceof Folder) {
                resourceIcon = (resource instanceof ExternalFolder) ? ELabel.fontIcon(FontAwesome.DROPBOX) : ELabel.fontIcon(FontAwesome.FOLDER);
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
                resourceIcon = ELabel.fontIcon(FileAssetsUtil.getFileIconResource(resource.getName()));
                resourceIcon.addStyleName("icon-38px");
            }
            resIconWrapper.addComponent(resourceIcon);

            layout.with(resIconWrapper).withAlign(resIconWrapper, Alignment.TOP_LEFT);

            MVerticalLayout informationLayout = new MVerticalLayout().withSpacing(false).withMargin(false);

            ELabel resourceLbl = ELabel.h3(resource.getName()).withFullWidth().withStyleName("link", UIConstants.TEXT_ELLIPSIS);
            CssLayout resourceLinkLayout = new CssLayout(resourceLbl);
            resourceLinkLayout.addLayoutClickListener(layoutClickEvent -> {
                if (resource instanceof Folder) {
                    baseFolder = (Folder) resource;
                    resourcesContainer.constructBody((Folder) resource);
                    fileBreadCrumb.gotoFolder((Folder) resource);
                } else {
                    FileDownloadWindow fileDownloadWindow = new FileDownloadWindow((Content) resource);
                    UI.getCurrent().addWindow(fileDownloadWindow);
                }
            });
            resourceLinkLayout.setWidth("100%");
            informationLayout.addComponent(resourceLinkLayout);

            if (StringUtils.isNotBlank(resource.getDescription())) {
                informationLayout.addComponent(new Label(resource.getDescription()));
            }

            MHorizontalLayout moreInfoAboutResLayout = new MHorizontalLayout();

            // If renameResource is dropbox renameResource then we can not
            // define the created date so we do not need to display\
            if (resource.getCreated() != null) {
                ELabel createdTimeLbl = ELabel.html(FontAwesome.CLOCK_O.getHtml() + " " + AppContext.formatPrettyTime
                        (resource.getCreated().getTime()))
                        .withDescription(AppContext.formatDateTime(resource.getCreated().getTime()))
                        .withStyleName(UIConstants.META_INFO);
                moreInfoAboutResLayout.addComponent(createdTimeLbl);
            }

            if (resource instanceof Content) {
                ELabel lbl = ELabel.html(FontAwesome.ARCHIVE.getHtml() + " " + FileUtils.getVolumeDisplay(resource.getSize()))
                        .withStyleName(UIConstants.META_INFO);
                moreInfoAboutResLayout.addComponent(lbl);
            }
            informationLayout.addComponent(moreInfoAboutResLayout);

            // If renameResource is dropbox renameResource then we can not
            // define the
            // created user so we do not need to display, then we assume the
            // current user is created user
            if (StringUtils.isBlank(resource.getCreatedUser())) {
                UserLink usernameLbl = new UserLink(AppContext.getUsername(), AppContext.getUserAvatarId(),
                        AppContext.getUser().getDisplayName());
                usernameLbl.addStyleName(UIConstants.META_INFO);
                moreInfoAboutResLayout.addComponent(usernameLbl);
            } else {
                UserService userService = AppContextUtil.getSpringBean(UserService.class);
                SimpleUser user = userService.findUserByUserNameInAccount(resource.getCreatedUser(), AppContext.getAccountId());
                if (user != null) {
                    UserLink userLink = new UserLink(user.getUsername(), user.getAvatarid(), user.getDisplayName());
                    userLink.addStyleName(UIConstants.META_INFO);
                    moreInfoAboutResLayout.addComponent(userLink);
                } else {
                    Label usernameLbl = new Label(resource.getCreatedBy());
                    usernameLbl.addStyleName(UIConstants.META_INFO);
                    moreInfoAboutResLayout.addComponent(usernameLbl);
                }
            }

            layout.with(informationLayout).withAlign(informationLayout, Alignment.MIDDLE_LEFT).expand(informationLayout);

            return layout;
        }

        Collection<Resource> getSelectedResourceCollection() {
            if (CollectionUtils.isNotEmpty(resources)) {
                return Collections2.filter(resources, Resource::isSelected);
            } else {
                return new ArrayList<>();
            }
        }
    }

    private class RenameResourceWindow extends Window {
        private static final long serialVersionUID = 1L;
        private Resource renameResource;

        RenameResourceWindow(Resource resource) {
            super("Edit folder/file name");
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
            final TextField folderName = layoutHelper.addComponent(new TextField("", renameResource.getName()), "Folder/File Name", 0, 0);
            contentLayout.addComponent(layoutHelper.getLayout());

            final MHorizontalLayout controlButtons = new MHorizontalLayout().withMargin(true);
            final Button saveBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_SAVE), new ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final ClickEvent event) {
                    String oldPath = renameResource.getPath();
                    String parentOldPath = oldPath.substring(0, oldPath.lastIndexOf("/") + 1);

                    String newNameValue = FileUtils.escape(folderName.getValue());
                    String newPath = parentOldPath + newNameValue;

                    if (renameResource.isExternalResource()) {
                        externalResourceService.rename(((ExternalFolder) renameResource).getExternalDrive(), oldPath, newPath);
                    } else {
                        resourceService.rename(oldPath, newPath, AppContext.getUsername());
                    }
                    resourcesContainer.constructBody(baseFolder);

                    close();
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
            controlButtons.with(cancelBtn, saveBtn);
            contentLayout.addComponent(controlButtons);
            contentLayout.setComponentAlignment(controlButtons, Alignment.MIDDLE_RIGHT);

            this.setContent(contentLayout);
        }
    }

    private class AddNewFolderWindow extends Window {
        private static final long serialVersionUID = 1L;


        AddNewFolderWindow() {
            this.setModal(true);
            this.setResizable(false);
            this.setWidth("500px");
            this.setCaption("New Folder");
            this.center();

            MVerticalLayout contentLayout = new MVerticalLayout().withSpacing(false).withMargin(new MarginInfo(false,
                    true, true, false));
            this.setContent(contentLayout);

            GridFormLayoutHelper layoutHelper = GridFormLayoutHelper.defaultFormLayoutHelper(1, 2);
            final TextField folderName = layoutHelper.addComponent(new TextField(), "Folder Name", 0, 0);
            final TextArea descAreaField = layoutHelper.addComponent(new TextArea(), AppContext.getMessage
                    (GenericI18Enum.FORM_DESCRIPTION), 0, 1);
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
                        String desc = descAreaField.getValue();
                        folderVal = FileUtils.escape(folderVal);

                        if (baseFolder instanceof ExternalFolder) {
                            String path = baseFolder.getPath() + "/" + folderVal;
                            externalResourceService.createNewFolder(((ExternalFolder) baseFolder).getExternalDrive(), path);
                        } else {
                            resourceService.createNewFolder(baseFolderPath, folderVal, desc, AppContext.getUsername());
                        }
                        resourcesContainer.constructBody(baseFolder);
                        close();
                    } else {
                        NotificationUtil.showErrorNotification("Folder name must be not null");
                    }
                }
            });
            saveBtn.addStyleName(UIConstants.BUTTON_ACTION);
            saveBtn.setIcon(FontAwesome.SAVE);

            Button cancelBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL), new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final ClickEvent event) {
                    close();
                }
            });
            cancelBtn.addStyleName(UIConstants.BUTTON_OPTION);
            controlsLayout.with(cancelBtn, saveBtn);

            contentLayout.with(controlsLayout).withAlign(controlsLayout, Alignment.MIDDLE_RIGHT);
        }
    }

    private class MultiUploadContentWindow extends Window {
        private static final long serialVersionUID = 1L;

        private final GridFormLayoutHelper layoutHelper;
        private final MultiFileUploadExt multiFileUploadExt;

        MultiUploadContentWindow() {
            super("Upload");
            this.setWidth("600px");
            this.setResizable(false);
            this.setModal(true);
            center();

            VerticalLayout contentLayout = new VerticalLayout();
            contentLayout.setMargin(new MarginInfo(false, false, true, false));
            this.setContent(contentLayout);
            final AttachmentPanel attachmentPanel = new AttachmentPanel();

            layoutHelper = GridFormLayoutHelper.defaultFormLayoutHelper(1, 1);

            multiFileUploadExt = new MultiFileUploadExt(attachmentPanel);
            multiFileUploadExt.addComponent(attachmentPanel);
            multiFileUploadExt.setWidth("100%");

            layoutHelper.addComponent(multiFileUploadExt, "File", 0, 0);
            contentLayout.addComponent(layoutHelper.getLayout());

            MHorizontalLayout controlsLayout = new MHorizontalLayout().withMargin(new MarginInfo(true, true, false, false));

            final Button uploadBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_UPLOAD), new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final ClickEvent event) {
                    List<File> attachments = attachmentPanel.files();
                    if (CollectionUtils.isNotEmpty(attachments)) {
                        for (File attachment : attachments) {
                            try {
                                String attachmentName = FileUtils.escape(attachment.getName());
                                if (!FileUtils.isValidFileName(attachmentName)) {
                                    NotificationUtil.showWarningNotification("Please upload valid file-name except any follow characters : <>:&/\\|?*&");
                                    return;
                                }
                                Content content = new Content(String.format("%s/%s", baseFolder.getPath(), attachmentName));
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

            Button cancelBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL), new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final ClickEvent event) {
                    MultiUploadContentWindow.this.close();
                }
            });
            cancelBtn.addStyleName(UIConstants.BUTTON_OPTION);
            controlsLayout.with(cancelBtn, uploadBtn);

            contentLayout.addComponent(controlsLayout);
            contentLayout.setComponentAlignment(controlsLayout, Alignment.MIDDLE_RIGHT);
        }
    }

    private class MoveResourceWindow extends AbstractResourceMovingWindow {
        private static final long serialVersionUID = 1L;

        MoveResourceWindow(Resource resource) {
            super(new Folder(rootPath), resource);
        }

        @Override
        public void displayAfterMoveSuccess(Folder folder, boolean checking) {
            fileBreadCrumb.gotoFolder(folder);
            resourcesContainer.constructBody(folder);
            if (!checking) {
                NotificationUtil.showNotification("Congrats", "Moved asset(s) successfully.");
            } else {
                NotificationUtil.showWarningNotification("Moving assets is finished, some items can't move to destination. Please " +
                        "check duplicated file-name and try again.");
            }
        }
    }
}
