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

import java.util.Collections;
import java.util.List;

import org.springframework.extensions.jcr.SessionHolderProvider;

/**
 * List based implementation of SessionHolderProviderManager. This class should is intended mainly for testing
 * or for declaring SessionHolderProviders in Spring context files.
 * @author Costin Leau
 * @author Sergio Bossa
 * @author Salvatore Incandela
 */
public class ListSessionHolderProviderManager extends CacheableSessionHolderProviderManager {

    private List<SessionHolderProvider> providers = Collections.emptyList();

    /**
     * @see org.springframework.extensions.jcr.support.AbstractSessionHolderProviderManager#getProviders()
     */
    public List<SessionHolderProvider> getProviders() {
        return providers;
    }

    /**
     * @param providers The providers to set.
     */
    public void setProviders(List<SessionHolderProvider> providers) {
        this.providers = providers;
    }

}
