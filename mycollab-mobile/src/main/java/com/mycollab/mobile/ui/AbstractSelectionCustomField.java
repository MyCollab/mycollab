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
        try {
            final AbstractSelectionView<B> selectionView = targetSelectionView.newInstance();
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
