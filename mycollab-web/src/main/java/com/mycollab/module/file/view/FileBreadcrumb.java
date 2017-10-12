/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.file.view;

import com.mycollab.common.i18n.FileI18nEnum;
import com.mycollab.core.MyCollabException;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.module.ecm.StorageNames;
import com.mycollab.module.ecm.domain.ExternalFolder;
import com.mycollab.module.ecm.domain.Folder;
import com.mycollab.module.file.domain.criteria.FileSearchCriteria;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.event.HasSearchHandlers;
import com.mycollab.vaadin.event.SearchHandler;
import com.mycollab.vaadin.mvp.CacheableComponent;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.CommonUIFactory;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class FileBreadcrumb extends MHorizontalLayout implements CacheableComponent, HasSearchHandlers<FileSearchCriteria> {
    private static final long serialVersionUID = 1L;

    private List<SearchHandler<FileSearchCriteria>> handlers;

    private String rootFolderPath;

    public FileBreadcrumb(String rootFolderPath) {
        if (StringUtils.isBlank(rootFolderPath)) {
            throw new MyCollabException("Root folder path can not be empty");
        }
        setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        this.rootFolderPath = rootFolderPath;
    }

    void initBreadcrumb() {
        removeAllComponents();

        MButton documentBtnLink = generateBreadcrumbLink(UserUIContext.getMessage(FileI18nEnum.OPT_MY_DOCUMENTS), clickEvent -> {
            FileSearchCriteria criteria = new FileSearchCriteria();
            criteria.setBaseFolder(rootFolderPath);
            criteria.setRootFolder(rootFolderPath);
            notifySearchHandler(criteria);
        });
        this.with(documentBtnLink);
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

        MButton btn1, btn2 = null;
        int index;
        if ((index = remainPath.lastIndexOf('/')) != -1) {
            String pathName = remainPath.substring(index + 1);
            final String newPath = remainPath.substring(0, index);
            remainPath = newPath;
            btn2 = new MButton(StringUtils.trim(pathName, 25, true), clickEvent -> {
                FileSearchCriteria criteria = new FileSearchCriteria();
                criteria.setBaseFolder(rootFolderPath + "/" + newPath);
                criteria.setRootFolder(rootFolderPath);
                notifySearchHandler(criteria);
            }).withDescription(pathName).withStyleName(WebThemes.BUTTON_LINK);
        }

        if ((index = remainPath.lastIndexOf('/')) != -1) {
            String pathName = remainPath.substring(index + 1);
            final String newPath = remainPath.substring(0, index);
            btn1 = new MButton(StringUtils.trim(pathName, 25, true), clickEvent -> {
                FileSearchCriteria criteria = new FileSearchCriteria();
                criteria.setBaseFolder(rootFolderPath + "/" + newPath);
                criteria.setRootFolder(rootFolderPath);
                notifySearchHandler(criteria);
            }).withDescription(pathName).withStyleName(WebThemes.BUTTON_LINK);
        } else {
            final String newPath = remainPath;
            btn1 = new MButton(StringUtils.trim(newPath, 25, true), clickEvent -> {
                FileSearchCriteria criteria = new FileSearchCriteria();
                criteria.setBaseFolder(rootFolderPath + "/" + newPath);
                criteria.setRootFolder(rootFolderPath);
                notifySearchHandler(criteria);
            }).withDescription(newPath).withStyleName(WebThemes.BUTTON_LINK);
        }

        with(new ELabel("/"), btn1);

        if (btn2 != null) {
            with(new ELabel("/"), btn2);
        }
    }

    private void displayExternalFolder(final ExternalFolder folder) {
        String remainPath = folder.getPath();

        Button btn1 = null, btn2 = null;
        int index;
        if ((index = remainPath.lastIndexOf('/')) != -1) {
            if (index == 0) {
                btn1 = new MButton(StringUtils.trim(folder.getExternalDrive().getFoldername(), 25, true), clickEvent -> {
                    FileSearchCriteria criteria = new FileSearchCriteria();
                    criteria.setBaseFolder("/");
                    criteria.setRootFolder("/");
                    criteria.setStorageName(StorageNames.DROPBOX);
                    criteria.setExternalDrive(folder.getExternalDrive());
                    notifySearchHandler(criteria);
                }).withStyleName(WebThemes.BUTTON_LINK);
            }
            String pathName = remainPath.substring(index + 1);
            if (StringUtils.isNotBlank(pathName)) {
                final String newPath = remainPath.substring(0, index);
                remainPath = newPath;
                btn2 = new MButton(StringUtils.trim(pathName, 25, true), clickEvent -> {
                    FileSearchCriteria criteria = new FileSearchCriteria();
                    criteria.setBaseFolder(newPath);
                    criteria.setRootFolder("/");
                    criteria.setStorageName(StorageNames.DROPBOX);
                    criteria.setExternalDrive(folder.getExternalDrive());
                    notifySearchHandler(criteria);
                }).withDescription(pathName).withStyleName(WebThemes.BUTTON_LINK);
            } else {
                remainPath = "";
            }
        }

        if ((index = remainPath.lastIndexOf('/')) != -1) {
            String pathName = remainPath.substring(index + 1);
            final String newPath = remainPath.substring(0, index);
            btn1 = new MButton(StringUtils.trim(pathName, 25, true), clickEvent -> {
                FileSearchCriteria criteria = new FileSearchCriteria();
                criteria.setBaseFolder(newPath);

                criteria.setRootFolder("/");
                criteria.setStorageName(StorageNames.DROPBOX);
                criteria.setExternalDrive(folder.getExternalDrive());
                notifySearchHandler(criteria);
            }).withDescription(pathName).withStyleName(WebThemes.BUTTON_LINK);
        }

        if (btn1 != null) {
            with(new ELabel("/"), btn1);
        }

        if (btn2 != null) {
            with(new ELabel("/"), btn2);
        }
    }

    private static MButton generateBreadcrumbLink(String linkName, Button.ClickListener listener) {
        return CommonUIFactory.createButtonTooltip(StringUtils.trim(linkName, 25, true),
                linkName, listener).withStyleName(WebThemes.BUTTON_LINK);
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
            handlers.forEach(handler -> handler.onSearch(criteria));
        }
    }

    @Override
    public void setTotalCountNumber(Integer totalCountNumber) {

    }
}
