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

import com.mycollab.configuration.DatabaseConfiguration;
import com.mycollab.spring.AppContextUtil;
import org.apache.jackrabbit.core.fs.db.DbFileSystem;

/**
 * Db file system of mycollab jackrabbit storage
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class DbFileSystemExt extends DbFileSystem {

    public DbFileSystemExt() {
        DatabaseConfiguration dbConf = AppContextUtil.getSpringBean(DatabaseConfiguration.class);
        this.schema = "mysql";
        this.driver = dbConf.getDriverClassName();
        this.user = dbConf.getUsername();
        this.password = dbConf.getPassword();
        this.url = dbConf.getUrl();
    }
}
