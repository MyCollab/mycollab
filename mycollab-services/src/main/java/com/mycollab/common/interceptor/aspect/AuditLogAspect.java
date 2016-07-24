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
package com.mycollab.common.interceptor.aspect;

import com.mycollab.cache.service.CacheService;
import com.mycollab.common.ActivityStreamConstants;
import com.mycollab.common.MonitorTypeConstants;
import com.mycollab.common.domain.ActivityStreamWithBLOBs;
import com.mycollab.common.domain.AuditLog;
import com.mycollab.common.domain.MonitorItem;
import com.mycollab.common.domain.RelayEmailNotificationWithBLOBs;
import com.mycollab.common.service.ActivityStreamService;
import com.mycollab.common.service.AuditLogService;
import com.mycollab.common.service.MonitorItemService;
import com.mycollab.common.service.RelayEmailNotificationService;
import com.mycollab.core.utils.BeanUtility;
import org.apache.commons.beanutils.PropertyUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Aspect
@Component
@Configurable
public class AuditLogAspect {
    private static final Logger LOG = LoggerFactory.getLogger(AuditLogAspect.class);

    private static final String AUDIT_TEMP_CACHE = "AUDIT_TEMP_CACHE";

    @Autowired
    private CacheService cacheService;

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private ActivityStreamService activityStreamService;

    @Autowired
    private MonitorItemService monitorItemService;

    @Autowired
    private RelayEmailNotificationService relayEmailNotificationService;

    @Before("(execution(public * com.mycollab..service..*.updateWithSession(..)) || (execution(public * com.mycollab..service..*.updateSelectiveWithSession(..)))) && args(bean, username)")
    public void traceBeforeUpdateActivity(JoinPoint joinPoint, Object bean, String username) {
        Advised advised = (Advised) joinPoint.getThis();
        Class<?> cls = advised.getTargetSource().getTargetClass();

        Traceable auditAnnotation = cls.getAnnotation(Traceable.class);
        if (auditAnnotation != null) {
            try {
                Integer typeId = (Integer) PropertyUtils.getProperty(bean, "id");
                Integer sAccountId = (Integer) PropertyUtils.getProperty(bean, "saccountid");
                // store old value to map, wait until the update process
                // successfully then add to log item

                // get old value
                Object service = advised.getTargetSource().getTarget();
                Method findMethod;
                Object oldValue;
                try {
                    findMethod = cls.getMethod("findById", Integer.class, Integer.class);
                } catch (Exception e) {
                    findMethod = cls.getMethod("findByPrimaryKey", Integer.class, Integer.class);
                }
                oldValue = findMethod.invoke(service, typeId, sAccountId);
                String key = bean.toString() + ClassInfoMap.getType(cls) + typeId;

                cacheService.putValue(AUDIT_TEMP_CACHE, key, oldValue);
            } catch (Exception e) {
                LOG.error("Error when save audit for save action of service " + cls.getName(), e);
            }
        }
    }

    @AfterReturning("(execution(public * com.mycollab..service..*.updateWithSession(..)) || (execution(public * com.mycollab..service..*.updateSelectiveWithSession(..))))  && args(bean, username)")
    public void traceAfterUpdateActivity(JoinPoint joinPoint, Object bean, String username) {
        Advised advised = (Advised) joinPoint.getThis();
        Class<?> cls = advised.getTargetSource().getTargetClass();
        boolean isSelective = "updateSelectiveWithSession".equals(joinPoint.getSignature().getName());

        try {
            Watchable watchableAnnotation = cls.getAnnotation(Watchable.class);
            if (watchableAnnotation != null) {
                String monitorType = ClassInfoMap.getType(cls);
                Integer sAccountId = (Integer) PropertyUtils.getProperty(bean, "saccountid");
                int typeId = (Integer) PropertyUtils.getProperty(bean, "id");

                Integer extraTypeId = null;
                if (!"".equals(watchableAnnotation.extraTypeId())) {
                    extraTypeId = (Integer) PropertyUtils.getProperty(bean, watchableAnnotation.extraTypeId());
                }

                MonitorItem monitorItem = new MonitorItem();
                monitorItem.setMonitorDate(new GregorianCalendar().getTime());
                monitorItem.setType(monitorType);
                monitorItem.setTypeid(typeId);
                monitorItem.setExtratypeid(extraTypeId);
                monitorItem.setUser(username);
                monitorItem.setSaccountid(sAccountId);
                monitorItemService.saveWithSession(monitorItem, username);

                // check whether the current user is in monitor list, if not add him in
                if (!watchableAnnotation.userFieldName().equals("")) {
                    String moreUser = (String) PropertyUtils.getProperty(bean, watchableAnnotation.userFieldName());
                    if (moreUser != null && !moreUser.equals(username)) {
                        monitorItem.setId(null);
                        monitorItem.setUser(moreUser);
                        monitorItemService.saveWithSession(monitorItem, moreUser);
                    }
                }
            }

            Traceable traceableAnnotation = cls.getAnnotation(Traceable.class);
            if (traceableAnnotation != null) {
                try {
                    ClassInfo classInfo = ClassInfoMap.getClassInfo(cls);
                    String changeSet = getChangeSet(cls, bean, classInfo.getExcludeHistoryFields(),isSelective);
                    if (changeSet != null) {
                        ActivityStreamWithBLOBs activity = TraceableCreateAspect.constructActivity(cls,
                                traceableAnnotation, bean, username, ActivityStreamConstants.ACTION_UPDATE);
                        Integer activityStreamId = activityStreamService.save(activity);

                        Integer sAccountId = (Integer) PropertyUtils.getProperty(bean, "saccountid");
                        Integer auditLogId = saveAuditLog(cls, bean, changeSet, username, sAccountId, activityStreamId);

                        Integer typeId = (Integer) PropertyUtils.getProperty(bean, "id");
                        // Save notification email
                        RelayEmailNotificationWithBLOBs relayNotification = new RelayEmailNotificationWithBLOBs();
                        relayNotification.setChangeby(username);
                        relayNotification.setChangecomment("");
                        relayNotification.setSaccountid(sAccountId);
                        relayNotification.setType(ClassInfoMap.getType(cls));
                        relayNotification.setTypeid("" + typeId);
                        if (auditLogId != null) {
                            relayNotification.setExtratypeid(auditLogId);
                        }
                        relayNotification.setAction(MonitorTypeConstants.UPDATE_ACTION);

                        relayEmailNotificationService.saveWithSession(relayNotification, username);
                    }
                } catch (Exception e) {
                    LOG.error("Error when save activity for save action of service " + cls.getName(), e);
                }
            }
        } catch (Exception e) {
            LOG.error("Error when save audit for save action of service "
                    + cls.getName() + "and bean: " + BeanUtility.printBeanObj(bean), e);
        }
    }

    private String getChangeSet(Class<?> targetCls, Object bean, List<String> excludeHistoryFields, boolean isSelective) {
        try {
            Integer typeId = (Integer) PropertyUtils.getProperty(bean, "id");
            String key = bean.toString() + ClassInfoMap.getType(targetCls) + typeId;

            Object oldValue = cacheService.getValue(AUDIT_TEMP_CACHE, key);
            if (oldValue != null) {
                return AuditLogUtil.getChangeSet(oldValue, bean, excludeHistoryFields, isSelective);
            }
            return null;
        } catch (Exception e) {
            LOG.error("Error while generate changeset", e);
            return null;
        }
    }

    private Integer saveAuditLog(Class<?> targetCls, Object bean, String changeSet, String username, Integer sAccountId,
                                 Integer activityStreamId) {
        try {
            Integer typeId = (Integer) PropertyUtils.getProperty(bean, "id");
            AuditLog auditLog = new AuditLog();
            auditLog.setPosteduser(username);
            auditLog.setModule(ClassInfoMap.getModule(targetCls));
            auditLog.setType(ClassInfoMap.getType(targetCls));
            auditLog.setTypeid(typeId);
            auditLog.setSaccountid(sAccountId);
            auditLog.setPosteddate(new GregorianCalendar().getTime());
            auditLog.setChangeset(changeSet);
            auditLog.setObjectClass(bean.getClass().getName());
            if (activityStreamId != null) {
                auditLog.setActivitylogid(activityStreamId);
            }

            return auditLogService.saveWithSession(auditLog, "");
        } catch (Exception e) {
            LOG.error("Error when save audit for save action of service "
                    + targetCls.getName() + "and bean: " + BeanUtility.printBeanObj(bean)
                    + " and changeset is " + changeSet, e);
            return null;
        }
    }
}
