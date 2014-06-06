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
package org.springframework.extensions.jcr.support;

import javax.jcr.Repository;

import org.springframework.extensions.jcr.SessionHolderProvider;
import org.springframework.extensions.jcr.util.CachingMapDecorator;

/**
 * Manager which caches providers in order to avoid lookups.
 * @author Costin Leau
 * @author Sergio Bossa
 * @author Salvatore Incandela
 */
public abstract class CacheableSessionHolderProviderManager extends AbstractSessionHolderProviderManager {

    /**
     * Caching class based on CachingMapDecorator from main Spring distribution.
     * @author Costin Leau
     * @author Sergio Bossa
     * @author Salvatore Incandela
     */
    protected class ProvidersCache extends CachingMapDecorator<Repository, SessionHolderProvider> {

        private static final long serialVersionUID = 1L;

        private ProvidersCache() {
            super(true);
        }

        /**
         * @see org.springframework.extensions.jcr.util.CachingMapDecorator#create(java.lang.Object)
         */
        @Override
        protected SessionHolderProvider create(Repository key) {
            return parentLookup(key);
        }

    }

    /**
     * Providers cache.
     */
    private final ProvidersCache providersCache = new ProvidersCache();

    /**
     * Method for retrieving the parent functionality.
     * @param repository
     * @return
     */
    private SessionHolderProvider parentLookup(Repository repository) {
        return super.getSessionProvider(repository);
    }

    /**
     * Overwrite the method to provide caching.
     * @see org.springframework.extensions.jcr.support.AbstractSessionHolderProviderManager#getSessionProvider(Repository)
     */
    @Override
    public SessionHolderProvider getSessionProvider(Repository repository) {
        return providersCache.get(repository);
    }

}
