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
package com.esofthead.mycollab.mobile.module.crm.ui;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.module.crm.domain.SimpleNote;
import com.esofthead.mycollab.module.crm.domain.criteria.NoteSearchCriteria;
import com.esofthead.mycollab.module.crm.service.NoteService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractMobilePageView;
import com.esofthead.mycollab.vaadin.ui.BeanList;
import com.esofthead.mycollab.vaadin.ui.BeanList.RowDisplayHandler;
import com.esofthead.mycollab.vaadin.ui.UrlDetectableLabel;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class NotesList extends AbstractMobilePageView {
	private static final long serialVersionUID = -9063602627063299844L;

	private String type;
	private Integer typeid;
	private VerticalLayout noteListContainer;
	private BeanList<NoteService, NoteSearchCriteria, SimpleNote> noteList;

	private final NoteService noteService;

	public NotesList(final String title) {
		this(title, "", 0);
	}

	public NotesList(final String title, final String type, final Integer typeid) {
		super();
		this.setCaption(title);
		this.setWidth("100%");

		noteService = ApplicationContextUtil.getSpringBean(NoteService.class);
		this.type = type;
		this.typeid = typeid;

		initUI();
	}

	private void displayNotes() {
		noteListContainer.removeAllComponents();
		noteListContainer.addComponent(noteList);

		final NoteSearchCriteria searchCriteria = new NoteSearchCriteria();
		searchCriteria.setType(new StringSearchField(SearchField.AND, type));
		searchCriteria.setTypeid(new NumberSearchField(typeid));
		noteList.setSearchCriteria(searchCriteria);
	}

	private void initUI() {
		noteList = new BeanList<NoteService, NoteSearchCriteria, SimpleNote>(
				noteService, NoteRowDisplayHandler.class);
		noteList.setDisplayEmptyListText(false);
		noteList.setStyleName("noteList");

		noteListContainer = new VerticalLayout();
		this.setContent(noteListContainer);
		displayNotes();
	}

	public void showNotes(final String type, final int typeid) {
		this.type = type;
		this.typeid = typeid;
		displayNotes();
	}

	public static class NoteRowDisplayHandler implements
			RowDisplayHandler<SimpleNote> {
		private static final long serialVersionUID = 1L;

		@Override
		public Component generateRow(final SimpleNote note, final int rowIndex) {
			final HorizontalLayout layout = new HorizontalLayout();
			layout.setStyleName("message");
			layout.setSpacing(true);
			layout.setWidth("100%");
			layout.setMargin(true);
			layout.addComponent(UserAvatarControlFactory
					.createUserAvatarButtonLink(note.getCreatedUserAvatarId(),
							note.getCreateUserFullName()));

			final VerticalLayout rowLayout = new VerticalLayout();
			rowLayout.setStyleName("message-container");
			rowLayout.setSpacing(true);
			rowLayout.setWidth("100%");
			rowLayout.setDefaultComponentAlignment(Alignment.TOP_LEFT);

			final HorizontalLayout messageHeader = new HorizontalLayout();
			messageHeader.setStyleName("message-header");
			messageHeader.setWidth("100%");

			final Label username = new Label(note.getCreateUserFullName());
			username.setStyleName("user-name");
			username.setWidth("100%");
			messageHeader.addComponent(username);
			messageHeader.setExpandRatio(username, 1.0f);

			final Label timePostLbl = new Label(
					DateTimeUtils.getStringDateFromNow(note.getCreatedtime(),
							AppContext.getUserLocale()));
			timePostLbl.setSizeUndefined();
			timePostLbl.setStyleName("time-post");
			messageHeader.addComponent(timePostLbl);

			rowLayout.addComponent(messageHeader);

			final Label messageContent = new UrlDetectableLabel(note.getNote());
			messageContent.setStyleName("message-body");
			rowLayout.addComponent(messageContent);
			rowLayout.setExpandRatio(messageContent, 1.0f);

			layout.addComponent(rowLayout);
			layout.setExpandRatio(rowLayout, 1.0f);
			return layout;
		}
	}
}
