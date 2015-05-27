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
package org.springframework.extensions.jcr.config;

import java.util.Iterator;
import java.util.List;

import javax.jcr.observation.Event;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.core.Constants;
import org.springframework.extensions.jcr.EventListenerDefinition;
import org.springframework.extensions.jcr.JcrSessionFactory;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

/**
 * NamespaceHandler for Jcr tags.
 * @author Costin Leau
 * @author Sergio Bossa
 * @author Salvatore Incandela
 */
public class JcrNamespaceHandler extends NamespaceHandlerSupport {

    /*
     * (non-Javadoc)
     * @see org.springframework.beans.factory.xml.NamespaceHandler#init()
     */
    @Override
    public void init() {
        // registerBeanDefinitionParser("repository", new
        // JcrBeanDefinitionParser());
        registerBeanDefinitionParser("eventListenerDefinition", new JcrEventListenerBeanDefinitionParser());
        registerBeanDefinitionParser("sessionFactory", new JcrSessionFactoryBeanDefinitionParser());
    }

    private static class JcrEventListenerBeanDefinitionParser extends AbstractSimpleBeanDefinitionParser {
        public static final String EVENT_TYPE = "eventType";

        public static final String NODE_TYPE_NAME = "nodeTypeName";

        public static final String UUID = "uuid";

        /*
         * (non-Javadoc)
         * @see org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser
         * #getBeanClass(org.w3c.dom.Element)
         */
        @Override
        protected Class<EventListenerDefinition> getBeanClass(Element element) {
            return EventListenerDefinition.class;
        }

        /*
         * (non-Javadoc)
         * @see org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser
         * #postProcess(org.springframework.beans.factory.support. BeanDefinitionBuilder, org.w3c.dom.Element)
         */
        @Override
        protected void postProcess(BeanDefinitionBuilder definitionBuilder, Element element) {
            List<Element> eventTypes = DomUtils.getChildElementsByTagName(element, EVENT_TYPE);
            if (eventTypes != null && eventTypes.size() > 0) {
                // compute event type
                int eventType = 0;
                Constants types = new Constants(Event.class);
                for (Iterator<Element> iter = eventTypes.iterator(); iter.hasNext();) {
                    Element evenTypeElement = iter.next();
                    eventType |= types.asNumber(DomUtils.getTextValue(evenTypeElement)).intValue();
                }
                definitionBuilder.addPropertyValue("eventTypes", Integer.valueOf(eventType));
            }

            List<Element> nodeTypeNames = DomUtils.getChildElementsByTagName(element, NODE_TYPE_NAME);
            String[] nodeTypeValues = new String[nodeTypeNames.size()];

            for (int i = 0; i < nodeTypeValues.length; i++) {
                nodeTypeValues[i] = DomUtils.getTextValue(nodeTypeNames.get(i));
            }
            definitionBuilder.addPropertyValue(NODE_TYPE_NAME, nodeTypeValues);
            List<Element> uuids = DomUtils.getChildElementsByTagName(element, UUID);

            String[] uuidsValues = new String[uuids.size()];

            for (int i = 0; i < uuidsValues.length; i++) {
                uuidsValues[i] = DomUtils.getTextValue(uuids.get(i));
            }

            definitionBuilder.addPropertyValue(UUID, uuidsValues);
            //TODO, reference a listenerBean, it is not a propertyReference
            Element eventListner = DomUtils.getChildElementByTagName(element, "listener");
            String listenerBeanRefName = DomUtils.getTextValue(eventListner);

            definitionBuilder.addPropertyReference("listener", listenerBeanRefName);
        }
    }

    private static class JcrSessionFactoryBeanDefinitionParser extends AbstractSimpleBeanDefinitionParser {
        /*
         * (non-Javadoc)
         * @see org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser
         * #getBeanClass(org.w3c.dom.Element)
         */
        @Override
        protected Class<JcrSessionFactory> getBeanClass(Element element) {
            return JcrSessionFactory.class;
        }
    }
}
