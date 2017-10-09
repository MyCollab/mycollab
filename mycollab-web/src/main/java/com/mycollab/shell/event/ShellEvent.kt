package com.mycollab.shell.event

import com.mycollab.vaadin.event.ApplicationEvent

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object ShellEvent {
    class NotifyErrorEvent(source: Any, val data: Any?) : ApplicationEvent(source)

    class RefreshPage(source: Any) : ApplicationEvent(source)

    class GotoMainPage(source: Any, val data: Any?) : ApplicationEvent(source)

    class LogOut(source: Any, val data: Any?) : ApplicationEvent(source)

    class GotoProjectModule(source: Any, val data: Any?) : ApplicationEvent(source)

    class GotoCrmModule(source: Any, val data: Any?) : ApplicationEvent(source)

    class GotoFileModule(source: Any, val data: Any?) : ApplicationEvent(source)

    class GotoUserAccountModule(source: Any, val data: Any?) : ApplicationEvent(source)

    class GotoForgotPasswordPage(source: Any, val data: Any?) : ApplicationEvent(source)

    class NewNotification(source: Any, val data: Any?) : ApplicationEvent(source)

    class AddQueryParam(source: Any, val data: Any?) : ApplicationEvent(source)
}