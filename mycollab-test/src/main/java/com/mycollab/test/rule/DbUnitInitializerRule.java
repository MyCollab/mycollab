/**
 * This file is part of mycollab-test.
 *
 * mycollab-test is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-test is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-test.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.test.rule;

import com.mycollab.test.DataSet;
import com.mycollab.test.TestDbConfiguration;
import com.mycollab.test.TestException;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Assert;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
public class DbUnitInitializerRule implements TestRule {
    private static final Logger LOG = LoggerFactory.getLogger(DbUnitInitializerRule.class);

    @Override
    public Statement apply(Statement base, Description description) {
        if (description.getAnnotation(DataSet.class) != null) {
            return new WrappedStatement(description.getTestClass(), base);
        } else {
            return base;
        }
    }

    private static class WrappedStatement extends Statement {
        private Statement baseStm;
        private Class testClass = null;
        private IDatabaseTester databaseTester;

        WrappedStatement(Class testClass, Statement base) {
            this.baseStm = base;
            this.testClass = testClass;
        }

        @Override
        public void evaluate() throws Throwable {
            try {
                setUp();
                baseStm.evaluate();
            } finally {
                tearDown();
            }
        }

        private void setUp() {
            // Load dataset from xml file
            IDataSet dataSet;
            String classFileName = testClass.getName();
            String xmlFile = classFileName.replace('.', '/') + ".xml";

            InputStream stream;
            String xmlFullPath = "src/test/resources/" + xmlFile;
            if (new File(xmlFullPath).exists()) {
                try {
                    stream = new FileInputStream(xmlFullPath);
                } catch (FileNotFoundException e) {
                    throw new TestException(e);
                }
            } else {
                stream = getClass().getClassLoader().getResourceAsStream(xmlFile);
            }
            Assert.assertNotNull(String.format("Can not find resource %s. Stream is %s", xmlFile, stream), stream);

            try {
                FlatXmlDataSetBuilder dataSetBuilder = new FlatXmlDataSetBuilder();
                dataSetBuilder.setCaseSensitiveTableNames(true);
                dataSetBuilder.setMetaDataSetFromDtd(DbUnitInitializerRule.class.getClassLoader().getResourceAsStream("mycollab.dtd"));
                dataSet = dataSetBuilder.build(stream);
            } catch (Exception e) {
                throw new TestException(e);
            }

            try {
                TestDbConfiguration dbConf = new TestDbConfiguration();
                databaseTester = new DbUnitTester(dbConf.getDriverClassName(), dbConf.getJdbcUrl(), dbConf.getUsername(),
                        dbConf.getPassword());
                databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
                databaseTester.setDataSet(dataSet);
                databaseTester.onSetup();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            LOG.debug("Finish test setup");
        }

        private void tearDown() {
            if (databaseTester != null) {
                try {
                    databaseTester.onTearDown();
                } catch (Exception e) {
                    throw new TestException(e);
                }
            }
        }
    }

    private static class DbUnitTester extends JdbcDatabaseTester {
        public DbUnitTester(String driverClass, String connectionUrl, String username, String password) throws ClassNotFoundException {
            super(driverClass, connectionUrl, username, password);
        }

        public IDatabaseConnection getConnection() throws Exception {
            IDatabaseConnection connection = super.getConnection();
            DatabaseConfig config = connection.getConfig();
            config.setProperty(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, true);
            config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new MySqlDataTypeFactory());
            return connection;
        }
    }
}
