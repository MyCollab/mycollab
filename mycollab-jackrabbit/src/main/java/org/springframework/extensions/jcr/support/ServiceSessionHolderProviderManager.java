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
