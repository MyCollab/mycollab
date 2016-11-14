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
package com.mycollab.mobile.mvp;

import com.mycollab.core.ResourceNotFoundException;
import com.mycollab.core.SecureAccessException;
import com.mycollab.security.PermissionChecker;
import com.mycollab.security.PermissionMap;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.*;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.mycollab.core.utils.ExceptionUtils.getExceptionType;

/**
 * @param <V>
 * @author MyCollab Ltd.
 * @since 3.0
 */
public abstract class AbstractPresenter<V extends PageView> implements IPresenter<V> {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(AbstractPresenter.class);

    private Class<V> viewClass;
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
    public boolean go(HasComponents container, ScreenData<?> data) {
        getView();

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

    protected abstract void onGo(HasComponents container, ScreenData<?> data);

    private boolean checkPermissionAccessIfAny() {
        ViewPermission viewPermission = this.getClass().getAnnotation(ViewPermission.class);
        if (viewPermission != null) {
            if (UserUIContext.isAdmin()) {
                return true;
            } else {
                String permissionId = viewPermission.permissionId();
                int impliedPermissionVal = viewPermission.impliedPermissionVal();
                PermissionMap permissionMap = UserUIContext.getPermissionMap();
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
    public void handleChain(HasComponents container, PageActionChain pageActionChain) {
        ScreenData pageAction = pageActionChain.pop();
        boolean isSuccess = go(container, pageAction);

        if (pageActionChain.hasNext() && isSuccess) {
            onHandleChain(container, pageActionChain);
        }
    }

    protected void onErrorStopChain(Throwable throwable) {
        if (getExceptionType(throwable, ResourceNotFoundException.class) != null) {
            NotificationUtil.showRecordNotExistNotification();
        } else if (getExceptionType(throwable, SecureAccessException.class) != null) {
            NotificationUtil.showMessagePermissionAlert();
        } else if (getExceptionType(throwable, PresenterNotFoundException.class) != null) {
            NotificationUtil.showFeatureNotPresentInSubscription();
        } else {
            LOG.error("Exception", throwable);
        }
    }

    protected void onHandleChain(HasComponents container, PageActionChain pageActionChain) {
        throw new UnsupportedOperationException("You need override this method");
    }
}
