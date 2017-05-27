/**
 * This file is part of mycollab-esb.
 *
 * mycollab-esb is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-esb is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-esb.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.esb

import java.util.GregorianCalendar

import com.google.common.eventbus.{AllowConcurrentEvents, Subscribe}
import com.mycollab.common.dao.OptionValMapper
import com.mycollab.common.domain.{OptionVal, OptionValExample}
import com.mycollab.module.esb.GenericCommand
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
  * @author MyCollab Ltd
  * @since 5.1.1
  */
@Component class AddProjectCommandImpl extends GenericCommand {
  @Autowired val optionValMapper: OptionValMapper = null

  @AllowConcurrentEvents
  @Subscribe
  def addProject(event: AddProjectEvent): Unit = {
    val ex = new OptionValExample
    ex.createCriteria().andIsdefaultEqualTo(true).andSaccountidEqualTo(event.accountId)
    import collection.JavaConverters._
    val defaultOptions = optionValMapper.selectByExample(ex).asScala
    for (option <- defaultOptions) {
      val prjOption = new OptionVal
      prjOption.setCreatedtime(new GregorianCalendar().getTime)
      prjOption.setDescription(option.getDescription)
      prjOption.setExtraid(event.projectId)
      prjOption.setIsdefault(false)
      prjOption.setSaccountid(event.accountId)
      prjOption.setType(option.getType)
      prjOption.setTypeval(option.getTypeval)
      prjOption.setFieldgroup(option.getFieldgroup)
      prjOption.setRefoption(option.getId)
      prjOption.setColor("fdde86")
      optionValMapper.insert(prjOption)
    }
  }
}
