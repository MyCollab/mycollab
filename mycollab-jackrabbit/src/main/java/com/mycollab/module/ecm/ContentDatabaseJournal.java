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
import org.apache.jackrabbit.core.journal.DatabaseJournal;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ContentDatabaseJournal extends DatabaseJournal {

    public ContentDatabaseJournal() {
        DatabaseConfiguration dbConf = AppContextUtil.getSpringBean(DatabaseConfiguration.class);
        setDriver(dbConf.getDriverClassName());
        setUser(dbConf.getUsername());
        setPassword(dbConf.getPassword());
        setUrl(dbConf.getUrl());
        this.setDatabaseType("mysql");
    }
}
