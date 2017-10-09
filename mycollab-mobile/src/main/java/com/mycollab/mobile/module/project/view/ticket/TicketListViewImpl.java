package com.mycollab.mobile.module.project.view.ticket;

import com.mycollab.common.GenericLinkUtils;
import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.mobile.module.project.event.BugEvent;
import com.mycollab.mobile.module.project.event.RiskEvent;
import com.mycollab.mobile.module.project.event.TaskEvent;
import com.mycollab.mobile.module.project.ui.AbstractListPageView;
import com.mycollab.mobile.ui.SearchInputView;
import com.mycollab.mobile.ui.SearchNavigationButton;
import com.mycollab.mobile.ui.AbstractPagedBeanList;
import com.mycollab.mobile.ui.DefaultPagedBeanList;
import com.mycollab.mobile.ui.SearchInputField;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria;
import com.mycollab.module.project.i18n.BugI18nEnum;
import com.mycollab.module.project.i18n.RiskI18nEnum;
import com.mycollab.module.project.i18n.TaskI18nEnum;
import com.mycollab.module.project.i18n.TicketI18nEnum;
import com.mycollab.module.project.service.ProjectTicketService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.touchkit.NavigationBarQuickMenu;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.4.3
 */
@ViewComponent
public class TicketListViewImpl extends AbstractListPageView<ProjectTicketSearchCriteria, ProjectTicket> implements TicketListView {

    public TicketListViewImpl() {
        setCaption(UserUIContext.getMessage(TicketI18nEnum.LIST));
    }

    @Override
    protected AbstractPagedBeanList<ProjectTicketSearchCriteria, ProjectTicket> createBeanList() {
        return new DefaultPagedBeanList<>(AppContextUtil.getSpringBean(ProjectTicketService.class), new TicketRowDisplayHandler());
    }

    @Override
    protected SearchInputField<ProjectTicketSearchCriteria> createSearchField() {
        return null;
    }

    @Override
    protected Component buildRightComponent() {
        MHorizontalLayout controls = new MHorizontalLayout();
        controls.setDefaultComponentAlignment(Alignment.TOP_RIGHT);
        SearchNavigationButton searchBtn = new SearchNavigationButton() {
            @Override
            protected SearchInputView getSearchInputView() {
                return new TicketSearchInputView();
            }
        };
        controls.with(searchBtn);
        NavigationBarQuickMenu actionMenu = new NavigationBarQuickMenu();
        MVerticalLayout content = new MVerticalLayout();
        if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS)) {
            content.with(new Button(UserUIContext.getMessage(TaskI18nEnum.NEW),
                    clickEvent -> EventBusFactory.getInstance().post(new TaskEvent.GotoAdd(TicketListViewImpl.this, null))));
        }

        if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.BUGS)) {
            content.with(new Button(UserUIContext.getMessage(BugI18nEnum.NEW),
                    clickEvent -> EventBusFactory.getInstance().post(new BugEvent.GotoAdd(TicketListViewImpl.this, null))));
        }

        if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.RISKS) && !SiteConfiguration.isCommunityEdition()) {
            content.with(new Button(UserUIContext.getMessage(RiskI18nEnum.NEW),
                    clickEvent -> EventBusFactory.getInstance().post(new RiskEvent.GotoAdd(TicketListViewImpl.this, null))));
        }


        if (content.getComponentCount() > 0) {
            actionMenu.setContent(content);
            controls.with(actionMenu);
        }
        return controls;
    }

    @Override
    public void onBecomingVisible() {
        super.onBecomingVisible();
        AppUI.addFragment("project/ticket/dashboard/" + GenericLinkUtils.encodeParam(CurrentProjectVariables.getProjectId()),
                UserUIContext.getMessage(TicketI18nEnum.LIST));
    }
}
