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

import javax.jcr.Session;

import org.springframework.extensions.jcr.SessionHolder;
import org.springframework.extensions.jcr.SessionHolderProvider;

/**
 * Generic implementation of org.springframework.extensions.jcr.SessionHolderProvider w/o any
 * transaction support.
 * 
 * @author Costin Leau
 * @author Sergio Bossa 
 * @author Salvatore Incandela
 * 
 */
public class GenericSessionHolderProvider implements SessionHolderProvider {

	/**
	 * @see org.springframework.extensions.jcr.SessionHolderProvider#acceptsRepository(java.lang.String)
	 */
	public boolean acceptsRepository(String repositoryName) {
		return true;
	}

	/**
	 * @see org.springframework.extensions.jcr.SessionHolderProvider#createSessionHolder(javax.jcr.Session)
	 */
	public SessionHolder createSessionHolder(Session session) {
		return new SessionHolder(session);
	}

}
