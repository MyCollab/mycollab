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
import com.mycollab.common.i18n.ErrorI18nEnum;
import com.mycollab.common.i18n.FileI18nEnum;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.configuration.StorageFactory;
import com.mycollab.core.DebugException;
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
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.resources.LazyStreamSource;
import com.mycollab.vaadin.resources.OnDemandFileDownloader;
import com.mycollab.vaadin.resources.StreamDownloadResourceUtil;
import com.mycollab.vaadin.resources.file.FileAssetsUtil;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.web.ui.AttachmentPanel;
import com.mycollab.vaadin.web.ui.ConfirmDialogExt;
import com.mycollab.vaadin.web.ui.UserLink;
import com.mycollab.vaadin.web.ui.WebUIConstants;
import com.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
                selectedFolder = externalResourceService.getCurrentResourceByPath(criteria.getExternalDrive(), criteria.getBaseFolder());
            } else {
                selectedFolder = resourceService.getResource(criteria.getBaseFolder());
            }

            if (selectedFolder == null) {
                throw new DebugException(String.format("Can not find folder with path %s--%s", criteria.getBaseFolder(),
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
        ELabel headerLbl = ELabel.h2(ProjectAssetsManager.getAsset(ProjectTypeConstants.FILE).getHtml() + " " +
                UserUIContext.getMessage(FileI18nEnum.LIST));

        MButton createBtn = new MButton(UserUIContext.getMessage(FileI18nEnum.ACTION_NEW_FOLDER), clickEvent -> UI.getCurrent().addWindow
                (new AddNewFolderWindow()))
                .withIcon(FontAwesome.PLUS).withStyleName(WebUIConstants.BUTTON_ACTION)
                .withVisible(UserUIContext.canWrite(RolePermissionCollections.PUBLIC_DOCUMENT_ACCESS));

        MButton uploadBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_UPLOAD), clickEvent -> {
            MultiUploadContentWindow multiUploadWindow = new MultiUploadContentWindow();
            UI.getCurrent().addWindow(multiUploadWindow);
        }).withIcon(FontAwesome.UPLOAD).withStyleName(WebUIConstants.BUTTON_ACTION)
                .withVisible(UserUIContext.canWrite(RolePermissionCollections.PUBLIC_DOCUMENT_ACCESS));

        MHorizontalLayout headerLayout = new MHorizontalLayout(headerLbl, new MHorizontalLayout(createBtn, uploadBtn)).expand(headerLbl);
        resourcesContainer = new ResourcesContainer();
        MVerticalLayout floatControl = new MVerticalLayout(headerLayout, fileBreadCrumb).withMargin(new MarginInfo
                (false, false, true, false)).withStyleName("floatControl");
        this.with(floatControl, resourcesContainer);

        fileBreadCrumb.initBreadcrumb();
        resourcesContainer.constructBody(baseFolder);
    }

    private void deleteResourceAction(final Collection<Resource> deletedResources) {
        ConfirmDialogExt.show(UI.getCurrent(), UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, MyCollabUI.getSiteName()),
                UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                UserUIContext.getMessage(GenericI18Enum.BUTTON_YES),
                UserUIContext.getMessage(GenericI18Enum.BUTTON_NO),
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
                                resourceService.removeResource(res.getPath(), UserUIContext.getUsername(),
                                        MyCollabUI.getAccountId());
                            }
                        }

                        resourcesContainer.constructBody(baseFolder);
                        NotificationUtil.showNotification(UserUIContext.getMessage(GenericI18Enum.OPT_CONGRATS),
                                UserUIContext.getMessage(FileI18nEnum.OPT_DELETE_RESOURCES_SUCCESSFULLY));
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
            withFullWidth();
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
                List<ExternalDrive> externalDrives = externalDriveService.getExternalDrivesOfUser(UserUIContext.getUsername());
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
                        bodyContainer.addComponent(resContainer);
                    }
                }
            }
        }

        private void displaySelectedResourceControls() {
            if (selectedResource != null) {
                selectedResourceControlLayout.removeAllComponents();
                ELabel resourceHeaderLbl = ELabel.h3(selectedResource.getName()).withStyleName(UIConstants.TEXT_ELLIPSIS);
                MHorizontalLayout headerLayout = new MHorizontalLayout(resourceHeaderLbl).withMargin(new MarginInfo
                        (false, true, false, true)).withStyleName(WebUIConstants.PANEL_HEADER).withFullWidth().alignAll(Alignment.MIDDLE_LEFT);
                selectedResourceControlLayout.with(headerLayout);

                MButton renameBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.ACTION_RENAME), clickEvent -> UI.getCurrent()
                        .addWindow(new RenameResourceWindow(selectedResource)))
                        .withIcon(FontAwesome.EDIT).withStyleName(WebUIConstants.BUTTON_LINK);

                MButton downloadBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_DOWNLOAD))
                        .withStyleName(WebUIConstants.BUTTON_LINK).withIcon(FontAwesome.DOWNLOAD);

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

                MButton moveBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.ACTION_MOVE) + "...", clickEvent ->
                        UI.getCurrent().addWindow(new MoveResourceWindow(selectedResource)))
                        .withIcon(FontAwesome.ARROWS).withStyleName(WebUIConstants.BUTTON_LINK);

                MButton deleteBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_DELETE),
                        clickEvent -> deleteResourceAction(Collections.singletonList(selectedResource)))
                        .withStyleName(WebUIConstants.BUTTON_LINK).withIcon(FontAwesome.TRASH_O);

                selectedResourceControlLayout.with(new MVerticalLayout(renameBtn, downloadBtn, moveBtn, deleteBtn)
                        .withStyleName("panel-body"));
            } else {
                selectedResourceControlLayout.removeAllComponents();
                selectedResourceControlLayout.removeStyleName(WebUIConstants.BOX);
            }
        }

        private HorizontalLayout buildResourceRowComp(final Resource resource) {
            if (resource.getName().startsWith(".")) {
                return null;
            }
            final MHorizontalLayout layout = new MHorizontalLayout().withMargin(new MarginInfo(true, false, true, false))
                    .withFullWidth().withStyleName(WebUIConstants.HOVER_EFFECT_NOT_BOX, "border-bottom");
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
                    resourceIcon = new Embedded(null, new ExternalResource(StorageFactory.getResourcePath(content.getThumbnail())));
                    resourceIcon.setWidth("38px");
                    resourceIcon.setHeight("38px");
                } else {
                    if (content instanceof ExternalContent) {
                        final byte[] thumbnailBytes = ((ExternalContent) content).getThumbnailBytes();
                        if (thumbnailBytes != null) {
                            resourceIcon = new Embedded(null, new StreamResource((StreamSource) () -> new ByteArrayInputStream(
                                    thumbnailBytes), String.format("thumbnail%s.%s", content.getPath(), "png")));
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
                ELabel createdTimeLbl = ELabel.html(FontAwesome.CLOCK_O.getHtml() + " " + UserUIContext.formatPrettyTime
                        (resource.getCreated().getTime()))
                        .withDescription(UserUIContext.formatDateTime(resource.getCreated().getTime()))
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
                UserLink usernameLbl = new UserLink(UserUIContext.getUsername(), UserUIContext.getUserAvatarId(),
                        UserUIContext.getUser().getDisplayName());
                usernameLbl.addStyleName(UIConstants.META_INFO);
                moreInfoAboutResLayout.addComponent(usernameLbl);
            } else {
                UserService userService = AppContextUtil.getSpringBean(UserService.class);
                SimpleUser user = userService.findUserByUserNameInAccount(resource.getCreatedUser(), MyCollabUI.getAccountId());
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

    private class RenameResourceWindow extends MWindow {
        private static final long serialVersionUID = 1L;
        private Resource renameResource;

        RenameResourceWindow(Resource resource) {
            super(UserUIContext.getMessage(FileI18nEnum.OPT_EDIT_FOLDER_FILE_NAME));
            this.withCenter().withModal(true).withResizable(false).withWidth("400px");
            this.renameResource = resource;
            this.constructBody();
        }

        private void constructBody() {
            VerticalLayout contentLayout = new VerticalLayout();
            GridFormLayoutHelper layoutHelper = GridFormLayoutHelper.defaultFormLayoutHelper(1, 1);
            final TextField folderName = layoutHelper.addComponent(new TextField("", renameResource.getName()),
                    UserUIContext.getMessage(GenericI18Enum.FORM_NAME), 0, 0);
            contentLayout.addComponent(layoutHelper.getLayout());

            MButton saveBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SAVE), clickEvent -> {
                String oldPath = renameResource.getPath();
                String parentOldPath = oldPath.substring(0, oldPath.lastIndexOf("/") + 1);

                String newNameValue = FileUtils.escape(folderName.getValue());
                String newPath = parentOldPath + newNameValue;

                if (renameResource.isExternalResource()) {
                    externalResourceService.rename(((ExternalFolder) renameResource).getExternalDrive(), oldPath, newPath);
                } else {
                    resourceService.rename(oldPath, newPath, UserUIContext.getUsername());
                }
                resourcesContainer.constructBody(baseFolder);
                close();
            }).withIcon(FontAwesome.SAVE).withStyleName(WebUIConstants.BUTTON_ACTION);

            MButton cancelBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CANCEL), clickEvent -> close())
                    .withStyleName(WebUIConstants.BUTTON_OPTION);
            final MHorizontalLayout controlButtons = new MHorizontalLayout(cancelBtn, saveBtn).withMargin(true);
            contentLayout.addComponent(controlButtons);
            contentLayout.setComponentAlignment(controlButtons, Alignment.MIDDLE_RIGHT);

            this.setContent(contentLayout);
        }
    }

    private class AddNewFolderWindow extends MWindow {
        private static final long serialVersionUID = 1L;


        AddNewFolderWindow() {
            this.setCaption(UserUIContext.getMessage(FileI18nEnum.ACTION_NEW_FOLDER));

            MVerticalLayout contentLayout = new MVerticalLayout().withSpacing(false).withMargin(new MarginInfo(false, true, true, false));
            withModal(true).withResizable(false).withWidth("500px").withCenter().withContent(contentLayout);

            GridFormLayoutHelper layoutHelper = GridFormLayoutHelper.defaultFormLayoutHelper(1, 2);
            final TextField folderName = layoutHelper.addComponent(new TextField(), UserUIContext.getMessage(GenericI18Enum.FORM_NAME), 0, 0);
            final TextArea descAreaField = layoutHelper.addComponent(new TextArea(), UserUIContext.getMessage(GenericI18Enum.FORM_DESCRIPTION), 0, 1);
            contentLayout.addComponent(layoutHelper.getLayout());

            MButton saveBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SAVE), clickEvent -> {
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
                        resourceService.createNewFolder(baseFolderPath, folderVal, desc, UserUIContext.getUsername());
                    }
                    resourcesContainer.constructBody(baseFolder);
                    close();
                } else {
                    NotificationUtil.showErrorNotification(UserUIContext.getMessage(ErrorI18nEnum.FIELD_MUST_NOT_NULL,
                            UserUIContext.getMessage(GenericI18Enum.FORM_NAME)));
                }
            }).withIcon(FontAwesome.SAVE).withStyleName(WebUIConstants.BUTTON_ACTION);

            MButton cancelBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CANCEL), clickEvent -> close())
                    .withStyleName(WebUIConstants.BUTTON_OPTION);
            MHorizontalLayout controlsLayout = new MHorizontalLayout(cancelBtn, saveBtn).withMargin(true);
            contentLayout.with(controlsLayout).withAlign(controlsLayout, Alignment.MIDDLE_RIGHT);
        }
    }

    private class MultiUploadContentWindow extends MWindow {
        private static final long serialVersionUID = 1L;

        private final GridFormLayoutHelper layoutHelper;

        MultiUploadContentWindow() {
            super(UserUIContext.getMessage(GenericI18Enum.BUTTON_UPLOAD));
            this.withWidth("600px").withResizable(false).withModal(true).withCenter();

            VerticalLayout contentLayout = new VerticalLayout();
            contentLayout.setMargin(new MarginInfo(false, false, true, false));
            this.setContent(contentLayout);

            layoutHelper = GridFormLayoutHelper.defaultFormLayoutHelper(1, 1);

            final AttachmentPanel attachmentPanel = new AttachmentPanel();
            layoutHelper.addComponent(attachmentPanel, UserUIContext.getMessage(FileI18nEnum.SINGLE), 0, 0);
            contentLayout.addComponent(layoutHelper.getLayout());

            MButton uploadBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_UPLOAD), clickEvent -> {
                List<File> attachments = attachmentPanel.files();
                if (CollectionUtils.isNotEmpty(attachments)) {
                    for (File attachment : attachments) {
                        try {
                            String attachmentName = FileUtils.escape(attachment.getName());
                            if (!FileUtils.isValidFileName(attachmentName)) {
                                NotificationUtil.showWarningNotification(UserUIContext.getMessage(FileI18nEnum.ERROR_INVALID_FILE_NAME));
                                return;
                            }
                            Content content = new Content(String.format("%s/%s", baseFolder.getPath(), attachmentName));
                            content.setSize(attachment.length());
                            FileInputStream fileInputStream = new FileInputStream(attachment);

                            if (baseFolder instanceof ExternalFolder) {
                                externalResourceService.saveContent(((ExternalFolder) baseFolder)
                                        .getExternalDrive(), content, fileInputStream);
                            } else
                                resourceService.saveContent(content, UserUIContext.getUsername(),
                                        fileInputStream, MyCollabUI.getAccountId());
                        } catch (IOException e) {
                            throw new MyCollabException(e);
                        }
                    }
                    resourcesContainer.constructBody(baseFolder);
                    close();
                    NotificationUtil.showNotification(UserUIContext.getMessage(GenericI18Enum.OPT_CONGRATS),
                            UserUIContext.getMessage(FileI18nEnum.OPT_UPLOAD_FILE_SUCCESSFULLY));
                } else {
                    NotificationUtil.showWarningNotification(UserUIContext.getMessage(FileI18nEnum.NOT_ATTACH_FILE_WARNING));
                }
            }).withStyleName(WebUIConstants.BUTTON_ACTION).withIcon(FontAwesome.UPLOAD);

            MButton cancelBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CANCEL), clickEvent -> close())
                    .withStyleName(WebUIConstants.BUTTON_OPTION);
            MHorizontalLayout controlsLayout = new MHorizontalLayout(cancelBtn, uploadBtn).withMargin(true);
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
                NotificationUtil.showNotification(UserUIContext.getMessage(GenericI18Enum.OPT_CONGRATS),
                        UserUIContext.getMessage(FileI18nEnum.OPT_MOVE_ASSETS_SUCCESSFULLY));
            } else {
                NotificationUtil.showWarningNotification(UserUIContext.getMessage(FileI18nEnum.ERROR_SOME_FILES_MOVING_ERROR));
            }
        }
    }
}
