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
import com.esofthead.mycollab.module.ecm.domain.ExternalDrive;
import com.esofthead.mycollab.module.ecm.domain.ExternalFolder;
import com.esofthead.mycollab.module.ecm.domain.Folder;
import com.esofthead.mycollab.module.ecm.domain.Resource;
import com.esofthead.mycollab.module.ecm.service.ExternalDriveService;
import com.esofthead.mycollab.module.ecm.service.ExternalResourceService;
import com.esofthead.mycollab.module.ecm.service.ResourceMover;
import com.esofthead.mycollab.module.ecm.service.ResourceService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Tree.CollapseEvent;
import com.vaadin.ui.Tree.ExpandEvent;
import org.apache.commons.collections.CollectionUtils;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;

import java.util.Calendar;
import java.util.*;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public abstract class AbstractResourceMovingWindow extends Window {
    private static final long serialVersionUID = 1L;

    private final ResourceService resourceService;
    private final ExternalResourceService externalResourceService;
    private final ExternalDriveService externalDriveService;

    protected TreeTable folderTree;
    protected Folder baseFolder;
    private Collection<Resource> movedResources;
    private final ResourceMover resourceMover;

    public AbstractResourceMovingWindow(Resource resource) {
        this(Arrays.asList(resource));
    }

    public AbstractResourceMovingWindow(Collection<Resource> lstRes) {
        super("Move asset(s)");
        center();
        this.setWidth("600px");
        this.setResizable(false);
        this.movedResources = lstRes;
        this.resourceService = ApplicationContextUtil.getSpringBean(ResourceService.class);
        this.externalResourceService = ApplicationContextUtil.getSpringBean(ExternalResourceService.class);
        this.externalDriveService = ApplicationContextUtil.getSpringBean(ExternalDriveService.class);
        this.resourceMover = ApplicationContextUtil.getSpringBean(ResourceMover.class);
        constructBody();
    }

    private void constructBody() {
        MVerticalLayout contentLayout = new MVerticalLayout();
        this.setContent(contentLayout);

        final HorizontalLayout resourceContainer = new HorizontalLayout();
        resourceContainer.setSizeFull();

        this.folderTree = new TreeTable();
        this.folderTree.setMultiSelect(false);
        this.folderTree.setSelectable(true);
        this.folderTree.setImmediate(true);
        this.folderTree.addContainerProperty("Name", String.class, "");
        this.folderTree.addContainerProperty("Date Modified", String.class, "");
        this.folderTree.setColumnWidth("Date Modified",
                UIConstants.TABLE_DATE_TIME_WIDTH);
        this.folderTree.setColumnExpandRatio("Name", 1.0f);
        this.folderTree.setWidth("100%");

        resourceContainer.addComponent(folderTree);

        this.folderTree.addExpandListener(new Tree.ExpandListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void nodeExpand(final ExpandEvent event) {
                final Folder expandFolder = (Folder) event.getItemId();
                // load externalResource if currentExpandFolder is rootFolder
                if (baseFolder.getPath().equals(expandFolder.getPath())) {
                    List<ExternalDrive> externalDrives = externalDriveService
                            .getExternalDrivesOfUser(AppContext.getUsername());
                    for (ExternalDrive externalDrive : externalDrives) {
                        ExternalFolder externalMapFolder = new ExternalFolder(
                                "/");
                        externalMapFolder.setStorageName(externalDrive
                                .getStoragename());
                        externalMapFolder.setExternalDrive(externalDrive);
                        externalMapFolder.setName(externalDrive.getFoldername());

                        Calendar cal = GregorianCalendar.getInstance();
                        cal.setTime(externalDrive.getCreatedtime());

                        externalMapFolder.setCreated(cal);
                        expandFolder.addChild(externalMapFolder);

                        folderTree.addItem(
                                new Object[]{
                                        externalMapFolder.getName(),
                                        AppContext
                                                .formatDateTime(externalMapFolder
                                                        .getCreated().getTime())},
                                externalMapFolder);

                        folderTree.setItemIcon(externalMapFolder, FontAwesome.DROPBOX);
                        folderTree.setItemCaption(externalMapFolder, externalMapFolder.getName());
                        folderTree.setParent(externalMapFolder, expandFolder);
                    }
                }
                if (expandFolder instanceof ExternalFolder) {
                    List<ExternalFolder> subFolders = externalResourceService.getSubFolders(
                            ((ExternalFolder) expandFolder).getExternalDrive(),
                            expandFolder.getPath());
                    for (final Folder subFolder : subFolders) {
                        expandFolder.addChild(subFolder);
                        Date dateTime = ((ExternalFolder) subFolder).getExternalDrive().getCreatedtime();

                        folderTree.addItem(new Object[]{subFolder.getName(), AppContext.formatDateTime(dateTime)}, subFolder);

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
                                folderTree.addItem(new Object[]{subFolderName,
                                        AppContext.formatDateTime(subFolder.getCreated().getTime())}, subFolder);
                                folderTree.setItemIcon(subFolder, FontAwesome.FOLDER);
                                folderTree.setItemCaption(subFolder, subFolderName);
                                folderTree.setParent(subFolder, expandFolder);
                            }
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
                    folderTree.setItemIcon(collapseFolder, FontAwesome.DROPBOX);
                } else {
                    folderTree.setItemIcon(collapseFolder, FontAwesome.FOLDER);
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
                    folderTree.removeItem(collapseFolder);
                } else {
                    folderTree.removeItem(collapseFolder);
                }
            }
        });

        this.folderTree
                .addItemClickListener(new ItemClickEvent.ItemClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void itemClick(final ItemClickEvent event) {
                        baseFolder = (Folder) event.getItemId();
                    }
                });

        contentLayout.addComponent(resourceContainer);
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
                            resourceMover.moveResource(res, baseFolder,
                                    AppContext.getUsername(),
                                    AppContext.getAccountId());
                        } catch (Exception e) {
                            checkingFail = true;
                        }
                    }
                    AbstractResourceMovingWindow.this.close();
                    displayAfterMoveSuccess(baseFolder, checkingFail);
                }
            }

        });
        moveBtn.setIcon(FontAwesome.ARROWS);
        moveBtn.addStyleName(UIConstants.THEME_GREEN_LINK);
        controlGroupBtnLayout.addComponent(moveBtn);
        Button cancelBtn = new Button(
                AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL),
                new ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(ClickEvent event) {
                        AbstractResourceMovingWindow.this.close();
                    }
                });
        cancelBtn.addStyleName(UIConstants.THEME_GRAY_LINK);
        controlGroupBtnLayout.addComponent(cancelBtn);

        contentLayout.with(controlGroupBtnLayout).withAlign(
                controlGroupBtnLayout, Alignment.MIDDLE_CENTER);
    }

    public abstract void displayAfterMoveSuccess(Folder folder, boolean checking);

    protected abstract void displayFiles();
}
