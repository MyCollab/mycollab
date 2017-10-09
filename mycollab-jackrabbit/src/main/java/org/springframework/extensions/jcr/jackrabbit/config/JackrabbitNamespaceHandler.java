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
