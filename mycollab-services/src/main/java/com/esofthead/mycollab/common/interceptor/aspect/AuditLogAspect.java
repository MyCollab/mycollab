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

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.GregorianCalendar;

import org.apache.commons.beanutils.PropertyUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.infinispan.commons.api.BasicCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import com.esofthead.mycollab.cache.LocalCacheManager;
import com.esofthead.mycollab.common.ActivityStreamConstants;
import com.esofthead.mycollab.common.MonitorTypeConstants;
import com.esofthead.mycollab.common.domain.ActivityStream;
import com.esofthead.mycollab.common.domain.AuditLog;
import com.esofthead.mycollab.common.domain.MonitorItem;
import com.esofthead.mycollab.common.domain.RelayEmailNotification;
import com.esofthead.mycollab.common.service.ActivityStreamService;
import com.esofthead.mycollab.common.service.AuditLogService;
import com.esofthead.mycollab.common.service.MonitorItemService;
import com.esofthead.mycollab.common.service.RelayEmailNotificationService;
import com.esofthead.mycollab.common.service.ibatis.AuditLogServiceImpl.AuditLogUtil;
import com.esofthead.mycollab.core.utils.BeanUtility;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Aspect
@Component
@Configurable
public class AuditLogAspect {

	private static final Logger LOG = LoggerFactory.getLogger(AuditLogAspect.class);
	private static BasicCache<Object, Object> caches = LocalCacheManager
			.getCache();

	@Autowired
	protected AuditLogService auditLogService;

	@Autowired
	private ActivityStreamService activityStreamService;

	@Autowired
	protected MonitorItemService monitorItemService;

	@Autowired
	private RelayEmailNotificationService relayEmailNotificationService;

	@Before("(execution(public * com.esofthead.mycollab..service..*.updateWithSession(..)) || (execution(public * com.esofthead.mycollab..service..*.updateSelectiveWithSession(..)))) && args(bean, username)")
	public void traceBeforeUpdateActivity(JoinPoint joinPoint, Object bean,
			String username) {

		Advised advised = (Advised) joinPoint.getThis();
		Class<?> cls = advised.getTargetSource().getTargetClass();

		Auditable auditAnnotation = cls.getAnnotation(Auditable.class);
		if (auditAnnotation != null) {
			try {
				int typeid = (Integer) PropertyUtils.getProperty(bean, "id");
				int sAccountId = (Integer) PropertyUtils.getProperty(bean,
						"saccountid");
				// store old value to map, wait until the update process
				// successfully then add to log item

				// get old value
				Object service = advised.getTargetSource().getTarget();
				Method findMethod = null;
				Object oldValue = null;
				try {
					findMethod = cls.getMethod("findById", new Class[] {
							int.class, int.class });
				} catch (Exception e) {
					findMethod = cls.getMethod("findByPrimaryKey", new Class[] {
							Serializable.class, int.class, int.class });
				}
				oldValue = findMethod.invoke(service, typeid, sAccountId);
				String key = bean.toString() + auditAnnotation.type() + typeid;

				caches.put(key, oldValue);
			} catch (Exception e) {
				LOG.error("Error when save audit for save action of service "
						+ cls.getName(), e);
			}
		}
	}

	@AfterReturning("(execution(public * com.esofthead.mycollab..service..*.updateWithSession(..)) || (execution(public * com.esofthead.mycollab..service..*.updateSelectiveWithSession(..))))  && args(bean, username)")
	public void traceAfterUpdateActivity(JoinPoint joinPoint, Object bean,
			String username) {

		Advised advised = (Advised) joinPoint.getThis();
		Class<?> cls = advised.getTargetSource().getTargetClass();

		Traceable traceableAnnotation = cls.getAnnotation(Traceable.class);
		Integer activityStreamId = null;
		if (traceableAnnotation != null) {
			try {
				ActivityStream activity = TraceableAspect.constructActivity(
						traceableAnnotation, bean, username,
						ActivityStreamConstants.ACTION_UPDATE);
				activityStreamId = activityStreamService.save(activity);
			} catch (Exception e) {
				LOG.error(
						"Error when save activity for save action of service "
								+ cls.getName(), e);
			}
		}

		int sAccountId;
		try {
			sAccountId = (Integer) PropertyUtils
					.getProperty(bean, "saccountid");
		} catch (IllegalAccessException | InvocationTargetException
				| NoSuchMethodException e) {
			return;
		}

		Integer auditLogId = saveAuditLog(cls, bean, username, sAccountId,
				activityStreamId);

		Watchable watchableAnnotation = cls.getAnnotation(Watchable.class);
		if (watchableAnnotation != null) {
			try {
				int monitorTypeId = (Integer) PropertyUtils.getProperty(bean,
						"id");
				String monitorType = watchableAnnotation.type();

				Integer extraTypeId = null;
				if (!"".equals(watchableAnnotation.extraTypeId())) {
					extraTypeId = (Integer) PropertyUtils.getProperty(bean,
							watchableAnnotation.extraTypeId());
				}

				MonitorItem monitorItem = new MonitorItem();
				monitorItem.setMonitorDate(new GregorianCalendar().getTime());
				monitorItem.setType(monitorType);
				monitorItem.setTypeid(monitorTypeId);
				monitorItem.setExtratypeid(extraTypeId);
				monitorItem.setUser(username);
				monitorItem.setSaccountid(sAccountId);

				if (!monitorItemService.isUserWatchingItem(username,
						monitorType, monitorTypeId)) {
					monitorItemService.saveWithSession(monitorItem, username);
				}

				// check whether the current user is in monitor list, if
				// not add him in
				if (!watchableAnnotation.userFieldName().equals("")) {
					String moreUser = (String) PropertyUtils.getProperty(bean,
							watchableAnnotation.userFieldName());
					if (moreUser != null && !moreUser.equals(username)) {
						monitorItem.setId(null);
						monitorItem.setUser(moreUser);
						if (!monitorItemService.isUserWatchingItem(moreUser,
								monitorType, monitorTypeId)) {
							monitorItemService.saveWithSession(monitorItem,
									moreUser);
						}
					}
				}

				// Save notification email
				RelayEmailNotification relayNotification = new RelayEmailNotification();
				relayNotification.setChangeby(username);
				relayNotification.setChangecomment("");
				relayNotification.setSaccountid(sAccountId);
				relayNotification.setType(monitorType);
				relayNotification.setTypeid("" + monitorTypeId);
				relayNotification.setEmailhandlerbean(watchableAnnotation
						.emailHandlerBean().getName());
				if (auditLogId != null) {
					relayNotification.setExtratypeid(auditLogId);
				}

				relayNotification.setAction(MonitorTypeConstants.UPDATE_ACTION);

				relayEmailNotificationService.saveWithSession(
						relayNotification, username);
			} catch (Exception e) {
				LOG.error(
						"Error when save audit for save action of service "
								+ cls.getName() + "and bean: "
								+ BeanUtility.printBeanObj(bean), e);
			}
		}
	}

	private Integer saveAuditLog(Class<?> targetCls, Object bean,
			String username, Integer sAccountId, Integer activityStreamId) {
		Auditable auditAnnotation = targetCls.getAnnotation(Auditable.class);
		if (auditAnnotation != null) {
			String key = null;
			String changeSet = "";
			try {

				int typeid = (Integer) PropertyUtils.getProperty(bean, "id");
				key = bean.toString() + auditAnnotation.type() + typeid;

				Object oldValue = caches.get(key);
				if (oldValue != null) {
					AuditLog auditLog = new AuditLog();
					auditLog.setPosteduser(username);
					auditLog.setModule(auditAnnotation.module());
					auditLog.setType(auditAnnotation.type());
					auditLog.setTypeid(typeid);
					auditLog.setSaccountid(sAccountId);
					auditLog.setPosteddate(new GregorianCalendar().getTime());
					changeSet = AuditLogUtil.getChangeSet(oldValue, bean);
					auditLog.setChangeset(changeSet);
					auditLog.setObjectClass(oldValue.getClass().getName());
					if (activityStreamId != null) {
						auditLog.setActivitylogid(activityStreamId);
					}

					return auditLogService.saveWithSession(auditLog, "");
				}
			} catch (Exception e) {
				LOG.error(
						"Error when save audit for save action of service "
								+ targetCls.getName() + "and bean: "
								+ BeanUtility.printBeanObj(bean)
								+ " and changeset is " + changeSet, e);
				return null;
			}
		}

		return null;
	}
}
