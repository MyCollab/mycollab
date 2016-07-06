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
package com.mycollab.schedule.jobs

import com.mycollab.common.domain.LiveInstance
import com.mycollab.common.service.AppPropertiesService
import com.mycollab.core.MyCollabVersion
import com.mycollab.module.project.dao.ProjectMapper
import com.mycollab.module.project.domain.ProjectExample
import com.mycollab.module.user.dao.UserMapper
import com.mycollab.module.user.domain.UserExample
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
  @Autowired private val appPropertiesService: AppPropertiesService = null

  def executeJob(context: JobExecutionContext): Unit = {
    val numProjects = projectMapper.countByExample(new ProjectExample)
    val numUsers = userMapper.countByExample(new UserExample)

    val liveInstance = new LiveInstance()
    liveInstance.setAppversion(MyCollabVersion.getVersion)
    liveInstance.setInstalleddate(new DateTime().toDate)
    liveInstance.setJavaversion(System.getProperty("java.version"))
    liveInstance.setSysid(appPropertiesService.getSysId)
    liveInstance.setSysproperties(System.getProperty("os.arch") + ":" + System.getProperty("os.name") + ":" +
      System.getProperty("os.name"))
    liveInstance.setNumprojects(numProjects)
    liveInstance.setNumusers(numUsers)
    val restTemplate = new RestTemplate()
    restTemplate.postForObject("https://api.mycollab.com/api/checkInstance", liveInstance, classOf[String])
  }
}
