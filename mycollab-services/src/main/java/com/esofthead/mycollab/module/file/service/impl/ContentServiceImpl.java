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
package com.esofthead.mycollab.module.file.service.impl;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esofthead.mycollab.module.file.service.ContentService;
import com.esofthead.mycollab.module.file.service.RawContentService;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	protected RawContentService rawContentService;

	@Override
	public void saveContent(Integer accountId, String objectPath,
			InputStream stream) {
		String newPath = ((accountId == null) ? "" : accountId + "/")
				+ objectPath;
		rawContentService.saveContent(newPath, stream);
	}

	@Override
	public InputStream getContent(Integer accountId, String objectPath) {
		String newPath = ((accountId == null) ? "" : accountId + "/")
				+ objectPath;
		return rawContentService.getContentStream(newPath);
	}

	@Override
	public void removeContent(Integer accountId, String objectPath) {
		String newPath = ((accountId == null) ? "" : accountId + "/")
				+ objectPath;
		rawContentService.removePath(newPath);
	}
}
