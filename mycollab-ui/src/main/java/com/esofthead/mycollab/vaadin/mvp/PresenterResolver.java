/**
 * This file is part of mycollab-ui.
 *
 * mycollab-ui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-ui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-ui.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.vaadin.mvp;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.spring.AppContextUtil;
import com.esofthead.mycollab.vaadin.mvp.service.ComponentScannerService;
import com.esofthead.mycollab.vaadin.ui.MyCollabSession;

import java.util.Map;
import java.util.WeakHashMap;

import static com.esofthead.mycollab.vaadin.ui.MyCollabSession.PRESENTER_VAL;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public final class PresenterResolver {

    public static <P extends IPresenter> P getPresenter(Class<P> presenterClass) {
        Map<Class<?>, Object> presenterMap = (Map<Class<?>, Object>) MyCollabSession.getCurrentUIVariable(PRESENTER_VAL);
        if (presenterMap == null) {
            presenterMap = new WeakHashMap<>();
            MyCollabSession.putCurrentUIVariable(PRESENTER_VAL, presenterMap);
        }

        P value = (P) presenterMap.get(presenterClass);
        if (value == null) {
            value = initPresenter(presenterClass);
            presenterMap.put(presenterClass, value);
            return value;
        } else {
            LoadPolicy policy = presenterClass.getAnnotation(LoadPolicy.class);
            if (policy != null && policy.scope() == ViewScope.PROTOTYPE) {
                value = initPresenter(presenterClass);
                presenterMap.put(presenterClass, value);
            }
            return value;
        }
    }

    public static <P extends IPresenter> P getPresenterAndInitView(Class<P> presenterClass) {
        P presenter = getPresenter(presenterClass);
        presenter.getView();
        return presenter;
    }

    private static <P extends IPresenter> P initPresenter(Class<P> presenterClass) {
        P value = null;
        try {
            if (!presenterClass.isInterface()) {
                value = presenterClass.newInstance();
            } else {
                ComponentScannerService componentScannerService = AppContextUtil.getSpringBean
                        (ComponentScannerService.class);
                Class presenterClassImpl = componentScannerService.getPresenterImplCls(presenterClass);
                if (presenterClassImpl != null) {
                    value = (P) presenterClassImpl.newInstance();
                }
            }
        } catch (Exception e) {
            throw new MyCollabException(e);
        }
        if (value != null) {
            return value;
        } else {
            throw new PresenterNotFoundException("Can not find instance of " + presenterClass);
        }
    }
}
