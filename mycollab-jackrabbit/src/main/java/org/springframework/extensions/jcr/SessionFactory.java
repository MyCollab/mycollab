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

import javax.jcr.RepositoryException;
import javax.jcr.Session;

/**
 * Session Factory interface. This interface describes a simplified contract for retrieving a session and acts
 * as a central point inside Spring Extensions JCR support. </p>
 * @author Costin Leau
 * @author Sergio Bossa
 * @author Salvatore Incandela
 */
public interface SessionFactory {

    /**
     * Returns a JCR Session using the credentials and workspace on this JcrSessionFactory. The session
     * factory doesn't allow specification of a different workspace name because:
     * <p>
     *" Each Session object is associated one-to-one with a Workspace object. The Workspace object represents
     * a `view` of an actual repository workspace entity as seen through the authorization settings of its
     * associated Session." (quote from javax.jcr.Session javadoc).
     * </p>
     * @return the JCR session.
     * @throws RepositoryException
     */
    public Session getSession() throws RepositoryException;

    /**
     * Returns a specific SessionHolder for the given Session. The holder provider is used internally by the
     * framework in components such as transaction managers to provide implementation specific information
     * such as transactional support (if it is available).
     * @param session
     * @return specific sessionHolder.
     */
    public SessionHolder getSessionHolder(Session session);
}