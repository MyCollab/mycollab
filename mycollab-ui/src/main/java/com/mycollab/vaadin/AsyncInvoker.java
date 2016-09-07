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
package com.mycollab.vaadin;

import com.mycollab.configuration.SiteConfiguration;
import com.vaadin.ui.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author MyCollab Ltd
 * @since 5.2.1
 */
public class AsyncInvoker {
    private static Logger LOG = LoggerFactory.getLogger(AsyncInvoker.class);

    public static void access(final UI ui, final PageCommand pageCommand) {
        if (ui == null) {
            return;
        }
        pageCommand.setUI(ui);
        if (SiteConfiguration.getPullMethod() == SiteConfiguration.PullMethod.push) {
            new Thread() {
                @Override
                public void run() {
                    ui.access(() -> {
                        try {
                            pageCommand.run();
                            ui.push();
                            pageCommand.postRun();
                        } finally {
                            pageCommand.cleanUp();
                            try {
                                ui.push();
                            } catch (Exception e) {
                                LOG.error("Error", e);
                            }
                        }
                    });
                }
            }.start();
        } else {
            ui.getSession().getLockInstance().lock();
            try {
                ui.setPollInterval(1000);
                pageCommand.run();
                pageCommand.postRun();
            } finally {
                pageCommand.cleanUp();
                ui.setPollInterval(-1);
                ui.getSession().getLockInstance().unlock();
            }
        }
    }

    public static abstract class PageCommand {
        UI currentUI;

        void setUI(UI ui) {
            currentUI = ui;
        }

        public UI getUI() {
            return currentUI;
        }

        abstract public void run();

        public void postRun() {
        }

        public void cleanUp() {
        }

        public void push() {
            if (SiteConfiguration.getPullMethod() == SiteConfiguration.PullMethod.push) {
                currentUI.push();
            }
        }
    }
}
