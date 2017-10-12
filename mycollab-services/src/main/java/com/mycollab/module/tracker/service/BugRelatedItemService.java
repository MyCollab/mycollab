/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.tracker.service;

import com.mycollab.cache.IgnoreCacheClass;
import com.mycollab.db.persistence.service.IService;
import com.mycollab.module.tracker.domain.Component;
import com.mycollab.module.tracker.domain.Version;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@IgnoreCacheClass
public interface BugRelatedItemService extends IService {

    void saveAffectedVersionsOfBug(Integer bugId, List<Version> versions);

    void saveFixedVersionsOfBug(Integer bugId, List<Version> versions);

    void saveComponentsOfBug(Integer bugId, List<Component> components);

    void updateAffectedVersionsOfBug(Integer bugId, List<Version> versions);

    void updateFixedVersionsOfBug(Integer bugId, List<Version> versions);

    void updateComponentsOfBug(Integer bugId, List<Component> components);
}
