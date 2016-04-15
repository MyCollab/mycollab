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


import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultCrudService;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum;
import com.esofthead.mycollab.module.tracker.dao.RelatedBugMapper;
import com.esofthead.mycollab.module.tracker.dao.RelatedBugMapperExt;
import com.esofthead.mycollab.module.tracker.domain.RelatedBug;
import com.esofthead.mycollab.module.tracker.domain.RelatedBugExample;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.domain.SimpleRelatedBug;
import com.esofthead.mycollab.module.tracker.service.BugRelationService;
import com.esofthead.mycollab.module.tracker.service.BugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0.0
 */
@Service
public class BugRelationServiceImpl extends DefaultCrudService<Integer, RelatedBug> implements BugRelationService {
    @Autowired
    private RelatedBugMapper relatedBugMapper;

    @Autowired
    private RelatedBugMapperExt relatedBugMapperExt;

    @Autowired
    private BugService bugService;

    @Override
    public ICrudGenericDAO<Integer, RelatedBug> getCrudMapper() {
        return relatedBugMapper;
    }

    public Integer saveWithSession(RelatedBug record, String username) {
        Integer bugId = record.getBugid();
        if (OptionI18nEnum.BugRelation.Duplicated.name().equals(record.getRelatetype())) {
            SimpleBug bug = bugService.findById(bugId, 0);
            if (bug != null) {
                bug.setStatus(OptionI18nEnum.BugStatus.Resolved.name());
                bug.setResolution(OptionI18nEnum.BugRelation.Duplicated.name());
                bugService.updateSelectiveWithSession(bug, username);
            }
        }
        return super.saveWithSession(record, username);
    }

    @Override
    public int removeDuplicatedBugs(Integer bugId) {
        RelatedBugExample ex = new RelatedBugExample();
        ex.createCriteria().andBugidEqualTo(bugId).andRelatetypeEqualTo(OptionI18nEnum.BugRelation.Duplicated.name());
        return relatedBugMapper.deleteByExample(ex);
    }

    @Override
    public List<SimpleRelatedBug> findRelatedBugs(Integer bugId) {
        return relatedBugMapperExt.findRelatedBugs(bugId);
    }
}
