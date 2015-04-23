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
package com.esofthead.mycollab.common.domain;

import java.util.ArrayList;
import java.util.List;

import com.esofthead.mycollab.core.utils.JsonDeSerializer;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class SimpleAuditLog extends AuditLog {
	private static final long serialVersionUID = 1L;

	private List<AuditChangeItem> changeItems;

	private String postedUserFullName;

	private String postedUserAvatarId;

	public List<AuditChangeItem> getChangeItems() {
		if (changeItems == null) {
			System.out.println("CHANGESET: " + getChangeset() + "---" + getType());
			changeItems = parseChangeItems();
		}
		if (changeItems == null) {
			changeItems = new ArrayList<>();
		}
		return changeItems;
	}

	public String getPostedUserFullName() {
		if (StringUtils.isBlank(postedUserFullName)) {
			return com.esofthead.mycollab.core.utils.StringUtils.extractNameFromEmail(getPosteduser());
		}
		return postedUserFullName;
	}

	public void setPostedUserFullName(String postedUserFullName) {
		this.postedUserFullName = postedUserFullName;
	}

	public String getPostedUserAvatarId() {
		return postedUserAvatarId;
	}

	public void setPostedUserAvatarId(String postedUserAvatarId) {
		this.postedUserAvatarId = postedUserAvatarId;
	}

	private List<AuditChangeItem> parseChangeItems() {
		List<AuditChangeItem> items = JsonDeSerializer.fromJson(
				this.getChangeset(), new TypeToken<List<AuditChangeItem>>() {
				}.getType());

		return items;
	}
}
