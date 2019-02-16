/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.community.shell.view.components;

import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Text;
import com.mycollab.core.Version;
import com.mycollab.core.utils.FileUtils;
import com.mycollab.module.file.StorageUtils;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.AbstractAboutWindow;
import com.mycollab.vaadin.web.ui.WebResourceIds;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.server.WebBrowser;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.time.LocalDate;

/**
 * @author MyCollab Ltd.
 * @since 5.0.5
 */
@ViewComponent
public class AboutWindow extends AbstractAboutWindow {
    public AboutWindow() {

        MHorizontalLayout content = new MHorizontalLayout().withMargin(true).withFullWidth();
        this.setContent(content);

        Image about = new Image("", new ExternalResource(StorageUtils.generateAssetRelativeLink(WebResourceIds._about)));

        MVerticalLayout rightPanel = new MVerticalLayout();
        ELabel versionLbl = ELabel.h2(String.format("MyCollab Community Edition %s", Version.getVersion())).withFullWidth();
        ELabel javaNameLbl = new ELabel(String.format("%s, %s", System.getProperty("java.vm.name"),
                System.getProperty("java.runtime.version"))).withFullWidth();
        Label homeFolderLbl = new Label("Home folder: " + FileUtils.getHomeFolder().getAbsolutePath());
        WebBrowser browser = Page.getCurrent().getWebBrowser();
        ELabel osLbl = new ELabel(String.format("%s, %s", System.getProperty("os.name"), browser.getBrowserApplication())).withFullWidth();
        Div licenseDiv = new Div().appendChild(new Text("Powered by: "))
                .appendChild(new A("https://www.mycollab.com")
                        .appendText("MyCollab")).appendChild(new Text(". Open source under GPL license"));
        ELabel licenseLbl = ELabel.html(licenseDiv.write()).withFullWidth();
        Label copyRightLbl = ELabel.html(String.format("&copy; %s - %s MyCollab Ltd. All rights reserved", "2011",
                LocalDate.now().getYear() + "")).withFullWidth();
        rightPanel.with(versionLbl, javaNameLbl, osLbl, homeFolderLbl, licenseLbl, copyRightLbl).withAlign(copyRightLbl, Alignment.BOTTOM_LEFT);
        content.with(about, rightPanel).expand(rightPanel);
    }
}
