/**
 * This file is part of mycollab-test.
 *
 * mycollab-test is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-test is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-test.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.spring.test;

import com.esofthead.mycollab.module.ecm.MyCollabContentSessionFactory;
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
    public MyCollabContentSessionFactory jcrSessionFactory() throws Exception {
        MyCollabContentSessionFactory bean = new MyCollabContentSessionFactory();
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
