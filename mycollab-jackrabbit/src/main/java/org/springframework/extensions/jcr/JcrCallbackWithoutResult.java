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

import java.io.IOException;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

/**
 * Simple convenience class for JcrCallback implementation.
 * Allows for implementing a doInJcr version without result,
 * i.e. without the need for a return statement.
 *
 * @author Mirko Zeibig
 * @since 28.11.2012
 * @see JcrTemplate
 */
public abstract class JcrCallbackWithoutResult implements JcrCallback<Void> {

    @Override
    public final Void doInJcr(final Session session) throws IOException, RepositoryException {
        doInJcrWithoutResult(session);
        return null;
    }

    /**
     * Called by {@link JcrTemplate#execute} within an active JCR {@link javax.jcr.Session}. It is not
     * responsible for logging out of the <code>Session</code> or handling transactions. Allows for returning
     * a result object created within the callback, i.e. a domain object or a collection of domain objects. A
     * thrown {@link RuntimeException} is treated as an application exception; it is propagated to the caller
     * of the template.
     * @param session
     * @throws java.io.IOException
     * @throws javax.jcr.RepositoryException
     */
    protected abstract void doInJcrWithoutResult(Session session) throws IOException, RepositoryException;
}
