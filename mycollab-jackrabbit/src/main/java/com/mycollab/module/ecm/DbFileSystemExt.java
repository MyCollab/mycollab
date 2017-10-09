package com.mycollab.module.ecm;

import com.mycollab.configuration.DatabaseConfiguration;
import com.mycollab.configuration.SiteConfiguration;
import org.apache.jackrabbit.core.fs.db.DbFileSystem;

/**
 * Db file system of mycollab jackrabbit storage
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class DbFileSystemExt extends DbFileSystem {

    public DbFileSystemExt() {
        DatabaseConfiguration dbConf = SiteConfiguration.getDatabaseConfiguration();
        this.schema = "mysql";
        this.driver = dbConf.getDriverClass();
        this.user = dbConf.getUser();
        this.password = dbConf.getPassword();
        this.url = dbConf.getUrl();
    }
}
