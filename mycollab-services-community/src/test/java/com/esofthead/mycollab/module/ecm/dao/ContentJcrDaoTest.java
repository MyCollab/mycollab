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
package com.esofthead.mycollab.module.ecm.dao;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.esofthead.mycollab.test.MyCollabClassRunner;
import com.esofthead.mycollab.test.service.ServiceTest;

@RunWith(MyCollabClassRunner.class)
public class ContentJcrDaoTest extends ServiceTest {

	@Test
	public void testGetContent() {
	}
	// @Autowired
	// private ContentJcrDao contentJcrDao;
	//
	// @Before
	// public void setup() {
	// Content pageContent = new Content();
	// pageContent.setCreatedBy("hainguyen");
	// pageContent.setTitle("page example");
	// pageContent.setDescription("aaa");
	// pageContent.setPath("example/a");
	// contentJcrDao.saveContent(pageContent);
	// }
	//
	// @After
	// public void teardown() {
	// contentJcrDao.removeResource("/");
	// }
	//
	// @Test
	// public void testGetContent() {
	// Resource content = contentJcrDao.getResource("example/a");
	// Assert.assertNotNull(content);
	// }
	//
	// @Test
	// public void testRemoveContent() {
	// contentJcrDao.removeResource("example/a/b");
	// Resource content = contentJcrDao.getResource("example/a/b");
	// Assert.assertNull(content);
	// }

	// public void testSaveOverride() {
	// Content pageContent = new Content();
	// pageContent.setCreatedBy("hainguyen");
	// pageContent.setTitle("page example");
	// pageContent.setDescription("aaa");
	// pageContent.setPath("a/b/xyz.mycollabtext");
	// contentJcrDao.saveContent(pageContent, "example/a/b");
	// }
}
