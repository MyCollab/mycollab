package com.mycollab.test.spring;

import com.mycollab.module.ecm.ContentSessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.extensions.jcr.JcrTemplate;
import org.springframework.extensions.jcr.jackrabbit.RepositoryFactoryBean;

import javax.jcr.SimpleCredentials;

/**
 * @author MyCollab Ltd.
 * @since 4.6.0
 */
@Configuration
@Profile("test")
public class EcmConfigurationTest {
    @Bean
    public RepositoryFactoryBean repository() {
        RepositoryFactoryBean bean = new RepositoryFactoryBean();
        bean.setConfiguration(new ClassPathResource("jackrabbit-repo-test.xml"));
        bean.setHomeDir(new FileSystemResource("repo-test"));
        return bean;
    }

    @Bean
    public ContentSessionFactory jcrSessionFactory() throws Exception {
        ContentSessionFactory bean = new ContentSessionFactory();
        bean.setRepository(repository().getObject());
        bean.setCredentials(new SimpleCredentials("hainguyen", "esofthead321"
                .toCharArray()));
        return bean;
    }

    @Bean
    public JcrTemplate jcrTemplate() throws Exception {
        JcrTemplate bean = new JcrTemplate();
        bean.setSessionFactory(jcrSessionFactory());
        bean.setAllowCreate(true);
        return bean;
    }
}
