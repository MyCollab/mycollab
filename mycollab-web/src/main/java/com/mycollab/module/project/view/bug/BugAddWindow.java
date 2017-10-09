package com.mycollab.module.project.view.bug;

import com.mycollab.module.project.i18n.BugI18nEnum;
import com.mycollab.module.tracker.domain.SimpleBug;
import com.mycollab.vaadin.UserUIContext;
import org.vaadin.viritin.layouts.MWindow;

/**
 * @author MyCollab Ltd
 * @since 5.2.0
 */
public class BugAddWindow extends MWindow {
    public BugAddWindow(SimpleBug bug) {
        if (bug.getId() == null) {
            setCaption(UserUIContext.getMessage(BugI18nEnum.NEW));
        } else {
            setCaption(UserUIContext.getMessage(BugI18nEnum.SINGLE) + ": " + bug.getName());
        }

        BugEditForm editForm = new BugEditForm() {
            @Override
            protected void postExecution() {
                close();
            }
        };
        editForm.setBean(bug);

        withWidth("1200px").withModal(true).withResizable(false).withContent(editForm).withCenter();
    }
}
