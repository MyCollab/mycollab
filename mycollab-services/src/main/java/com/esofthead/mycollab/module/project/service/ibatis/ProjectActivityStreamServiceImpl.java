package com.esofthead.mycollab.module.project.service.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esofthead.mycollab.common.domain.criteria.ActivityStreamSearchCriteria;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.cache.CacheKey;
import com.esofthead.mycollab.module.project.dao.ProjectMapperExt;
import com.esofthead.mycollab.module.project.domain.ProjectActivityStream;
import com.esofthead.mycollab.module.project.service.ProjectActivityStreamService;

@Service
public class ProjectActivityStreamServiceImpl implements
		ProjectActivityStreamService {

	@Autowired
	private ProjectMapperExt projectMapperExt;

	@Override
	public int getTotalActivityStream(
			@CacheKey ActivityStreamSearchCriteria criteria) {
		return projectMapperExt.getTotalActivityStream(criteria);
	}

	@Override
	public List<ProjectActivityStream> getProjectActivityStreams(
			@CacheKey SearchRequest<ActivityStreamSearchCriteria> searchRequest) {
		return projectMapperExt.getProjectActivityStreams(
				searchRequest.getSearchCriteria(),
				new RowBounds((searchRequest.getCurrentPage() - 1)
						* searchRequest.getNumberOfItems(), searchRequest
						.getNumberOfItems()));
	}
}
