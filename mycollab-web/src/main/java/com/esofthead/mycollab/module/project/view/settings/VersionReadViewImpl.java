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
import com.esofthead.mycollab.html.DivLessFormatter;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum;
import com.esofthead.mycollab.module.project.i18n.VersionI18nEnum;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.module.project.ui.components.AbstractPreviewItemComp;
import com.esofthead.mycollab.module.project.ui.components.DateInfoComp;
import com.esofthead.mycollab.module.project.ui.components.ProjectActivityComponent;
import com.esofthead.mycollab.module.project.ui.format.VersionFieldFormatter;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.domain.Version;
import com.esofthead.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.module.tracker.service.VersionService;
import com.esofthead.mycollab.schedule.email.project.VersionRelayEmailNotificationAction;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.utils.TooltipHelper;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.*;
import com.esofthead.mycollab.vaadin.ui.form.field.ContainerViewField;
import com.esofthead.mycollab.vaadin.ui.form.field.DateViewField;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Span;
import com.vaadin.data.Property;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.apache.commons.collections.CollectionUtils;
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
public class VersionReadViewImpl extends AbstractPreviewItemComp<Version> implements VersionReadView {
    private static final long serialVersionUID = 1L;

    private Button quickActionStatusBtn;
    private ProjectActivityComponent activityComponent;
    private DateInfoComp dateInfoComp;
    private VersionTimeLogComp versionTimeLogComp;

    public VersionReadViewImpl() {
        super(AppContext.getMessage(VersionI18nEnum.VIEW_READ_TITLE),
                ProjectAssetsManager.getAsset(ProjectTypeConstants.BUG_VERSION));
    }

    @Override
    public HasPreviewFormHandlers<Version> getPreviewFormHandlers() {
        return this.previewForm;
    }

    @Override
    protected void initRelatedComponents() {
        activityComponent = new ProjectActivityComponent(ProjectTypeConstants.BUG_VERSION,
                CurrentProjectVariables.getProjectId(), VersionFieldFormatter.instance(),
                VersionRelayEmailNotificationAction.class);

        dateInfoComp = new DateInfoComp();
        versionTimeLogComp = new VersionTimeLogComp();
        addToSideBar(dateInfoComp, versionTimeLogComp);
    }

    @Override
    protected void onPreviewItem() {
        activityComponent.loadActivities("" + beanItem.getId());
        dateInfoComp.displayEntryDateTime(beanItem);
        versionTimeLogComp.displayTime(beanItem);

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
    protected String initFormTitle() {
        return beanItem.getVersionname();
    }

    @Override
    protected AdvancedPreviewBeanForm<Version> initPreviewForm() {
        return new AdvancedPreviewBeanForm<>();
    }

    @Override
    protected IFormLayoutFactory initFormLayoutFactory() {
        return new DynaFormLayout(ProjectTypeConstants.BUG_VERSION,
                VersionDefaultFormLayoutFactory.getForm(), Version.Field.versionname.name());
    }

    @Override
    protected AbstractBeanFieldGroupViewFieldFactory<Version> initBeanFormFieldFactory() {
        return new AbstractBeanFieldGroupViewFieldFactory<Version>(previewForm) {
            private static final long serialVersionUID = 1L;

            @Override
            protected Field<?> onCreateField(Object propertyId) {
                if (Version.Field.duedate.equalTo(propertyId)) {
                    return new DateViewField(beanItem.getDuedate());
                } else if (Version.Field.id.equalTo(propertyId)) {
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
        ProjectPreviewFormControlsGenerator<Version> versionPreviewForm = new ProjectPreviewFormControlsGenerator<>(
                previewForm);
        HorizontalLayout topPanel = versionPreviewForm.createButtonControls(ProjectRolePermissionCollections.VERSIONS);

        quickActionStatusBtn = new Button("", new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                if (StatusI18nEnum.Closed.name().equals(beanItem.getStatus())) {
                    beanItem.setStatus(StatusI18nEnum.Open.name());
                    VersionReadViewImpl.this.removeLayoutStyleName(UIConstants.LINK_COMPLETED);
                    quickActionStatusBtn.setCaption(AppContext.getMessage(GenericI18Enum.BUTTON_CLOSE));
                    quickActionStatusBtn.setIcon(FontAwesome.ARCHIVE);
                } else {
                    beanItem.setStatus(StatusI18nEnum.Closed.name());
                    VersionReadViewImpl.this.addLayoutStyleName(UIConstants.LINK_COMPLETED);
                    quickActionStatusBtn.setCaption(AppContext.getMessage(GenericI18Enum.BUTTON_REOPEN));
                    quickActionStatusBtn.setIcon(FontAwesome.CLIPBOARD);
                }

                VersionService service = ApplicationContextUtil.getSpringBean(VersionService.class);
                service.updateSelectiveWithSession(beanItem, AppContext.getUsername());
            }
        });

        quickActionStatusBtn.setStyleName(UIConstants.BUTTON_ACTION);
        versionPreviewForm.insertToControlBlock(quickActionStatusBtn);

        if (!CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.VERSIONS)) {
            quickActionStatusBtn.setEnabled(false);
        }

        return topPanel;
    }

    @Override
    protected ComponentContainer createBottomPanel() {
        return activityComponent;
    }

    @Override
    public Version getItem() {
        return beanItem;
    }

    @Override
    protected String getType() {
        return ProjectTypeConstants.BUG_VERSION;
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

            header.with(openSelection, inprogressSelection, reOpenSelection, verifiedSelection, resolvedSelection, spacingLbl1)
                    .withAlign(openSelection, Alignment.MIDDLE_LEFT).withAlign(inprogressSelection, Alignment.MIDDLE_LEFT)
                    .withAlign(reOpenSelection, Alignment.MIDDLE_LEFT).withAlign(verifiedSelection, Alignment.MIDDLE_LEFT)
                    .withAlign(resolvedSelection, Alignment.MIDDLE_LEFT).expand(spacingLbl1);

            issueLayout = new MVerticalLayout();

            searchCriteria = new BugSearchCriteria();
            searchCriteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
            searchCriteria.setVersionids(new SetSearchField<>(beanItem.getId()));
            searchCriteria.setStatuses(new SetSearchField<>(new String[]{OptionI18nEnum.BugStatus.Open.name(),
                    OptionI18nEnum.BugStatus.InProgress.name(), OptionI18nEnum.BugStatus.ReOpened.name(),
                    OptionI18nEnum.BugStatus.Verified.name(), OptionI18nEnum.BugStatus.Resolved.name()}));
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
                        rowComp.with(new ELabel(ProjectAssetsManager.getAsset(ProjectTypeConstants.BUG).getHtml(),
                                ContentMode.HTML).withWidth("-1px"));

                        String bugPriority = bug.getPriority();
                        Span priorityLink = new Span().appendText(ProjectAssetsManager.getBugPriorityHtml(bugPriority)).setTitle(bugPriority);
                        rowComp.with(new ELabel(priorityLink.write(), ContentMode.HTML).withWidth("-1px"));

                        String avatarLink = StorageFactory.getInstance().getAvatarPath(bug.getAssignUserAvatarId(), 16);
                        Img img = new Img(bug.getAssignuserFullName(), avatarLink).setTitle(bug.getAssignuserFullName());
                        rowComp.with(new ELabel(img.write(), ContentMode.HTML));

                        rowComp.with(issueLbl).expand(issueLbl);
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
}