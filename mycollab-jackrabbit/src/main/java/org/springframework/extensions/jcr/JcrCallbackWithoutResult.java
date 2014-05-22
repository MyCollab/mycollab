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
