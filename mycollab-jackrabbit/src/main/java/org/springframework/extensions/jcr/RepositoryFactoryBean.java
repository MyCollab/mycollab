/**
 * This file is part of mycollab-jackrabbit.
 *
 * mycollab-jackrabbit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-jackrabbit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-jackrabbit.  If not, see <http://www.gnu.org/licenses/>.
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
