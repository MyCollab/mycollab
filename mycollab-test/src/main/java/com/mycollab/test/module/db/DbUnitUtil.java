package com.mycollab.test.module.db;

import com.mycollab.test.spring.DataSourceFactoryBean;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.xml.FlatDtdDataSet;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileOutputStream;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class DbUnitUtil {

    public static void main(String[] args) throws Exception {
        DataSource dataSource = new DataSourceFactoryBean().getDataSource();
        File file = new File("src/main/resources/mycollab.dtd");
        IDatabaseConnection connection = new DatabaseDataSourceConnection(dataSource);
        connection.getConfig().setProperty(DatabaseConfig.FEATURE_QUALIFIED_TABLE_NAMES, true);
        // write DTD file
        FlatDtdDataSet.write(connection.createDataSet(), new FileOutputStream(file));
    }
}
