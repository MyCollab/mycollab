/**
 * Copyright 2009-2012 the original author or authors
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.springframework.extensions.jcr.jackrabbit;

import javax.jcr.Repository;

import org.apache.jackrabbit.api.JackrabbitRepository;
import org.apache.jackrabbit.core.RepositoryImpl;
import org.apache.jackrabbit.core.config.RepositoryConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.xml.sax.InputSource;

/**
 * FactoryBean for creating a JackRabbit (JCR-170) repository through Spring configuration files. Use this
 * factory bean when you have to manually configure the repository; for retrieving the repository from JNDI
 * use the JndiObjectFactoryBean {@link org.springframework.jndi.JndiObjectFactoryBean}
 * @see org.springframework.jndi.JndiObjectFactoryBean
 * @author Costin Leau
 * @author Sergio Bossa
 * @author Salvatore Incandela
 */
public class RepositoryFactoryBean extends org.springframework.extensions.jcr.RepositoryFactoryBean {

    private static final Logger LOG = LoggerFactory.getLogger(RepositoryFactoryBean.class);

    /**
     * Default repository configuration file.
     */
    private static final String DEFAULT_CONF_FILE = "repository.xml";

    /**
     * Default repository directory.
     */
    private static final String DEFAULT_REP_DIR = ".";

    /**
     * Home directory for the repository.
     */
    private Resource homeDir;

    /**
     * Repository configuratin created through Spring.
     */
    private RepositoryConfig repositoryConfig;

    /**
     * @see org.springframework.extensions.jcr.RepositoryFactoryBean#createRepository()
     */
    protected Repository createRepository() throws Exception {
        // return JackRabbit repository.
        return RepositoryImpl.create(repositoryConfig);
    }

    /**
     * @see org.springframework.extensions.jcr.RepositoryFactoryBean#resolveConfigurationResource()
     */
    protected void resolveConfigurationResource() throws Exception {
        // read the configuration object
        if (repositoryConfig != null)
            return;

        if (this.configuration == null) {
            if (LOG.isDebugEnabled())
                LOG.debug("no configuration resource specified, using the default one:" + DEFAULT_CONF_FILE);
            configuration = new ClassPathResource(DEFAULT_CONF_FILE);
        }

        if (homeDir == null) {
            if (LOG.isDebugEnabled())
                LOG.debug("no repository home dir specified, using the default one:" + DEFAULT_REP_DIR);
            homeDir = new FileSystemResource(DEFAULT_REP_DIR);
        }

        repositoryConfig = RepositoryConfig.create(new InputSource(configuration.getInputStream()), homeDir.getFile().getAbsolutePath());
    }

    /**
     * Shutdown method.
     */
    public void destroy() throws Exception {
        // force cast (but use only the interface)
        if (repository instanceof JackrabbitRepository)
            ((JackrabbitRepository) repository).shutdown();
    }

    /**
     * @return Returns the defaultRepDir.
     */
    public Resource getHomeDir() {
        return this.homeDir;
    }

    /**
     * @param defaultRepDir The defaultRepDir to set.
     */
    public void setHomeDir(Resource defaultRepDir) {
        this.homeDir = defaultRepDir;
    }

    /**
     * @return Returns the repositryConfig.
     */
    public RepositoryConfig getRepositoryConfig() {
        return this.repositoryConfig;
    }

    /**
     * @param repositoryConfig The repositryConfig to set.
     */
    public void setRepositoryConfig(RepositoryConfig repositoryConfig) {
        this.repositoryConfig = repositoryConfig;
    }
}
