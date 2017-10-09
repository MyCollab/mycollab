package com.mycollab.community.module.file.view;

import com.mycollab.module.file.view.IFileModule;
import com.mycollab.module.file.view.IFileModulePresenter;
import com.mycollab.shell.view.MainView;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class FileModulePresenter extends AbstractPresenter<IFileModule> implements IFileModulePresenter {
    private static final long serialVersionUID = 1L;

    public FileModulePresenter() {
        super(IFileModule.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        MainView mainView = (MainView) container;
        mainView.addModule(view);
    }
}
