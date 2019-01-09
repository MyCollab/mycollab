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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.ui

import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.reporting.ReportExportType
import com.mycollab.vaadin.UserUIContext
import com.mycollab.vaadin.event.HasMassItemActionHandler
import com.mycollab.vaadin.event.MassItemActionHandler
import com.mycollab.vaadin.event.ViewItemAction
import com.mycollab.vaadin.web.ui.ButtonGroup
import com.mycollab.vaadin.web.ui.WebThemes
import com.vaadin.icons.VaadinIcons
import com.vaadin.server.FileDownloader
import com.vaadin.server.Resource
import com.vaadin.server.StreamResource
import com.vaadin.shared.ui.MarginInfo
import com.vaadin.ui.Button
import org.vaadin.viritin.button.MButton
import org.vaadin.viritin.layouts.MHorizontalLayout
import java.io.InputStream

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class DefaultMassItemActionHandlerContainer : MHorizontalLayout(), HasMassItemActionHandler {
    private var actionHandler: MassItemActionHandler? = null
    private val groupMap = mutableMapOf<String, ButtonGroup>()

    init {
        this.margin = MarginInfo(false, true, false, true)
    }

    /**
     *
     * @param id
     * @param resource
     * @param groupId
     * @param description
     */
    private fun addActionItem(id: String, resource: Resource, groupId: String, description: String) {
        var group = groupMap[groupId]
        if (group == null) {
            group = ButtonGroup()
            groupMap[groupId] = group
            this.addComponent(group)
        }
        val optionBtn = MButton("", Button.ClickListener {
        }).withIcon(resource).withStyleName(WebThemes.BUTTON_SMALL_PADDING).withDescription(description)
        when (groupId) {
            "delete" -> optionBtn.addStyleName(WebThemes.BUTTON_DANGER)
            else -> optionBtn.addStyleName(WebThemes.BUTTON_ACTION)
        }
        group.addButton(optionBtn)
    }

    fun addDeleteActionItem() {
        addActionItem(ViewItemAction.DELETE_ACTION, VaadinIcons.TRASH, "delete", UserUIContext.getMessage(GenericI18Enum.BUTTON_DELETE))
    }

    fun addMailActionItem() {
        addActionItem(ViewItemAction.MAIL_ACTION, VaadinIcons.ENVELOPE_O, "mail", UserUIContext.getMessage(GenericI18Enum.BUTTON_MAIL))
    }

    fun addMassUpdateActionItem() {
        addActionItem(ViewItemAction.MASS_UPDATE_ACTION, VaadinIcons.DATABASE, "update",
                UserUIContext.getMessage(GenericI18Enum.TOOLTIP_MASS_UPDATE))
    }

    fun addDownloadPdfActionItem() {
        addDownloadActionItem(ReportExportType.PDF, VaadinIcons.FILE_O,
                "export", "export.pdf", UserUIContext.getMessage(GenericI18Enum.BUTTON_EXPORT_PDF))
    }

    fun addDownloadExcelActionItem() {
        addDownloadActionItem(ReportExportType.EXCEL, VaadinIcons.FILE_TABLE,
                "export", "export.xlsx", UserUIContext.getMessage(GenericI18Enum.BUTTON_EXPORT_EXCEL))
    }

    fun addDownloadCsvActionItem() {
        addDownloadActionItem(ReportExportType.CSV, VaadinIcons.FILE_TEXT_O,
                "export", "export.csv", UserUIContext.getMessage(GenericI18Enum.BUTTON_EXPORT_CSV))
    }

    /**
     *
     * @param exportType
     * @param resource
     * @param groupId
     * @param downloadFileName
     * @param description
     */
    private fun addDownloadActionItem(exportType: ReportExportType, resource: Resource, groupId: String, downloadFileName: String, description: String) {
        var group = groupMap[groupId]
        if (group == null) {
            group = ButtonGroup()
            groupMap[groupId] = group
            this.addComponent(group)
        }
        val optionBtn = MButton("").withIcon(resource).withStyleName(WebThemes.BUTTON_ACTION, WebThemes.BUTTON_SMALL_PADDING)
                .withDescription(description)
        val fileDownloader = FileDownloader(StreamResource(DownloadStreamSource(this, exportType), downloadFileName))
        fileDownloader.extend(optionBtn)
        group.addButton(optionBtn)
    }

    private fun changeOption(id: String) {
        if (actionHandler != null) {
            actionHandler!!.onSelect(id)
        }
    }

    private fun buildStreamResource(id: ReportExportType): StreamResource? {
        if (actionHandler != null) {
            val streamResource = actionHandler!!.buildStreamResource(id)
            if (streamResource != null) {
                return streamResource
            }
        }
        return null
    }

    override fun setMassActionHandler(handler: MassItemActionHandler) {
        actionHandler = handler
    }

    private inner class DownloadStreamSource(val container: DefaultMassItemActionHandlerContainer,
                                             val exportType: ReportExportType) : StreamResource.StreamSource {
        override fun getStream(): InputStream = container.buildStreamResource(exportType)!!.streamSource.stream
    }

}