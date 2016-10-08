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
package com.mycollab.module.file.view;

import com.mycollab.common.i18n.FileI18nEnum;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.ecm.domain.ExternalDrive;
import com.mycollab.module.ecm.domain.ExternalFolder;
import com.mycollab.module.ecm.domain.Folder;
import com.mycollab.module.ecm.domain.Resource;
import com.mycollab.module.ecm.service.ExternalDriveService;
import com.mycollab.module.ecm.service.ExternalResourceService;
import com.mycollab.module.ecm.service.ResourceMover;
import com.mycollab.module.ecm.service.ResourceService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.web.ui.WebUIConstants;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.Tree.CollapseEvent;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.jouni.restrain.Restrain;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

import java.util.Calendar;
import java.util.*;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
abstract class AbstractResourceMovingWindow extends MWindow {
    private static final long serialVersionUID = 1L;
    private static Logger LOG = LoggerFactory.getLogger(AbstractResourceMovingWindow.class);

    private final ResourceService resourceService;
    private final ExternalResourceService externalResourceService;
    private final ExternalDriveService externalDriveService;

    private Tree folderTree;
    private Folder baseFolder;
    private Collection<Resource> movedResources;
    private final ResourceMover resourceMover;

    AbstractResourceMovingWindow(Folder baseFolder, Resource resource) {
        this(baseFolder, Collections.singletonList(resource));
    }

    AbstractResourceMovingWindow(Folder baseFolder, Collection<Resource> lstRes) {
        super(UserUIContext.getMessage(FileI18nEnum.ACTION_MOVE_ASSETS));
        withModal(true).withResizable(false).withWidth("600px").withCenter();
        this.baseFolder = baseFolder;
        this.movedResources = lstRes;
        this.resourceService = AppContextUtil.getSpringBean(ResourceService.class);
        this.externalResourceService = AppContextUtil.getSpringBean(ExternalResourceService.class);
        this.externalDriveService = AppContextUtil.getSpringBean(ExternalDriveService.class);
        this.resourceMover = AppContextUtil.getSpringBean(ResourceMover.class);
        constructBody();
    }

    private void constructBody() {
        MVerticalLayout contentLayout = new MVerticalLayout();
        new Restrain(contentLayout).setMaxHeight("600px");
        this.setContent(contentLayout);

        folderTree = new Tree();
        folderTree.setMultiSelect(false);
        folderTree.setSelectable(true);
        folderTree.setImmediate(true);
        folderTree.setSizeFull();

        folderTree.addExpandListener(expandEvent -> {
            final Folder expandFolder = (Folder) expandEvent.getItemId();
            // load externalResource if currentExpandFolder is rootFolder
            if (baseFolder.getPath().equals(expandFolder.getPath())) {
                List<ExternalDrive> externalDrives = externalDriveService.getExternalDrivesOfUser(UserUIContext.getUsername());
                for (ExternalDrive externalDrive : externalDrives) {
                    ExternalFolder externalMapFolder = new ExternalFolder("/");
                    externalMapFolder.setStorageName(externalDrive.getStoragename());
                    externalMapFolder.setExternalDrive(externalDrive);
                    externalMapFolder.setName(externalDrive.getFoldername());

                    Calendar cal = GregorianCalendar.getInstance();
                    cal.setTime(externalDrive.getCreatedtime());

                    externalMapFolder.setCreated(cal);
                    expandFolder.addChild(externalMapFolder);
                    folderTree.addItem(externalMapFolder);
                    folderTree.setItemIcon(externalMapFolder, FontAwesome.DROPBOX);
                    folderTree.setItemCaption(externalMapFolder, externalMapFolder.getName());
                    folderTree.setParent(externalMapFolder, expandFolder);
                }
            }
            if (expandFolder instanceof ExternalFolder) {
                List<ExternalFolder> subFolders = externalResourceService.getSubFolders(
                        ((ExternalFolder) expandFolder).getExternalDrive(), expandFolder.getPath());
                for (final Folder subFolder : subFolders) {
                    expandFolder.addChild(subFolder);
                    folderTree.addItem(subFolder);
                    folderTree.setItemIcon(subFolder, FontAwesome.DROPBOX);
                    folderTree.setItemCaption(subFolder, subFolder.getName());
                    folderTree.setParent(subFolder, expandFolder);
                }
            } else {
                final List<Folder> subFolders = resourceService.getSubFolders(expandFolder.getPath());
                folderTree.setItemIcon(expandFolder, FontAwesome.FOLDER_OPEN);

                if (subFolders != null) {
                    for (final Folder subFolder : subFolders) {
                        String subFolderName = subFolder.getName();
                        if (!subFolderName.startsWith(".")) {
                            expandFolder.addChild(subFolder);
                            folderTree.addItem(subFolder);
                            folderTree.setItemIcon(subFolder, FontAwesome.FOLDER);
                            folderTree.setItemCaption(subFolder, subFolderName);
                            folderTree.setParent(subFolder, expandFolder);
                        }
                    }
                }
            }
        });

        folderTree.addCollapseListener(new Tree.CollapseListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void nodeCollapse(final CollapseEvent event) {
                final Folder collapseFolder = (Folder) event.getItemId();
                if (collapseFolder instanceof ExternalFolder) {
                    folderTree.setItemIcon(collapseFolder, FontAwesome.DROPBOX);
                } else {
                    folderTree.setItemIcon(collapseFolder, FontAwesome.FOLDER);
                }
                collapseFolder.getChilds().forEach(this::recursiveRemoveSubItem);
            }

            private void recursiveRemoveSubItem(Folder collapseFolder) {
                List<Folder> childFolders = collapseFolder.getChilds();
                if (childFolders.size() > 0) {
                    childFolders.forEach(this::recursiveRemoveSubItem);
                    folderTree.removeItem(collapseFolder);
                } else {
                    folderTree.removeItem(collapseFolder);
                }
            }
        });

        folderTree.addItemClickListener(itemClickEvent -> baseFolder = (Folder) itemClickEvent.getItemId());

        CssLayout treeWrapper = new CssLayout(folderTree);
        treeWrapper.setSizeFull();
        contentLayout.addComponent(treeWrapper);
        displayFiles();

        MButton moveBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.ACTION_MOVE), clickEvent -> {
            if (!CollectionUtils.isEmpty(movedResources)) {
                boolean checkingFail = false;
                for (Resource res : movedResources) {
                    try {
                        resourceMover.moveResource(res, baseFolder, UserUIContext.getUsername(), MyCollabUI.getAccountId());
                    } catch (Exception e) {
                        checkingFail = true;
                        LOG.error("Error", e);
                    }
                }
                close();
                displayAfterMoveSuccess(baseFolder, checkingFail);
            }
        }).withIcon(FontAwesome.ARROWS).withStyleName(WebUIConstants.BUTTON_ACTION);

        MButton cancelBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CANCEL), clickEvent -> close())
                .withStyleName(WebUIConstants.BUTTON_OPTION);

        MHorizontalLayout controlGroupBtnLayout = new MHorizontalLayout(cancelBtn, moveBtn);
        contentLayout.with(controlGroupBtnLayout).withAlign(controlGroupBtnLayout, Alignment.MIDDLE_RIGHT);
    }

    public abstract void displayAfterMoveSuccess(Folder folder, boolean checking);

    private void displayFiles() {
        folderTree.addItem(baseFolder);
        folderTree.setItemCaption(baseFolder, UserUIContext.getMessage(FileI18nEnum.OPT_MY_DOCUMENTS));
        folderTree.expandItem(baseFolder);
    }
}
