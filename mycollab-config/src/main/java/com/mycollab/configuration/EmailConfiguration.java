package com.mycollab.configuration;

import com.mycollab.core.MyCollabException;
import com.mycollab.core.arguments.ValuedBean;
import com.mycollab.core.utils.StringUtils;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

/**
 * Email configuration of MyCollab
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class EmailConfiguration extends ValuedBean implements Cloneable {
    @NotNull
    private String host;

    @NotNull
    private String user;

    @NotNull
    private String password;

    @Digits(integer = 6, fraction = 0)
    private Integer port = 25;

    private boolean isStartTls = false;
    private boolean isSsl = false;
    private String notifyEmail;

    EmailConfiguration(String host, String username, String password, int port, boolean isStartTls, boolean isSsl, String notifyEmail) {
        this.host = host;
        this.user = username;
        this.password = password;
        this.port = port;
        this.isStartTls = isStartTls;
        this.isSsl = isSsl;
        this.notifyEmail = StringUtils.isBlank(notifyEmail) ? user : notifyEmail;
    }

    public String getHost() {
        return host;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public Integer getPort() {
        return port;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public boolean getIsStartTls() {
        return isStartTls;
    }

    public void setIsStartTls(boolean isStartTls) {
        this.isStartTls = isStartTls;
    }

    public boolean getIsSsl() {
        return isSsl;
    }

    public void setIsSsl(boolean isSsl) {
        this.isSsl = isSsl;
    }

    public EmailConfiguration clone() {
        try {
            return (EmailConfiguration) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new MyCollabException(e);
        }
    }

    public String getNotifyEmail() {
        return notifyEmail;
    }

    public void setNotifyEmail(String notifyEmail) {
        this.notifyEmail = notifyEmail;
    }
}
