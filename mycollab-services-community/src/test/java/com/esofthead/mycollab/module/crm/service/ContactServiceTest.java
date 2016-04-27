/**
 * This file is part of mycollab-services-community.
 *
 * mycollab-services-community is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services-community is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services-community.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.crm.service;

import com.esofthead.mycollab.core.arguments.*;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.esofthead.mycollab.test.DataSet;
import com.esofthead.mycollab.test.service.IntergrationServiceTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@RunWith(SpringJUnit4ClassRunner.class)
public class ContactServiceTest extends IntergrationServiceTest {

    @Autowired
    protected ContactService contactService;

    @SuppressWarnings("unchecked")
    @DataSet
    @Test
    public void testGetFindByCriteria() {
        List<SimpleContact> contacts = contactService.findPagableListByCriteria(new BasicSearchRequest<>(getCriteria(), 0, Integer.MAX_VALUE));

        assertThat(contacts.size()).isEqualTo(1);
        assertThat(contacts).extracting("id", "assignuser").contains(tuple(1, "linh"));
    }

    @SuppressWarnings("unchecked")
    @DataSet
    @Test
    public void testGetTotalCount() {
        List<SimpleContact> contacts = contactService.findPagableListByCriteria(new BasicSearchRequest<>(getCriteria(), 0, Integer.MAX_VALUE));
        assertThat(contacts.size()).isEqualTo(1);
    }

    private ContactSearchCriteria getCriteria() {
        ContactSearchCriteria criteria = new ContactSearchCriteria();
        criteria.setAssignUsers(new SetSearchField<>("linh"));
        criteria.setContactName(StringSearchField.and("Hai"));
        criteria.setSaccountid(new NumberSearchField(1));
        return criteria;
    }

    @SuppressWarnings("unchecked")
    @Test
    @DataSet
    public void testSearchContactName() {
        ContactSearchCriteria criteria = new ContactSearchCriteria();
        criteria.setContactName(StringSearchField.and("Nguyen Hai"));
        criteria.setSaccountid(new NumberSearchField(1));
        List<SimpleContact> contacts = contactService.findPagableListByCriteria(new BasicSearchRequest<>(criteria, 0, Integer.MAX_VALUE));

        assertThat(contacts.size()).isEqualTo(1);
        assertThat(contacts).extracting("id", "assignuser").contains(tuple(1, "linh"));
    }

    @SuppressWarnings("unchecked")
    @Test
    @DataSet
    public void testSearchAssignUsers() {
        ContactSearchCriteria criteria = new ContactSearchCriteria();
        criteria.setAssignUsers(new SetSearchField<>("linh", "hai"));
        criteria.setSaccountid(new NumberSearchField(1));

        List<SimpleContact> contacts = contactService.findPagableListByCriteria(new BasicSearchRequest<>(criteria, 0, Integer.MAX_VALUE));

        assertThat(contacts.size()).isEqualTo(3);
        assertThat(contacts).extracting("id", "assignuser").contains(
                tuple(1, "linh"), tuple(2, "linh"), tuple(3, "hai"));
    }

    @SuppressWarnings("unchecked")
    @Test
    @DataSet
    public void testSearchLeadSources() {
        ContactSearchCriteria criteria = new ContactSearchCriteria();
        criteria.setLeadSources(new SetSearchField<>("Email", "Campaign"));
        criteria.setSaccountid(new NumberSearchField(1));
        List<SimpleContact> contacts = contactService.findPagableListByCriteria(new BasicSearchRequest<>(criteria, 0, Integer.MAX_VALUE));

        assertThat(contacts.size()).isEqualTo(2);
        assertThat(contacts).extracting("id", "assignuser").contains(tuple(2, "linh"), tuple(3, "hai"));
    }

    @SuppressWarnings("unchecked")
    @Test
    @DataSet
    public void testSearchAnyPostalCode() {
        ContactSearchCriteria criteria = new ContactSearchCriteria();
        criteria.setAnyPostalCode(StringSearchField.and("70000"));
        criteria.setSaccountid(new NumberSearchField(1));

        List<SimpleContact> contacts = contactService.findPagableListByCriteria(new BasicSearchRequest<>(criteria, 0, Integer.MAX_VALUE));

        assertThat(contacts.size()).isEqualTo(2);
        assertThat(contacts).extracting("id", "assignuser").contains(tuple(1, "linh"), tuple(2, "linh"));
    }

    @SuppressWarnings("unchecked")
    @Test
    @DataSet
    public void testSearchAnyCity() {
        ContactSearchCriteria criteria = new ContactSearchCriteria();
        criteria.setAnyCity(StringSearchField.and("HCM"));
        criteria.setSaccountid(new NumberSearchField(1));

        List<SimpleContact> contacts = contactService.findPagableListByCriteria(new BasicSearchRequest<>(criteria, 0, Integer.MAX_VALUE));

        assertThat(contacts.size()).isEqualTo(2);
        assertThat(contacts).extracting("id", "assignuser").contains(tuple(2, "linh"), tuple(3, "hai"));
    }

    @SuppressWarnings("unchecked")
    @Test
    @DataSet
    public void testSearchAnyPhone() {
        ContactSearchCriteria criteria = new ContactSearchCriteria();
        criteria.setAnyPhone(StringSearchField.and("(111)-(222)"));
        criteria.setSaccountid(new NumberSearchField(1));

        List<SimpleContact> contacts = contactService.findPagableListByCriteria(new BasicSearchRequest<>(criteria, 0, Integer.MAX_VALUE));

        assertThat(contacts.size()).isEqualTo(2);
        assertThat(contacts).extracting("id", "assignuser").contains(tuple(2, "linh"), tuple(3, "hai"));
    }

    @SuppressWarnings("unchecked")
    @Test
    @DataSet
    public void testSearchAnyCountries() {
        ContactSearchCriteria criteria = new ContactSearchCriteria();
        criteria.setCountries(new SetSearchField<>("Viet nam", "America"));
        criteria.setSaccountid(new NumberSearchField(1));
        List<SimpleContact> contacts = contactService.findPagableListByCriteria(new BasicSearchRequest<>(criteria, 0, Integer.MAX_VALUE));

        assertThat(contacts.size()).isEqualTo(2);
        assertThat(contacts).extracting("id", "assignuser").contains(tuple(1, "linh"), tuple(3, "hai"));
    }

    @SuppressWarnings("unchecked")
    @Test
    @DataSet
    public void testSearchAnyState() {
        ContactSearchCriteria criteria = new ContactSearchCriteria();
        criteria.setAnyState(StringSearchField.and("abc"));
        criteria.setSaccountid(new NumberSearchField(1));

        List<SimpleContact> contacts = contactService.findPagableListByCriteria(new BasicSearchRequest<>(criteria, 0, Integer.MAX_VALUE));

        assertThat(contacts.size()).isEqualTo(2);
        assertThat(contacts).extracting("id", "assignuser").contains(
                tuple(1, "linh"), tuple(3, "hai"));
    }

    @SuppressWarnings("unchecked")
    @Test
    @DataSet
    public void testSearchAnyAddress() {
        ContactSearchCriteria criteria = new ContactSearchCriteria();
        criteria.setAnyAddress(StringSearchField.and("ade"));
        criteria.setSaccountid(new NumberSearchField(1));
        List<SimpleContact> contacts = contactService.findPagableListByCriteria(new BasicSearchRequest<>(criteria, 0, Integer.MAX_VALUE));

        assertThat(contacts.size()).isEqualTo(2);
        assertThat(contacts).extracting("id", "assignuser").contains(tuple(1, "linh"), tuple(2, "linh"));
    }

    @SuppressWarnings("unchecked")
    @Test
    @DataSet
    public void testSearchAnyEmail() {
        ContactSearchCriteria criteria = new ContactSearchCriteria();
        criteria.setAnyEmail(StringSearchField.and("abc@y.co"));
        criteria.setSaccountid(new NumberSearchField(1));

        List<SimpleContact> contacts = contactService
                .findPagableListByCriteria(new BasicSearchRequest<>(
                        criteria, 0, Integer.MAX_VALUE));

        assertThat(contacts.size()).isEqualTo(2);
        assertThat(contacts).extracting("id", "assignuser").contains(
                tuple(1, "linh"), tuple(3, "hai"));
    }

    @SuppressWarnings("unchecked")
    @Test
    @DataSet
    public void testSearchLastname() {
        ContactSearchCriteria criteria = new ContactSearchCriteria();
        criteria.setLastname(StringSearchField.and("Linh"));
        criteria.setSaccountid(new NumberSearchField(1));

        List<SimpleContact> contacts = contactService
                .findPagableListByCriteria(new BasicSearchRequest<>(
                        criteria, 0, Integer.MAX_VALUE));

        assertThat(contacts.size()).isEqualTo(1);
        assertThat(contacts).extracting("id", "assignuser").contains(
                tuple(2, "linh"));
    }

    @SuppressWarnings("unchecked")
    @Test
    @DataSet
    public void testSearchFirstname() {
        ContactSearchCriteria criteria = new ContactSearchCriteria();
        criteria.setFirstname(StringSearchField.and("Nguyen"));
        criteria.setSaccountid(new NumberSearchField(1));

        List<SimpleContact> contacts = contactService
                .findPagableListByCriteria(new BasicSearchRequest<>(
                        criteria, 0, Integer.MAX_VALUE));

        assertThat(contacts.size()).isEqualTo(3);
        assertThat(contacts).extracting("id", "assignuser").contains(
                tuple(1, "linh"), tuple(2, "linh"), tuple(3, "hai"));
    }
}
