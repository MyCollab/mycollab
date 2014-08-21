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
package com.esofthead.mycollab.module.wiki.service;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import com.esofthead.mycollab.common.i18n.WikiI18nEnum;
import com.esofthead.mycollab.module.wiki.domain.Page;
import com.esofthead.mycollab.test.MyCollabClassRunner;
import com.esofthead.mycollab.test.service.ServiceTest;

@RunWith(MyCollabClassRunner.class)
public class WikiServiceTest extends ServiceTest {

	@Autowired
	private WikiService wikiService;

	@Before
	public void setup() {
		Page page = new Page();
		page.setCreatedUser("hainguyen@esofthead.com");
		page.setCategory("abc");
		page.setPath("1/page/document_1");
		page.setStatus(WikiI18nEnum.status_public.name());
		page.setSubject("Hello world");
		page.setContent("My name is <b>Hai Nguyen</b>");
		wikiService.savePage(page, "hainguyen@esofthead.com");
	}

	@After
	public void teardown() {
		wikiService.removeResource("/");
	}

	@Test
	public void testGetWikiPages() {
		List<Page> pages = wikiService.getPages("1/page");
		Assert.assertEquals(1, pages.size());
		Page page = pages.get(0);
		Assert.assertEquals("abc", page.getCategory());
	}

	private void savePage2() {
		Page page = new Page();
		page.setCreatedUser("hainguyen@esofthead.com");
		page.setCategory("abc");
		page.setPath("1/page/document_2");
		page.setStatus(WikiI18nEnum.status_public.name());
		page.setSubject("Hello world 2");
		page.setContent("My name is <b>Bao Han</b>");
		wikiService.savePage(page, "hainguyen@esofthead.com");
	}

	@Test
	public void testUpdatePage() {
		Page page = new Page();
		page.setCreatedUser("hainguyen@esofthead.com");
		page.setCategory("abc");
		page.setPath("1/page/document_1");
		page.setStatus(WikiI18nEnum.status_public.name());
		page.setSubject("Hello world 2");
		page.setContent("My name is <b>Bao Han</b>");

		wikiService.savePage(page, "hainguyen@esofthead.com");
		List<Page> pages = wikiService.getPages("1/page");
		Assert.assertEquals(1, pages.size());
		page = pages.get(0);
		Assert.assertEquals("Hello world 2", page.getSubject());

		wikiService.getPageVersions("1/page/document_1");
	}

	@Test
	public void testGetResources() {
		savePage2();

	}
}
