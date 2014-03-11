/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.crm.service;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

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
import com.esofthead.mycollab.test.MyCollabClassRunner;
import com.esofthead.mycollab.test.service.ServiceTest;

@RunWith(MyCollabClassRunner.class)
public class AccountServiceTest extends ServiceTest {

	@Autowired
	protected AccountService accountService;

	@DataSet
	@Test
	public void testSearchByCriteria() {
		Assert.assertEquals(
				2,
				accountService.findPagableListByCriteria(
						new SearchRequest<AccountSearchCriteria>(getCriteria(),
								0, Integer.MAX_VALUE)).size());
	}

	@DataSet
	@Test
	public void testGetTotalCounts() {
		Assert.assertEquals(2, accountService.getTotalCount(getCriteria()));
	}

	@DataSet
	@Test
	public void testSearchAnyPhoneField() {
		AccountSearchCriteria criteria = new AccountSearchCriteria();
		criteria.setAnyPhone(new StringSearchField(SearchField.AND, "111"));
		criteria.setSaccountid(new NumberSearchField(1));

		Assert.assertEquals(2, accountService.getTotalCount(criteria));
	}

	@DataSet
	@Test
	public void testSearchAnyMailField() {
		AccountSearchCriteria criteria = new AccountSearchCriteria();
		criteria.setAnyMail(new StringSearchField(SearchField.AND, "abc"));
		criteria.setSaccountid(new NumberSearchField(1));

		Assert.assertEquals(2, accountService.getTotalCount(criteria));
	}

	private AccountSearchCriteria getCriteria() {
		AccountSearchCriteria criteria = new AccountSearchCriteria();
		criteria.setAccountname(new StringSearchField(SearchField.AND, "xy"));
		criteria.setAssignUsers(new SetSearchField<String>(SearchField.AND,
				new String[] { "hai79", "linhduong" }));
		criteria.setIndustries(new SetSearchField<String>(SearchField.AND,
				new String[] { "a", "b" }));
		criteria.setTypes(new SetSearchField<String>(SearchField.AND,
				new String[] { "a", "b" }));
		criteria.setSaccountid(new NumberSearchField(1));
		return criteria;
	}

	@Test
	@DataSet
	public void testRemoveAccounts() {
		accountService.massRemoveWithSession(Arrays.asList(1, 2), "hai79", 1);
		AccountSearchCriteria criteria = new AccountSearchCriteria();
		criteria.setSaccountid(new NumberSearchField(1));

		Assert.assertEquals(1, accountService.getTotalCount(criteria));
	}

	@Test
	@DataSet
	public void testFindAccountById() {
		SimpleAccount account = accountService.findById(1, 1);
		Assert.assertEquals("xyz", account.getAccountname());
	}

	@Test
	@DataSet
	public void testRemoveAccountBySearchCriteria() {
		AccountSearchCriteria criteria = new AccountSearchCriteria();
		criteria.setIndustries(new SetSearchField<String>(SearchField.AND,
				new String[] { "a" }));
		criteria.setSaccountid(new NumberSearchField(1));

		accountService.removeByCriteria(criteria, 1);

		criteria = new AccountSearchCriteria();
		criteria.setSaccountid(new NumberSearchField(1));
		Assert.assertEquals(2, accountService.getTotalCount(criteria));
	}

	@Test
	@DataSet
	public void testUpdateAccount() {
		Account account = new Account();
		account.setId(1);
		account.setAccountname("abc");
		account.setSaccountid(1);
		accountService.updateWithSession(account, "hai79");

		accountService.findById(1, 1);
		Assert.assertEquals("abc", account.getAccountname());
	}

	@Test
	@DataSet
	public void testSearchWebsite() {
		AccountSearchCriteria criteria = new AccountSearchCriteria();
		criteria.setWebsite(new StringSearchField(SearchField.AND,
				"http://www.esofthead.com"));
		criteria.setSaccountid(new NumberSearchField(1));

		Assert.assertEquals(3, accountService.getTotalCount(criteria));
		Assert.assertEquals(
				3,
				accountService.findPagableListByCriteria(
						new SearchRequest<AccountSearchCriteria>(criteria, 0,
								Integer.MAX_VALUE)).size());
	}

	@Test
	@DataSet
	public void tesSearchAnyAddress() {
		AccountSearchCriteria criteria = new AccountSearchCriteria();
		criteria.setAnyAddress(new StringSearchField(SearchField.AND, "123"));
		criteria.setSaccountid(new NumberSearchField(1));

		Assert.assertEquals(2, accountService.getTotalCount(criteria));
		Assert.assertEquals(
				2,
				accountService.findPagableListByCriteria(
						new SearchRequest<AccountSearchCriteria>(criteria, 0,
								Integer.MAX_VALUE)).size());
	}

	@Test
	@DataSet
	public void tesSearchAnyCity() {
		AccountSearchCriteria criteria = new AccountSearchCriteria();
		criteria.setAnyCity(new StringSearchField(SearchField.AND, "ha noi"));
		criteria.setSaccountid(new NumberSearchField(1));

		Assert.assertEquals(2, accountService.getTotalCount(criteria));
		Assert.assertEquals(
				2,
				accountService.findPagableListByCriteria(
						new SearchRequest<AccountSearchCriteria>(criteria, 0,
								Integer.MAX_VALUE)).size());
	}

	@Test
	@DataSet
	public void testAssignUser() {
		AccountSearchCriteria criteria = new AccountSearchCriteria();
		criteria.setAssignUser(new StringSearchField(SearchField.AND, "hai79"));
		criteria.setSaccountid(new NumberSearchField(1));

		Assert.assertEquals(1, accountService.getTotalCount(criteria));
		Assert.assertEquals(
				1,
				accountService.findPagableListByCriteria(
						new SearchRequest<AccountSearchCriteria>(criteria, 0,
								Integer.MAX_VALUE)).size());
	}

	@Test
	@DataSet
	public void testSearchByAssignUserName() {
		AccountSearchCriteria criteria = new AccountSearchCriteria();
		criteria.setAssignUser(new StringSearchField("hai79"));
		criteria.setSaccountid(new NumberSearchField(1));

		Assert.assertEquals(1, accountService.getTotalCount(criteria));
		Assert.assertEquals(
				1,
				accountService.findPagableListByCriteria(
						new SearchRequest<AccountSearchCriteria>(criteria, 0,
								Integer.MAX_VALUE)).size());
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

		List<SimpleAccount> accountList = accountService
				.findPagableListByCriteria(new SearchRequest<AccountSearchCriteria>(
						criteria, 0, Integer.MAX_VALUE));
		Assert.assertEquals(3, accountList.size());
		for (SimpleAccount account1 : accountList) {
			Assert.assertEquals("hai79", account1.getAssignuser());
		}
	}

	@Test
	@DataSet
	public void testQueryAccountWithExtNoValueSearchField() {
		AccountSearchCriteria criteria = new AccountSearchCriteria();
		criteria.setSaccountid(new NumberSearchField(1));
		criteria.addExtraField(new NoValueSearchField(SearchField.AND,
				"m_crm_account.accountName is not null"));
		Assert.assertEquals(3, accountService.getTotalCount(criteria));
		Assert.assertEquals(
				3,
				accountService.findPagableListByCriteria(
						new SearchRequest<AccountSearchCriteria>(criteria, 0,
								Integer.MAX_VALUE)).size());
	}

	@Test
	@DataSet
	public void testQueryAccountWithExtOneValueSearchField() {
		AccountSearchCriteria criteria = new AccountSearchCriteria();
		criteria.setSaccountid(new NumberSearchField(1));
		criteria.addExtraField(new OneValueSearchField(SearchField.AND,
				"m_crm_account.accountName = ", "xyz"));
		Assert.assertEquals(1, accountService.getTotalCount(criteria));
		Assert.assertEquals(
				1,
				accountService.findPagableListByCriteria(
						new SearchRequest<AccountSearchCriteria>(criteria, 0,
								Integer.MAX_VALUE)).size());
	}

	@Test
	@DataSet
	public void testQueryAccountWithExtCollectionValueSearchField() {
		AccountSearchCriteria criteria = new AccountSearchCriteria();
		criteria.setSaccountid(new NumberSearchField(1));

		criteria.addExtraField(new CollectionValueSearchField(SearchField.AND,
				"m_crm_account.industry in ", Arrays.asList("a", "b")));
		Assert.assertEquals(2, accountService.getTotalCount(criteria));
		Assert.assertEquals(
				2,
				accountService.findPagableListByCriteria(
						new SearchRequest<AccountSearchCriteria>(criteria, 0,
								Integer.MAX_VALUE)).size());
	}

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
		Assert.assertEquals(1, accountService.getTotalCount(criteria));
	}
}
