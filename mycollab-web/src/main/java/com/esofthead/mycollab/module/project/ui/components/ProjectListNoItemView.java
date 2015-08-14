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
package com.esofthead.mycollab.module.project.ui.components;

import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.PageView;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Label;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 5.0.0
 */
public abstract class ProjectListNoItemView extends MVerticalLayout implements PageView {
    public ProjectListNoItemView() {
        this.withMargin(false).withStyleName("case-noitem");
        this.setDefaultComponentAlignment(Alignment.TOP_CENTER);

        Label image = new Label(viewIcon().getHtml(), ContentMode.HTML);
        image.setSizeUndefined();

        Label title = new Label(viewTitle());
        title.addStyleName("h2");
        title.setSizeUndefined();

        Label body = new Label(String.format("<div style=\"text-align:center\">%s</div>",
                viewHint()), ContentMode.HTML);
        body.setWidth("500px");

        MHorizontalLayout links = createControlButtons();

        this.with(image, title, body, links);
    }

    protected MHorizontalLayout createControlButtons() {
        Button createItemBtn = new Button(actionMessage(), actionListener());
        createItemBtn.setEnabled(hasPermission());
        createItemBtn.addStyleName(UIConstants.THEME_GREEN_LINK);

        return new MHorizontalLayout().with(createItemBtn);
    }

    abstract protected FontAwesome viewIcon();

    abstract protected String viewTitle();

    abstract protected String viewHint();

    abstract protected String actionMessage();

    abstract protected Button.ClickListener actionListener();

    abstract protected boolean hasPermission();

    @Override
    public ComponentContainer getWidget() {
        return this;
    }

    @Override
    public <E> void addViewListener(ViewListener<E> listener) {

    }
}