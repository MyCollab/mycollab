package com.mycollab.module.ecm;

import org.apache.jackrabbit.core.util.db.ConnectionHelper;

import javax.sql.DataSource;

/**
 * When we install MyCollab instances in the same machine, Jackrabbit will verify whether the database existing
 * See https://stackoverflow.com/questions/49554043/how-to-avoid-the-jackrabbit-datastore-table-or-view-does-not-exist-error
 * @author MyCollab Ltd
 * @since 7.0.0
 */
public class MySqlConnectionHelper extends ConnectionHelper {
    public MySqlConnectionHelper(DataSource dataSrc) {
        super(dataSrc, true, false);
    }
}
