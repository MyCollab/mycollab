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
