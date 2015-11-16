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
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.mvp.service.ComponentScannerService;
import com.esofthead.mycollab.vaadin.ui.MyCollabSession;

import java.util.HashMap;
import java.util.Map;

import static com.esofthead.mycollab.vaadin.ui.MyCollabSession.VIEW_MANAGER_VAL;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ViewManager {

    @SuppressWarnings("unchecked")
    public static <T extends CacheableComponent> T getCacheComponent(final Class<T> viewClass) {
        Map<Class<?>, Object> viewMap = (Map<Class<?>, Object>) MyCollabSession.getVariable(VIEW_MANAGER_VAL);
        if (viewMap == null) {
            viewMap = new HashMap<>();
            MyCollabSession.putVariable(VIEW_MANAGER_VAL, viewMap);
        }

        try {
            LoadPolicy policy = viewClass.getAnnotation(LoadPolicy.class);
            if (policy != null && policy.scope() == ViewScope.PROTOTYPE) {
                return createInstanceFromCls(viewClass);
            }
            T value = (T) viewMap.get(viewClass);
            if (value == null) {
                value = createInstanceFromCls(viewClass);
                viewMap.put(viewClass, value);
                return value;
            } else {
                return value;
            }
        } catch (Exception e) {
            throw new MyCollabException("Can not create view instance of class: " + viewClass, e);
        }
    }

    private static <T> T createInstanceFromCls(Class<T> viewClass) throws IllegalAccessException,
            InstantiationException {
        ComponentScannerService componentScannerService = ApplicationContextUtil.getSpringBean
                (ComponentScannerService.class);
        Class<?> implCls = componentScannerService.getViewImplCls(viewClass);
        if (implCls != null) {
            return (T) implCls.newInstance();
        }
        throw new MyCollabException("Can not find the implementation class for view " + viewClass);
    }
}
