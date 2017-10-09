package com.mycollab.common.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.mycollab.core.arguments.ValuedBean;
import org.apache.commons.lang.StringUtils;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class MailRecipientField extends ValuedBean {
    private static final long serialVersionUID = 1L;

    private String email;
    private String name;

    @JsonCreator
    public MailRecipientField(String email, String name) {
        this.email = email;
        this.name = StringUtils.isNotBlank(name) ? name : email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
