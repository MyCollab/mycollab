package com.mycollab.security

/**
 * Permission item
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
data class PermissionDefItem(var key: String,
                             /**
                              * Display name of permission
                              */
                             var caption: Enum<*>?,
                             /**
                              * Permission flag of permission
                              */
                             var permissionCls: Class<out PermissionFlag>?)
