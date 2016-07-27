/**
 * This file is part of mycollab-ui.
 *
 * mycollab-ui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-ui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-ui.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.mvp.service

import com.mycollab.vaadin.mvp.{IPresenter, PageView, ViewComponent}
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.core.`type`.filter.{AnnotationTypeFilter, AssignableTypeFilter}
import org.springframework.stereotype.Component
import org.springframework.util.ClassUtils

/**
  * @author MyCollab Ltd
  * @since 5.3.5
  */
@Component
class ComponentScannerService extends InitializingBean {
  val LOG = LoggerFactory.getLogger(classOf[ComponentScannerService])
  
  var viewClasses: Set[Class[_]] = Set()
  var presenterClasses: Set[Class[IPresenter[_ <: PageView]]] = Set()
  
  var cacheViewClasses: Map[Class[_], Class[_]] = Map()
  var cachePresenterClasses: Map[Class[_], Class[_]] = Map()
  
  override def afterPropertiesSet(): Unit = {
    val provider: ClassPathScanningCandidateComponentProvider = new ClassPathScanningCandidateComponentProvider(false)
    provider.addIncludeFilter(new AnnotationTypeFilter(classOf[ViewComponent]))
    provider.addIncludeFilter(new AssignableTypeFilter(classOf[IPresenter[_ <: PageView]]))
    LOG.info("Started resolving view and presenter classes")
    import collection.JavaConverters._
    val candidateComponents = provider.findCandidateComponents("com.mycollab.**.view").asScala.toSet
    for (candidate <- candidateComponents) {
      val cls = ClassUtils.resolveClassName(candidate.getBeanClassName, ClassUtils.getDefaultClassLoader)
      if (cls.getAnnotation(classOf[ViewComponent]) != null) viewClasses += cls
      else if (classOf[IPresenter[_ <: PageView]].isAssignableFrom(cls)) presenterClasses += cls.asInstanceOf[Class[IPresenter[_ <: PageView]]]
    }
    LOG.info("Resolved view and presenter classes")
  }
  
  def getViewImplCls(viewClass: Class[_]): Class[_] = {
    val aClass = cacheViewClasses.get(viewClass)
    aClass match {
      case None =>
        for (classInstance <- viewClasses) {
          if (viewClass.isAssignableFrom(classInstance)) {
            cacheViewClasses += (viewClass -> classInstance)
            return classInstance
          }
        }
        null
      case _ => aClass.get
    }
  }
  
  def getPresenterImplCls(presenterClass: Class[_]): Class[_] = {
    val aClass = cachePresenterClasses.get(presenterClass)
    aClass match {
      case None =>
        
        for (classInstance <- presenterClasses) {
          if (presenterClass.isAssignableFrom(classInstance) && !classInstance.isInterface) {
            cachePresenterClasses += (presenterClass -> classInstance)
            return classInstance
          }
        }
        null
      case _ => aClass.get
    }
  }
}
