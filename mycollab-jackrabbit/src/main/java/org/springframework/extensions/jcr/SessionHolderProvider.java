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

import javax.jcr.Session;

/**
 * SessionHolderProvider is a factory that creates a session holder for classes which require collaboration
 * with TransactionSynchronizationManager. Because there is no standard on how to a Jcr repository
 * participates inside transactions, each implementation has it's own support (XAResource,Transaction) which
 * has to be wrapped in the appropriate holder.
 * @author Costin Leau
 * @author Sergio Bossa
 * @author Salvatore Incandela
 */
public interface SessionHolderProvider {

    /**
     * Return the specific session holder.
     * @param session
     * @return
     */
    public SessionHolder createSessionHolder(Session session);

    /**
     * Method for matching the sessionHolderProvider against a repository (given by name).
     * @param repositoryName
     * @return true if the sessionHolderProvider is suitable for the given repository name, false otherwise.
     */
    public boolean acceptsRepository(String repositoryName);
}
