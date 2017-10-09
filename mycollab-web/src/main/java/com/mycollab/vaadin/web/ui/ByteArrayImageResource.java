package com.mycollab.vaadin.web.ui;

import com.vaadin.server.StreamResource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class ByteArrayImageResource extends StreamResource {
    private static final long serialVersionUID = 1L;

    public ByteArrayImageResource(final byte[] imageData, String mimeType) {
        super(new StreamResource.StreamSource() {
            private static final long serialVersionUID = 1L;

            public InputStream getStream() {
                return new ByteArrayInputStream(imageData);
            }
        }, "avatar");

        this.setMIMEType(mimeType);
    }
}
