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

import com.esofthead.mycollab.mobile.module.project.ui.AbstractListViewComp;
import com.esofthead.mycollab.mobile.ui.AbstractPagedBeanList;
import com.esofthead.mycollab.module.project.domain.SimpleMessage;
import com.esofthead.mycollab.module.project.domain.criteria.MessageSearchCriteria;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.vaadin.ui.Component;

@ViewComponent
public class MessageListViewImpl extends
		AbstractListViewComp<MessageSearchCriteria, SimpleMessage> implements
		MessageListView {

	private static final long serialVersionUID = -5340014066758050437L;

	public MessageListViewImpl() {
		super();
		setCaption(AppContext.getMessage(ProjectCommonI18nEnum.VIEW_MESSAGE));
		setStyleName("message-list-view");

	}

	@Override
	protected AbstractPagedBeanList<MessageSearchCriteria, SimpleMessage> createBeanTable() {
		MessageListDisplay messageListDisplay = new MessageListDisplay();
		return messageListDisplay;
	}

	@Override
	protected Component createRightComponent() {
		// TODO Auto-generated method stub
		return null;
	}

}
