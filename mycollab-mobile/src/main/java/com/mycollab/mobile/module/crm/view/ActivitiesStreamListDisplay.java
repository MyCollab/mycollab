/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.mobile.module.crm.view;

import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Text;
import com.mycollab.common.ActivityStreamConstants;
import com.mycollab.common.domain.SimpleActivityStream;
import com.mycollab.common.domain.criteria.ActivityStreamSearchCriteria;
import com.mycollab.common.service.ActivityStreamService;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.html.DivLessFormatter;
import com.mycollab.mobile.ui.AbstractPagedBeanList;
import com.mycollab.mobile.ui.FormSectionBuilder;
import com.mycollab.module.crm.CrmLinkGenerator;
import com.mycollab.module.crm.i18n.CrmCommonI18nEnum;
import com.mycollab.module.crm.ui.CrmAssetsManager;
import com.mycollab.module.crm.ui.CrmLocalizationTypeMap;
import com.mycollab.module.file.service.AbstractStorageService;
import com.mycollab.module.user.AccountLinkGenerator;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.TooltipHelper;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.IBeanList;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.ui.registry.AuditLogRegistry;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 4.5.9
 */
class ActivitiesStreamListDisplay extends AbstractPagedBeanList<ActivityStreamSearchCriteria, SimpleActivityStream> {
    private ActivityStreamService activityStreamService;

    ActivitiesStreamListDisplay() {
        super(new ActivityStreamRowHandler(), 20);
        activityStreamService = AppContextUtil.getSpringBean(ActivityStreamService.class);
    }

    @Override
    protected int queryTotalCount() {
        return activityStreamService.getTotalCount(searchRequest.getSearchCriteria());
    }

    @Override
    protected List<SimpleActivityStream> queryCurrentData() {
        return (List<SimpleActivityStream>) activityStreamService.findPageableListByCriteria(searchRequest);
    }

    @Override
    protected void renderRows() {
        int i = 0;
        Date currentDate = new GregorianCalendar(2100, 1, 1).getTime();
        for (final SimpleActivityStream item : currentListData) {
            if (!DateUtils.isSameDay(item.getCreatedtime(), currentDate)) {
                listContainer.addComponent(FormSectionBuilder.build(UserUIContext.formatDate(item.getCreatedtime())));
                currentDate = item.getCreatedtime();
            }
            final Component row = getRowDisplayHandler().generateRow(this, item, i);
            if (row != null) {
                listContainer.addComponent(row);
            }
            i++;
        }
    }

    private static class ActivityStreamRowHandler implements IBeanList.RowDisplayHandler<SimpleActivityStream> {
        @Override
        public Component generateRow(IBeanList<SimpleActivityStream> host, SimpleActivityStream activityStream, int rowIndex) {
            CssLayout layout = new CssLayout();
            layout.addStyleName("activity-cell");
            StringBuilder content = new StringBuilder();
            AuditLogRegistry auditLogRegistry = AppContextUtil.getSpringBean(AuditLogRegistry.class);
            String itemType = CrmLocalizationTypeMap.getType(activityStream.getType());
            String assigneeValue = buildAssigneeValue(activityStream);
            String itemValue = buildItemValue(activityStream);

            if (ActivityStreamConstants.ACTION_CREATE.equals(activityStream.getAction())) {
                content.append(UserUIContext.getMessage(CrmCommonI18nEnum.WIDGET_ACTIVITY_CREATE_ACTION,
                        assigneeValue, itemType, itemValue));
            } else if (ActivityStreamConstants.ACTION_UPDATE.equals(activityStream.getAction())) {
                content.append(UserUIContext.getMessage(CrmCommonI18nEnum.WIDGET_ACTIVITY_UPDATE_ACTION,
                        assigneeValue, itemType, itemValue));
                if (activityStream.getAssoAuditLog() != null) {
                    content.append(auditLogRegistry.generatorDetailChangeOfActivity(activityStream));
                }
            } else if (ActivityStreamConstants.ACTION_COMMENT.equals(activityStream.getAction())) {
                content.append(UserUIContext.getMessage(CrmCommonI18nEnum.WIDGET_ACTIVITY_COMMENT_ACTION, assigneeValue, itemType, itemValue));
                if (activityStream.getAssoAuditLog() != null) {
                    content.append("<p><ul><li>\"").append(activityStream.getAssoAuditLog().getChangeset()).append("\"</li></ul></p>");
                }
            } else if (ActivityStreamConstants.ACTION_DELETE.equals(activityStream.getAction())) {
                content.append(UserUIContext.getMessage(CrmCommonI18nEnum.WIDGET_ACTIVITY_DELETE_ACTION,
                        assigneeValue, itemType, itemValue));
            }
            layout.addComponent(ELabel.html(content.toString()));
            return layout;
        }

        private String buildAssigneeValue(SimpleActivityStream activityStream) {
            DivLessFormatter div = new DivLessFormatter();
            Img userAvatar = new Img("", AppContextUtil.getSpringBean(AbstractStorageService.class)
                    .getAvatarPath(activityStream.getCreatedUserAvatarId(), 16))
                    .setCSSClass(UIConstants.CIRCLE_BOX);
            A userLink = new A().setId("tag" + TooltipHelper.TOOLTIP_ID).setHref(AccountLinkGenerator.generateUserLink(
                    activityStream.getCreateduser())).appendText(StringUtils.trim
                    (activityStream.getCreatedUserFullName(), 30, true));

            div.appendChild(userAvatar, DivLessFormatter.EMPTY_SPACE, userLink);

            return div.write();
        }

        private String buildItemValue(SimpleActivityStream activityStream) {
            DivLessFormatter div = new DivLessFormatter();
            Text itemImg = new Text(CrmAssetsManager.getAsset(activityStream.getType()).getHtml());
            A itemLink = new A().setId("tag" + TooltipHelper.TOOLTIP_ID).setHref(CrmLinkGenerator.generateCrmItemLink(
                    activityStream.getType(), Integer.parseInt(activityStream.getTypeid())));

            itemLink.appendText(activityStream.getNamefield());

            div.appendChild(itemImg, DivLessFormatter.EMPTY_SPACE, itemLink);
            return div.write();
        }
    }
}
