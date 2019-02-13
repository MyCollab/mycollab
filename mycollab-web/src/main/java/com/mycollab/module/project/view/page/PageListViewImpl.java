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
package com.mycollab.module.project.view.page;

import com.google.common.collect.Ordering;
import com.hp.gagawa.java.elements.A;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.utils.DateTimeUtils;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.module.page.domain.Folder;
import com.mycollab.module.page.domain.Page;
import com.mycollab.module.page.domain.PageResource;
import com.mycollab.module.page.service.PageService;
import com.mycollab.module.project.*;
import com.mycollab.module.project.event.PageEvent;
import com.mycollab.module.project.i18n.PageI18nEnum;
import com.mycollab.module.project.ui.components.ComponentUtils;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.AbstractVerticalPageView;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.HeaderWithIcon;
import com.mycollab.vaadin.ui.SafeHtmlLabel;
import com.mycollab.vaadin.web.ui.ButtonGroup;
import com.mycollab.vaadin.web.ui.ConfirmDialogExt;
import com.mycollab.vaadin.web.ui.SortButton;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.UI;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.Comparator;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
@ViewComponent
public class PageListViewImpl extends AbstractVerticalPageView implements PageListView {
    private static final long serialVersionUID = 1L;

    private MHorizontalLayout headerLayout;
    private MVerticalLayout pagesLayout;
    private List<? extends PageResource> resources;

    private static Comparator<PageResource> dateSort = Comparator.comparing(PageResource::getCreatedTime);

    private static Comparator<PageResource> kindSort = (o1, o2) -> {
        if (o1.getClass() == o2.getClass()) {
            return 0;
        } else {
            if ((o1 instanceof Folder) && (o2 instanceof Page)) {
                return 1;
            } else {
                return -1;
            }
        }
    };

    private static Comparator<PageResource> nameSort = (o1, o2) -> {
        String name1 = (o1 instanceof Folder) ? ((Folder) o1).getName() : ((Page) o1).getSubject();
        String name2 = (o2 instanceof Folder) ? ((Folder) o2).getName() : ((Page) o2).getSubject();
        return name1.compareTo(name2);
    };

    private boolean dateSourceAscend = true;
    private boolean nameSortAscend = true;
    private boolean kindSortAscend = true;

    public PageListViewImpl() {
        this.setMargin(new MarginInfo(false, true, true, true));

        headerLayout = new MHorizontalLayout().withFullWidth()
                .withMargin(new MarginInfo(true, false, true, false));
        headerLayout.setHeightUndefined();

        this.addComponent(headerLayout);
        initHeader();

        pagesLayout = new MVerticalLayout().withMargin(false).withSpacing(false).withStyleName("pages-list-layout");
        this.addComponent(pagesLayout);
    }

    private void initHeader() {
        HeaderWithIcon headerText = ComponentUtils.headerH2(ProjectTypeConstants.PAGE,
                UserUIContext.getMessage(PageI18nEnum.LIST));

        headerLayout.with(headerText).alignAll(Alignment.MIDDLE_LEFT).expand(headerText);

        Label sortLbl = new Label(UserUIContext.getMessage(PageI18nEnum.OPT_SORT_LABEL));
        sortLbl.setSizeUndefined();
        headerLayout.with(sortLbl).withAlign(sortLbl, Alignment.MIDDLE_RIGHT);


        SortButton sortDateBtn = new SortButton(UserUIContext.getMessage(PageI18nEnum.OPT_SORT_BY_DATE), clickEvent -> {
            dateSourceAscend = !dateSourceAscend;
            if (dateSourceAscend) {
                resources.sort(Ordering.from(dateSort));
            } else {
                resources.sort(Ordering.from(dateSort).reverse());
            }
            displayPages(resources);
        });

        SortButton sortNameBtn = new SortButton(UserUIContext.getMessage(PageI18nEnum.OPT_SORT_BY_NAME), clickEvent -> {
            nameSortAscend = !nameSortAscend;
            if (nameSortAscend) {
                resources.sort(Ordering.from(nameSort));
            } else {
                resources.sort(Ordering.from(nameSort).reverse());
            }
            displayPages(resources);
        });

        SortButton sortKindBtn = new SortButton(UserUIContext.getMessage(PageI18nEnum.OPT_SORT_BY_KIND), clickEvent -> {
            kindSortAscend = !kindSortAscend;
            if (kindSortAscend) {
                resources.sort(Ordering.from(kindSort));
            } else {
                resources.sort(Ordering.from(kindSort).reverse());
            }
            displayPages(resources);
        });

        ButtonGroup sortGroup = new ButtonGroup(sortDateBtn, sortNameBtn, sortKindBtn).withDefaultButton(sortDateBtn);
        headerLayout.with(sortGroup).withAlign(sortGroup, Alignment.MIDDLE_RIGHT);

        MButton newGroupBtn = new MButton(UserUIContext.getMessage(PageI18nEnum.NEW_GROUP),
                clickEvent -> UI.getCurrent().addWindow(new GroupPageAddWindow()))
                .withIcon(VaadinIcons.PLUS).withStyleName(WebThemes.BUTTON_ACTION);
        newGroupBtn.setVisible(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.PAGES));
        headerLayout.with(newGroupBtn).withAlign(newGroupBtn, Alignment.MIDDLE_RIGHT);

        MButton newPageBtn = new MButton(UserUIContext.getMessage(PageI18nEnum.NEW),
                clickEvent -> EventBusFactory.getInstance().post(new PageEvent.GotoAdd(this, null)))
                .withIcon(VaadinIcons.PLUS).withStyleName(WebThemes.BUTTON_ACTION);
        newPageBtn.setVisible(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.PAGES));

        headerLayout.with(newPageBtn).withAlign(newPageBtn, Alignment.MIDDLE_RIGHT);
    }

    private void displayPages(List<? extends PageResource> resources) {
        this.resources = resources;
        pagesLayout.removeAllComponents();
        if (resources != null) {
            resources.stream().map(resource -> resource instanceof Page ? displayPageBlock((Page) resource)
                    : displayFolderBlock((Folder) resource)).forEach(resourceBlock -> pagesLayout.addComponent(resourceBlock));
        }
    }

    @Override
    public void showNoItemView() {
        removeAllComponents();
        this.addComponent(new PageListNoItemView());
    }

    @Override
    public void displayDefaultPages(List<? extends PageResource> resources) {
        resources.sort(Ordering.from(dateSort));
        displayPages(resources);
    }

    private Layout displayFolderBlock(final Folder resource) {
        MVerticalLayout container = new MVerticalLayout().withFullWidth().withStyleName("page-item-block");

        A folderHtml = new A(ProjectLinkGenerator.generatePagesLink(CurrentProjectVariables
                .getProjectId(), resource.getPath())).appendText(VaadinIcons.FOLDER_OPEN.getHtml() + " " + resource.getName());
        ELabel folderLink = ELabel.h3(folderHtml.write());
        container.addComponent(folderLink);
        if (StringUtils.isNotBlank(resource.getDescription())) {
            container.addComponent(new Label(StringUtils.trimHtmlTags(resource.getDescription())));
        }

        Label lastUpdateInfo = ELabel.html(UserUIContext.getMessage(PageI18nEnum.LABEL_LAST_UPDATE,
                ProjectLinkBuilder.generateProjectMemberHtmlLink(CurrentProjectVariables.getProjectId(), resource.getCreatedUser(), true),
                UserUIContext.formatPrettyTime(DateTimeUtils.toLocalDateTime(resource.getCreatedTime()))))
                .withDescription(UserUIContext.formatDateTime(DateTimeUtils.toLocalDateTime(resource.getCreatedTime())))
                .withStyleName(WebThemes.META_INFO);
        container.addComponent(lastUpdateInfo);

        MButton editBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_EDIT),
                clickEvent -> UI.getCurrent().addWindow(new GroupPageAddWindow(resource)))
                .withStyleName(WebThemes.BUTTON_LINK, WebThemes.BUTTON_SMALL_PADDING).withIcon(VaadinIcons.EDIT)
                .withVisible(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.PAGES));

        MButton deleteBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_DELETE), clickEvent -> ConfirmDialogExt.show(UI.getCurrent(),
                UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, AppUI.getSiteName()),
                UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                UserUIContext.getMessage(GenericI18Enum.ACTION_YES),
                UserUIContext.getMessage(GenericI18Enum.ACTION_NO),
                confirmDialog -> {
                    if (confirmDialog.isConfirmed()) {
                        PageService pageService = AppContextUtil.getSpringBean(PageService.class);
                        pageService.removeResource(resource.getPath());
                        resources.remove(resource);
                        displayPages(resources);
                    }
                })).withIcon(VaadinIcons.TRASH).withStyleName(WebThemes.BUTTON_LINK, WebThemes.BUTTON_SMALL_PADDING)
                .withVisible(CurrentProjectVariables.canAccess(ProjectRolePermissionCollections.PAGES));

        container.addComponent(new MHorizontalLayout(editBtn, deleteBtn));
        return container;
    }

    private Layout displayPageBlock(final Page resource) {
        MVerticalLayout container = new MVerticalLayout().withFullWidth().withStyleName("page-item-block");
        A pageHtml = new A(ProjectLinkGenerator.generatePageRead(CurrentProjectVariables.getProjectId(), resource
                .getPath())).appendText(VaadinIcons.FILE_TEXT.getHtml() + " " + resource.getSubject());
        ELabel pageLink = ELabel.h3(pageHtml.write());

        container.with(pageLink, new SafeHtmlLabel(resource.getContent(), 400));

        Label lastUpdateInfo = ELabel.html(UserUIContext.getMessage(
                PageI18nEnum.LABEL_LAST_UPDATE, ProjectLinkBuilder.generateProjectMemberHtmlLink(
                        CurrentProjectVariables.getProjectId(), resource.getLastUpdatedUser(), true),
                UserUIContext.formatPrettyTime(DateTimeUtils.toLocalDateTime(resource.getLastUpdatedTime()))))
                .withDescription(UserUIContext.formatDateTime(DateTimeUtils.toLocalDateTime(resource.getLastUpdatedTime())))
                .withStyleName(WebThemes.META_INFO);
        container.addComponent(lastUpdateInfo);

        MButton editBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_EDIT),
                clickEvent -> EventBusFactory.getInstance().post(new PageEvent.GotoEdit(PageListViewImpl.this, resource)))
                .withIcon(VaadinIcons.EDIT).withStyleName(WebThemes.BUTTON_LINK, WebThemes.BUTTON_SMALL_PADDING)
                .withVisible(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.PAGES));

        MButton deleteBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_DELETE), clickEvent -> {
            ConfirmDialogExt.show(UI.getCurrent(),
                    UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, AppUI.getSiteName()),
                    UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                    UserUIContext.getMessage(GenericI18Enum.ACTION_YES),
                    UserUIContext.getMessage(GenericI18Enum.ACTION_NO),
                    confirmDialog -> {
                        if (confirmDialog.isConfirmed()) {
                            PageService pageService = AppContextUtil.getSpringBean(PageService.class);
                            pageService.removeResource(resource.getPath());
                            resources.remove(resource);
                            displayPages(resources);
                        }
                    });
        }).withIcon(VaadinIcons.TRASH).withStyleName(WebThemes.BUTTON_LINK, WebThemes.BUTTON_SMALL_PADDING)
                .withVisible(CurrentProjectVariables.canAccess(ProjectRolePermissionCollections.PAGES));

        container.addComponent(new MHorizontalLayout(editBtn, deleteBtn));
        return container;
    }
}
