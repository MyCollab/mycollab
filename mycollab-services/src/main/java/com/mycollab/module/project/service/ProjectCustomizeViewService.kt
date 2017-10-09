package com.mycollab.module.project.service

import com.mycollab.cache.IgnoreCacheClass
import com.mycollab.db.persistence.service.ICrudService
import com.mycollab.module.project.domain.ProjectCustomizeView

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
@IgnoreCacheClass
interface ProjectCustomizeViewService : ICrudService<Int, ProjectCustomizeView>
