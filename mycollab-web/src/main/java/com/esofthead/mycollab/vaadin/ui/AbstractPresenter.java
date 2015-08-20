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
package com.esofthead.mycollab.vaadin.ui;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.security.PermissionChecker;
import com.esofthead.mycollab.security.PermissionMap;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.*;
import com.vaadin.ui.ComponentContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @param <V>
 * @author MyCollab Ltd.
 * @since 2.0
 */
public abstract class AbstractPresenter<V extends PageView> implements IPresenter<V> {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(AbstractPresenter.class);

    protected Class<V> viewClass;
    protected Class<V> implClass;
    protected V view;

    @SuppressWarnings("unchecked")
    public AbstractPresenter(Class<V> viewClass) {
        this.viewClass = viewClass;
        implClass = (Class<V>) ViewManager.getViewImplCls(viewClass);
        if (implClass == null) {
            throw new MyCollabException("Can not find the implementation for view " + viewClass);
        }
    }

    @Override
    public V getView() {
        initView();
        return view;
    }

    private void initView() {
        if (view == null) {
            try {
                view = implClass.newInstance();
                postInitView();
            } catch (Exception e) {
                LOG.error("Can not init view " + implClass, e);
            }
        }
    }

    protected void postInitView() {
    }

    @Override
    public void go(ComponentContainer container, ScreenData<?> data) {
        go(container, data, true);
    }

    @Override
    public void go(ComponentContainer container, ScreenData<?> data, boolean isHistoryTrack) {
        initView();
        LOG.debug("Go to view: " + view);
        if (isHistoryTrack) {
            ViewState state = new ViewState(container, this, data);
            HistoryViewManager.addHistory(state);
        }

        if (view == null) {
            NotificationUtil.showMessagePermissionAlert();
        }

        if (checkPermissionAccessIfAny()) {
            onGo(container, data);
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }

    }

    protected abstract void onGo(ComponentContainer container, ScreenData<?> data);

    private boolean checkPermissionAccessIfAny() {
        ViewPermission viewPermission = this.getClass().getAnnotation(ViewPermission.class);
        if (viewPermission != null) {
            String permissionId = viewPermission.permissionId();
            int impliedPermissionVal = viewPermission.impliedPermissionVal();

            if (AppContext.isAdmin()) {
                return true;
            } else {
                PermissionMap permissionMap = AppContext.getPermissionMap();
                if (permissionMap == null) {
                    return false;
                } else {
                    Integer value = permissionMap.get(permissionId);
                    if (value == null) {
                        return false;
                    } else {
                        return PermissionChecker.isImplied(value, impliedPermissionVal);
                    }
                }
            }
        } else {
            return true;
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void handleChain(ComponentContainer container, PageActionChain pageActionChain) {
        ScreenData pageAction = pageActionChain.pop();
        go(container, pageAction);

        if (pageActionChain.hasNext()) {
            onHandleChain(container, pageActionChain);
        } else {
            onDefaultStopChain();
        }
    }

    protected void onDefaultStopChain() {

    }

    protected void onHandleChain(ComponentContainer container, PageActionChain pageActionChain) {
        throw new UnsupportedOperationException("You need override this method");
    }
}
