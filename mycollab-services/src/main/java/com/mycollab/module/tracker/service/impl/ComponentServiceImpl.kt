package com.mycollab.module.tracker.service.impl

import com.mycollab.aspect.ClassInfo
import com.mycollab.aspect.ClassInfoMap
import com.mycollab.aspect.Traceable
import com.mycollab.common.ModuleNameConstants
import com.mycollab.core.MyCollabException
import com.mycollab.db.persistence.ICrudGenericDAO
import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.db.persistence.service.DefaultService
import com.mycollab.module.project.ProjectTypeConstants
import com.mycollab.module.tracker.dao.ComponentMapper
import com.mycollab.module.tracker.dao.ComponentMapperExt
import com.mycollab.module.tracker.domain.Component
import com.mycollab.module.tracker.domain.ComponentExample
import com.mycollab.module.tracker.domain.SimpleComponent
import com.mycollab.module.tracker.domain.criteria.ComponentSearchCriteria
import com.mycollab.module.tracker.service.ComponentService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author MyCollab Ltd.
 * @since 1.0.0
 */
@Service
@Transactional
@Traceable(nameField = "name", extraFieldName = "projectid")
class ComponentServiceImpl(private val componentMapper: ComponentMapper,
                           private val componentMapperExt: ComponentMapperExt) : DefaultService<Int, Component, ComponentSearchCriteria>(), ComponentService {

    override val crudMapper: ICrudGenericDAO<Int, Component>
        get() = componentMapper as ICrudGenericDAO<Int, Component>

    override val searchMapper: ISearchableDAO<ComponentSearchCriteria>
        get() = componentMapperExt

    override fun findById(componentId: Int?, sAccountId: Int?): SimpleComponent {
        return componentMapperExt.findComponentById(componentId!!)
    }

    override fun saveWithSession(record: Component, username: String?): Int {
        // check whether there is exiting record
        val ex = ComponentExample()
        ex.createCriteria().andNameEqualTo(record.name).andProjectidEqualTo(record.projectid)

        val count = componentMapper.countByExample(ex)
        return if (count > 0) {
            throw MyCollabException("There is an existing record has name " + record.name)
        } else {
            super.saveWithSession(record, username)
        }
    }

    companion object {
        init {
            ClassInfoMap.put(ComponentServiceImpl::class.java, ClassInfo(ModuleNameConstants.PRJ, ProjectTypeConstants.BUG_COMPONENT))
        }
    }
}