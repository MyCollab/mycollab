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
package com.esofthead.mycollab.mobile.ui;

import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;

public interface PreviewBeanForm<B> extends HasPreviewFormHandlers<B> {

	public B getBean();

	public void setBean(B bean);

	public void fireAssignForm(B bean);

	public void fireEditForm(B bean);

	public void showHistory();

	public void fireCancelForm(B bean);

	public void fireDeleteForm(B bean);

	public void fireCloneForm(B bean);

	public void fireExtraAction(String action, B bean);
}
