/**
 * This file is part of mycollab-services-community.
 *
 * mycollab-services-community is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services-community is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services-community.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.tracker.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.module.tracker.domain.SimpleVersion;
import com.mycollab.module.tracker.domain.Version;
import com.mycollab.module.tracker.domain.criteria.VersionSearchCriteria;
import com.mycollab.test.DataSet;
import com.mycollab.test.service.IntegrationServiceTest;

@RunWith(SpringJUnit4ClassRunner.class)
public class VersionServiceTest extends IntegrationServiceTest {

	private static final DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	@Autowired
	protected VersionService versionService;

	private VersionSearchCriteria getCriteria() {
		VersionSearchCriteria criteria = new VersionSearchCriteria();
		criteria.setSaccountid(new NumberSearchField(1));
		criteria.setProjectId(new NumberSearchField(1));
		return criteria;
	}

	@SuppressWarnings("unchecked")
	@DataSet
	@Test
	public void testGetListVersions() throws ParseException {
		List<SimpleVersion> versions = versionService
				.findPagableListByCriteria(new BasicSearchRequest<>(
						getCriteria(), 0, Integer.MAX_VALUE));

		assertThat(versions.size()).isEqualTo(4);
		assertThat(versions).extracting("id", "description", "status",
				"versionname", "numBugs", "numOpenBugs", "duedate",
				"createdtime").contains(
				tuple(4, "Version 4.0.0", "Open", "4.0.0", 0, 0, dateformat.parse("2014-09-17 10:10:10"), dateformat.parse("2014-09-10 10:10:10")),
				tuple(3, "Version 3.0.0", "Closed", "3.0.0", 1, 1, dateformat.parse("2014-09-15 10:10:10"), dateformat.parse("2014-08-10 10:10:10")),
				tuple(2, "Version 2.0.0", "Closed", "2.0.0", 2, 1, dateformat.parse("2014-09-12 10:10:10"), dateformat.parse("2014-07-10 10:10:10")),
				tuple(1, "Version 1.0.0", "Open", "1.0.0", 1, 1, dateformat.parse("2014-09-10 10:10:10"), dateformat.parse("2014-06-10 10:10:10")));
	}

	@SuppressWarnings("unchecked")
	@DataSet
	@Test
	public void testTotalCount() {
		List<SimpleVersion> versions = versionService
				.findPagableListByCriteria(new BasicSearchRequest<>(
						getCriteria(), 0, Integer.MAX_VALUE));

		assertThat(versions.size()).isEqualTo(4);
	}

	@SuppressWarnings("unchecked")
	@DataSet
	@Test
	public void testFindVersionById() {
		VersionSearchCriteria criteria = new VersionSearchCriteria();
		criteria.setId(new NumberSearchField(1));

		List<SimpleVersion> versions = versionService
				.findPagableListByCriteria(new BasicSearchRequest<>(
						criteria, 0, Integer.MAX_VALUE));
		assertThat(versions.size()).isEqualTo(1);
		assertThat(versions).extracting("id", "description", "status",
				"versionname", "numBugs", "numOpenBugs").contains(
				tuple(1, "Version 1.0.0", "Open", "1.0.0", 1, 1));
	}

	@SuppressWarnings("unchecked")
	@DataSet
	@Test
	public void testFindByCriteria() {
		VersionSearchCriteria criteria = getCriteria();
		criteria.setId(new NumberSearchField(2));
		criteria.setStatus(StringSearchField.and("Closed"));
		criteria.setVersionname(StringSearchField.and("2.0.0"));

		List<SimpleVersion> versions = versionService
				.findPagableListByCriteria(new BasicSearchRequest<>(
						criteria, 0, Integer.MAX_VALUE));
		assertThat(versions.size()).isEqualTo(1);
		assertThat(versions).extracting("id", "description", "status",
				"versionname", "numBugs", "numOpenBugs").contains(
				tuple(2, "Version 2.0.0", "Closed", "2.0.0", 2, 1));
	}

	@DataSet
	@Test
	public void testSaveVersion() {
		Version version = new Version();
		version.setProjectid(1);
		version.setDuedate(new GregorianCalendar(2014, 10, 6).getTime());
		version.setVersionname("sss");
		version.setCreateduser("hai79");
		version.setSaccountid(1);
		version.setDescription("a");
		version.setStatus("Open");

		int versionId = versionService.saveWithSession(version, "hai79");
		assertThat(versionId > 0).isEqualTo(true);
	}
}
