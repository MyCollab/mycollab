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
package org.springframework.extensions.jcr.support;

import java.util.Collections;
import java.util.List;

import org.springframework.extensions.jcr.SessionHolderProvider;

/**
 * List based implementation of SessionHolderProviderManager. This class should is intended mainly for testing
 * or for declaring SessionHolderProviders in Spring context files.
 * @author Costin Leau
 * @author Sergio Bossa
 * @author Salvatore Incandela
 */
public class ListSessionHolderProviderManager extends CacheableSessionHolderProviderManager {

    private List<SessionHolderProvider> providers = Collections.emptyList();

    /**
     * @see org.springframework.extensions.jcr.support.AbstractSessionHolderProviderManager#getProviders()
     */
    public List<SessionHolderProvider> getProviders() {
        return providers;
    }

    /**
     * @param providers The providers to set.
     */
    public void setProviders(List<SessionHolderProvider> providers) {
        this.providers = providers;
    }

}
