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
package com.mycollab.module.ecm;

import com.mycollab.spring.AppContextUtil;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.jackrabbit.core.fs.db.DbFileSystem;
import org.apache.jackrabbit.core.util.db.ConnectionHelper;

import javax.sql.DataSource;

/**
 * Db file system of mycollab jackrabbit storage
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class DbFileSystemExt extends DbFileSystem {

    public DbFileSystemExt() {
        HikariDataSource ds = AppContextUtil.getSpringBean(HikariDataSource.class);
        this.schema = DbUtil.getSchemaType(ds.getDriverClassName());
        this.driver = ds.getDriverClassName();
        this.user = ds.getUsername();
        this.password = ds.getPassword();
        this.url = ds.getJdbcUrl();
    }
}
