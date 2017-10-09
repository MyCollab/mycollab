package com.mycollab.module.ecm;

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
 * @since 1.0
 */
public class ContentSessionFactory extends JcrSessionFactory {
    private static final Logger LOG = LoggerFactory.getLogger(ContentSessionFactory.class);

    @Override
    protected void registerNodeTypes() throws Exception {
        LOG.info("Register node types");
        Session session = getSession();
        final String[] jcrNamespaces = session.getWorkspace().getNamespaceRegistry().getPrefixes();
        boolean createNamespace = true;
        for (String jcrNamespace : jcrNamespaces) {
            if (jcrNamespace.equals("mycollab")) {
                createNamespace = false;
                LOG.debug("Jackrabbit OCM namespace exists.");
            }
        }
        if (createNamespace) {
            session.getWorkspace().getNamespaceRegistry()
                    .registerNamespace("mycollab", "http://www.esofthead.com/mycollab");
            LOG.debug("Successfully created Mycollab content namespace.");
        }
        if (session.getRootNode() == null) {
            throw new ContentException("Jcr session setup not successful.");
        }

        NodeTypeManager manager = session.getWorkspace().getNodeTypeManager();
        manager.registerNodeType(createMyCollabContentType(manager), true);
        manager.registerNodeType(createMyCollabFolderType(manager), true);
        session.logout();
    }

    private NodeTypeTemplate createMyCollabContentType(NodeTypeManager manager) throws RepositoryException {
        LOG.info("Register mycollab content type");
        NodeType hierachyNode = manager.getNodeType(NodeType.NT_HIERARCHY_NODE);
        // Create content node type
        NodeTypeTemplate contentTypeTemplate = manager.createNodeTypeTemplate(hierachyNode);

        contentTypeTemplate.setAbstract(false);
        contentTypeTemplate.setMixin(false);
        contentTypeTemplate.setName("mycollab:content");
        contentTypeTemplate.setPrimaryItemName("content");
        contentTypeTemplate.setDeclaredSuperTypeNames(new String[]{NodeType.NT_HIERARCHY_NODE});
        contentTypeTemplate.setQueryable(true);
        contentTypeTemplate.setOrderableChildNodes(false);

        PropertyDefinitionTemplate createdUserPropertyTemplate = manager
                .createPropertyDefinitionTemplate();
        createdUserPropertyTemplate.setMultiple(false);
        createdUserPropertyTemplate.setName("mycollab:createdUser");
        createdUserPropertyTemplate.setMandatory(true);
        createdUserPropertyTemplate.setRequiredType(PropertyType.STRING);
        contentTypeTemplate.getPropertyDefinitionTemplates().add(createdUserPropertyTemplate);

        PropertyDefinitionTemplate contentPathPropertyTemplate = manager.createPropertyDefinitionTemplate();
        contentPathPropertyTemplate.setMultiple(false);
        contentPathPropertyTemplate.setName("mycollab:contentPath");
        contentPathPropertyTemplate.setMandatory(false);
        contentPathPropertyTemplate.setRequiredType(PropertyType.STRING);
        contentTypeTemplate.getPropertyDefinitionTemplates().add(contentPathPropertyTemplate);

        PropertyDefinitionTemplate thumbnailPathPropertyTemplate = manager.createPropertyDefinitionTemplate();
        thumbnailPathPropertyTemplate.setMultiple(false);
        thumbnailPathPropertyTemplate.setName("mycollab:thumbnailPath");
        thumbnailPathPropertyTemplate.setMandatory(false);
        thumbnailPathPropertyTemplate.setRequiredType(PropertyType.STRING);
        contentTypeTemplate.getPropertyDefinitionTemplates().add(
                thumbnailPathPropertyTemplate);

        PropertyDefinitionTemplate lastModifiedUserPropertyTemplate = manager.createPropertyDefinitionTemplate();
        lastModifiedUserPropertyTemplate.setMultiple(false);
        lastModifiedUserPropertyTemplate.setName("mycollab:lastModifiedUser");
        lastModifiedUserPropertyTemplate.setMandatory(true);
        lastModifiedUserPropertyTemplate.setRequiredType(PropertyType.STRING);
        contentTypeTemplate.getPropertyDefinitionTemplates().add(lastModifiedUserPropertyTemplate);

        PropertyDefinitionTemplate mimeTypePropertyTemplate = manager
                .createPropertyDefinitionTemplate();
        mimeTypePropertyTemplate.setMultiple(false);
        mimeTypePropertyTemplate.setName("mycollab:mimeType");
        mimeTypePropertyTemplate.setMandatory(false);
        mimeTypePropertyTemplate.setRequiredType(PropertyType.STRING);
        contentTypeTemplate.getPropertyDefinitionTemplates().add(
                mimeTypePropertyTemplate);

        PropertyDefinitionTemplate sizePropertyTemplate = manager.createPropertyDefinitionTemplate();
        sizePropertyTemplate.setMultiple(false);
        sizePropertyTemplate.setName("mycollab:size");
        sizePropertyTemplate.setMandatory(true);
        sizePropertyTemplate.setRequiredType(PropertyType.LONG);
        contentTypeTemplate.getPropertyDefinitionTemplates().add(sizePropertyTemplate);

        return contentTypeTemplate;
    }

    private NodeTypeTemplate createMyCollabFolderType(NodeTypeManager manager) throws RepositoryException {
        // Create content node type
        NodeTypeTemplate contentTypeTemplate = manager.createNodeTypeTemplate();

        contentTypeTemplate.setAbstract(false);
        contentTypeTemplate.setMixin(false);
        contentTypeTemplate.setName("mycollab:folder");
        contentTypeTemplate.setPrimaryItemName("folder");
        contentTypeTemplate.setDeclaredSuperTypeNames(new String[]{NodeType.NT_FOLDER});
        contentTypeTemplate.setQueryable(true);
        contentTypeTemplate.setOrderableChildNodes(false);

        PropertyDefinitionTemplate createdPropertyTemplate = manager
                .createPropertyDefinitionTemplate();
        createdPropertyTemplate.setMultiple(false);
        createdPropertyTemplate.setName("mycollab:createdUser");
        createdPropertyTemplate.setMandatory(true);
        createdPropertyTemplate.setRequiredType(PropertyType.STRING);
        contentTypeTemplate.getPropertyDefinitionTemplates().add(createdPropertyTemplate);

        PropertyDefinitionTemplate descPropertyTemplate = manager
                .createPropertyDefinitionTemplate();
        descPropertyTemplate.setMultiple(false);
        descPropertyTemplate.setName("jcr:description");
        descPropertyTemplate.setMandatory(false);
        descPropertyTemplate.setRequiredType(PropertyType.STRING);
        contentTypeTemplate.getPropertyDefinitionTemplates().add(descPropertyTemplate);

        return contentTypeTemplate;
    }
}
