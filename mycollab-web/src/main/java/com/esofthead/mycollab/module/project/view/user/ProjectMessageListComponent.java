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

import com.esofthead.mycollab.common.GenericLinkUtils;
import com.esofthead.mycollab.configuration.StorageManager;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.html.DivLessFormatter;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleMessage;
import com.esofthead.mycollab.module.project.domain.criteria.MessageSearchCriteria;
import com.esofthead.mycollab.module.project.i18n.MessageI18nEnum;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.project.service.MessageService;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.utils.TooltipHelper;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.DefaultBeanPagedList;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Text;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;

import java.util.UUID;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectMessageListComponent extends MVerticalLayout {
    private static final long serialVersionUID = 1L;

    private Label titleLbl;
    private final DefaultBeanPagedList<MessageService, MessageSearchCriteria, SimpleMessage> messageList;

    public ProjectMessageListComponent() {
        withSpacing(false).withMargin(false);

        titleLbl = new Label(AppContext.getMessage(MessageI18nEnum.WIDGET_LASTEST_NEWS));
        MHorizontalLayout header = new MHorizontalLayout().withMargin(new MarginInfo(false, true,
                false, true)).withHeight("34px").withWidth("100%").with(titleLbl).withAlign(titleLbl, Alignment
                .MIDDLE_LEFT);
        header.addStyleName("panel-header");

        messageList = new DefaultBeanPagedList<>(
                ApplicationContextUtil.getSpringBean(MessageService.class),
                new MessageRowDisplayHandler(), 5);
        this.with(header, messageList);
    }

    public void showLatestMessages() {
        final MessageSearchCriteria searchCriteria = new MessageSearchCriteria();
        searchCriteria.setProjectids(new SetSearchField<>(
                CurrentProjectVariables.getProjectId()));

        int totalCountItems = messageList.setSearchCriteria(searchCriteria);
        titleLbl.setValue(AppContext.getMessage(MessageI18nEnum.WIDGET_LASTEST_NEWS) + " (" + totalCountItems + ")");
    }

    public static class MessageRowDisplayHandler implements DefaultBeanPagedList.RowDisplayHandler<SimpleMessage> {

        @Override
        public Component generateRow(final SimpleMessage message, final int rowIndex) {
            final CssLayout layout = new CssLayout();
            layout.setWidth("100%");
            layout.setStyleName("list-row");

            if ((rowIndex + 1) % 2 != 0) {
                layout.addStyleName("odd");
            }

            final CssLayout header = new CssLayout();

            final String content = AppContext.getMessage(
                    ProjectCommonI18nEnum.FEED_PROJECT_MESSAGE_TITLE,
                    buildAssigneeValue(message), buildMessage(message));
            final Label actionLbl = new Label(content, ContentMode.HTML);

            header.addComponent(actionLbl);

            layout.addComponent(header);

            final CssLayout body = new CssLayout();
            body.setStyleName("activity-date");
            final Label dateLbl = new Label(AppContext.getMessage(
                    MessageI18nEnum.OPT_FROM_TIME_ENTRY, AppContext
                            .formatPrettyTime(message.getPosteddate())));
            body.addComponent(dateLbl);

            layout.addComponent(body);
            return layout;
        }

        private String buildAssigneeValue(SimpleMessage message) {
            String uid = UUID.randomUUID().toString();
            DivLessFormatter div = new DivLessFormatter();
            Img avatar = new Img("", StorageManager.getAvatarLink(
                    message.getPostedUserAvatarId(), 16));
            A assigneeLink = new A();
            assigneeLink.setId("tag" + uid);
            assigneeLink.setHref(ProjectLinkBuilder
                    .generateProjectMemberFullLink(message.getProjectid(),
                            message.getPosteduser()));
            assigneeLink.setAttribute("onmouseover", TooltipHelper.buildUserHtmlTooltip(uid, message.getPosteduser()));
            assigneeLink.appendText(message.getFullPostedUserName());
            div.appendChild(avatar, DivLessFormatter.EMPTY_SPACE(), assigneeLink, TooltipHelper.buildDivTooltipEnable(uid));
            return div.write();
        }

        private String buildMessage(SimpleMessage message) {
            String uid = UUID.randomUUID().toString();
            DivLessFormatter div = new DivLessFormatter();
            Text messageIcon = new Text(ProjectAssetsManager.getAsset(ProjectTypeConstants.MESSAGE).getHtml());
            A msgLink = new A();
            msgLink.setId("tag" + uid);
            msgLink.setHref(ProjectLinkBuilder.generateMessagePreviewFullLink(
                    message.getProjectid(), message.getId(),
                    GenericLinkUtils.URL_PREFIX_PARAM));
            String arg17 = "'" + uid + "'";
            String arg18 = "'" + ProjectTypeConstants.MESSAGE + "'";
            String arg19 = "'" + message.getId() + "'";
            String arg20 = "'" + AppContext.getSiteUrl() + "tooltip/'";
            String arg21 = "'" + AppContext.getAccountId() + "'";
            String arg22 = "'" + AppContext.getSiteUrl() + "'";
            String arg23 = AppContext.getSession().getTimezone();
            String arg24 = "'" + AppContext.getUserLocale().toString() + "'";

            String mouseOverFunc = String.format(
                    "return overIt(%s,%s,%s,%s,%s,%s,%s,%s);", arg17, arg18, arg19,
                    arg20, arg21, arg22, arg23, arg24);
            msgLink.setAttribute("onmouseover", mouseOverFunc);
            msgLink.appendText(message.getTitle());
            div.appendChild(messageIcon, DivLessFormatter.EMPTY_SPACE(), msgLink, DivLessFormatter.EMPTY_SPACE(), TooltipHelper
                    .buildDivTooltipEnable(uid));
            return div.write();
        }
    }
}