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
package com.mycollab.mobile.ui;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.MyCollabException;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.FieldSelection;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;

/**
 * @author MyCollab Ltd.
 * @since 4.3.1
 */
public abstract class AbstractSelectionCustomField<T, B> extends CustomField<T> implements FieldSelection<B> {
    private static final long serialVersionUID = 1L;

    protected NavigationButton navButton;
    protected B beanItem;

    public AbstractSelectionCustomField(Class<? extends AbstractSelectionView<B>> targetSelectionView) {
        Class<? extends AbstractSelectionView<B>> targetSelectionViewCls = targetSelectionView;
        try {
            final AbstractSelectionView<B> selectionView = targetSelectionViewCls.newInstance();
            selectionView.setSelectionField(this);
            navButton = new NavigationButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SELECT), selectionView);
            navButton.setTargetView(selectionView);
            navButton.setWidth("100%");
            navButton.addClickListener(navigationButtonClickEvent -> selectionView.load());
        } catch (SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException e) {
            throw new MyCollabException(e);
        }
    }

    @Override
    protected Component initContent() {
        return navButton;
    }
}
