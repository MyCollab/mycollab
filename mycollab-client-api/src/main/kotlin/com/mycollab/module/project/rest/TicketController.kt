package com.mycollab.module.project.rest

import com.mycollab.module.project.domain.ProjectTicket
import com.mycollab.module.project.service.ProjectTicketService

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import java.util.ArrayList

/**
 * @author MyCollab Ltd
 * @since 5.4.6
 */
@RestController
@RequestMapping(value = "/{accountId}/tickets")
class TicketController {

    @Autowired
    private val projectTicketService: ProjectTicketService? = null
}