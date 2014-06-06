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
