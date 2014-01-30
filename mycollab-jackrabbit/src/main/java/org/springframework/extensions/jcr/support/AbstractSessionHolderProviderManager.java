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

import java.util.List;

import javax.jcr.Repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.extensions.jcr.SessionHolderProvider;
import org.springframework.extensions.jcr.SessionHolderProviderManager;

/**
 * Base implementation for SessionHolderProviderManager that adds most of the functionality needed by the
 * interface. Usually interface implementations will extends this class.
 * @author Costin Leau
 * @author Sergio Bossa
 * @author Salvatore Incandela
 */
public abstract class AbstractSessionHolderProviderManager implements SessionHolderProviderManager {

    protected static final Logger LOG = LoggerFactory.getLogger(AbstractSessionHolderProviderManager.class);

    protected SessionHolderProvider defaultProvider = new GenericSessionHolderProvider();

    /**
     * Returns all the providers for this class. Subclasses have to implement this method.
     * @return sessionHolderProviders
     */
    public abstract List<SessionHolderProvider> getProviders();

    /**
     * @see org.springframework.extensions.jcr.SessionHolderProviderManager#getSessionProvider(Repository)
     */
    public SessionHolderProvider getSessionProvider(Repository repository) {
        // graceful fallback
        if (repository == null)
            return defaultProvider;

        String key = repository.getDescriptor(Repository.REP_NAME_DESC);
        List<SessionHolderProvider> providers = getProviders();

        // search the provider
        for (int i = 0; i < providers.size(); i++) {
            SessionHolderProvider provider = providers.get(i);
            if (provider.acceptsRepository(key)) {
                if (LOG.isDebugEnabled())
                    LOG.debug("specific SessionHolderProvider found for repository " + key);
                return provider;
            }
        }

        // no provider found - return the default one
        if (LOG.isDebugEnabled())
            LOG.debug("no specific SessionHolderProvider found for repository " + key + "; using the default one");
        return defaultProvider;
    }
}
