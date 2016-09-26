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
package com.mycollab.module.crm.view;

import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.crm.event.CrmEvent;
import com.mycollab.vaadin.mvp.PageView;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.ComponentContainer;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class CrmGenericPresenter<V extends PageView> extends AbstractPresenter<V> {
    private static final long serialVersionUID = 1L;

    public CrmGenericPresenter(Class<V> viewClass) {
        super(viewClass);
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        CrmModule crmModule = (CrmModule) container;
        crmModule.addView(view);
    }

    @Override
    protected void onErrorStopChain(Throwable throwable) {
        super.onErrorStopChain(throwable);
        if (!(this instanceof CrmHomePresenter)) {
            EventBusFactory.getInstance().post(new CrmEvent.GotoHome(this, null));
        }
    }
}
