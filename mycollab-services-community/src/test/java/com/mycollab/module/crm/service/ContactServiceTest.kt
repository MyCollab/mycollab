/**
 * mycollab-services-community - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.service

import com.mycollab.db.arguments.BasicSearchRequest
import com.mycollab.db.arguments.NumberSearchField
import com.mycollab.db.arguments.SetSearchField
import com.mycollab.db.arguments.StringSearchField
import com.mycollab.module.crm.domain.SimpleContact
import com.mycollab.module.crm.domain.criteria.ContactSearchCriteria
import com.mycollab.test.DataSet
import com.mycollab.test.spring.IntegrationServiceTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

@RunWith(SpringJUnit4ClassRunner::class)
class ContactServiceTest : IntegrationServiceTest() {

    @Autowired
    private lateinit var contactService: ContactService

    @DataSet
    @Test
    fun testGetFindByCriteria() {
        val contacts = contactService.findPageableListByCriteria(BasicSearchRequest(criteria)) as List<SimpleContact>

        assertThat(contacts.size).isEqualTo(1)
        assertThat<SimpleContact>(contacts).extracting("id", "assignuser").contains(tuple(1, "linh"))
    }

    @DataSet
    @Test
    fun testGetTotalCount() {
        val contacts = contactService.findPageableListByCriteria(BasicSearchRequest(criteria))
        assertThat(contacts.size).isEqualTo(1)
    }

    private val criteria: ContactSearchCriteria
        get() {
            val criteria = ContactSearchCriteria()
            criteria.assignUsers = SetSearchField("linh")
            criteria.contactName = StringSearchField.and("Hai")
            criteria.saccountid = NumberSearchField(1)
            return criteria
        }

    @Test
    @DataSet
    fun testSearchContactName() {
        val criteria = ContactSearchCriteria()
        criteria.contactName = StringSearchField.and("Nguyen Hai")
        criteria.saccountid = NumberSearchField(1)
        val contacts = contactService.findPageableListByCriteria(BasicSearchRequest(criteria)) as List<SimpleContact>

        assertThat(contacts.size).isEqualTo(1)
        assertThat<SimpleContact>(contacts).extracting("id", "assignuser").contains(tuple(1, "linh"))
    }

    @Test
    @DataSet
    fun testSearchAssignUsers() {
        val criteria = ContactSearchCriteria()
        criteria.assignUsers = SetSearchField("linh", "hai")
        criteria.saccountid = NumberSearchField(1)

        val contacts = contactService.findPageableListByCriteria(BasicSearchRequest(criteria, 0, Integer.MAX_VALUE)) as List<SimpleContact>

        assertThat(contacts.size).isEqualTo(3)
        assertThat<SimpleContact>(contacts).extracting("id", "assignuser").contains(tuple(1, "linh"), tuple(2, "linh"), tuple(3, "hai"))
    }

    @Test
    @DataSet
    fun testSearchLeadSources() {
        val criteria = ContactSearchCriteria()
        criteria.leadSources = SetSearchField("Email", "Campaign")
        criteria.saccountid = NumberSearchField(1)
        val contacts = contactService.findPageableListByCriteria(BasicSearchRequest(criteria, 0, Integer.MAX_VALUE)) as List<SimpleContact>

        assertThat(contacts.size).isEqualTo(2)
        assertThat<SimpleContact>(contacts).extracting("id", "assignuser").contains(tuple(2, "linh"), tuple(3, "hai"))
    }

    @Test
    @DataSet
    fun testSearchAnyPostalCode() {
        val criteria = ContactSearchCriteria()
        criteria.anyPostalCode = StringSearchField.and("70000")
        criteria.saccountid = NumberSearchField(1)

        val contacts = contactService.findPageableListByCriteria(BasicSearchRequest(criteria, 0, Integer.MAX_VALUE)) as List<SimpleContact>

        assertThat(contacts.size).isEqualTo(2)
        assertThat<SimpleContact>(contacts).extracting("id", "assignuser").contains(tuple(1, "linh"), tuple(2, "linh"))
    }

    @Test
    @DataSet
    fun testSearchAnyCity() {
        val criteria = ContactSearchCriteria()
        criteria.anyCity = StringSearchField.and("HCM")
        criteria.saccountid = NumberSearchField(1)

        val contacts = contactService.findPageableListByCriteria(BasicSearchRequest(criteria, 0, Integer.MAX_VALUE)) as List<SimpleContact>

        assertThat(contacts.size).isEqualTo(2)
        assertThat<SimpleContact>(contacts).extracting("id", "assignuser").contains(tuple(2, "linh"), tuple(3, "hai"))
    }

    @Test
    @DataSet
    fun testSearchAnyPhone() {
        val criteria = ContactSearchCriteria()
        criteria.anyPhone = StringSearchField.and("(111)-(222)")
        criteria.saccountid = NumberSearchField(1)

        val contacts = contactService.findPageableListByCriteria(BasicSearchRequest(criteria)) as List<SimpleContact>

        assertThat(contacts.size).isEqualTo(2)
        assertThat<SimpleContact>(contacts).extracting("id", "assignuser").contains(tuple(2, "linh"), tuple(3, "hai"))
    }

    @Test
    @DataSet
    fun testSearchAnyCountries() {
        val criteria = ContactSearchCriteria()
        criteria.countries = SetSearchField("Viet nam", "America")
        criteria.saccountid = NumberSearchField(1)
        val contacts = contactService.findPageableListByCriteria(BasicSearchRequest(criteria)) as List<SimpleContact>

        assertThat(contacts.size).isEqualTo(2)
        assertThat<SimpleContact>(contacts).extracting("id", "assignuser").contains(tuple(1, "linh"), tuple(3, "hai"))
    }

    @Test
    @DataSet
    fun testSearchAnyState() {
        val criteria = ContactSearchCriteria()
        criteria.anyState = StringSearchField.and("abc")
        criteria.saccountid = NumberSearchField(1)

        val contacts = contactService.findPageableListByCriteria(BasicSearchRequest(criteria)) as List<SimpleContact>

        assertThat(contacts.size).isEqualTo(2)
        assertThat<SimpleContact>(contacts).extracting("id", "assignuser").contains(
                tuple(1, "linh"), tuple(3, "hai"))
    }

    @Test
    @DataSet
    fun testSearchAnyAddress() {
        val criteria = ContactSearchCriteria()
        criteria.anyAddress = StringSearchField.and("ade")
        criteria.saccountid = NumberSearchField(1)
        val contacts = contactService.findPageableListByCriteria(BasicSearchRequest(criteria)) as List<SimpleContact>

        assertThat(contacts.size).isEqualTo(2)
        assertThat<SimpleContact>(contacts).extracting("id", "assignuser").contains(tuple(1, "linh"), tuple(2, "linh"))
    }

    @Test
    @DataSet
    fun testSearchAnyEmail() {
        val criteria = ContactSearchCriteria()
        criteria.anyEmail = StringSearchField.and("abc@y.co")
        criteria.saccountid = NumberSearchField(1)

        val contacts = contactService.findPageableListByCriteria(BasicSearchRequest(criteria)) as List<SimpleContact>

        assertThat(contacts.size).isEqualTo(2)
        assertThat<SimpleContact>(contacts).extracting("id", "assignuser").contains(
                tuple(1, "linh"), tuple(3, "hai"))
    }

    @Test
    @DataSet
    fun testSearchLastname() {
        val criteria = ContactSearchCriteria()
        criteria.lastname = StringSearchField.and("Linh")
        criteria.saccountid = NumberSearchField(1)

        val contacts = contactService.findPageableListByCriteria(BasicSearchRequest(criteria)) as List<SimpleContact>

        assertThat(contacts.size).isEqualTo(1)
        assertThat<SimpleContact>(contacts).extracting("id", "assignuser").contains(
                tuple(2, "linh"))
    }

    @Test
    @DataSet
    fun testSearchFirstname() {
        val criteria = ContactSearchCriteria()
        criteria.firstname = StringSearchField.and("Nguyen")
        criteria.saccountid = NumberSearchField(1)

        val contacts = contactService.findPageableListByCriteria(BasicSearchRequest(criteria)) as List<SimpleContact>

        assertThat(contacts.size).isEqualTo(3)
        assertThat<SimpleContact>(contacts).extracting("id", "assignuser").contains(
                tuple(1, "linh"), tuple(2, "linh"), tuple(3, "hai"))
    }
}
