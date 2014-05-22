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

/**
 * This manager returns the appropriate sessionHolderProvider for the given repository. See the
 * implementations of the interface for more details. <strong>NOTE</strong> one of the reason for this
 * interface was to allow dynamical discovery of SessionHolderProviders for specific JSR-170 implementations
 * at runtime.
 * @author Costin Leau
 * @author Sergio Bossa
 * @author Salvatore Incandela
 */
public interface SessionHolderProviderManager {

    /**
     * Returns the SessionHolderProvider suitable for the given Jcr Repository.
     * @param repository
     * @return
     */
    public SessionHolderProvider getSessionProvider(Repository repository);

}