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
package com.mycollab.module.crm.service;

import com.mycollab.module.crm.dao.AccountMapper;
import com.mycollab.module.crm.domain.Account;
import com.mycollab.module.crm.domain.SimpleAccount;
import com.mycollab.module.crm.domain.criteria.AccountSearchCriteria;
import com.mycollab.test.DataSet;
import com.mycollab.test.service.IntergrationServiceTest;
import com.mycollab.db.arguments.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@RunWith(SpringJUnit4ClassRunner.class)
public class AccountServiceTest extends IntergrationServiceTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountMapper accountMapper;

    private AccountSearchCriteria getCriteria() {
        AccountSearchCriteria criteria = new AccountSearchCriteria();
        criteria.setAccountname(StringSearchField.and("xy"));
        criteria.setAssignUsers(new SetSearchField<>("hai79", "linhduong"));
        criteria.setIndustries(new SetSearchField<>("a", "b"));
        criteria.setTypes(new SetSearchField<>("a", "b"));
        criteria.setSaccountid(new NumberSearchField(1));
        return criteria;
    }

    @DataSet
    @Test
    public void testSearchByCriteria() {
        List<SimpleAccount> accounts = accountService.findPagableListByCriteria(new BasicSearchRequest<>(getCriteria(), 0, Integer.MAX_VALUE));

        assertThat(accounts.size()).isEqualTo(2);
        assertThat(accounts).extracting("id", "accountname", "industry").contains(tuple(1, "xyz", "a"), tuple(2, "xyz1", "b"));
    }

    @DataSet
    @Test
    public void testGetTotalCounts() {
        assertThat(accountService.getTotalCount(getCriteria())).isEqualTo(2);
    }

    @DataSet
    @Test
    public void testSearchAnyPhoneField() {
        AccountSearchCriteria criteria = new AccountSearchCriteria();
        criteria.setAnyPhone(StringSearchField.and("111"));
        criteria.setSaccountid(new NumberSearchField(1));
        List<SimpleAccount> accounts = accountService.findPagableListByCriteria(new BasicSearchRequest<>(criteria, 0, Integer.MAX_VALUE));

        assertThat(accounts.size()).isEqualTo(2);
        assertThat(accounts).extracting("id", "accountname", "industry").contains(tuple(1, "xyz", "a"), tuple(2, "xyz1", "b"));
    }

    @DataSet
    @Test
    public void testSearchAnyMailField() {
        AccountSearchCriteria criteria = new AccountSearchCriteria();
        criteria.setAnyMail(StringSearchField.and("abc"));
        criteria.setSaccountid(new NumberSearchField(1));

        List<SimpleAccount> accounts = accountService.findPagableListByCriteria(new BasicSearchRequest<>(criteria, 0, Integer.MAX_VALUE));
        assertThat(accounts.size()).isEqualTo(2);
        assertThat(accounts).extracting("id", "accountname", "industry")
                .contains(tuple(2, "xyz1", "b"), tuple(3, "xyz2", "c"));
    }

    @Test
    @DataSet
    public void testRemoveAccounts() {
        accountMapper.removeKeysWithSession(Arrays.asList(1, 2));

        AccountSearchCriteria criteria = new AccountSearchCriteria();
        criteria.setSaccountid(new NumberSearchField(1));

        List<SimpleAccount> accounts = accountService.findPagableListByCriteria(new BasicSearchRequest<>(criteria, 0, Integer.MAX_VALUE));

        assertThat(accounts.size()).isEqualTo(1);
        assertThat(accounts).extracting("id", "accountname", "industry").contains(tuple(3, "xyz2", "c"));
    }

    @Test
    @DataSet
    public void testFindAccountById() {
        SimpleAccount account = accountService.findById(1, 1);
        assertThat(account.getAccountname()).isEqualTo("xyz");
    }

    @Test
    @DataSet
    public void testRemoveAccountBySearchCriteria() {
        AccountSearchCriteria criteria = new AccountSearchCriteria();
        criteria.setIndustries(new SetSearchField<>("a"));
        criteria.setSaccountid(new NumberSearchField(1));

        accountService.removeByCriteria(criteria, 1);

        criteria = new AccountSearchCriteria();
        criteria.setSaccountid(new NumberSearchField(1));

        List<SimpleAccount> accounts = accountService.findPagableListByCriteria(new BasicSearchRequest<>(criteria, 0, Integer.MAX_VALUE));

        assertThat(accounts.size()).isEqualTo(2);
        assertThat(accounts).extracting("id", "accountname", "industry").contains(tuple(2, "xyz1", "b"), tuple(3, "xyz2", "c"));
    }

    @Test
    @DataSet
    public void testUpdateAccount() {
        Account account = new Account();
        account.setId(1);
        account.setAccountname("abc");
        account.setSaccountid(1);
        accountService.updateWithSession(account, "hai79");

        SimpleAccount simpleAccount = accountService.findById(1, 1);
        assertThat(simpleAccount.getAccountname()).isEqualTo("abc");
        assertThat(simpleAccount.getIndustry()).isEqualTo(null);
    }

    @Test
    @DataSet
    public void testSearchWebsite() {
        AccountSearchCriteria criteria = new AccountSearchCriteria();
        criteria.setWebsite(StringSearchField.and("http://www.esofthead.com"));
        criteria.setSaccountid(new NumberSearchField(1));

        List<SimpleAccount> accounts = accountService.findPagableListByCriteria(new BasicSearchRequest<>(criteria, 0, Integer.MAX_VALUE));

        assertThat(accounts.size()).isEqualTo(3);
        assertThat(accounts).extracting("id", "accountname", "industry")
                .contains(tuple(1, "xyz", "a"), tuple(2, "xyz1", "b"), tuple(3, "xyz2", "c"));
    }

    @Test
    @DataSet
    public void tesSearchAnyAddress() {
        AccountSearchCriteria criteria = new AccountSearchCriteria();
        criteria.setAnyAddress(StringSearchField.and("123"));
        criteria.setSaccountid(new NumberSearchField(1));

        List<SimpleAccount> accounts = accountService.findPagableListByCriteria(new BasicSearchRequest<>(criteria, 0, Integer.MAX_VALUE));

        assertThat(accounts.size()).isEqualTo(2);
        assertThat(accounts).extracting("id", "accountname", "industry").contains(tuple(1, "xyz", "a"), tuple(2, "xyz1", "b"));
    }

    @Test
    @DataSet
    public void tesSearchAnyCity() {
        AccountSearchCriteria criteria = new AccountSearchCriteria();
        criteria.setAnyCity(StringSearchField.and("ha noi"));
        criteria.setSaccountid(new NumberSearchField(1));

        List<SimpleAccount> accounts = accountService.findPagableListByCriteria(new BasicSearchRequest<>(criteria, 0, Integer.MAX_VALUE));

        assertThat(accounts.size()).isEqualTo(2);
        assertThat(accounts).extracting("id", "accountname", "industry").contains(tuple(1, "xyz", "a"), tuple(2, "xyz1", "b"));
    }

    @Test
    @DataSet
    public void testAssignUser() {
        AccountSearchCriteria criteria = new AccountSearchCriteria();
        criteria.setAssignUser(StringSearchField.and("hai79"));
        criteria.setSaccountid(new NumberSearchField(1));

        List<SimpleAccount> accounts = accountService.findPagableListByCriteria(new BasicSearchRequest<>(criteria, 0, Integer.MAX_VALUE));

        assertThat(accounts.size()).isEqualTo(1);
        assertThat(accounts).extracting("id", "accountname", "industry").contains(tuple(1, "xyz", "a"));
    }

    @Test
    @DataSet
    public void testSearchByAssignUserName() {
        AccountSearchCriteria criteria = new AccountSearchCriteria();
        criteria.setAssignUser(StringSearchField.and("hai79"));
        criteria.setSaccountid(new NumberSearchField(1));

        List<SimpleAccount> accounts = accountService.findPagableListByCriteria(new BasicSearchRequest<>(criteria, 0, Integer.MAX_VALUE));

        assertThat(accounts.size()).isEqualTo(1);
        assertThat(accounts).extracting("id", "accountname", "industry").contains(tuple(1, "xyz", "a"));
    }

    @Test
    @DataSet
    public void testMassUpdate() {
        List<Integer> updateKeys = Arrays.asList(1, 2, 3);
        Account account = new Account();
        account.setAssignuser("hai79");
        account.setIndustry("aaa");
        accountService.massUpdateWithSession(account, updateKeys, 1);

        AccountSearchCriteria criteria = new AccountSearchCriteria();
        criteria.setSaccountid(new NumberSearchField(1));

        List<SimpleAccount> accounts = accountService.findPagableListByCriteria(new BasicSearchRequest<>(
                criteria, 0, Integer.MAX_VALUE));

        assertThat(accounts.size()).isEqualTo(3);
        assertThat(accounts).extracting("id", "accountname", "industry",
                "assignuser").contains(tuple(1, "xyz", "aaa", "hai79"),
                tuple(2, "xyz1", "aaa", "hai79"),
                tuple(3, "xyz2", "aaa", "hai79"));
    }

    @Test
    @DataSet
    public void testQueryAccountWithExtNoValueSearchField() {
        AccountSearchCriteria criteria = new AccountSearchCriteria();
        criteria.setSaccountid(new NumberSearchField(1));
        criteria.addExtraField(new NoValueSearchField(SearchField.AND, "m_crm_account.accountName is not null"));

        List<SimpleAccount> accounts = accountService.findPagableListByCriteria(new BasicSearchRequest<>(criteria, 0, Integer.MAX_VALUE));

        assertThat(accounts.size()).isEqualTo(3);
        assertThat(accounts).extracting("id", "accountname", "industry")
                .contains(tuple(1, "xyz", "a"), tuple(2, "xyz1", "b"), tuple(3, "xyz2", "c"));
    }

    @Test
    @DataSet
    public void testQueryAccountWithExtOneValueSearchField() {
        AccountSearchCriteria criteria = new AccountSearchCriteria();
        criteria.setSaccountid(new NumberSearchField(1));
        criteria.addExtraField(new OneValueSearchField(SearchField.AND, "m_crm_account.accountName = ", "xyz"));

        List<SimpleAccount> accounts = accountService.findPagableListByCriteria(new BasicSearchRequest<>(criteria, 0, Integer.MAX_VALUE));
        assertThat(accounts.size()).isEqualTo(1);
        assertThat(accounts).extracting("id", "accountname", "industry").contains(tuple(1, "xyz", "a"));
    }

    @Test
    @DataSet
    public void testQueryAccountWithExtCollectionValueSearchField() {
        AccountSearchCriteria criteria = new AccountSearchCriteria();
        criteria.setSaccountid(new NumberSearchField(1));
        criteria.addExtraField(new CollectionValueSearchField(SearchField.AND, "m_crm_account.industry in ", Arrays.asList("a", "b")));
        List<SimpleAccount> accounts = accountService.findPagableListByCriteria(new BasicSearchRequest<>(criteria, 0, Integer.MAX_VALUE));

        assertThat(accounts.size()).isEqualTo(2);
        assertThat(accounts).extracting("id", "accountname", "industry").contains(tuple(1, "xyz", "a"), tuple(2, "xyz1", "b"));
    }

    @SuppressWarnings("unchecked")
    @Test
    @DataSet
    public void testQueryAccountWithCompositeValueSearchField() {
        AccountSearchCriteria criteria = new AccountSearchCriteria();
        criteria.setSaccountid(new NumberSearchField(1));

        CompositionSearchField compoField = new CompositionSearchField(SearchField.AND);
        compoField.addField(new OneValueSearchField("", "m_crm_account.city = ", "ha noi"));
        compoField.addField(new OneValueSearchField("", "m_crm_account.shippingCity = ", "ha noi"));
        criteria.addExtraField(compoField);

        List<SimpleAccount> accounts = accountService.findPagableListByCriteria(new BasicSearchRequest<>(criteria, 0, Integer.MAX_VALUE));

        assertThat(accounts.size()).isEqualTo(1);
        assertThat(accounts).extracting("id", "accountname", "industry").contains(tuple(1, "xyz", "a"));
    }
}
