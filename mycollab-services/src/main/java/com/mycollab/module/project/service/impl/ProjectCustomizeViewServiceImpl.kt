package com.mycollab.module.project.service.impl

import com.mycollab.db.persistence.ICrudGenericDAO
import com.mycollab.db.persistence.service.DefaultCrudService
import com.mycollab.module.project.dao.ProjectCustomizeViewMapper
import com.mycollab.module.project.domain.ProjectCustomizeView
import com.mycollab.module.project.service.ProjectCustomizeViewService
import org.springframework.stereotype.Service

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
@Service
class ProjectCustomizeViewServiceImpl(private val projectCustomizeMapper: ProjectCustomizeViewMapper) : DefaultCrudService<Int, ProjectCustomizeView>(), ProjectCustomizeViewService {

    override val crudMapper: ICrudGenericDAO<Int, ProjectCustomizeView>
        get() = projectCustomizeMapper as ICrudGenericDAO<Int, ProjectCustomizeView>

}
