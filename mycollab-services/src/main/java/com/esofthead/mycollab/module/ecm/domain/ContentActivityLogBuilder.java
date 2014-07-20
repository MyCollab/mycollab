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
package com.esofthead.mycollab.module.ecm.domain;

import com.esofthead.mycollab.module.ecm.domain.ContentActivityLogAction.Create;
import com.esofthead.mycollab.module.ecm.domain.ContentActivityLogAction.Delete;
import com.esofthead.mycollab.module.ecm.domain.ContentActivityLogAction.Move;
import com.esofthead.mycollab.module.ecm.domain.ContentActivityLogAction.Rename;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 *
 */
public class ContentActivityLogBuilder {
	public static ContentActivityLogAction makeCreateFolder(String path) {
		return makeCreate(path, ContentActivityLogAction.FOLDER_TYPE);
	}

	public static ContentActivityLogAction makeCreateContent(String path) {
		return makeCreate(path, ContentActivityLogAction.CONTENT_TYPE);
	}

	private static ContentActivityLogAction makeCreate(String path,
			String createType) {
		Create createAction = new Create();
		createAction.setPath(path);
		createAction.setCreateType(createType);
		return createAction;
	}

	public static ContentActivityLogAction makeMoveFolder(String oldPath,
			String newPath) {
		return makeMove(oldPath, newPath, ContentActivityLogAction.FOLDER_TYPE);
	}

	public static ContentActivityLogAction makeMoveContent(String oldPath,
			String newPath) {
		return makeMove(oldPath, newPath, ContentActivityLogAction.CONTENT_TYPE);
	}

	public static ContentActivityLogAction makeRenameFolder(String oldPath,
			String newPath) {
		return makeRenameFolder(oldPath, newPath,
				ContentActivityLogAction.FOLDER_TYPE);
	}

	public static ContentActivityLogAction makeRenameContent(String oldPath,
			String newPath) {
		return makeRenameFolder(oldPath, newPath,
				ContentActivityLogAction.CONTENT_TYPE);
	}

	private static ContentActivityLogAction makeRenameFolder(String oldPath,
			String newPath, String type) {
		Rename renameAction = new Rename();
		renameAction.setOldPath(oldPath);
		renameAction.setNewPath(newPath);
		renameAction.setResourceType(type);
		return renameAction;
	}

	private static ContentActivityLogAction makeMove(String oldPath,
			String newPath, String moveType) {
		Move moveAction = new Move();
		moveAction.setOldPath(oldPath);
		moveAction.setNewPath(newPath);
		moveAction.setMoveType(moveType);
		return moveAction;
	}

	public static ContentActivityLogAction makeDeleteFolder(String path) {
		return makeDelete(path, ContentActivityLogAction.FOLDER_TYPE);
	}

	public static ContentActivityLogAction makeDeleteContent(String path) {
		return makeDelete(path, ContentActivityLogAction.CONTENT_TYPE);
	}

	private static ContentActivityLogAction makeDelete(String path,
			String deleteType) {
		Delete deleteAction = new Delete();
		deleteAction.setDeleteType(deleteType);
		deleteAction.setPath(path);
		return deleteAction;
	}
}
