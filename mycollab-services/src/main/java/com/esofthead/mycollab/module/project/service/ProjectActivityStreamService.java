package com.esofthead.mycollab.module.project.service;

import java.util.List;

import com.esofthead.mycollab.common.domain.criteria.ActivityStreamSearchCriteria;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.cache.CacheKey;
import com.esofthead.mycollab.core.cache.Cacheable;
import com.esofthead.mycollab.core.persistence.service.IService;
import com.esofthead.mycollab.module.project.domain.ProjectActivityStream;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public interface ProjectActivityStreamService extends IService {
	@Cacheable
	int getTotalActivityStream(@CacheKey ActivityStreamSearchCriteria criteria);

	@Cacheable
	List<ProjectActivityStream> getProjectActivityStreams(
			@CacheKey SearchRequest<ActivityStreamSearchCriteria> searchRequest);
}
