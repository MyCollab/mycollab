package com.mycollab.vaadin.web.ui;

import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.web.CustomLayoutExt;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class AddViewLayout extends CustomLayoutExt {
    private static final long serialVersionUID = 1L;

    private FontAwesome viewIcon;
    private Label titleLbl;
    private final MHorizontalLayout header;

    public AddViewLayout(String viewTitle, FontAwesome viewIcon) {
        super("addView");

        this.viewIcon = viewIcon;

        header = new MHorizontalLayout().withFullWidth().withMargin(new MarginInfo(true, false, true, false));
        header.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

        titleLbl = ELabel.h2("");
        header.with(titleLbl).expand(titleLbl);
        setHeader(viewTitle);
        addComponent(header, "addViewHeader");
    }

    public AddViewLayout(MHorizontalLayout header) {
        super("addView");
        this.header = header;
        addComponent(header, "addViewHeader");
    }

    public void addBody(Component body) {
        addComponent(body, "addViewBody");
    }

    public void addBottom(Component bottomControls) {
        this.addComponent(bottomControls, "addViewBottomControls");
    }

    public void addHeaderTitle(Component headerContainer) {
        header.addComponent(headerContainer, 0);
        header.withAlign(headerContainer, Alignment.TOP_LEFT).expand(headerContainer);
    }

    public void addHeaderRight(Component headerRight) {
        header.with(headerRight).withAlign(headerRight, Alignment.TOP_RIGHT);
    }

    public void setHeader(String viewTitle) {
        String title = viewIcon.getHtml() + " " + viewTitle;
        titleLbl.setValue(title);
    }

    public void setTitle(final String title) {
        if (title != null) {
            CssLayout titleWrap = new CssLayout();
            titleWrap.setWidth("100%");
            titleWrap.addComponent(ELabel.h3(title));
            addComponent(titleWrap, "addViewTitle");
        } else {
            removeComponent("addViewTitle");
        }
    }
}
