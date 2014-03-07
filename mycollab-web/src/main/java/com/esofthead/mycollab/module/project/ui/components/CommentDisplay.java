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
package com.esofthead.mycollab.module.project.ui.components;

import com.esofthead.mycollab.common.CommentType;
import com.esofthead.mycollab.common.domain.SimpleComment;
import com.esofthead.mycollab.common.domain.criteria.CommentSearchCriteria;
import com.esofthead.mycollab.common.service.CommentService;
import com.esofthead.mycollab.common.ui.components.CommentRowDisplayHandler;
import com.esofthead.mycollab.common.ui.components.ReloadableComponent;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.schedule.email.SendingRelayEmailNotificationAction;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.BeanList;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class CommentDisplay extends VerticalLayout implements
ReloadableComponent {
	private static final long serialVersionUID = 1L;

	private final BeanList<CommentService, CommentSearchCriteria, SimpleComment> commentList;
	private CommentType type;
	private Integer typeid;
	private Integer numComments;
	private ProjectCommentInput commentBox;

	public CommentDisplay(
			final CommentType type,
			final Integer extraTypeId,
			final boolean isDisplayCommentInput,
			final boolean isSendingRelayEmail,
			final Class<? extends SendingRelayEmailNotificationAction> emailHandler) {
		setSpacing(true);
		this.type = type;
		this.setStyleName("comment-display");

		commentList = new BeanList<CommentService, CommentSearchCriteria, SimpleComment>(
				ApplicationContextUtil.getSpringBean(CommentService.class),
				CommentRowDisplayHandler.class);
		commentList.setDisplayEmptyListText(false);
		this.addComponent(commentList);

		if (isDisplayCommentInput) {
			commentBox = new ProjectCommentInput(this, type, extraTypeId, false, isSendingRelayEmail, emailHandler);
			this.addComponent(createCommentBox());
		}

		displayCommentList();
	}

	@Override
	public void cancel() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	private void displayCommentList() {
		if (type == null || typeid == null || typeid == 0) {
			return;
		}

		final CommentSearchCriteria searchCriteria = new CommentSearchCriteria();
		searchCriteria.setType(new StringSearchField(type.toString()));
		searchCriteria.setTypeid(new NumberSearchField(typeid));
		numComments = commentList.setSearchCriteria(searchCriteria);
	}

	public int getNumComments() {
		return numComments;
	}

	public void loadComments(final int typeid) {
		this.typeid = typeid;
		if (commentBox != null) {
			commentBox.setTypeAndId(typeid);
		}
		displayCommentList();
	}

	@Override
	public void reload() {
		displayCommentList();
	}

	protected Component createCommentBox() {

		HorizontalLayout commentWrap = new HorizontalLayout();
		commentWrap.setSpacing(true);
		commentWrap.addStyleName("message");
		commentWrap.setWidth("100%");

		SimpleUser currentUser = AppContext.getSession();
		VerticalLayout userBlock = new VerticalLayout();
		userBlock.setDefaultComponentAlignment(Alignment.TOP_CENTER);
		userBlock.setWidth("80px");
		userBlock.setSpacing(true);
		userBlock.addComponent(UserAvatarControlFactory
				.createUserAvatarButtonLink(
						currentUser.getAvatarid(),
						currentUser.getDisplayName()));
		Label userName = new Label(currentUser.getDisplayName());
		userName.setStyleName("user-name");
		userBlock.addComponent(userName);

		commentWrap.addComponent(userBlock);
		CssLayout textAreaWrap = new CssLayout();
		textAreaWrap.setStyleName("message-container");
		textAreaWrap.setWidth("100%");
		textAreaWrap.addComponent(commentBox);
		commentBox.setWidth("100%");
		commentWrap.addComponent(textAreaWrap);
		commentWrap.setExpandRatio(textAreaWrap, 1.0f);

		return commentWrap;
	}
}