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
package com.esofthead.mycollab.test.module;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.test.TestDbConfiguration;
import com.esofthead.mycollab.test.TestException;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public final class DbUnitModule extends AbstractMyCollabTestModule {
	private static Logger log = LoggerFactory.getLogger(DbUnitModule.class);

	private IDatabaseTester databaseTester;

	public void setUp() {

		// Load dataset from xml file
		IDataSet dataSet = null;
		String classFileName = this.host.getName();
		String xmlFile = classFileName.replace('.', '/') + ".xml";

		InputStream stream = null;
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
		Assert.assertNotNull("Can not find resource " + xmlFile
				+ ". Stream is " + stream, stream);

		try {
			FlatXmlDataSetBuilder dataSetBuilder = new FlatXmlDataSetBuilder();
			dataSetBuilder.setCaseSensitiveTableNames(true);
			dataSetBuilder.setMetaDataSetFromDtd(DbUnitModule.class
					.getClassLoader().getResourceAsStream("mycollab.dtd"));
			dataSet = dataSetBuilder.build(stream);
		} catch (Exception e) {
			throw new TestException(e);
		}

		try {
			TestDbConfiguration dbConf = new TestDbConfiguration();
			this.databaseTester = new JdbcDatabaseTester(
					dbConf.getDriverClassName(), dbConf.getJdbcUrl(),
					dbConf.getUsername(), dbConf.getPassword());
			this.databaseTester
					.getConnection()
					.getConfig()
					.setProperty(
							DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES,
							true);
			this.databaseTester
					.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
			this.databaseTester.setDataSet(dataSet);
			this.databaseTester.onSetup();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		log.debug("Finish test setup");
	}

	public void tearDown() {
		try {
			this.databaseTester.onTearDown();
		} catch (Exception e) {
			throw new TestException(e);
		}

	}
}
