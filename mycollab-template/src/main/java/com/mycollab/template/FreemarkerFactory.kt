package com.mycollab.template

import com.mycollab.core.MyCollabException
import com.mycollab.core.utils.FileUtils
import freemarker.cache.ClassTemplateLoader
import freemarker.cache.FileTemplateLoader
import freemarker.cache.MultiTemplateLoader
import freemarker.cache.TemplateLoader
import freemarker.template.Configuration
import freemarker.template.Template

import java.io.File
import java.io.IOException
import java.util.ArrayList

/**
 * @author MyCollab
 * @since 6.0.0
 */
object FreemarkerFactory {

    @JvmField
    val configuration = Configuration(Configuration.VERSION_2_3_26)

    init {
        configuration.defaultEncoding = "UTF-8"
        try {
            val loaders = ArrayList<TemplateLoader>()
            val i18nFolder = File(FileUtils.userFolder, "i18n")
            val confFolder1 = File(FileUtils.userFolder, "config")
            val confFolder2 = File(FileUtils.userFolder, "src/main/config")
            if (i18nFolder.exists()) {
                loaders.add(FileTemplateLoader(i18nFolder))
            }
            if (confFolder1.exists()) {
                loaders.add(FileTemplateLoader(confFolder1))
            }
            if (confFolder2.exists()) {
                loaders.add(FileTemplateLoader(confFolder2))
            }
            loaders.add(ClassTemplateLoader(FreemarkerFactory::class.java.classLoader, ""))
            configuration.templateLoader = MultiTemplateLoader(loaders.toTypedArray())
        } catch (e: IOException) {
            throw MyCollabException(e)
        }
    }

    @JvmStatic
    fun template(templatePath: String): Template = configuration.getTemplate(templatePath)
}
