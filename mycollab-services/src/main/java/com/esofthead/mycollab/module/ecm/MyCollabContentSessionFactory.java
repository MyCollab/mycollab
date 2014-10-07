/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.ecm;

import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.nodetype.NodeType;
import javax.jcr.nodetype.NodeTypeManager;
import javax.jcr.nodetype.NodeTypeTemplate;
import javax.jcr.nodetype.PropertyDefinitionTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.extensions.jcr.JcrSessionFactory;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class MyCollabContentSessionFactory extends JcrSessionFactory {

	private static Logger log = LoggerFactory
			.getLogger(MyCollabContentSessionFactory.class);

	@Override
	protected void registerNodeTypes() throws Exception {
		log.info("Register node types");
		Session session = getSession();
		final String[] jcrNamespaces = session.getWorkspace()
				.getNamespaceRegistry().getPrefixes();
		boolean createNamespace = true;
		for (int i = 0; i < jcrNamespaces.length; i++) {
			if (jcrNamespaces[i].equals("mycollab")) {
				createNamespace = false;
				log.debug("Jackrabbit OCM namespace exists.");
			}
		}
		if (createNamespace) {
			session.getWorkspace()
					.getNamespaceRegistry()
					.registerNamespace("mycollab",
							"http://www.esofthead.com/mycollab");
			log.debug("Successfully created Mycollab content namespace.");
		}
		if (session.getRootNode() == null) {
			throw new ContentException("Jcr session setup not successful.");
		}

		NodeTypeManager manager = (NodeTypeManager) session.getWorkspace()
				.getNodeTypeManager();
		manager.registerNodeType(createMyCollabContentType(manager), true);
		manager.registerNodeType(createMyCollabFolderType(manager), true);
		session.logout();
	}

	@SuppressWarnings("unchecked")
	private NodeTypeTemplate createMyCollabContentType(NodeTypeManager manager)
			throws NoSuchNodeTypeException, RepositoryException {
		log.info("Register mycollab content type");
		NodeType hierachyNode = manager.getNodeType(NodeType.NT_HIERARCHY_NODE);
		// Create content node type
		NodeTypeTemplate contentTypeTemplate = manager
				.createNodeTypeTemplate(hierachyNode);

		contentTypeTemplate.setAbstract(false);
		contentTypeTemplate.setMixin(false);
		contentTypeTemplate.setName("mycollab:content");
		contentTypeTemplate.setPrimaryItemName("content");
		contentTypeTemplate
				.setDeclaredSuperTypeNames(new String[] { NodeType.NT_HIERARCHY_NODE });
		contentTypeTemplate.setQueryable(true);
		contentTypeTemplate.setOrderableChildNodes(false);
		log.debug("PROPERTY {} {}",
				contentTypeTemplate.getDeclaredPropertyDefinitions().length,
				contentTypeTemplate.getDeclaredChildNodeDefinitions().length);

		PropertyDefinitionTemplate createdUserPropertyTemplate = manager
				.createPropertyDefinitionTemplate();
		createdUserPropertyTemplate.setMultiple(false);
		createdUserPropertyTemplate.setName("mycollab:createdUser");
		createdUserPropertyTemplate.setMandatory(true);
		createdUserPropertyTemplate.setRequiredType(PropertyType.STRING);
		contentTypeTemplate.getPropertyDefinitionTemplates().add(
				createdUserPropertyTemplate);

		PropertyDefinitionTemplate contentPathPropertyTemplate = manager
				.createPropertyDefinitionTemplate();
		contentPathPropertyTemplate.setMultiple(false);
		contentPathPropertyTemplate.setName("mycollab:contentPath");
		contentPathPropertyTemplate.setMandatory(false);
		contentPathPropertyTemplate.setRequiredType(PropertyType.STRING);
		contentTypeTemplate.getPropertyDefinitionTemplates().add(
				contentPathPropertyTemplate);

		PropertyDefinitionTemplate lastModifiedUserPropertyTemplate = manager
				.createPropertyDefinitionTemplate();
		lastModifiedUserPropertyTemplate.setMultiple(false);
		lastModifiedUserPropertyTemplate.setName("mycollab:lastModifiedUser");
		lastModifiedUserPropertyTemplate.setMandatory(true);
		lastModifiedUserPropertyTemplate.setRequiredType(PropertyType.STRING);
		contentTypeTemplate.getPropertyDefinitionTemplates().add(
				lastModifiedUserPropertyTemplate);

		PropertyDefinitionTemplate mimeTypePropertyTemplate = manager
				.createPropertyDefinitionTemplate();
		mimeTypePropertyTemplate.setMultiple(false);
		mimeTypePropertyTemplate.setName("mycollab:mimeType");
		mimeTypePropertyTemplate.setMandatory(false);
		mimeTypePropertyTemplate.setRequiredType(PropertyType.STRING);
		contentTypeTemplate.getPropertyDefinitionTemplates().add(
				mimeTypePropertyTemplate);

		PropertyDefinitionTemplate sizePropertyTemplate = manager
				.createPropertyDefinitionTemplate();
		sizePropertyTemplate.setMultiple(false);
		sizePropertyTemplate.setName("mycollab:size");
		sizePropertyTemplate.setMandatory(true);
		sizePropertyTemplate.setRequiredType(PropertyType.LONG);
		contentTypeTemplate.getPropertyDefinitionTemplates().add(
				sizePropertyTemplate);

		return contentTypeTemplate;
	}

	@SuppressWarnings("unchecked")
	private NodeTypeTemplate createMyCollabFolderType(NodeTypeManager manager)
			throws NoSuchNodeTypeException, RepositoryException {
		// Create content node type
		NodeTypeTemplate contentTypeTemplate = manager.createNodeTypeTemplate();

		contentTypeTemplate.setAbstract(false);
		contentTypeTemplate.setMixin(false);
		contentTypeTemplate.setName("mycollab:folder");
		contentTypeTemplate.setPrimaryItemName("folder");
		contentTypeTemplate
				.setDeclaredSuperTypeNames(new String[] { NodeType.NT_FOLDER });
		contentTypeTemplate.setQueryable(true);
		contentTypeTemplate.setOrderableChildNodes(false);

		PropertyDefinitionTemplate createdPropertyTemplate = manager
				.createPropertyDefinitionTemplate();
		createdPropertyTemplate.setMultiple(false);
		createdPropertyTemplate.setName("mycollab:createdUser");
		createdPropertyTemplate.setMandatory(true);
		createdPropertyTemplate.setRequiredType(PropertyType.STRING);
		contentTypeTemplate.getPropertyDefinitionTemplates().add(
				createdPropertyTemplate);
		return contentTypeTemplate;
	}
}
