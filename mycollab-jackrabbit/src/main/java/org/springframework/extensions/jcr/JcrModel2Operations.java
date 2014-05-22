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
    public boolean hasPendingChanges();

    /**
     * @see javax.jcr.Session#importXML(java.lang.String, java.io.InputStream, int)
     */
    public void importXML(String parentAbsPath, InputStream in, int uuidBehavior);

    /**
     * @see javax.jcr.Session#refresh(boolean)
     */
    public void refresh(boolean keepChanges);

    /**
     * @see javax.jcr.Session#setNamespacePrefix(java.lang.String, java.lang.String)
     */
    public void setNamespacePrefix(String prefix, String uri);

    /**
     * @see javax.jcr.Session#move(java.lang.String, java.lang.String)
     */
    public void move(String srcAbsPath, String destAbsPath);

    /**
     * @see javax.jcr.Session#save()
     */
    public void save();

}