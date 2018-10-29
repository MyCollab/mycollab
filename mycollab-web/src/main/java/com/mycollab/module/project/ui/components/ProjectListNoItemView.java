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
package com.mycollab.module.project.ui.components;

import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 5.0.0
 */
public abstract class ProjectListNoItemView extends VerticalLayout {
    public ProjectListNoItemView() {
        MVerticalLayout content = new MVerticalLayout().withWidth("700px");
        ELabel image = ELabel.h2(viewIcon().getHtml()).withUndefinedWidth();

        ELabel title = ELabel.h2(viewTitle()).withUndefinedWidth();
        ELabel body = ELabel.html(viewHint()).withStyleName(UIConstants.LABEL_WORD_WRAP).withUndefinedWidth();
        MHorizontalLayout links = createControlButtons();
        content.with(image, title, body, links).alignAll(Alignment.TOP_CENTER);
        this.addComponent(content);
        this.setComponentAlignment(content, Alignment.MIDDLE_CENTER);
    }

    protected MHorizontalLayout createControlButtons() {
        MButton createItemBtn = new MButton(actionMessage(), actionListener()).withStyleName(WebThemes.BUTTON_ACTION)
                .withVisible(hasPermission());
        return new MHorizontalLayout(createItemBtn);
    }

    abstract protected VaadinIcons viewIcon();

    abstract protected String viewTitle();

    abstract protected String viewHint();

    abstract protected String actionMessage();

    abstract protected Button.ClickListener actionListener();

    abstract protected boolean hasPermission();
}