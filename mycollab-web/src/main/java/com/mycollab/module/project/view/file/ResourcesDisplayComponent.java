/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view.file;

import com.mycollab.common.i18n.ErrorI18nEnum;
import com.mycollab.common.i18n.FileI18nEnum;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.DebugException;
import com.mycollab.core.MyCollabException;
import com.mycollab.core.utils.DateTimeUtils;
import com.mycollab.core.utils.FileUtils;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.form.view.LayoutType;
import com.mycollab.module.ecm.domain.Content;
import com.mycollab.module.ecm.domain.Folder;
import com.mycollab.module.ecm.domain.Resource;
import com.mycollab.module.ecm.service.ResourceService;
import com.mycollab.module.file.StorageUtils;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.event.FileEvent;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.view.ProjectView;
import com.mycollab.module.user.domain.SimpleUser;
import com.mycollab.module.user.service.UserService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.resources.LazyStreamSource;
import com.mycollab.vaadin.resources.OnDemandFileDownloader;
import com.mycollab.vaadin.resources.file.FileAssetsUtil;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.ui.UIUtils;
import com.mycollab.vaadin.web.ui.AttachmentPanel;
import com.mycollab.vaadin.web.ui.ConfirmDialogExt;
import com.mycollab.vaadin.web.ui.UserLink;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
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
class ResourcesDisplayComponent extends MVerticalLayout {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(ResourcesDisplayComponent.class);

    private ResourceService resourceService;
    private Folder baseFolder;
    private String rootPath;

    private FileBreadcrumb fileBreadCrumb;
    private ResourcesContainer resourcesContainer;

    ResourcesDisplayComponent(Folder rootFolder) {
        this.baseFolder = rootFolder;
        this.rootPath = rootFolder.getPath();
        resourceService = AppContextUtil.getSpringBean(ResourceService.class);

        withSpacing(false).withMargin(new MarginInfo(true, false, true, false));
        fileBreadCrumb = ViewManager.getCacheComponent(FileBreadcrumb.class);
        fileBreadCrumb.setRootFolderPath(rootPath);
        fileBreadCrumb.addSearchHandler(criteria -> {
            Resource selectedFolder = resourceService.getResource(criteria.getBaseFolder());

            if (selectedFolder == null) {
                throw new DebugException(String.format("Can not find folder with path %s--%s in account", criteria.getBaseFolder(), criteria.getRootFolder()));
            } else if (!(selectedFolder instanceof Folder)) {
                LOG.error(String.format("Expect folder but the result is file %s--%s", criteria.getBaseFolder(), criteria.getRootFolder()));
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
                .withIcon(VaadinIcons.PLUS).withStyleName(WebThemes.BUTTON_ACTION)
                .withVisible(UserUIContext.canWrite(ProjectRolePermissionCollections.FILES));

        MButton uploadBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_UPLOAD), clickEvent -> {
            MultiUploadContentWindow multiUploadWindow = new MultiUploadContentWindow();
            UI.getCurrent().addWindow(multiUploadWindow);
        }).withIcon(VaadinIcons.UPLOAD).withStyleName(WebThemes.BUTTON_ACTION)
                .withVisible(UserUIContext.canWrite(ProjectRolePermissionCollections.FILES));

        MHorizontalLayout headerLayout = new MHorizontalLayout(headerLbl, new MHorizontalLayout(createBtn, uploadBtn)).expand(headerLbl);
        resourcesContainer = new ResourcesContainer();
        MVerticalLayout floatControl = new MVerticalLayout(headerLayout, fileBreadCrumb).withMargin(new MarginInfo
                (false, false, true, false));
        this.with(floatControl, resourcesContainer);

        fileBreadCrumb.initBreadcrumb();
        resourcesContainer.constructBody(baseFolder);
    }

    private void deleteResourceAction(final Collection<Resource> deletedResources) {
        ConfirmDialogExt.show(UI.getCurrent(), UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, AppUI.getSiteName()),
                UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                UserUIContext.getMessage(GenericI18Enum.ACTION_YES),
                UserUIContext.getMessage(GenericI18Enum.ACTION_NO),
                confirmDialog -> {
                    if (confirmDialog.isConfirmed()) {
                        for (Resource res : deletedResources) {
                            if (res instanceof Folder) {
                                EventBusFactory.getInstance().post(new FileEvent.ResourceRemovedEvent
                                        (ResourcesDisplayComponent.this, res));
                            }
                            resourceService.removeResource(res.getPath(), UserUIContext.getUsername(), true, AppUI.getAccountId());
                        }

                        resourcesContainer.constructBody(baseFolder);
                        NotificationUtil.showNotification(UserUIContext.getMessage(GenericI18Enum.OPT_CONGRATS),
                                UserUIContext.getMessage(FileI18nEnum.OPT_DELETE_RESOURCES_SUCCESSFULLY));
                    }
                });
    }

    private void constructBodyItemContainer(Folder folder) {
        this.baseFolder = folder;
        fileBreadCrumb.gotoFolder(folder);
        resourcesContainer.constructBody(folder);
    }

    private class ResourcesContainer extends MVerticalLayout {
        private Resource selectedResource;
        private List<Resource> resources;
        private MVerticalLayout selectedResourceControlLayout;
        private Panel resourceActionPanel;

        ResourcesContainer() {
            withMargin(false).withSpacing(false).withFullWidth();
            this.addStyleName(WebThemes.BORDER_TOP);
        }

        private void constructBody(Folder currentFolder) {
            this.removeAllComponents();
            resources = resourceService.getResources(currentFolder.getPath());

            if (CollectionUtils.isNotEmpty(resources)) {
                for (Resource res : resources) {
                    ComponentContainer resContainer = buildResourceRowComp(res);
                    if (resContainer != null) {
                        this.addComponent(resContainer);
                    }
                }
            }
        }

        private void displaySelectedResourceControls() {
            if (resourceActionPanel == null) {
                ProjectView projectView = UIUtils.getRoot(this, ProjectView.class);
                selectedResourceControlLayout = new MVerticalLayout().withMargin(new MarginInfo(false, true, true, true));
                resourceActionPanel = new Panel(UserUIContext.getMessage(GenericI18Enum.OPT_DETAILS), selectedResourceControlLayout);
                UIUtils.makeStackPanel(resourceActionPanel);
                projectView.addComponentToRightBar(resourceActionPanel);
            }

            if (selectedResource != null) {
                resourceActionPanel.setVisible(true);
                resourceActionPanel.setCaption(selectedResource.getName());
                selectedResourceControlLayout.removeAllComponents();

                MButton renameBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.ACTION_RENAME), clickEvent -> UI.getCurrent()
                        .addWindow(new RenameResourceWindow(selectedResource)))
                        .withIcon(VaadinIcons.EDIT).withStyleName(WebThemes.BUTTON_LINK)
                        .withVisible(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.FILES));

                MButton downloadBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_DOWNLOAD))
                        .withStyleName(WebThemes.BUTTON_LINK).withIcon(VaadinIcons.DOWNLOAD)
                        .withVisible(CurrentProjectVariables.canRead(ProjectRolePermissionCollections.FILES));

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
                        .withIcon(VaadinIcons.ARROWS).withStyleName(WebThemes.BUTTON_LINK)
                        .withVisible(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.FILES));

                MButton deleteBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_DELETE),
                        clickEvent -> deleteResourceAction(Collections.singletonList(selectedResource)))
                        .withStyleName(WebThemes.DANGER, WebThemes.BUTTON_LINK).withIcon(VaadinIcons.TRASH)
                        .withVisible(CurrentProjectVariables.canAccess(ProjectRolePermissionCollections.FILES));

                selectedResourceControlLayout.with(renameBtn, downloadBtn, moveBtn, deleteBtn);
            } else {
                resourceActionPanel.setVisible(false);
            }
        }

        private HorizontalLayout buildResourceRowComp(final Resource resource) {
            if (resource.getName().startsWith(".")) {
                return null;
            }
            MHorizontalLayout layout = new MHorizontalLayout().withMargin(new MarginInfo(true, false, true, true))
                    .withFullWidth().withStyleName(WebThemes.HOVER_EFFECT_NOT_BOX, WebThemes.BORDER_BOTTOM);
            layout.addLayoutClickListener(layoutClickEvent -> {
                selectedResource = resource;
                displaySelectedResourceControls();
                for (int i = 0; i < this.getComponentCount(); i++) {
                    this.getComponent(i).removeStyleName("selected");
                }
                layout.addStyleName("selected");
            });

            CssLayout resIconWrapper = new CssLayout();
            Component resourceIcon = null;
            if (resource instanceof Folder) {
                resourceIcon = ELabel.fontIcon(VaadinIcons.FOLDER);
                resourceIcon.addStyleName("icon-38px");
            } else if (resource instanceof Content) {
                Content content = (Content) resource;
                if (StringUtils.isNotBlank(content.getThumbnail())) {
                    resourceIcon = new Embedded(null, new ExternalResource(StorageUtils.getResourcePath(content.getThumbnail())));
                    resourceIcon.setWidth("38px");
                    resourceIcon.setHeight("38px");
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

            ELabel resourceLbl = ELabel.h3(resource.getName()).withFullWidth().withStyleName("link", WebThemes.TEXT_ELLIPSIS);
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
                ELabel createdTimeLbl = ELabel.html(VaadinIcons.CLOCK.getHtml() + " " + UserUIContext.formatPrettyTime
                        (DateTimeUtils.toLocalDateTime(resource.getCreated())))
                        .withDescription(UserUIContext.formatDateTime(DateTimeUtils.toLocalDateTime(resource.getCreated())))
                        .withStyleName(WebThemes.META_INFO);
                moreInfoAboutResLayout.addComponent(createdTimeLbl);
            }

            if (resource instanceof Content) {
                ELabel lbl = ELabel.html(VaadinIcons.ARCHIVE.getHtml() + " " + FileUtils.getVolumeDisplay(resource.getSize()))
                        .withStyleName(WebThemes.META_INFO);
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
                usernameLbl.addStyleName(WebThemes.META_INFO);
                moreInfoAboutResLayout.addComponent(usernameLbl);
            } else {
                UserService userService = AppContextUtil.getSpringBean(UserService.class);
                SimpleUser user = userService.findUserByUserNameInAccount(resource.getCreatedUser(), AppUI.getAccountId());
                if (user != null) {
                    UserLink userLink = new UserLink(user.getUsername(), user.getAvatarid(), user.getDisplayName());
                    userLink.addStyleName(WebThemes.META_INFO);
                    moreInfoAboutResLayout.addComponent(userLink);
                } else {
                    moreInfoAboutResLayout.addComponent(new ELabel(resource.getCreatedBy()).withStyleName(WebThemes.META_INFO));
                }
            }

            layout.with(informationLayout).withAlign(informationLayout, Alignment.MIDDLE_LEFT).expand(informationLayout);
            return layout;
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
            GridFormLayoutHelper layoutHelper = GridFormLayoutHelper.defaultFormLayoutHelper(LayoutType.ONE_COLUMN);
            TextField folderName = layoutHelper.addComponent(new TextField("", renameResource.getName()),
                    UserUIContext.getMessage(GenericI18Enum.FORM_NAME), 0, 0);
            contentLayout.addComponent(layoutHelper.getLayout());

            MButton saveBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SAVE), clickEvent -> {
                String oldPath = renameResource.getPath();
                String parentOldPath = oldPath.substring(0, oldPath.lastIndexOf("/") + 1);

                String newNameValue = FileUtils.escape(folderName.getValue());
                String newPath = parentOldPath + newNameValue;

                resourceService.rename(oldPath, newPath, UserUIContext.getUsername());
                resourcesContainer.constructBody(baseFolder);
                close();
            }).withIcon(VaadinIcons.CLIPBOARD).withStyleName(WebThemes.BUTTON_ACTION)
                    .withClickShortcut(KeyCode.ENTER).withEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.FILES));

            MButton cancelBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CANCEL), clickEvent -> close())
                    .withStyleName(WebThemes.BUTTON_OPTION);
            MHorizontalLayout controlButtons = new MHorizontalLayout(cancelBtn, saveBtn).withMargin(false);

            contentLayout.addComponent(controlButtons);
            contentLayout.setComponentAlignment(controlButtons, Alignment.MIDDLE_RIGHT);

            this.setContent(contentLayout);
        }
    }

    private class AddNewFolderWindow extends MWindow {
        private static final long serialVersionUID = 1L;

        AddNewFolderWindow() {
            this.setCaption(UserUIContext.getMessage(FileI18nEnum.ACTION_NEW_FOLDER));

            MVerticalLayout contentLayout = new MVerticalLayout().withSpacing(false).withMargin(new MarginInfo(false, true, true, true));
            withModal(true).withResizable(false).withWidth("500px").withCenter().withContent(contentLayout);

            GridFormLayoutHelper layoutHelper = GridFormLayoutHelper.defaultFormLayoutHelper(LayoutType.ONE_COLUMN);
            TextField folderName = layoutHelper.addComponent(new TextField(), UserUIContext.getMessage(GenericI18Enum.FORM_NAME), 0, 0);
            TextArea descAreaField = layoutHelper.addComponent(new TextArea(), UserUIContext.getMessage(GenericI18Enum.FORM_DESCRIPTION), 0, 1);
            contentLayout.addComponent(layoutHelper.getLayout());

            MButton saveBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SAVE), clickEvent -> {
                String folderVal = folderName.getValue();

                if (StringUtils.isNotBlank(folderVal)) {
                    FileUtils.assertValidFolderName(folderVal);
                    String baseFolderPath = baseFolder.getPath();
                    String desc = descAreaField.getValue();
                    folderVal = FileUtils.escape(folderVal);

                    resourceService.createNewFolder(baseFolderPath, folderVal, desc, UserUIContext.getUsername());
                    resourcesContainer.constructBody(baseFolder);
                    close();
                } else {
                    NotificationUtil.showErrorNotification(UserUIContext.getMessage(ErrorI18nEnum.FIELD_MUST_NOT_NULL,
                            UserUIContext.getMessage(GenericI18Enum.FORM_NAME)));
                }
            }).withIcon(VaadinIcons.CLIPBOARD).withStyleName(WebThemes.BUTTON_ACTION)
                    .withClickShortcut(KeyCode.ENTER);

            MButton cancelBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CANCEL), clickEvent -> close())
                    .withStyleName(WebThemes.BUTTON_OPTION);
            MHorizontalLayout controlsLayout = new MHorizontalLayout(cancelBtn, saveBtn);
            contentLayout.with(controlsLayout).withAlign(controlsLayout, Alignment.MIDDLE_RIGHT);
        }
    }

    private class MultiUploadContentWindow extends MWindow {
        private static final long serialVersionUID = 1L;

        private final GridFormLayoutHelper layoutHelper;

        MultiUploadContentWindow() {
            super(UserUIContext.getMessage(GenericI18Enum.BUTTON_UPLOAD));
            MVerticalLayout contentLayout = new MVerticalLayout();
            this.withWidth("600px").withResizable(false).withModal(true).withContent(contentLayout).withCenter();

            layoutHelper = GridFormLayoutHelper.defaultFormLayoutHelper(LayoutType.ONE_COLUMN);

            AttachmentPanel attachmentPanel = new AttachmentPanel();
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

                            resourceService.saveContent(content, UserUIContext.getUsername(), fileInputStream, AppUI.getAccountId());
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
            }).withStyleName(WebThemes.BUTTON_ACTION).withIcon(VaadinIcons.UPLOAD);

            MButton cancelBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CANCEL), clickEvent -> close())
                    .withStyleName(WebThemes.BUTTON_OPTION);
            MHorizontalLayout controlsLayout = new MHorizontalLayout(cancelBtn, uploadBtn);
            contentLayout.with(controlsLayout).withAlign(controlsLayout, Alignment.MIDDLE_RIGHT);
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
