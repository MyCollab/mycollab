/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.esofthead.mycollab.common.interceptor.aspect;

import java.util.GregorianCalendar;

import org.apache.commons.beanutils.PropertyUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Aspect
@Component
public class InjectTimeLoggingAspect {
	private static final Logger LOG = LoggerFactory.getLogger(InjectTimeLoggingAspect.class);

	@Before("execution(public * com.esofthead.mycollab..service..*.saveWithSession(..)) && args(bean, username)")
	public void injectDateForSaveMethod(JoinPoint joinPoint, Object bean, String username) {
		try {
			LOG.debug("Set createtime and lastupdatedtime if enable");
			PropertyUtils.setProperty(bean, "createdtime", new GregorianCalendar().getTime());
			PropertyUtils.setProperty(bean, "lastupdatedtime", new GregorianCalendar().getTime());
		} catch (Exception e) {
		}
	}

	@Before("(execution(public * com.esofthead.mycollab..service..*.updateWithSession(..)) || (execution(public * com.esofthead.mycollab..service..*.updateSelectiveWithSession(..))))  && args(bean, username)")
	public void injectDateForUpdateMethod(JoinPoint joinPoint, Object bean, String username) {
		try {
			LOG.debug("Set createtime and lastupdatedtime if enable");
			PropertyUtils.setProperty(bean, "lastupdatedtime", new GregorianCalendar().getTime());
		} catch (Exception e) {
		}
	}
}