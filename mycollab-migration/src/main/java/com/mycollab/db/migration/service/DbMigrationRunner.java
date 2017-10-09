package com.mycollab.db.migration.service;

import com.mycollab.configuration.IDeploymentMode;
import com.zaxxer.hikari.pool.HikariPool;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Component("dbMigration")
@Profile({"production", "test"})
@DependsOn("appContextUtil")
public class DbMigrationRunner {
    private static final Logger LOG = LoggerFactory.getLogger(DbMigrationRunner.class);

    @Autowired
    private DataSource dataSource;

    @Autowired
    private IDeploymentMode deploymentMode;

    @PostConstruct
    public void migrate() {
        try {
            Flyway flyway = new Flyway();
            flyway.setBaselineOnMigrate(true);
            flyway.setDataSource(dataSource);
            flyway.setValidateOnMigrate(false);
            if (deploymentMode.isDemandEdition()) {
                flyway.setLocations("db/migration", "db/migration2");
            } else {
                flyway.setLocations("db/migration");
            }

            boolean doMigrateLoop = true;
            while (doMigrateLoop) {
                try {
                    flyway.migrate();
                    doMigrateLoop = false;
                } catch (HikariPool.PoolInitializationException e) {
                    LOG.info("Error: {}", e.getMessage());
                }
            }
        } catch (Exception e) {
            LOG.error("Error while migrate database", e);
            System.exit(-1);
        }
    }
}
