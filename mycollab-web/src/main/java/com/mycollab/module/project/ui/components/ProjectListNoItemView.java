package com.mycollab.module.project.ui.components;

import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.server.FontAwesome;
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
        ELabel image = ELabel.h2(viewIcon().getHtml()).withWidthUndefined();

        ELabel title = ELabel.h2(viewTitle()).withWidthUndefined();
        ELabel body = ELabel.html(viewHint()).withStyleName(UIConstants.LABEL_WORD_WRAP).withWidthUndefined();
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

    abstract protected FontAwesome viewIcon();

    abstract protected String viewTitle();

    abstract protected String viewHint();

    abstract protected String actionMessage();

    abstract protected Button.ClickListener actionListener();

    abstract protected boolean hasPermission();
}