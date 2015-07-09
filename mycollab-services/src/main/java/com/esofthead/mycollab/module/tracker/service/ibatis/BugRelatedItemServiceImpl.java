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

package com.esofthead.mycollab.module.tracker.service.ibatis;

import com.esofthead.mycollab.module.tracker.dao.BugRelatedItemMapper;
import com.esofthead.mycollab.module.tracker.domain.BugRelatedItem;
import com.esofthead.mycollab.module.tracker.domain.BugRelatedItemExample;
import com.esofthead.mycollab.module.tracker.domain.Component;
import com.esofthead.mycollab.module.tracker.domain.Version;
import com.esofthead.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.esofthead.mycollab.module.tracker.service.BugRelatedItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
public class BugRelatedItemServiceImpl implements BugRelatedItemService {
    @Autowired
    private BugRelatedItemMapper bugRelatedItemMapper;

    @Override
    public void saveAffectedVersionsOfBug(Integer bugid, List<Version> versions) {
        insertAffectedVersionsOfBug(bugid, versions);
    }

    private void insertAffectedVersionsOfBug(Integer bugid, List<Version> versions) {
        for (Version version : versions) {
            BugRelatedItem relatedItem = new BugRelatedItem();
            relatedItem.setBugid(bugid);
            relatedItem.setTypeid(version.getId());
            relatedItem.setType(BugSearchCriteria.AFFVERSION);
            bugRelatedItemMapper.insert(relatedItem);
        }
    }

    @Override
    public void saveFixedVersionsOfBug(Integer bugid, List<Version> versions) {
        insertFixedVersionsOfBug(bugid, versions);
    }

    private void insertFixedVersionsOfBug(Integer bugid, List<Version> versions) {
        for (Version version : versions) {
            BugRelatedItem relatedItem = new BugRelatedItem();
            relatedItem.setBugid(bugid);
            relatedItem.setTypeid(version.getId());
            relatedItem.setType(BugSearchCriteria.FIXVERSION);
            bugRelatedItemMapper.insert(relatedItem);
        }
    }

    @Override
    public void saveComponentsOfBug(Integer bugid, List<Component> components) {
        insertComponentsOfBug(bugid, components);
    }

    public void insertComponentsOfBug(Integer bugid, List<Component> components) {
        for (Component component : components) {
            BugRelatedItem relatedItem = new BugRelatedItem();
            relatedItem.setBugid(bugid);
            relatedItem.setTypeid(component.getId());
            relatedItem.setType(BugSearchCriteria.COMPONENT);
            bugRelatedItemMapper.insert(relatedItem);
        }
    }

    private void deleteTrackerBugRelatedItem(Integer bugid, String type) {
        BugRelatedItemExample ex = new BugRelatedItemExample();
        ex.createCriteria().andBugidEqualTo(bugid).andTypeEqualTo(type);

        bugRelatedItemMapper.deleteByExample(ex);
    }


    @Override
    public void updateAfftedVersionsOfBug(Integer bugid, List<Version> versions) {
        deleteTrackerBugRelatedItem(bugid, BugSearchCriteria.AFFVERSION);
        if (versions.size() > 0) {
            insertAffectedVersionsOfBug(bugid, versions);
        }
    }

    @Override
    public void updateFixedVersionsOfBug(Integer bugid, List<Version> versions) {
        deleteTrackerBugRelatedItem(bugid, BugSearchCriteria.FIXVERSION);
        if (versions.size() > 0) {
            insertFixedVersionsOfBug(bugid, versions);
        }
    }

    @Override
    public void updateComponentsOfBug(Integer bugid, List<Component> components) {
        deleteTrackerBugRelatedItem(bugid, BugSearchCriteria.COMPONENT);
        if (components.size() > 0) {
            insertComponentsOfBug(bugid, components);
        }
    }

}
