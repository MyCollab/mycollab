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
package com.mycollab.module.crm;

import com.mycollab.core.MyCollabException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class CrmResources {
    private static Logger LOG = LoggerFactory.getLogger(CrmResources.class);
    private static Method getResMethod;

    static {
        try {
            Class<?> resourceCls = Class.forName("com.mycollab.module.crm.ui.CrmAssetsManager");
            getResMethod = resourceCls.getMethod("toHexString", String.class);
        } catch (Exception e) {
            throw new MyCollabException("Can not reload resource", e);
        }
    }

    public static String getFontIconHtml(String type) {
        try {
            String codePoint = (String) getResMethod.invoke(null, type);
            return String.format("<span class=\"v-icon\" style=\"font-family: FontAwesome;\">%s;</span>", codePoint);
        } catch (Exception e) {
            LOG.error("Can not get resource type {}", type);
            return "";
        }
    }
}
