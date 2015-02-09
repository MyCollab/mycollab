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
import com.esofthead.mycollab.module.project.ui.components.HeaderView;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.mvp.ViewScope;
import com.esofthead.mycollab.vaadin.ui.*;
import com.esofthead.mycollab.vaadin.ui.form.field.RichTextEditField;
import com.google.common.collect.Ordering;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;

import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
@ViewComponent(scope = ViewScope.PROTOTYPE)
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
        this.setMargin(new MarginInfo(false, true, false, true));

        headerLayout = new MHorizontalLayout()
                .withStyleName(UIConstants.HEADER_VIEW).withWidth("100%")
                .withSpacing(true)
                .withMargin(new MarginInfo(true, false, true, false));
        headerLayout.setHeightUndefined();

        this.addComponent(headerLayout);
        initHeader();

        pagesLayout = new MVerticalLayout().withStyleName("pages-list-layout");
        this.addComponent(pagesLayout);
    }

    private void initHeader() {
        Label headerText = new HeaderView(ProjectTypeConstants.PAGE,
                AppContext.getMessage(Page18InEnum.VIEW_LIST_TITLE));

        headerLayout.with(headerText)
                .alignAll(Alignment.MIDDLE_LEFT).expand(headerText);

        Label sortLbl = new Label(
                AppContext.getMessage(Page18InEnum.OPT_SORT_LABEL));
        sortLbl.setSizeUndefined();
        headerLayout.with(sortLbl).withAlign(sortLbl, Alignment.MIDDLE_RIGHT);

        ToggleButtonGroup sortGroup = new ToggleButtonGroup();
        headerLayout.with(sortGroup).withAlign(sortGroup,
                Alignment.MIDDLE_RIGHT);

        SortButton sortDateBtn = new SortButton(
                AppContext.getMessage(Page18InEnum.OPT_SORT_BY_DATE),
                new Button.ClickListener() {

                    private static final long serialVersionUID = -6987503077975316907L;

                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        dateSourceAscend = !dateSourceAscend;
                        if (dateSourceAscend) {
                            Collections
                                    .sort(resources, Ordering.from(dateSort));
                        } else {
                            Collections.sort(resources, Ordering.from(dateSort)
                                    .reverse());
                        }
                        displayPages(resources);

                    }
                });
        sortGroup.addButton(sortDateBtn);

        SortButton sortNameBtn = new SortButton(
                AppContext.getMessage(Page18InEnum.OPT_SORT_BY_NAME),
                new Button.ClickListener() {

                    private static final long serialVersionUID = 2847554379518387585L;

                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        nameSortAscend = !nameSortAscend;
                        if (nameSortAscend) {
                            Collections
                                    .sort(resources, Ordering.from(nameSort));
                        } else {
                            Collections.sort(resources, Ordering.from(nameSort)
                                    .reverse());
                        }
                        displayPages(resources);

                    }
                });
        sortGroup.addButton(sortNameBtn);

        SortButton sortKindBtn = new SortButton(
                AppContext.getMessage(Page18InEnum.OPT_SORT_BY_KIND),
                new Button.ClickListener() {

                    private static final long serialVersionUID = 2230933690084074590L;

                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        kindSortAscend = !kindSortAscend;
                        if (kindSortAscend) {
                            Collections
                                    .sort(resources, Ordering.from(kindSort));
                        } else {
                            Collections.sort(resources, Ordering.from(kindSort)
                                    .reverse());
                        }
                        displayPages(resources);

                    }
                });
        sortGroup.addButton(sortKindBtn);

        sortGroup.setDefaultButton(sortDateBtn);

        final Button newGroupBtn = new Button(
                AppContext.getMessage(Page18InEnum.BUTTON_NEW_GROUP),
                new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(final Button.ClickEvent event) {
                        UI.getCurrent().addWindow(new PageGroupWindow());
                    }
                });
        newGroupBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
        newGroupBtn.setIcon(FontAwesome.PLUS);
        newGroupBtn.setEnabled(CurrentProjectVariables
                .canWrite(ProjectRolePermissionCollections.PAGES));
        headerLayout.with(newGroupBtn).withAlign(newGroupBtn,
                Alignment.MIDDLE_RIGHT);

        final Button newPageBtn = new Button(
                AppContext.getMessage(Page18InEnum.BUTTON_NEW_PAGE),
                new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(final Button.ClickEvent event) {
                        EventBusFactory.getInstance().post(
                                new PageEvent.GotoAdd(this, null));
                    }
                });
        newPageBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
        newPageBtn.setIcon(FontAwesome.PLUS);
        newPageBtn.setEnabled(CurrentProjectVariables
                .canWrite(ProjectRolePermissionCollections.PAGES));

        headerText.setStyleName(UIConstants.HEADER_TEXT);
        headerLayout.with(newPageBtn).withAlign(newPageBtn,
                Alignment.MIDDLE_RIGHT);
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
        HorizontalLayout container = new HorizontalLayout();
        container.setWidth("100%");
        container.setSpacing(true);
        container.setStyleName("page-item-block");
        Image iconResource = new Image(null,
                MyCollabResource.newResource("icons/48/project/folder.png"));
        container.addComponent(iconResource);
        container.setComponentAlignment(iconResource, Alignment.TOP_LEFT);

        VerticalLayout block = new VerticalLayout();
        block.setWidth("600px");
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
        folderLink.addStyleName("link");
        folderLink.addStyleName("h3");
        headerPanel.addComponent(folderLink);
        block.addComponent(headerPanel);
        block.addComponent(new Label(StringUtils.trimHtmlTags(resource
                .getDescription())));

        Label lastUpdateInfo = new Label(
                AppContext.getMessage(Page18InEnum.LABEL_LAST_UPDATE,
                        ProjectLinkBuilder.generateProjectMemberHtmlLink(
                                resource.getCreatedUser(),
                                CurrentProjectVariables.getProjectId()),
                        AppContext.formatDateTime(resource.getCreatedTime()
                                .getTime())), ContentMode.HTML);
        lastUpdateInfo.addStyleName("last-update-info");
        block.addComponent(lastUpdateInfo);

        MHorizontalLayout controlBtns = new MHorizontalLayout().withStyleName("control-btns");
        Button editBtn = new Button(
                AppContext.getMessage(GenericI18Enum.BUTTON_EDIT),
                new Button.ClickListener() {

                    private static final long serialVersionUID = -5387015552598157076L;

                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        UI.getCurrent()
                                .addWindow(new PageGroupWindow(resource));
                    }
                });
        editBtn.setIcon(FontAwesome.EDIT);
        editBtn.setStyleName("link");
        editBtn.setEnabled(CurrentProjectVariables
                .canWrite(ProjectRolePermissionCollections.PAGES));
        controlBtns.addComponent(editBtn);

        Button deleteBtn = new Button(
                AppContext.getMessage(GenericI18Enum.BUTTON_DELETE),
                new Button.ClickListener() {

                    private static final long serialVersionUID = -5387015552598157076L;

                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        ConfirmDialogExt.show(
                                UI.getCurrent(),
                                AppContext.getMessage(
                                        GenericI18Enum.DIALOG_DELETE_TITLE,
                                        SiteConfiguration.getSiteName()),
                                AppContext
                                        .getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                                AppContext
                                        .getMessage(GenericI18Enum.BUTTON_YES),
                                AppContext.getMessage(GenericI18Enum.BUTTON_NO),
                                new ConfirmDialog.Listener() {
                                    private static final long serialVersionUID = 1L;

                                    @Override
                                    public void onClose(ConfirmDialog dialog) {
                                        if (dialog.isConfirmed()) {
                                            PageService wikiService = ApplicationContextUtil
                                                    .getSpringBean(PageService.class);
                                            wikiService.removeResource(resource
                                                    .getPath());
                                            resources.remove(resource);
                                            displayPages(resources);
                                        }
                                    }
                                });

                    }
                });
        deleteBtn.setEnabled(CurrentProjectVariables
                .canAccess(ProjectRolePermissionCollections.PAGES));
        deleteBtn.setIcon(FontAwesome.TRASH_O);
        deleteBtn.setStyleName("link");
        controlBtns.addComponent(deleteBtn);

        block.addComponent(controlBtns);

        HorizontalLayout footer = new HorizontalLayout();
        block.addComponent(footer);

        container.addComponent(block);
        container.setExpandRatio(block, 1);
        return container;
    }

    private Layout displayPageBlock(final Page resource) {
        HorizontalLayout container = new HorizontalLayout();
        container.setWidth("100%");
        container.setSpacing(true);
        container.setStyleName("page-item-block");

        Image iconResource = new Image(null,
                MyCollabResource.newResource("icons/48/project/document.png"));
        container.addComponent(iconResource);
        container.setComponentAlignment(iconResource, Alignment.TOP_LEFT);

        VerticalLayout block = new VerticalLayout();
        block.setWidth("600px");
        HorizontalLayout headerPanel = new HorizontalLayout();
        Button pageLink = new Button(resource.getSubject(),
                new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        EventBusFactory.getInstance().post(
                                new PageEvent.GotoRead(PageListViewImpl.this,
                                        resource));

                    }
                });
        pageLink.addStyleName("link");
        pageLink.addStyleName("h3");
        headerPanel.addComponent(pageLink);

        block.addComponent(headerPanel);

        block.addComponent(new Label(StringUtils.trimHtmlTags(resource
                .getContent())));

        Label lastUpdateInfo = new Label(AppContext.getMessage(
                Page18InEnum.LABEL_LAST_UPDATE, ProjectLinkBuilder
                        .generateProjectMemberHtmlLink(
                                resource.getLastUpdatedUser(),
                                CurrentProjectVariables.getProjectId()),
                AppContext.formatDateTime(resource.getLastUpdatedTime()
                        .getTime())), ContentMode.HTML);
        lastUpdateInfo.addStyleName("last-update-info");
        block.addComponent(lastUpdateInfo);

        HorizontalLayout controlBtns = new HorizontalLayout();
        controlBtns.setSpacing(true);
        controlBtns.setStyleName("control-btns");
        Button editBtn = new Button(
                AppContext.getMessage(GenericI18Enum.BUTTON_EDIT),
                new Button.ClickListener() {

                    private static final long serialVersionUID = -5387015552598157076L;

                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        EventBusFactory.getInstance().post(
                                new PageEvent.GotoEdit(PageListViewImpl.this,
                                        resource));
                    }
                });
        editBtn.setEnabled(CurrentProjectVariables
                .canWrite(ProjectRolePermissionCollections.PAGES));
        editBtn.setIcon(FontAwesome.EDIT);
        editBtn.setStyleName("link");
        controlBtns.addComponent(editBtn);

        Button deleteBtn = new Button(
                AppContext.getMessage(GenericI18Enum.BUTTON_DELETE),
                new Button.ClickListener() {

                    private static final long serialVersionUID = 2575434171770462361L;

                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        ConfirmDialogExt.show(
                                UI.getCurrent(),
                                AppContext.getMessage(
                                        GenericI18Enum.DIALOG_DELETE_TITLE,
                                        SiteConfiguration.getSiteName()),
                                AppContext
                                        .getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                                AppContext
                                        .getMessage(GenericI18Enum.BUTTON_YES),
                                AppContext.getMessage(GenericI18Enum.BUTTON_NO),
                                new ConfirmDialog.Listener() {
                                    private static final long serialVersionUID = 1L;

                                    @Override
                                    public void onClose(ConfirmDialog dialog) {
                                        if (dialog.isConfirmed()) {
                                            PageService wikiService = ApplicationContextUtil
                                                    .getSpringBean(PageService.class);
                                            wikiService.removeResource(resource
                                                    .getPath());
                                            resources.remove(resource);
                                            displayPages(resources);
                                        }
                                    }
                                });

                    }
                });
        deleteBtn.setEnabled(CurrentProjectVariables
                .canAccess(ProjectRolePermissionCollections.PAGES));
        deleteBtn.setIcon(FontAwesome.TRASH_O);
        deleteBtn.setStyleName("link");
        controlBtns.addComponent(deleteBtn);

        block.addComponent(controlBtns);

        container.addComponent(block);
        container.setExpandRatio(block, 1);
        return container;
    }

    private class PageGroupWindow extends Window {
        private static final long serialVersionUID = 1L;

        private Folder folder;
        private boolean isEditMode = false;

        public PageGroupWindow(Folder editFolder) {
            super();
            this.setModal(true);
            this.setWidth("700px");
            this.setResizable(false);
            this.center();
            MVerticalLayout content = new MVerticalLayout().withMargin(new MarginInfo(false, false, true, false));

            EditForm editForm = new EditForm();

            if (editFolder == null) {
                folder = new Folder();
                this.setCaption(AppContext
                        .getMessage(Page18InEnum.DIALOG_NEW_GROUP_TITLE));
                String pagePath = CurrentProjectVariables.getCurrentPagePath();
                folder.setPath(pagePath + "/"
                        + StringUtils.generateSoftUniqueId());
            } else {
                folder = editFolder;
                isEditMode = true;
                this.setCaption(AppContext
                        .getMessage(Page18InEnum.DIALOG_EDIT_GROUP_TITLE));
            }

            editForm.setBean(folder);
            content.addComponent(editForm);

            this.setContent(content);
        }

        public PageGroupWindow() {
            this(null);
        }

        private class EditForm extends AdvancedEditBeanForm<Folder> {

            private static final long serialVersionUID = -1898444508905690238L;

            @Override
            public void setBean(final Folder item) {
                this.setFormLayoutFactory(new FormLayoutFactory());
                this.setBeanFormFieldFactory(new EditFormFieldFactory(
                        EditForm.this));
                super.setBean(item);
            }

            class FormLayoutFactory implements IFormLayoutFactory {

                private static final long serialVersionUID = 1L;
                private GridFormLayoutHelper informationLayout;

                @Override
                public ComponentContainer getLayout() {
                    final VerticalLayout layout = new VerticalLayout();
                    this.informationLayout = new GridFormLayoutHelper(2, 2,
                            "100%", "167px", Alignment.TOP_LEFT);
                    this.informationLayout.getLayout().setWidth("100%");
                    this.informationLayout.getLayout().setMargin(false);
                    this.informationLayout.getLayout().addStyleName(
                            "colored-gridlayout");

                    layout.addComponent(this.informationLayout.getLayout());

                    final HorizontalLayout controlsBtn = new HorizontalLayout();
                    controlsBtn.setSpacing(true);
                    controlsBtn.setMargin(new MarginInfo(true, true, true,
                            false));
                    layout.addComponent(controlsBtn);

                    final Button cancelBtn = new Button(
                            AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL),
                            new Button.ClickListener() {
                                private static final long serialVersionUID = 1L;

                                @Override
                                public void buttonClick(
                                        final Button.ClickEvent event) {
                                    PageGroupWindow.this.close();
                                }
                            });
                    cancelBtn.setStyleName(UIConstants.THEME_GRAY_LINK);
                    controlsBtn.addComponent(cancelBtn);
                    controlsBtn.setComponentAlignment(cancelBtn,
                            Alignment.MIDDLE_LEFT);

                    final Button saveBtn = new Button(
                            AppContext.getMessage(GenericI18Enum.BUTTON_SAVE),
                            new Button.ClickListener() {
                                private static final long serialVersionUID = 1L;

                                @Override
                                public void buttonClick(
                                        final Button.ClickEvent event) {
                                    if (EditForm.this.validateForm()) {

                                        PageService wikiService = ApplicationContextUtil
                                                .getSpringBean(PageService.class);
                                        wikiService.createFolder(folder,
                                                AppContext.getUsername());
                                        folder.setCreatedTime(new GregorianCalendar());
                                        folder.setCreatedUser(AppContext
                                                .getUsername());
                                        if (!isEditMode) {
                                            resources.add(folder);
                                        }

                                        PageGroupWindow.this.close();
                                        displayPages(resources);
                                    }
                                }
                            });
                    saveBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
                    controlsBtn.addComponent(saveBtn);
                    controlsBtn.setComponentAlignment(saveBtn,
                            Alignment.MIDDLE_RIGHT);

                    layout.setComponentAlignment(controlsBtn,
                            Alignment.MIDDLE_RIGHT);

                    return layout;
                }

                @Override
                public void attachField(Object propertyId, Field<?> field) {
                    if (propertyId.equals("name")) {
                        this.informationLayout.addComponent(field,
                                AppContext.getMessage(Page18InEnum.FORM_GROUP),
                                0, 0);
                    } else if (propertyId.equals("description")) {
                        this.informationLayout.addComponent(field, AppContext
                                        .getMessage(GenericI18Enum.FORM_DESCRIPTION),
                                0, 1, 2, "100%", Alignment.MIDDLE_LEFT);
                    }

                }
            }
        }

        private class EditFormFieldFactory extends AbstractBeanFieldGroupEditFieldFactory<Folder> {
            private static final long serialVersionUID = 1L;

            public EditFormFieldFactory(GenericBeanForm<Folder> form) {
                super(form);
            }

            @Override
            protected Field<?> onCreateField(final Object propertyId) {
                if (propertyId.equals("description")) {
                    return new RichTextEditField();
                }

                return null;
            }
        }
    }

}
