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

import java.io.InputStream;

/**
 * Interface used for delimiting Jcr operations based on what the underlying repository supports (in this case
 * model 2 operations). Normally not used but useful for casting to restrict access in some situations.
 * @author Costin Leau
 * @author Sergio Bossa
 * @author Salvatore Incandela
 */
public interface JcrModel2Operations extends JcrModel1Operations {

    /**
     * @see javax.jcr.Session#hasPendingChanges()
     */
    boolean hasPendingChanges();

    /**
     * @see javax.jcr.Session#importXML(java.lang.String, java.io.InputStream, int)
     */
    void importXML(String parentAbsPath, InputStream in, int uuidBehavior);

    /**
     * @see javax.jcr.Session#refresh(boolean)
     */
    void refresh(boolean keepChanges);

    /**
     * @see javax.jcr.Session#setNamespacePrefix(java.lang.String, java.lang.String)
     */
    void setNamespacePrefix(String prefix, String uri);

    /**
     * @see javax.jcr.Session#move(java.lang.String, java.lang.String)
     */
    void move(String srcAbsPath, String destAbsPath);

    /**
     * @see javax.jcr.Session#save()
     */
    void save();

}