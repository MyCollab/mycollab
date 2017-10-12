/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.aspect;

import com.mycollab.common.MonitorTypeConstants;
import com.mycollab.common.domain.MonitorItem;
import com.mycollab.common.domain.RelayEmailNotificationWithBLOBs;
import com.mycollab.common.service.MonitorItemService;
import com.mycollab.common.service.RelayEmailNotificationService;
import org.apache.commons.beanutils.PropertyUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import java.util.GregorianCalendar;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Aspect
@Component
@Configurable
public class MonitorItemAspect {
    private static final Logger LOG = LoggerFactory.getLogger(MonitorItemAspect.class);

    @Autowired
    private MonitorItemService monitorItemService;

    @Autowired
    private RelayEmailNotificationService relayEmailNotificationService;

    @AfterReturning("execution(public * com.mycollab..service..*.saveWithSession(..)) && args(bean, username)")
    public void traceSaveActivity(JoinPoint joinPoint, Object bean, String username) {
        Advised advised = (Advised) joinPoint.getThis();
        Class<?> cls = advised.getTargetSource().getTargetClass();
        try {
            Watchable watchableAnnotation = cls.getAnnotation(Watchable.class);
            if (watchableAnnotation != null) {
                int sAccountId = (Integer) PropertyUtils.getProperty(bean, "saccountid");
                int typeId = (Integer) PropertyUtils.getProperty(bean, "id");
                Integer extraTypeId = null;
                if (!"".equals(watchableAnnotation.extraTypeId())) {
                    extraTypeId = (Integer) PropertyUtils.getProperty(bean, watchableAnnotation.extraTypeId());
                }

                MonitorItem monitorItem = new MonitorItem();
                monitorItem.setMonitorDate(new GregorianCalendar().getTime());
                monitorItem.setType(ClassInfoMap.getType(cls));
                monitorItem.setTypeid(typeId);
                monitorItem.setExtratypeid(extraTypeId);
                monitorItem.setUser(username);
                monitorItem.setSaccountid(sAccountId);

                monitorItemService.saveWithSession(monitorItem, username);

                if (!watchableAnnotation.userFieldName().equals("")) {
                    String moreUser = (String) PropertyUtils.getProperty(bean, watchableAnnotation.userFieldName());
                    if (moreUser != null && !moreUser.equals(username)) {
                        monitorItem.setId(null);
                        monitorItem.setUser(moreUser);
                        monitorItemService.saveWithSession(monitorItem, moreUser);
                    }
                }
            }

            Traceable traceAnnotation = cls.getAnnotation(Traceable.class);
            if (traceAnnotation != null) {
                int sAccountId = (Integer) PropertyUtils.getProperty(bean, "saccountid");
                int typeId = (Integer) PropertyUtils.getProperty(bean, "id");
                RelayEmailNotificationWithBLOBs relayNotification = new RelayEmailNotificationWithBLOBs();
                relayNotification.setChangeby(username);
                relayNotification.setChangecomment("");
                relayNotification.setSaccountid(sAccountId);
                relayNotification.setType(ClassInfoMap.getType(cls));
                relayNotification.setAction(MonitorTypeConstants.CREATE_ACTION);
                relayNotification.setTypeid("" + typeId);
                relayEmailNotificationService.saveWithSession(relayNotification, username);
                // Save notification item
            }
        } catch (Exception e) {
            LOG.error("Error when save relay email notification for save action of service " + cls.getName(), e);
        }
    }
}
