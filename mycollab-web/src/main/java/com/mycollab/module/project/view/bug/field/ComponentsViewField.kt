package com.mycollab.module.project.view.bug.field

import com.hp.gagawa.java.elements.A
import com.mycollab.common.i18n.OptionI18nEnum
import com.mycollab.core.utils.StringUtils
import com.mycollab.module.project.ProjectLinkGenerator
import com.mycollab.module.project.domain.Component
import com.mycollab.vaadin.ui.ELabel
import com.mycollab.vaadin.web.ui.WebThemes
import com.vaadin.ui.CustomField
import com.vaadin.ui.Label
import org.apache.commons.collections4.CollectionUtils
import org.vaadin.viritin.layouts.MCssLayout

/**
 * @author MyCollab Ltd
 * @since 7.0.0
 */
class ComponentsViewField : CustomField<Collection<Component>>() {

    private val containerLayout: MCssLayout = MCssLayout()

    override fun initContent(): com.vaadin.ui.Component = containerLayout

    override fun doSetValue(components: Collection<Component>) {
        if (CollectionUtils.isNotEmpty(components)) {
            components.forEach { component -> containerLayout.addComponent(buildComponentLink(component)) }
        }
    }

    private fun buildComponentLink(component: Component): Label {
        val componentLink = A(ProjectLinkGenerator.generateBugComponentPreviewLink(component.projectid!!, component.id!!))
                .appendText(StringUtils.trim(component.name, 25, true))

        val lbl = ELabel.html(componentLink.write()).withStyleName(WebThemes.FIELD_NOTE)

        if (component.status != null && component.status == OptionI18nEnum.StatusI18nEnum.Closed.name) {
            lbl.addStyleName(WebThemes.LINK_COMPLETED)
        }

        return lbl
    }

    override fun getValue(): Collection<Component>? = null
}
