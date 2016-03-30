/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.vaadin.ui

import java.io.InputStream

import com.esofthead.mycollab.common.i18n.GenericI18Enum
import com.esofthead.mycollab.reporting.ReportExportType
import com.esofthead.mycollab.vaadin.AppContext
import com.esofthead.mycollab.vaadin.events.{HasMassItemActionHandler, MassItemActionHandler, ViewItemAction}
import com.esofthead.mycollab.vaadin.web.ui.UIConstants
import com.vaadin.server.StreamResource.StreamSource
import com.vaadin.server.{FileDownloader, FontAwesome, Resource, StreamResource}
import com.vaadin.ui.Button
import org.vaadin.peter.buttongroup.ButtonGroup
import org.vaadin.viritin.layouts.MHorizontalLayout

/**
 * @author MyCollab Ltd
 * @since 5.0.9
 */
class DefaultMassItemActionHandlerContainer extends MHorizontalLayout with HasMassItemActionHandler {
    private var actionHandler: MassItemActionHandler = null
    private val groupMap: scala.collection.mutable.Map[String, ButtonGroup] = scala.collection.mutable.Map().withDefaultValue(null)

    /**
     *
     * @param id
     * @param resource
     * @param groupId
     * @param description
     */
    def addActionItem(id: String, resource: Resource, groupId: String, description: String) {
        var group = groupMap(groupId)
        if (group == null) {
            group = new ButtonGroup
            groupMap.put(groupId, group)
            this.addComponent(group)
        }
        val optionBtn = new Button(null, new Button.ClickListener() {
            def buttonClick(event: Button.ClickEvent) {
                changeOption(id)
            }
        })
        optionBtn.setIcon(resource)
        if ("delete" == groupId) {
            optionBtn.addStyleName(UIConstants.BUTTON_DANGER)
        }
        else {
            optionBtn.addStyleName(UIConstants.BUTTON_ACTION)
        }
        optionBtn.addStyleName(UIConstants.BUTTON_SMALL_PADDING)
        optionBtn.setDescription(description)
        group.addButton(optionBtn)
    }

    def addDeleteActionItem(): Unit = {
        addActionItem(ViewItemAction.DELETE_ACTION, FontAwesome.TRASH_O, "delete", AppContext.getMessage(GenericI18Enum.BUTTON_DELETE))
    }

    def addMailActionItem(): Unit = {
        addActionItem(ViewItemAction.MAIL_ACTION, FontAwesome.ENVELOPE_O, "mail", AppContext.getMessage(GenericI18Enum.BUTTON_MAIL))
    }

    def addMassUpdateActionItem(): Unit = {
        addActionItem(ViewItemAction.MASS_UPDATE_ACTION, FontAwesome.DATABASE, "update",
            AppContext.getMessage(GenericI18Enum.TOOLTIP_MASS_UPDATE))
    }

    def addDownloadPdfActionItem(): Unit = {
        addDownloadActionItem(ReportExportType.PDF, FontAwesome.FILE_PDF_O,
            "export", "export.pdf", AppContext.getMessage(GenericI18Enum.BUTTON_EXPORT_PDF))
    }

    def addDownloadExcelActionItem(): Unit = {
        addDownloadActionItem(ReportExportType.EXCEL, FontAwesome.FILE_EXCEL_O,
            "export", "export.xlsx", AppContext.getMessage(GenericI18Enum.BUTTON_EXPORT_EXCEL))
    }

    def addDownloadCsvActionItem(): Unit = {
        addDownloadActionItem(ReportExportType.CSV, FontAwesome.FILE_TEXT_O,
            "export", "export.csv", AppContext.getMessage(GenericI18Enum.BUTTON_EXPORT_CSV))
    }

    /**
     *
     * @param exportType
     * @param resource
     * @param groupId
     * @param downloadFileName
     * @param description
     */
    def addDownloadActionItem(exportType: ReportExportType, resource: Resource, groupId: String, downloadFileName: String,
                              description: String) {
        var group = groupMap(groupId)
        if (group == null) {
            group = new ButtonGroup
            groupMap.put(groupId, group)
            this.addComponent(group)
        }
        val optionBtn = new Button("")
        val fileDownloader: FileDownloader = new FileDownloader(new StreamResource(new DownloadStreamSource(exportType),
            downloadFileName))
        fileDownloader.extend(optionBtn)
        optionBtn.setIcon(resource)
        optionBtn.addStyleName(UIConstants.BUTTON_ACTION)
        optionBtn.addStyleName(UIConstants.BUTTON_SMALL_PADDING)
        optionBtn.setDescription(description)
        group.addButton(optionBtn)
    }

    private def changeOption(id: String) {
        if (actionHandler != null) {
            actionHandler.onSelect(id)
        }
    }

    protected def buildStreamResource(id: ReportExportType): StreamResource = {
        if (actionHandler != null) {
            val streamResource: StreamResource = actionHandler.buildStreamResource(id)
            if (streamResource != null) {
                return streamResource
            }
        }
        return null
    }

    def setMassActionHandler(handler: MassItemActionHandler) {
        actionHandler = handler
    }

    @SerialVersionUID(1L)
    private class DownloadStreamSource extends StreamSource {
        private var exportType: ReportExportType = null

        def this(exportType: ReportExportType) {
            this()
            this.exportType = exportType
        }

        def getStream: InputStream = {
            return buildStreamResource(exportType).getStreamSource.getStream
        }
    }

}
