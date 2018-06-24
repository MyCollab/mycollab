package com.mycollab.module.project.rest

import com.mycollab.db.arguments.BasicSearchRequest
import com.mycollab.db.arguments.NumberSearchField
import com.mycollab.module.project.domain.criteria.ProjectSearchCriteria
import com.mycollab.module.project.service.ProjectService

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * @author MyCollab Ltd
 * @since 5.4.6
 */
@RestController
@RequestMapping(value = "/{accountId}/projects")
class ProjectController {

    @Autowired
    private lateinit var projectService: ProjectService

    @RequestMapping(method = [(RequestMethod.GET)])
    fun list(@PathVariable accountId: Int,
             @RequestParam(value = "offset", required = false, defaultValue = "0") offset: Int?,
             @RequestParam(value = "limit", required = false) limit: Int?): List<*> {
        val searchCriteria = ProjectSearchCriteria()
        searchCriteria.saccountid = NumberSearchField.equal(accountId)
        return projectService.findPageableListByCriteria(BasicSearchRequest(searchCriteria))
    }
}
