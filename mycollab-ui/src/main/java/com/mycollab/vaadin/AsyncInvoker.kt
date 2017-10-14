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
package com.mycollab.vaadin

import com.mycollab.configuration.ServerConfiguration
import com.mycollab.spring.AppContextUtil
import com.vaadin.ui.UI
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author MyCollab Ltd
 * @since 5.2.1
 */
object AsyncInvoker {
    private val LOG = LoggerFactory.getLogger(AsyncInvoker::class.java)

    fun access(ui: UI, pageCommand: PageCommand) {
        pageCommand.ui = ui
        if (pageCommand.isPush) {
            Thread {
                ui.access {
                    try {
                        pageCommand.run()
                        ui.push()
                        pageCommand.postRun()
                    } catch (e: Exception) {
                        LOG.error("Error", e)
                    } finally {
                        pageCommand.cleanUp()
                        try {
                            ui.push()
                        } catch (e: Exception) {
                            LOG.error("Error", e)
                        }

                    }
                }
            }.start()
        } else {
            ui.session.lockInstance.lock()
            try {
                ui.pollInterval = 1000
                pageCommand.run()
                pageCommand.postRun()
            } finally {
                pageCommand.cleanUp()
                ui.pollInterval = -1
                ui.session.lockInstance.unlock()
            }
        }
    }

    abstract class PageCommand {
        lateinit var ui: UI
        var isPush: Boolean

        init {
            val serverConfiguration = AppContextUtil.getSpringBean(ServerConfiguration::class.java)
            isPush = serverConfiguration.isPush
        }

        abstract fun run()

        open fun postRun() {}

        open fun cleanUp() {}

        fun push() {
            if (isPush) {
                ui.push()
            }
        }
    }
}
