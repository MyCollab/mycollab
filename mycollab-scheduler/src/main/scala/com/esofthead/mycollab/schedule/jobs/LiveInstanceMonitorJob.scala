/**
 * This file is part of mycollab-scheduler.
 *
 * mycollab-scheduler is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-scheduler is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-scheduler.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.schedule.jobs

import com.esofthead.mycollab.common.domain.LiveInstance
import com.esofthead.mycollab.core.MyCollabVersion
import com.esofthead.mycollab.core.utils.MiscUtils
import com.esofthead.mycollab.module.project.dao.ProjectMapper
import com.esofthead.mycollab.module.project.domain.ProjectExample
import com.esofthead.mycollab.module.user.dao.UserMapper
import com.esofthead.mycollab.module.user.domain.UserExample
import org.joda.time.DateTime
import org.quartz.JobExecutionContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

/**
  * @author MyCollab Ltd
  * @since 5.2.6
  */
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
class LiveInstanceMonitorJob extends GenericQuartzJobBean {

  @Autowired private val projectMapper: ProjectMapper = null
  @Autowired private val userMapper: UserMapper = null

  def executeJob(context: JobExecutionContext): Unit = {
    val numProjects = projectMapper.countByExample(new ProjectExample)
    val numUsers = userMapper.countByExample(new UserExample)

    val liveInstance = new LiveInstance()
    liveInstance.setAppversion(MyCollabVersion.getVersion())
    liveInstance.setInstalleddate(new DateTime().toDate())
    liveInstance.setJavaversion(System.getProperty("java.version"))
    liveInstance.setSysid(MiscUtils.getMacAddressOfServer())
    liveInstance.setSysproperties(System.getProperty("os.arch") + ":" + System.getProperty("os.name") + ":" +
      System.getProperty("os.name"))
    liveInstance.setNumprojects(numProjects)
    liveInstance.setNumusers(numUsers)
    val restTemplate = new RestTemplate()
    restTemplate.postForObject("http://127.0.0.1:7070/api/checkInstance", liveInstance, classOf[String])
  }
}
