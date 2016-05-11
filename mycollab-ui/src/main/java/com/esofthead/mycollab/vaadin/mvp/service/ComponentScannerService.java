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
package com.esofthead.mycollab.vaadin.mvp.service;

import com.esofthead.mycollab.vaadin.mvp.IPresenter;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
@Component
public class ComponentScannerService implements InitializingBean {
    private static Logger LOG = LoggerFactory.getLogger(ComponentScannerService.class);
    private Set<Class<?>> viewClasses = new HashSet<>();
    private Set<Class<? extends IPresenter>> presenterClasses = new HashSet<>();

    private Map<Class, Class> cacheViewClasses = new ConcurrentHashMap<>();
    private Map<Class, Class> cachePresenterClasses = new ConcurrentHashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(ViewComponent.class));
        provider.addIncludeFilter(new AssignableTypeFilter(IPresenter.class));
        for (BeanDefinition candidate : provider.findCandidateComponents("com.esofthead.mycollab.**.view")) {
            Class cls = ClassUtils.resolveClassName(candidate.getBeanClassName(), ClassUtils.getDefaultClassLoader());
            if (cls.getAnnotation(ViewComponent.class) != null) {
                viewClasses.add(cls);
            } else if (IPresenter.class.isAssignableFrom(cls)) {
                presenterClasses.add(cls);
            }
        }
        LOG.info("Resolve view and presenter classes");
    }

    public Class<?> getViewImplCls(Class<?> viewClass) {
        Class aClass = cacheViewClasses.get(viewClass);
        if (aClass == null) {
            for (Class<?> classInstance : viewClasses) {
                if (viewClass.isAssignableFrom(classInstance)) {
                    cacheViewClasses.put(viewClass, classInstance);
                    return classInstance;
                }
            }
        }

        return aClass;
    }

    public Class<?> getPresenterImplCls(Class<?> presenterClass) {
        Class aClass = cachePresenterClasses.get(presenterClass);
        if (aClass == null) {
            for (Class<?> classInstance : presenterClasses) {
                if (presenterClass.isAssignableFrom(classInstance) && !classInstance.isInterface()) {
                    cachePresenterClasses.put(presenterClass, classInstance);
                    return classInstance;
                }
            }
        }

        return aClass;
    }
}
