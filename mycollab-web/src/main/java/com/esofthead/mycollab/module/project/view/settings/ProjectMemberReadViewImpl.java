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

import com.esofthead.mycollab.common.GenericLinkUtils;
import com.esofthead.mycollab.common.ModuleNameConstants;
import com.esofthead.mycollab.common.domain.criteria.ActivityStreamSearchCriteria;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.configuration.Storage;
import com.esofthead.mycollab.core.arguments.*;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.html.DivLessFormatter;
import com.esofthead.mycollab.module.billing.RegisterStatusConstants;
import com.esofthead.mycollab.module.project.*;
import com.esofthead.mycollab.module.project.dao.ProjectMemberMapper;
import com.esofthead.mycollab.module.project.domain.ProjectGenericTask;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectGenericTaskSearchCriteria;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.project.i18n.ProjectMemberI18nEnum;
import com.esofthead.mycollab.module.project.i18n.TaskI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectGenericTaskService;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.module.project.view.AbstractProjectPageView;
import com.esofthead.mycollab.module.project.view.user.ProjectActivityStreamPagedList;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.utils.TooltipHelper;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.*;
import com.esofthead.mycollab.vaadin.ui.form.field.DefaultViewField;
import com.esofthead.mycollab.vaadin.ui.form.field.LinkViewField;
import com.esofthead.mycollab.vaadin.ui.form.field.UserLinkViewField;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Text;
import com.vaadin.data.Property;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;

import java.util.Date;
import java.util.UUID;

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
        super(AppContext.getMessage(ProjectMemberI18nEnum.VIEW_READ_TITLE), FontAwesome.USER);

        contentWrapper.addStyleName("member-preview");

        previewForm = initPreviewForm();
        previewForm.setWidth("100%");
        previewForm.setStyleName("member-preview-form");

        bottomLayout = new MHorizontalLayout().withMargin(new MarginInfo(true, false, true, false))
                .withWidth("100%");
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
        return new ProjectPreviewFormControlsGenerator<>(previewForm)
                .createButtonControls(ProjectPreviewFormControlsGenerator.DELETE_BTN_PRESENTED
                                | ProjectPreviewFormControlsGenerator.EDIT_BTN_PRESENTED,
                        ProjectRolePermissionCollections.USERS);
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
        searchCriteria.setCreatedUser(new StringSearchField(previewForm.getBean().getUsername()));
        searchCriteria.setExtraTypeIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
        searchCriteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));
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

    protected class ProjectMemberReadLayoutFactory implements IFormLayoutFactory {
        private static final long serialVersionUID = 8833593761607165873L;

        @Override
        public ComponentContainer getLayout() {
            HorizontalLayout blockContent = new HorizontalLayout();
            blockContent.addStyleName("member-block");
            Image memberAvatar = UserAvatarControlFactory.createUserAvatarEmbeddedComponent(
                    beanItem.getMemberAvatarId(), 100);
            blockContent.addComponent(memberAvatar);

            MVerticalLayout memberInfo = new MVerticalLayout().withStyleName("member-info")
                    .withMargin(new MarginInfo(false, false, false, true));

            Label memberLink = new Label(beanItem.getMemberFullName());
            memberLink.setWidth("100%");
            memberLink.addStyleName("member-name");

            memberInfo.addComponent(memberLink);

            String memberRoleLinkPrefix = String.format("<a href=\"%s%s%s\"", AppContext.getSiteUrl(), GenericLinkUtils.URL_PREFIX_PARAM,
                    ProjectLinkGenerator.generateRolePreviewLink(beanItem.getProjectid(), beanItem.getProjectRoleId()));
            Label memberRole = new Label();
            memberRole.setContentMode(ContentMode.HTML);
            memberRole.setStyleName("member-role");
            if (Boolean.TRUE.equals(beanItem.getIsadmin()) || beanItem.getProjectroleid() == null) {
                memberRole.setValue(memberRoleLinkPrefix
                        + "style=\"color: #B00000;\">" + "Project Admin"
                        + "</a>");
            } else {
                memberRole.setValue(memberRoleLinkPrefix
                        + "style=\"color:gray;font-size:12px;\">"
                        + beanItem.getRoleName() + "</a>");
            }
            memberRole.setSizeUndefined();
            memberInfo.addComponent(memberRole);

            Label memberEmailLabel = new Label(String.format("<a href='mailto:%s'>%s</a>", beanItem.getUsername(),
                    beanItem.getUsername()), ContentMode.HTML);
            memberEmailLabel.addStyleName("member-email");
            memberEmailLabel.setWidth("100%");
            memberInfo.addComponent(memberEmailLabel);

            ELabel memberSinceLabel = new ELabel(String.format("Member since: %s", AppContext.formatPrettyTime(beanItem.getJoindate())))
                    .withDescription(AppContext.formatDateTime
                            (beanItem.getJoindate()));
            memberSinceLabel.addStyleName("member-email");
            memberSinceLabel.setWidth("100%");
            memberInfo.addComponent(memberSinceLabel);

            if (RegisterStatusConstants.SENT_VERIFICATION_EMAIL.equals(beanItem.getStatus())) {
                final VerticalLayout waitingNotLayout = new VerticalLayout();
                Label infoStatus = new Label(AppContext
                        .getMessage(ProjectMemberI18nEnum.WAITING_ACCEPT_INVITATION));
                infoStatus.addStyleName("member-email");
                waitingNotLayout.addComponent(infoStatus);

                ButtonLinkLegacy resendInvitationLink = new ButtonLinkLegacy(
                        "Resend Invitation", new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(ClickEvent event) {
                        ProjectMemberMapper projectMemberMapper = ApplicationContextUtil
                                .getSpringBean(ProjectMemberMapper.class);
                        beanItem.setStatus(RegisterStatusConstants.VERIFICATING);
                        projectMemberMapper.updateByPrimaryKeySelective(beanItem);
                        waitingNotLayout.removeAllComponents();
                        Label statusEmail = new Label(AppContext
                                .getMessage(ProjectMemberI18nEnum.SENDING_EMAIL_INVITATION));
                        statusEmail.addStyleName("member-email");
                        waitingNotLayout.addComponent(statusEmail);
                    }
                });
                resendInvitationLink.setStyleName("link");
                resendInvitationLink.addStyleName("member-email");
                waitingNotLayout.addComponent(resendInvitationLink);
                memberInfo.addComponent(waitingNotLayout);
            } else if (RegisterStatusConstants.ACTIVE.equals(beanItem.getStatus())) {
                Label lastAccessTimeLbl = new ELabel(String.format("Logged in %s", AppContext.formatPrettyTime(beanItem.getLastAccessTime())))
                        .withDescription(AppContext.formatDateTime(beanItem.getLastAccessTime()));
                lastAccessTimeLbl.addStyleName("member-email");
                memberInfo.addComponent(lastAccessTimeLbl);
            } else if (RegisterStatusConstants.VERIFICATING.equals(beanItem.getStatus())) {
                Label infoStatus = new Label(AppContext
                        .getMessage(ProjectMemberI18nEnum.WAITING_ACCEPT_INVITATION));
                infoStatus.addStyleName("member-email");
                memberInfo.addComponent(infoStatus);
            }

            String bugStatus = beanItem.getNumOpenBugs() + " open bug";
            if (beanItem.getNumOpenBugs() > 1) {
                bugStatus += "s";
            }

            String taskStatus = beanItem.getNumOpenTasks() + " open task";
            if (beanItem.getNumOpenTasks() > 1) {
                taskStatus += "s";
            }

            Label memberWorkStatus = new Label(bugStatus + " - " + taskStatus);
            memberInfo.addComponent(memberWorkStatus);
            memberInfo.setWidth("100%");

            blockContent.addComponent(memberInfo);
            blockContent.setExpandRatio(memberInfo, 1.0f);
            blockContent.setWidth("100%");

            return blockContent;
        }

        @Override
        public void attachField(Object propertyId, Field<?> field) {

        }

    }

    private static class ProjectMemberFormFieldFactory extends
            AbstractBeanFieldGroupViewFieldFactory<SimpleProjectMember> {

        private static final long serialVersionUID = 1L;

        ProjectMemberFormFieldFactory(GenericBeanForm<SimpleProjectMember> form) {
            super(form);
        }

        @Override
        protected Field<?> onCreateField(final Object propertyId) {
            if (propertyId.equals("projectroleid")) {
                if (Boolean.FALSE.equals(attachForm.getBean().getIsadmin())) {
                    return new LinkViewField(attachForm.getBean().getRoleName(),
                            ProjectLinkBuilder.generateRolePreviewFullLink(
                                    attachForm.getBean().getProjectid(),
                                    attachForm.getBean().getProjectroleid()), null);
                } else {
                    return new DefaultViewField("Project Admin");
                }
            } else if (propertyId.equals("username")) {
                return new UserLinkViewField(attachForm.getBean().getUsername(),
                        attachForm.getBean().getMemberAvatarId(),
                        attachForm.getBean().getMemberFullName());
            }
            return null;
        }
    }

    private class UserAssignmentWidget extends MVerticalLayout {
        private static final long serialVersionUID = 1L;

        private ProjectGenericTaskSearchCriteria searchCriteria;

        private Label titleLbl;
        private final DefaultBeanPagedList<ProjectGenericTaskService, ProjectGenericTaskSearchCriteria, ProjectGenericTask> taskList;

        public UserAssignmentWidget() {
            withSpacing(false).withMargin(false).withWidth("400px");
            titleLbl = new Label(AppContext.getMessage(ProjectCommonI18nEnum.WIDGET_OPEN_ASSIGNMENTS_TITLE, 0));

            final CheckBox overdueSelection = new CheckBox("Overdue");
            overdueSelection.addValueChangeListener(new Property.ValueChangeListener() {
                @Override
                public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                    boolean isOverdueOption = overdueSelection.getValue();
                    if (isOverdueOption) {
                        searchCriteria.setDueDate(new DateSearchField(DateTimeUtils.getCurrentDateWithoutMS()));
                    } else {
                        searchCriteria.setDueDate(null);
                    }
                    updateSearchResult();
                }
            });

            final CheckBox isOpenSelection = new CheckBox("Open", true);
            isOpenSelection.addValueChangeListener(new Property.ValueChangeListener() {
                @Override
                public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                    boolean isOpenOption = isOpenSelection.getValue();
                    if (isOpenOption) {
                        searchCriteria.setIsOpenned(new SearchField());
                    } else {
                        searchCriteria.setIsOpenned(null);
                    }
                    updateSearchResult();
                }
            });

            MHorizontalLayout header = new MHorizontalLayout().withMargin(new MarginInfo(false, true, false, true)).
                    withHeight("34px").with(titleLbl, overdueSelection, isOpenSelection).
                    withAlign(titleLbl, Alignment.MIDDLE_LEFT).withAlign(overdueSelection, Alignment.MIDDLE_RIGHT).
                    withAlign(isOpenSelection, Alignment.MIDDLE_RIGHT).expand(titleLbl);
            header.addStyleName("panel-header");

            taskList = new DefaultBeanPagedList<>(
                    ApplicationContextUtil.getSpringBean(ProjectGenericTaskService.class),
                    new TaskRowDisplayHandler(), 10);
            this.with(header, taskList);
        }

        private void showOpenAssignments() {
            searchCriteria = new ProjectGenericTaskSearchCriteria();
            searchCriteria.setProjectIds(new SetSearchField<>(
                    CurrentProjectVariables.getProjectId()));
            searchCriteria.setAssignUser(new StringSearchField(beanItem.getUsername()));
            searchCriteria.setIsOpenned(new SearchField());
            updateSearchResult();
        }

        private void updateSearchResult() {
            taskList.setSearchCriteria(searchCriteria);
            titleLbl.setValue(AppContext.getMessage(ProjectCommonI18nEnum.WIDGET_OPEN_ASSIGNMENTS_TITLE, taskList.getTotalCount()));
        }
    }

    public static class TaskRowDisplayHandler implements DefaultBeanPagedList.RowDisplayHandler<ProjectGenericTask> {

        @Override
        public Component generateRow(AbstractBeanPagedList host, ProjectGenericTask genericTask, int rowIndex) {
            CssLayout layout = new CssLayout();
            layout.setWidth("100%");
            layout.setStyleName("list-row");

            Div itemDiv = buildItemValue(genericTask);

            Label taskLbl = new Label(itemDiv.write(), ContentMode.HTML);
            if (genericTask.isOverdue()) {
                taskLbl.addStyleName("overdue");
            } else if (genericTask.isClosed()) {
                taskLbl.addStyleName("completed");
            }

            layout.addComponent(taskLbl);

            Div footerDiv = new Div().setCSSClass("activity-date");

            Date dueDate = genericTask.getDueDate();
            if (dueDate != null) {
                footerDiv.appendChild(new Text(AppContext.getMessage(TaskI18nEnum.OPT_DUE_DATE,
                        AppContext.formatPrettyTime(dueDate)))).setTitle(AppContext.formatDate(dueDate));
            } else {
                footerDiv.appendChild(new Text(AppContext.getMessage(TaskI18nEnum.OPT_DUE_DATE, "Undefined")));
            }

            if (genericTask.getAssignUser() != null) {
                footerDiv.appendChild(buildAssigneeValue(genericTask));
            }

            layout.addComponent(new Label(footerDiv.write(), ContentMode.HTML));
            return layout;
        }

        private Div buildItemValue(ProjectGenericTask task) {
            String uid = UUID.randomUUID().toString();
            Div div = new DivLessFormatter();
            Text image = new Text(ProjectAssetsManager.getAsset(task.getType()).getHtml());
            A itemLink = new A().setId("tag" + uid);
            if (ProjectTypeConstants.TASK.equals(task.getType()) || ProjectTypeConstants.BUG.equals(task.getType())) {
                itemLink.setHref(ProjectLinkBuilder.generateProjectItemLink(task.getProjectShortName(),
                        task.getProjectId(), task.getType(), task.getExtraTypeId() + ""));
            } else {
                itemLink.setHref(ProjectLinkBuilder.generateProjectItemLink(
                        task.getProjectShortName(), task.getProjectId(), task.getType(), task.getTypeId() + ""));
            }

            itemLink.setAttribute("onmouseover", TooltipHelper.projectHoverJsFunction(uid, task.getType(), task.getTypeId() + ""));
            itemLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction(uid));
            itemLink.appendText(task.getName());

            div.appendChild(image, DivLessFormatter.EMPTY_SPACE(), itemLink, DivLessFormatter.EMPTY_SPACE(),
                    TooltipHelper.buildDivTooltipEnable(uid));
            return div;
        }

        private Div buildAssigneeValue(ProjectGenericTask task) {
            String uid = UUID.randomUUID().toString();
            Div div = new DivLessFormatter();
            Img userAvatar = new Img("", Storage.getAvatarPath(task.getAssignUserAvatarId(), 16));
            A userLink = new A().setId("tag" + uid).setHref(ProjectLinkBuilder.generateProjectMemberFullLink(
                    task.getProjectId(), task.getAssignUser()));

            userLink.setAttribute("onmouseover", TooltipHelper.userHoverJsFunction(uid, task.getAssignUser()));
            userLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction(uid));
            userLink.appendText(StringUtils.trim(task.getAssignUserFullName(), 30, true));

            String assigneeTxt = AppContext.getMessage(GenericI18Enum.FORM_ASSIGNEE) + ": ";

            div.appendChild(DivLessFormatter.EMPTY_SPACE(), DivLessFormatter.EMPTY_SPACE(), DivLessFormatter.EMPTY_SPACE(),
                    DivLessFormatter.EMPTY_SPACE(), new Text(assigneeTxt), userAvatar, DivLessFormatter.EMPTY_SPACE(), userLink,
                    DivLessFormatter.EMPTY_SPACE(), TooltipHelper.buildDivTooltipEnable(uid));

            return div;
        }
    }
}
