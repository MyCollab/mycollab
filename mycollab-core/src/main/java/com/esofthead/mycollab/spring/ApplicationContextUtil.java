/**
 * This file is part of mycollab-core.
 *
 * mycollab-core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-core.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.Validator;

/**
 * Static spring application context to retrieve spring bean without in servlet
 * context
 */
@Component("appContextUtil")
public class ApplicationContextUtil implements ApplicationContextAware {
    private static ApplicationContext ctx;

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        ctx = appContext;
    }

    public static <T> T getSpringBean(String name, Class<T> classType) {
        if (ctx == null) {
            return null;
        }
        return ctx.getBean(name, classType);
    }

    public static Validator getValidator() {
        return getSpringBean("validator", LocalValidatorFactoryBean.class);
    }

    public static <T> T getSpringBean(Class<T> classType) {
        if (ctx == null) {
            return null;
        }
        try {
            return ctx.getBean(classType);
        } catch (NoSuchBeanDefinitionException e) {
            return null;
        }
    }

}
