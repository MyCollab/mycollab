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
package com.esofthead.mycollab.module.ecm;

import org.apache.commons.beanutils.PropertyUtils;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.utils.BeanUtility;
import com.esofthead.mycollab.module.ecm.domain.ExternalContent;
import com.esofthead.mycollab.module.ecm.domain.ExternalDrive;
import com.esofthead.mycollab.module.ecm.domain.ExternalFolder;
import com.esofthead.mycollab.module.ecm.domain.Resource;
import com.esofthead.mycollab.module.ecm.service.DropboxResourceService;
import com.esofthead.mycollab.module.ecm.service.ExternalResourceService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;

/**
 * Utility class of processing MyCollab resources.
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class ResourceUtils {


	/**
	 * 
	 * @param resourceType
	 * @return
	 */
	public static ExternalResourceService getExternalResourceService(
			ResourceType resourceType) {
		if (ResourceType.Dropbox == resourceType) {
			return ApplicationContextUtil
					.getSpringBean(DropboxResourceService.class);
		} else {
			throw new MyCollabException(
					"Current support only dropbox resource service");
		}
	}

	/**
	 * 
	 * @param resource
	 * @return
	 */
	public static ExternalDrive getExternalDrive(Resource resource) {
		if (resource instanceof ExternalFolder) {
			return ((ExternalFolder) resource).getExternalDrive();
		} else if (resource instanceof ExternalContent) {
			return ((ExternalContent) resource).getExternalDrive();
		}
		return null;
	}

	/**
	 * 
	 * @param resource
	 * @return
	 */
	public static ResourceType getType(Resource resource) {
		if (!resource.isExternalResource()) {
			return ResourceType.MyCollab;
		} else {
			try {
				String storageName = (String) PropertyUtils.getProperty(
						resource, "storageName");
				if (StorageNames.DROPBOX.equals(storageName)) {
					return ResourceType.Dropbox;
				} else {
					throw new Exception(
							"Current support only dropbox resource service");
				}
			} catch (Exception e) {
				throw new MyCollabException(
						"Can not define sotrage name of bean "
								+ BeanUtility.printBeanObj(resource));
			}
		}
	}
}
