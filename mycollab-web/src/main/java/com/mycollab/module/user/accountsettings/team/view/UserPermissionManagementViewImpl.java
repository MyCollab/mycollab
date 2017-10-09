package com.mycollab.module.user.accountsettings.team.view;

import com.mycollab.module.user.accountsettings.localization.RoleI18nEnum;
import com.mycollab.module.user.accountsettings.localization.UserI18nEnum;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.AbstractVerticalPageView;
import com.mycollab.vaadin.mvp.PresenterResolver;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.web.ui.TabSheetDecorator;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.TabSheet.Tab;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
@ViewComponent
public class UserPermissionManagementViewImpl extends AbstractVerticalPageView implements UserPermissionManagementView {
    private static final long serialVersionUID = 1L;
    private TabSheetDecorator groupTab;
    private UserPresenter userPresenter;
    private RolePresenter rolePresenter;

    public UserPermissionManagementViewImpl() {
        groupTab = new TabSheetDecorator();
        this.addComponent(groupTab);
        buildComponents();
    }

    private void buildComponents() {
        userPresenter = PresenterResolver.getPresenter(UserPresenter.class);
        groupTab.addTab(userPresenter.getView(), UserUIContext.getMessage(UserI18nEnum.LIST));

        rolePresenter = PresenterResolver.getPresenter(RolePresenter.class);
        groupTab.addTab(rolePresenter.getView(), UserUIContext.getMessage(RoleI18nEnum.LIST));

        groupTab.addSelectedTabChangeListener(new SelectedTabChangeListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void selectedTabChange(SelectedTabChangeEvent event) {
                Tab tab = ((TabSheetDecorator) event.getTabSheet()).getSelectedTabInfo();
                String caption = tab.getCaption();
                if (UserUIContext.getMessage(UserI18nEnum.LIST).equals(caption)) {
                    userPresenter.go(UserPermissionManagementViewImpl.this, null);
                } else if (UserUIContext.getMessage(RoleI18nEnum.LIST).equals(caption)) {
                    rolePresenter.go(UserPermissionManagementViewImpl.this, null);
                }
            }
        });

    }

    @Override
    public Component gotoSubView(String name) {
        return groupTab.selectTab(name).getComponent();
    }

}
