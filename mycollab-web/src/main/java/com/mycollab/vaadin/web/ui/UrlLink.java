package com.mycollab.vaadin.web.ui;

import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Link;

/**
 * Vaadin Url link
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class UrlLink extends Link {
    private static final long serialVersionUID = 1L;

    public UrlLink(String urlLink) {
        this.setResource(new ExternalResource(urlLink));
        this.setCaption(urlLink);
        this.setTargetName("_blank");
    }
}
