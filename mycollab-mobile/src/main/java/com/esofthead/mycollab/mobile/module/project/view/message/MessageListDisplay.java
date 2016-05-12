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
package com.esofthead.mycollab.mobile.module.project.view.message;

import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.mobile.ui.DefaultPagedBeanList;
import com.esofthead.mycollab.mobile.ui.MobileAttachmentUtils;
import com.esofthead.mycollab.mobile.ui.UIConstants;
import com.esofthead.mycollab.module.ecm.domain.Content;
import com.esofthead.mycollab.module.ecm.service.ResourceService;
import com.esofthead.mycollab.module.file.AttachmentUtils;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleMessage;
import com.esofthead.mycollab.module.project.domain.criteria.MessageSearchCriteria;
import com.esofthead.mycollab.module.project.service.MessageService;
import com.esofthead.mycollab.spring.AppContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.ELabel;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.hp.gagawa.java.elements.A;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.apache.commons.collections.CollectionUtils;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
public class MessageListDisplay extends DefaultPagedBeanList<MessageService, MessageSearchCriteria, SimpleMessage> {
    private static final long serialVersionUID = 7625380843753142287L;

    public MessageListDisplay() {
        super(AppContextUtil.getSpringBean(MessageService.class), new MessageRowDisplayHandler());
    }

    public static class MessageRowDisplayHandler implements RowDisplayHandler<SimpleMessage> {

        @Override
        public Component generateRow(final SimpleMessage message, int rowIndex) {
            HorizontalLayout mainLayout = new HorizontalLayout();
            mainLayout.setStyleName("message-block");
            Image userAvatarImg = UserAvatarControlFactory.createUserAvatarEmbeddedComponent(message.getPostedUserAvatarId(), 32);
            userAvatarImg.setStyleName("user-avatar");
            mainLayout.addComponent(userAvatarImg);

            CssLayout rightCol = new CssLayout();
            rightCol.setWidth("100%");

            MHorizontalLayout metadataRow = new MHorizontalLayout().withFullWidth();

            ELabel userNameLbl = new ELabel(message.getFullPostedUserName()).withStyleName(UIConstants.META_INFO);
            userNameLbl.addStyleName(UIConstants.TRUNCATE);
            CssLayout userNameLblWrap = new CssLayout(userNameLbl);

            ELabel messageTimePost = new ELabel(AppContext.formatPrettyTime(message.getPosteddate())).withStyleName
                    (UIConstants.META_INFO).withWidthUndefined();
            metadataRow.with(userNameLblWrap, messageTimePost).withAlign(messageTimePost, Alignment.TOP_RIGHT).expand(userNameLblWrap);

            rightCol.addComponent(metadataRow);

            MHorizontalLayout titleRow = new MHorizontalLayout().withWidth("100%").withStyleName("title-row");

            A messageLink = new A(ProjectLinkBuilder.generateMessagePreviewFullLink(CurrentProjectVariables
                    .getProjectId(), message.getId())).appendText(message.getTitle());
            ELabel messageTitle = new ELabel(messageLink.write(), ContentMode.HTML).withStyleName(UIConstants.LABEL_H3);
            messageTitle.addStyleName(UIConstants.TRUNCATE);
            CssLayout messageWrap = new CssLayout(messageTitle);

            if (message.getCommentsCount() > 0) {
                Label msgCommentCount = new Label(String.valueOf(message.getCommentsCount()));
                msgCommentCount.setStyleName("comment-count");
                msgCommentCount.setWidthUndefined();
                titleRow.addComponent(msgCommentCount);
                titleRow.addStyleName("has-comment");
                titleRow.with(messageWrap, msgCommentCount).expand(messageWrap);
            } else {
                titleRow.with(messageWrap);
            }
            rightCol.addComponent(titleRow);

            Label messageContent = new Label(StringUtils.trim(StringUtils.trimHtmlTags(message.getMessage()), 150, true));
            rightCol.addComponent(messageContent);

            ResourceService attachmentService = AppContextUtil.getSpringBean(ResourceService.class);
            List<Content> attachments = attachmentService.getContents(AttachmentUtils.getProjectEntityAttachmentPath(
                    AppContext.getAccountId(), message.getProjectid(),
                    ProjectTypeConstants.MESSAGE, "" + message.getId()));
            if (CollectionUtils.isNotEmpty(attachments)) {
                CssLayout attachmentPanel = new CssLayout();
                attachmentPanel.setStyleName("attachment-panel");
                attachmentPanel.setWidth("100%");

                for (Content attachment : attachments) {
                    attachmentPanel.addComponent(MobileAttachmentUtils.renderAttachmentRow(attachment));
                }
                rightCol.addComponent(attachmentPanel);
            }

            mainLayout.addComponent(rightCol);
            mainLayout.setExpandRatio(rightCol, 1.0f);
            mainLayout.setWidth("100%");
            return mainLayout;
        }
    }

}
