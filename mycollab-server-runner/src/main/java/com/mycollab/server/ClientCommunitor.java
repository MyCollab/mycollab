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
package com.mycollab.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author MyCollab Ltd.
 * @since 5.0.7
 */
public class ClientCommunitor {
    private static Logger LOG = LoggerFactory.getLogger(ClientCommunitor.class);

    private int port;

    public ClientCommunitor(int port) {
        this.port = port;
    }

    public void reloadRequest(File file) {
        LOG.info(String.format("Send update request to the main process at port %d with file %s",
                port, file.getAbsolutePath()));
        try (Socket socket = new Socket("localhost", port);
             OutputStream outputStream = socket.getOutputStream();
             DataOutputStream dataOutputStream = new DataOutputStream(outputStream)) {
            dataOutputStream.writeUTF("RELOAD:" + file.getAbsolutePath());
        } catch (Exception e) {
            LOG.error("Error while send RELOAD request to the host process", e);
        }
    }

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 53631);
             OutputStream outputStream = socket.getOutputStream();
             DataOutputStream dataOutputStream = new DataOutputStream(outputStream)) {
            dataOutputStream.writeUTF("RELOAD MYCOLLAB");
        } catch (Exception e) {
            LOG.error("Error while send reload request to the host process", e);
        }
    }
}
