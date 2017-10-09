package com.mycollab.community.common.service;

import com.mycollab.common.service.AppPropertiesService;
import com.mycollab.core.utils.DateTimeUtils;
import com.mycollab.core.utils.FileUtils;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.UUID;

/**
 * @author MyCollab Ltd
 * @since 5.2.12
 */
@Service
public class AppPropertiesServiceImpl implements AppPropertiesService, InitializingBean {
    private static final Logger LOG = LoggerFactory.getLogger(AppPropertiesServiceImpl.class);

    private Properties properties;

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            File homeFolder = FileUtils.getHomeFolder();
            File sysFile = new File(homeFolder, ".app.properties");
            properties = new Properties();
            if (sysFile.isFile() && sysFile.exists()) {
                properties.load(new FileInputStream(sysFile));
                String startDate = properties.getProperty("startdate");
                if (startDate == null) {
                    properties.setProperty("startdate", DateTimeUtils.formatDateToW3C(new GregorianCalendar().getTime()));
                }
                properties.setProperty("edition", getEdition());
                properties.store(new FileOutputStream(sysFile), "");
            } else {
                properties.setProperty("id", UUID.randomUUID().toString() + new LocalDateTime().getMillisOfSecond());
                properties.setProperty("startdate", DateTimeUtils.formatDateToW3C(new GregorianCalendar().getTime()));
                properties.setProperty("edition", getEdition());
                properties.store(new FileOutputStream(sysFile), "");
            }
        } catch (IOException e) {
            LOG.error("Error", e);
        }
    }

    @Override
    public String getSysId() {
        return properties.getProperty("id", UUID.randomUUID().toString() + new LocalDateTime().getMillisOfSecond());
    }

    @Override
    public Date getStartDate() {
        try {
            String dateValue = properties.getProperty("startdate");
            return DateTimeUtils.convertDateByString(dateValue, "yyyy-MM-dd'T'HH:mm:ss");
        } catch (Exception e) {
            return new GregorianCalendar().getTime();
        }
    }

    @Override
    public String getEdition() {
        return "Community";
    }
}
