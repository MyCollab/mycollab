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

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Workspace;

import org.apache.jackrabbit.core.nodetype.NodeTypeManagerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.extensions.jcr.JcrSessionFactory;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/**
 * Jackrabbit specific {@link JcrSessionFactory} which allows registration of node types in <a
 * href="http://jackrabbit.apache.org/node-types.html">Content node types</a> format.
 * @author Costin Leau
 * @author Sergio Bossa
 * @author Salvatore Incandela
 */
public class JackrabbitSessionFactory extends JcrSessionFactory {

    private static final Logger LOG = LoggerFactory.getLogger(JackrabbitSessionFactory.class);

    private static final String DEFAULT_CONTENT_TYPE = "text/xml";

    /**
     * Node definitions in CND format.
     */
    private Resource[] nodeDefinitions;

    private String contentType = DEFAULT_CONTENT_TYPE;

    /*
     * (non-Javadoc)
     * @see org.springframework.extensions.jcr.JcrSessionFactory#registerNodeTypes()
     */
    @Override
    protected void registerNodeTypes() throws Exception {
        if (!ObjectUtils.isEmpty(nodeDefinitions)) {

            Session session = getBareSession();
            Workspace ws = session.getWorkspace();

            NodeTypeManagerImpl jackrabbitNodeTypeManager = (NodeTypeManagerImpl) ws.getNodeTypeManager();

            boolean debug = LOG.isDebugEnabled();
            for (int i = 0; i < nodeDefinitions.length; i++) {
                Resource resource = nodeDefinitions[i];
                if (debug) {
                    LOG.debug("adding node type definitions from " + resource.getDescription());
                }
                try {
                    //                    ws.getNodeTypeManager().registerNodeType(ntd, allowUpdate)
                    jackrabbitNodeTypeManager.registerNodeTypes(resource.getInputStream(), contentType);
                } catch (RepositoryException ex) {
                    LOG.error("Error registering nodetypes ", ex.getCause());
                }
            }
            session.logout();
        }
    }

    /**
     * @param nodeDefinitions The nodeDefinitions to set.
     */
    public void setNodeDefinitions(Resource[] nodeDefinitions) {
        this.nodeDefinitions = nodeDefinitions;
    }

    /**
     * Indicate the node definition content type (by default, JackrabbitNodeTypeManager#TEXT_XML).
     * @see NodeTypeManagerImpl#TEXT_X_JCR_CND
     * @see NodeTypeManagerImpl#TEXT_XML
     * @param contentType The contentType to set.
     */
    public void setContentType(String contentType) {
        Assert.hasText(contentType, "contentType is required");
        this.contentType = contentType;
    }

}
