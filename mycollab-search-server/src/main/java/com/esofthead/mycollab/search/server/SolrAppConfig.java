package com.esofthead.mycollab.search.server;

import java.io.IOException;

import javax.annotation.Resource;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;
import org.xml.sax.SAXException;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.3.2
 *
 */
@Configuration
@EnableSolrRepositories(basePackages = { "com.esofthead.mycollab.solr" })
public class SolrAppConfig {

	@Resource
	private Environment environment;

	@Bean
	public SolrServer solrServer() throws ParserConfigurationException,
			IOException, SAXException {
//		EmbeddedSolrServerFactoryBean factory = new EmbeddedSolrServerFactoryBean();
//		String userHome = System.getProperty("user.home");
//		File solrHome = new File(userHome + "/.solr");
//		factory.setSolrHome(solrHome.getAbsolutePath());
//		return factory.getSolrServer();
		return new HttpSolrServer("http://localhost:8983/solr");
	}

	@Bean
	public SolrOperations solrTemplate() throws ParserConfigurationException,
			IOException, SAXException {
		return new SolrTemplate(solrServer());
	}
}
