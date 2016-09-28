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

import com.mycollab.common.i18n.FileI18nEnum;
import com.mycollab.core.MyCollabException;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.module.ecm.StorageNames;
import com.mycollab.module.ecm.domain.ExternalFolder;
import com.mycollab.module.ecm.domain.Folder;
import com.mycollab.module.file.domain.criteria.FileSearchCriteria;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.events.HasSearchHandlers;
import com.mycollab.vaadin.events.SearchHandler;
import com.mycollab.vaadin.mvp.CacheableComponent;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.web.ui.CommonUIFactory;
import com.mycollab.vaadin.web.ui.WebUIConstants;
import com.vaadin.breadcrumb.Breadcrumb;
import com.vaadin.ui.Button;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class FileBreadcrumb extends Breadcrumb implements CacheableComponent, HasSearchHandlers<FileSearchCriteria> {
    private static final long serialVersionUID = 1L;

    private List<SearchHandler<FileSearchCriteria>> handlers;

    private String rootFolderPath;

    public FileBreadcrumb(String rootFolderPath) {
        if (StringUtils.isBlank(rootFolderPath)) {
            throw new MyCollabException("Root folder path can not be empty");
        }
        this.rootFolderPath = rootFolderPath;
        this.setShowAnimationSpeed(Breadcrumb.AnimSpeed.SLOW);
        this.setHideAnimationSpeed(Breadcrumb.AnimSpeed.SLOW);
        this.setUseDefaultClickBehaviour(false);
    }

    void initBreadcrumb() {
        // home Btn ----------------
        this.addLink(new Button(null, clickEvent -> {
            FileSearchCriteria criteria = new FileSearchCriteria();
            criteria.setBaseFolder(rootFolderPath);
            criteria.setRootFolder(rootFolderPath);
            notifySearchHandler(criteria);
        }));

        this.select(0);
        Button documentBtnLink = generateBreadcrumbLink(UserUIContext.getMessage(FileI18nEnum.OPT_MY_DOCUMENTS), clickEvent -> {
            FileSearchCriteria criteria = new FileSearchCriteria();
            criteria.setBaseFolder(rootFolderPath);
            criteria.setRootFolder(rootFolderPath);
            notifySearchHandler(criteria);
        });
        documentBtnLink.addStyleName(WebUIConstants.BUTTON_LINK);
        this.addLink(documentBtnLink);
        this.setLinkEnabled(true, 1);
    }

    void gotoFolder(final Folder folder) {
        initBreadcrumb();

        if (folder == null) {
            throw new MyCollabException("Folder is null");
        } else if (folder instanceof ExternalFolder) {
            displayExternalFolder((ExternalFolder) folder);
        } else {
            displayMyCollabFolder(folder);
        }
    }

    private void displayMyCollabFolder(final Folder folder) {
        String folderPath = folder.getPath();
        if (!folderPath.startsWith(rootFolderPath)) {
            throw new MyCollabException("Invalid path " + rootFolderPath + "---" + folderPath);
        }

        String remainPath = folderPath.substring(rootFolderPath.length());
        if (remainPath.startsWith("/")) {
            remainPath = remainPath.substring(1);
        }

        Button btn1, btn2 = null;
        int index;
        if ((index = remainPath.lastIndexOf('/')) != -1) {
            String pathName = remainPath.substring(index + 1);
            final String newPath = remainPath.substring(0, index);
            remainPath = newPath;
            btn2 = new Button(StringUtils.trim(pathName, 25, true), clickEvent -> {
                FileSearchCriteria criteria = new FileSearchCriteria();
                criteria.setBaseFolder(rootFolderPath + "/" + newPath);
                criteria.setRootFolder(rootFolderPath);
                notifySearchHandler(criteria);
            });
            btn2.setDescription(pathName);
        }

        if ((index = remainPath.lastIndexOf('/')) != -1) {
            String pathName = remainPath.substring(index + 1);
            final String newPath = remainPath.substring(0, index);
            btn1 = new Button(StringUtils.trim(pathName, 25, true), clickEvent -> {
                FileSearchCriteria criteria = new FileSearchCriteria();
                criteria.setBaseFolder(rootFolderPath + "/" + newPath);
                criteria.setRootFolder(rootFolderPath);
                notifySearchHandler(criteria);
            });
            btn1.setDescription(pathName);
        } else {
            final String newPath = remainPath;
            btn1 = new Button(StringUtils.trim(newPath, 25, true), clickEvent -> {
                FileSearchCriteria criteria = new FileSearchCriteria();
                criteria.setBaseFolder(rootFolderPath + "/" + newPath);
                criteria.setRootFolder(rootFolderPath);
                notifySearchHandler(criteria);
            });
            btn1.setDescription(newPath);
        }

        addLink(btn1);

        if (btn2 != null) {
            addLink(btn2);
            select(btn2);
        }
    }

    private void displayExternalFolder(final ExternalFolder folder) {
        String remainPath = folder.getPath();

        Button btn1 = null, btn2 = null;
        int index;
        if ((index = remainPath.lastIndexOf('/')) != -1) {
            if (index == 0) {
                btn1 = new Button(StringUtils.trim(folder.getExternalDrive().getFoldername(), 25, true), clickEvent -> {
                    FileSearchCriteria criteria = new FileSearchCriteria();
                    criteria.setBaseFolder("/");
                    criteria.setRootFolder("/");
                    criteria.setStorageName(StorageNames.DROPBOX);
                    criteria.setExternalDrive(folder.getExternalDrive());
                    notifySearchHandler(criteria);
                });
            }
            String pathName = remainPath.substring(index + 1);
            if (StringUtils.isNotBlank(pathName)) {
                final String newPath = remainPath.substring(0, index);
                remainPath = newPath;
                btn2 = new Button(StringUtils.trim(pathName, 25, true), clickEvent -> {
                    FileSearchCriteria criteria = new FileSearchCriteria();
                    criteria.setBaseFolder(newPath);
                    criteria.setRootFolder("/");
                    criteria.setStorageName(StorageNames.DROPBOX);
                    criteria.setExternalDrive(folder.getExternalDrive());
                    notifySearchHandler(criteria);
                });
                btn2.setDescription(pathName);
            } else {
                remainPath = "";
            }
        }

        if ((index = remainPath.lastIndexOf('/')) != -1) {
            String pathName = remainPath.substring(index + 1);
            final String newPath = remainPath.substring(0, index);
            btn1 = new Button(StringUtils.trim(pathName, 25, true), clickEvent -> {
                FileSearchCriteria criteria = new FileSearchCriteria();
                criteria.setBaseFolder(newPath);

                criteria.setRootFolder("/");
                criteria.setStorageName(StorageNames.DROPBOX);
                criteria.setExternalDrive(folder.getExternalDrive());
                notifySearchHandler(criteria);
            });
            btn1.setDescription(pathName);
        }

        if (btn1 != null) {
            addLink(btn1);
        }

        if (btn2 != null) {
            addLink(btn2);
            select(btn2);
        }
    }

    private static Button generateBreadcrumbLink(String linkname, Button.ClickListener listener) {
        return CommonUIFactory.createButtonTooltip(StringUtils.trim(linkname, 25, true), linkname, listener);
    }

    @Override
    public void addSearchHandler(final SearchHandler<FileSearchCriteria> handler) {
        if (handlers == null) {
            handlers = new ArrayList<>();
        }
        handlers.add(handler);
    }

    @Override
    public void notifySearchHandler(final FileSearchCriteria criteria) {
        if (handlers != null) {
            for (SearchHandler<FileSearchCriteria> handler : handlers) {
                handler.onSearch(criteria);
            }
        }
    }

    @Override
    public void setTotalCountNumber(Integer totalCountNumber) {

    }
}
