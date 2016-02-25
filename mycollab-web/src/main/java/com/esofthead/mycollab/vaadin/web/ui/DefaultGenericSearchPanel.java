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
package com.esofthead.mycollab.vaadin.web.ui;

import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.vaadin.ui.HeaderWithFontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComponentContainer;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @param <S>
 * @author MyCollab Ltd.
 * @since 2.0
 */
public abstract class DefaultGenericSearchPanel<S extends SearchCriteria> extends GenericSearchPanel<S> {
    private static final long serialVersionUID = 1L;

    private MHorizontalLayout header;
    private ComponentContainer headerText;
    private MHorizontalLayout rightComponent;

    public DefaultGenericSearchPanel() {
        moveToBasicSearchLayout();
    }

    abstract protected SearchLayout<S> createBasicSearchLayout();

    abstract protected SearchLayout<S> createAdvancedSearchLayout();

    abstract protected ComponentContainer buildSearchTitle();

    protected void buildExtraControls() {
    }

    protected ComponentContainer constructHeader() {
        if (header == null) {
            headerText = buildSearchTitle();
            rightComponent = new MHorizontalLayout();

            header = new MHorizontalLayout().withStyleName(UIConstants.HEADER_VIEW).withWidth("100%")
                    .withMargin(new MarginInfo(true, false, true, false));

            header.with(headerText, rightComponent).withAlign(headerText, Alignment.MIDDLE_LEFT)
                    .withAlign(rightComponent, Alignment.MIDDLE_RIGHT).expand(headerText);

            buildExtraControls();
            return header;
        } else {
            return header;
        }
    }

    public void setTotalCountNumber(int countNumber) {
        if (headerText instanceof HeaderWithFontAwesome) {
            ((HeaderWithFontAwesome) headerText).appendToTitle(String.format("(%d Total)", countNumber));
        }
    }

    protected void moveToBasicSearchLayout() {
        SearchLayout<S> layout = createBasicSearchLayout();
        setCompositionRoot(layout);
    }

    protected void moveToAdvancedSearchLayout() {
        SearchLayout<S> layout = createAdvancedSearchLayout();
        setCompositionRoot(layout);
    }
}