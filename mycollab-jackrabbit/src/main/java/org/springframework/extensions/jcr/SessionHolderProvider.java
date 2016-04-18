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
    SessionHolder createSessionHolder(Session session);

    /**
     * Method for matching the sessionHolderProvider against a repository (given by name).
     * @param repositoryName
     * @return true if the sessionHolderProvider is suitable for the given repository name, false otherwise.
     */
    boolean acceptsRepository(String repositoryName);
}
