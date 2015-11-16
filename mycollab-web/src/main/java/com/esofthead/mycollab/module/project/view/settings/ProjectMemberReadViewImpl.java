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
import com.esofthead.mycollab.configuration.StorageFactory;
import com.esofthead.mycollab.core.arguments.*;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.core.utils.NumberUtils;
import com.esofthead.mycollab.module.billing.RegisterStatusConstants;
import com.esofthead.mycollab.module.project.*;
import com.esofthead.mycollab.module.project.dao.ProjectMemberMapper;
import com.esofthead.mycollab.module.project.domain.ProjectGenericTask;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectGenericTaskSearchCriteria;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.project.i18n.ProjectMemberI18nEnum;
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
import com.hp.gagawa.java.elements.Span;
import com.vaadin.data.Property;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

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

        previewForm = initPreviewForm();
        previewForm.setWidth("100%");
        previewForm.setStyleName("member-preview-form");

        bottomLayout = new MHorizontalLayout().withMargin(new MarginInfo(true, false, true, false)).withWidth("100%");
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
            Image memberAvatar = UserAvatarControlFactory.createUserAvatarEmbeddedComponent(beanItem.getMemberAvatarId(), 100);
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
                memberRole.setValue(memberRoleLinkPrefix + "style=\"color: #B00000;\">" + "Project Admin" + "</a>");
            } else {
                memberRole.setValue(memberRoleLinkPrefix + "style=\"color:gray;font-size:12px;\">" + beanItem.getRoleName() + "</a>");
            }
            memberRole.setSizeUndefined();
            memberInfo.addComponent(memberRole);

            Label memberEmailLabel = new Label(String.format("<a href='mailto:%s'>%s</a>", beanItem.getUsername(),
                    beanItem.getUsername()), ContentMode.HTML);
            memberEmailLabel.addStyleName(UIConstants.LABEL_META_INFO);
            memberEmailLabel.setWidth("100%");
            memberInfo.addComponent(memberEmailLabel);

            ELabel memberSinceLabel = new ELabel(String.format("Member since: %s", AppContext.formatPrettyTime(beanItem.getJoindate())))
                    .withDescription(AppContext.formatDateTime(beanItem.getJoindate()));
            memberSinceLabel.addStyleName(UIConstants.LABEL_META_INFO);
            memberSinceLabel.setWidth("100%");
            memberInfo.addComponent(memberSinceLabel);

            if (RegisterStatusConstants.SENT_VERIFICATION_EMAIL.equals(beanItem.getStatus())) {
                final VerticalLayout waitingNotLayout = new VerticalLayout();
                Label infoStatus = new Label(AppContext.getMessage(ProjectMemberI18nEnum.WAITING_ACCEPT_INVITATION));
                infoStatus.addStyleName(UIConstants.LABEL_META_INFO);
                waitingNotLayout.addComponent(infoStatus);

                ButtonLink resendInvitationLink = new ButtonLink("Resend Invitation", new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(ClickEvent event) {
                        ProjectMemberMapper projectMemberMapper = ApplicationContextUtil.getSpringBean(ProjectMemberMapper.class);
                        beanItem.setStatus(RegisterStatusConstants.VERIFICATING);
                        projectMemberMapper.updateByPrimaryKeySelective(beanItem);
                        waitingNotLayout.removeAllComponents();
                        Label statusEmail = new Label(AppContext.getMessage(ProjectMemberI18nEnum.SENDING_EMAIL_INVITATION));
                        statusEmail.addStyleName(UIConstants.LABEL_META_INFO);
                        waitingNotLayout.addComponent(statusEmail);
                    }
                });
                resendInvitationLink.setStyleName(UIConstants.BUTTON_LINK);
                resendInvitationLink.addStyleName(UIConstants.LABEL_META_INFO);
                waitingNotLayout.addComponent(resendInvitationLink);
                memberInfo.addComponent(waitingNotLayout);
            } else if (RegisterStatusConstants.ACTIVE.equals(beanItem.getStatus())) {
                Label lastAccessTimeLbl = new ELabel(String.format("Logged in %s", AppContext.formatPrettyTime(beanItem.getLastAccessTime())))
                        .withDescription(AppContext.formatDateTime(beanItem.getLastAccessTime()));
                lastAccessTimeLbl.addStyleName(UIConstants.LABEL_META_INFO);
                memberInfo.addComponent(lastAccessTimeLbl);
            } else if (RegisterStatusConstants.VERIFICATING.equals(beanItem.getStatus())) {
                Label infoStatus = new Label(AppContext.getMessage(ProjectMemberI18nEnum.WAITING_ACCEPT_INVITATION));
                infoStatus.addStyleName(UIConstants.LABEL_META_INFO);
                memberInfo.addComponent(infoStatus);
            }

            String memberWorksInfo = ProjectAssetsManager.getAsset(ProjectTypeConstants.TASK).getHtml() + " " + new Span
                    ().appendText("" + beanItem.getNumOpenTasks()).setTitle("Open tasks") + "  " + ProjectAssetsManager
                    .getAsset(ProjectTypeConstants.BUG).getHtml() + " " + new Span().appendText("" + beanItem
                    .getNumOpenBugs()).setTitle("Open bugs") + " " +
                    " " + FontAwesome.MONEY.getHtml() + " " + new Span().appendText("" + NumberUtils.roundDouble(2,
                    beanItem.getTotalBillableLogTime())).setTitle("Billable hours") + "  " + FontAwesome.GIFT.getHtml() +
                    " " + new Span().appendText("" + NumberUtils.roundDouble(2, beanItem.getTotalNonBillableLogTime())).setTitle("Non billable hours");

            Label memberWorkStatus = new Label(memberWorksInfo, ContentMode.HTML);
            memberWorkStatus.addStyleName(UIConstants.LABEL_META_INFO);
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
                    return new DefaultViewField("Project Admin");
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

        private ProjectGenericTaskSearchCriteria searchCriteria;
        private final DefaultBeanPagedList<ProjectGenericTaskService, ProjectGenericTaskSearchCriteria, ProjectGenericTask> taskList;

        public UserAssignmentWidget() {
            super(AppContext.getMessage(ProjectCommonI18nEnum.WIDGET_OPEN_ASSIGNMENTS_TITLE, 0), new CssLayout());
            this.setWidth("400px");

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

            addHeaderElement(overdueSelection);
            addHeaderElement(isOpenSelection);

            taskList = new DefaultBeanPagedList<>(ApplicationContextUtil.getSpringBean(ProjectGenericTaskService.class),
                    new TaskRowDisplayHandler(), 10);
            bodyContent.addComponent(taskList);
        }

        private void showOpenAssignments() {
            searchCriteria = new ProjectGenericTaskSearchCriteria();
            searchCriteria.setProjectIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
            searchCriteria.setAssignUser(new StringSearchField(beanItem.getUsername()));
            searchCriteria.setIsOpenned(new SearchField());
            updateSearchResult();
        }

        private void updateSearchResult() {
            taskList.setSearchCriteria(searchCriteria);
            setTitle(AppContext.getMessage(ProjectCommonI18nEnum.WIDGET_OPEN_ASSIGNMENTS_TITLE, taskList.getTotalCount()));
        }
    }

    public static class TaskRowDisplayHandler implements DefaultBeanPagedList.RowDisplayHandler<ProjectGenericTask> {

        @Override
        public Component generateRow(AbstractBeanPagedList host, ProjectGenericTask genericTask, int rowIndex) {
            MHorizontalLayout rowComp = new MHorizontalLayout().withStyleName("list-row").withWidth("100%");
            rowComp.setDefaultComponentAlignment(Alignment.TOP_LEFT);
            Div issueDiv = new Div();
            String uid = UUID.randomUUID().toString();
            A taskLink = new A().setId("tag" + uid);

            taskLink.setAttribute("onmouseover", TooltipHelper.projectHoverJsFunction(uid, genericTask.getType(), genericTask.getTypeId() + ""));
            taskLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction(uid));
            if (ProjectTypeConstants.BUG.equals(genericTask.getType()) || ProjectTypeConstants.TASK.equals(genericTask.getType())) {
                taskLink.appendText(String.format("[#%d] - %s", genericTask.getExtraTypeId(), genericTask.getName()));
                taskLink.setHref(ProjectLinkBuilder.generateProjectItemLink(genericTask.getProjectShortName(),
                        genericTask.getProjectId(), genericTask.getType(), genericTask.getExtraTypeId() + ""));
            } else {
                taskLink.appendText(genericTask.getName());
                taskLink.setHref(ProjectLinkBuilder.generateProjectItemLink(genericTask.getProjectShortName(),
                        genericTask.getProjectId(), genericTask.getType(), genericTask.getTypeId() + ""));
            }

            issueDiv.appendChild(taskLink, TooltipHelper.buildDivTooltipEnable(uid));
            Label issueLbl = new Label(issueDiv.write(), ContentMode.HTML);
            if (genericTask.isClosed()) {
                issueLbl.addStyleName("completed");
            } else if (genericTask.isOverdue()) {
                issueLbl.addStyleName("overdue");
            }

            String avatarLink = StorageFactory.getInstance().getAvatarPath(genericTask.getAssignUserAvatarId(), 16);
            Img img = new Img(genericTask.getAssignUserFullName(), avatarLink).setTitle(genericTask
                    .getAssignUserFullName());

            MHorizontalLayout iconsLayout = new MHorizontalLayout().with(new ELabel(ProjectAssetsManager.getAsset
                    (genericTask.getType()).getHtml(), ContentMode.HTML), new ELabel(img.write(), ContentMode.HTML));
            MCssLayout issueWrapper = new MCssLayout(issueLbl);
            rowComp.with(iconsLayout, issueWrapper).expand(issueWrapper);
            return rowComp;
        }
    }
}
