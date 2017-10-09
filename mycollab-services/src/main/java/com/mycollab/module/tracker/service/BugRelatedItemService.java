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
