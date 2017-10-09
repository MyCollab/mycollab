package com.mycollab.module.ecm;

import com.mycollab.core.MyCollabException;

/**
 * Generic exception relate to MyCollab storage processing.
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ContentException extends MyCollabException {
    private static final long serialVersionUID = 1L;

    public ContentException(String message) {
        super(message);
    }

}
