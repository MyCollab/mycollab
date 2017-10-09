package com.mycollab.module.file.event

import com.mycollab.vaadin.event.ApplicationEvent
import com.mycollab.module.ecm.domain.ExternalDrive
import com.mycollab.module.ecm.domain.Resource

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object FileEvent {
    class GotoList(source: Any, val data: Any?) : ApplicationEvent(source)

    class ResourceRemovedEvent(source: Any, val data: Resource) : ApplicationEvent(source)

    class ExternalDriveConnectedEvent(source: Any, val data: ExternalDrive) : ApplicationEvent(source)

    class ExternalDriveDeleteEvent(source: Any, val data: ExternalDrive) : ApplicationEvent(source)
}