package com.mycollab.module.ecm;

import com.mycollab.configuration.DatabaseConfiguration;
import com.mycollab.configuration.SiteConfiguration;
import org.apache.jackrabbit.core.journal.DatabaseJournal;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ContentDatabaseJournal extends DatabaseJournal {

    public ContentDatabaseJournal() {
        DatabaseConfiguration dbConf = SiteConfiguration.getDatabaseConfiguration();
        setDriver(dbConf.getDriverClass());
        setUser(dbConf.getUser());
        setPassword(dbConf.getPassword());
        setUrl(dbConf.getUrl());
        this.setDatabaseType("mysql");
    }
}
