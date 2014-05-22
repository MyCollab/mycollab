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

import java.io.IOException;

import javax.jcr.RepositoryException;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;

/**
 * Base class for JcrTemplate and JcrInterceptor, defining common properties like JcrSessionFactory. The
 * required property is sessionFactory.
 * <p>
 * Not intended to be used directly. See JcrTemplate and JcrInterceptor.
 * @see JcrTemplate
 * @see JcrInterceptor
 * @author Costin Leau
 * @author Sergio Bossa
 * @author Salvatore Incandela
 */
public abstract class JcrAccessor implements InitializingBean {

    private SessionFactory sessionFactory;

    /**
     * Eagerly initialize the session holder provider, creating a default one if one is not set.
     */
    public void afterPropertiesSet() {
        if (getSessionFactory() == null) {
            throw new IllegalArgumentException("sessionFactory is required");
        }
    }

    /**
     * Convert the given RepositoryException to an appropriate exception from the
     * <code>org.springframework.dao</code> hierarchy.
     * <p>
     * May be overridden in subclasses.
     * @param ex RepositoryException that occured
     * @return the corresponding DataAccessException instance
     */
    public DataAccessException convertJcrAccessException(RepositoryException ex) {
        return SessionFactoryUtils.translateException(ex);
    }

    /**
     * Convert the given IOException to an appropriate exception from the <code>org.springframework.dao</code>
     * hierarchy.
     * <p>
     * May be overridden in subclasses.
     * @param ex IOException that occured
     * @return the corresponding DataAccessException instance
     */
    public DataAccessException convertJcrAccessException(IOException ex) {
        return SessionFactoryUtils.translateException(ex);
    }

    /**
     * Convert the given RuntimeException to an appropriate exception from the
     * <code>org.springframework.dao</code> hierarchy.
     * <p>
     * May be overridden in subclasses.
     * @param ex
     * @return
     */
    public RuntimeException convertJcrAccessException(RuntimeException ex) {
        return ex;
    }

    /**
     * @return Returns the sessionFactory.
     */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * @param sessionFactory The sessionFactory to set.
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
