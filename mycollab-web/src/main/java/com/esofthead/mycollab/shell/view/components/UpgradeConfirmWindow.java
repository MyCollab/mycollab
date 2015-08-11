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
package com.esofthead.mycollab.shell.view.components;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.utils.FileUtils;
import com.esofthead.mycollab.jetty.ServerInstance;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.esofthead.mycollab.vaadin.ui.ProgressBarIndicator;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.locks.Lock;

/**
 * @author MyCollab Ltd.
 * @since 5.0.4
 */
public class UpgradeConfirmWindow extends Window {
    private static String headerTemplate = "MyCollab just got better . For the " +
            "enhancements and security purpose, you should upgrade to the latest version";

    private Properties props;

    public UpgradeConfirmWindow(Properties props) {
        super("A new update is ready to install");
        this.props = props;
        this.setModal(true);
        this.setResizable(false);
        this.center();
        this.setWidth("600px");

        MVerticalLayout content = new MVerticalLayout();
        this.setContent(content);

        Div titleDiv = new Div().appendText(String.format(headerTemplate, props.getProperty("version"))).setStyle("font-weight:bold");
        content.with(new Label(titleDiv.write(), ContentMode.HTML));

        Div manualInstallLink = new Div().appendText("&nbsp;&nbsp;&nbsp;&nbsp;Manual install: ")
                .appendChild(new A(props.getProperty("downloadLink"), "_blank")
                        .appendText("Download link"));
        content.with(new Label(manualInstallLink.write(), ContentMode.HTML));

        Div releaseNoteLink = new Div().appendText("&nbsp;&nbsp;&nbsp;&nbsp;Release Notes: ")
                .appendChild(new A(props.getProperty("releaseNotes"), "_blank")
                        .appendText("Link"));
        content.with(new Label(releaseNoteLink.write(), ContentMode.HTML));

        MHorizontalLayout buttonControls = new MHorizontalLayout().withMargin(true);
        Button skipBtn = new Button("Skip", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                UpgradeConfirmWindow.this.close();
            }
        });
        skipBtn.addStyleName(UIConstants.THEME_GRAY_LINK);

        Button autoUpgradeBtn = new Button("Auto Upgrade", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                new Thread(new AutoUpgradeProcess()).start();
                UpgradeConfirmWindow.this.close();
            }
        });
        autoUpgradeBtn.addStyleName(UIConstants.THEME_GREEN_LINK);
        buttonControls.with(skipBtn, autoUpgradeBtn);
        content.with(buttonControls).withAlign(buttonControls, Alignment.MIDDLE_RIGHT);
    }

    class AutoUpgradeProcess implements Runnable {
        private boolean isKill = false;

        @Override
        public void run() {
            try {
                Lock lock = UI.getCurrent().getSession().getLockInstance();
                lock.lock();
                DownloadProgressWindow progressWindow = new DownloadProgressWindow();
                UI.getCurrent().addWindow(progressWindow);
                lock.unlock();
                File tmpFile = File.createTempFile("mycollab", ".zip");
                URL url = new URL(props.getProperty("autoDownload"));
                HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                int responseCode = httpConn.getResponseCode();

                // always check HTTP response code first
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    int contentLength = httpConn.getContentLength();
                    progressWindow.setContentLength(Long.valueOf(contentLength));

                    // opens input stream from the HTTP connection
                    InputStream inputStream = httpConn.getInputStream();

                    // opens an output stream to save into file
                    try(FileOutputStream outputStream = new FileOutputStream(tmpFile)) {
                        long currentBytesRead = 0;
                        int bytesRead;
                        byte[] buffer = new byte[4096];
                        while (((bytesRead = inputStream.read(buffer)) != -1) && !isKill) {
                            outputStream.write(buffer, 0, bytesRead);
                            currentBytesRead += bytesRead;
                            progressWindow.setProgressValue(currentBytesRead);
                        }
                        outputStream.close();
                        inputStream.close();
                        httpConn.disconnect();

                        if (isKill) {
                            progressWindow.close();
                            return;
                        }

                        UI.getCurrent().setPollInterval(-1);
                        ServerInstance.getInstance().preUpgrade();
                        String locUrl = SiteConfiguration.getSiteUrl(AppContext.getSubDomain()) + "it/upgrade";
                        lock = UI.getCurrent().getSession().getLockInstance();
                        lock.lock();
                        Page.getCurrent().setLocation(locUrl);
                        progressWindow.close();
                        lock.unlock();

                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        ServerInstance.getInstance().upgrade(tmpFile);
                    }
                } else {
                    NotificationUtil.showErrorNotification("Can not download the latest MyCollab distribution. You could try again or install MyCollab manually");
                    httpConn.disconnect();
                    progressWindow.close();
                }
            } catch (IOException e) {
                NotificationUtil.showErrorNotification("Can not download the latest MyCollab distribution. You could try again or install MyCollab manually");
            } finally {
                if (UI.getCurrent() != null) {
                    UI.getCurrent().push();
                }
            }
        }

        class DownloadProgressWindow extends Window {
            private Long contentLength;
            private MVerticalLayout content;
            private ProgressBarIndicator progressBar;
            private Label currentVolumeLabel;

            DownloadProgressWindow() {
                super("Upgrading MyCollab (Beta)");
                this.setResizable(false);
                this.setModal(false);
                this.setWidth("600px");

                content = new MVerticalLayout();
                content.with(new Label("Connecting to the server. Please wait for a moment"));
                this.setContent(content);
            }

            void setContentLength(Long contentLength) {
                this.contentLength = contentLength;
                content.removeAllComponents();
                MHorizontalLayout progressLayout = new MHorizontalLayout();
                Label totalVolumeLabel = new Label(FileUtils.getVolumeDisplay(contentLength));
                currentVolumeLabel = new Label();
                progressBar = new ProgressBarIndicator();
                progressBar.setWidth("100%");
                progressLayout.with(progressBar, new MHorizontalLayout().with(currentVolumeLabel, new Label("/"), totalVolumeLabel)).expand(progressBar);
                content.with(new Label("Downloading the latest MyCollab distribution. Please be patient"), progressLayout);
                Button cancelBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL), new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent clickEvent) {
                        isKill = true;
                    }
                });
                cancelBtn.addStyleName(UIConstants.THEME_GRAY_LINK);
                content.with(cancelBtn).withAlign(cancelBtn, Alignment.MIDDLE_RIGHT);
            }

            void setProgressValue(Long value) {
                progressBar.setProgressValue((float) value / contentLength);
                currentVolumeLabel.setValue(FileUtils.getVolumeDisplay(value));
            }
        }
    }
}
