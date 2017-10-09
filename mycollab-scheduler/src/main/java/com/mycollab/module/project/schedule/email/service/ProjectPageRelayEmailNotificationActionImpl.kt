package com.mycollab.module.project.schedule.email.service

import com.mycollab.core.utils.StringUtils
import com.mycollab.module.page.domain.Page
import com.mycollab.module.page.service.PageService
import com.mycollab.module.project.domain.ProjectRelayEmailNotification
import com.mycollab.schedule.email.ItemFieldMapper
import com.mycollab.schedule.email.MailContext
import com.mycollab.schedule.email.project.ProjectPageRelayEmailNotificationAction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
class ProjectPageRelayEmailNotificationActionImpl : SendMailToAllMembersAction<Page>(), ProjectPageRelayEmailNotificationAction {
    @Autowired private lateinit var pageService: PageService

    override fun getBeanInContext(notification: ProjectRelayEmailNotification): Page =
            pageService.getPage(notification.typeid, "")

    override fun buildExtraTemplateVariables(context: MailContext<Page>) {}

    override fun getItemName(): String = StringUtils.trim(bean!!.subject, 100)

    override fun getProjectName(): String = ""

    override fun getCreateSubject(context: MailContext<Page>): String = ""

    override fun getUpdateSubject(context: MailContext<Page>): String = ""

    override fun getCommentSubject(context: MailContext<Page>): String = ""

    override fun getItemFieldMapper(): ItemFieldMapper = ItemFieldMapper()
}