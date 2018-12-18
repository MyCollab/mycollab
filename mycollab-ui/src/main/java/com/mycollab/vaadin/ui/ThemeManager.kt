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
 * along with this program.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.ui

import com.mycollab.common.i18n.ShellI18nEnum
import com.mycollab.core.UserInvalidInputException
import com.mycollab.core.utils.ColorUtils
import com.mycollab.module.user.domain.AccountTheme
import com.mycollab.module.user.service.AccountThemeService
import com.mycollab.spring.AppContextUtil
import com.mycollab.vaadin.AppUI
import com.mycollab.vaadin.UserUIContext
import com.vaadin.server.Page

/**
 * @author MyCollab Ltd.
 * @since 4.1.2
 */
object ThemeManager {

    @JvmStatic
    fun loadDesktopTheme(sAccountId: Int) {
        val themeService = AppContextUtil.getSpringBean(AccountThemeService::class.java)
        var accountTheme = themeService.findTheme(sAccountId)

        if (accountTheme == null) {
            accountTheme = themeService.findDefaultTheme(AppUI.accountId)
            if (accountTheme == null) {
                throw UserInvalidInputException(UserUIContext.getMessage(ShellI18nEnum.ERROR_CAN_NOT_LOAD_THEME))
            }
        }

        val extraStyles = StringBuilder()
        /* Top Menu */
        if (accountTheme.topmenubg != null) {
            extraStyles.append(".topNavigation { background-color: #${accountTheme.topmenubg}; }")
            extraStyles.append("#login-header { background-color: #${accountTheme.topmenubg}; }")
            extraStyles.append(".topNavigation #mainLogo { background-color: #${accountTheme.topmenubg}; }")
        }

        if (accountTheme.topmenubgselected != null) {
            extraStyles.append(".topNavigation .serviceMenuContainer .service-menu .v-button.selected { background-color: #${accountTheme.topmenubgselected}; }")
            extraStyles.append(".topNavigation .serviceMenuContainer .service-menu .v-button:hover { background-color: #${accountTheme.topmenubgselected}; }")
            extraStyles.append(".v-button.add-btn-popup:hover { background-color: #${accountTheme.topmenubgselected}; }")
            extraStyles.append(".topNavigation .v-button.ad { background-color: #${accountTheme.topmenubgselected}; }")
        }

        if (accountTheme.topmenutext != null) {
            extraStyles.append(".topNavigation .v-button { color: #${accountTheme.topmenutext}; }")
            extraStyles.append(".subDomain { color: #${accountTheme.topmenutext}; }")
            extraStyles.append(".accountMenuContainer .v-popup-indicator::before { color: #${accountTheme.topmenutext}; }")
        }

        if (accountTheme.topmenutextselected != null) {
            extraStyles.append(".topNavigation .serviceMenuContainer .service-menu .v-button.selected { color: #${accountTheme.topmenutextselected}; }")
            extraStyles.append(".topNavigation .serviceMenuContainer .service-menu .v-button:hover { color: #${accountTheme.topmenutextselected}; }")
            extraStyles.append(".v-button.add-btn-popup:hover { color: #${accountTheme.topmenutextselected}; }")
            extraStyles.append(".topNavigation .v-button.ad { color: #${accountTheme.topmenutextselected}; }")
            extraStyles.append(".topNavigation .v-button.ad .v-icon { color: #${accountTheme.topmenutextselected}; }")
        }

        /* Vertical Tabsheet */

        if (accountTheme.vtabsheetbg != null) {
            extraStyles.append(".vertical-tabsheet .navigator-wrap { background-color: #${accountTheme.vtabsheetbg}; }")
        }

        if (accountTheme.vtabsheettext != null) {
            extraStyles.append(".vertical-tabsheet .v-button-tab > .v-button-wrap { color: #${accountTheme.vtabsheettext}; }")
            extraStyles.append(".closed-button .v-button-wrap .v-icon { color: #${accountTheme.vtabsheettext}; }")
            extraStyles.append(".expand-button .v-button-wrap .v-icon { color: #${accountTheme.vtabsheettext}; }")
            extraStyles.append(".project-info .header { color: #${accountTheme.vtabsheettext}; }")
            extraStyles.append(".intro-text-wrap .v-label { color: #${accountTheme.vtabsheettext}; }")
        }

        if (accountTheme.vtabsheetbgselected != null) {
            extraStyles.append(".vertical-tabsheet .v-button-tab.tab-selected { background-color: #${accountTheme.vtabsheetbgselected}; }")
            extraStyles.append(".vertical-tabsheet .v-button-tab:hover {background-color: #${accountTheme.vtabsheetbgselected};}")
        }

        if (accountTheme.vtabsheettextselected != null) {
            extraStyles.append(".vertical-tabsheet .v-button-tab.tab-selected > .v-button-wrap { color: #${accountTheme.vtabsheettextselected}; }")
            //Color while hover on sidebar menu
            extraStyles.append(".vertical-tabsheet .v-button-tab .v-button-wrap:hover {color: #${accountTheme.vtabsheettextselected}!important;}")
            extraStyles.append(".vertical-tabsheet .v-button-tab:hover .v-button-wrap {color: #${accountTheme.vtabsheettextselected}!important;}")
            //Volume text display bar in file manager
            extraStyles.append(".v-label.volumeUsageInfo div { color: #${accountTheme.vtabsheettextselected};}")
        }

        /* Action Buttons */

        if (accountTheme.actionbtn != null) {
            extraStyles.append(".v-button.v-button-greenbtn, .v-button-greenbtn:focus { background-color: #${accountTheme.actionbtn}; }")
            extraStyles.append(".splitbutton:hover .v-button.v-button-greenbtn, .v-button-greenbtn:hover { background-color: ${ColorUtils.darkerColor("#" + accountTheme.actionbtn)}; }")
            extraStyles.append(".upload-field .v-upload-immediate .v-button {background-color: #${accountTheme.actionbtn};}")
            extraStyles.append(".upload-field .v-upload-immediate .v-button:hover {background-color: ${ColorUtils.darkerColor("#" + accountTheme.actionbtn)};}")
            extraStyles.append(".optionPopupContent .action-wrap:hover {background-color: #${accountTheme.actionbtn}};")
            extraStyles.append(".v-buttongroup.toggle-btn-group .v-button.active { background-color: #${accountTheme.actionbtn}; }")
            //Button paging
            extraStyles.append(".v-button.buttonPaging.current, .v-button.buttonPaging:hover { background-color:#${accountTheme.actionbtn}; }")
            //Selection background of selected item
            extraStyles.append(".v-filterselect-suggestpopup .gwt-MenuItem-selected { background-color:#${accountTheme.actionbtn}; }")
            //Year block of activity stream
            extraStyles.append(".v-label.year-lbl { box-shadow: 0 0 0 5px #${accountTheme.actionbtn};}")
            //Date label of activity stream
            extraStyles.append(".activity-list .feed-block-wrap .date-lbl { background-color:#${accountTheme.actionbtn};}")
            extraStyles.append(".activity-list .feed-block-wrap .date-lbl::after{ border-left-color:#${accountTheme.actionbtn};}")
            extraStyles.append(".activity-list .feed-block-wrap:hover .date-lbl { background-color:" + ColorUtils.darkerColor("#" + accountTheme.actionbtn) + ";}")
            extraStyles.append(".activity-list .feed-block-wrap:hover .date-lbl::after{ border-left-color:${ColorUtils.darkerColor("#" + accountTheme.actionbtn)};}")

            // Button group default button
            extraStyles.append(".toggle-btn-group .v-button.btn-group-default {background-color:#${accountTheme.actionbtn};}")
            extraStyles.append(".toggle-btn-group .v-button.btn-group-default:hover {background-color:${ColorUtils.darkerColor("#" + accountTheme.actionbtn)};}")
            extraStyles.append(".v-context-menu-container .v-context-menu .v-context-submenu:hover {background-color:#${accountTheme.actionbtn};}")
        }

        if (accountTheme.actionbtntext != null) {
            extraStyles.append(".v-button.v-button-greenbtn, .v-button-greenbtn:focus { color: #${accountTheme.actionbtntext}; }")

            extraStyles.append(".upload-field .v-upload-immediate .v-button, .upload-field .v-upload-immediate .v-button:focus {color: #${accountTheme.actionbtntext};}")

            extraStyles.append(".optionPopupContent .action-wrap .v-button-action .v-button-wrap:hover {color: #${accountTheme.actionbtntext}};")

            //Button paging
            extraStyles.append(".v-button.buttonPaging.current, .v-button.buttonPaging:hover { color:#${accountTheme.actionbtntext}; }")

            //Selection text color of selected item
            extraStyles.append(".v-filterselect-suggestpopup .gwt-MenuItem-selected { color:#${accountTheme.actionbtntext}; }")

            //Date label of activity stream
            extraStyles.append(".activity-list .feed-block-wrap .date-lbl { color:#${accountTheme.actionbtntext};}")

            extraStyles.append(".v-button.v-button-block {color:#${accountTheme.actionbtntext};}")

            extraStyles.append(".v-context-menu-container .v-context-menu .v-context-submenu:hover {color:#${accountTheme.actionbtntext};}")

            extraStyles.append(".toggle-btn-group .v-button.btn-group-default {color:#${accountTheme.actionbtntext};}")
        }

        /* Option Buttons */

        if (accountTheme.optionbtn != null) {
            extraStyles.append(".v-button.v-button-graybtn, .v-button-graybtn:focus { background-color: #${accountTheme.optionbtn};}")
            extraStyles.append(".splitbutton:hover .v-button-graybtn, .v-button-graybtn:hover { background-color: ${ColorUtils.darkerColor("#" + accountTheme.optionbtn)};}")
            //Set toggle button group background
            extraStyles.append(".toggle-btn-group .v-button { background-color: #${accountTheme.optionbtn};}")
            extraStyles.append(".toggle-btn-group .v-button:hover { background-color: ${ColorUtils.darkerColor("#" + accountTheme.optionbtn)};}")
        }

        if (accountTheme.optionbtntext != null) {
            extraStyles.append(".v-button.v-button-graybtn, .v-button-graybtn:focus { color: #${accountTheme.optionbtntext}; }")
            extraStyles.append(".toggle-btn-group .v-button { color: #${accountTheme.optionbtntext}; }")
        }

        /* Danger Buttons */

        if (accountTheme.dangerbtn != null) {
            extraStyles.append(".v-button.v-button-redbtn, .v-button-redbtn:focus { background-color: #${accountTheme.dangerbtn}; }")
            extraStyles.append(".v-button-redbtn:hover { background-color: ${ColorUtils.darkerColor("#" + accountTheme.dangerbtn, 0.1)}; }")
            //Set style of popup content action
            extraStyles.append(".optionPopupContent .action-wrap.danger .v-button-action { color: #${accountTheme.dangerbtn}; }")
            extraStyles.append(".optionPopupContent .action-wrap.danger:hover {background-color: #${accountTheme.dangerbtn};}")
        }

        if (accountTheme.dangerbtntext != null) {
            extraStyles.append(".v-button.v-button-redbtn, .v-button-redbtn:focus { color: #${accountTheme.dangerbtntext}; }")
        }

        if (extraStyles.isNotBlank()) {
            Page.getCurrent().styles.add(extraStyles.toString())
        }

    }

    @JvmStatic
    fun loadDemoTheme(accountTheme: AccountTheme) {
        val demoExtraStyles = StringBuilder()
        /* Top Menu */

        if (accountTheme.topmenubg != null) {
            demoExtraStyles.append(".example-block .topNavigation { background-color: #${accountTheme.topmenubg}; }")
        }

        if (accountTheme.topmenubgselected != null) {
            demoExtraStyles.append(".example-block .topNavigation .service-menu.v-buttongroup .v-button.selected { background-color: #${accountTheme.topmenubgselected}; }")
        }

        if (accountTheme.topmenutext != null) {
            demoExtraStyles.append(".example-block .topNavigation .v-button-caption { color: #${accountTheme.topmenutext}; }")
        }

        if (accountTheme.topmenutextselected != null) {
            demoExtraStyles.append(".example-block .topNavigation .service-menu.v-buttongroup .v-button.selected .v-button-caption { color: #${accountTheme.topmenutextselected}; }")
        }

        /* Vertical Tabsheet */
        if (accountTheme.vtabsheetbg != null) {
            demoExtraStyles.append(".example-block .navigator-wrap { background-color: #${accountTheme.vtabsheetbg}; }")
        }

        if (accountTheme.vtabsheetbgselected != null) {
            demoExtraStyles.append(".example-block .vertical-tabsheet .v-button-tab.tab-selected { background-color: #${accountTheme.vtabsheetbgselected}; }")
        }

        if (accountTheme.vtabsheettext != null) {
            demoExtraStyles.append(".example-block .vertical-tabsheet .v-button-tab > .v-button-wrap > .v-button-caption { color: #${accountTheme.vtabsheettext}; }")
        }

        if (accountTheme.vtabsheettextselected != null) {
            demoExtraStyles.append(".example-block .vertical-tabsheet .v-button-tab.tab-selected > .v-button-wrap > .v-button-caption { color: #${accountTheme.vtabsheettextselected}; }")
        }

        /* Action Buttons */
        if (accountTheme.actionbtn != null) {
            demoExtraStyles.append(".example-block .v-button.v-button-greenbtn, .example-block .v-button-greenbtn:focus { background-color: #${accountTheme.actionbtn}; }")
        }

        if (accountTheme.actionbtntext != null) {
            demoExtraStyles.append(".example-block .v-button.v-button-greenbtn, .example-block .v-button-greenbtn:focus { color: #${accountTheme.actionbtntext}; }")
        }

        /* Option Buttons */

        if (accountTheme.optionbtn != null) {
            demoExtraStyles.append(".example-block .v-button.v-button-graybtn, .example-block .v-button-graybtn:focus { background-color: #${accountTheme.optionbtn}; }")
        }

        if (accountTheme.optionbtntext != null) {
            demoExtraStyles.append(".example-block .v-button.v-button-graybtn, .example-block .v-button-graybtn:focus { color: #${accountTheme.optionbtntext}; }")
        }

        /* Danger Buttons */
        if (accountTheme.dangerbtn != null) {
            demoExtraStyles.append(".example-block .v-button.v-button-redbtn, .example-block .v-button-redbtn:focus { background-color: #${accountTheme.dangerbtn}; }")
        }

        if (accountTheme.dangerbtntext != null) {
            demoExtraStyles.append(".example-block .v-button.v-button-redbtn, .example-block .v-button-redbtn:focus { color: #${accountTheme.dangerbtntext}; }")
        }

        if (demoExtraStyles.isNotBlank()) {
            Page.getCurrent().styles.add(demoExtraStyles.toString())
        }
    }
}
