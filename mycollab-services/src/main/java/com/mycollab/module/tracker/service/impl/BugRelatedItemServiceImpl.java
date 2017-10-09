package com.mycollab.module.tracker.service.impl;

import com.mycollab.module.tracker.dao.BugRelatedItemMapper;
import com.mycollab.module.tracker.domain.*;
import com.mycollab.module.tracker.service.BugRelatedItemService;
import org.apache.commons.collections.CollectionUtils;
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
    public void saveAffectedVersionsOfBug(Integer bugId, List<Version> versions) {
        insertAffectedVersionsOfBug(bugId, versions);
    }

    private void insertAffectedVersionsOfBug(Integer bugId, List<Version> versions) {
        for (Version version : versions) {
            BugRelatedItem relatedItem = new BugRelatedItem();
            relatedItem.setBugid(bugId);
            relatedItem.setTypeid(version.getId());
            relatedItem.setType(SimpleRelatedBug.AFFVERSION);
            bugRelatedItemMapper.insert(relatedItem);
        }
    }

    @Override
    public void saveFixedVersionsOfBug(Integer bugId, List<Version> versions) {
        insertFixedVersionsOfBug(bugId, versions);
    }

    private void insertFixedVersionsOfBug(Integer bugId, List<Version> versions) {
        for (Version version : versions) {
            BugRelatedItem relatedItem = new BugRelatedItem();
            relatedItem.setBugid(bugId);
            relatedItem.setTypeid(version.getId());
            relatedItem.setType(SimpleRelatedBug.FIXVERSION);
            bugRelatedItemMapper.insert(relatedItem);
        }
    }

    @Override
    public void saveComponentsOfBug(Integer bugId, List<Component> components) {
        insertComponentsOfBug(bugId, components);
    }

    public void insertComponentsOfBug(Integer bugId, List<Component> components) {
        for (Component component : components) {
            BugRelatedItem relatedItem = new BugRelatedItem();
            relatedItem.setBugid(bugId);
            relatedItem.setTypeid(component.getId());
            relatedItem.setType(SimpleRelatedBug.COMPONENT);
            bugRelatedItemMapper.insert(relatedItem);
        }
    }

    private void deleteTrackerBugRelatedItem(Integer bugId, String type) {
        BugRelatedItemExample ex = new BugRelatedItemExample();
        ex.createCriteria().andBugidEqualTo(bugId).andTypeEqualTo(type);

        bugRelatedItemMapper.deleteByExample(ex);
    }


    @Override
    public void updateAffectedVersionsOfBug(Integer bugId, List<Version> versions) {
        deleteTrackerBugRelatedItem(bugId, SimpleRelatedBug.AFFVERSION);
        if (CollectionUtils.isNotEmpty(versions)) {
            insertAffectedVersionsOfBug(bugId, versions);
        }
    }

    @Override
    public void updateFixedVersionsOfBug(Integer bugId, List<Version> versions) {
        deleteTrackerBugRelatedItem(bugId, SimpleRelatedBug.FIXVERSION);
        if (CollectionUtils.isNotEmpty(versions)) {
            insertFixedVersionsOfBug(bugId, versions);
        }
    }

    @Override
    public void updateComponentsOfBug(Integer bugId, List<Component> components) {
        deleteTrackerBugRelatedItem(bugId, SimpleRelatedBug.COMPONENT);
        if (CollectionUtils.isNotEmpty(components)) {
            insertComponentsOfBug(bugId, components);
        }
    }
}
