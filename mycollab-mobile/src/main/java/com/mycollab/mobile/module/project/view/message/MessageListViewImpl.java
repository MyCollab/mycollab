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
package com.mycollab.mobile.module.project.view.message;

import com.hp.gagawa.java.elements.A;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.mobile.module.project.events.MessageEvent;
import com.mycollab.mobile.module.project.ui.AbstractListPageView;
import com.mycollab.mobile.ui.AbstractPagedBeanList;
import com.mycollab.mobile.ui.DefaultPagedBeanList;
import com.mycollab.mobile.ui.MobileAttachmentUtils;
import com.mycollab.mobile.ui.SearchInputField;
import com.mycollab.module.ecm.domain.Content;
import com.mycollab.module.ecm.service.ResourceService;
import com.mycollab.module.file.AttachmentUtils;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectLinkBuilder;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.SimpleMessage;
import com.mycollab.module.project.domain.criteria.MessageSearchCriteria;
import com.mycollab.module.project.i18n.MessageI18nEnum;
import com.mycollab.module.project.service.MessageService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.IBeanList;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import org.apache.commons.collections.CollectionUtils;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 4.0.0
 */
@ViewComponent
public class MessageListViewImpl extends AbstractListPageView<MessageSearchCriteria, SimpleMessage> implements MessageListView {
    private static final long serialVersionUID = -5340014066758050437L;

    public MessageListViewImpl() {
        super();
        setCaption(UserUIContext.getMessage(MessageI18nEnum.LIST));
        setStyleName("message-list-view");
    }

    @Override
    protected AbstractPagedBeanList<MessageSearchCriteria, SimpleMessage> createBeanList() {
        return new DefaultPagedBeanList<>(AppContextUtil.getSpringBean(MessageService.class), new MessageRowDisplayHandler());
    }

    @Override
    protected SearchInputField<MessageSearchCriteria> createSearchField() {
        return new SearchInputField<MessageSearchCriteria>() {
            @Override
            protected MessageSearchCriteria fillUpSearchCriteria(String value) {
                MessageSearchCriteria searchCriteria = new MessageSearchCriteria();
                searchCriteria.setProjectids(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
                searchCriteria.setTitle(StringSearchField.and(value));
                return searchCriteria;
            }
        };
    }

    @Override
    protected Component buildRightComponent() {
        return new MButton("", clickEvent -> EventBusFactory.getInstance().post(new MessageEvent.GotoAdd(this, null)))
                .withIcon(FontAwesome.PLUS).withStyleName(UIConstants.CIRCLE_BOX)
                .withVisible(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.MESSAGES));
    }

    private static class MessageRowDisplayHandler implements IBeanList.RowDisplayHandler<SimpleMessage> {

        @Override
        public Component generateRow(IBeanList<SimpleMessage> host, final SimpleMessage message, int rowIndex) {
            MHorizontalLayout mainLayout = new MHorizontalLayout().withStyleName("message-block").withFullWidth();
            Image userAvatarImg = UserAvatarControlFactory.createUserAvatarEmbeddedComponent(message.getPostedUserAvatarId(), 32);
            userAvatarImg.addStyleName(UIConstants.CIRCLE_BOX);
            mainLayout.addComponent(userAvatarImg);

            CssLayout rightCol = new CssLayout();
            rightCol.setWidth("100%");

            MHorizontalLayout metadataRow = new MHorizontalLayout().withFullWidth();

            ELabel userNameLbl = new ELabel(message.getFullPostedUserName()).withStyleName(UIConstants.META_INFO,
                    UIConstants.TEXT_ELLIPSIS);

            ELabel messageTimePost = new ELabel(UserUIContext.formatPrettyTime(message.getPosteddate())).withStyleName
                    (UIConstants.META_INFO).withWidthUndefined();
            metadataRow.with(userNameLbl, messageTimePost).withAlign(messageTimePost, Alignment.TOP_RIGHT).expand(userNameLbl);
            rightCol.addComponent(metadataRow);

            MHorizontalLayout titleRow = new MHorizontalLayout().withFullWidth().withStyleName("title-row");

            A messageLink = new A(ProjectLinkBuilder.generateMessagePreviewFullLink(CurrentProjectVariables
                    .getProjectId(), message.getId())).appendText(message.getTitle());
            ELabel messageTitle = ELabel.h3(messageLink.write());
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
                    MyCollabUI.getAccountId(), message.getProjectid(), ProjectTypeConstants.MESSAGE, "" + message.getId()));
            if (CollectionUtils.isNotEmpty(attachments)) {
                CssLayout attachmentPanel = new CssLayout();
                attachmentPanel.setStyleName("attachment-panel");
                attachmentPanel.setWidth("100%");

                for (Content attachment : attachments) {
                    attachmentPanel.addComponent(MobileAttachmentUtils.renderAttachmentRow(attachment));
                }
                rightCol.addComponent(attachmentPanel);
            }

            mainLayout.with(rightCol).expand(rightCol);
            return mainLayout;
        }
    }
}
