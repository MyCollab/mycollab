/**
 * This file is part of mycollab-services-community.
 *
 * mycollab-services-community is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services-community is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services-community.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.community.module.file.service;

import com.esofthead.mycollab.cache.IgnoreCacheClass;
import com.esofthead.mycollab.core.persistence.service.IService;
import com.esofthead.mycollab.module.file.service.RawContentService;
import com.esofthead.mycollab.module.file.service.impl.FileRawContentServiceImpl;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.stereotype.Service;

/**
 * Factory spring bean to solve resolution of MyCollab raw content service
 * should be <code>FileRawContentServiceImpl</code> if MyCollab is installed in
 * local server (dev, community or premium mode) or
 * <code>AmazonRawContentServiceImpl</code> if MyCollab is installed on MyCollab
 * server.
 * 
 */
@Service(value = "rawContentService")
@IgnoreCacheClass
public class RawContentServiceFactoryBean extends
		AbstractFactoryBean<RawContentService> implements IService {

	@Override
	protected RawContentService createInstance() throws Exception {
		return new FileRawContentServiceImpl();

	}

	@Override
	public Class<RawContentService> getObjectType() {
		return RawContentService.class;
	}

}
