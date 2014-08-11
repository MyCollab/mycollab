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
	public void testGetResources() {
		savePage2();

	}
}
