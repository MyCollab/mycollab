/**
 * This file is part of mycollab-ui.
 *
 * mycollab-ui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-ui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-ui.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.ui;

import com.mycollab.common.i18n.ShellI18nEnum;
import com.mycollab.core.UserInvalidInputException;
import com.mycollab.core.utils.ColorUtils;
import com.mycollab.module.user.domain.AccountTheme;
import com.mycollab.module.user.service.AccountThemeService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.vaadin.server.Page;

/**
 * @author MyCollab Ltd.
 * @since 4.1.2
 */
public class ThemeManager {
    public static void loadMobileTheme(int sAccountId) {
        AccountThemeService themeService = AppContextUtil.getSpringBean(AccountThemeService.class);
        AccountTheme accountTheme = themeService.findTheme(sAccountId);

        if (accountTheme == null) {
            accountTheme = themeService.findDefaultTheme(MyCollabUI.getAccountId());
            if (accountTheme == null) {
                throw new UserInvalidInputException(UserUIContext.getMessage(ShellI18nEnum.ERROR_CAN_NOT_LOAD_THEME));
            }
        }

        StringBuilder extraStyles = new StringBuilder();

        extraStyles.append(".v-touchkit-navbar-caption { width: " + (UIUtils.getBrowserWidth() - 144) + "px " +
                "!important; }");
        extraStyles.append(".v-touchkit-navbar-caption span { width: " + (UIUtils.getBrowserWidth() - 144) + "px " +
                "!important; }");
        if (accountTheme.getTopmenubg() != null) {
            extraStyles.append(".v-touchkit-navbar { background-color: #" + accountTheme.getTopmenubg() + "; }");
        }

        if (accountTheme.getTopmenutext() != null) {
            extraStyles.append(".v-touchkit-navbar { color: #" + accountTheme.getTopmenutext() + "; }");
            extraStyles.append(".v-touchkit-navbar .v-button { color: #" + accountTheme.getTopmenutext() + "; }");
            extraStyles.append(".v-touchkit-navbar .v-touchkit-navbutton { color: #" + accountTheme.getTopmenutext() + "; }");
            extraStyles.append(".v-touchkit-navbar .v-touchkit-navbutton:after { color: #" + accountTheme.getTopmenutext() + "; }");
            extraStyles.append(".v-touchkit-navbar .v-navbar-quickmenu-button { color: #" + accountTheme.getTopmenutext() + "; }");
        }

        if (accountTheme.getVtabsheetbg() != null) {
            extraStyles.append(".section { background-color: #" + accountTheme.getVtabsheetbg() + "; }");
            extraStyles.append(".v-navbar-quickmenu-content { background-color: #" + accountTheme.getVtabsheetbg() + "; }");
            extraStyles.append(".slidemenu .v-window-contents { background-color: #" + accountTheme.getVtabsheetbg() + "; }");
            extraStyles.append(".project-dashboard .project-info-layout { background-color: #" + accountTheme.getVtabsheetbg() + "; }");
        }

        if (accountTheme.getVtabsheettext() != null) {
            extraStyles.append(".v-navbar-quickmenu-content .v-button { color: #" + accountTheme.getVtabsheettext() + "; }");
            extraStyles.append(".slidemenu .v-window-contents .v-button { color: #" + accountTheme.getVtabsheettext() + " !important; }");
            extraStyles.append(".section { color: #" + accountTheme.getVtabsheettext() + "; }");
        }

        /* Action Buttons */

        if (accountTheme.getActionbtn() != null) {
            extraStyles.append(".v-touchkit-tabbar-toolbar .v-button.selected { background-color: #"
                    + accountTheme.getActionbtn() + " !important; }");

            extraStyles.append(".v-button.v-button-action-btn, .v-button-action-btn:focus { background-color: #" +
                    accountTheme.getActionbtn() + "; }");

            extraStyles.append(".section .v-touchkit-navbutton:after { color: #" +
                    accountTheme.getActionbtn() + "; }");

            extraStyles.append(".v-touchkit-verticalcomponentgroup .v-touchkit-navbutton:after { color: #" +
                    accountTheme.getActionbtn() + "; }");
        }

        if (accountTheme.getActionbtntext() != null) {
            extraStyles.append(".v-touchkit-tabbar-toolbar .v-button.selected { color: #"
                    + accountTheme.getActionbtntext() + "; }");

            extraStyles.append(".v-button.v-button-action-btn, .v-button-action-btn:focus { color: #" +
                    accountTheme.getActionbtntext() + "; }");
        }

		/* Option Buttons */

        if (accountTheme.getOptionbtn() != null) {
            extraStyles.append(".v-touchkit-tabbar-toolbar .v-button { background-color: #"
                    + accountTheme.getOptionbtn() + " !important; }");

            extraStyles.append(".v-button.v-button-option-btn, .v-button-option-btn:focus { background-color: #" +
                    accountTheme.getOptionbtn() + "; }");
        }

        if (accountTheme.getOptionbtntext() != null) {
            extraStyles.append(".v-touchkit-tabbar-toolbar .v-button { color: #"
                    + accountTheme.getOptionbtntext() + " !important; }");

            extraStyles.append(".v-button.v-button-option-btn, .v-button-option-btn:focus { color: #" +
                    accountTheme.getOptionbtntext() + "; }");
        }

		/* Danger Buttons */

        if (accountTheme.getDangerbtn() != null) {

        }

        if (accountTheme.getDangerbtntext() != null) {

        }

        if (extraStyles.length() > 0) {
            Page.getCurrent().getStyles().add(extraStyles.toString());
        }
    }

    public static void loadDesktopTheme(int sAccountId) {
        AccountThemeService themeService = AppContextUtil.getSpringBean(AccountThemeService.class);
        AccountTheme accountTheme = themeService.findTheme(sAccountId);

        if (accountTheme == null) {
            accountTheme = themeService.findDefaultTheme(MyCollabUI.getAccountId());
            if (accountTheme == null) {
                throw new UserInvalidInputException(UserUIContext.getMessage(ShellI18nEnum.ERROR_CAN_NOT_LOAD_THEME));
            }
        }

        StringBuilder extraStyles = new StringBuilder();
        /* Top Menu */
        if (accountTheme.getTopmenubg() != null) {
            extraStyles.append(".topNavigation { background-color: #" + accountTheme.getTopmenubg() + "; }");
            extraStyles.append("#login-header { background-color: #" + accountTheme.getTopmenubg() + "; }");
            extraStyles.append(".topNavigation #mainLogo { background-color: " + ColorUtils.darkerColor("#" + accountTheme.getTopmenubg()) + "; }");
        }

        if (accountTheme.getTopmenubgselected() != null) {
            extraStyles.append(".topNavigation .serviceMenuContainer .service-menu.v-buttongroup .v-button.selected { background-color: #"
                    + accountTheme.getTopmenubgselected() + "; }");

            extraStyles.append(".topNavigation .serviceMenuContainer .service-menu.v-buttongroup .v-button:hover { background-color: #"
                    + accountTheme.getTopmenubgselected() + "; }");

            extraStyles.append(".v-button.add-btn-popup:hover { background-color: #" + accountTheme.getTopmenubgselected() + "; }");
        }

        if (accountTheme.getTopmenutext() != null) {
            extraStyles.append(".topNavigation .v-button { color: #" + accountTheme.getTopmenutext() + "; }");
        }

        if (accountTheme.getTopmenutextselected() != null) {
            extraStyles.append(".topNavigation .serviceMenuContainer .service-menu.v-buttongroup .v-button.selected { color: #"
                    + accountTheme.getTopmenutextselected() + "; }");

            extraStyles.append(".topNavigation .serviceMenuContainer .service-menu.v-buttongroup .v-button:hover { color: #" + accountTheme.getTopmenutextselected() + "; }");

            extraStyles.append(".v-button.add-btn-popup:hover { color: #" + accountTheme.getTopmenutextselected() + "; }");
        }

		/* Vertical Tabsheet */

        if (accountTheme.getVtabsheetbg() != null) {
            extraStyles.append(".vertical-tabsheet .navigator-wrap { background-color: #" + accountTheme.getVtabsheetbg() + "; }");

            extraStyles.append(".projectfeed-hdr-wrapper { background-color: #" + accountTheme.getVtabsheetbg() + "; }");

            extraStyles.append(".project-info { background-color: #" + accountTheme.getVtabsheetbg() + "; }");

            extraStyles.append("div.v-csslayout.rightsidebar-layout .sidebar-wrap { background-color: " + ColorUtils
                    .brighterColor("#" + accountTheme.getVtabsheetbg()) + ";}");

            extraStyles.append(".v-sliderpanel-content, .v-sliderpanel-tab { background-color: " + ColorUtils
                    .darkerColor("#" + accountTheme.getVtabsheetbg()) + "; }");
        }

        if (accountTheme.getVtabsheettext() != null) {
            extraStyles.append(".vertical-tabsheet .v-button-tab > .v-button-wrap { color: #"
                    + accountTheme.getVtabsheettext() + "; }");

            extraStyles.append(".project-info .header { color: #" + accountTheme.getVtabsheettext() + "; }");

            extraStyles.append("div.v-csslayout.rightsidebar-layout .sidebar-wrap { color: #" + accountTheme.getVtabsheettext() + "; }");

            extraStyles.append(".crmContainer .navigator-wrap .basic-info { color: #" + accountTheme.getVtabsheettext() + "; }");

            extraStyles.append(".intro-text-wrap .v-label { color: #" + accountTheme.getVtabsheettext() + "; }");
        }

        if (accountTheme.getVtabsheetbgselected() != null) {
            extraStyles.append(".vertical-tabsheet .v-button-tab.tab-selected { background-color: #"
                    + accountTheme.getVtabsheetbgselected() + "; }");

            extraStyles.append(".vertical-tabsheet .v-button-tab:hover {background-color: #" + accountTheme
                    .getVtabsheetbgselected() + ";}");
        }

        if (accountTheme.getVtabsheettextselected() != null) {
            extraStyles.append(".vertical-tabsheet .v-button-tab.tab-selected > .v-button-wrap { color: #"
                    + accountTheme.getVtabsheettextselected() + "; }");

            //Color while hover on sidebar menu
            extraStyles.append(".vertical-tabsheet .v-button-tab .v-button-wrap:hover {color: #" + accountTheme
                    .getVtabsheettextselected() + "!important;}");

            extraStyles.append(".vertical-tabsheet .v-button-tab:hover .v-button-wrap {color: #" + accountTheme
                    .getVtabsheettextselected() + "!important;}");

            //Volume text display bar in file manager
            extraStyles.append(".v-label.volumeUsageInfo div { color: #" + accountTheme
                    .getVtabsheettextselected() + ";}");
        }

		/* Action Buttons */

        if (accountTheme.getActionbtn() != null) {
            extraStyles.append(".v-button.v-button-greenbtn, .v-button-greenbtn:focus { background-color: #" + accountTheme.getActionbtn()
                    + "; }");

            extraStyles.append(".splitbutton:hover .v-button.v-button-greenbtn, .v-button-greenbtn:hover { " +
                    "background-color: " + ColorUtils.darkerColor("#" + accountTheme.getActionbtn()) + "; }");

            extraStyles.append(".upload-field .v-upload-immediate .v-button {background-color: #" + accountTheme.getActionbtn() + ";}");

            extraStyles.append(".upload-field .v-upload-immediate .v-button:hover {background-color: " +
                    ColorUtils.darkerColor("#" + accountTheme.getActionbtn()) + ";}");

            extraStyles.append(".optionPopupContent .action-wrap:hover {" + "background-color: #" + accountTheme.getActionbtn() + "};");

            extraStyles.append(".v-buttongroup.toggle-btn-group .v-button.active { background-color: #" + accountTheme.getActionbtn()
                    + "; }");

            //Button paging
            extraStyles.append(".v-button.buttonPaging.current, .v-button.buttonPaging:hover { background-color:#" +
                    accountTheme.getActionbtn() + "; }");

            //Selection background of selected item
            extraStyles.append(".v-filterselect-suggestpopup .gwt-MenuItem-selected { background-color:#" + accountTheme.getActionbtn() + "; }");

            //Year block of activity stream
            extraStyles.append(".v-label.year-lbl { box-shadow: 0 0 0 5px #" + accountTheme.getActionbtn() + ";}");

            //Date label of activity stream
            extraStyles.append(".feed-block-wrap .date-lbl { background-color:#" + accountTheme.getActionbtn() + ";}");

            extraStyles.append(".feed-block-wrap .date-lbl::after{ border-left-color:#" + accountTheme.getActionbtn() + ";}");

            extraStyles.append(".feed-block-wrap:hover .date-lbl { background-color:" + ColorUtils.darkerColor("#" +
                    accountTheme.getActionbtn()) + ";}");

            extraStyles.append(".feed-block-wrap:hover .date-lbl::after{ border-left-color:" + ColorUtils.darkerColor
                    ("#" + accountTheme.getActionbtn()) + ";}");

            // Button group default button
            extraStyles.append(".v-buttongroup.toggle-btn-group .v-button.btn-group-default {background-color:#" + accountTheme
                    .getActionbtn() + ";}");

            extraStyles.append(".v-buttongroup.toggle-btn-group .v-button.btn-group-default:hover {background-color:"
                    + ColorUtils.darkerColor("#" + accountTheme.getActionbtn()) + ";}");

            extraStyles.append("div.v-button.token-field { background-color: " + ColorUtils.brighterColor("#" + accountTheme.getActionbtn()) + "; }");

//            extraStyles.append(".block {background-color: #" + accountTheme.getActionbtn() + ";}");
//            extraStyles.append(".block:hover {background-color: " + ColorUtils.darkerColor("#" + accountTheme.getActionbtn()) + ";}");

            extraStyles.append(".v-context-menu-container .v-context-menu .v-context-submenu:hover " +
                    "{background-color:#" + accountTheme.getActionbtn() + ";}");
        }

        if (accountTheme.getActionbtntext() != null) {
            extraStyles.append(".v-button.v-button-greenbtn, .v-button-greenbtn:focus { color: #"
                    + accountTheme.getActionbtntext() + "; }");

            extraStyles.append(".upload-field .v-upload-immediate .v-button, .upload-field .v-upload-immediate " +
                    ".v-button:focus {color: #" + accountTheme.getActionbtntext() + ";}");

            extraStyles.append(".optionPopupContent .action-wrap .v-button-action .v-button-wrap:hover" +
                    " {" + "color: #" + accountTheme.getActionbtntext() + "};");

            //Button paging
            extraStyles.append(".v-button.buttonPaging.current, .v-button.buttonPaging:hover { color:#" + accountTheme
                    .getActionbtntext() + "; }");

            //Selection text color of selected item
            extraStyles.append(".v-filterselect-suggestpopup .gwt-MenuItem-selected { color:#" +
                    accountTheme.getActionbtntext() + "; }");

            //Date label of activity stream
            extraStyles.append(".feed-block-wrap .date-lbl { color:#" + accountTheme.getActionbtntext() + ";}");

            extraStyles.append(".v-button.v-button-block {color:#" + accountTheme.getActionbtntext() + ";}");

            extraStyles.append("div.v-button.token-field { color: #" + accountTheme.getActionbtntext() + "; }");

            extraStyles.append(".block {color:#" + accountTheme.getActionbtntext() + ";}");

            extraStyles.append(".v-context-menu-container .v-context-menu .v-context-submenu:hover " +
                    "{color:#" + accountTheme.getActionbtntext() + ";}");
        }

		/* Option Buttons */

        if (accountTheme.getOptionbtn() != null) {
            extraStyles.append(".v-button.v-button-graybtn, .v-button-graybtn:focus { background-color: #"
                    + accountTheme.getOptionbtn() + ";}");

            extraStyles.append(".splitbutton:hover .v-button-graybtn, .v-button-graybtn:hover { background-color: "
                    + ColorUtils.darkerColor("#" + accountTheme.getOptionbtn()) + ";}");

            //Set toggle button group background
            extraStyles.append(".v-buttongroup.toggle-btn-group .v-button { background-color: #" +
                    accountTheme.getOptionbtn() + ";}");

            extraStyles.append(".v-buttongroup.toggle-btn-group .v-button:hover { background-color: " +
                    ColorUtils.darkerColor("#" + accountTheme.getOptionbtn()) + ";}");

            extraStyles.append(".block-popupedit { background-color: " + ColorUtils.brighterColor("#" + accountTheme
                    .getOptionbtn(), 0.2) + ";}");
            extraStyles.append(".block-popupedit:hover { background-color: #" + accountTheme.getOptionbtn() + ";}");
        }

        if (accountTheme.getOptionbtntext() != null) {
            extraStyles.append(".v-button.v-button-graybtn, .v-button-graybtn:focus { color: #" + accountTheme.getOptionbtntext() + "; }");

            extraStyles.append(".v-buttongroup.toggle-btn-group .v-button { color: #"
                    + accountTheme.getOptionbtntext() + "; }");
        }

		/* Danger Buttons */

        if (accountTheme.getDangerbtn() != null) {
            extraStyles.append(".v-button.v-button-redbtn, .v-button-redbtn:focus { background-color: #"
                    + accountTheme.getDangerbtn() + "; }");

            extraStyles.append(".v-button-redbtn:hover { background-color: "
                    + ColorUtils.darkerColor("#" + accountTheme.getDangerbtn(), 0.1) + "; }");

            //Set style of popup content action
            extraStyles.append(".optionPopupContent .action-wrap.danger .v-button-action { color: #" + accountTheme
                    .getDangerbtn() + "; }");

            extraStyles.append(".optionPopupContent .action-wrap.danger:hover {" +
                    "background-color: #" + accountTheme.getDangerbtn() + ";}");
        }

        if (accountTheme.getDangerbtntext() != null) {
            extraStyles.append(".v-button.v-button-redbtn, .v-button-redbtn:focus { color: #"
                    + accountTheme.getDangerbtntext() + "; }");
        }

        if (extraStyles.length() > 0) {
            Page.getCurrent().getStyles().add(extraStyles.toString());
        }

    }

    public static void loadDemoTheme(AccountTheme accountTheme) {
        StringBuilder demoExtraStyles = new StringBuilder();
        /* Top Menu */

        if (accountTheme.getTopmenubg() != null) {
            demoExtraStyles.append(".example-block .topNavigation { background-color: #"
                    + accountTheme.getTopmenubg() + "; }");
        }

        if (accountTheme.getTopmenubgselected() != null) {
            demoExtraStyles.append(".example-block .topNavigation .service-menu.v-buttongroup .v-button.selected { background-color: #"
                    + accountTheme.getTopmenubgselected() + "; }");
        }

        if (accountTheme.getTopmenutext() != null) {
            demoExtraStyles.append(".example-block .topNavigation .v-button-caption { color: #"
                    + accountTheme.getTopmenutext() + "; }");
        }

        if (accountTheme.getTopmenutextselected() != null) {
            demoExtraStyles.append(".example-block .topNavigation .service-menu.v-buttongroup .v-button.selected .v-button-caption { color: #"
                    + accountTheme.getTopmenutextselected() + "; }");
        }

		/* Vertical Tabsheet */
        if (accountTheme.getVtabsheetbg() != null) {
            demoExtraStyles.append(".example-block .navigator-wrap { background-color: #"
                    + accountTheme.getVtabsheetbg() + "; }");
        }

        if (accountTheme.getVtabsheetbgselected() != null) {
            demoExtraStyles.append(".example-block .vertical-tabsheet .v-button-tab.tab-selected { background-color: #"
                    + accountTheme.getVtabsheetbgselected() + "; }");
        }

        if (accountTheme.getVtabsheettext() != null) {
            demoExtraStyles.append(".example-block .vertical-tabsheet .v-button-tab > .v-button-wrap > .v-button-caption { color: #"
                    + accountTheme.getVtabsheettext() + "; }");
        }

        if (accountTheme.getVtabsheettextselected() != null) {
            demoExtraStyles.append(".example-block .vertical-tabsheet .v-button-tab.tab-selected > .v-button-wrap > .v-button-caption { color: #"
                    + accountTheme.getVtabsheettextselected() + "; }");
        }

		/* Action Buttons */
        if (accountTheme.getActionbtn() != null) {
            demoExtraStyles.append(".example-block .v-button.v-button-greenbtn, .example-block .v-button-greenbtn:focus { background-color: #"
                    + accountTheme.getActionbtn() + "; }");
        }

        if (accountTheme.getActionbtntext() != null) {
            demoExtraStyles.append(".example-block .v-button.v-button-greenbtn, .example-block .v-button-greenbtn:focus { color: #"
                    + accountTheme.getActionbtntext() + "; }");
        }

		/* Option Buttons */

        if (accountTheme.getOptionbtn() != null) {
            demoExtraStyles.append(".example-block .v-button.v-button-graybtn, .example-block .v-button-graybtn:focus { background-color: #"
                    + accountTheme.getOptionbtn() + "; }");
        }

        if (accountTheme.getOptionbtntext() != null) {
            demoExtraStyles.append(".example-block .v-button.v-button-graybtn, .example-block .v-button-graybtn:focus { color: #"
                    + accountTheme.getOptionbtntext() + "; }");
        }

		/* Danger Buttons */
        if (accountTheme.getDangerbtn() != null) {
            demoExtraStyles.append(".example-block .v-button.v-button-redbtn, .example-block .v-button-redbtn:focus { background-color: #"
                    + accountTheme.getDangerbtn() + "; }");
        }

        if (accountTheme.getDangerbtntext() != null) {
            demoExtraStyles.append(".example-block .v-button.v-button-redbtn, .example-block .v-button-redbtn:focus { color: #"
                    + accountTheme.getDangerbtntext() + "; }");
        }

        if (demoExtraStyles.length() > 0) {
            Page.getCurrent().getStyles().add(demoExtraStyles.toString());
        }
    }
}
