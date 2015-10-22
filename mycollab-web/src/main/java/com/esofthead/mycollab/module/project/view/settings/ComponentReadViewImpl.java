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
package com.esofthead.mycollab.module.project.view.settings;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.esofthead.mycollab.configuration.StorageFactory;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.ValuedBean;
import com.esofthead.mycollab.core.utils.BeanUtility;
import com.esofthead.mycollab.html.DivLessFormatter;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.i18n.ComponentI18nEnum;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.module.project.ui.components.*;
import com.esofthead.mycollab.module.project.ui.format.ComponentFieldFormatter;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectUserFormLinkField;
import com.esofthead.mycollab.module.tracker.domain.Component;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.domain.SimpleComponent;
import com.esofthead.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.module.tracker.service.ComponentService;
import com.esofthead.mycollab.schedule.email.project.ComponentRelayEmailNotificationAction;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.utils.TooltipHelper;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.*;
import com.esofthead.mycollab.vaadin.ui.form.field.ContainerViewField;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Span;
import com.vaadin.data.Property;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.List;
import java.util.UUID;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class ComponentReadViewImpl extends AbstractPreviewItemComp<SimpleComponent> implements ComponentReadView {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(ComponentReadViewImpl.class);

    private ProjectActivityComponent activityComponent;
    private Button quickActionStatusBtn;

    private DateInfoComp dateInfoComp;
    private PeopleInfoComp peopleInfoComp;
    private ComponentTimeLogComp componentTimeLogComp;

    public ComponentReadViewImpl() {
        super(AppContext.getMessage(ComponentI18nEnum.VIEW_READ_TITLE),
                ProjectAssetsManager.getAsset(ProjectTypeConstants.BUG_COMPONENT));
    }

    @Override
    public SimpleComponent getItem() {
        return this.beanItem;
    }

    @Override
    public HasPreviewFormHandlers<SimpleComponent> getPreviewFormHandlers() {
        return this.previewForm;
    }

    @Override
    protected String initFormTitle() {
        return beanItem.getComponentname();
    }

    @Override
    protected IFormLayoutFactory initFormLayoutFactory() {
        return new DynaFormLayout(ProjectTypeConstants.BUG_COMPONENT,
                ComponentDefaultFormLayoutFactory.getForm(),
                Component.Field.componentname.name());
    }

    @Override
    protected void initRelatedComponents() {
        activityComponent = new ProjectActivityComponent(ProjectTypeConstants.BUG_COMPONENT, CurrentProjectVariables
                .getProjectId(), ComponentFieldFormatter.instance(), ComponentRelayEmailNotificationAction.class);
        dateInfoComp = new DateInfoComp();
        peopleInfoComp = new PeopleInfoComp();
        componentTimeLogComp = new ComponentTimeLogComp();
        addToSideBar(dateInfoComp, peopleInfoComp, componentTimeLogComp);
    }

    @Override
    protected void onPreviewItem() {
        activityComponent.loadActivities("" + beanItem.getId());
        dateInfoComp.displayEntryDateTime(beanItem);
        peopleInfoComp.displayEntryPeople(beanItem);
        componentTimeLogComp.displayTime(beanItem);

        if (StatusI18nEnum.Open.name().equals(beanItem.getStatus())) {
            removeLayoutStyleName(UIConstants.LINK_COMPLETED);
            quickActionStatusBtn.setCaption(AppContext.getMessage(GenericI18Enum.BUTTON_CLOSE));
            quickActionStatusBtn.setIcon(FontAwesome.ARCHIVE);
        } else {
            addLayoutStyleName(UIConstants.LINK_COMPLETED);
            quickActionStatusBtn.setCaption(AppContext.getMessage(GenericI18Enum.BUTTON_REOPEN));
            quickActionStatusBtn.setIcon(FontAwesome.CLIPBOARD);
        }

    }

    @Override
    protected AdvancedPreviewBeanForm<SimpleComponent> initPreviewForm() {
        return new AdvancedPreviewBeanForm<>();
    }

    @Override
    protected AbstractBeanFieldGroupViewFieldFactory<SimpleComponent> initBeanFormFieldFactory() {
        return new AbstractBeanFieldGroupViewFieldFactory<SimpleComponent>(previewForm) {
            private static final long serialVersionUID = 1L;

            @Override
            protected Field<?> onCreateField(final Object propertyId) {
                if (Component.Field.userlead.equalTo(propertyId)) {
                    return new ProjectUserFormLinkField(beanItem.getUserlead(),
                            beanItem.getUserLeadAvatarId(), beanItem.getUserLeadFullName());
                } else if (Component.Field.id.equalTo(propertyId)) {
                    ContainerViewField containerField = new ContainerViewField();
                    containerField.addComponentField(new BugsComp());
                    return containerField;
                }
                return null;
            }
        };
    }

    @Override
    protected ComponentContainer createButtonControls() {
        ProjectPreviewFormControlsGenerator<SimpleComponent> componentPreviewForm = new
                ProjectPreviewFormControlsGenerator<>(previewForm);
        HorizontalLayout topPanel = componentPreviewForm
                .createButtonControls(ProjectRolePermissionCollections.COMPONENTS);
        quickActionStatusBtn = new Button("", new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                if (StatusI18nEnum.Closed.name().equals(beanItem.getStatus())) {
                    beanItem.setStatus(StatusI18nEnum.Open.name());
                    ComponentReadViewImpl.this.removeLayoutStyleName(UIConstants.LINK_COMPLETED);
                    quickActionStatusBtn.setCaption(AppContext.getMessage(GenericI18Enum.BUTTON_CLOSE));
                    quickActionStatusBtn.setIcon(FontAwesome.ARCHIVE);
                } else {
                    beanItem.setStatus(StatusI18nEnum.Closed.name());
                    ComponentReadViewImpl.this.addLayoutStyleName(UIConstants.LINK_COMPLETED);
                    quickActionStatusBtn.setCaption(AppContext.getMessage(GenericI18Enum.BUTTON_REOPEN));
                    quickActionStatusBtn.setIcon(FontAwesome.CLIPBOARD);
                }

                ComponentService service = ApplicationContextUtil.getSpringBean(ComponentService.class);
                service.updateSelectiveWithSession(beanItem, AppContext.getUsername());

            }
        });

        quickActionStatusBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
        componentPreviewForm.insertToControlBlock(quickActionStatusBtn);

        if (!CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.COMPONENTS)) {
            quickActionStatusBtn.setEnabled(false);
        }
        return topPanel;
    }

    @Override
    protected ComponentContainer createBottomPanel() {
        return activityComponent;
    }

    @Override
    protected String getType() {
        return ProjectTypeConstants.BUG_COMPONENT;
    }

    private class BugsComp extends MVerticalLayout {
        private BugSearchCriteria searchCriteria;
        private MVerticalLayout issueLayout;

        BugsComp() {
            withMargin(false).withWidth("100%");
            MHorizontalLayout header = new MHorizontalLayout().withWidth("100%");

            final CheckBox openSelection = new BugStatusCheckbox(OptionI18nEnum.BugStatus.Open, true);
            CheckBox inprogressSelection = new BugStatusCheckbox(OptionI18nEnum.BugStatus.InProgress, true);
            CheckBox reOpenSelection = new BugStatusCheckbox(OptionI18nEnum.BugStatus.ReOpened, true);
            CheckBox verifiedSelection = new BugStatusCheckbox(OptionI18nEnum.BugStatus.Verified, true);
            CheckBox resolvedSelection = new BugStatusCheckbox(OptionI18nEnum.BugStatus.Resolved, true);

            Label spacingLbl1 = new Label("");

            header.with(openSelection, inprogressSelection, reOpenSelection, verifiedSelection, resolvedSelection, spacingLbl1).withAlign
                    (openSelection, Alignment.MIDDLE_LEFT).withAlign(inprogressSelection, Alignment.MIDDLE_LEFT)
                    .withAlign(reOpenSelection, Alignment.MIDDLE_LEFT).withAlign(verifiedSelection, Alignment
                    .MIDDLE_LEFT).withAlign(resolvedSelection, Alignment.MIDDLE_LEFT)
                    .expand(spacingLbl1);

            issueLayout = new MVerticalLayout();

            searchCriteria = new BugSearchCriteria();
            searchCriteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
            searchCriteria.setComponentids(new SetSearchField<>(beanItem.getId()));
            searchCriteria.setStatuses(new SetSearchField<>(OptionI18nEnum.BugStatus.Open.name(),
                    OptionI18nEnum.BugStatus.InProgress.name(), OptionI18nEnum.BugStatus.ReOpened.name(),
                    OptionI18nEnum.BugStatus.Verified.name(), OptionI18nEnum.BugStatus.Resolved.name()));
            updateSearchStatus();

            this.with(header, issueLayout);
        }

        private void updateTypeSearchStatus(boolean selection, String type) {
            SetSearchField<String> types = searchCriteria.getStatuses();
            if (types == null) {
                types = new SetSearchField<>();
            }
            if (selection) {
                types.addValue(type);
            } else {
                types.removeValue(type);
            }
            searchCriteria.setStatuses(types);
            updateSearchStatus();
        }

        private void updateSearchStatus() {
            issueLayout.removeAllComponents();
            BugService bugService = ApplicationContextUtil.getSpringBean(BugService.class);
            int totalCount = bugService.getTotalCount(searchCriteria);
            for (int i = 0; i < (totalCount / 20) + 1; i++) {
                List<SimpleBug> bugs = bugService.findPagableListByCriteria(new SearchRequest<>(searchCriteria, i + 1, 20));
                if (CollectionUtils.isNotEmpty(bugs)) {
                    for (SimpleBug bug : bugs) {
                        Div bugDiv = new Div();
                        String uid = UUID.randomUUID().toString();

                        A itemLink = new A().setId("tag" + uid).setHref(ProjectLinkBuilder.generateProjectItemLink(
                                bug.getProjectShortName(), bug.getProjectid(), ProjectTypeConstants.BUG, bug.getBugkey() + ""));
                        itemLink.setAttribute("onmouseover", TooltipHelper.projectHoverJsFunction(uid, ProjectTypeConstants.BUG, bug.getId() + ""));
                        itemLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction(uid));
                        itemLink.appendText(String.format("[#%d] - %s", bug.getBugkey(), bug.getSummary()));
                        bugDiv.appendChild(itemLink, DivLessFormatter.EMPTY_SPACE(), TooltipHelper.buildDivTooltipEnable(uid));

                        Label issueLbl = new Label(bugDiv.write(), ContentMode.HTML);
                        if (bug.isCompleted()) {
                            issueLbl.addStyleName("completed");
                        } else if (bug.isOverdue()) {
                            issueLbl.addStyleName("overdue");
                        }

                        MHorizontalLayout rowComp = new MHorizontalLayout();
                        rowComp.setDefaultComponentAlignment(Alignment.TOP_LEFT);
                        rowComp.with(new ELabel(ProjectAssetsManager.getAsset(ProjectTypeConstants.BUG).getHtml(), ContentMode.HTML));

                        String bugPriority = bug.getPriority();
                        Span priorityLink = new Span().appendText(ProjectAssetsManager.getBugPriorityHtml(bugPriority)).setTitle(bugPriority);
                        rowComp.with(new ELabel(priorityLink.write(), ContentMode.HTML));

                        String avatarLink = StorageFactory.getInstance().getAvatarPath(bug.getAssignUserAvatarId(), 16);
                        Img img = new Img(bug.getAssignuserFullName(), avatarLink).setTitle(bug.getAssignuserFullName());
                        rowComp.with(new ELabel(img.write(), ContentMode.HTML));

                        MCssLayout issueWrapper = new MCssLayout(issueLbl);
                        rowComp.with(issueWrapper);
                        issueLayout.add(rowComp);
                    }
                }
            }
        }

        private class BugStatusCheckbox extends CheckBox {
            BugStatusCheckbox(final Enum name, boolean defaultValue) {
                super(AppContext.getMessage(name), defaultValue);
                this.addValueChangeListener(new ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                        updateTypeSearchStatus(BugStatusCheckbox.this.getValue(), name.name());
                    }
                });
            }
        }
    }

    private static class PeopleInfoComp extends MVerticalLayout {
        private static final long serialVersionUID = 1L;

        public void displayEntryPeople(ValuedBean bean) {
            this.removeAllComponents();
            this.withMargin(new MarginInfo(false, false, false, true));

            Label peopleInfoHeader = new Label(FontAwesome.USER.getHtml() + " " +
                    AppContext.getMessage(ProjectCommonI18nEnum.SUB_INFO_PEOPLE), ContentMode.HTML);
            peopleInfoHeader.setStyleName("info-hdr");
            this.addComponent(peopleInfoHeader);

            GridLayout layout = new GridLayout(2, 2);
            layout.setSpacing(true);
            layout.setWidth("100%");
            layout.setMargin(new MarginInfo(false, false, false, true));
            try {
                Label createdLbl = new Label(AppContext.getMessage(ProjectCommonI18nEnum.ITEM_CREATED_PEOPLE));
                createdLbl.setSizeUndefined();
                layout.addComponent(createdLbl, 0, 0);

                String createdUserName = (String) PropertyUtils.getProperty(bean, "createduser");
                String createdUserAvatarId = (String) PropertyUtils.getProperty(bean, "createdUserAvatarId");
                String createdUserDisplayName = (String) PropertyUtils.getProperty(bean, "createdUserFullName");

                ProjectMemberLink createdUserLink = new ProjectMemberLink(createdUserName,
                        createdUserAvatarId, createdUserDisplayName);
                layout.addComponent(createdUserLink, 1, 0);
                layout.setColumnExpandRatio(1, 1.0f);

                Label assigneeLbl = new Label(AppContext.getMessage(ProjectCommonI18nEnum.ITEM_ASSIGN_PEOPLE));
                assigneeLbl.setSizeUndefined();
                layout.addComponent(assigneeLbl, 0, 1);
                String assignUserName = (String) PropertyUtils.getProperty(
                        bean, "userlead");
                String assignUserAvatarId = (String) PropertyUtils.getProperty(
                        bean, "userLeadAvatarId");
                String assignUserDisplayName = (String) PropertyUtils
                        .getProperty(bean, "userLeadFullName");

                ProjectMemberLink assignUserLink = new ProjectMemberLink(assignUserName,
                        assignUserAvatarId, assignUserDisplayName);
                layout.addComponent(assignUserLink, 1, 1);
            } catch (Exception e) {
                LOG.error("Can not build user link {} ", BeanUtility.printBeanObj(bean));
            }

            this.addComponent(layout);
        }
    }

}
