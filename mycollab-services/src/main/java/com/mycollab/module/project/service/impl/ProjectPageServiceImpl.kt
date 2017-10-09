package com.mycollab.module.project.service.impl

import com.mycollab.common.ActivityStreamConstants
import com.mycollab.common.ModuleNameConstants
import com.mycollab.common.domain.ActivityStreamWithBLOBs
import com.mycollab.common.service.ActivityStreamService
import com.mycollab.module.page.domain.Page
import com.mycollab.module.page.service.PageService
import com.mycollab.module.project.ProjectTypeConstants
import com.mycollab.module.project.service.ProjectPageService
import org.springframework.stereotype.Service
import java.util.*

@Service
class ProjectPageServiceImpl(private val pageService: PageService,
                             private val activityStreamService: ActivityStreamService) : ProjectPageService {

    override fun savePage(page: Page, createdUser: String, projectId: Int, accountId: Int) {
        pageService.savePage(page, createdUser)

        val activityStream = ActivityStreamWithBLOBs()
        activityStream.action = ActivityStreamConstants.ACTION_CREATE
        activityStream.createduser = createdUser
        activityStream.createdtime = GregorianCalendar().time
        activityStream.module = ModuleNameConstants.PRJ
        activityStream.namefield = page.subject
        activityStream.saccountid = accountId
        activityStream.type = ProjectTypeConstants.PAGE
        activityStream.typeid = page.path
        activityStream.extratypeid = projectId
        activityStreamService.save(activityStream)
    }

    override fun getPage(path: String, requestedUser: String): Page {
        return pageService.getPage(path, requestedUser)
    }
}