/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.view.setting;

import com.mycollab.module.crm.data.CustomViewScreenData;
import com.mycollab.module.crm.data.NotificationSettingScreenData;
import com.mycollab.vaadin.mvp.AbstractCssPageView;
import com.mycollab.vaadin.mvp.PageView;
import com.mycollab.vaadin.mvp.PresenterResolver;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.web.ui.VerticalTabsheet;
import com.mycollab.vaadin.web.ui.VerticalTabsheet.TabImpl;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.TabSheet.Tab;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
@ViewComponent
public class CrmSettingContainer extends AbstractCssPageView implements PageView {
    private static final long serialVersionUID = 1L;

    private final VerticalTabsheet settingTab;

    private ICrmCustomViewPresenter customViewPresenter;
    private CrmNotificationSettingPresenter notificationPresenter;

    public CrmSettingContainer() {
        this.setWidth("100%");

        final CssLayout contentWrapper = new CssLayout();
        contentWrapper.setWidth("100%");
        this.addComponent(contentWrapper);

        settingTab = new VerticalTabsheet(false);
        settingTab.setSizeFull();
        settingTab.setNavigatorWidth("250px");
        settingTab.setNavigatorStyleName("sidebar-menu");
        settingTab.setHeight(null);

        buildComponents();
        contentWrapper.addComponent(settingTab);

    }

    @Override
    public void attach() {
        super.attach();
        if (this.getParent() instanceof CustomLayout) {
            this.getParent().addStyleName("preview-comp");
        }
    }

    private void buildComponents() {
        settingTab.addTab(constructNotificationSettingView(), "notification",
                "Notifications");

        settingTab.addTab(constructCustomLayoutView(), "customlayout",
                "Custom Layouts");

        settingTab.addSelectedTabChangeListener(new SelectedTabChangeListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void selectedTabChange(SelectedTabChangeEvent event) {
                Tab tab = ((VerticalTabsheet) event.getSource()).getSelectedTab();
                String tabId = ((TabImpl) tab).getTabId();

                if ("notification".equals(tabId)) {
                    notificationPresenter.go(CrmSettingContainer.this, new NotificationSettingScreenData.Read());
                } else if ("customlayout".equals(tabId)) {
                    customViewPresenter.go(CrmSettingContainer.this, new CustomViewScreenData.Read());
                }

            }
        });
    }

    private Component constructNotificationSettingView() {
        notificationPresenter = PresenterResolver.getPresenter(CrmNotificationSettingPresenter.class);
        return notificationPresenter.getView();
    }

    private Component constructCustomLayoutView() {
        customViewPresenter = PresenterResolver.getPresenter(ICrmCustomViewPresenter.class);
        return customViewPresenter.getView();
    }

    public Component gotoSubView(String viewId) {
        return settingTab.selectTab(viewId);
    }

}
