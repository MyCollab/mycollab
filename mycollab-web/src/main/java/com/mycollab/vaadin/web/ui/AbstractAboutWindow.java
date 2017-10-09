package com.mycollab.vaadin.web.ui;

import com.mycollab.common.i18n.ShellI18nEnum;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.CacheableComponent;
import org.vaadin.viritin.layouts.MWindow;

/**
 * @author MyCollab Ltd
 * @since 5.2.6
 */
public abstract class AbstractAboutWindow extends MWindow implements CacheableComponent {
    public AbstractAboutWindow() {
        super(UserUIContext.getMessage(ShellI18nEnum.OPT_ABOUT_MYCOLLAB));
        this.withModal(true).withResizable(false).withWidth("600px").withCenter();
    }
}
