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

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.beanutils.PropertyUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esofthead.mycollab.common.ActivityStreamConstants;
import com.esofthead.mycollab.common.domain.ActivityStream;
import com.esofthead.mycollab.common.service.ActivityStreamService;
import com.esofthead.mycollab.core.utils.DateTimeUtils;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Aspect
@Component
public class TraceableAspect {

	private static Logger log = LoggerFactory.getLogger(TraceableAspect.class);
	@Autowired
	private ActivityStreamService activityStreamService;

	@AfterReturning("execution(public * com.esofthead.mycollab..service..*.saveWithSession(..)) && args(bean, username)")
	public void traceSaveActivity(JoinPoint joinPoint, Object bean,
			String username) {

		Advised advised = (Advised) joinPoint.getThis();
		Class<?> cls = advised.getTargetSource().getTargetClass();

		Traceable traceableAnnotation = cls.getAnnotation(Traceable.class);
		if (traceableAnnotation != null) {
			try {
				ActivityStream activity = constructActivity(
						traceableAnnotation, bean, username,
						ActivityStreamConstants.ACTION_CREATE);
				activityStreamService.save(activity);
			} catch (Exception e) {
				log.error(
						"Error when save activity for save action of service "
								+ cls.getName(), e);
			}
		}

	}

	static ActivityStream constructActivity(Traceable traceableAnnotation,
			Object bean, String username, String action)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {

		ActivityStream activity = new ActivityStream();
		activity.setModule(traceableAnnotation.module());
		activity.setType(traceableAnnotation.type());
		activity.setTypeid((Integer) PropertyUtils.getProperty(bean,
				traceableAnnotation.idField()));
		activity.setCreatedtime(new GregorianCalendar().getTime());
		activity.setAction(action);
		activity.setSaccountid((Integer) PropertyUtils.getProperty(bean,
				"saccountid"));
		activity.setCreateduser(username);

		Object nameObj = PropertyUtils.getProperty(bean,
				traceableAnnotation.nameField());
		String nameField = "";
		if (nameObj instanceof Date) {
			nameField = DateTimeUtils.formatDate((Date) nameObj, "MM/dd/yyyy");
		} else {
			nameField = nameObj.toString();
		}
		activity.setNamefield(nameField);

		if (!"".equals(traceableAnnotation.extraFieldName())) {
			Integer extraTypeId = (Integer) PropertyUtils.getProperty(bean,
					traceableAnnotation.extraFieldName());
			activity.setExtratypeid(extraTypeId);
		}
		return activity;
	}
}
