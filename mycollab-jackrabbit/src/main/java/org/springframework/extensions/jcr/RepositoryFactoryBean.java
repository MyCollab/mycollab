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
package org.springframework.extensions.jcr;

import javax.jcr.Repository;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

/**
 * Base class with common functionality for creating JCR repositories. Subclasses should extend this class for
 * custom configuration.
 * @author Costin Leau
 * @author Sergio Bossa
 * @author Salvatore Incandela
 */
public abstract class RepositoryFactoryBean implements InitializingBean, DisposableBean, FactoryBean<Repository> {

    /**
     * Repository configuration.
     */
    protected Resource configuration;

    /**
     * The actual repository.
     */
    protected Repository repository;

    /**
     * Subclasses have to implement this method to allow specific JSR-170 implementation repository
     * configuration.
     * @throws Exception
     */
    protected abstract void resolveConfigurationResource() throws Exception;

    /**
     * Subclasses have to implement this method to allow specific JSR-170 implementation repository creation.
     * @return
     * @throws Exception
     */
    protected abstract Repository createRepository() throws Exception;

    /**
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        resolveConfigurationResource();
        repository = createRepository();

    }

    /**
     * @see org.springframework.beans.factory.DisposableBean#destroy()
     */
    public void destroy() throws Exception {
    }

    /**
     * @see org.springframework.beans.factory.FactoryBean#getObject()
     */
    public Repository getObject() throws Exception {
        return this.repository;
    }

    /**
     * @see org.springframework.beans.factory.FactoryBean#getObjectType()
     */
    public Class<Repository> getObjectType() {
        // the repository is proxied.
        return Repository.class;
    }

    /**
     * @see org.springframework.beans.factory.FactoryBean#isSingleton()
     */
    public boolean isSingleton() {
        return true;
    }

    /**
     * @return Returns the configuration.
     */
    public Resource getConfiguration() {
        return this.configuration;
    }

    /**
     * @param configuration The configuration to set.
     */
    public void setConfiguration(Resource configuration) {
        this.configuration = configuration;
    }

}
