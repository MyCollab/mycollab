/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.query;

import com.google.common.collect.Sets;
import com.mycollab.db.query.VariableInjector;
import com.mycollab.module.project.CurrentProjectVariables;

import java.util.Set;

/**
 * @author MyCollab Ltd
 * @since 5.2.1
 */
public class CurrentProjectIdInjector implements VariableInjector<Integer> {
    @Override
    public Set<Integer> eval() {
        return Sets.newHashSet(CurrentProjectVariables.getProjectId());
    }

    @Override
    public Class<Integer> getType() {
        return Integer.class;
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public boolean isCollection() {
        return true;
    }
}
