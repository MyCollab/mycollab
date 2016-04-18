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

/**
 * Interface used for delimiting Jcr operations based on what the underlying repository supports (in this case
 * optional operations).. Normally not used but useful for casting to restrict access in some situations.
 * @author Costin Leau
 * @author Sergio Bossa
 * @author Salvatore Incandela
 */
public interface JcrOptionalOperations extends JcrModel2Operations {

    /**
     * @see javax.jcr.Session#addLockToken(java.lang.String)
     */
    void addLockToken(String lock);

    /**
     * @see javax.jcr.Session#getLockTokens()
     */
    String[] getLockTokens();

    /**
     * @see javax.jcr.Session#removeLockToken(java.lang.String)
     */
    void removeLockToken(String lt);

}