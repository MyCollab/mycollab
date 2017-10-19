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

import java.io.IOException;

import javax.jcr.RepositoryException;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;

/**
 * Base class for JcrTemplate and JcrInterceptor, defining common properties like JcrSessionFactory. The
 * required property is sessionFactory.
 *
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
     *
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
     *
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
     *
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
