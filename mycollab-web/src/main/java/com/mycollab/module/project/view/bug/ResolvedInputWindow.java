package com.mycollab.module.project.view.bug;

import com.mycollab.module.project.i18n.BugI18nEnum;
import com.mycollab.module.tracker.domain.SimpleBug;
import com.mycollab.vaadin.UserUIContext;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ResolvedInputWindow extends MWindow {
    private static final long serialVersionUID = 1L;

    public ResolvedInputWindow(SimpleBug bugValue) {
        super(UserUIContext.getMessage(BugI18nEnum.OPT_RESOLVE_BUG, bugValue.getName()));
        ResolvedInputForm editForm = new ResolvedInputForm(bugValue) {
            @Override
            protected void postExecution() {
                close();
            }
        };
        VerticalLayout contentLayout = new VerticalLayout();
        contentLayout.setMargin(new MarginInfo(false, false, true, false));
        contentLayout.addComponent(editForm);
        withWidth("900px").withModal(true).withResizable(false).withContent(contentLayout).withCenter();
    }
}
