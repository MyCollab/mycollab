/**
 * mycollab-services-community - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
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
package com.mycollab.module.page.service

import com.mycollab.common.i18n.WikiI18nEnum
import com.mycollab.module.page.domain.Page
import com.mycollab.module.page.service.PageService
import com.mycollab.test.spring.IntegrationServiceTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import javax.jcr.RepositoryException

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple

@RunWith(SpringJUnit4ClassRunner::class)
class PageServiceTest : IntegrationServiceTest() {

    @Autowired
    private lateinit var pageService: PageService

    @Before
    fun setup() {
        val page = Page()
        page.createdUser = "hainguyen@esofthead.com"
        page.category = "abc"
        page.path = "1/page/document_1"
        page.status = WikiI18nEnum.status_public.name
        page.subject = "Hello world"
        page.content = "My name is <b>Hai Nguyen</b>"
        pageService.savePage(page, "hainguyen@esofthead.com")
    }

    @After
    fun tearDown() {
        pageService.removeResource("")
    }

    @Test
    fun testGetWikiPages() {
        val pages = pageService.getPages("1/page", "hainguyen@esofthead.com")
        assertThat(pages.size).isEqualTo(1)
        assertThat(pages[0].category).isEqualTo("abc")
    }

    private fun savePage2() {
        val page = Page()
        page.createdUser = "hainguyen@esofthead.com"
        page.category = "abc"
        page.path = "1/page/document_2"
        page.status = WikiI18nEnum.status_public.name
        page.subject = "Hello world 2"
        page.content = "My name is <b>Bao Han</b>"
        page.status = WikiI18nEnum.status_private.name
        pageService.savePage(page, "hainguyen@esofthead.com")

        assertThat(pageService.getPage("1/page/document_2", "hainguyen@esofthead.com").subject).isEqualTo("Hello world 2")
    }

    @Test
    fun testGetResources() {
        savePage2()
        val pages = pageService.getPages("1/page", "hainguyen@esofthead.com")
        assertThat(pages.size).isEqualTo(2)
        assertThat(pages).extracting("subject", "status").contains(
                tuple("Hello world", "status_public"),
                tuple("Hello world 2", "status_private"))
    }

    @Test
    @Throws(RepositoryException::class)
    fun testUpdatePage() {
        val page = Page()
        page.createdUser = "hainguyen@esofthead.com"
        page.category = "abc"
        page.path = "1/page/document_1"
        page.status = WikiI18nEnum.status_public.name
        page.subject = "Hello world 2"
        page.content = "My name is <b>Bao Han</b>"

        pageService.savePage(page, "hainguyen@esofthead.com")
        val pages = pageService.getPages("1/page", "hainguyen@esofthead.com")
        assertThat(pages.size).isEqualTo(1)
        assertThat(pages[0].subject).isEqualTo("Hello world 2")
    }

    @Test
    fun testGetVersions() {
        var page = Page()
        page.createdUser = "hainguyen@esofthead.com"
        page.category = "abc"
        page.path = "1/page/document_1"
        page.status = WikiI18nEnum.status_public.name
        page.subject = "Hello world 2"
        page.content = "My name is <b>Bao Han</b>"
        pageService.savePage(page, "hainguyen@esofthead.com")

        page.subject = "Hello world 3"
        pageService.savePage(page, "hainguyen@esofthead.com")

        val versions = pageService.getPageVersions("1/page/document_1")
        assertThat(versions.size).isEqualTo(2)

        page = pageService.getPageByVersion("1/page/document_1", "1.0")
        assertThat(page.subject).isEqualTo("Hello world 2")

        val restorePage = pageService.restorePage("1/page/document_1", "1.0")
        assertThat(restorePage.subject).isEqualTo("Hello world 2")
        val page2 = pageService.getPage("1/page/document_1", "hainguyen@esofthead.com")
        assertThat(page2.subject).isEqualTo("Hello world 2")
    }
}
