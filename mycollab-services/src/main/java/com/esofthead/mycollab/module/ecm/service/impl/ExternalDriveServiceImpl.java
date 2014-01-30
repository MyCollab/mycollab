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
package com.esofthead.mycollab.module.ecm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultCrudService;
import com.esofthead.mycollab.module.ecm.dao.ExternalDriveMapper;
import com.esofthead.mycollab.module.ecm.domain.ExternalDrive;
import com.esofthead.mycollab.module.ecm.domain.ExternalDriveExample;
import com.esofthead.mycollab.module.ecm.service.ExternalDriveService;

@Service
public class ExternalDriveServiceImpl extends
		DefaultCrudService<Integer, ExternalDrive> implements
		ExternalDriveService {

	@Autowired
	private ExternalDriveMapper externalDriveMapper;

	@Override
	public ICrudGenericDAO<Integer, ExternalDrive> getCrudMapper() {
		return externalDriveMapper;
	}

	@Override
	public List<ExternalDrive> getExternalDrivesOfUser(String username) {
		ExternalDriveExample ex = new ExternalDriveExample();
		ex.createCriteria().andOwnerEqualTo(username);
		return externalDriveMapper.selectByExample(ex);
	}

}
