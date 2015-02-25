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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.esofthead.mycollab.core.arguments.CollectionValueSearchField;
import com.esofthead.mycollab.core.arguments.CompositionSearchField;
import com.esofthead.mycollab.core.arguments.NoValueSearchField;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.OneValueSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.module.crm.domain.Account;
import com.esofthead.mycollab.module.crm.domain.SimpleAccount;
import com.esofthead.mycollab.module.crm.domain.criteria.AccountSearchCriteria;
import com.esofthead.mycollab.test.DataSet;
import com.esofthead.mycollab.test.service.IntergrationServiceTest;

@RunWith(SpringJUnit4ClassRunner.class)
public class AccountServiceTest extends IntergrationServiceTest {

	@Autowired
	protected AccountService accountService;

	private AccountSearchCriteria getCriteria() {
		AccountSearchCriteria criteria = new AccountSearchCriteria();
		criteria.setAccountname(new StringSearchField(SearchField.AND, "xy"));
		criteria.setAssignUsers(new SetSearchField<>(SearchField.AND,
				new String[]{"hai79", "linhduong"}));
		criteria.setIndustries(new SetSearchField<>(SearchField.AND,
				new String[]{"a", "b"}));
		criteria.setTypes(new SetSearchField<>(SearchField.AND,
				new String[]{"a", "b"}));
		criteria.setSaccountid(new NumberSearchField(1));
		return criteria;
	}

	@SuppressWarnings("unchecked")
	@DataSet
	@Test
	public void testSearchByCriteria() {
		List<SimpleAccount> accounts = accountService
				.findPagableListByCriteria(new SearchRequest<>(
						getCriteria(), 0, Integer.MAX_VALUE));

		assertThat(accounts.size()).isEqualTo(2);
		assertThat(accounts).extracting("id", "accountname", "industry")
				.contains(tuple(1, "xyz", "a"), tuple(2, "xyz1", "b"));
	}

	@DataSet
	@Test
	public void testGetTotalCounts() {
		assertThat(accountService.getTotalCount(getCriteria())).isEqualTo(2);
	}

	@SuppressWarnings("unchecked")
	@DataSet
	@Test
	public void testSearchAnyPhoneField() {
		AccountSearchCriteria criteria = new AccountSearchCriteria();
		criteria.setAnyPhone(new StringSearchField(SearchField.AND, "111"));
		criteria.setSaccountid(new NumberSearchField(1));

		List<SimpleAccount> accounts = accountService
				.findPagableListByCriteria(new SearchRequest<>(
						criteria, 0, Integer.MAX_VALUE));
		assertThat(accounts.size()).isEqualTo(2);
		assertThat(accounts).extracting("id", "accountname", "industry")
				.contains(tuple(1, "xyz", "a"), tuple(2, "xyz1", "b"));
	}

	@SuppressWarnings("unchecked")
	@DataSet
	@Test
	public void testSearchAnyMailField() {
		AccountSearchCriteria criteria = new AccountSearchCriteria();
		criteria.setAnyMail(new StringSearchField(SearchField.AND, "abc"));
		criteria.setSaccountid(new NumberSearchField(1));

		List<SimpleAccount> accounts = accountService
				.findPagableListByCriteria(new SearchRequest<>(
						criteria, 0, Integer.MAX_VALUE));
		assertThat(accounts.size()).isEqualTo(2);
		assertThat(accounts).extracting("id", "accountname", "industry")
				.contains(tuple(2, "xyz1", "b"), tuple(3, "xyz2", "c"));
	}

	@SuppressWarnings("unchecked")
	@Test
	@DataSet
	public void testRemoveAccounts() {
		accountService.massRemoveWithSession(Arrays.asList(1, 2), "hai79", 1);

		AccountSearchCriteria criteria = new AccountSearchCriteria();
		criteria.setSaccountid(new NumberSearchField(1));

		List<SimpleAccount> accounts = accountService
				.findPagableListByCriteria(new SearchRequest<>(
						criteria, 0, Integer.MAX_VALUE));

		assertThat(accounts.size()).isEqualTo(1);
		assertThat(accounts).extracting("id", "accountname", "industry")
				.contains(tuple(3, "xyz2", "c"));
	}

	@Test
	@DataSet
	public void testFindAccountById() {
		SimpleAccount account = accountService.findById(1, 1);
		assertThat(account.getAccountname()).isEqualTo("xyz");
	}

	@SuppressWarnings("unchecked")
	@Test
	@DataSet
	public void testRemoveAccountBySearchCriteria() {
		AccountSearchCriteria criteria = new AccountSearchCriteria();
		criteria.setIndustries(new SetSearchField<>(SearchField.AND,
				new String[]{"a"}));
		criteria.setSaccountid(new NumberSearchField(1));

		accountService.removeByCriteria(criteria, 1);

		criteria = new AccountSearchCriteria();
		criteria.setSaccountid(new NumberSearchField(1));

		List<SimpleAccount> accounts = accountService
				.findPagableListByCriteria(new SearchRequest<>(
						criteria, 0, Integer.MAX_VALUE));

		assertThat(accounts.size()).isEqualTo(2);
		assertThat(accounts).extracting("id", "accountname", "industry")
				.contains(tuple(2, "xyz1", "b"), tuple(3, "xyz2", "c"));
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

	@SuppressWarnings("unchecked")
	@Test
	@DataSet
	public void testSearchWebsite() {
		AccountSearchCriteria criteria = new AccountSearchCriteria();
		criteria.setWebsite(new StringSearchField(SearchField.AND,
				"http://www.esofthead.com"));
		criteria.setSaccountid(new NumberSearchField(1));

		List<SimpleAccount> accounts = accountService
				.findPagableListByCriteria(new SearchRequest<>(
						criteria, 0, Integer.MAX_VALUE));

		assertThat(accounts.size()).isEqualTo(3);
		assertThat(accounts).extracting("id", "accountname", "industry")
				.contains(tuple(1, "xyz", "a"), tuple(2, "xyz1", "b"),
						tuple(3, "xyz2", "c"));
	}

	@SuppressWarnings("unchecked")
	@Test
	@DataSet
	public void tesSearchAnyAddress() {
		AccountSearchCriteria criteria = new AccountSearchCriteria();
		criteria.setAnyAddress(new StringSearchField(SearchField.AND, "123"));
		criteria.setSaccountid(new NumberSearchField(1));

		List<SimpleAccount> accounts = accountService
				.findPagableListByCriteria(new SearchRequest<>(
						criteria, 0, Integer.MAX_VALUE));

		assertThat(accounts.size()).isEqualTo(2);
		assertThat(accounts).extracting("id", "accountname", "industry")
				.contains(tuple(1, "xyz", "a"), tuple(2, "xyz1", "b"));
	}

	@SuppressWarnings("unchecked")
	@Test
	@DataSet
	public void tesSearchAnyCity() {
		AccountSearchCriteria criteria = new AccountSearchCriteria();
		criteria.setAnyCity(new StringSearchField(SearchField.AND, "ha noi"));
		criteria.setSaccountid(new NumberSearchField(1));

		List<SimpleAccount> accounts = accountService
				.findPagableListByCriteria(new SearchRequest<>(
						criteria, 0, Integer.MAX_VALUE));

		assertThat(accounts.size()).isEqualTo(2);
		assertThat(accounts).extracting("id", "accountname", "industry")
				.contains(tuple(1, "xyz", "a"), tuple(2, "xyz1", "b"));
	}

	@SuppressWarnings("unchecked")
	@Test
	@DataSet
	public void testAssignUser() {
		AccountSearchCriteria criteria = new AccountSearchCriteria();
		criteria.setAssignUser(new StringSearchField(SearchField.AND, "hai79"));
		criteria.setSaccountid(new NumberSearchField(1));

		List<SimpleAccount> accounts = accountService
				.findPagableListByCriteria(new SearchRequest<>(
						criteria, 0, Integer.MAX_VALUE));

		assertThat(accounts.size()).isEqualTo(1);
		assertThat(accounts).extracting("id", "accountname", "industry")
				.contains(tuple(1, "xyz", "a"));
	}

	@SuppressWarnings("unchecked")
	@Test
	@DataSet
	public void testSearchByAssignUserName() {
		AccountSearchCriteria criteria = new AccountSearchCriteria();
		criteria.setAssignUser(new StringSearchField("hai79"));
		criteria.setSaccountid(new NumberSearchField(1));

		List<SimpleAccount> accounts = accountService
				.findPagableListByCriteria(new SearchRequest<>(
						criteria, 0, Integer.MAX_VALUE));

		assertThat(accounts.size()).isEqualTo(1);
		assertThat(accounts).extracting("id", "accountname", "industry")
				.contains(tuple(1, "xyz", "a"));
	}

	@SuppressWarnings("unchecked")
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

		List<SimpleAccount> accounts = accountService
				.findPagableListByCriteria(new SearchRequest<>(
						criteria, 0, Integer.MAX_VALUE));

		assertThat(accounts.size()).isEqualTo(3);
		assertThat(accounts).extracting("id", "accountname", "industry",
				"assignuser").contains(tuple(1, "xyz", "aaa", "hai79"),
				tuple(2, "xyz1", "aaa", "hai79"),
				tuple(3, "xyz2", "aaa", "hai79"));
	}

	@SuppressWarnings("unchecked")
	@Test
	@DataSet
	public void testQueryAccountWithExtNoValueSearchField() {
		AccountSearchCriteria criteria = new AccountSearchCriteria();
		criteria.setSaccountid(new NumberSearchField(1));
		criteria.addExtraField(new NoValueSearchField(SearchField.AND,
				"m_crm_account.accountName is not null"));
		
		List<SimpleAccount> accounts = accountService
				.findPagableListByCriteria(new SearchRequest<>(
						criteria, 0, Integer.MAX_VALUE));
		
		assertThat(accounts.size()).isEqualTo(3);
		assertThat(accounts).extracting("id", "accountname", "industry")
				.contains(tuple(1, "xyz", "a"), tuple(2, "xyz1", "b"),
						tuple(3, "xyz2", "c"));
	}

	@SuppressWarnings("unchecked")
	@Test
	@DataSet
	public void testQueryAccountWithExtOneValueSearchField() {
		AccountSearchCriteria criteria = new AccountSearchCriteria();
		criteria.setSaccountid(new NumberSearchField(1));
		criteria.addExtraField(new OneValueSearchField(SearchField.AND,
				"m_crm_account.accountName = ", "xyz"));
		
		List<SimpleAccount> accounts = accountService
				.findPagableListByCriteria(new SearchRequest<>(
						criteria, 0, Integer.MAX_VALUE));
		
		assertThat(accounts.size()).isEqualTo(1);
		assertThat(accounts).extracting("id", "accountname", "industry")
				.contains(tuple(1, "xyz", "a"));
	}

	@SuppressWarnings("unchecked")
	@Test
	@DataSet
	public void testQueryAccountWithExtCollectionValueSearchField() {
		AccountSearchCriteria criteria = new AccountSearchCriteria();
		criteria.setSaccountid(new NumberSearchField(1));
		criteria.addExtraField(new CollectionValueSearchField(SearchField.AND,
				"m_crm_account.industry in ", Arrays.asList("a", "b")));
		
		
		List<SimpleAccount> accounts = accountService
				.findPagableListByCriteria(new SearchRequest<>(
						criteria, 0, Integer.MAX_VALUE));
		
		assertThat(accounts.size()).isEqualTo(2);
		assertThat(accounts).extracting("id", "accountname", "industry")
				.contains(tuple(1, "xyz", "a"), tuple(2, "xyz1", "b"));
	}

	@SuppressWarnings("unchecked")
	@Test
	@DataSet
	public void testQueryAccountWithCompositeValueSearchField() {
		AccountSearchCriteria criteria = new AccountSearchCriteria();
		criteria.setSaccountid(new NumberSearchField(1));

		CompositionSearchField compoField = new CompositionSearchField(
				SearchField.AND);
		compoField.addField(new OneValueSearchField("",
				"m_crm_account.city = ", "ha noi"));
		compoField.addField(new OneValueSearchField("",
				"m_crm_account.shippingCity = ", "ha noi"));
		criteria.addExtraField(compoField);
		
		List<SimpleAccount> accounts = accountService
				.findPagableListByCriteria(new SearchRequest<>(
						criteria, 0, Integer.MAX_VALUE));
		
		assertThat(accounts.size()).isEqualTo(1);
		assertThat(accounts).extracting("id", "accountname", "industry")
				.contains(tuple(1, "xyz", "a"));
	}
}
