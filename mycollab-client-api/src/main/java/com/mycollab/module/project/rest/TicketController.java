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

import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.service.ProjectTicketService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.4.6
 */
@RestController
@RequestMapping(value = "/{accountId}/tickets")
public class TicketController {

    @Autowired
    private ProjectTicketService projectTicketService;

    public List<ProjectTicket> list(@PathVariable Integer sAccountId) {
        return new ArrayList<>();
    }
}