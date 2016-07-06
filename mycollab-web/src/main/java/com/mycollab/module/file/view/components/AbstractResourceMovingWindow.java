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
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.web.ui.UIConstants;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Tree.CollapseEvent;
import com.vaadin.ui.Tree.ExpandEvent;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.jouni.restrain.Restrain;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.Calendar;
import java.util.*;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public abstract class AbstractResourceMovingWindow extends Window {
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
        super("Moving asset(s)");
        this.baseFolder = baseFolder;
        center();
        this.setWidth("600px");
        this.setModal(true);
        this.setResizable(false);
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

        folderTree.addExpandListener(new Tree.ExpandListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void nodeExpand(final ExpandEvent event) {
                final Folder expandFolder = (Folder) event.getItemId();
                // load externalResource if currentExpandFolder is rootFolder
                if (baseFolder.getPath().equals(expandFolder.getPath())) {
                    List<ExternalDrive> externalDrives = externalDriveService.getExternalDrivesOfUser(AppContext.getUsername());
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
                for (Folder folder : collapseFolder.getChilds()) {
                    recursiveRemoveSubItem(folder);
                }
            }

            private void recursiveRemoveSubItem(Folder collapseFolder) {
                List<Folder> childFolders = collapseFolder.getChilds();
                if (childFolders.size() > 0) {
                    for (Folder subFolder : childFolders) {
                        recursiveRemoveSubItem(subFolder);
                    }
                    folderTree.removeItem(collapseFolder);
                } else {
                    folderTree.removeItem(collapseFolder);
                }
            }
        });

        folderTree.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void itemClick(final ItemClickEvent event) {
                baseFolder = (Folder) event.getItemId();
            }
        });

        CssLayout treeWrapper = new CssLayout(folderTree);
        treeWrapper.setSizeFull();
        contentLayout.addComponent(treeWrapper);
        displayFiles();

        MHorizontalLayout controlGroupBtnLayout = new MHorizontalLayout();

        Button moveBtn = new Button("Move", new ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                if (!CollectionUtils.isEmpty(movedResources)) {
                    boolean checkingFail = false;
                    for (Resource res : movedResources) {
                        try {
                            resourceMover.moveResource(res, baseFolder, AppContext.getUsername(), AppContext.getAccountId());
                        } catch (Exception e) {
                            checkingFail = true;
                            LOG.error("Error", e);
                        }
                    }
                    close();
                    displayAfterMoveSuccess(baseFolder, checkingFail);
                }
            }

        });
        moveBtn.setIcon(FontAwesome.ARROWS);
        moveBtn.addStyleName(UIConstants.BUTTON_ACTION);

        Button cancelBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL), new ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                close();
            }
        });
        cancelBtn.addStyleName(UIConstants.BUTTON_OPTION);
        controlGroupBtnLayout.with(cancelBtn, moveBtn);

        contentLayout.with(controlGroupBtnLayout).withAlign(controlGroupBtnLayout, Alignment.MIDDLE_RIGHT);
    }

    public abstract void displayAfterMoveSuccess(Folder folder, boolean checking);

    private void displayFiles() {
        folderTree.addItem(baseFolder);
        folderTree.setItemCaption(baseFolder, "Documents");
        folderTree.expandItem(baseFolder);
    }
}
