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

import javax.jcr.Repository;

/**
 * This manager returns the appropriate sessionHolderProvider for the given repository. See the
 * implementations of the interface for more details. <strong>NOTE</strong> one of the reason for this
 * interface was to allow dynamical discovery of SessionHolderProviders for specific JSR-170 implementations
 * at runtime.
 * @author Costin Leau
 * @author Sergio Bossa
 * @author Salvatore Incandela
 */
public interface SessionHolderProviderManager {

    /**
     * Returns the SessionHolderProvider suitable for the given Jcr Repository.
     * @param repository
     * @return
     */
    SessionHolderProvider getSessionProvider(Repository repository);

}