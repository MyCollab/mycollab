package com.mycollab.module.crm.view.activity;

import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.ui.CrmAssetsManager;
import com.mycollab.vaadin.ui.WrappedFormLayoutFactory;
import com.mycollab.vaadin.web.ui.AddViewLayout2;
import com.mycollab.vaadin.web.ui.DefaultDynaFormLayout;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public abstract class MeetingFormLayoutFactory extends WrappedFormLayoutFactory {
    private static final long serialVersionUID = 1L;
    private String title;

    public MeetingFormLayoutFactory(String title) {
        this.title = title;
    }

    @Override
    public AbstractComponent getLayout() {
        AddViewLayout2 meetingLayout = new AddViewLayout2(title, CrmAssetsManager.getAsset(CrmTypeConstants.MEETING));

        Layout topPanel = createTopPanel();
        if (topPanel != null) {
            meetingLayout.addControlButtons(topPanel);
        }
        wrappedLayoutFactory = new DefaultDynaFormLayout(CrmTypeConstants.MEETING, MeetingDefaultFormLayoutFactory.getForm());
        VerticalLayout body = new VerticalLayout();
        body.setStyleName(WebThemes.BOX);
        body.addComponent(wrappedLayoutFactory.getLayout());
        meetingLayout.addBody(body);

        return meetingLayout;
    }

    protected abstract Layout createTopPanel();

    protected abstract Layout createBottomPanel();
}
