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

import org.springframework.dao.UncategorizedDataAccessException;

/**
 * Runtime (unchecked) exception used for wrapping the JSR-170 specific checked exceptions.
 * @see javax.jcr.RepositoryException
 * @author Guillaume Bort <guillaume.bort@zenexity.fr>
 * @author Costin Leau
 * @author Sergio Bossa
 * @author Salvatore Incandela
 */
@SuppressWarnings("serial")
public class JcrSystemException extends UncategorizedDataAccessException {

    public JcrSystemException(String message, Throwable ex) {
        super(message, ex);
    }

    /**
     * @param ex
     */
    public JcrSystemException(Throwable ex) {
        super("Repository access exception", ex);
    }
}
