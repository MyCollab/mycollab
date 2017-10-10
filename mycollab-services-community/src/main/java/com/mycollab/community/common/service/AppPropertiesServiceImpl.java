/**
 * mycollab-services-community - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
