package com.mycollab.module.tracker.service

import com.mycollab.db.arguments.BasicSearchRequest
import com.mycollab.db.arguments.NumberSearchField
import com.mycollab.db.arguments.StringSearchField
import com.mycollab.module.tracker.domain.SimpleComponent
import com.mycollab.module.tracker.domain.criteria.ComponentSearchCriteria
import com.mycollab.test.DataSet
import com.mycollab.test.spring.IntegrationServiceTest
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple

@RunWith(SpringJUnit4ClassRunner::class)
class ComponentServiceTest : IntegrationServiceTest() {

    @Autowired
    private val componentService: ComponentService? = null

    private val criteria: ComponentSearchCriteria
        get() {
            val criteria = ComponentSearchCriteria()
            criteria.projectId = NumberSearchField(1)
            criteria.saccountid = NumberSearchField(1)
            return criteria
        }

    @DataSet
    @Test
    @Throws(ParseException::class)
    fun testGetListComponents() {
        val components = componentService!!.findPageableListByCriteria(BasicSearchRequest(criteria)) as List<SimpleComponent>

        assertThat(components.size).isEqualTo(4)
        assertThat<SimpleComponent>(components).extracting("id", "description", "status",
                "name", "numBugs", "numOpenBugs", "userLeadFullName",
                "lastupdatedtime").contains(
                tuple(1, "aaaaaaa", "Open", "com 1", 1, 1, "Nguyen Hai", dateformat.parse("2014-10-02 06:45:22")),
                tuple(2, "bbbbbbb", "Closed", "com 2", 2, 1, "Nghiem Le", dateformat.parse("2014-10-02 07:45:22")),
                tuple(3, "ccccccc", "Closed", "com 3", 1, 1, "Nguyen Hai", dateformat.parse("2014-10-03 06:45:22")),
                tuple(4, "ddddddd", "Open", "com 4", 0, 0, "Nghiem Le", dateformat.parse("2014-10-02 06:32:22")))
    }

    @DataSet
    @Test
    fun testTotalCount() {
        val components = componentService!!.findPageableListByCriteria(BasicSearchRequest(criteria)) as List<SimpleComponent>
        assertThat(components.size).isEqualTo(4)
    }

    @DataSet
    @Test
    fun testFindComponentById() {
        val criteria = ComponentSearchCriteria()
        criteria.id = NumberSearchField(1)

        val components = componentService!!.findPageableListByCriteria(BasicSearchRequest(criteria)) as List<SimpleComponent>
        assertThat(components.size).isEqualTo(1)
        assertThat<SimpleComponent>(components).extracting("id", "description", "status",
                "name", "numBugs", "numOpenBugs").contains(
                tuple(1, "aaaaaaa", "Open", "com 1", 1, 1))
    }

    @DataSet
    @Test
    fun testFindByCriteria() {
        val criteria = criteria
        criteria.id = NumberSearchField(2)
        criteria.componentName = StringSearchField.and("com 2")
        criteria.status = StringSearchField.and("Closed")
        criteria.userlead = StringSearchField.and("nghiemle")

        val components = componentService!!.findPageableListByCriteria(BasicSearchRequest(criteria)) as List<SimpleComponent>
        assertThat(components.size).isEqualTo(1)
        assertThat<SimpleComponent>(components).extracting("id", "description", "status",
                "name", "numBugs", "numOpenBugs").contains(
                tuple(2, "bbbbbbb", "Closed", "com 2", 2, 1))
    }

    companion object {
        private val dateformat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
    }
}
