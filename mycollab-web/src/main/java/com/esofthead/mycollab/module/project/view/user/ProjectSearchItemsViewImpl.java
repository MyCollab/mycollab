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
package com.esofthead.mycollab.module.project.view.user;

import com.esofthead.mycollab.configuration.StorageFactory;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.html.DivLessFormatter;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.domain.ProjectGenericItem;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectGenericItemSearchCriteria;
import com.esofthead.mycollab.module.project.service.ProjectGenericItemService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.utils.TooltipHelper;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanPagedList;
import com.esofthead.mycollab.vaadin.ui.DefaultBeanPagedList;
import com.esofthead.mycollab.vaadin.ui.SafeHtmlLabel;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Text;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.UUID;

/**
 * @author MyCollab Ltd.
 * @since 5.0.3
 */
@ViewComponent
public class ProjectSearchItemsViewImpl extends AbstractPageView implements ProjectSearchItemsView {
    private static final String headerTitle = FontAwesome.SEARCH.getHtml() + " Search for '%s' (Found: %d)";

    public ProjectSearchItemsViewImpl() {
        this.withMargin(true).withStyleName("searchitems-layout");
    }

    @Override
    public void displayResults(String value) {
        this.removeAllComponents();

        MVerticalLayout layout = new MVerticalLayout().withWidth("100%");
        this.addComponent(layout);

        Label headerLbl = new Label("", ContentMode.HTML);
        headerLbl.addStyleName(ValoTheme.LABEL_H2);
        headerLbl.addStyleName(ValoTheme.LABEL_NO_MARGIN);

        DefaultBeanPagedList<ProjectGenericItemService, ProjectGenericItemSearchCriteria, ProjectGenericItem>
                searchItemsTable = new DefaultBeanPagedList<>(ApplicationContextUtil.getSpringBean(ProjectGenericItemService.class), new
                ItemRowDisplayHandler());
        searchItemsTable.setControlStyle("borderlessControl");

        layout.with(headerLbl, searchItemsTable);
        ProjectGenericItemSearchCriteria criteria = new ProjectGenericItemSearchCriteria();
        criteria.setPrjKeys(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
        criteria.setTxtValue(new StringSearchField(value));
        int foundNum = searchItemsTable.setSearchCriteria(criteria);
        headerLbl.setValue(String.format(headerTitle, value, foundNum));
    }

    private static class ItemRowDisplayHandler implements AbstractBeanPagedList.RowDisplayHandler<ProjectGenericItem> {
        @Override
        public Component generateRow(AbstractBeanPagedList host, ProjectGenericItem item, int rowIndex) {
            MVerticalLayout layout = new MVerticalLayout().withMargin(new MarginInfo(true, true, false, false))
                    .withWidth("100%");
            Label link = new Label(ProjectLinkBuilder.generateProjectItemHtmlLinkAndTooltip(item.getProjectShortName(), item
                    .getProjectId(), item.getSummary(), item.getType(), item.getTypeId()), ContentMode.HTML);
            link.setStyleName(ValoTheme.LABEL_H3);

            String desc = (StringUtils.isBlank(item.getDescription())) ? "&lt;&lt;No description&gt;&gt;" : item
                    .getDescription();
            SafeHtmlLabel descLbl = new SafeHtmlLabel(desc);

            Div div = new Div().setStyle("width:100%").setCSSClass("footer");
            Text createdByTxt = new Text("Created by: ");
            Div lastUpdatedOn = new Div().appendChild(new Text("Modified: " + AppContext.formatPrettyTime(item.getLastUpdatedTime
                    ()))).setTitle(AppContext.formatDateTime(item.getLastUpdatedTime())).setStyle("float:right;" +
                    "margin-right:5px");

            if (StringUtils.isBlank(item.getCreatedUser())) {
                div.appendChild(createdByTxt, DivLessFormatter.EMPTY_SPACE(), new Text("None"), lastUpdatedOn);
            } else {
                String uid = UUID.randomUUID().toString();
                Img userAvatar = new Img("", StorageFactory.getInstance().getAvatarPath(item.getCreatedUserAvatarId(), 16));
                A userLink = new A().setId("tag" + uid).setHref(ProjectLinkBuilder.generateProjectMemberFullLink(item.getProjectId(), item
                        .getCreatedUser())).appendText(item.getCreatedUserDisplayName());
                userLink.setAttribute("onmouseover", TooltipHelper.userHoverJsFunction(uid, item.getCreatedUser()));
                userLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction(uid));

                div.appendChild(createdByTxt, DivLessFormatter.EMPTY_SPACE(), userAvatar, DivLessFormatter.EMPTY_SPACE(),
                        userLink, TooltipHelper.buildDivTooltipEnable(uid),
                        lastUpdatedOn);
            }

            Label footer = new Label(div.write(), ContentMode.HTML);
            footer.setWidth("100%");
            layout.with(link, descLbl, footer);
            layout.addStyleName("project-item-search-box");
            return layout;
        }
    }
}
