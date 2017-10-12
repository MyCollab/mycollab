/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.resources;

import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;

import java.io.InputStream;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
public abstract class LazyStreamSource implements StreamResource.StreamSource {
    private static final long serialVersionUID = 1L;

    @Override
    public InputStream getStream() {
        StreamSource streamSource = buildStreamSource();
        return streamSource.getStream();
    }

    public String getFilename() {
        return null;
    }

    abstract protected StreamSource buildStreamSource();

}
