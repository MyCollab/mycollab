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
package com.mycollab.mobile.mvp

import com.mycollab.core.ResourceNotFoundException
import com.mycollab.core.SecureAccessException
import com.mycollab.core.utils.ExceptionUtils.getExceptionType
import com.mycollab.security.PermissionChecker
import com.mycollab.vaadin.UserUIContext
import com.mycollab.vaadin.mvp.*
import com.mycollab.vaadin.ui.NotificationUtil
import com.vaadin.addon.touchkit.ui.NavigationManager
import com.vaadin.ui.HasComponents
import com.vaadin.ui.UI
import org.slf4j.LoggerFactory

/**
 * @param <V>
 * @author MyCollab Ltd.
 * @since 3.0
</V> */
abstract class AbstractPresenter<out V : PageView>(private val viewClass: Class<V>) : IPresenter<V> {
    private var _view: V? = null

    override fun getView(): V? {
        if (_view == null) {
            _view = ViewManager.getCacheComponent(viewClass)
            postInitView()
        }
        return _view
    }

    protected open fun postInitView() {}

    fun go(data: ScreenData<Any>) {
        val manager = UI.getCurrent().content as NavigationManager
        go(manager, data)
    }

    override fun go(container: HasComponents, data: ScreenData<Any>?): Boolean {
        getView()

        when {
            checkPermissionAccessIfAny() -> try {
                onGo(container, data)
            } catch (e: Throwable) {
                onErrorStopChain(e)
                return false
            }
            else -> NotificationUtil.showMessagePermissionAlert()
        }
        return true
    }

    protected abstract fun onGo(container: HasComponents, data: ScreenData<Any>?)

    private fun checkPermissionAccessIfAny(): Boolean {
        val viewPermission = this.javaClass.getAnnotation(ViewPermission::class.java)
        when {
            viewPermission != null -> return if (UserUIContext.isAdmin()) {
                true
            } else {
                val permissionId = viewPermission.permissionId
                val impliedPermissionVal = viewPermission.impliedPermissionVal
                val permissionMap = UserUIContext.getPermissionMap()
                when (permissionMap) {
                    null -> false
                    else -> {
                        val value = permissionMap[permissionId]
                        value != null && PermissionChecker.isImplied(value, impliedPermissionVal)
                    }
                }
            }
            else -> return true
        }
    }

    override fun handleChain(container: HasComponents, pageActionChain: PageActionChain) {
        val pageAction = pageActionChain.pop()
        val isSuccess = go(container, pageAction)

        if (pageActionChain.hasNext() && isSuccess) {
            onHandleChain(container, pageActionChain)
        }
    }

    protected open fun onErrorStopChain(throwable: Throwable) {
        when {
            getExceptionType(throwable, ResourceNotFoundException::class.java) != null -> NotificationUtil.showRecordNotExistNotification()
            getExceptionType(throwable, SecureAccessException::class.java) != null -> NotificationUtil.showMessagePermissionAlert()
            getExceptionType(throwable, PresenterNotFoundException::class.java) != null -> NotificationUtil.showFeatureNotPresentInSubscription()
            else -> LOG.error("Exception", throwable)
        }
    }

    protected open fun onHandleChain(container: HasComponents, pageActionChain: PageActionChain) {
        throw UnsupportedOperationException("You need override this method")
    }

    companion object {
        private val serialVersionUID = 1L
        private val LOG = LoggerFactory.getLogger(AbstractPresenter::class.java)
    }
}
