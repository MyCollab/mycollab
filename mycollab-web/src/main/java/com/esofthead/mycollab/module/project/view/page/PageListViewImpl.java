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
package com.esofthead.mycollab.module.project.view.page;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.page.domain.Folder;
import com.esofthead.mycollab.module.page.domain.Page;
import com.esofthead.mycollab.module.page.domain.PageResource;
import com.esofthead.mycollab.module.page.service.PageService;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.events.PageEvent;
import com.esofthead.mycollab.module.project.i18n.Page18InEnum;
import com.esofthead.mycollab.module.project.ui.components.ProjectViewHeader;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.*;
import com.google.common.collect.Ordering;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
@ViewComponent
public class PageListViewImpl extends AbstractPageView implements PageListView {
    private static final long serialVersionUID = 1L;

    private MHorizontalLayout headerLayout;
    private MVerticalLayout pagesLayout;
    private List<PageResource> resources;

    private static Comparator<PageResource> dateSort = new Comparator<PageResource>() {

        @Override
        public int compare(PageResource o1, PageResource o2) {
            return o1.getCreatedTime().compareTo(o2.getCreatedTime());
        }
    };

    private static Comparator<PageResource> kindSort = new Comparator<PageResource>() {

        @Override
        public int compare(PageResource o1, PageResource o2) {
            if (o1.getClass() == o2.getClass()) {
                return 0;
            } else {
                if ((o1 instanceof Folder) && (o2 instanceof Page)) {
                    return 1;
                } else {
                    return -1;
                }
            }
        }
    };

    private static Comparator<PageResource> nameSort = new Comparator<PageResource>() {

        @Override
        public int compare(PageResource o1, PageResource o2) {
            String name1 = (o1 instanceof Folder) ? ((Folder) o1).getName()
                    : ((Page) o1).getSubject();
            String name2 = (o2 instanceof Folder) ? ((Folder) o2).getName()
                    : ((Page) o2).getSubject();
            return name1.compareTo(name2);
        }
    };

    private boolean dateSourceAscend = true;
    private boolean nameSortAscend = true;
    private boolean kindSortAscend = true;

    public PageListViewImpl() {
        this.setMargin(new MarginInfo(false, true, true, true));

        headerLayout = new MHorizontalLayout()
                .withStyleName(UIConstants.HEADER_VIEW).withWidth("100%")
                .withMargin(new MarginInfo(true, false, true, false));
        headerLayout.setHeightUndefined();

        this.addComponent(headerLayout);
        initHeader();

        pagesLayout = new MVerticalLayout().withMargin(false).withStyleName("pages-list-layout");
        this.addComponent(pagesLayout);
    }

    private void initHeader() {
        ProjectViewHeader headerText = new ProjectViewHeader(ProjectTypeConstants.PAGE,
                AppContext.getMessage(Page18InEnum.VIEW_LIST_TITLE));

        headerLayout.with(headerText).alignAll(Alignment.MIDDLE_LEFT).expand(headerText);

        Label sortLbl = new Label(AppContext.getMessage(Page18InEnum.OPT_SORT_LABEL));
        sortLbl.setSizeUndefined();
        headerLayout.with(sortLbl).withAlign(sortLbl, Alignment.MIDDLE_RIGHT);

        ToggleButtonGroup sortGroup = new ToggleButtonGroup();
        headerLayout.with(sortGroup).withAlign(sortGroup, Alignment.MIDDLE_RIGHT);

        SortButton sortDateBtn = new SortButton(AppContext.getMessage(Page18InEnum.OPT_SORT_BY_DATE),
                new Button.ClickListener() {
                    private static final long serialVersionUID = -6987503077975316907L;

                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        dateSourceAscend = !dateSourceAscend;
                        if (dateSourceAscend) {
                            Collections.sort(resources, Ordering.from(dateSort));
                        } else {
                            Collections.sort(resources, Ordering.from(dateSort).reverse());
                        }
                        displayPages(resources);

                    }
                });
        sortGroup.addButton(sortDateBtn);

        SortButton sortNameBtn = new SortButton(AppContext.getMessage(Page18InEnum.OPT_SORT_BY_NAME),
                new Button.ClickListener() {

                    private static final long serialVersionUID = 2847554379518387585L;

                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        nameSortAscend = !nameSortAscend;
                        if (nameSortAscend) {
                            Collections.sort(resources, Ordering.from(nameSort));
                        } else {
                            Collections.sort(resources, Ordering.from(nameSort).reverse());
                        }
                        displayPages(resources);

                    }
                });
        sortGroup.addButton(sortNameBtn);

        SortButton sortKindBtn = new SortButton(AppContext.getMessage(Page18InEnum.OPT_SORT_BY_KIND),
                new Button.ClickListener() {
                    private static final long serialVersionUID = 2230933690084074590L;

                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        kindSortAscend = !kindSortAscend;
                        if (kindSortAscend) {
                            Collections.sort(resources, Ordering.from(kindSort));
                        } else {
                            Collections.sort(resources, Ordering.from(kindSort).reverse());
                        }
                        displayPages(resources);

                    }
                });
        sortGroup.addButton(sortKindBtn);
        sortGroup.setDefaultButton(sortDateBtn);

        Button newGroupBtn = new Button(AppContext.getMessage(Page18InEnum.BUTTON_NEW_GROUP),
                new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(final Button.ClickEvent event) {
                        UI.getCurrent().addWindow(new GroupPageAddWindow());
                    }
                });
        newGroupBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
        newGroupBtn.setIcon(FontAwesome.PLUS);
        newGroupBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.PAGES));
        headerLayout.with(newGroupBtn).withAlign(newGroupBtn, Alignment.MIDDLE_RIGHT);

        Button newPageBtn = new Button(AppContext.getMessage(Page18InEnum.BUTTON_NEW_PAGE),
                new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(final Button.ClickEvent event) {
                        EventBusFactory.getInstance().post(new PageEvent.GotoAdd(this, null));
                    }
                });
        newPageBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
        newPageBtn.setIcon(FontAwesome.PLUS);
        newPageBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.PAGES));

        headerText.setStyleName(UIConstants.HEADER_TEXT);
        headerLayout.with(newPageBtn).withAlign(newPageBtn, Alignment.MIDDLE_RIGHT);
    }

    private void displayPages(List<PageResource> resources) {
        this.resources = resources;
        pagesLayout.removeAllComponents();
        if (resources != null) {
            for (PageResource resource : resources) {
                Layout resourceBlock = resource instanceof Page ? displayPageBlock((Page) resource)
                        : displayFolderBlock((Folder) resource);
                pagesLayout.addComponent(resourceBlock);
            }
        }
    }

    @Override
    public void displayDefaultPages(List<PageResource> resources) {
        Collections.sort(resources, Ordering.from(dateSort));
        displayPages(resources);
    }

    private Layout displayFolderBlock(final Folder resource) {
        MHorizontalLayout container = new MHorizontalLayout().withWidth("100%").withStyleName("page-item-block");

        ELabel iconResource = new ELabel(FontAwesome.FOLDER_OPEN);
        iconResource.addStyleName("icon-48px");

        VerticalLayout block = new VerticalLayout();
        block.setWidth("100%");
        HorizontalLayout headerPanel = new HorizontalLayout();
        Button folderLink = new Button(resource.getName(),
                new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        EventBusFactory.getInstance().post(
                                new PageEvent.GotoList(PageListViewImpl.this,
                                        resource.getPath()));

                    }
                });
        folderLink.addStyleName("link h3");
        headerPanel.addComponent(folderLink);
        block.addComponent(headerPanel);
        block.addComponent(new Label(StringUtils.trimHtmlTags(resource.getDescription())));

        Label lastUpdateInfo = new ELabel(AppContext.getMessage(Page18InEnum.LABEL_LAST_UPDATE,
                        ProjectLinkBuilder.generateProjectMemberHtmlLink(
                                CurrentProjectVariables.getProjectId(), resource.getCreatedUser()),
                        AppContext.formatPrettyTime(resource.getCreatedTime()
                                .getTime())), ContentMode.HTML).withDescription(AppContext.formatDateTime(resource.getCreatedTime().getTime()));
        lastUpdateInfo.addStyleName("last-update-info");
        block.addComponent(lastUpdateInfo);

        MHorizontalLayout controlBtns = new MHorizontalLayout().withStyleName("control-btns");
        Button editBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_EDIT),
                new Button.ClickListener() {

                    private static final long serialVersionUID = -5387015552598157076L;

                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        UI.getCurrent().addWindow(new GroupPageAddWindow(resource));
                    }
                });
        editBtn.setIcon(FontAwesome.EDIT);
        editBtn.setStyleName(UIConstants.THEME_LINK);
        editBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.PAGES));
        controlBtns.addComponent(editBtn);

        Button deleteBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_DELETE),
                new Button.ClickListener() {

                    private static final long serialVersionUID = -5387015552598157076L;

                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        ConfirmDialogExt.show(UI.getCurrent(),
                                AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, AppContext.getSiteName()),
                                AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                                AppContext.getMessage(GenericI18Enum.BUTTON_YES),
                                AppContext.getMessage(GenericI18Enum.BUTTON_NO),
                                new ConfirmDialog.Listener() {
                                    private static final long serialVersionUID = 1L;

                                    @Override
                                    public void onClose(ConfirmDialog dialog) {
                                        if (dialog.isConfirmed()) {
                                            PageService pageService = ApplicationContextUtil.getSpringBean(PageService.class);
                                            pageService.removeResource(resource.getPath());
                                            resources.remove(resource);
                                            displayPages(resources);
                                        }
                                    }
                                });

                    }
                });
        deleteBtn.setEnabled(CurrentProjectVariables.canAccess(ProjectRolePermissionCollections.PAGES));
        deleteBtn.setIcon(FontAwesome.TRASH_O);
        deleteBtn.setStyleName(UIConstants.THEME_LINK);
        controlBtns.addComponent(deleteBtn);

        block.addComponent(controlBtns);

        HorizontalLayout footer = new HorizontalLayout();
        block.addComponent(footer);

        MHorizontalLayout wrapper = new MHorizontalLayout();
        wrapper.with(iconResource, block);
        container.with(wrapper);
        return container;
    }

    private Layout displayPageBlock(final Page resource) {
        MHorizontalLayout container = new MHorizontalLayout().withWidth("100%").withStyleName("page-item-block");

        ELabel iconResource = new ELabel(FontAwesome.FILE_WORD_O);
        iconResource.addStyleName("icon-48px");

        MVerticalLayout block = new MVerticalLayout().withSpacing(false).withMargin(new MarginInfo(false, true, false, true)).withWidth("100%");
        HorizontalLayout headerPanel = new HorizontalLayout();
        Button pageLink = new Button(resource.getSubject(), new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        EventBusFactory.getInstance().post(new PageEvent.GotoRead(PageListViewImpl.this, resource));
                    }
                });
        pageLink.addStyleName("link h3");
        headerPanel.addComponent(pageLink);

        block.with(headerPanel, new SafeHtmlLabel(resource.getContent(), 400));

        Label lastUpdateInfo = new ELabel(AppContext.getMessage(
                Page18InEnum.LABEL_LAST_UPDATE, ProjectLinkBuilder.generateProjectMemberHtmlLink(
                                CurrentProjectVariables.getProjectId(), resource.getLastUpdatedUser()),
                AppContext.formatPrettyTime(resource.getLastUpdatedTime()
                        .getTime())), ContentMode.HTML).withDescription(AppContext.formatDateTime(resource.getLastUpdatedTime().getTime()));
        lastUpdateInfo.addStyleName("last-update-info");
        block.addComponent(lastUpdateInfo);

        MHorizontalLayout controlBtns = new MHorizontalLayout().withStyleName("control-btns");
        Button editBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_EDIT),
                new Button.ClickListener() {

                    private static final long serialVersionUID = -5387015552598157076L;

                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        EventBusFactory.getInstance().post(new PageEvent.GotoEdit(PageListViewImpl.this, resource));
                    }
                });
        editBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.PAGES));
        editBtn.setIcon(FontAwesome.EDIT);
        editBtn.setStyleName(UIConstants.THEME_LINK);
        controlBtns.addComponent(editBtn);

        Button deleteBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_DELETE),
                new Button.ClickListener() {

                    private static final long serialVersionUID = 2575434171770462361L;

                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        ConfirmDialogExt.show(UI.getCurrent(),
                                AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, AppContext.getSiteName()),
                                AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                                AppContext.getMessage(GenericI18Enum.BUTTON_YES),
                                AppContext.getMessage(GenericI18Enum.BUTTON_NO),
                                new ConfirmDialog.Listener() {
                                    private static final long serialVersionUID = 1L;

                                    @Override
                                    public void onClose(ConfirmDialog dialog) {
                                        if (dialog.isConfirmed()) {
                                            PageService pageService = ApplicationContextUtil.getSpringBean(PageService.class);
                                            pageService.removeResource(resource.getPath());
                                            resources.remove(resource);
                                            displayPages(resources);
                                        }
                                    }
                                });

                    }
                });
        deleteBtn.setEnabled(CurrentProjectVariables.canAccess(ProjectRolePermissionCollections.PAGES));
        deleteBtn.setIcon(FontAwesome.TRASH_O);
        deleteBtn.setStyleName(UIConstants.THEME_LINK);
        controlBtns.addComponent(deleteBtn);

        block.addComponent(controlBtns);

        MHorizontalLayout wrapper = new MHorizontalLayout();
        wrapper.with(iconResource, block);
        container.with(wrapper);
        return container;
    }
}
