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
package com.mycollab.module.project.view.settings;

import com.hp.gagawa.java.elements.*;
import com.jarektoro.responsivelayout.ResponsiveLayout;
import com.jarektoro.responsivelayout.ResponsiveRow;
import com.mycollab.common.ModuleNameConstants;
import com.mycollab.common.domain.criteria.ActivityStreamSearchCriteria;
import com.mycollab.core.utils.DateTimeUtils;
import com.mycollab.core.utils.NumberUtils;
import com.mycollab.db.arguments.*;
import com.mycollab.module.file.StorageUtils;
import com.mycollab.module.project.*;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.domain.SimpleProjectMember;
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria;
import com.mycollab.module.project.i18n.OptionI18nEnum;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.module.project.i18n.ProjectMemberI18nEnum;
import com.mycollab.module.project.i18n.TimeTrackingI18nEnum;
import com.mycollab.module.project.service.ProjectTicketService;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.ui.components.ProjectPreviewFormControlsGenerator;
import com.mycollab.module.project.view.AbstractProjectPageView;
import com.mycollab.module.project.view.settings.component.NotificationSettingWindow;
import com.mycollab.module.project.view.user.ProjectActivityStreamPagedList;
import com.mycollab.module.user.accountsettings.localization.UserI18nEnum;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.TooltipHelper;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.event.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.*;
import com.mycollab.vaadin.web.ui.AdvancedPreviewBeanForm;
import com.mycollab.vaadin.web.ui.DefaultBeanPagedList;
import com.mycollab.vaadin.web.ui.Depot;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.mycollab.vaadin.web.ui.field.LinkViewField;
import com.mycollab.vaadin.web.ui.field.UserLinkViewField;
import com.vaadin.data.HasValue;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.Label;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.lang.Object;

import static com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class ProjectMemberReadViewImpl extends AbstractProjectPageView implements ProjectMemberReadView {
    private static final long serialVersionUID = 1L;

    private SimpleProjectMember beanItem;
    private AdvancedPreviewBeanForm<SimpleProjectMember> previewForm;

    private ResponsiveLayout bottomLayout;

    public ProjectMemberReadViewImpl() {
        super(UserUIContext.getMessage(ProjectMemberI18nEnum.DETAIL), ProjectAssetsManager.getAsset(ProjectTypeConstants.MEMBER));

        previewForm = initPreviewForm();
        previewForm.setWidth("100%");

        this.addHeaderRightContent(createButtonControls());

        bottomLayout = new ResponsiveLayout();
        this.with(previewForm, bottomLayout);
    }

    @Override
    public SimpleProjectMember getItem() {
        return beanItem;
    }

    @Override
    public HasPreviewFormHandlers<SimpleProjectMember> getPreviewFormHandlers() {
        return previewForm;
    }

    public void previewItem(SimpleProjectMember projectMember) {
        this.beanItem = projectMember;
        previewForm.setFormLayoutFactory(initFormLayoutFactory());
        previewForm.setBeanFormFieldFactory(initBeanFormFieldFactory());
        previewForm.setBean(projectMember);
        createBottomPanel();
    }

    public SimpleProjectMember getBeanItem() {
        return beanItem;
    }

    public AdvancedPreviewBeanForm<SimpleProjectMember> getPreviewForm() {
        return previewForm;
    }

    protected AdvancedPreviewBeanForm<SimpleProjectMember> initPreviewForm() {
        return new AdvancedPreviewBeanForm<>();
    }

    private ComponentContainer createButtonControls() {
        return new ProjectPreviewFormControlsGenerator<>(previewForm).createButtonControls(ProjectPreviewFormControlsGenerator.DELETE_BTN_PRESENTED
                | ProjectPreviewFormControlsGenerator.EDIT_BTN_PRESENTED, ProjectRolePermissionCollections.USERS);
    }

    private void createBottomPanel() {
        bottomLayout.removeAllComponents();
        ResponsiveRow row = bottomLayout.addRow();
        row.setMargin(true);
        row.setSpacing(true);
        ProjectActivityStreamPagedList activityStreamList = new ProjectActivityStreamPagedList();
        row.addColumn().withDisplayRules(12, 12, 12, 6).withComponent(activityStreamList);

        UserAssignmentWidget userAssignmentWidget = new UserAssignmentWidget();
        userAssignmentWidget.showOpenAssignments();
        row.addColumn().withDisplayRules(12, 12, 12, 6).withComponent(userAssignmentWidget);

        ActivityStreamSearchCriteria searchCriteria = new ActivityStreamSearchCriteria();
        searchCriteria.setModuleSet(new SetSearchField<>(ModuleNameConstants.PRJ));
        searchCriteria.setCreatedUser(StringSearchField.and(previewForm.getBean().getUsername()));
        searchCriteria.setExtraTypeIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
        searchCriteria.setSaccountid(new NumberSearchField(AppUI.getAccountId()));
        activityStreamList.setSearchCriteria(searchCriteria);
    }

    protected String initFormTitle() {
        return beanItem.getMemberFullName();
    }

    protected IFormLayoutFactory initFormLayoutFactory() {
        return new ProjectMemberReadLayoutFactory();
    }

    protected AbstractBeanFieldGroupViewFieldFactory<SimpleProjectMember> initBeanFormFieldFactory() {
        return new ProjectMemberFormFieldFactory(previewForm);
    }

    private class ProjectMemberReadLayoutFactory extends AbstractFormLayoutFactory {

        @Override
        public AbstractComponent getLayout() {
            ResponsiveLayout layout = new ResponsiveLayout();
            layout.addStyleNames(WebThemes.BORDER_TOP, WebThemes.BORDER_BOTTOM);
            layout.setWidth("100%");

            ResponsiveRow row = layout.addRow();
            row.setMargin(true);

            Image memberAvatar = UserAvatarControlFactory.createUserAvatarEmbeddedComponent(beanItem.getMemberAvatarId(), 100);
            memberAvatar.addStyleName(WebThemes.CIRCLE_BOX);
            row.addColumn().withDisplayRules(12, 12, 3, 2).withComponent(memberAvatar);

            MVerticalLayout memberInfo = new MVerticalLayout().withMargin(new MarginInfo(false, false, false, true));

            ELabel memberLink = ELabel.h3(beanItem.getMemberFullName()).withUndefinedWidth();
            MButton editNotificationBtn = new MButton(UserUIContext.getMessage(ProjectCommonI18nEnum
                    .ACTION_EDIT_NOTIFICATION), clickEvent -> UI.getCurrent().addWindow(new NotificationSettingWindow(beanItem)))
                    .withStyleName(WebThemes.BUTTON_LINK).withVisible(CurrentProjectVariables.canAccess
                            (ProjectRolePermissionCollections.USERS));
            memberInfo.addComponent(new MHorizontalLayout(memberLink, editNotificationBtn).alignAll(Alignment.MIDDLE_LEFT));

            A roleLink = new A(ProjectLinkGenerator.generateRolePreviewLink(beanItem.getProjectid(), beanItem.getProjectroleid())).appendText(beanItem.getRoleName());
            memberInfo.addComponent(ELabel.html(roleLink.write()).withStyleName(WebThemes.META_INFO).withFullWidth());

            if (Boolean.TRUE.equals(AppUI.showEmailPublicly())) {
                Label memberEmailLabel = ELabel.html(String.format("<a href='mailto:%s'>%s</a>", beanItem.getUsername(),
                        beanItem.getUsername())).withStyleName(WebThemes.META_INFO).withFullWidth();
                memberInfo.addComponent(memberEmailLabel);
            }

            ELabel memberSinceLabel = ELabel.html(UserUIContext.getMessage(UserI18nEnum.OPT_MEMBER_SINCE, UserUIContext.formatPrettyTime(beanItem.getCreatedtime())))
                    .withDescription(UserUIContext.formatDateTime(beanItem.getCreatedtime())).withFullWidth();
            memberInfo.addComponent(memberSinceLabel);

            if (ProjectMemberStatusConstants.ACTIVE.equals(beanItem.getStatus())) {
                Label lastAccessTimeLbl = ELabel.html(UserUIContext.getMessage(UserI18nEnum.OPT_MEMBER_LOGGED_IN, UserUIContext.formatPrettyTime(beanItem.getLastAccessTime())))
                        .withDescription(UserUIContext.formatDateTime(beanItem.getLastAccessTime()));
                memberInfo.addComponent(lastAccessTimeLbl);
            }

            String memberWorksInfo = String.format("%s %s  %s %s  %s %s  %s %s",
                    ProjectAssetsManager.getAsset(ProjectTypeConstants.TASK).getHtml(),
                    new Span().appendText("" + beanItem.getNumOpenTasks()).setTitle(UserUIContext.getMessage(ProjectCommonI18nEnum.OPT_OPEN_TASKS)),
                    ProjectAssetsManager.getAsset(ProjectTypeConstants.BUG).getHtml(), new Span().appendText("" + beanItem
                            .getNumOpenBugs()).setTitle(UserUIContext.getMessage(ProjectCommonI18nEnum.OPT_OPEN_BUGS)), VaadinIcons.MONEY.getHtml(), new Span().appendText("" + NumberUtils.roundDouble(2,
                            beanItem.getTotalBillableLogTime())).setTitle(UserUIContext.getMessage(TimeTrackingI18nEnum.OPT_BILLABLE_HOURS)), VaadinIcons.GIFT.getHtml(), new Span().appendText("" + NumberUtils.roundDouble(2, beanItem.getTotalNonBillableLogTime()
                    )).setTitle(UserUIContext.getMessage(TimeTrackingI18nEnum.OPT_NON_BILLABLE_HOURS)));

            Label memberWorkStatus = ELabel.html(memberWorksInfo).withStyleName(WebThemes.META_INFO);
            memberInfo.addComponent(memberWorkStatus);

            row.addColumn().withDisplayRules(12, 12, 9, 10).withComponent(memberInfo);

            return layout;
        }

        @Override
        protected HasValue<?> onAttachField(Object propertyId, HasValue<?> field) {
            return null;
        }

    }

    private static class ProjectMemberFormFieldFactory extends AbstractBeanFieldGroupViewFieldFactory<SimpleProjectMember> {
        private static final long serialVersionUID = 1L;

        ProjectMemberFormFieldFactory(GenericBeanForm<SimpleProjectMember> form) {
            super(form);
        }

        @Override
        protected HasValue<?> onCreateField(final Object propertyId) {
            SimpleProjectMember projectMember = attachForm.getBean();
            if (propertyId.equals("projectroleid")) {
                return new LinkViewField(attachForm.getBean().getRoleName(), ProjectLinkGenerator.generateRolePreviewLink(
                        projectMember.getProjectid(), projectMember.getProjectroleid()), null);
            } else if (propertyId.equals("username")) {
                return new UserLinkViewField(projectMember.getUsername(),
                        projectMember.getMemberAvatarId(), projectMember.getMemberFullName());
            }
            return null;
        }
    }

    private class UserAssignmentWidget extends Depot {
        private static final long serialVersionUID = 1L;

        private ProjectTicketSearchCriteria searchCriteria;
        private final DefaultBeanPagedList<ProjectTicketService, ProjectTicketSearchCriteria, ProjectTicket> ticketList;

        UserAssignmentWidget() {
            super(UserUIContext.getMessage(ProjectCommonI18nEnum.OPT_ASSIGNMENT_VALUE, 0), new CssLayout());

            CheckBox overdueSelection = new CheckBox(UserUIContext.getMessage(StatusI18nEnum.Overdue));
            overdueSelection.addValueChangeListener(valueChangeEvent -> {
                boolean isOverdueOption = overdueSelection.getValue();
                if (isOverdueOption) {
                    searchCriteria.setDueDate(new DateSearchField(DateTimeUtils.getCurrentDateWithoutMS().toLocalDate(),
                            DateSearchField.LESS_THAN));
                } else {
                    searchCriteria.setDueDate(null);
                }
                updateSearchResult();
            });

            CheckBox isOpenSelection = new CheckBox(UserUIContext.getMessage(StatusI18nEnum.Open), true);
            isOpenSelection.addValueChangeListener(valueChangeEvent -> {
                boolean isOpenOption = isOpenSelection.getValue();
                if (isOpenOption) {
                    searchCriteria.setOpen(new SearchField());
                } else {
                    searchCriteria.setOpen(null);
                }
                updateSearchResult();
            });

            addHeaderElement(overdueSelection);
            addHeaderElement(isOpenSelection);

            ticketList = new DefaultBeanPagedList<>(AppContextUtil.getSpringBean(ProjectTicketService.class),
                    new TicketRowDisplayHandler(), 10);
            bodyContent.addComponent(ticketList);
        }

        private void showOpenAssignments() {
            searchCriteria = new ProjectTicketSearchCriteria();
            searchCriteria.setProjectIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
            searchCriteria.setAssignUser(StringSearchField.and(beanItem.getUsername()));
            searchCriteria.setOpen(new SearchField());
            updateSearchResult();
        }

        private void updateSearchResult() {
            ticketList.setSearchCriteria(searchCriteria);
            setTitle(UserUIContext.getMessage(ProjectCommonI18nEnum.OPT_ASSIGNMENT_VALUE, ticketList.getTotalCount()));
        }
    }

    private static class TicketRowDisplayHandler implements IBeanList.RowDisplayHandler<ProjectTicket> {

        @Override
        public Component generateRow(IBeanList<ProjectTicket> host, ProjectTicket ticket, int rowIndex) {
            MHorizontalLayout rowComp = new MHorizontalLayout().withStyleName("list-row").withFullWidth();
            rowComp.setDefaultComponentAlignment(Alignment.TOP_LEFT);

            Div issueDiv = new Div().appendText(ProjectAssetsManager.getAsset(ticket.getType()).getHtml());
            String status = "";
            if (ticket.isBug()) {
                status = UserUIContext.getMessage(StatusI18nEnum.class, ticket.getStatus());
                rowComp.addStyleName("bug");
            } else if (ticket.isMilestone()) {
                status = UserUIContext.getMessage(OptionI18nEnum.MilestoneStatus.class, ticket.getStatus());
                rowComp.addStyleName("milestone");
            } else if (ticket.isRisk()) {
                status = UserUIContext.getMessage(StatusI18nEnum.class, ticket.getStatus());
                rowComp.addStyleName("risk");
            } else if (ticket.isTask()) {
                status = UserUIContext.getMessage(StatusI18nEnum.class, ticket.getStatus());
                rowComp.addStyleName("task");
            }
            issueDiv.appendChild(new Span().appendText(status).setCSSClass(WebThemes.BLOCK));

            String avatarLink = StorageUtils.getAvatarPath(ticket.getAssignUserAvatarId(), 16);
            Img img = new Img(ticket.getAssignUserFullName(), avatarLink).setCSSClass(WebThemes.CIRCLE_BOX)
                    .setTitle(ticket.getAssignUserFullName());
            issueDiv.appendChild(img, new Text(" "));

            A ticketLink = new A().setId("tag" + TooltipHelper.TOOLTIP_ID);
            ticketLink.setAttribute("onmouseover", TooltipHelper.projectHoverJsFunction(ticket.getType(), ticket.getTypeId() + ""));
            ticketLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction());
            if (ProjectTypeConstants.BUG.equals(ticket.getType()) || ProjectTypeConstants.TASK.equals(ticket.getType())) {
                ticketLink.appendText(ticket.getName());
                ticketLink.setHref(ProjectLinkGenerator.generateProjectItemLink(ticket.getProjectShortName(),
                        ticket.getProjectId(), ticket.getType(), ticket.getExtraTypeId() + ""));
            } else {
                ticketLink.appendText(ticket.getName());
                ticketLink.setHref(ProjectLinkGenerator.generateProjectItemLink(ticket.getProjectShortName(),
                        ticket.getProjectId(), ticket.getType(), ticket.getTypeId() + ""));
            }

            issueDiv.appendChild(ticketLink);
            if (ticket.isClosed()) {
                ticketLink.setCSSClass("completed");
            } else if (ticket.isOverdue()) {
                ticketLink.setCSSClass("overdue");
            }

            rowComp.with(ELabel.html(issueDiv.write()).withFullWidth());
            return rowComp;
        }
    }
}
