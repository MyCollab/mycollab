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
package com.esofthead.mycollab.module.project.view;

import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.ui.ELabel;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class AbstractProjectPageView extends AbstractPageView {
    private static final long serialVersionUID = 1L;

    protected ELabel headerText;
    protected MCssLayout contentWrapper;
    protected MHorizontalLayout header;

    public AbstractProjectPageView(String headerText, FontAwesome icon) {
        super();

        this.headerText = ELabel.h2(icon.getHtml() + " " + headerText);
        super.addComponent(constructHeader());

        contentWrapper = new MCssLayout().withStyleName(UIConstants.CONTENT_WRAPPER);
        super.addComponent(contentWrapper);

    }

    private ComponentContainer constructHeader() {
        header = new MHorizontalLayout().with(headerText).withStyleName("hdr-view").withWidth("100%").withMargin(true);
        return header;
    }

    public void addHeaderRightContent(Component c) {
        header.with(c).withAlign(c, Alignment.MIDDLE_RIGHT);
    }

    @Override
    public void addComponent(Component c) {
        contentWrapper.addComponent(c);
    }

    @Override
    public void replaceComponent(Component oldComponent, Component newComponent) {
        contentWrapper.replaceComponent(oldComponent, newComponent);
    }

    public ComponentContainer getBody() {
        return contentWrapper;
    }
}
