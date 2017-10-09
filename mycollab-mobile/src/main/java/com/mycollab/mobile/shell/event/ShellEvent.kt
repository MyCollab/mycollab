package com.mycollab.mobile.shell.event

import com.mycollab.vaadin.event.ApplicationEvent

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object ShellEvent {
    class GotoLoginView(source: Any) : ApplicationEvent(source)

    class GotoMainPage(source: Any, val data: Any?) : ApplicationEvent(source)

    class GotoCrmModule(source: Any, val data: Any?) : ApplicationEvent(source)

    class GotoProjectModule(source: Any, val data: Any?) : ApplicationEvent(source)

    class GotoUserAccountModule(source: Any, val data: Any?) : ApplicationEvent(source)

    class PushView(source: Any, val data: Any?) : ApplicationEvent(source)

    class NavigateBack(source: Any, val data: Any?) : ApplicationEvent(source)

    class LogOut(source: Any) : ApplicationEvent(source)
}