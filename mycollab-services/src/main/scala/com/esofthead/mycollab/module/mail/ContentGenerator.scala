/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.mail

import java.io._
import java.util.Locale

import com.esofthead.mycollab.configuration.{SharingOptions, SiteConfiguration}
import com.esofthead.mycollab.core.MyCollabException
import com.esofthead.mycollab.i18n.LocalizationHelper
import com.esofthead.mycollab.template.velocity.TemplateContext
import org.apache.velocity.app.VelocityEngine
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

/**
 * @author MyCollab Ltd.
 * @since 4.6.0
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
class ContentGenerator extends IContentGenerator with InitializingBean {
  private var templateContext: TemplateContext = _
  @Autowired private val templateEngine: VelocityEngine = null

  @throws(classOf[Exception])
  def afterPropertiesSet() {
    templateContext = new TemplateContext
    val sharingOptions = SharingOptions.getDefaultSharingOptions
    val defaultUrls = Map[String, String](
      "cdn_url" -> SiteConfiguration.getCdnUrl,
      "facebook_url" -> sharingOptions.getFacebookUrl,
      "google_url" -> sharingOptions.getGoogleplusUrl,
      "linkedin_url" -> sharingOptions.getLinkedinUrl,
      "twitter_url" -> sharingOptions.getTwitterUrl)
    putVariable("defaultUrls", defaultUrls)
  }

  override def putVariable(key: String, value: scala.Any): Unit = {
    import scala.collection.JavaConversions._
    value match {
      case map: Map[_, _] => templateContext.put(key, mapAsJavaMap(map))
      case list: List[_] => templateContext.put(key, seqAsJavaList(list))
      case _ => templateContext.put(key, value)
    }
  }

  override def generateBodyContent(templateFilePath: String): String = {
    val writer = new StringWriter
    val resourceStream = classOf[LocalizationHelper].getClassLoader.getResourceAsStream(templateFilePath)

    var reader: Reader = null
    try {
      reader = new InputStreamReader(resourceStream, "UTF-8")
    }
    catch {
      case e: UnsupportedEncodingException => reader = new InputStreamReader(resourceStream)
    }

    templateEngine.evaluate(templateContext.getVelocityContext, writer, "log task", reader)
    writer.toString
  }

  override def generateBodyContent(templateFilePath: String, currentLocale: Locale): String = this.generateBodyContent(templateFilePath, currentLocale, null)

  override def generateBodyContent(templateFilePath: String, currentLocale: Locale, defaultLocale: Locale): String = {
    val writer = new StringWriter
    var reader = LocalizationHelper.templateReader(templateFilePath, currentLocale)
    if (reader == null) {
      if (defaultLocale == null) {
        throw new MyCollabException("Can not find file " + templateFilePath + " in locale " + currentLocale)
      }
      reader = LocalizationHelper.templateReader(templateFilePath, defaultLocale)
      if (reader == null) {
        throw new MyCollabException("Can not find file " + templateFilePath + " in locale " + currentLocale + " and default locale " + defaultLocale)
      }
    }

    templateEngine.evaluate(templateContext.getVelocityContext, writer, "log task", reader)
    writer.toString
  }

  override def generateSubjectContent(subject: String): String = {
    val writer = new StringWriter
    val reader = new StringReader(subject)
    templateEngine.evaluate(templateContext.getVelocityContext, writer, "log task", reader)
    writer.toString
  }
}