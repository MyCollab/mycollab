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
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.mobile.ui.AbstractMobilePageView;
import com.mycollab.mobile.ui.IListView;
import com.mycollab.vaadin.UserUIContext;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.ui.Component;
import org.vaadin.viritin.button.MButton;

/**
 * @author MyCollab Ltd
 * @since 5.4.3
 */
public abstract class SearchInputView<S extends SearchCriteria> extends AbstractMobilePageView {
    private NavigationManager.NavigationListener listener;

    public SearchInputView() {
        setCaption(UserUIContext.getMessage(GenericI18Enum.BUTTON_SEARCH));
    }

    @Override
    protected void onBecomingVisible() {
        listener = (NavigationManager.NavigationListener) navigationEvent -> {
            if (navigationEvent.getDirection() == NavigationManager.NavigationEvent.Direction.BACK) {
                Component currentComponent = getNavigationManager().getCurrentComponent();
                if (currentComponent instanceof IListView) {
                    S criteria = fillSearchCriteria();
                    ((IListView) currentComponent).getPagedBeanTable().search(criteria);
                }
            }
        };
        getNavigationManager().addNavigationListener(listener);
        setRightComponent(new MButton(UserUIContext.getMessage(GenericI18Enum.ACTION_DONE), clickEvent ->
                getNavigationManager().navigateBack()));
    }

    abstract protected S buildSearchCriteria();

    private S fillSearchCriteria() {
        S criteria = buildSearchCriteria();
        getNavigationManager().removeListener(listener);
        return criteria;
    }
}
