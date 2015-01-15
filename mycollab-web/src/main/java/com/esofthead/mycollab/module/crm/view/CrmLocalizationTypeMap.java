/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.crm.view;

import java.util.HashMap;
import java.util.Map;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.i18n.CrmTypeI18nEnum;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
class CrmLocalizationTypeMap {
	private static Map<String, CrmTypeI18nEnum> typeMap;

	static {
		typeMap = new HashMap<>();
		typeMap.put(CrmTypeConstants.ACCOUNT, CrmTypeI18nEnum.ACCOUNT);
		typeMap.put(CrmTypeConstants.CALL, CrmTypeI18nEnum.CALL);
		typeMap.put(CrmTypeConstants.CAMPAIGN, CrmTypeI18nEnum.CAMPAIGN);
		typeMap.put(CrmTypeConstants.CASE, CrmTypeI18nEnum.CASES);
		typeMap.put(CrmTypeConstants.CONTACT, CrmTypeI18nEnum.CONTACT);
		typeMap.put(CrmTypeConstants.LEAD, CrmTypeI18nEnum.LEAD);
		typeMap.put(CrmTypeConstants.MEETING, CrmTypeI18nEnum.MEETING);
		typeMap.put(CrmTypeConstants.OPPORTUNITY, CrmTypeI18nEnum.OPPORTUNITY);
		typeMap.put(CrmTypeConstants.TASK, CrmTypeI18nEnum.TASK);
	}

	public static CrmTypeI18nEnum getType(String key) {
		CrmTypeI18nEnum result = typeMap.get(key);
		if (result == null) {
			throw new MyCollabException("CAN NOT GET VALUE FOR KEY: " + key);
		}

		return result;
	}
}
