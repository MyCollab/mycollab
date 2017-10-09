package com.mycollab.module.project.view

import com.mycollab.core.utils.StringUtils
import com.mycollab.db.arguments.DateSearchField
import com.mycollab.vaadin.EventBusFactory
import com.mycollab.module.project.domain.criteria.StandupReportSearchCriteria
import com.mycollab.module.project.event.StandUpEvent
import org.joda.time.format.DateTimeFormat
import java.util.*

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class StandupUrlResolver : ProjectUrlResolver() {
    init {
        this.addSubResolver("list", ListUrlResolver())
    }

    private class ListUrlResolver : ProjectUrlResolver() {
        private val simpleDateFormat = DateTimeFormat.forPattern("MM-dd-yyyy")

        override fun handlePage(vararg params: String) {
            val searchCriteria = StandupReportSearchCriteria()
            if (params.isEmpty()) {
                searchCriteria.onDate = DateSearchField(GregorianCalendar().time)
            } else {
                val date = parseDate(params[0])
                searchCriteria.onDate = DateSearchField(date)
            }
            EventBusFactory.getInstance().post(StandUpEvent.GotoList(this, searchCriteria))
        }

        /**
         * @param dateVal
         * @return
         */
        private fun parseDate(dateVal: String?): Date {
            return if (StringUtils.isBlank(dateVal)) {
                GregorianCalendar().time
            } else {
                simpleDateFormat.parseDateTime(dateVal).toDate()
            }
        }
    }

}