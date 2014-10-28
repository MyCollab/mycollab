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
package com.esofthead.mycollab.esb;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.ProxyBuilder;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.spring.ApplicationContextUtil;

/**
 * Utility class to bind a spring service bean with endpoints in integration
 * pattern.
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class CamelProxyBuilderUtil {
	private CamelProxyBuilderUtil() {
	}

	public static <S> S build(String endpoint, Class<S> buildCls) {
		try {
			CamelContext camelContext = ApplicationContextUtil
					.getSpringBean(CamelContext.class);
			return new ProxyBuilder(camelContext).endpoint(endpoint).build(
					buildCls);
		} catch (Exception e) {
			throw new MyCollabException(e);
		}
	}
}
