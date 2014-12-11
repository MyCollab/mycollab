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

import org.vaadin.maddon.layouts.MVerticalLayout;

import com.esofthead.mycollab.common.CommentType;
import com.esofthead.mycollab.common.domain.SimpleComment;
import com.esofthead.mycollab.common.domain.criteria.CommentSearchCriteria;
import com.esofthead.mycollab.common.service.CommentService;
import com.esofthead.mycollab.common.ui.components.CommentRowDisplayHandler;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.schedule.email.SendingRelayEmailNotificationAction;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.ui.BeanList;
import com.esofthead.mycollab.vaadin.ui.ReloadableComponent;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class CommentDisplay extends MVerticalLayout implements
		ReloadableComponent {
	private static final long serialVersionUID = 1L;

	private final BeanList<CommentService, CommentSearchCriteria, SimpleComment> commentList;
	private CommentType type;
	private String typeid;
	private Integer numComments;
	private ProjectCommentInput commentBox;

	public CommentDisplay(
			final CommentType type,
			final Integer extraTypeId,
			final boolean isDisplayCommentInput,
			final boolean isSendingRelayEmail,
			final Class<? extends SendingRelayEmailNotificationAction> emailHandler) {
		withStyleName("comment-display").withSpacing(true).withMargin(false);
		this.type = type;
		if (isDisplayCommentInput) {
			commentBox = new ProjectCommentInput(this, type, extraTypeId,
					false, isSendingRelayEmail, emailHandler);
			this.addComponent(commentBox);
		}

		commentList = new BeanList<CommentService, CommentSearchCriteria, SimpleComment>(
				ApplicationContextUtil.getSpringBean(CommentService.class),
				CommentRowDisplayHandler.class);
		commentList.setDisplayEmptyListText(false);
		this.addComponent(commentList);

		displayCommentList();
	}

	@Override
	public void cancel() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	private void displayCommentList() {
		if (type == null || typeid == null) {
			return;
		}

		final CommentSearchCriteria searchCriteria = new CommentSearchCriteria();
		searchCriteria.setType(new StringSearchField(type.toString()));
		searchCriteria.setTypeid(new StringSearchField(typeid));
		numComments = commentList.setSearchCriteria(searchCriteria);
	}

	public int getNumComments() {
		return numComments;
	}

	public void loadComments(final String typeid) {
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
}