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
package com.mycollab.mobile.module.project.view.bug;

import com.esofthead.vaadin.navigationbarquickmenu.NavigationBarQuickMenu;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Img;
import com.mycollab.common.i18n.DayI18nEnum;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.configuration.StorageFactory;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchField;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.html.DivLessFormatter;
import com.mycollab.mobile.module.project.events.BugEvent;
import com.mycollab.mobile.module.project.ui.AbstractListPageView;
import com.mycollab.mobile.ui.AbstractPagedBeanList;
import com.mycollab.mobile.ui.DefaultPagedBeanList;
import com.mycollab.mobile.ui.SearchInputField;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectLinkBuilder;
import com.mycollab.module.project.ProjectLinkGenerator;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.i18n.BugI18nEnum;
import com.mycollab.module.project.i18n.OptionI18nEnum;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.tracker.domain.SimpleBug;
import com.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.mycollab.module.tracker.service.BugService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.UIConstants;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import static com.mycollab.common.i18n.QueryI18nEnum.StringI18nEnum;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
@ViewComponent
public class BugListViewImpl extends AbstractListPageView<BugSearchCriteria, SimpleBug> implements BugListView {
    private static final long serialVersionUID = -7877935907665712184L;

    public BugListViewImpl() {
        this.setCaption(UserUIContext.getMessage(BugI18nEnum.LIST));
    }

    @Override
    protected AbstractPagedBeanList<BugSearchCriteria, SimpleBug> createBeanList() {
        return new DefaultPagedBeanList<>(AppContextUtil.getSpringBean(BugService.class), new BugRowDisplayHandler());
    }

    @Override
    protected SearchInputField<BugSearchCriteria> createSearchField() {
        return new SearchInputField<BugSearchCriteria>() {
            @Override
            protected BugSearchCriteria fillUpSearchCriteria(String value) {
                BugSearchCriteria searchCriteria = new BugSearchCriteria();
                searchCriteria.setProjectId(NumberSearchField.equal(CurrentProjectVariables.getProjectId()));
                searchCriteria.addExtraField(BugSearchCriteria.p_textDesc.buildSearchField(SearchField.AND,
                        StringI18nEnum.CONTAINS.name(), value));
                return searchCriteria;
            }
        };
    }

    @Override
    protected Component buildRightComponent() {
        NavigationBarQuickMenu menu = new NavigationBarQuickMenu();
        menu.setButtonCaption("...");
        MVerticalLayout content = new MVerticalLayout();
        content.with(new Button(UserUIContext.getMessage(BugI18nEnum.NEW),
                clickEvent -> EventBusFactory.getInstance().post(new BugEvent.GotoAdd(this, null))));
        menu.setContent(content);
        return menu;
    }

    private static class BugRowDisplayHandler implements AbstractPagedBeanList.RowDisplayHandler<SimpleBug> {

        @Override
        public Component generateRow(final SimpleBug bug, int rowIndex) {
            MVerticalLayout bugRowLayout = new MVerticalLayout().withFullWidth();

            A bugLink = new A(ProjectLinkBuilder.generateBugPreviewFullLink(bug.getBugkey(), bug.getProjectShortName
                    ())).appendText(String.format("[#%s] - %s", bug.getBugkey(), bug.getName()));

            CssLayout bugLbl = new CssLayout(new ELabel(bugLink.write(), ContentMode.HTML).withStyleName(UIConstants.TEXT_ELLIPSIS));
            bugRowLayout.with(new MHorizontalLayout(new ELabel(ProjectAssetsManager.getAsset(ProjectTypeConstants.BUG)
                    .getHtml(), ContentMode.HTML).withWidthUndefined(), bugLbl).expand(bugLbl).withFullWidth());

            CssLayout metaInfoLayout = new CssLayout();
            bugRowLayout.with(metaInfoLayout);

            ELabel lastUpdatedTimeLbl = new ELabel(UserUIContext.getMessage(DayI18nEnum.LAST_UPDATED_ON, UserUIContext
                    .formatPrettyTime((bug.getLastupdatedtime())))).withStyleName(UIConstants.META_INFO);
            metaInfoLayout.addComponent(lastUpdatedTimeLbl);

            A assigneeLink = new A(ProjectLinkGenerator.generateProjectMemberFullLink(MyCollabUI.getSiteUrl(),
                    CurrentProjectVariables.getProjectId(), bug.getAssignuser()));
            assigneeLink.appendText(StringUtils.trim(bug.getAssignuserFullName(), 30, true));
            Div assigneeDiv = new Div().appendText(UserUIContext.getMessage(GenericI18Enum.FORM_ASSIGNEE))
                    .appendChild(DivLessFormatter.EMPTY_SPACE(), new Img("", StorageFactory
                                    .getAvatarPath(bug.getAssignUserAvatarId(), 16)), DivLessFormatter.EMPTY_SPACE(),
                            assigneeLink);

            ELabel assigneeLbl = ELabel.html(assigneeDiv.write()).withStyleName(UIConstants.META_INFO)
                    .withWidthUndefined();
            metaInfoLayout.addComponent(assigneeLbl);

            ELabel statusLbl = ELabel.html(UserUIContext.getMessage(GenericI18Enum.FORM_STATUS) + ": " + UserUIContext.getMessage
                    (OptionI18nEnum.BugStatus.class, bug.getStatus())).withStyleName(UIConstants.META_INFO);
            metaInfoLayout.addComponent(statusLbl);

            return bugRowLayout;
        }
    }
}
