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
package org.springframework.extensions.jcr.jackrabbit;

import javax.jcr.Repository;

import org.apache.jackrabbit.core.TransientRepository;

/**
 * FactoryBean for creating Jackrabbit's TransientRepository (i.e. repository
 * are initialized for the first session and closed once the last session is
 * closed.
 * 
 * @author Costin Leau
 * @author Sergio Bossa 
 * @author Salvatore Incandela
 * 
 * @since 0.5
 */
public class TransientRepositoryFactoryBean extends RepositoryFactoryBean {

    protected Repository createRepository() throws Exception {
        return new TransientRepository(getRepositoryConfig());
    }

}
