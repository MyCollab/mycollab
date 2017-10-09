package com.mycollab.module.wiki.service;

import com.mycollab.common.i18n.WikiI18nEnum;
import com.mycollab.module.page.domain.Page;
import com.mycollab.module.page.domain.PageVersion;
import com.mycollab.module.page.service.PageService;
import com.mycollab.test.spring.IntegrationServiceTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.jcr.RepositoryException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@RunWith(SpringJUnit4ClassRunner.class)
public class PageServiceTest extends IntegrationServiceTest {

    @Autowired
    private PageService wikiService;

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
        wikiService.removeResource("");
    }

    @Test
    public void testGetWikiPages() {
        List<Page> pages = wikiService.getPages("1/page", "hainguyen@esofthead.com");
        assertThat(pages.size()).isEqualTo(1);
        assertThat(pages.get(0).getCategory()).isEqualTo("abc");
    }

    private void savePage2() {
        Page page = new Page();
        page.setCreatedUser("hainguyen@esofthead.com");
        page.setCategory("abc");
        page.setPath("1/page/document_2");
        page.setStatus(WikiI18nEnum.status_public.name());
        page.setSubject("Hello world 2");
        page.setContent("My name is <b>Bao Han</b>");
        page.setStatus(WikiI18nEnum.status_private.name());
        wikiService.savePage(page, "hainguyen@esofthead.com");

        assertThat(wikiService.getPage("1/page/document_2", "hainguyen@esofthead.com").getSubject()).isEqualTo("Hello world 2");
    }

    @Test
    public void testGetResources() {
        savePage2();
        List<Page> pages = wikiService.getPages("1/page", "hainguyen@esofthead.com");
        assertThat(pages.size()).isEqualTo(2);
        assertThat(pages).extracting("subject", "status").contains(
                tuple("Hello world", "status_public"),
                tuple("Hello world 2", "status_private"));
    }

    @Test
    public void testUpdatePage() throws RepositoryException {
        Page page = new Page();
        page.setCreatedUser("hainguyen@esofthead.com");
        page.setCategory("abc");
        page.setPath("1/page/document_1");
        page.setStatus(WikiI18nEnum.status_public.name());
        page.setSubject("Hello world 2");
        page.setContent("My name is <b>Bao Han</b>");

        wikiService.savePage(page, "hainguyen@esofthead.com");
        List<Page> pages = wikiService.getPages("1/page", "hainguyen@esofthead.com");
        assertThat(pages.size()).isEqualTo(1);
        assertThat(pages.get(0).getSubject()).isEqualTo("Hello world 2");
    }

    @Test
    public void testGetVersions() {
        Page page = new Page();
        page.setCreatedUser("hainguyen@esofthead.com");
        page.setCategory("abc");
        page.setPath("1/page/document_1");
        page.setStatus(WikiI18nEnum.status_public.name());
        page.setSubject("Hello world 2");
        page.setContent("My name is <b>Bao Han</b>");
        wikiService.savePage(page, "hainguyen@esofthead.com");

        page.setSubject("Hello world 3");
        wikiService.savePage(page, "hainguyen@esofthead.com");

        List<PageVersion> versions = wikiService.getPageVersions("1/page/document_1");
        assertThat(versions.size()).isEqualTo(2);

        page = wikiService.getPageByVersion("1/page/document_1", "1.0");
        assertThat(page.getSubject()).isEqualTo("Hello world 2");

        Page restorePage = wikiService.restorePage("1/page/document_1", "1.0");
        assertThat(restorePage.getSubject()).isEqualTo("Hello world 2");
        Page page2 = wikiService.getPage("1/page/document_1", "hainguyen@esofthead.com");
        assertThat(page2.getSubject()).isEqualTo("Hello world 2");
    }
}
