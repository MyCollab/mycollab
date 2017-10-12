/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.mobile.ui;

import com.mycollab.vaadin.ui.FieldSelection;

/**
 * @author MyCollab Ltd.
 * @since 4.3.1
 */
public abstract class AbstractSelectionView<B> extends AbstractMobilePageView {
    private static final long serialVersionUID = 1L;

    protected FieldSelection<B> selectionField;

    public FieldSelection<B> getSelectionField() {
        return selectionField;
    }

    public void setSelectionField(FieldSelection<B> selectionField) {
        this.selectionField = selectionField;
    }

    public abstract void load();
}
