package com.mycollab.module.billing.esb

import com.google.common.eventbus.AllowConcurrentEvents
import com.google.common.eventbus.Subscribe
import com.mycollab.common.NotificationType
import com.mycollab.common.i18n.OptionI18nEnum
import com.mycollab.common.i18n.OptionI18nEnum.*
import com.mycollab.common.i18n.WikiI18nEnum
import com.mycollab.common.service.OptionValService
import com.mycollab.core.utils.BeanUtility
import com.mycollab.core.utils.StringUtils
import com.mycollab.module.esb.GenericCommand
import com.mycollab.module.file.PathUtils
import com.mycollab.module.page.domain.Folder
import com.mycollab.module.page.domain.Page
import com.mycollab.module.page.service.PageService
import com.mycollab.module.project.domain.*
import com.mycollab.module.project.i18n.OptionI18nEnum.*
import com.mycollab.module.project.service.*
import com.mycollab.module.tracker.domain.BugWithBLOBs
import com.mycollab.module.tracker.domain.Version
import com.mycollab.module.tracker.service.BugRelatedItemService
import com.mycollab.module.tracker.service.BugService
import com.mycollab.module.tracker.service.ComponentService
import com.mycollab.module.tracker.service.VersionService
import org.joda.time.DateTime
import org.springframework.stereotype.Component
import java.util.*

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
@Component
class AccountCreatedCommand(private val optionValService: OptionValService,
                            private val projectService: ProjectService,
                            private val messageService: MessageService,
                            private val milestoneService: MilestoneService,
                            private val taskService: ProjectTaskService,
                            private val bugService: BugService,
                            private val bugRelatedService: BugRelatedItemService,
                            private val componentService: ComponentService,
                            private val versionService: VersionService,
                            private val pageService: PageService,
                            private val projectNotificationSettingService: ProjectNotificationSettingService)  : GenericCommand() {


    @AllowConcurrentEvents
    @Subscribe
    fun execute(event: AccountCreatedEvent) {
        createDefaultOptionVals(event.accountId)
        if (event.createSampleData) {
            createSampleProjectData(event.initialUser, event.accountId)
        }
    }

    private fun createDefaultOptionVals(accountId: Int) {
        optionValService.createDefaultOptions(accountId)
    }

    private fun createSampleProjectData(initialUser: String, accountId: Int) {
        val now = DateTime()

        val project = Project()
        project.saccountid = accountId
        project.description = "Sample project"
        project.homepage = "https://www.mycollab.com"
        project.name = "Sample project"
        project.projectstatus = StatusI18nEnum.Open.name
        project.shortname = "SP1"
        val projectId = projectService.saveWithSession(project, initialUser)

        val projectNotificationSetting = ProjectNotificationSetting()
        projectNotificationSetting.level = NotificationType.None.name
        projectNotificationSetting.projectid = projectId
        projectNotificationSetting.saccountid = accountId
        projectNotificationSetting.username = initialUser
        projectNotificationSettingService.saveWithSession(projectNotificationSetting, initialUser)

        val message = Message()
        message.isstick = true
        message.posteduser = initialUser
        message.message = "Welcome to MyCollab workspace. I hope you enjoy it!"
        message.saccountid = accountId
        message.projectid = projectId
        message.title = "Thank you for using MyCollab!"
        message.posteddate = now.toLocalDate().toDate()
        messageService.saveWithSession(message, initialUser)

        val milestone = Milestone()
        milestone.createduser = initialUser
        milestone.duedate = now.plusDays(14).toLocalDate().toDate()
        milestone.startdate = now.toLocalDate().toDate()
        milestone.enddate = now.plusDays(14).toLocalDate().toDate()
        milestone.name = "Sample milestone"
        milestone.assignuser = initialUser
        milestone.projectid = projectId
        milestone.saccountid = accountId
        milestone.status = MilestoneStatus.InProgress.name
        val sampleMilestoneId = milestoneService.saveWithSession(milestone, initialUser)

        val taskA = Task()
        taskA.name = "Task A"
        taskA.projectid = projectId
        taskA.createduser = initialUser
        taskA.percentagecomplete = 0.0
        taskA.priority = Priority.Medium.name
        taskA.saccountid = accountId
        taskA.status = StatusI18nEnum.Open.name
        taskA.startdate = now.toLocalDate().toDate()
        taskA.enddate = now.plusDays(3).toLocalDate().toDate()
        val taskAId = taskService.saveWithSession(taskA, initialUser)

        val taskB = BeanUtility.deepClone(taskA)
        taskB.name = "Task B"
        taskB.id = null
        taskB.milestoneid = sampleMilestoneId
        taskB.startdate = now.plusDays(2).toLocalDate().toDate()
        taskB.enddate = now.plusDays(4).toLocalDate().toDate()
        taskService.saveWithSession(taskB, initialUser)

        val taskC = BeanUtility.deepClone(taskA)
        taskC.id = null
        taskC.name = "Task C"
        taskC.startdate = now.plusDays(3).toLocalDate().toDate()
        taskC.enddate = now.plusDays(5).toLocalDate().toDate()
        taskC.parenttaskid = taskAId
        taskService.saveWithSession(taskC, initialUser)

        val taskD = BeanUtility.deepClone(taskA)
        taskD.id = null
        taskD.name = "Task D"
        taskD.startdate = now.toLocalDate().toDate()
        taskD.enddate = now.plusDays(2).toLocalDate().toDate()
        taskService.saveWithSession(taskD, initialUser)

        val component = com.mycollab.module.tracker.domain.Component()
        component.name = "Component 1"
        component.createduser = initialUser
        component.description = "Sample Component 1"
        component.status = StatusI18nEnum.Open.name
        component.projectid = projectId
        component.saccountid = accountId
        component.userlead = initialUser
        componentService.saveWithSession(component, initialUser)

        val version = Version()
        version.createduser = initialUser
        version.name = "Version 1"
        version.description = "Sample version"
        version.duedate = now.plusDays(21).toLocalDate().toDate()
        version.projectid = projectId
        version.saccountid = accountId
        version.status = StatusI18nEnum.Open.name
        versionService.saveWithSession(version, initialUser)

        val bugA = BugWithBLOBs()
        bugA.description = "Sample bug"
        bugA.environment = "All platforms"
        bugA.assignuser = initialUser
        bugA.duedate = now.plusDays(2).toLocalDate().toDate()
        bugA.createduser = initialUser
        bugA.milestoneid = sampleMilestoneId
        bugA.name = "Bug A"
        bugA.status = BugStatus.Open.name
        bugA.priority = Priority.Medium.name
        bugA.projectid = projectId
        bugA.saccountid = accountId
        val bugAId = bugService.saveWithSession(bugA, initialUser)

        val bugB = BeanUtility.deepClone(bugA)
        bugB.id = null
        bugB.name = "Bug B"
        bugB.status = BugStatus.Resolved.name
        bugB.resolution = BugResolution.CannotReproduce.name
        bugB.priority = Priority.Low.name
        bugService.saveWithSession(bugB, initialUser)

        bugRelatedService.saveAffectedVersionsOfBug(bugAId, listOf(version))
        bugRelatedService.saveComponentsOfBug(bugAId, listOf(component))

        val page = Page()
        page.subject = "Welcome to sample workspace"
        page.content = "I hope you enjoy MyCollab!"
        page.path = PathUtils.getProjectDocumentPath(accountId, projectId) + "/" + StringUtils.generateSoftUniqueId()
        page.status = WikiI18nEnum.status_public.name
        pageService.savePage(page, initialUser)

        val folder = Folder()
        folder.name = "Requirements"
        folder.description = "Sample folder"
        folder.path = PathUtils.getProjectDocumentPath(accountId, projectId) + "/" + StringUtils.generateSoftUniqueId()
        pageService.createFolder(folder, initialUser)

        val timer = Timer("Set member notification")
        timer.schedule(object: TimerTask() {
            override fun run() {
                projectNotificationSetting.level = NotificationType.Default.name
                projectNotificationSettingService.updateWithSession(projectNotificationSetting, initialUser)
            }
        }, 90000)
    }
}