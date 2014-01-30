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
package com.esofthead.mycollab.module.ecm;

import org.apache.jackrabbit.core.journal.DatabaseJournal;

import com.esofthead.mycollab.configuration.DatabaseConfiguration;
import com.esofthead.mycollab.configuration.SiteConfiguration;

public class MyCollabDatabaseJournal extends DatabaseJournal {

	public MyCollabDatabaseJournal() {
		DatabaseConfiguration dbConfiguration = SiteConfiguration
				.getDatabaseConfiguration();
		this.setDriver(dbConfiguration.getDriverClass());
		this.setUrl(dbConfiguration.getDbUrl());
		this.setUser(dbConfiguration.getUser());
		this.setPassword(dbConfiguration.getPassword());
		this.setSchemaObjectPrefix("ecm_journal");
		this.setDatabaseType("mysql");
	}
}
