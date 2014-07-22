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
 * Utility class for Java Content Repository. The hex escaping/unescaping is based on Brian Moseley
 * <bcm@osafoundation.org> work.
 * @author Costin Leau
 * @author Sergio Bossa
 * @author Salvatore Incandela
 */
public abstract class JcrUtils {

    

    public static boolean supportsLevel2(Repository repository) {
        return "true".equals(repository.getDescriptor(Repository.LEVEL_2_SUPPORTED));
    }

    public static boolean supportsTransactions(Repository repository) {
        return "true".equals(repository.getDescriptor(Repository.OPTION_TRANSACTIONS_SUPPORTED));
    }

    public static boolean supportsVersioning(Repository repository) {
        return "true".equals(repository.getDescriptor(Repository.OPTION_VERSIONING_SUPPORTED));
    }

    public static boolean supportsObservation(Repository repository) {
        return "true".equals(repository.getDescriptor(Repository.OPTION_OBSERVATION_SUPPORTED));
    }

    public static boolean supportsLocking(Repository repository) {
        return "true".equals(repository.getDescriptor(Repository.OPTION_LOCKING_SUPPORTED));
    }

    public static boolean supportsSQLQuery(Repository repository) {
        return "true".equals(repository.getDescriptor(Repository.OPTION_QUERY_SQL_SUPPORTED));
    }

    public static boolean supportsXPathPosIndex(Repository repository) {
        return "true".equals(repository.getDescriptor(Repository.QUERY_XPATH_POS_INDEX));
    }

    public static boolean supportsXPathDocOrder(Repository repository) {
        return "true".equals(repository.getDescriptor(Repository.QUERY_XPATH_DOC_ORDER));
    }

}
