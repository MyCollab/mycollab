/**
 * This file is part of mycollab-core.
 *
 * mycollab-core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-core.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.core;

import com.esofthead.mycollab.core.utils.FileUtils;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

/**
 * @author MyCollab Ltd
 * @since 5.2.9
 */
public class SystemProperties {
    private static final Logger LOG = LoggerFactory.getLogger(SystemProperties.class);

    private static Properties properties;
    private static String id;

    static {
        try {
            File homeFolder = FileUtils.getHomeFolder();
            File sysFile = new File(homeFolder, ".app.properties");
            properties = new Properties();
            if (sysFile.isFile() && sysFile.exists()) {
                properties.load(new FileInputStream(sysFile));
            } else {
                properties.setProperty("id", UUID.randomUUID().toString() + new LocalDateTime().getMillisOfSecond());
                properties.store(new FileOutputStream(sysFile), "");
            }
        } catch (IOException e) {
            LOG.error("Error", e);
        }
    }

    public static String getId() {
        return properties.getProperty("id", UUID.randomUUID().toString() + new LocalDateTime().getMillisOfSecond());
    }
}
