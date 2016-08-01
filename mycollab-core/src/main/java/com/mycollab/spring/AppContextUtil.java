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
package com.mycollab.spring;

import com.mycollab.core.MyCollabException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.Validator;

/**
 * Static spring application context to retrieve spring bean without in servlet
 * context
 *
 * @author MyCollab Ltd
 * @since 1.0.0
 */
@Component("appContextUtil")
public class AppContextUtil implements ApplicationContextAware {
    private static ApplicationContext ctx;

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        ctx = appContext;
    }

    public static <T> T getSpringBean(String name, Class<T> classType) {
        if (ctx == null) {
            throw new MyCollabException("Can not find service " + name);
        }
        try {
            return ctx.getBean(name, classType);
        } catch (Exception e) {
            throw new MyCollabException("Can not find service " + name);
        }
    }

    public static Validator getValidator() {
        return getSpringBean("validator", LocalValidatorFactoryBean.class);
    }

    public static <T> T getSpringBean(Class<T> classType) {
        if (ctx == null) {
            throw new MyCollabException("Can not find service " + classType);
        }
        try {
            return ctx.getBean(classType);
        } catch (Exception e) {
            throw new MyCollabException("Can not find service " + classType, e);
        }
    }
}
