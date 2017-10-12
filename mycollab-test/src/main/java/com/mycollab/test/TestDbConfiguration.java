/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.test;

import java.io.InputStream;
import java.util.Properties;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class TestDbConfiguration {
    private Properties props;

    public TestDbConfiguration() {
        InputStream stream = getClass().getClassLoader().getResourceAsStream("mycollab.properties");
        if (stream == null) {
            stream = getClass().getClassLoader().getResourceAsStream("default-mycollab-test.properties");
        }

        props = new Properties();
        if (stream != null) {
            try {
                props.load(stream);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String getDriverClassName() {
        return props.getProperty("db.driverClassName", "com.mysql.jdbc.Driver");
    }

    public String getJdbcUrl() {
        return props.getProperty("db.url",
                "jdbc:mysql://localhost/mycollab_test?useUnicode=true&characterEncoding=utf-8&autoReconnect=true");
    }

    public String getUsername() {
        return props.getProperty("db.username", "root");
    }

    public String getPassword() {
        return props.getProperty("db.password", "");
    }
}
