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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
