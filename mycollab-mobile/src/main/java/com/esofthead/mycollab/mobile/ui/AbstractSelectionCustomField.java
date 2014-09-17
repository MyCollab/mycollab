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

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.vaadin.ui.FieldSelection;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.3.1
 */
public abstract class AbstractSelectionCustomField<T, B> extends CustomField<T>
		implements FieldSelection<B> {
	private static final long serialVersionUID = 1L;

	private Class<? extends AbstractSelectionView<B>> targetSelectionViewCls;
	protected MobileNavigationButton navButton = new MobileNavigationButton();
	protected B beanItem;

	public AbstractSelectionCustomField(
			Class<? extends AbstractSelectionView<B>> targetSelectionView) {
		this.targetSelectionViewCls = targetSelectionView;
	}

	@Override
	protected Component initContent() {
		navButton.setStyleName("combo-box");
		try {

			final AbstractSelectionView<B> selectionView = targetSelectionViewCls
					.newInstance();
			selectionView.setSelectionField(this);
			navButton.setTargetView(selectionView);
			navButton.setWidth("100%");
			navButton
					.addClickListener(new NavigationButton.NavigationButtonClickListener() {
						private static final long serialVersionUID = 7766417204333658973L;

						@Override
						public void buttonClick(
								NavigationButton.NavigationButtonClickEvent event) {
							selectionView.load();
						}
					});
			return navButton;
		} catch (SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException e) {
			throw new MyCollabException(e);
		}
	}
}
