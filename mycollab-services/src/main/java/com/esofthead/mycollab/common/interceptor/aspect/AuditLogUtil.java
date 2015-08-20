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

import com.esofthead.mycollab.common.domain.AuditChangeItem;
import com.esofthead.mycollab.core.utils.JsonDeSerializer;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.1.2
 */
public class AuditLogUtil {
    private static Logger LOG = LoggerFactory.getLogger(AuditLogUtil.class);

    static public String getChangeSet(Object oldObj, Object newObj, List<String> excludeFields) {
        Class cl = oldObj.getClass();
        List<AuditChangeItem> changeItems = new ArrayList<>();

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(cl, Object.class);

            for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
                String fieldName = propertyDescriptor.getName();
                if (excludeFields.contains(fieldName)) {
                    continue;
                }
                String oldProp = getValue(PropertyUtils.getProperty(oldObj, fieldName));

                Object newPropVal;
                try {
                    newPropVal = PropertyUtils.getProperty(newObj, fieldName);
                } catch (Exception e) {
                    continue;
                }
                String newProp = getValue(newPropVal);

                if (!oldProp.equals(newProp)) {
                    AuditChangeItem changeItem = new AuditChangeItem();
                    changeItem.setField(fieldName);
                    changeItem.setNewvalue(newProp);
                    changeItem.setOldvalue(oldProp);
                    changeItems.add(changeItem);
                }
            }
        } catch (Exception e) {
            LOG.error("There is error when convert changeset", e);
            return null;
        }

        return (changeItems.size() > 0) ? JsonDeSerializer.toJson(changeItems) : null;
    }

    private static String getValue(Object obj) {
        if (obj != null) {
            if (obj instanceof Date) {
                return formatDateW3C((Date) obj);
            } else {
                return obj.toString();
            }
        } else {
            return "";
        }
    }

    static private String formatDateW3C(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        String text = df.format(date);
        return text.substring(0, 22) + ":" + text.substring(22);
    }
}
