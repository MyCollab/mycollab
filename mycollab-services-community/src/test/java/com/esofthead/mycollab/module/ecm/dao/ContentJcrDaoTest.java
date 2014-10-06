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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.esofthead.mycollab.module.ecm.domain.Content;
import com.esofthead.mycollab.module.ecm.domain.Folder;
import com.esofthead.mycollab.module.ecm.domain.Resource;
import com.esofthead.mycollab.test.service.IntergrationServiceTest;

@RunWith(SpringJUnit4ClassRunner.class)
public class ContentJcrDaoTest extends IntergrationServiceTest {

	@Autowired
	private ContentJcrDao contentJcrDao;

	@Before
	public void setup() {
		Content pageContent = new Content("example/a");
		pageContent.setCreatedBy("hainguyen");
		pageContent.setTitle("page example");
		pageContent.setDescription("aaa");
		contentJcrDao.saveContent(pageContent, "hainguyen");
	}

	@After
	public void teardown() {
		contentJcrDao.removeResource("");
	}

	@Test
	public void testGetContent() {
		Content content = (Content) contentJcrDao.getResource("example/a");
		assertThat(content.getPath()).isEqualTo("example/a");
		assertThat(content.getTitle()).isEqualTo("page example");
		assertThat(content.getDescription()).isEqualTo("aaa");
	}

	@Test
	public void testRemoveContent() {
		contentJcrDao.removeResource("example/a/b");
		Resource content = contentJcrDao.getResource("example/a/b");
		assertThat(content).isNull();
	}

	@Test
	public void testSaveOverride() {
		Content pageContent = new Content("a/b/xyz.mycollabtext");
		pageContent.setCreatedBy("hainguyen");
		pageContent.setTitle("page example");
		pageContent.setDescription("aaa");
		contentJcrDao.saveContent(pageContent, "abc");

		Content resource = (Content) contentJcrDao
				.getResource("a/b/xyz.mycollabtext");
		assertThat(resource.getPath()).isEqualTo("a/b/xyz.mycollabtext");
	}

	@Test
	public void testCreateFolder() {
		Folder folder = new Folder("a/b/c");
		contentJcrDao.createFolder(folder, "abc");

		Resource resource = contentJcrDao.getResource("a/b/c");
		assertThat(resource).isExactlyInstanceOf(Folder.class);
		assertThat(resource.getPath()).isEqualTo("a/b/c");
	}

	@Test
	public void testGetResources() {
		Content pageContent = new Content("example/b");
		pageContent.setCreatedBy("hainguyen");
		pageContent.setTitle("page example2");
		pageContent.setDescription("aaa2");
		contentJcrDao.saveContent(pageContent, "hainguyen");

		List<Resource> resources = contentJcrDao.getResources("example");
		assertThat(resources.size()).isEqualTo(2);
		assertThat(resources).extracting("path", "title").contains(
				tuple("example/a", "page example"),
				tuple("example/b", "page example2"));
	}

	@Test
	public void testGetSubFolders() {
		Folder folder = new Folder("a/b/c");
		contentJcrDao.createFolder(folder, "abc");

		Folder folder2 = new Folder("a/b/c2");
		contentJcrDao.createFolder(folder2, "abc");

		Folder folder3 = new Folder("a/b/c3");
		contentJcrDao.createFolder(folder3, "abc");

		List<Folder> subFolders = contentJcrDao.getSubFolders("a/b");
		assertThat(subFolders.size()).isEqualTo(3);

		assertThat(subFolders).extracting("path").contains("a/b/c", "a/b/c2",
				"a/b/c3");
	}

	@Test
	public void testRenameContent() {
		contentJcrDao.rename("example/a", "example/x");

		Content content = (Content) contentJcrDao.getResource("example/x");
		assertThat(content.getPath()).isEqualTo("example/x");
		assertThat(content.getTitle()).isEqualTo("page example");
		assertThat(content.getDescription()).isEqualTo("aaa");
	}

	@Test
	public void testRenameFolder() {
		Folder folder = new Folder("a/b/c");
		contentJcrDao.createFolder(folder, "abc");

		contentJcrDao.rename("a/b/c", "a/b/d");
		Resource resource = contentJcrDao.getResource("a/b/d");
		assertThat(resource).isExactlyInstanceOf(Folder.class);
		assertThat(resource.getPath()).isEqualTo("a/b/d");
	}

	@Test
	public void testMoveResource() {
		Folder folder = new Folder("xy/yz/zx");
		contentJcrDao.createFolder(folder, "abc");

		contentJcrDao.moveResource("xy/yz/zx", "ab/bc/ca");
		Resource resource = contentJcrDao.getResource("ab/bc/ca");
		assertThat(resource).isExactlyInstanceOf(Folder.class);
		assertThat(resource.getPath()).isEqualTo("ab/bc/ca");
	}

	@Test
	public void testGetContents() {
		Content pageContent = new Content("example/e/a");
		pageContent.setCreatedBy("hainguyen");
		pageContent.setTitle("page example2");
		pageContent.setDescription("aaa2");
		contentJcrDao.saveContent(pageContent, "hainguyen");

		Content pageContent2 = new Content("example/e/b");
		pageContent2.setCreatedBy("hainguyen");
		pageContent2.setTitle("page example3");
		pageContent2.setDescription("aaa2");
		contentJcrDao.saveContent(pageContent2, "hainguyen");

		List<Content> contents = contentJcrDao.getContents("example/e");
		assertThat(contents.size()).isEqualTo(2);
		assertThat(contents).extracting("path", "title").contains(
				tuple("example/e/a", "page example2"),
				tuple("example/e/b", "page example3"));
	}
}
