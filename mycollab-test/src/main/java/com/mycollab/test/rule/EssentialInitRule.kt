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
 * along with this program.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 */
package com.mycollab.test.rule

import com.mycollab.configuration.SiteConfiguration
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

import java.util.TimeZone

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
class EssentialInitRule : TestRule {

    override fun apply(base: Statement, description: Description): Statement {
        SiteConfiguration.loadConfiguration()
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
        return base
    }
}
