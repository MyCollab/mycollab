package com.mycollab.server;

import com.mycollab.core.MyCollabException;
import com.mycollab.core.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileWriter;

/**
 * @author MyCollab Ltd
 * @since 5.5.0
 */
@Configuration
@EnableAutoConfiguration(exclude = {FreeMarkerAutoConfiguration.class, DataSourceAutoConfiguration.class,
        FlywayAutoConfiguration.class})
@ComponentScan(basePackages = {"com.mycollab.**.spring, com.mycollab.**.configuration, com.mycollab.**.servlet"})
public class DefaultServerRunner {
    private static Logger LOG = LoggerFactory.getLogger(DefaultServerRunner.class);

    public static String PID_FILE = ".mycollab.pid";

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(DefaultServerRunner.class);
        if (!checkConfigFileExist()) {
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
            application.setAdditionalProfiles("production");
            application.run(args);
        }
    }

    private static boolean checkConfigFileExist() {
        File confFolder = FileUtils.getDesireFile(FileUtils.getUserFolder(), "config", "src/main/config");
        return confFolder != null && new File(confFolder, "mycollab.properties").exists();
    }
}
