package com.mycollab.module.project

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
object ProjectRolePermissionCollections {

    @JvmField
    val MESSAGES = "Message"

    @JvmField
    val MILESTONES = "Milestone"

    @JvmField
    val INVOICE = "Invoice"

    @JvmField
    val TIME = "Time"

    @JvmField
    val FINANCE = "Finance"

    @JvmField
    val TASKS = "Task"

    @JvmField
    val BUGS = "Bug"

    @JvmField
    val VERSIONS = "Version"

    @JvmField
    val COMPONENTS = "Component"

    @JvmField
    val RISKS = "Risk"

    @JvmField
    val USERS = "User"

    @JvmField
    val ROLES = "Role"

    @JvmField
    val PAGES = "Page"

    @JvmField
    val PROJECT = "Project"

    @JvmField
    val PROJECT_PERMISSIONS = arrayOf(MESSAGES, MILESTONES, TASKS, BUGS, COMPONENTS, VERSIONS, PAGES, RISKS, TIME, INVOICE, USERS, ROLES, PROJECT, FINANCE)
}
