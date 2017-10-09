package com.mycollab.vaadin.resources;

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
