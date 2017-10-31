/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 */
package com.mycollab.reporting

import com.mycollab.db.arguments.BasicSearchRequest
import com.mycollab.db.arguments.SearchCriteria
import com.mycollab.db.persistence.service.ISearchableService
import net.sf.jasperreports.engine.JRDataSource
import net.sf.jasperreports.engine.JRException
import net.sf.jasperreports.engine.JRField
import org.apache.commons.beanutils.PropertyUtils
import org.slf4j.LoggerFactory

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class GroupIteratorDataSource<SearchService : ISearchableService<S>, S : SearchCriteria>(private val searchService: SearchService, private val searchCriteria: S, private val totalItems: Int) : JRDataSource {

    private var currentIndex = 0
    private var currentPage = 0

    private var currentData: List<*>? = null
    private var currentItem: Any? = null

    init {
        val searchRequest = BasicSearchRequest(searchCriteria, currentPage, ITEMS_PER_PAGE)
        currentData = searchService.findPageableListByCriteria(searchRequest)
    }

    @Throws(JRException::class)
    override fun next(): Boolean {
        val result = currentIndex < totalItems
        if (result) {
            if (currentIndex == (currentPage + 1) * ITEMS_PER_PAGE) {
                currentPage += 1
                val searchRequest = BasicSearchRequest(searchCriteria, currentPage, ITEMS_PER_PAGE)
                currentData = searchService.findPageableListByCriteria(searchRequest)
                LOG.debug("Current data ${currentData!!.size}")
            }

            LOG.debug("Current index $currentIndex - $currentPage - ${currentData!!.size} - $totalItems")
            if (currentIndex % ITEMS_PER_PAGE < currentData!!.size) {
                currentItem = currentData!![currentIndex % ITEMS_PER_PAGE]
            }

            currentIndex += 1
        }

        return result
    }

    @Throws(JRException::class)
    override fun getFieldValue(jrField: JRField): Any = try {
        val fieldName = jrField.name
        PropertyUtils.getProperty(currentItem, fieldName)
    } catch (e: Exception) {
        throw JRException(e)
    }

    companion object {
        private val ITEMS_PER_PAGE = 20
        private val LOG = LoggerFactory.getLogger(GroupIteratorDataSource::class.java)
    }
}
