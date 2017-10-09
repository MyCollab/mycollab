package com.mycollab.module.tracker.dao;

import com.mycollab.module.tracker.domain.SimpleRelatedBug;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 1.0.0
 */
public interface RelatedBugMapperExt {

    List<SimpleRelatedBug> findRelatedBugs(@Param("bugId") Integer bugId);
}
