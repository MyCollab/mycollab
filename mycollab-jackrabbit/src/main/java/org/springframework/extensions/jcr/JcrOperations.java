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

import javax.jcr.Node;

import org.springframework.dao.DataAccessException;

/**
 * Interface that specifies a basic set of JCR operations. Not often used, but a useful option to enhance
 * testability, as it can easily be mocked or stubbed.
 * <p>
 * Provides JcrTemplate's data access methods that mirror various Session methods. See the JCR Session
 * javadocs for details on those methods.
 * @author Costin Leau
 * @author Sergio Bossa
 * @author Salvatore Incandela
 */
public interface JcrOperations extends JcrOptionalOperations {

    /**
     * Execute the action specified by the given action object within a Session.
     * @param action callback object that specifies the Jcr action
     * @param exposeNativeSession whether to expose the native Jcr Session to callback code
     * @return a result object returned by the action, or null
     * @throws org.springframework.dao.DataAccessException in case of Jcr errors
     */
    public <T> T execute(JcrCallback<T> action, boolean exposeNativeSession) throws DataAccessException;

    /**
     * Execute the action specified by the given action object within a {@link javax.jcr.Session}. Application
     * exceptions thrown by the action object get propagated to the caller (can only be unchecked). JCR
     * exceptions are transformed into appropriate DAO ones. Allows for returning a result object, i.e. a
     * domain object or a collection of domain objects. Note: Callback code does not need to explicitly log
     * out of the <code>Session</code>; this method will handle that itself. The workspace logged into will be
     * that named by the <code>workspaceName</code> property; if that property is <code>null</code>, the
     * repository's default workspace will be used.
     * @param callback the <code>JCRCallback</code> that executes the client operation
     */
    public <T> T execute(JcrCallback<T> callback) throws DataAccessException;

    /**
     * Dump the contents of the given node in a String. This method parses the whole tree under the node and
     * can generate a huge String.
     * @param node node to be dumped (null is equivalent to the root node)
     * @return node tree in a string representation.
     */
    public String dump(Node node);

    /**
     * Renames a node (with the given name)
     * @param node node to rename
     * @param newName new name for the node
     */
    public void rename(Node node, String newName);

}