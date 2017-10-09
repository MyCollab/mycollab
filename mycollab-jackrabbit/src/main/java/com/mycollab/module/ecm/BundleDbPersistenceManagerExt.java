package com.mycollab.module.ecm;

import com.mycollab.configuration.DatabaseConfiguration;
import com.mycollab.configuration.SiteConfiguration;
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
        DatabaseConfiguration dbConf = SiteConfiguration.getDatabaseConfiguration();
        setDriver(dbConf.getDriverClass());
        setUser(dbConf.getUser());
        setPassword(dbConf.getPassword());
        setUrl(dbConf.getUrl());

        if (getSchemaObjectPrefix() == null) {
            setSchemaObjectPrefix("ecm_p_workspace");
        }

        if (getDatabaseType() == null) {
            setDatabaseType("mysql");
        }
        super.init(context);
    }
}
