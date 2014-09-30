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
package com.esofthead.mycollab.vaadin.ui;

import com.esofthead.mycollab.common.ui.components.AbstractCloudDriveOAuthWindow;
import com.esofthead.mycollab.core.MyCollabException;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.5.2
 *
 */
public class ComponentManagerFactory {
	private static final String oauthWindowImplCls[] = new String[] {
			"com.esofthead.mycollab.ondemand.module.file.view",
			"com.esofthead.mycollab.module.file.view.DropBoxOAuthWindow" };

	@SuppressWarnings("unchecked")
	public static AbstractCloudDriveOAuthWindow getCloudDriveOAuthWindow(
			String title) {
		for (String clsName : oauthWindowImplCls) {

			Class<AbstractCloudDriveOAuthWindow> cls;
			try {
				cls = (Class<AbstractCloudDriveOAuthWindow>) Class
						.forName(clsName);
			} catch (ClassNotFoundException e) {
				continue;
			}
			if (cls != null) {
				try {
					AbstractCloudDriveOAuthWindow newInstance = cls
							.newInstance();
					newInstance.setCaption(title);
					return newInstance;
				} catch (Exception e) {
					throw new MyCollabException(e);
				}
			}
		}

		throw new MyCollabException("Can not create the oauth window");
	}
}
