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

package com.esofthead.mycollab.module.project.view.bug;

import com.esofthead.mycollab.common.CommentType;
import com.esofthead.mycollab.common.ModuleNameConstants;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.esofthead.mycollab.configuration.StorageManager;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.html.DivLessFormatter;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.project.i18n.VersionI18nEnum;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.module.project.ui.components.AbstractPreviewItemComp;
import com.esofthead.mycollab.module.project.ui.components.CommentDisplay;
import com.esofthead.mycollab.module.project.ui.components.DateInfoComp;
import com.esofthead.mycollab.module.project.ui.components.DynaFormLayout;
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
import com.esofthead.mycollab.vaadin.mvp.ViewScope;
import com.esofthead.mycollab.vaadin.ui.*;
import com.esofthead.mycollab.vaadin.ui.form.field.ContainerViewField;
import com.esofthead.mycollab.vaadin.ui.form.field.DateViewField;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Text;
import com.vaadin.data.Property;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComponentContainer;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;

import java.util.UUID;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent(scope = ViewScope.PROTOTYPE)
public class VersionReadViewImpl extends AbstractPreviewItemComp<Version>
        implements VersionReadView {
    private static final long serialVersionUID = 1L;

    private CommentDisplay commentDisplay;
    private VersionHistoryLogList historyLogList;

    private Button quickActionStatusBtn;

    private DateInfoComp dateInfoComp;

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
        commentDisplay = new CommentDisplay(CommentType.PRJ_VERSION,
                CurrentProjectVariables.getProjectId(), true, true,
                VersionRelayEmailNotificationAction.class);
        commentDisplay.setWidth("100%");

        historyLogList = new VersionHistoryLogList(ModuleNameConstants.PRJ,
                ProjectTypeConstants.BUG_VERSION);

        dateInfoComp = new DateInfoComp();
        addToSideBar(dateInfoComp);
    }

    @Override
    protected void onPreviewItem() {
        commentDisplay.loadComments("" + beanItem.getId());
        historyLogList.loadHistory(beanItem.getId());
        dateInfoComp.displayEntryDateTime(beanItem);

        if (StatusI18nEnum.Open.name().equals(beanItem.getStatus())) {
            removeLayoutStyleName(UIConstants.LINK_COMPLETED);
            quickActionStatusBtn.setCaption(AppContext
                    .getMessage(GenericI18Enum.BUTTON_CLOSE));
            quickActionStatusBtn.setIcon(FontAwesome.ARCHIVE);
        } else {
            addLayoutStyleName(UIConstants.LINK_COMPLETED);
            quickActionStatusBtn.setCaption(AppContext
                    .getMessage(GenericI18Enum.BUTTON_REOPEN));
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
                VersionDefaultFormLayoutFactory.getForm(),
                Version.Field.versionname.name());
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
        final HorizontalLayout topPanel = versionPreviewForm
                .createButtonControls(ProjectRolePermissionCollections.VERSIONS);

        quickActionStatusBtn = new Button("", new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                if (StatusI18nEnum.Closed.name().equals(beanItem.getStatus())) {
                    beanItem.setStatus(StatusI18nEnum.Open.name());
                    VersionReadViewImpl.this
                            .removeLayoutStyleName(UIConstants.LINK_COMPLETED);
                    quickActionStatusBtn.setCaption(AppContext
                            .getMessage(GenericI18Enum.BUTTON_CLOSE));
                    quickActionStatusBtn.setIcon(FontAwesome.ARCHIVE);
                } else {
                    beanItem.setStatus(StatusI18nEnum.Closed.name());

                    VersionReadViewImpl.this
                            .addLayoutStyleName(UIConstants.LINK_COMPLETED);
                    quickActionStatusBtn.setCaption(AppContext
                            .getMessage(GenericI18Enum.BUTTON_REOPEN));
                    quickActionStatusBtn.setIcon(FontAwesome.CLIPBOARD);
                }

                VersionService service = ApplicationContextUtil
                        .getSpringBean(VersionService.class);
                service.updateSelectiveWithSession(beanItem,
                        AppContext.getUsername());

            }
        });

        quickActionStatusBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
        versionPreviewForm.insertToControlBlock(quickActionStatusBtn);

        if (!CurrentProjectVariables
                .canWrite(ProjectRolePermissionCollections.VERSIONS)) {
            quickActionStatusBtn.setEnabled(false);
        }

        return topPanel;
    }

    @Override
    protected ComponentContainer createBottomPanel() {
        final TabSheetLazyLoadComponent tabContainer = new TabSheetLazyLoadComponent();
        tabContainer.addTab(commentDisplay, AppContext.getMessage(ProjectCommonI18nEnum.TAB_COMMENT), FontAwesome.COMMENTS);
        tabContainer.addTab(historyLogList, AppContext.getMessage(ProjectCommonI18nEnum.TAB_HISTORY), FontAwesome.HISTORY);
        return tabContainer;
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
        private DefaultBeanPagedList<BugService, BugSearchCriteria, SimpleBug> bugList;

        BugsComp() {
            withMargin(false).withWidth("100%");
            MHorizontalLayout header = new MHorizontalLayout().withWidth("100%");

            final CheckBox openSelection = new BugStatusCheckbox(OptionI18nEnum.BugStatus.Open, true);
            CheckBox inprogressSelection = new BugStatusCheckbox(OptionI18nEnum.BugStatus.InProgress, true);
            CheckBox reOpenSelection = new BugStatusCheckbox(OptionI18nEnum.BugStatus.ReOpened, true);
            CheckBox verifiedSelection = new BugStatusCheckbox(OptionI18nEnum.BugStatus.Verified, true);
            CheckBox resolvedSelection = new BugStatusCheckbox(OptionI18nEnum.BugStatus.Resolved, true);

            Label spacingLbl1 = new Label("");

            Button chartBtn = new Button("");

            chartBtn.setIcon(FontAwesome.TH_LARGE);
            chartBtn.setStyleName(UIConstants.THEME_GREEN_LINK);

            header.with(openSelection, inprogressSelection, reOpenSelection, verifiedSelection, resolvedSelection, spacingLbl1,
                    chartBtn).withAlign
                    (openSelection, Alignment.MIDDLE_LEFT).withAlign(inprogressSelection, Alignment.MIDDLE_LEFT)
                    .withAlign(reOpenSelection, Alignment.MIDDLE_LEFT).withAlign(verifiedSelection, Alignment
                    .MIDDLE_LEFT).withAlign(resolvedSelection, Alignment.MIDDLE_LEFT)
                    .withAlign(chartBtn, Alignment
                            .MIDDLE_RIGHT)
                    .expand(spacingLbl1);

            bugList = new DefaultBeanPagedList<>(ApplicationContextUtil.getSpringBean(BugService.class), new
                    AssignmentRowDisplay(), 10);
            bugList.setControlStyle("borderlessControl");

            searchCriteria = new BugSearchCriteria();
            searchCriteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
            searchCriteria.setVersionids(new SetSearchField<>(beanItem.getId()));
            searchCriteria.setStatuses(new SetSearchField<>(new String[]{OptionI18nEnum.BugStatus.Open.name(),
                    OptionI18nEnum
                            .BugStatus.InProgress.name(), OptionI18nEnum.BugStatus.ReOpened.name(), OptionI18nEnum.BugStatus
                    .Verified.name(), OptionI18nEnum.BugStatus.Resolved.name()}));
            updateSearchStatus();

            this.with(header, bugList);
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
            bugList.setSearchCriteria(searchCriteria);
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

    private static class AssignmentRowDisplay implements AbstractBeanPagedList.RowDisplayHandler<SimpleBug> {
        @Override
        public com.vaadin.ui.Component generateRow(SimpleBug bug, int rowIndex) {
            Label lbl = new Label(buildDivLine(bug).write(), ContentMode.HTML);
            if (bug.isOverdue()) {
                lbl.addStyleName("overdue");
            } else if (bug.isCompleted()) {
                lbl.addStyleName("completed");
            }
            return lbl;
        }

        private Div buildDivLine(SimpleBug bug) {
            Div div = new Div().setCSSClass("project-tableless");
            div.appendChild(buildItemValue(bug), buildAssigneeValue(bug), buildLastUpdateTime(bug));
            return div;
        }

        private Div buildItemValue(SimpleBug bug) {
            String uid = UUID.randomUUID().toString();
            Div div = new Div();
            Text image = new Text(ProjectAssetsManager.getAsset(ProjectTypeConstants.BUG).getHtml());

            A itemLink = new A();
            itemLink.setId("tag" + uid);
            itemLink.setHref(ProjectLinkBuilder.generateProjectItemLink(
                    bug.getProjectShortName(),
                    bug.getProjectid(), ProjectTypeConstants.BUG,
                    bug.getBugkey() + ""));

            String arg17 = "'" + uid + "'";
            String arg18 = "'" + ProjectTypeConstants.BUG + "'";
            String arg19 = "'" + bug.getId() + "'";
            String arg20 = "'" + AppContext.getSiteUrl() + "tooltip/'";
            String arg21 = "'" + AppContext.getAccountId() + "'";
            String arg22 = "'" + AppContext.getSiteUrl() + "'";
            String arg23 = AppContext.getSession().getTimezone();
            String arg24 = "'" + AppContext.getUserLocale().toString() + "'";

            String mouseOverFunc = String.format(
                    "return overIt(%s,%s,%s,%s,%s,%s,%s,%s);", arg17, arg18, arg19,
                    arg20, arg21, arg22, arg23, arg24);
            itemLink.setAttribute("onmouseover", mouseOverFunc);
            itemLink.appendText(String.format("[%s-%d] %s", bug.getProjectShortName(), bug.getBugkey(), bug
                    .getSummary()));

            div.appendChild(image, DivLessFormatter.EMPTY_SPACE(), itemLink, DivLessFormatter.EMPTY_SPACE(),
                    TooltipHelper.buildDivTooltipEnable(uid));
            return div.setCSSClass("columnExpand");
        }

        private Div buildAssigneeValue(SimpleBug bug) {
            if (bug.getAssignuser() == null) {
                return new Div().setCSSClass("column200");
            }
            String uid = UUID.randomUUID().toString();
            Div div = new Div();
            Img userAvatar = new Img("", StorageManager.getAvatarLink(
                    bug.getAssignUserAvatarId(), 16));
            A userLink = new A();
            userLink.setId("tag" + uid);
            userLink.setHref(ProjectLinkBuilder.generateProjectMemberFullLink(
                    bug.getProjectid(),
                    bug.getAssignuser()));

            userLink.setAttribute("onmouseover", TooltipHelper.buildUserHtmlTooltip(uid, bug.getAssignuser()));
            userLink.appendText(bug.getAssignuserFullName());

            div.appendChild(userAvatar, DivLessFormatter.EMPTY_SPACE(), userLink, DivLessFormatter.EMPTY_SPACE(),
                    TooltipHelper.buildDivTooltipEnable(uid));

            return div.setCSSClass("column200");
        }

        private Div buildLastUpdateTime(SimpleBug bug) {
            Div div = new Div();
            div.appendChild(new Text(DateTimeUtils.getPrettyDateValue(bug.getLastupdatedtime(), AppContext.getUserLocale())));
            return div.setCSSClass("column100");
        }
    }
}