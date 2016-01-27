/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.spring;

import com.esofthead.mycollab.core.MyCollabVersion;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.core.utils.FileUtils;
import com.esofthead.mycollab.core.utils.MiscUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.GregorianCalendar;
import java.util.Properties;

/**
 * @author MyCollab Ltd
 * @since 5.2.5
 */
@Service
public class SystemInfoService implements InitializingBean {
    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            File sysFile = getSysFile();
            Properties props = new Properties();
            if (sysFile.exists() && sysFile.isFile()) {
                props.load(new FileInputStream(sysFile));
                props.setProperty("appVersion", MyCollabVersion.getVersion());
                props.setProperty("javaVersion", System.getProperty("java.version"));
                props.store(new FileOutputStream(sysFile), "");
            } else {
                props.setProperty("appVersion", MyCollabVersion.getVersion());
                props.setProperty("installedDate", DateTimeUtils.formatDateToW3C(new GregorianCalendar().getTime()));
                props.setProperty("javaVersion", System.getProperty("java.version"));
                props.setProperty("sysId", MiscUtils.getMacAddressOfServer());
                props.setProperty("sysProperties", System.getProperty("os.arch") + ":" + System.getProperty("os.name") + ":" +
                        System.getProperty("os.name"));
                props.store(new FileOutputStream(sysFile), "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File getSysFile() {
        File homeDir = FileUtils.getHomeFolder();
        return new File(homeDir, "app.properties");
    }
}
