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
package com.esofthead.mycollab.module.crm.view.activity;

import com.esofthead.mycollab.module.crm.domain.SimpleMeeting;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;

@ViewComponent
public class MeetingReadViewImpl extends AbstractPageView implements
		MeetingReadView {

	private static final long serialVersionUID = 1L;
	private MeetingReadComp previewForm;

	public MeetingReadViewImpl() {
		super();
		previewForm = new MeetingReadComp();
		this.addComponent(previewForm);
	}

	@Override
	public void previewItem(SimpleMeeting meeting) {
		previewForm.previewItem(meeting);
	}

	@Override
	public HasPreviewFormHandlers<SimpleMeeting> getPreviewFormHandlers() {
		return previewForm.getPreviewForm();
	}

	@Override
	public SimpleMeeting getItem() {
		return previewForm.getBeanItem();
	}
}
