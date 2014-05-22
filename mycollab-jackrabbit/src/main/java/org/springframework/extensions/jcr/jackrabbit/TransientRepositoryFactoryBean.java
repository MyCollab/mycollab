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
