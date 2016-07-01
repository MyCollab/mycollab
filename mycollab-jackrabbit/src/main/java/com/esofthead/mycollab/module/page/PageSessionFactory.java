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
package com.esofthead.mycollab.module.page;

import com.esofthead.mycollab.module.ecm.ContentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.extensions.jcr.JcrSessionFactory;

import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.nodetype.NodeType;
import javax.jcr.nodetype.NodeTypeManager;
import javax.jcr.nodetype.NodeTypeTemplate;
import javax.jcr.nodetype.PropertyDefinitionTemplate;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
public class PageSessionFactory extends JcrSessionFactory {
    private static final Logger LOG = LoggerFactory.getLogger(PageSessionFactory.class);

    @Override
    protected void registerNodeTypes() throws Exception {
        LOG.info("Register node types");
        Session session = getSession();
        final String[] jcrNamespaces = session.getWorkspace()
                .getNamespaceRegistry().getPrefixes();
        boolean createNamespace = true;
        for (int i = 0; i < jcrNamespaces.length; i++) {
            if (jcrNamespaces[i].equals("wiki")) {
                createNamespace = false;
                LOG.debug("Jackrabbit OCM namespace exists.");
            }
        }
        if (createNamespace) {
            session.getWorkspace().getNamespaceRegistry().registerNamespace("wiki", "http://www.esofthead.com/wiki");
            LOG.debug("Successfully created Mycollab content namespace.");
        }
        if (session.getRootNode() == null) {
            throw new ContentException("Jcr session setup not successful.");
        }

        NodeTypeManager manager = session.getWorkspace()
                .getNodeTypeManager();
        manager.registerNodeType(createWikiPageType(manager), true);
        manager.registerNodeType(createWikiFolderType(manager), true);
        session.logout();
    }

    private NodeTypeTemplate createWikiPageType(NodeTypeManager manager) throws RepositoryException {
        LOG.info("Register mycollab content type");
        NodeType hierachyNode = manager.getNodeType(NodeType.NT_HIERARCHY_NODE);
        // Create content node type
        NodeTypeTemplate pageTypeTemplate = manager.createNodeTypeTemplate(hierachyNode);

        pageTypeTemplate.setAbstract(false);
        pageTypeTemplate.setMixin(false);
        pageTypeTemplate.setName("wiki:page");
        pageTypeTemplate.setPrimaryItemName("page");
        pageTypeTemplate.setDeclaredSuperTypeNames(new String[]{NodeType.NT_HIERARCHY_NODE});
        pageTypeTemplate.setQueryable(true);
        pageTypeTemplate.setOrderableChildNodes(false);
        LOG.debug("PROPERTY {} {}",
                pageTypeTemplate.getDeclaredPropertyDefinitions().length,
                pageTypeTemplate.getDeclaredChildNodeDefinitions().length);

        PropertyDefinitionTemplate subjectPropertyTemplate = manager.createPropertyDefinitionTemplate();
        subjectPropertyTemplate.setMultiple(false);
        subjectPropertyTemplate.setName("wiki:subject");
        subjectPropertyTemplate.setMandatory(true);
        subjectPropertyTemplate.setRequiredType(PropertyType.STRING);
        pageTypeTemplate.getPropertyDefinitionTemplates().add(subjectPropertyTemplate);

        PropertyDefinitionTemplate contentPropertyTemplate = manager.createPropertyDefinitionTemplate();
        contentPropertyTemplate.setMultiple(false);
        contentPropertyTemplate.setName("wiki:content");
        contentPropertyTemplate.setMandatory(true);
        contentPropertyTemplate.setRequiredType(PropertyType.STRING);
        pageTypeTemplate.getPropertyDefinitionTemplates().add(contentPropertyTemplate);

        PropertyDefinitionTemplate lockPropertyTemplate = manager.createPropertyDefinitionTemplate();
        lockPropertyTemplate.setMultiple(false);
        lockPropertyTemplate.setName("wiki:isLock");
        lockPropertyTemplate.setMandatory(false);
        lockPropertyTemplate.setRequiredType(PropertyType.BOOLEAN);
        pageTypeTemplate.getPropertyDefinitionTemplates().add(lockPropertyTemplate);

        PropertyDefinitionTemplate statusPropertyTemplate = manager.createPropertyDefinitionTemplate();
        statusPropertyTemplate.setMultiple(false);
        statusPropertyTemplate.setName("wiki:status");
        statusPropertyTemplate.setMandatory(true);
        statusPropertyTemplate.setRequiredType(PropertyType.STRING);
        pageTypeTemplate.getPropertyDefinitionTemplates().add(statusPropertyTemplate);

        PropertyDefinitionTemplate categoryPropertyTemplate = manager.createPropertyDefinitionTemplate();
        categoryPropertyTemplate.setMultiple(false);
        categoryPropertyTemplate.setName("wiki:category");
        categoryPropertyTemplate.setMandatory(false);
        categoryPropertyTemplate.setRequiredType(PropertyType.STRING);
        pageTypeTemplate.getPropertyDefinitionTemplates().add(categoryPropertyTemplate);

        PropertyDefinitionTemplate createdPropertyTemplate = manager.createPropertyDefinitionTemplate();
        createdPropertyTemplate.setMultiple(false);
        createdPropertyTemplate.setName("wiki:createdUser");
        createdPropertyTemplate.setMandatory(true);
        createdPropertyTemplate.setRequiredType(PropertyType.STRING);
        pageTypeTemplate.getPropertyDefinitionTemplates().add(createdPropertyTemplate);

        return pageTypeTemplate;
    }

    @SuppressWarnings("unchecked")
    private NodeTypeTemplate createWikiFolderType(NodeTypeManager manager) throws RepositoryException {
        // Create content node type
        NodeTypeTemplate folderTypeTemplate = manager.createNodeTypeTemplate();

        folderTypeTemplate.setAbstract(false);
        folderTypeTemplate.setMixin(false);
        folderTypeTemplate.setName("wiki:folder");
        folderTypeTemplate.setPrimaryItemName("folder");
        folderTypeTemplate.setDeclaredSuperTypeNames(new String[]{NodeType.NT_FOLDER});
        folderTypeTemplate.setQueryable(true);
        folderTypeTemplate.setOrderableChildNodes(false);

        PropertyDefinitionTemplate createdPropertyTemplate = manager.createPropertyDefinitionTemplate();
        createdPropertyTemplate.setMultiple(false);
        createdPropertyTemplate.setName("wiki:createdUser");
        createdPropertyTemplate.setMandatory(true);
        createdPropertyTemplate.setRequiredType(PropertyType.STRING);
        folderTypeTemplate.getPropertyDefinitionTemplates().add(createdPropertyTemplate);

        PropertyDefinitionTemplate namePropertyTemplate = manager.createPropertyDefinitionTemplate();
        namePropertyTemplate.setMultiple(false);
        namePropertyTemplate.setName("wiki:name");
        namePropertyTemplate.setMandatory(true);
        namePropertyTemplate.setRequiredType(PropertyType.STRING);
        folderTypeTemplate.getPropertyDefinitionTemplates().add(namePropertyTemplate);

        PropertyDefinitionTemplate descPropertyTemplate = manager.createPropertyDefinitionTemplate();
        descPropertyTemplate.setMultiple(false);
        descPropertyTemplate.setName("wiki:description");
        descPropertyTemplate.setMandatory(true);
        descPropertyTemplate.setRequiredType(PropertyType.STRING);
        folderTypeTemplate.getPropertyDefinitionTemplates().add(descPropertyTemplate);
        return folderTypeTemplate;
    }
}
