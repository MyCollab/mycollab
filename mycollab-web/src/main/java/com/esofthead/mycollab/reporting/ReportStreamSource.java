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
package com.esofthead.mycollab.reporting;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.shell.events.ShellEvent;
import com.vaadin.server.StreamResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Map;

/**
 * @author MyCollab Ltd
 * @since 5.1.4
 */
public abstract class ReportStreamSource implements StreamResource.StreamSource {
    private static Logger LOG = LoggerFactory.getLogger(ReportStreamSource.class);

    private ReportTemplateExecutor templateExecutor;

    public ReportStreamSource(ReportTemplateExecutor templateExecutor) {
        this.templateExecutor = templateExecutor;
    }

    @Override
    public InputStream getStream() {
        final PipedInputStream inStream = new PipedInputStream();
        final PipedOutputStream outStream = new PipedOutputStream();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    templateExecutor.setParameters(initReportParameters());
                    templateExecutor.initReport();
                    templateExecutor.fillReport();
                    templateExecutor.outputReport(outStream);
                } catch (Exception e) {
                    EventBusFactory.getInstance().post(new ShellEvent.NotifyErrorEvent(ReportStreamSource.this, e));
                } finally {
                    try {
                        outStream.close();
                    } catch (IOException e) {
                        LOG.error("Try to close reporting stream error", e);
                    }
                }
            }
        }).start();
        try {
            outStream.connect(inStream);
        } catch (IOException e) {
            throw new MyCollabException(e);
        }
        return inStream;
    }

    protected abstract Map<String, Object> initReportParameters();
}
