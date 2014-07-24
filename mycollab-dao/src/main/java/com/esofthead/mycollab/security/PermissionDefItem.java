/**
 * This file is part of mycollab-dao.
 *
 * mycollab-dao is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-dao is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-dao.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.security;

/**
 * Permission item
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class PermissionDefItem {
	private String key;

	/**
	 * Display name of permission
	 */
	private String caption;

	/**
	 * Permission flag of permission
	 */
	private Class<? extends PermissionFlag> permissionCls;

	public PermissionDefItem(String key, String caption,
			Class<? extends PermissionFlag> permissionCls) {
		this.key = key;
		this.caption = caption;
		this.permissionCls = permissionCls;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public Class<? extends PermissionFlag> getPermissionCls() {
		return permissionCls;
	}

	public void setPermissionCls(Class<? extends PermissionFlag> permissionCls) {
		this.permissionCls = permissionCls;
	}
}
