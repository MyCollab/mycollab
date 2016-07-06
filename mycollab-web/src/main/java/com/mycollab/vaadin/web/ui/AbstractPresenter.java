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
package com.mycollab.vaadin.web.ui;

import com.mycollab.core.MyCollabException;
import com.mycollab.core.ResourceNotFoundException;
import com.mycollab.core.SecureAccessException;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.user.accountsettings.view.AccountModulePresenter;
import com.mycollab.security.PermissionChecker;
import com.mycollab.security.PermissionMap;
import com.mycollab.shell.events.ShellEvent;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.mvp.*;
import com.mycollab.vaadin.mvp.service.ComponentScannerService;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.server.ClientConnector;
import com.vaadin.ui.ComponentContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.mycollab.core.utils.ExceptionUtils.getExceptionType;

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

    public AbstractPresenter(Class<V> viewClass) {
        this.viewClass = viewClass;
        ComponentScannerService componentScannerService = AppContextUtil.getSpringBean(ComponentScannerService.class);
        implClass = (Class<V>) componentScannerService.getViewImplCls(viewClass);
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
                view.addAttachListener(new ClientConnector.AttachListener() {
                    @Override
                    public void attach(ClientConnector.AttachEvent event) {
                        if (view instanceof InitializingView) {
                            ((InitializingView) view).initContent();
                        }
                        viewAttached();
                    }
                });

                view.addDetachListener(new ClientConnector.DetachListener() {
                    @Override
                    public void detach(ClientConnector.DetachEvent event) {
                        viewDetached();
                    }
                });
                postInitView();
            } catch (Exception e) {
                LOG.error("Can not init view " + implClass, e);
            }
        }
    }

    protected void postInitView() {
    }

    protected void viewAttached() {
    }

    protected void viewDetached() {
    }

    @Override
    public boolean go(ComponentContainer container, ScreenData<?> data) {
        if (!AppContext.getInstance().getIsValidAccount() && (!(this instanceof AccountModulePresenter)
                && ModuleHelper.getCurrentModule() != null && !ModuleHelper.isCurrentAccountModule())) {
            EventBusFactory.getInstance().post(new ShellEvent.GotoUserAccountModule(this, new String[]{"billing"}));
            return true;
        } else {
            initView();

            if (view == null) {
                LOG.error("Can not find view " + viewClass);
                return false;
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
                    return (value != null) && PermissionChecker.isImplied(value, impliedPermissionVal);
                }
            }
        } else {
            return true;
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public final void handleChain(ComponentContainer container, PageActionChain pageActionChain) {
        ScreenData pageAction = pageActionChain.pop();
        boolean isSuccess = go(container, pageAction);

        if (isSuccess) {
            if (pageActionChain.hasNext()) {
                onHandleChain(container, pageActionChain);
            } else {
                onDefaultStopChain();
            }
        }
    }

    protected void onDefaultStopChain() {

    }

    protected void onHandleChain(ComponentContainer container, PageActionChain pageActionChain) {
        throw new UnsupportedOperationException("You need override this method");
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
}
