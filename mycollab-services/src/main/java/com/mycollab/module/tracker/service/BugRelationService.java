package com.mycollab.module.tracker.service;

import com.mycollab.cache.IgnoreCacheClass;
import com.mycollab.db.persistence.service.ICrudService;
import com.mycollab.module.tracker.domain.RelatedBug;
import com.mycollab.module.tracker.domain.SimpleRelatedBug;

import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 1.0.0
 */
@IgnoreCacheClass
public interface BugRelationService extends ICrudService<Integer, RelatedBug> {
    List<SimpleRelatedBug> findRelatedBugs(Integer bugId);

    int removeDuplicatedBugs(Integer bugId);
}
