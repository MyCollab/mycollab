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
package com.esofthead.mycollab.mobile.module.project.view.bug;

import com.esofthead.mycollab.common.i18n.DayI18nEnum;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.configuration.StorageFactory;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.db.query.StringParam;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.html.DivLessFormatter;
import com.esofthead.mycollab.mobile.module.project.events.BugEvent;
import com.esofthead.mycollab.mobile.module.project.ui.AbstractListPageView;
import com.esofthead.mycollab.mobile.ui.AbstractPagedBeanList;
import com.esofthead.mycollab.mobile.ui.DefaultPagedBeanList;
import com.esofthead.mycollab.mobile.ui.SearchInputField;
import com.esofthead.mycollab.mobile.ui.UIConstants;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectLinkGenerator;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.ELabel;
import com.esofthead.vaadin.navigationbarquickmenu.NavigationBarQuickMenu;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Img;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
@ViewComponent
public class BugListViewImpl extends AbstractListPageView<BugSearchCriteria, SimpleBug> implements BugListView {
    private static final long serialVersionUID = -7877935907665712184L;

    public BugListViewImpl() {
        this.setCaption(AppContext.getMessage(BugI18nEnum.LIST));
    }

    @Override
    protected AbstractPagedBeanList<BugSearchCriteria, SimpleBug> createBeanList() {
        return new DefaultPagedBeanList<>(ApplicationContextUtil.getSpringBean(BugService.class), new BugRowDisplayHandler());
    }

    @Override
    protected SearchInputField<BugSearchCriteria> createSearchField() {
        return new SearchInputField<BugSearchCriteria>() {
            @Override
            protected BugSearchCriteria fillUpSearchCriteria(String value) {
                BugSearchCriteria searchCriteria = new BugSearchCriteria();
                searchCriteria.setProjectId(NumberSearchField.and(CurrentProjectVariables.getProjectId()));
                searchCriteria.addExtraField(BugSearchCriteria.p_textDesc.buildSearchField(SearchField.AND,
                        StringParam.CONTAINS, value));
                return searchCriteria;
            }
        };
    }

    @Override
    protected Component buildRightComponent() {
        NavigationBarQuickMenu menu = new NavigationBarQuickMenu();
        menu.setButtonCaption("...");
        MVerticalLayout content = new MVerticalLayout();
        content.with(new Button(AppContext.getMessage(BugI18nEnum.NEW), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                EventBusFactory.getInstance().post(new BugEvent.GotoAdd(this, null));
            }
        }));
        menu.setContent(content);
        return menu;
    }

    private static class BugRowDisplayHandler implements AbstractPagedBeanList.RowDisplayHandler<SimpleBug> {

        @Override
        public Component generateRow(final SimpleBug bug, int rowIndex) {
            MVerticalLayout bugRowLayout = new MVerticalLayout().withWidth("100%");

            A bugLink = new A(ProjectLinkBuilder.generateBugPreviewFullLink(bug.getBugkey(), bug.getProjectShortName
                    ())).appendText(String.format("[#%s] - %s", bug.getBugkey(), bug.getSummary()));

            CssLayout bugLbl = new CssLayout(new ELabel(bugLink.write(), ContentMode.HTML).withStyleName(UIConstants.TRUNCATE));
            bugRowLayout.with(new MHorizontalLayout(new ELabel(ProjectAssetsManager.getAsset(ProjectTypeConstants.BUG)
                    .getHtml(), ContentMode.HTML).withWidthUndefined(), bugLbl).expand(bugLbl).withFullWidth());

            CssLayout metaInfoLayout = new CssLayout();
            bugRowLayout.with(metaInfoLayout);

            ELabel lastUpdatedTimeLbl = new ELabel(AppContext.getMessage(DayI18nEnum.LAST_UPDATED_ON, AppContext
                    .formatPrettyTime((bug.getLastupdatedtime())))).withStyleName(UIConstants.META_INFO);
            metaInfoLayout.addComponent(lastUpdatedTimeLbl);

            A assigneeLink = new A(ProjectLinkGenerator.generateProjectMemberFullLink(AppContext.getSiteUrl(),
                    CurrentProjectVariables.getProjectId(), bug.getAssignuser()));
            assigneeLink.appendText(StringUtils.trim(bug.getAssignuserFullName(), 30, true));
            Div assigneeDiv = new Div().appendText(AppContext.getMessage(GenericI18Enum.FORM_ASSIGNEE))
                    .appendChild(DivLessFormatter.EMPTY_SPACE(), new Img("", StorageFactory.getInstance()
                            .getAvatarPath(bug.getAssignUserAvatarId(), 16)), DivLessFormatter.EMPTY_SPACE(),
                            assigneeLink);

            ELabel assigneeLbl = new ELabel(assigneeDiv.write(), ContentMode.HTML).withStyleName(UIConstants.META_INFO)
                    .withWidthUndefined();
            metaInfoLayout.addComponent(assigneeLbl);

            ELabel statusLbl = new ELabel(AppContext.getMessage(GenericI18Enum.FORM_STATUS) + ": " + AppContext.getMessage
                    (OptionI18nEnum.BugStatus.class, bug.getStatus()), ContentMode.HTML).withStyleName(UIConstants.META_INFO);
            metaInfoLayout.addComponent(statusLbl);

            return bugRowLayout;
        }
    }
}
