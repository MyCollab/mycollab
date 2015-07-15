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
package com.esofthead.mycollab.mobile.module.project.view;

import com.esofthead.mycollab.common.ActivityStreamConstants;
import com.esofthead.mycollab.common.domain.criteria.ActivityStreamSearchCriteria;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.project.events.ProjectEvent;
import com.esofthead.mycollab.mobile.module.project.view.parameters.ProjectMemberScreenData;
import com.esofthead.mycollab.mobile.module.project.view.parameters.ProjectScreenData;
import com.esofthead.mycollab.mobile.ui.AbstractPagedBeanList;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.ProjectActivityStream;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectActivityStreamService;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.PageActionChain;
import com.hp.gagawa.java.elements.A;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.apache.commons.lang3.time.DateUtils;
import org.vaadin.maddon.layouts.MHorizontalLayout;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
public class ProjectActivityStreamListDisplay extends AbstractPagedBeanList<ActivityStreamSearchCriteria, ProjectActivityStream> {
    private static final long serialVersionUID = 9189667863722393067L;

    protected final ProjectActivityStreamService projectActivityStreamService;

    public ProjectActivityStreamListDisplay() {
        super(new ActivityStreamRowHandler(), 20);
        this.projectActivityStreamService = ApplicationContextUtil.getSpringBean(ProjectActivityStreamService.class);
    }

    @Override
    protected int queryTotalCount() {
        return projectActivityStreamService.getTotalActivityStream(searchRequest.getSearchCriteria());
    }

    @Override
    protected List<ProjectActivityStream> queryCurrentData() {
        return projectActivityStreamService.getProjectActivityStreams(searchRequest);
    }

    @Override
    protected void renderRows() {
        int i = 0;
        Date currentDate = new GregorianCalendar(2100, 1, 1).getTime();
        for (final ProjectActivityStream item : currentListData) {
            if (!DateUtils.isSameDay(item.getCreatedtime(), currentDate)) {
                Label dateLbl = new Label(AppContext.formatDate(item.getCreatedtime()));
                dateLbl.setStyleName("activity-date");
                listContainer.addComponent(dateLbl);
                currentDate = item.getCreatedtime();
            }
            final Component row = getRowDisplayHandler().generateRow(item, i);
            listContainer.addComponent(row);
            i++;
        }
    }

    private static class ActivityStreamRowHandler implements RowDisplayHandler<ProjectActivityStream> {

        @Override
        public Component generateRow(final ProjectActivityStream obj, int rowIndex) {
            MHorizontalLayout layout = new MHorizontalLayout().withWidth("100%").withStyleName("list-item");
            layout.addStyleName("activity-row");

            Label typeIcon = new Label("<span aria-hidden=\"true\" data-icon=\""
                    + ProjectAssetsManager.toHexString(obj.getType()) + "\"></span>");
            typeIcon.setWidthUndefined();
            typeIcon.setContentMode(ContentMode.HTML);
            typeIcon.setStyleName("activity-type");

            layout.addComponent(typeIcon);

            VerticalLayout rightCol = new VerticalLayout();
            rightCol.setWidth("100%");
            Label streamItem = new Label(generateItemLink(obj));
            streamItem.setStyleName("activity-item");
            streamItem.setContentMode(ContentMode.HTML);
            rightCol.addComponent(streamItem);

            CssLayout detailRow = new CssLayout();
            detailRow.setWidthUndefined();
            detailRow.setStyleName("activity-detail-row");

            Label streamDetail = new Label();
            streamDetail.setWidthUndefined();
            streamDetail.setStyleName("activity-detail");
            if (ActivityStreamConstants.ACTION_CREATE.equals(obj.getAction())) {
                streamDetail
                        .setValue(AppContext
                                .getMessage(ProjectCommonI18nEnum.M_FEED_USER_ACTIVITY_CREATE_ACTION_TITLE));
            } else if (ActivityStreamConstants.ACTION_UPDATE.equals(obj
                    .getAction())) {
                streamDetail
                        .setValue(AppContext
                                .getMessage(ProjectCommonI18nEnum.M_FEED_USER_ACTIVITY_UPDATE_ACTION_TITLE));
            }
            detailRow.addComponent(streamDetail);
            Button activityUser = new Button(obj.getCreatedUserFullName(), new Button.ClickListener() {
                private static final long serialVersionUID = 719162256058709352L;

                @Override
                public void buttonClick(Button.ClickEvent event) {
                    PageActionChain chain = new PageActionChain(new ProjectScreenData.Goto(obj.getProjectId()),
                            new ProjectMemberScreenData.Read(obj.getCreateduser()));
                    EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this, chain));
                }
            });
            activityUser.setStyleName("link");
            detailRow.addComponent(activityUser);
            rightCol.addComponent(detailRow);

            layout.addComponent(rightCol);
            layout.setExpandRatio(rightCol, 1.0f);

            return layout;
        }

    }

    private static String generateItemLink(ProjectActivityStream stream) {
        A itemLink = new A();
        if (ProjectTypeConstants.TASK.equals(stream.getType())
                || ProjectTypeConstants.BUG.equals(stream.getType())) {
            itemLink.setHref(ProjectLinkBuilder.generateProjectItemLink(
                    stream.getProjectShortName(), stream.getExtratypeid(),
                    stream.getType(), stream.getItemKey() + ""));
        } else {
            itemLink.setHref(ProjectLinkBuilder.generateProjectItemLink(
                    stream.getProjectShortName(), stream.getExtratypeid(),
                    stream.getType(), stream.getTypeid()));
        }
        itemLink.appendText(stream.getNamefield());
        return itemLink.write();
    }

}
