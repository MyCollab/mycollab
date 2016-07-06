/**
 * This file is part of mycollab-jackrabbit.
 *
 * mycollab-jackrabbit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-jackrabbit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-jackrabbit.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.ecm;

import org.apache.jackrabbit.core.persistence.PMContext;
import org.apache.jackrabbit.core.persistence.pool.BundleDbPersistenceManager;

/**
 * Customize db persistence of jackrabbit
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class BundleDbPersistenceManagerExt extends BundleDbPersistenceManager {

    /**
     * {@inheritDoc}
     */
    public void init(PMContext context) throws Exception {
        setDriver("javax.naming.InitialContext");
        setUrl("java:comp/env/jdbc/mycollabdatasource");

        if (getSchemaObjectPrefix() == null) {
            setSchemaObjectPrefix("ecm_p_workspace");
        }

        if (getDatabaseType() == null) {
            setDatabaseType("mysql");
        }
        super.init(context);
    }
}
