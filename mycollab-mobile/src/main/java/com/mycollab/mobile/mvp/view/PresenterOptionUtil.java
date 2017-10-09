package com.mycollab.mobile.mvp.view;

import com.mycollab.vaadin.mvp.IPresenter;
import com.mycollab.vaadin.mvp.PresenterNotFoundException;
import com.mycollab.vaadin.mvp.PresenterResolver;

/**
 * @author MyCollab Ltd
 * @since 5.2.6
 */
public class PresenterOptionUtil {
    public static IPresenter getPresenter(Class presenterClass) {
        try {
            return PresenterResolver.getPresenter(presenterClass);
        } catch (PresenterNotFoundException e) {
            return new NotPresentPresenter();
        }
    }
}
