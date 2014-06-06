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
