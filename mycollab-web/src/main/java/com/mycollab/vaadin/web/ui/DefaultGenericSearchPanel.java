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

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.event.HasSearchHandlers;
import com.mycollab.vaadin.event.SearchHandler;
import com.mycollab.vaadin.ui.HeaderWithIcon;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CustomComponent;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @param <S>
 * @author MyCollab Ltd.
 * @since 2.0
 */
public abstract class DefaultGenericSearchPanel<S extends SearchCriteria> extends CustomComponent implements HasSearchHandlers<S> {
    private static final long serialVersionUID = 1L;

    private List<SearchHandler<S>> searchHandlers;
    private MHorizontalLayout headerLayout;
    private MHorizontalLayout headerRightComp;
    private ComponentContainer headerTitleComp;

    protected boolean canSwitchToAdvanceLayout;
    protected SearchLayout<S> searchLayout;

    public DefaultGenericSearchPanel() {
        this(true);
    }

    public DefaultGenericSearchPanel(boolean canSwitchToAdvanceLayout) {
        this.canSwitchToAdvanceLayout = canSwitchToAdvanceLayout;
        moveToBasicSearchLayout();
    }

    abstract protected SearchLayout<S> createBasicSearchLayout();

    protected SearchLayout<S> createAdvancedSearchLayout() {
        return null;
    }

    protected ComponentContainer buildSearchTitle() {
        return null;
    }

    protected Component buildExtraControls() {
        return null;
    }

    MHorizontalLayout getHeader() {
        if (headerLayout == null) {
            headerLayout = new MHorizontalLayout().withFullWidth().withMargin(new MarginInfo(true, false, true, false));
            headerRightComp = new MHorizontalLayout();
            headerTitleComp = buildSearchTitle();
            if (headerTitleComp != null) {
                headerLayout.with(headerTitleComp, headerRightComp).expand(headerRightComp);
            }

            Component extraControls = buildExtraControls();
            if (extraControls != null) {
                addHeaderRight(extraControls);
            }
        }
        return headerLayout;
    }

    public void setTotalCountNumber(Integer countNumber) {
        if (headerTitleComp instanceof HeaderWithIcon) {
            ((HeaderWithIcon) headerTitleComp).appendToTitle(UserUIContext.getMessage(GenericI18Enum.OPT_TOTAL_VALUE, countNumber));
        }
    }

    protected SearchLayout<S> moveToBasicSearchLayout() {
        searchLayout = createBasicSearchLayout();
        setCompositionRoot(searchLayout);
        return searchLayout;
    }

    protected SearchLayout<S> moveToAdvancedSearchLayout() {
        searchLayout = createAdvancedSearchLayout();
        setCompositionRoot(searchLayout);
        return searchLayout;
    }

    @Override
    public synchronized void addSearchHandler(final SearchHandler<S> handler) {
        if (searchHandlers == null) searchHandlers = new ArrayList<>();
        searchHandlers.add(handler);
    }

    @Override
    public void notifySearchHandler(final S criteria) {
        if (searchHandlers != null) searchHandlers.forEach(handler -> handler.onSearch(criteria));
    }

    public void addHeaderRight(Component component) {
        headerRightComp.with(component).withAlign(component, Alignment.MIDDLE_RIGHT);
    }

    public void callSearchAction() {
        final S searchCriteria = searchLayout.fillUpSearchCriteria();
        this.notifySearchHandler(searchCriteria);
    }
}