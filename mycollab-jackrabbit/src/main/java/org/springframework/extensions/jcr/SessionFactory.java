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
    Session getSession() throws RepositoryException;

    /**
     * Returns a specific SessionHolder for the given Session. The holder provider is used internally by the
     * framework in components such as transaction managers to provide implementation specific information
     * such as transactional support (if it is available).
     * @param session
     * @return specific sessionHolder.
     */
    SessionHolder getSessionHolder(Session session);
}