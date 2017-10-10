/**
 * mycollab-mobile - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.mobile.ui;

import com.mycollab.vaadin.event.HasPreviewFormHandlers;

/**
 * MyCollab Ltd
 *
 * @param <B>
 * @since 1.0
 */
public interface PreviewBeanForm<B> extends HasPreviewFormHandlers<B> {

    B getBean();

    void setBean(B bean);

    void fireAssignForm(B bean);

    void fireEditForm(B bean);

    void fireCancelForm(B bean);

    void fireDeleteForm(B bean);

    void fireCloneForm(B bean);

    void fireExtraAction(String action, B bean);
}
