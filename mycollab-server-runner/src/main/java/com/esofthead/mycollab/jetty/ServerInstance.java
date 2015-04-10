/**
 * This file is part of mycollab-server-runner.
 *
 * mycollab-server-runner is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-server-runner is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-server-runner.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.jetty;

import com.esofthead.mycollab.core.MyCollabException;

import java.io.File;

/**
 * @author MyCollab Ltd.
 * @since 5.0.4
 */
public class ServerInstance {
    private static ServerInstance instance = new ServerInstance();
    private GenericServerRunner server;

    private boolean isFirstTimeRunner = false;
    private boolean isUpgrading = false;

    private ServerInstance() {}

    public void registerInstance(GenericServerRunner serverProcess) {
        if (server != null) {
            throw new MyCollabException("There is a running server instance already");
        }
        this.server = serverProcess;
    }

    public void preUpgrade() {
        isUpgrading = true;
    }

    public void upgrade(File upgradeFile) {
        server.upgrade(upgradeFile);
    }

    public boolean isUpgrading() {
        return isUpgrading;
    }

    void setIsUpgrading(boolean isUpgrading) {
        this.isUpgrading = isUpgrading;
    }

    public static ServerInstance getInstance() {
        return instance;
    }

    public boolean isFirstTimeRunner() {
        return isFirstTimeRunner;
    }

    public void setIsFirstTimeRunner(boolean isFirstTimeRunner) {
        this.isFirstTimeRunner = isFirstTimeRunner;
    }
}
