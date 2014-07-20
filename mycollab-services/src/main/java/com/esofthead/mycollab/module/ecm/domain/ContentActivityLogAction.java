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

import com.esofthead.mycollab.core.utils.JsonDeSerializer;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 *
 */
public class ContentActivityLogAction {

	public static final String FOLDER_TYPE = "folder";

	public static final String CONTENT_TYPE = "content";

	public String toString() {
		return JsonDeSerializer.toJson(this);
	}

	public static ContentActivityLogAction fromString(String actionDesc) {
		return JsonDeSerializer.fromJson(actionDesc,
				ContentActivityLogAction.class);
	}

	public static class Move extends ContentActivityLogAction {
		private String oldPath;
		private String newPath;
		private String moveType;

		public String getOldPath() {
			return oldPath;
		}

		public void setOldPath(String oldPath) {
			this.oldPath = oldPath;
		}

		public String getNewPath() {
			return newPath;
		}

		public void setNewPath(String newPath) {
			this.newPath = newPath;
		}

		public String getMoveType() {
			return moveType;
		}

		public void setMoveType(String moveType) {
			this.moveType = moveType;
		}
	}

	public static class Create extends ContentActivityLogAction {
		private String path;
		private String createType;

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public String getCreateType() {
			return createType;
		}

		public void setCreateType(String createType) {
			this.createType = createType;
		}
	}

	public static class Delete extends ContentActivityLogAction {
		private String path;
		private String deleteType;

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public String getDeleteType() {
			return deleteType;
		}

		public void setDeleteType(String deleteType) {
			this.deleteType = deleteType;
		}
	}

	public static class Rename extends ContentActivityLogAction {
		private String oldPath;
		private String newPath;
		private String resourceType;

		public String getOldPath() {
			return oldPath;
		}

		public void setOldPath(String oldPath) {
			this.oldPath = oldPath;
		}

		public String getNewPath() {
			return newPath;
		}

		public void setNewPath(String newPath) {
			this.newPath = newPath;
		}

		public String getResourceType() {
			return resourceType;
		}

		public void setResourceType(String resourceType) {
			this.resourceType = resourceType;
		}

	}
}
