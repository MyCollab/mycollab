package com.mycollab.mobile.mvp.view;

import com.mycollab.common.i18n.LicenseI18nEnum;
import com.mycollab.mobile.ui.AbstractMobilePageView;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.ELabel;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.2.6
 */
@ViewComponent
public class NotPresenterView extends AbstractMobilePageView {
    void display() {
        setContent(new MVerticalLayout().withFullWidth().with(new MVerticalLayout(ELabel.fontIcon(FontAwesome.WARNING),
                new ELabel(UserUIContext.getMessage(LicenseI18nEnum.FEATURE_NOT_AVAILABLE))).alignAll(Alignment.MIDDLE_CENTER)));
    }
}
