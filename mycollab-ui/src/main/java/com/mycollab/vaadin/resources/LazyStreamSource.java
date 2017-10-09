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
