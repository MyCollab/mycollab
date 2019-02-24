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
package com.mycollab.module.project.view;

import com.mycollab.vaadin.mvp.AbstractVerticalPageView;
import com.mycollab.vaadin.ui.ELabel;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class AbstractProjectPageView extends AbstractVerticalPageView {
    private static final long serialVersionUID = 1L;

    protected ELabel headerText;
    protected MHorizontalLayout header;

    public AbstractProjectPageView(String headerText, VaadinIcons icon) {
        this.headerText = ELabel.h2(String.format("%s %s", icon.getHtml(), headerText));
        super.with(constructHeader());
    }

    private ComponentContainer constructHeader() {
        header = new MHorizontalLayout().with(headerText).withFullWidth().withMargin(true);
        header.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        return header;
    }

    public void addHeaderRightContent(Component component) {
        header.with(component).withAlign(component, Alignment.MIDDLE_RIGHT);
    }
}
