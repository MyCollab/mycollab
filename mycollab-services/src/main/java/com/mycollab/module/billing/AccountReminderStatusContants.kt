/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.billing

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
object AccountReminderStatusContants {
    @JvmField
    val REMIND_ACCOUNT_IS_ABOUT_END_1ST_TIME = "Reminder1"

    @JvmField
    val REMIND_ACCOUNT_IS_ABOUT_END_2ST_TIME = "Reminder2"

    @JvmField
    val REMIND_ACCOUNT_IS_CONVERTED_TO_FREE_PLAN = "Reminder3"

    @JvmField
    val REMIND_ACCOUNT_IS_DELETED_1ST = "ReminderDelete1"
}
