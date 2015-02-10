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
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 5.0.0
 */
public abstract class ProjectListNoItemView extends AbstractPageView {
    public ProjectListNoItemView() {
        MVerticalLayout layout = new MVerticalLayout();
        layout.addStyleName("case-noitem");
        layout.setDefaultComponentAlignment(Alignment.TOP_CENTER);

        Label image = new Label(viewIcon().getHtml(), ContentMode.HTML);
        image.setSizeUndefined();
        layout.with(image).withAlign(image, Alignment.TOP_CENTER);

        Label title = new Label(viewTitle());
        title.addStyleName("h2");
        title.setSizeUndefined();
        layout.with(title).withAlign(title, Alignment.TOP_CENTER);

        Label body = new Label(viewHint());
        body.setWidthUndefined();
        layout.addComponent(body);

        Button createBugBtn = new Button(
                actionMessage(), actionListener());
        createBugBtn.setEnabled(hasPermission());

        MHorizontalLayout links = new MHorizontalLayout();

        links.addComponent(createBugBtn);
        createBugBtn.addStyleName(UIConstants.THEME_GREEN_LINK);

        layout.addComponent(links);
        this.addComponent(layout);
        this.setComponentAlignment(layout, Alignment.TOP_CENTER);
    }

    abstract protected FontAwesome viewIcon();

    abstract protected String viewTitle();

    abstract protected String viewHint();

    abstract protected String actionMessage();

    abstract protected Button.ClickListener actionListener();

    abstract protected boolean hasPermission();
}
