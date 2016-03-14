/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.mvp;

import com.esofthead.mycollab.core.ResourceNotFoundException;
import com.esofthead.mycollab.core.SecureAccessException;
import com.esofthead.mycollab.security.PermissionChecker;
import com.esofthead.mycollab.security.PermissionMap;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.*;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.esofthead.mycollab.core.utils.ExceptionUtils.getExceptionType;

/**
 * @param <V>
 * @author MyCollab Ltd.
 * @since 3.0
 */
public abstract class AbstractPresenter<V extends PageView> implements IPresenter<V> {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(AbstractPresenter.class);

    protected Class<V> viewClass;
    protected V view;

    public AbstractPresenter(Class<V> viewClass) {
        this.viewClass = viewClass;
    }

    @Override
    public V getView() {
        if (view == null) {
            view = ViewManager.getCacheComponent(viewClass);
            postInitView();
        }
        return view;
    }

    protected void postInitView() {
    }

    public void go(ScreenData<?> data) {
        NavigationManager manager = (NavigationManager) ((UI.getCurrent()).getContent());
        go(manager, data);
    }

    @Override
    public boolean go(ComponentContainer container, ScreenData<?> data) {
        return go(container, data, true);
    }

    @Override
    public boolean go(ComponentContainer container, ScreenData<?> data, boolean isHistoryTrack) {
        getView();
        if (isHistoryTrack) {
            ViewState state = new ViewState(container, this, data);
            HistoryViewManager.addHistory(state);
        } else {
            LOG.debug("Disable history track for " + this);
        }

        if (checkPermissionAccessIfAny()) {
            try {
                onGo(container, data);
            } catch (Throwable e) {
                onErrorStopChain(e);
                return false;
            }
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
        return true;
    }

    protected abstract void onGo(ComponentContainer container, ScreenData<?> data);

    private boolean checkPermissionAccessIfAny() {
        ViewPermission viewPermission = this.getClass().getAnnotation(ViewPermission.class);
        if (viewPermission != null) {
            if (AppContext.isAdmin()) {
                return true;
            } else {
                String permissionId = viewPermission.permissionId();
                int impliedPermissionVal = viewPermission.impliedPermissionVal();
                PermissionMap permissionMap = AppContext.getPermissionMap();
                if (permissionMap == null) {
                    return false;
                } else {
                    Integer value = permissionMap.get(permissionId);
                    return (value != null) && PermissionChecker.isImplied(value, impliedPermissionVal);
                }
            }
        } else {
            return true;
        }
    }

    @Override
    public void handleChain(ComponentContainer container, PageActionChain pageActionChain) {
        ScreenData pageAction = pageActionChain.pop();
        boolean isSuccess = go(container, pageAction);

        if (pageActionChain.hasNext() && isSuccess) {
            onHandleChain(container, pageActionChain);
        } else {
            onDefaultStopChain();
        }
    }

    protected void onDefaultStopChain() {

    }

    protected void onErrorStopChain(Throwable throwable) {
        if (getExceptionType(throwable, ResourceNotFoundException.class) != null) {
            NotificationUtil.showRecordNotExistNotification();
        } else if (getExceptionType(throwable, SecureAccessException.class) != null) {
            NotificationUtil.showMessagePermissionAlert();
        } else {
            LOG.error("Exception", throwable);
        }
    }

    protected void onHandleChain(ComponentContainer container, PageActionChain pageActionChain) {
        throw new UnsupportedOperationException("You need override this method");
    }
}
