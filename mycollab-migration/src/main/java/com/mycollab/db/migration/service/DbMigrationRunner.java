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
package com.mycollab.db.migration.service;

import com.mycollab.configuration.IDeploymentMode;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Component("dbMigration")
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
            String dbProductName = dataSource.getConnection().getMetaData().getDatabaseProductName();
            String[] locations;

            if (dbProductName.equals("H2")) {
                locations = new String[]{"db/migration/common", "db/migration/h2"};
            } else if (dbProductName.equals("PostgreSQL")) {
                locations = new String[]{"db/migration/common", "db/migration/postgresql"};
            } else {
                locations = deploymentMode.isDemandEdition() ? new String[]{"db/migration/common", "db/migration/mysql", "db/migration2"} : new String[]{"db/migration/common", "db/migration/mysql"};
            }

            Flyway flyway = Flyway.configure().baselineOnMigrate(true).dataSource(dataSource).validateOnMigrate(false).locations(locations).load();
            flyway.migrate();
        } catch (Exception e) {
            LOG.error("Error while migrate database", e);
            System.exit(-1);
        }
    }
}
