package com.mycollab.configuration.logging;

import ch.qos.logback.classic.net.SMTPAppender;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Layout;
import com.mycollab.configuration.EmailConfiguration;
import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.core.Version;
import com.mycollab.core.utils.StringUtils;

/**
 * @author MyCollab Ltd
 * @since 5.0.5
 */
public class MailAppender extends SMTPAppender {

    @Override
    protected Layout<ILoggingEvent> makeSubjectLayout(String subjectStr) {
        if (subjectStr == null) {
            String version;
            version = "MyCollab " + Version.getVersion();
            subjectStr = version + " - Error: %logger{20} - %m";
        }

        return super.makeSubjectLayout(subjectStr);
    }


    @Override
    public void start() {
        EmailConfiguration conf = SiteConfiguration.getEmailConfiguration();
        if (StringUtils.isBlank(conf.getHost())) {
            return;
        }

        this.setSMTPHost(conf.getHost());
        this.setSMTPPort(conf.getPort());
        this.setUsername(conf.getUser());
        this.setPassword(conf.getPassword());
        this.setSTARTTLS(conf.getIsStartTls());
        this.setFrom(conf.getNotifyEmail());
        this.addTo(SiteConfiguration.getSendErrorEmail());
        super.start();
    }
}