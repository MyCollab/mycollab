/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.mobile.module.project.view.milestone;

import com.esofthead.vaadin.navigationbarquickmenu.NavigationBarQuickMenu;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Span;
import com.mycollab.configuration.StorageFactory;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.mobile.module.project.events.MilestoneEvent;
import com.mycollab.mobile.module.project.ui.AbstractListPageView;
import com.mycollab.mobile.ui.AbstractPagedBeanList;
import com.mycollab.mobile.ui.DefaultPagedBeanList;
import com.mycollab.mobile.ui.SearchInputField;
import com.mycollab.mobile.ui.MobileUIConstants;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectLinkBuilder;
import com.mycollab.module.project.ProjectLinkGenerator;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.SimpleMilestone;
import com.mycollab.module.project.domain.criteria.MilestoneSearchCriteria;
import com.mycollab.module.project.i18n.MilestoneI18nEnum;
import com.mycollab.module.project.i18n.OptionI18nEnum;
import com.mycollab.module.project.service.MilestoneService;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.UIConstants;
import com.vaadin.addon.touchkit.ui.Toolbar;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
@ViewComponent
public class MilestoneListViewImpl extends AbstractListPageView<MilestoneSearchCriteria, SimpleMilestone> implements MilestoneListView {
    private static final long serialVersionUID = 2799191640785637556L;

    private OptionI18nEnum.MilestoneStatus status = OptionI18nEnum.MilestoneStatus.InProgress;
    private Button closedMilestoneBtn, inProgressMilestoneBtn, futureMilestoneBtn;

    @Override
    protected AbstractPagedBeanList<MilestoneSearchCriteria, SimpleMilestone> createBeanList() {
        return new DefaultPagedBeanList<>(AppContextUtil.getSpringBean(MilestoneService.class), new MilestoneRowDisplayHandler());
    }

    @Override
    protected SearchInputField<MilestoneSearchCriteria> createSearchField() {
        return new SearchInputField<MilestoneSearchCriteria>() {
            @Override
            protected MilestoneSearchCriteria fillUpSearchCriteria(String value) {
                MilestoneSearchCriteria searchCriteria = new MilestoneSearchCriteria();
                searchCriteria.setProjectIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
                searchCriteria.setMilestoneName(StringSearchField.and(value));
                return searchCriteria;
            }
        };
    }

    @Override
    protected Component buildRightComponent() {
        NavigationBarQuickMenu menu = new NavigationBarQuickMenu();
        menu.setButtonCaption("...");
        MVerticalLayout content = new MVerticalLayout();
        content.with(new Button(AppContext.getMessage(MilestoneI18nEnum.NEW),
                clickEvent -> EventBusFactory.getInstance().post(new MilestoneEvent.GotoAdd(MilestoneListViewImpl.this, null))));
        menu.setContent(content);
        return menu;
    }

    @Override
    public void displayStatus(OptionI18nEnum.MilestoneStatus status) {
        this.status = status;
        MilestoneSearchCriteria searchCriteria = new MilestoneSearchCriteria();
        searchCriteria.setProjectIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
        searchCriteria.setStatuses(new SetSearchField<>(status.name()));
        itemList.search(searchCriteria);
        updateTabStatus();
    }

    @Override
    public void onBecomingVisible() {
        super.onBecomingVisible();
        updateTabStatus();
    }

    private void updateTabStatus() {
        if (status == OptionI18nEnum.MilestoneStatus.Closed) {
            this.setCaption(AppContext.getMessage(OptionI18nEnum.MilestoneStatus.Closed));
            closedMilestoneBtn.setStyleName(MobileUIConstants.BUTTON_ACTION);
            inProgressMilestoneBtn.setStyleName(MobileUIConstants.BUTTON_OPTION);
            futureMilestoneBtn.setStyleName(MobileUIConstants.BUTTON_OPTION);
        } else if (status == OptionI18nEnum.MilestoneStatus.Future) {
            this.setCaption(AppContext.getMessage(OptionI18nEnum.MilestoneStatus.Future));
            closedMilestoneBtn.setStyleName(MobileUIConstants.BUTTON_OPTION);
            inProgressMilestoneBtn.setStyleName(MobileUIConstants.BUTTON_OPTION);
            futureMilestoneBtn.setStyleName(MobileUIConstants.BUTTON_ACTION);
        } else {
            this.setCaption(AppContext.getMessage(OptionI18nEnum.MilestoneStatus.InProgress));
            closedMilestoneBtn.setStyleName(MobileUIConstants.BUTTON_OPTION);
            inProgressMilestoneBtn.setStyleName(MobileUIConstants.BUTTON_ACTION);
            futureMilestoneBtn.setStyleName(MobileUIConstants.BUTTON_OPTION);
        }
    }

    @Override
    protected Component buildToolbar() {
        Toolbar toolbar = new Toolbar();
        closedMilestoneBtn = new Button(AppContext.getMessage(MilestoneI18nEnum.WIDGET_CLOSED_PHASE_TITLE),
                clickEvent -> displayStatus(OptionI18nEnum.MilestoneStatus.Closed));
        closedMilestoneBtn.setIcon(FontAwesome.MINUS);
        toolbar.addComponent(closedMilestoneBtn);

        inProgressMilestoneBtn = new Button(AppContext.getMessage(MilestoneI18nEnum.WIDGET_INPROGRESS_PHASE_TITLE),
                clickEvent -> displayStatus(OptionI18nEnum.MilestoneStatus.InProgress));
        inProgressMilestoneBtn.setIcon(FontAwesome.SPINNER);

        futureMilestoneBtn = new Button(AppContext.getMessage(MilestoneI18nEnum.WIDGET_FUTURE_PHASE_TITLE),
                clickEvent -> displayStatus(OptionI18nEnum.MilestoneStatus.Future));
        futureMilestoneBtn.setIcon(FontAwesome.CLOCK_O);

        toolbar.addComponents(closedMilestoneBtn, inProgressMilestoneBtn, futureMilestoneBtn);
        return toolbar;
    }

    private static class MilestoneRowDisplayHandler implements AbstractPagedBeanList.RowDisplayHandler<SimpleMilestone> {

        @Override
        public Component generateRow(final SimpleMilestone milestone, int rowIndex) {
            MVerticalLayout milestoneInfoLayout = new MVerticalLayout().withSpacing(false).withFullWidth();

            A milestoneLink = new A(ProjectLinkBuilder.generateMilestonePreviewFullLink(CurrentProjectVariables
                    .getProjectId(), milestone.getId())).appendChild(new Span().appendText(milestone.getName()));
            MCssLayout milestoneWrap = new MCssLayout(new ELabel(milestoneLink.write(), ContentMode.HTML));
            milestoneInfoLayout.addComponent(new MHorizontalLayout(new ELabel(ProjectAssetsManager.getAsset
                    (ProjectTypeConstants.MILESTONE).getHtml(), ContentMode.HTML).withWidthUndefined(), milestoneWrap)
                    .expand(milestoneWrap).withFullWidth());

            CssLayout metaLayout = new CssLayout();
            milestoneInfoLayout.addComponent(metaLayout);

            ELabel milestoneDatesInfo = new ELabel().withWidthUndefined();
            milestoneDatesInfo.setValue(AppContext.getMessage(MilestoneI18nEnum.M_LIST_DATE_INFO,
                    AppContext.formatDate(milestone.getStartdate(), " N/A "),
                    AppContext.formatDate(milestone.getEnddate(), " N/A ")));
            milestoneDatesInfo.addStyleName(UIConstants.META_INFO);
            metaLayout.addComponent(milestoneDatesInfo);

            A assigneeLink = new A(ProjectLinkGenerator.generateProjectMemberFullLink(AppContext.getSiteUrl(),
                    CurrentProjectVariables.getProjectId(), milestone.getOwner()))
                    .appendText(StringUtils.trim(milestone.getOwnerFullName(), 30, true));
            Div assigneeDiv = new Div().appendChild(new Img("", StorageFactory.getAvatarPath(milestone
                    .getOwnerAvatarId(), 16))).appendChild(assigneeLink);

            ELabel assigneeLbl = new ELabel(assigneeDiv.write(), ContentMode.HTML).withStyleName(UIConstants.META_INFO)
                    .withWidthUndefined();
            metaLayout.addComponent(assigneeLbl);

            return milestoneInfoLayout;
        }
    }
}
