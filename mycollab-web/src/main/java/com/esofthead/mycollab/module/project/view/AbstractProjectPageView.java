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
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class AbstractProjectPageView extends AbstractPageView {
    private static final long serialVersionUID = 1L;

    protected Label headerText;
    protected CssLayout contentWrapper;
    protected MHorizontalLayout header;

    public AbstractProjectPageView(String headerText, FontAwesome icon) {
        super();

        this.headerText = new Label(icon.getHtml() + " " + headerText, ContentMode.HTML);
        super.addComponent(constructHeader());

        contentWrapper = new CssLayout();
        contentWrapper.setStyleName("content-wrapper");
        super.addComponent(contentWrapper);

    }

    private ComponentContainer constructHeader() {
        headerText.setStyleName("hdr-text");
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
