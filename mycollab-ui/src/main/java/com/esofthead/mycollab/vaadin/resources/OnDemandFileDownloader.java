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
package com.esofthead.mycollab.vaadin.resources;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;

import java.io.IOException;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
public class OnDemandFileDownloader extends FileDownloader {
    private static final long serialVersionUID = 1L;

    private final LazyStreamSource lazyStreamSource;

    public OnDemandFileDownloader(LazyStreamSource lazyStreamSource) {
        super(new StreamResource(lazyStreamSource, ""));
        this.lazyStreamSource = lazyStreamSource;
    }

    @Override
    public boolean handleConnectorRequest(VaadinRequest request, VaadinResponse response, String path) throws IOException {
        try {
            getResource().setFilename(lazyStreamSource.getFilename());
            return super.handleConnectorRequest(request, response, path);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private StreamResource getResource() {
        return (StreamResource) this.getResource("dl");
    }
}
