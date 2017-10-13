/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * mycollab-services-community - Parent pom providing dependency and plugin management for applications
built with Maven
 * Copyright © 2017 MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.ecm.dao

import com.mycollab.core.UserInvalidInputException
import com.mycollab.module.ecm.domain.Content
import com.mycollab.module.ecm.domain.Folder
import com.mycollab.module.ecm.service.ContentJcrDao
import com.mycollab.test.spring.IntegrationServiceTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

@RunWith(SpringJUnit4ClassRunner::class)
class ContentJcrDaoTest : IntegrationServiceTest() {

    @Autowired
    private lateinit var contentJcrDao: ContentJcrDao

    @Rule
    @JvmField
    var thrown = ExpectedException.none()

    @Before
    fun setup() {
        val pageContent = Content("example/a")
        pageContent.createdBy = "hainguyen"
        pageContent.title = "page example"
        pageContent.description = "aaa"
        contentJcrDao.saveContent(pageContent, "hainguyen")
    }

    @After
    fun tearDown() {
        contentJcrDao.removeResource("")
    }

    @Test
    fun testGetContent() {
        val content = contentJcrDao.getResource("example/a") as Content
        assertThat(content.path).isEqualTo("example/a")
        assertThat(content.title).isEqualTo("page example")
        assertThat(content.description).isEqualTo("aaa")
    }

    @Test
    fun testRemoveContent() {
        contentJcrDao.removeResource("example/a/b")
        val content = contentJcrDao.getResource("example/a/b")
        assertThat(content).isNull()
    }

    @Test
    fun testSaveOverride() {
        val pageContent = Content("a/b/xyz.mycollabtext")
        pageContent.createdBy = "hainguyen"
        pageContent.title = "page example"
        pageContent.description = "aaa"
        contentJcrDao.saveContent(pageContent, "abc")

        val resource = contentJcrDao.getResource("a/b/xyz.mycollabtext") as Content
        assertThat(resource.path).isEqualTo("a/b/xyz.mycollabtext")
    }

    @Test
    fun testSaveInvalidContentName() {
        thrown.expect(UserInvalidInputException::class.java)
        val pageContent = Content("a/b/http-//anchoragesnowmobileclub.com/trail_report/weather-for-turnagain/")
        pageContent.createdBy = "hainguyen"
        pageContent.title = "page example"
        pageContent.description = "aaa"
        contentJcrDao.saveContent(pageContent, "abc")
    }

    @Test
    fun testCreateFolder() {
        val folder = Folder("a/b/c")
        contentJcrDao.createFolder(folder, "abc")

        val resource = contentJcrDao.getResource("a/b/c")
        assertThat(resource).isExactlyInstanceOf(Folder::class.java)
        assertThat(resource!!.path).isEqualTo("a/b/c")
    }

    @Test
    fun testGetResources() {
        val pageContent = Content("example/b")
        pageContent.createdBy = "hainguyen"
        pageContent.title = "page example2"
        pageContent.description = "aaa2"
        contentJcrDao.saveContent(pageContent, "hainguyen")

        val resources = contentJcrDao.getResources("example")
        assertThat(resources.size).isEqualTo(2)
        assertThat(resources).extracting("path", "title").contains(
                tuple("example/a", "page example"),
                tuple("example/b", "page example2"))
    }

    @Test
    fun testGetSubFolders() {
        val folder = Folder("a/b/c")
        contentJcrDao.createFolder(folder, "abc")

        val folder2 = Folder("a/b/c2")
        contentJcrDao.createFolder(folder2, "abc")

        val folder3 = Folder("a/b/c3")
        contentJcrDao.createFolder(folder3, "abc")

        val subFolders = contentJcrDao.getSubFolders("a/b")
        assertThat(subFolders.size).isEqualTo(3)
        assertThat(subFolders).extracting("path").contains("a/b/c", "a/b/c2", "a/b/c3")
    }

    @Test
    fun testRenameContent() {
        contentJcrDao.rename("example/a", "example/x")

        val content = contentJcrDao.getResource("example/x") as Content
        assertThat(content.path).isEqualTo("example/x")
        assertThat(content.title).isEqualTo("page example")
        assertThat(content.description).isEqualTo("aaa")
    }

    @Test
    fun testRenameFolder() {
        val folder = Folder("a/b/c")
        contentJcrDao.createFolder(folder, "abc")

        contentJcrDao.rename("a/b/c", "a/b/d")
        val resource = contentJcrDao.getResource("a/b/d")
        assertThat(resource).isExactlyInstanceOf(Folder::class.java)
        assertThat(resource!!.path).isEqualTo("a/b/d")
    }

    @Test
    fun testMoveResource() {
        val folder = Folder("xy/yz/zx")
        contentJcrDao.createFolder(folder, "abc")

        contentJcrDao.moveResource("xy/yz/zx", "ab/bc/ca")
        val resource = contentJcrDao.getResource("ab/bc/ca")
        assertThat(resource).isExactlyInstanceOf(Folder::class.java)
        assertThat(resource!!.path).isEqualTo("ab/bc/ca")
    }

    @Test
    fun testGetContents() {
        val pageContent = Content("example/e/a")
        pageContent.createdBy = "hainguyen"
        pageContent.title = "page example2"
        pageContent.description = "aaa2"
        contentJcrDao.saveContent(pageContent, "hainguyen")

        val pageContent2 = Content("example/e/b")
        pageContent2.createdBy = "hainguyen"
        pageContent2.title = "page example3"
        pageContent2.description = "aaa2"
        contentJcrDao.saveContent(pageContent2, "hainguyen")

        val contents = contentJcrDao.getContents("example/e")
        assertThat(contents.size).isEqualTo(2)
        assertThat(contents).extracting("path", "title").contains(
                tuple("example/e/a", "page example2"),
                tuple("example/e/b", "page example3"))
    }
}
