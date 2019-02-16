/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.web.ui;

import com.mycollab.vaadin.event.HasPreviewFormHandlers;
import com.mycollab.vaadin.event.PreviewFormHandler;
import com.mycollab.vaadin.ui.GenericBeanForm;

import java.util.HashSet;
import java.util.Set;

/**
 * @param <B>
 * @author MyCollab Ltd
 * @since 1.0
 */
public class AdvancedPreviewBeanForm<B> extends GenericBeanForm<B> implements HasPreviewFormHandlers<B> {
    private static final long serialVersionUID = 1L;

    private Set<PreviewFormHandler<B>> handlers = new HashSet<>();

    @Override
    public void addFormHandler(PreviewFormHandler<B> handler) {
        handlers.add(handler);
    }

    public void fireAssignForm(B bean) {
        handlers.forEach(hander -> hander.onAssign(bean));
    }

    public void fireEditForm(B bean) {
        handlers.forEach(hander -> hander.onEdit(bean));
    }

    public void fireAddForm(B bean) {
        handlers.forEach(hander -> hander.onAdd(bean));
    }

    public void fireCancelForm(B bean) {
        handlers.forEach(PreviewFormHandler::onCancel);
    }

    public void fireDeleteForm(B bean) {
        handlers.forEach(hander -> hander.onDelete(bean));
    }

    public void firePrintForm(Object source, B bean) {
        handlers.forEach(handler -> handler.onPrint(source, bean));
    }

    public void fireCloneForm(B bean) {
        handlers.forEach(hander -> hander.onClone(bean));
    }

    public void fireGotoNextItem(B bean) {
        handlers.forEach(hander -> hander.gotoNext(bean));
    }

    public void fireGotoPrevious(B bean) {
        handlers.forEach(hander -> hander.gotoPrevious(bean));
    }

    public void fireExtraAction(String action, B bean) {
        handlers.forEach(hander -> hander.onExtraAction(action, bean));
    }
}
