package com.esofthead.mycollab.module.wiki;

import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.nodetype.NodeType;
import javax.jcr.nodetype.NodeTypeManager;
import javax.jcr.nodetype.NodeTypeTemplate;
import javax.jcr.nodetype.PropertyDefinitionTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.extensions.jcr.JcrSessionFactory;

import com.esofthead.mycollab.module.ecm.ContentException;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.4.0
 *
 */
public class WikiSessionFactory extends JcrSessionFactory {
	private static Logger log = LoggerFactory
			.getLogger(WikiSessionFactory.class);

	@Override
	protected void registerNodeTypes() throws Exception {
		log.info("Register node types");
		final String[] jcrNamespaces = getSession().getWorkspace()
				.getNamespaceRegistry().getPrefixes();
		boolean createNamespace = true;
		for (int i = 0; i < jcrNamespaces.length; i++) {
			if (jcrNamespaces[i].equals("wiki")) {
				createNamespace = false;
				log.debug("Jackrabbit OCM namespace exists.");
			}
		}
		if (createNamespace) {
			getSession().getWorkspace().getNamespaceRegistry()
					.registerNamespace("wiki", "http://www.esofthead.com/wiki");
			log.debug("Successfully created Mycollab content namespace.");
		}
		if (getSession().getRootNode() == null) {
			throw new ContentException("Jcr session setup not successful.");
		}

		NodeTypeManager manager = (NodeTypeManager) getSession().getWorkspace()
				.getNodeTypeManager();
		manager.registerNodeType(createWikiPageType(manager), true);
		manager.registerNodeType(createWikiFolderType(manager), true);
	}

	private NodeTypeTemplate createWikiPageType(NodeTypeManager manager)
			throws NoSuchNodeTypeException, RepositoryException {
		log.info("Register mycollab content type");
		NodeType hierachyNode = manager.getNodeType(NodeType.NT_HIERARCHY_NODE);
		// Create content node type
		NodeTypeTemplate pageTypeTemplate = manager
				.createNodeTypeTemplate(hierachyNode);

		pageTypeTemplate.setAbstract(false);
		pageTypeTemplate.setMixin(false);
		pageTypeTemplate.setName("wiki:page");
		pageTypeTemplate.setPrimaryItemName("page");
		pageTypeTemplate
				.setDeclaredSuperTypeNames(new String[] { NodeType.NT_HIERARCHY_NODE });
		pageTypeTemplate.setQueryable(true);
		pageTypeTemplate.setOrderableChildNodes(false);
		log.debug("PROPERTY {} {}",
				pageTypeTemplate.getDeclaredPropertyDefinitions().length,
				pageTypeTemplate.getDeclaredChildNodeDefinitions().length);

		PropertyDefinitionTemplate subjectPropertyTemplate = manager
				.createPropertyDefinitionTemplate();
		subjectPropertyTemplate.setMultiple(false);
		subjectPropertyTemplate.setName("wiki:subject");
		subjectPropertyTemplate.setMandatory(true);
		subjectPropertyTemplate.setRequiredType(PropertyType.STRING);
		pageTypeTemplate.getPropertyDefinitionTemplates().add(
				subjectPropertyTemplate);

		PropertyDefinitionTemplate contentPropertyTemplate = manager
				.createPropertyDefinitionTemplate();
		contentPropertyTemplate.setMultiple(false);
		contentPropertyTemplate.setName("wiki:content");
		contentPropertyTemplate.setMandatory(true);
		contentPropertyTemplate.setRequiredType(PropertyType.STRING);
		pageTypeTemplate.getPropertyDefinitionTemplates().add(
				contentPropertyTemplate);

		PropertyDefinitionTemplate lockPropertyTemplate = manager
				.createPropertyDefinitionTemplate();
		lockPropertyTemplate.setMultiple(false);
		lockPropertyTemplate.setName("wiki:isLock");
		lockPropertyTemplate.setMandatory(false);
		lockPropertyTemplate.setRequiredType(PropertyType.BOOLEAN);
		pageTypeTemplate.getPropertyDefinitionTemplates().add(
				lockPropertyTemplate);

		PropertyDefinitionTemplate statusPropertyTemplate = manager
				.createPropertyDefinitionTemplate();
		statusPropertyTemplate.setMultiple(false);
		statusPropertyTemplate.setName("wiki:status");
		statusPropertyTemplate.setMandatory(true);
		statusPropertyTemplate.setRequiredType(PropertyType.STRING);
		pageTypeTemplate.getPropertyDefinitionTemplates().add(
				statusPropertyTemplate);

		PropertyDefinitionTemplate categoryPropertyTemplate = manager
				.createPropertyDefinitionTemplate();
		categoryPropertyTemplate.setMultiple(false);
		categoryPropertyTemplate.setName("wiki:category");
		categoryPropertyTemplate.setMandatory(false);
		categoryPropertyTemplate.setRequiredType(PropertyType.STRING);
		pageTypeTemplate.getPropertyDefinitionTemplates().add(
				categoryPropertyTemplate);

		PropertyDefinitionTemplate createdPropertyTemplate = manager
				.createPropertyDefinitionTemplate();
		createdPropertyTemplate.setMultiple(false);
		createdPropertyTemplate.setName("wiki:createdUser");
		createdPropertyTemplate.setMandatory(true);
		createdPropertyTemplate.setRequiredType(PropertyType.STRING);
		pageTypeTemplate.getPropertyDefinitionTemplates().add(
				createdPropertyTemplate);

		return pageTypeTemplate;
	}

	private NodeTypeTemplate createWikiFolderType(NodeTypeManager manager)
			throws NoSuchNodeTypeException, RepositoryException {
		// Create content node type
		NodeTypeTemplate folderTypeTemplate = manager.createNodeTypeTemplate();

		folderTypeTemplate.setAbstract(false);
		folderTypeTemplate.setMixin(false);
		folderTypeTemplate.setName("wiki:folder");
		folderTypeTemplate.setPrimaryItemName("folder");
		folderTypeTemplate
				.setDeclaredSuperTypeNames(new String[] { NodeType.NT_FOLDER });
		folderTypeTemplate.setQueryable(true);
		folderTypeTemplate.setOrderableChildNodes(false);

		PropertyDefinitionTemplate createdPropertyTemplate = manager
				.createPropertyDefinitionTemplate();
		createdPropertyTemplate.setMultiple(false);
		createdPropertyTemplate.setName("wiki:createdUser");
		createdPropertyTemplate.setMandatory(true);
		createdPropertyTemplate.setRequiredType(PropertyType.STRING);
		folderTypeTemplate.getPropertyDefinitionTemplates().add(
				createdPropertyTemplate);

		PropertyDefinitionTemplate namePropertyTemplate = manager
				.createPropertyDefinitionTemplate();
		namePropertyTemplate.setMultiple(false);
		namePropertyTemplate.setName("wiki:name");
		namePropertyTemplate.setMandatory(true);
		namePropertyTemplate.setRequiredType(PropertyType.STRING);
		folderTypeTemplate.getPropertyDefinitionTemplates().add(
				namePropertyTemplate);

		PropertyDefinitionTemplate descPropertyTemplate = manager
				.createPropertyDefinitionTemplate();
		descPropertyTemplate.setMultiple(false);
		descPropertyTemplate.setName("wiki:description");
		descPropertyTemplate.setMandatory(true);
		descPropertyTemplate.setRequiredType(PropertyType.STRING);
		folderTypeTemplate.getPropertyDefinitionTemplates().add(
				descPropertyTemplate);
		return folderTypeTemplate;
	}
}
