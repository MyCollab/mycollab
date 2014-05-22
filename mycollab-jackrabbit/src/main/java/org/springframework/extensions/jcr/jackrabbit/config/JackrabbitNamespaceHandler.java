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
package org.springframework.extensions.jcr.jackrabbit.config;

import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.extensions.jcr.jackrabbit.LocalTransactionManager;
import org.springframework.extensions.jcr.jackrabbit.RepositoryFactoryBean;
import org.w3c.dom.Element;

/**
 * Jackrabbit specifc namespace handler.
 * @author Costin Leau
 * @author Sergio Bossa
 * @author Salvatore Incandela
 */
public class JackrabbitNamespaceHandler extends NamespaceHandlerSupport {

    /*
     * (non-Javadoc)
     * @see org.springframework.beans.factory.xml.NamespaceHandler#init()
     */
    public void init() {
        registerBeanDefinitionParser("repository", new JackrabbitRepositoryBeanDefinitionParser());
        registerBeanDefinitionParser("transaction-manager", new JackrabbitLocalTransactionManagerBeanDefinitionParser());
    }

    private static class JackrabbitRepositoryBeanDefinitionParser extends AbstractSimpleBeanDefinitionParser {

        @Override
        protected Class<RepositoryFactoryBean> getBeanClass(Element element) {
            return RepositoryFactoryBean.class;
        }
    }

    private static class JackrabbitLocalTransactionManagerBeanDefinitionParser extends
            AbstractSimpleBeanDefinitionParser {

        @Override
        protected Class<LocalTransactionManager> getBeanClass(Element element) {
            return LocalTransactionManager.class;
        }
    }

}
