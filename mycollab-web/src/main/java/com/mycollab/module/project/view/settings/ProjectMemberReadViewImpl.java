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
package com.mycollab.module.project.view.settings;

import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Span;
import com.mycollab.common.GenericLinkUtils;
import com.mycollab.common.ModuleNameConstants;
import com.mycollab.common.domain.criteria.ActivityStreamSearchCriteria;
import com.mycollab.configuration.StorageFactory;
import com.mycollab.core.utils.DateTimeUtils;
import com.mycollab.core.utils.NumberUtils;
import com.mycollab.db.arguments.*;
import com.mycollab.module.project.*;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.domain.SimpleProjectMember;
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.module.project.i18n.ProjectMemberI18nEnum;
import com.mycollab.module.project.i18n.ProjectRoleI18nEnum;
import com.mycollab.module.project.i18n.TimeTrackingI18nEnum;
import com.mycollab.module.project.service.ProjectTicketService;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.view.AbstractProjectPageView;
import com.mycollab.module.project.view.settings.component.NotificationSettingWindow;
import com.mycollab.module.project.view.user.ProjectActivityStreamPagedList;
import com.mycollab.module.user.accountsettings.localization.UserI18nEnum;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.TooltipHelper;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.*;
import com.mycollab.vaadin.ui.field.DefaultViewField;
import com.mycollab.vaadin.web.ui.*;
import com.mycollab.vaadin.web.ui.field.LinkViewField;
import com.mycollab.vaadin.web.ui.field.UserLinkViewField;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import static com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import static com.mycollab.vaadin.TooltipHelper.TOOLTIP_ID;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class ProjectMemberReadViewImpl extends AbstractProjectPageView implements ProjectMemberReadView {
    private static final long serialVersionUID = 1L;

    private SimpleProjectMember beanItem;
    private AdvancedPreviewBeanForm<SimpleProjectMember> previewForm;

    private MHorizontalLayout bottomLayout;

    public ProjectMemberReadViewImpl() {
        super(UserUIContext.getMessage(ProjectMemberI18nEnum.DETAIL), FontAwesome.USER);

        previewForm = initPreviewForm();
        previewForm.setWidth("100%");

        bottomLayout = new MHorizontalLayout().withMargin(new MarginInfo(true, false, true, false)).withFullWidth();
        this.addHeaderRightContent(createButtonControls());
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

    public void previewItem(final SimpleProjectMember item) {
        this.beanItem = item;
        previewForm.setFormLayoutFactory(initFormLayoutFactory());
        previewForm.setBeanFormFieldFactory(initBeanFormFieldFactory());
        previewForm.setBean(item);
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

        MVerticalLayout leftColumn = new MVerticalLayout().withMargin(new MarginInfo(false, true, false, false));
        ProjectActivityStreamPagedList activityStreamList = new ProjectActivityStreamPagedList();
        leftColumn.with(activityStreamList);

        UserAssignmentWidget userAssignmentWidget = new UserAssignmentWidget();
        userAssignmentWidget.showOpenAssignments();
        bottomLayout.with(leftColumn, userAssignmentWidget).expand(leftColumn);

        ActivityStreamSearchCriteria searchCriteria = new ActivityStreamSearchCriteria();
        searchCriteria.setModuleSet(new SetSearchField<>(ModuleNameConstants.PRJ));
        searchCriteria.setCreatedUser(StringSearchField.and(previewForm.getBean().getUsername()));
        searchCriteria.setExtraTypeIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
        searchCriteria.setSaccountid(new NumberSearchField(MyCollabUI.getAccountId()));
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
        private static final long serialVersionUID = 8833593761607165873L;

        @Override
        public AbstractComponent getLayout() {
            HorizontalLayout blockContent = new HorizontalLayout();
            blockContent.addStyleName("member-block");
            Image memberAvatar = UserAvatarControlFactory.createUserAvatarEmbeddedComponent(beanItem.getMemberAvatarId(), 100);
            memberAvatar.addStyleName(UIConstants.CIRCLE_BOX);
            blockContent.addComponent(memberAvatar);

            MVerticalLayout memberInfo = new MVerticalLayout().withMargin(new MarginInfo(false, false, false, true));

            ELabel memberLink = ELabel.h3(beanItem.getMemberFullName()).withWidthUndefined();
            MButton editNotificationBtn = new MButton(UserUIContext.getMessage(ProjectCommonI18nEnum
                    .ACTION_EDIT_NOTIFICATION), clickEvent -> UI.getCurrent().addWindow(new NotificationSettingWindow(beanItem)))
                    .withStyleName(WebThemes.BUTTON_LINK).withVisible(CurrentProjectVariables.canAccess
                            (ProjectRolePermissionCollections.USERS));
            memberInfo.addComponent(new MHorizontalLayout(memberLink, editNotificationBtn).alignAll(Alignment.MIDDLE_LEFT));

            String memberRoleLinkPrefix = String.format("<a href=\"%s%s%s\"", MyCollabUI.getSiteUrl(), GenericLinkUtils.URL_PREFIX_PARAM,
                    ProjectLinkGenerator.generateRolePreviewLink(beanItem.getProjectid(), beanItem.getProjectroleid()));
            ELabel memberRole = new ELabel(ContentMode.HTML).withStyleName(UIConstants.META_INFO).withWidthUndefined();
            if (Boolean.TRUE.equals(beanItem.getIsadmin()) || beanItem.getProjectroleid() == null) {
                memberRole.setValue(String.format("%sstyle=\"color: #B00000;\">%s</a>", memberRoleLinkPrefix,
                        UserUIContext.getMessage(ProjectRoleI18nEnum.OPT_ADMIN_ROLE_DISPLAY)));
            } else {
                memberRole.setValue(memberRoleLinkPrefix + "style=\"color:gray;font-size:12px;\">" + beanItem.getRoleName() + "</a>");
            }
            memberInfo.addComponent(memberRole);

            if (Boolean.TRUE.equals(MyCollabUI.showEmailPublicly())) {
                Label memberEmailLabel = ELabel.html(String.format("<a href='mailto:%s'>%s</a>", beanItem.getUsername(),
                        beanItem.getUsername())).withStyleName(UIConstants.META_INFO).withFullWidth();
                memberInfo.addComponent(memberEmailLabel);
            }

            ELabel memberSinceLabel = ELabel.html(UserUIContext.getMessage(UserI18nEnum.OPT_MEMBER_SINCE, UserUIContext.formatPrettyTime(beanItem.getJoindate())))
                    .withDescription(UserUIContext.formatDateTime(beanItem.getJoindate())).withFullWidth();
            memberInfo.addComponent(memberSinceLabel);

            if (ProjectMemberStatusConstants.ACTIVE.equals(beanItem.getStatus())) {
                Label lastAccessTimeLbl = ELabel.html(UserUIContext.getMessage(UserI18nEnum.OPT_MEMBER_LOGGED_IN, UserUIContext.formatPrettyTime(beanItem.getLastAccessTime())))
                        .withDescription(UserUIContext.formatDateTime(beanItem.getLastAccessTime()));
                memberInfo.addComponent(lastAccessTimeLbl);
            }

            String memberWorksInfo = String.format("%s %s  %s %s  %s %s  %s %s",
                    ProjectAssetsManager.getAsset(ProjectTypeConstants.TASK).getHtml(),
                    new Span().appendText("" + beanItem.getNumOpenTasks()).setTitle(UserUIContext.getMessage(ProjectCommonI18nEnum.OPT_OPEN_TASKS)), ProjectAssetsManager
                            .getAsset(ProjectTypeConstants.BUG).getHtml(), new Span().appendText("" + beanItem
                            .getNumOpenBugs()).setTitle(UserUIContext.getMessage(ProjectCommonI18nEnum.OPT_OPEN_BUGS)), FontAwesome.MONEY.getHtml(), new Span().appendText("" + NumberUtils.roundDouble(2,
                            beanItem.getTotalBillableLogTime())).setTitle(UserUIContext.getMessage(TimeTrackingI18nEnum.OPT_BILLABLE_HOURS)), FontAwesome.GIFT.getHtml(), new Span().appendText("" + NumberUtils.roundDouble(2, beanItem.getTotalNonBillableLogTime()
                    )).setTitle(UserUIContext.getMessage(TimeTrackingI18nEnum.OPT_NON_BILLABLE_HOURS)));

            Label memberWorkStatus = ELabel.html(memberWorksInfo).withStyleName(UIConstants.META_INFO);
            memberInfo.addComponent(memberWorkStatus);
            memberInfo.setWidth("100%");

            blockContent.addComponent(memberInfo);
            blockContent.setExpandRatio(memberInfo, 1.0f);
            blockContent.setWidth("100%");

            return blockContent;
        }

        @Override
        protected Component onAttachField(Object propertyId, Field<?> field) {
            return null;
        }

    }

    private static class ProjectMemberFormFieldFactory extends AbstractBeanFieldGroupViewFieldFactory<SimpleProjectMember> {
        private static final long serialVersionUID = 1L;

        ProjectMemberFormFieldFactory(GenericBeanForm<SimpleProjectMember> form) {
            super(form);
        }

        @Override
        protected Field<?> onCreateField(final Object propertyId) {
            if (propertyId.equals("projectroleid")) {
                if (Boolean.FALSE.equals(attachForm.getBean().getIsadmin())) {
                    return new LinkViewField(attachForm.getBean().getRoleName(), ProjectLinkBuilder.generateRolePreviewFullLink(
                            attachForm.getBean().getProjectid(), attachForm.getBean().getProjectroleid()), null);
                } else {
                    return new DefaultViewField(UserUIContext.getMessage(ProjectRoleI18nEnum.OPT_ADMIN_ROLE_DISPLAY));
                }
            } else if (propertyId.equals("username")) {
                return new UserLinkViewField(attachForm.getBean().getUsername(),
                        attachForm.getBean().getMemberAvatarId(), attachForm.getBean().getMemberFullName());
            }
            return null;
        }
    }

    private class UserAssignmentWidget extends Depot {
        private static final long serialVersionUID = 1L;

        private ProjectTicketSearchCriteria searchCriteria;
        private final DefaultBeanPagedList<ProjectTicketService, ProjectTicketSearchCriteria, ProjectTicket> taskList;

        UserAssignmentWidget() {
            super(UserUIContext.getMessage(ProjectCommonI18nEnum.OPT_ASSIGNMENT_VALUE, 0), new CssLayout());
            this.setWidth("400px");

            final CheckBox overdueSelection = new CheckBox(UserUIContext.getMessage(StatusI18nEnum.Overdue));
            overdueSelection.addValueChangeListener(valueChangeEvent -> {
                boolean isOverdueOption = overdueSelection.getValue();
                if (isOverdueOption) {
                    searchCriteria.setDueDate(new DateSearchField(DateTimeUtils.getCurrentDateWithoutMS()));
                } else {
                    searchCriteria.setDueDate(null);
                }
                updateSearchResult();
            });

            final CheckBox isOpenSelection = new CheckBox(UserUIContext.getMessage(StatusI18nEnum.Open), true);
            isOpenSelection.addValueChangeListener(valueChangeEvent -> {
                boolean isOpenOption = isOpenSelection.getValue();
                if (isOpenOption) {
                    searchCriteria.setIsOpenned(new SearchField());
                } else {
                    searchCriteria.setIsOpenned(null);
                }
                updateSearchResult();
            });

            addHeaderElement(overdueSelection);
            addHeaderElement(isOpenSelection);

            taskList = new DefaultBeanPagedList<>(AppContextUtil.getSpringBean(ProjectTicketService.class),
                    new TaskRowDisplayHandler(), 10);
            bodyContent.addComponent(taskList);
        }

        private void showOpenAssignments() {
            searchCriteria = new ProjectTicketSearchCriteria();
            searchCriteria.setProjectIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
            searchCriteria.setAssignUser(StringSearchField.and(beanItem.getUsername()));
            searchCriteria.setIsOpenned(new SearchField());
            updateSearchResult();
        }

        private void updateSearchResult() {
            taskList.setSearchCriteria(searchCriteria);
            setTitle(UserUIContext.getMessage(ProjectCommonI18nEnum.OPT_ASSIGNMENT_VALUE, taskList.getTotalCount()));
        }
    }

    private static class TaskRowDisplayHandler implements IBeanList.RowDisplayHandler<ProjectTicket> {

        @Override
        public Component generateRow(IBeanList<ProjectTicket> host, ProjectTicket genericTask, int rowIndex) {
            MHorizontalLayout rowComp = new MHorizontalLayout().withStyleName("list-row").withFullWidth();
            rowComp.setDefaultComponentAlignment(Alignment.TOP_LEFT);

            A taskLink = new A().setId("tag" + TOOLTIP_ID);
            taskLink.setAttribute("onmouseover", TooltipHelper.projectHoverJsFunction(genericTask.getType(), genericTask.getTypeId() + ""));
            taskLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction());
            if (ProjectTypeConstants.BUG.equals(genericTask.getType()) || ProjectTypeConstants.TASK.equals(genericTask.getType())) {
                taskLink.appendText(String.format("[#%d] - %s", genericTask.getExtraTypeId(), genericTask.getName()));
                taskLink.setHref(ProjectLinkGenerator.generateProjectItemLink(genericTask.getProjectShortName(),
                        genericTask.getProjectId(), genericTask.getType(), genericTask.getExtraTypeId() + ""));
            } else {
                taskLink.appendText(genericTask.getName());
                taskLink.setHref(ProjectLinkGenerator.generateProjectItemLink(genericTask.getProjectShortName(),
                        genericTask.getProjectId(), genericTask.getType(), genericTask.getTypeId() + ""));
            }
            Label issueLbl = ELabel.html(taskLink.write());
            if (genericTask.isClosed()) {
                issueLbl.addStyleName("completed");
            } else if (genericTask.isOverdue()) {
                issueLbl.addStyleName("overdue");
            }

            String avatarLink = StorageFactory.getAvatarPath(genericTask.getAssignUserAvatarId(), 16);
            Img img = new Img(genericTask.getAssignUserFullName(), avatarLink).setCSSClass(UIConstants.CIRCLE_BOX)
                    .setTitle(genericTask.getAssignUserFullName());

            MHorizontalLayout iconsLayout = new MHorizontalLayout().with(ELabel.fontIcon(ProjectAssetsManager.getAsset(
                    genericTask.getType())), ELabel.html(img.write()));
            MCssLayout issueWrapper = new MCssLayout(issueLbl);
            rowComp.with(iconsLayout, issueWrapper).expand(issueWrapper);
            return rowComp;
        }
    }
}
