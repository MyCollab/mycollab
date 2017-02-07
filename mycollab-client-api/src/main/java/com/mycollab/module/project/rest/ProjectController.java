/**
 * This file is part of mycollab-client-api.
 *
 * mycollab-client-api is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-client-api is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-client-api.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.rest;

import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.module.project.domain.criteria.ProjectSearchCriteria;
import com.mycollab.module.project.service.ProjectService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.4.6
 */
@RestController
@RequestMapping(value = "/{accountId}/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @RequestMapping(method = RequestMethod.GET)
    public List list(@PathVariable Integer accountId,
                     @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
                     @RequestParam(value = "limit", required = false) Integer limit) {
        ProjectSearchCriteria searchCriteria = new ProjectSearchCriteria();
        searchCriteria.setSaccountid(NumberSearchField.equal(accountId));
        return projectService.findPageableListByCriteria(new BasicSearchRequest<>(searchCriteria));
    }
}
