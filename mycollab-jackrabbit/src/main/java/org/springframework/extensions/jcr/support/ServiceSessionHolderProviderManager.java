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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

import org.springframework.extensions.jcr.SessionHolderProvider;

/**
 * Implementation of SessionHolderProviderManager which does dynamic discovery of the providers using the JDK
 * 1.5+ <a href= "http://java.sun.com/j2se/1.5.0/docs/guide/jar/jar.html#Service%20Provider"> 'Service
 * Provider' specification</a>. The class will look for
 * org.springframework.extensions.jcr.SessionHolderProvider property files in META-INF/services directories.
 * 
 * Updated to use {@link java.util.ServiceLoader} to avoid restricted access to sun.com.**
 * 
 * @author Costin Leau
 * @author Sergio Bossa
 * @author Salvatore Incandela
 * @author Joerg Bellmann
 */
public class ServiceSessionHolderProviderManager extends CacheableSessionHolderProviderManager {

    /**
     * Loads the service providers using the discovery mechanism.
     * @return the list of service providers found.
     */
    @Override
    public List<SessionHolderProvider> getProviders() {
        final ServiceLoader<SessionHolderProvider> serviceLoader = ServiceLoader.load(SessionHolderProvider.class,
                Thread.currentThread().getContextClassLoader());

        List<SessionHolderProvider> providers = new ArrayList<SessionHolderProvider>();
        Iterator<SessionHolderProvider> sessionHolderProviderIterator = serviceLoader.iterator();
        while (sessionHolderProviderIterator.hasNext()) {
            providers.add(sessionHolderProviderIterator.next());
        }
        return Collections.unmodifiableList(providers);
    }
}
