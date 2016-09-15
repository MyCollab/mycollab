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
package com.mycollab.reporting;

import com.mycollab.core.MyCollabException;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.shell.events.ShellEvent;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.vaadin.server.StreamResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

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
        final CountDownLatch latch = new CountDownLatch(1);
        final PipedInputStream inStream = new PipedInputStream();

        InputStream in = new InputStream() {

            @Override
            public int read(byte[] b) throws IOException {
                return inStream.read(b);
            }

            @Override
            public int read() throws IOException {
                return inStream.read();
            }

            @Override
            public void close() throws IOException {
                super.close();
                latch.countDown();
            }
        };

        final PipedOutputStream outputStream = new PipedOutputStream();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final OutputStream out = new OutputStream() {
                    @Override
                    public void write(int b) throws IOException {
                        outputStream.write(b);
                    }

                    @Override
                    public void close() throws IOException {
                        while (latch.getCount() != 0) {
                            try {
                                latch.await();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                break;
                            }
                        }
                        super.close();
                    }
                };

                try {
                    templateExecutor.setParameters(initReportParameters());
                    templateExecutor.initReport();
                    templateExecutor.fillReport();
                    templateExecutor.outputReport(out);
                } catch (Exception e) {
                    EventBusFactory.getInstance().post(new ShellEvent.NotifyErrorEvent(ReportStreamSource.this, e));
                } finally {
                    try {
                        outputStream.close();
                        out.close();
                    } catch (IOException e) {
                        LOG.error("Try to close reporting stream error", e);
                    }
                }
            }
        }).start();
        try {
            outputStream.connect(inStream);
        } catch (IOException e) {
            throw new MyCollabException(e);
        }
        return in;
    }

    private Map<String, Object> initReportParameters() {
        Map<String, Object> parameters = new ConcurrentHashMap<>();
        parameters.put("siteUrl", MyCollabUI.getSiteUrl());
        parameters.put("user", UserUIContext.getUser());
        initReportParameters(parameters);
        return parameters;
    }

    protected abstract void initReportParameters(Map<String, Object> parameters);
}
