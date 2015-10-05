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
package com.esofthead.mycollab.vaadin.ui;

import com.esofthead.mycollab.core.UserInvalidInputException;
import com.esofthead.mycollab.module.user.domain.AccountTheme;
import com.esofthead.mycollab.module.user.service.AccountThemeService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.server.Page;

/**
 * @author MyCollab Ltd.
 * @since 4.1.2
 */
public class ThemeManager {

    public static void loadUserTheme(int sAccountId) {
        AccountThemeService themeService = ApplicationContextUtil.getSpringBean(AccountThemeService.class);

        AccountTheme accountTheme = themeService.findTheme(sAccountId);

        if (accountTheme == null) {
            accountTheme = themeService.findDefaultTheme(AppContext.getAccountId());
            if (accountTheme == null) {
                throw new UserInvalidInputException("Can not load theme for this account. You may get bad experience " +
                        "while using MyCollab. Please contact your site admoinistrator to solve this issue or fill a " +
                        "support request to MyCollab team" +
                        " ");
            }
        }

        StringBuilder extraStyles = new StringBuilder();
		/* Top Menu */
        if (accountTheme.getTopmenubg() != null) {
            extraStyles.append(".topNavigation { background-color: #" + accountTheme.getTopmenubg() + "; }");
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

            extraStyles.append(".topNavigation .serviceMenuContainer .service-menu.v-buttongroup .v-button:hover { color: #"
                    + accountTheme.getTopmenutextselected() + "; }");

            extraStyles.append(".v-button.add-btn-popup:hover { color: #"
                    + accountTheme.getTopmenutextselected() + "; }");
        }

		/* Vertical Tabsheet */

        if (accountTheme.getVtabsheetbg() != null) {
            extraStyles.append(".vertical-tabsheet .navigator-wrap { background-color: #"
                    + accountTheme.getVtabsheetbg() + "; }");
        }

        if (accountTheme.getVtabsheetbgselected() != null) {
            extraStyles.append(".v-progressbar-indicator { background-color: #"
                    + accountTheme.getVtabsheetbgselected() + "; }");

            extraStyles.append(".v-progressbar.medium .v-progressbar-indicator { background-color: #"
                    + accountTheme.getVtabsheetbgselected() + "; }");

            extraStyles.append(".vertical-tabsheet .v-button-tab.tab-selected > .v-button-wrap { background-color: #"
                    + accountTheme.getVtabsheetbgselected() + "; }");

            extraStyles.append(".sidebar-menu .v-button:hover {background-color: #" + accountTheme
                    .getVtabsheetbgselected() + ";}");
        }

        if (accountTheme.getVtabsheettext() != null) {
            extraStyles.append(".vertical-tabsheet .v-button-tab > .v-button-wrap { color: #"
                    + accountTheme.getVtabsheettext() + "; }");
        }

        if (accountTheme.getVtabsheettextselected() != null) {
            extraStyles.append(".vertical-tabsheet .v-button-tab.tab-selected > .v-button-wrap { color: #"
                    + accountTheme.getVtabsheettextselected() + "; }");

            //Color while hover on sidebar menu
            extraStyles.append(".vertical-tabsheet .v-button-tab .v-button-wrap:hover {color: #" + accountTheme
                    .getVtabsheettextselected() + "!important;}");
        }

		/* Tabsheet */
        if (accountTheme.getTabsheetbg() != null) {
            extraStyles.append(".tab-style3 > .v-tabsheet-tabcontainer > .v-tabsheet-tabs > tbody > tr > .v-tabsheet-tabitemcell > " +
                    ".v-tabsheet-tabitem { background-color: #"
                    + accountTheme.getTabsheetbg() + "; }");
        }

        if (accountTheme.getTabsheetbgselected() != null) {
            extraStyles.append(".project-info { background-color: #"
                    + accountTheme.getTabsheetbgselected() + "; }");


            extraStyles.append(".h-sidebar-menu, .projectfeed-hdr-wrapper { background-color: #"
                    + accountTheme.getTabsheetbgselected() + "; }");

            //Set style of popup content
            extraStyles.append(".optionPopupContent .action-wrap .v-button-action { color: #" + accountTheme
                    .getTabsheetbgselected() + "; }");

            //Set link style
            extraStyles.append(".v-app a { color: #" + accountTheme.getTabsheetbgselected() + "; }");

            // Set button link
            extraStyles.append(".v-button.v-button-link { color: #" + accountTheme
                    .getTabsheetbgselected() + "; }");

            extraStyles.append(".tab-style3 > .v-tabsheet-tabcontainer > .v-tabsheet-tabs > tbody > tr >" +
                    " .v-tabsheet-tabitemcell.v-tabsheet-tabitemcell-selected > .v-tabsheet-tabitem { " +
                    "background-color: #" + accountTheme.getTabsheetbgselected() + "; }");

            extraStyles.append(".tab-style3 > .v-tabsheet-tabcontainer > .v-tabsheet-tabs > tbody > tr > " +
                    ".v-tabsheet-tabitemcell > .v-tabsheet-tabitem:hover {background-color: #" +
                    accountTheme.getTabsheetbgselected() + ";}");

            //Set milestone header background
            extraStyles.append(".milestone-view-header {background-color: #" + accountTheme.getTabsheetbgselected() + ";}");

            //Volume display bar in file manager
            extraStyles.append(".v-label.volumeUsageInfo { border-top: 25px solid #" + accountTheme
                    .getTabsheetbgselected() + ";}");
        }

        if (accountTheme.getTabsheettext() != null) {
            extraStyles.append(".tab-style3 > .v-tabsheet-tabcontainer > .v-tabsheet-tabs > tbody > tr > .v-tabsheet-tabitemcell > .v-tabsheet-tabitem { color: #"
                    + accountTheme.getTabsheettext() + "; }");
        }

        if (accountTheme.getTabsheettextselected() != null) {
            extraStyles.append(".tab-style3 > .v-tabsheet-tabcontainer > .v-tabsheet-tabs > tbody > tr > .v-tabsheet-tabitemcell > .v-tabsheet-tabitem.v-tabsheet-tabitem-selected { color: #"
                    + accountTheme.getTabsheettextselected() + "; }");

            extraStyles.append(".tab-style3 > .v-tabsheet-tabcontainer > .v-tabsheet-tabs > tbody > tr > " +
                    ".v-tabsheet-tabitemcell > .v-tabsheet-tabitem:hover {color: #" +
                    accountTheme.getTabsheettextselected() + ";}");

            //Set milestone header text color
            extraStyles.append(".milestone-view-header {color: #" + accountTheme.getTabsheettextselected() + ";}");

            //Volume text display bar in file manager
            extraStyles.append(".v-label.volumeUsageInfo div { color: #" + accountTheme
                    .getTabsheettextselected() + ";}");
        }

		/* Action Buttons */

        if (accountTheme.getActionbtn() != null) {
            extraStyles.append(".v-button.v-button-greenbtn, .v-button-greenbtn:focus { background-color: #" + accountTheme.getActionbtn()
                    + "; border-color: #" + getBorderColor(accountTheme.getActionbtn()) + "; }");

            extraStyles.append(".v-button.add-project-btn {background-color: #" + accountTheme
                    .getActionbtn() + ";}");

            extraStyles.append(".upload-field .v-upload-immediate .v-button {background-color: #" +
                    accountTheme.getActionbtn() + ";}");

            extraStyles.append(".optionPopupContent .action-wrap:hover {" +
                    "background-color: #" + accountTheme.getActionbtn() + "};");

            extraStyles.append(".v-buttongroup.toggle-btn-group .v-button.active { background-color: #" + accountTheme.getActionbtn()
                    + "; border-color: #" + getBorderColor(accountTheme.getActionbtn()) + "; }");

            //Button paging
            extraStyles.append("div.v-button-link.buttonPaging.current, div.v-button-link" +
                    ".buttonPaging:hover { background-color:#" + accountTheme.getActionbtn() + "; }");

            //Selection background of selected item
            extraStyles.append(".v-filterselect-suggestpopup .gwt-MenuItem-selected { background-color:#" +
                    accountTheme.getActionbtn() + "; }");

            //Year block of activity stream
            extraStyles.append(".v-label.year-lbl { box-shadow: 0 0 0 5px #" + accountTheme
                    .getActionbtn() + ";}");

            //Date label of activity stream
            extraStyles.append(".feed-block-wrap .date-lbl { background-color:#" + accountTheme
                    .getActionbtn() + ";}");

            extraStyles.append(".feed-block-wrap .date-lbl::after{ border-left-color:#" + accountTheme
                    .getActionbtn() + ";}");

            // Add style for tree selected
            extraStyles.append("div.v-tree-node-selected {background-color:#" + accountTheme
                    .getActionbtn() + ";}");

            extraStyles.append("div.v-tree-node-selected span {background-color:#" + accountTheme
                    .getActionbtn() + ";}");

            // Button group default button
            extraStyles.append(".v-buttongroup.toggle-btn-group .v-button.btn-group-default {background-color:#" + accountTheme
                    .getActionbtn() + ";}");
        }

        if (accountTheme.getActionbtntext() != null) {
            extraStyles.append(".v-button.v-button-greenbtn, .v-button-greenbtn:focus { color: #"
                    + accountTheme.getActionbtntext() + "; }");

            extraStyles.append(".optionPopupContent .action-wrap .v-button-action .v-button-wrap:hover" +
                    " {" + "color: #" + accountTheme.getActionbtntext() + "};");

            //Button paging
            extraStyles.append("div.v-button-link.buttonPaging.current, div.v-button-link" +
                    ".buttonPaging:hover { color:#" + accountTheme.getActionbtntext() + "; }");

            //Selection text color of selected item
            extraStyles.append(".v-filterselect-suggestpopup .gwt-MenuItem-selected { color:#" +
                    accountTheme.getActionbtntext() + "; }");

            //Date label of activity stream
            extraStyles.append(".feed-block-wrap .date-lbl { color:#" + accountTheme
                    .getActionbtntext() + ";}");

            //Style for tree
            extraStyles.append("div.v-tree-node-selected span {color:#" + accountTheme
                    .getActionbtntext() + ";}");
        }

		/* Option Buttons */

        if (accountTheme.getOptionbtn() != null) {
            extraStyles.append(".v-button.v-button-graybtn, .v-button-graybtn:focus { background-color: #"
                    + accountTheme.getOptionbtn() + "; border-color: #" + getBorderColor(accountTheme.getOptionbtn())
                    + "; }");

            //Set toogle button group background
            extraStyles.append(".v-buttongroup.toggle-btn-group .v-button { background-color: #" +
                    accountTheme.getOptionbtn() + "; border-color: #" + getBorderColor(accountTheme.getOptionbtn()) + ";}");
        }

        if (accountTheme.getOptionbtntext() != null) {
            extraStyles.append(".v-button.v-button-graybtn, .v-button-graybtn:focus { color: #" + accountTheme.getOptionbtntext() + "; }");

            extraStyles.append(".v-buttongroup.toggle-btn-group .v-button { color: #"
                    + accountTheme.getOptionbtntext() + "; }");
        }

		/* Danger Buttons */

        if (accountTheme.getDangerbtn() != null) {
            extraStyles.append(".v-button.v-button-redbtn, .v-button-redbtn:focus { background-color: #"
                            + accountTheme.getDangerbtn() + "; border-color: #"
                            + getBorderColor(accountTheme.getDangerbtn()) + "; }");
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
            demoExtraStyles.append(".example-block .vertical-tabsheet .v-button-tab.tab-selected > .v-button-wrap { background-color: #"
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

		/* Tabsheet */

        if (accountTheme.getTabsheetbg() != null) {
            demoExtraStyles.append(".example-block .tab-style3 > .v-tabsheet-tabcontainer > .v-tabsheet-tabs > tbody > tr > .v-tabsheet-tabitemcell > .v-tabsheet-tabitem { background-color: #"
                    + accountTheme.getTabsheetbg() + "; }");
        }

        if (accountTheme.getTabsheetbgselected() != null) {
            demoExtraStyles.append(".example-block .tab-style3 > .v-tabsheet-tabcontainer > .v-tabsheet-tabs > tbody > tr > .v-tabsheet-tabitemcell.v-tabsheet-tabitemcell-selected > .v-tabsheet-tabitem { background-color: #"
                    + accountTheme.getTabsheetbgselected() + "; }");
        }

        if (accountTheme.getTabsheettext() != null) {
            demoExtraStyles.append(".example-block .tab-style3 > .v-tabsheet-tabcontainer > .v-tabsheet-tabs > tbody > tr > .v-tabsheet-tabitemcell > .v-tabsheet-tabitem .v-caption .v-captiontext { color: #"
                            + accountTheme.getTabsheettext() + "; }");
        }

        if (accountTheme.getTabsheettextselected() != null) {
            demoExtraStyles.append(".example-block .tab-style3 > .v-tabsheet-tabcontainer > .v-tabsheet-tabs > tbody > tr > .v-tabsheet-tabitemcell > .v-tabsheet-tabitem.v-tabsheet-tabitem-selected .v-caption .v-captiontext { color: #"
                    + accountTheme.getTabsheettextselected() + "; }");
        }

		/* Action Buttons */

        if (accountTheme.getActionbtn() != null) {
            demoExtraStyles.append(".example-block .v-button.v-button-greenbtn, .example-block .v-button-greenbtn:focus { background-color: #"
                    + accountTheme.getActionbtn()
                    + "; border-color: #"
                    + getBorderColor(accountTheme.getActionbtn())
                    + "; }");
        }

        if (accountTheme.getActionbtntext() != null) {
            demoExtraStyles.append(".example-block .v-button.v-button-greenbtn, .example-block .v-button-greenbtn:focus { color: #"
                    + accountTheme.getActionbtntext() + "; }");
        }

		/* Option Buttons */

        if (accountTheme.getOptionbtn() != null) {
            demoExtraStyles.append(".example-block .v-button.v-button-graybtn, .example-block .v-button-graybtn:focus { background-color: #"
                    + accountTheme.getOptionbtn()
                    + "; border-color: #"
                    + getBorderColor(accountTheme.getOptionbtn())
                    + "; }");
        }

        if (accountTheme.getOptionbtntext() != null) {
            demoExtraStyles.append(".example-block .v-button.v-button-graybtn, .example-block .v-button-graybtn:focus { color: #"
                    + accountTheme.getOptionbtntext() + "; }");
        }

		/* Danger Buttons */

        if (accountTheme.getDangerbtn() != null) {
            demoExtraStyles.append(".example-block .v-button.v-button-redbtn, .example-block .v-button-redbtn:focus { background-color: #"
                    + accountTheme.getDangerbtn()
                    + "; border-color: #"
                    + getBorderColor(accountTheme.getDangerbtn())
                    + "; }");
        }

        if (accountTheme.getDangerbtntext() != null) {
            demoExtraStyles.append(".example-block .v-button.v-button-redbtn, .example-block .v-button-redbtn:focus { color: #"
                            + accountTheme.getDangerbtntext() + "; }");
        }

        if (demoExtraStyles.length() > 0) {
            Page.getCurrent().getStyles().add(demoExtraStyles.toString());
        }

    }

    private static String getBorderColor(String bgColor) {
        String rgb = "";
        Long c;
        for (int i = 0; i < 3; i++) {
            c = Long.valueOf(bgColor.substring(i * 2, i * 2 + 2), 16);
            c = Math.round(Math.min(Math.max(0, c - c * 0.2), 255));
            String strVal = Long.toHexString(c);
            rgb += ("00" + strVal).substring(strVal.length());
        }

        return rgb;
    }

}
