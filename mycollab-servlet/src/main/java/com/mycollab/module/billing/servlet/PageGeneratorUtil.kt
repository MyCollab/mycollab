package com.mycollab.module.billing.servlet

import com.mycollab.configuration.ApplicationConfiguration
import com.mycollab.configuration.IDeploymentMode
import com.mycollab.spring.AppContextUtil
import freemarker.template.Configuration
import freemarker.template.TemplateException
import java.io.IOException
import java.io.StringWriter
import java.util.*
import javax.servlet.http.HttpServletResponse

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
internal object PageGeneratorUtil {
    @Throws(IOException::class, TemplateException::class)
    @JvmStatic
    fun responseUserNotExistPage(response: HttpServletResponse, username: String, loginURL: String) {
        val context = HashMap<String, Any>()
        context.put("loginURL", loginURL)
        context.put("username", username)
        val applicationConfiguration = AppContextUtil.getSpringBean(ApplicationConfiguration::class.java)
        val deploymentMode = AppContextUtil.getSpringBean(IDeploymentMode::class.java)
        var defaultUrls = applicationConfiguration.defaultUrls()
        defaultUrls += ("cdn_url" to deploymentMode.getCdnUrl())
        context.put("defaultUrls", defaultUrls)

        val writer = StringWriter()
        val templateEngine = AppContextUtil.getSpringBean(Configuration::class.java)
        val template = templateEngine.getTemplate("pageUserNotExist.ftl", Locale.US)
        template.process(context, writer)

        val html = writer.toString()
        val out = response.writer
        out.println(html)
    }
}
