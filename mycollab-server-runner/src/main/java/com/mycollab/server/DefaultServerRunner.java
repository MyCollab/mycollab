/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.server;

import com.mycollab.core.MyCollabException;
import com.mycollab.core.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.io.File;
import java.io.FileWriter;

/**
 * @author MyCollab Ltd
 * @since 5.5.0
 */
public class DefaultServerRunner {
    private static Logger LOG = LoggerFactory.getLogger(DefaultServerRunner.class);

    public static String PID_FILE = ".mycollab.pid";

    public static void main(String[] args) {
        if (!checkConfigFileExist()) {
            SpringApplication application = new SpringApplication(SetupApp.class);
            application.setAdditionalProfiles("setup");
            application.run(args);
            while (!checkConfigFileExist()) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new MyCollabException(e);
                }
            }
            try (FileWriter writer = new FileWriter(new File(FileUtils.getUserFolder(), PID_FILE), false)) {
                writer.write("RESTART");
            } catch (Exception e) {
                LOG.error("Error when restart server", e);
            }
        } else {
            SpringApplication application = new SpringApplication(MainApp.class);
            application.run(args);
        }
    }

    private static boolean checkConfigFileExist() {
        File confFolder = FileUtils.getDesireFile(FileUtils.getUserFolder(), "config", "src/main/config");
        return confFolder != null && new File(confFolder, "application.properties").exists();
    }

    @SpringBootApplication(exclude = {FreeMarkerAutoConfiguration.class, FlywayAutoConfiguration.class, DataSourceAutoConfiguration.class},
            scanBasePackages = "com.mycollab.installation")
    static class SetupApp {
    }

    @SpringBootApplication(exclude = {FreeMarkerAutoConfiguration.class, FlywayAutoConfiguration.class},
            scanBasePackages = "com.mycollab.**.spring, com.mycollab.**.configuration, com.mycollab.**.servlet")
    static class MainApp {
    }
}
