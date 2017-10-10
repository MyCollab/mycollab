package com.mycollab.module.crm.service

import com.mycollab.db.arguments.*
import com.mycollab.module.crm.dao.AccountMapper
import com.mycollab.module.crm.domain.Account
import com.mycollab.module.crm.domain.SimpleAccount
import com.mycollab.module.crm.domain.criteria.AccountSearchCriteria
import com.mycollab.test.DataSet
import com.mycollab.test.spring.IntegrationServiceTest
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import java.util.Arrays

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple

@RunWith(SpringJUnit4ClassRunner::class)
class AccountServiceTest : IntegrationServiceTest() {

    @Autowired
    private lateinit var accountService: AccountService

    @Autowired
    private lateinit var accountMapper: AccountMapper

    private val criteria: AccountSearchCriteria
        get() {
            val criteria = AccountSearchCriteria()
            criteria.accountname = StringSearchField.and("xy")
            criteria.assignUsers = SetSearchField("hai79", "linhduong")
            criteria.industries = SetSearchField("a", "b")
            criteria.types = SetSearchField("a", "b")
            criteria.saccountid = NumberSearchField(1)
            return criteria
        }

    @DataSet
    @Test
    fun testSearchByCriteria() {
        val accounts = accountService.findPageableListByCriteria(BasicSearchRequest(criteria, 0, Integer.MAX_VALUE)) as List<SimpleAccount>
        assertThat(accounts.size).isEqualTo(2)
        assertThat<SimpleAccount>(accounts).extracting("id", "accountname", "industry").contains(tuple(1, "xyz", "a"), tuple(2, "xyz1", "b"))
    }

    @DataSet
    @Test
    fun testGetTotalCounts() {
        assertThat(accountService.getTotalCount(criteria)).isEqualTo(2)
    }

    @DataSet
    @Test
    fun testSearchAnyPhoneField() {
        val criteria = AccountSearchCriteria()
        criteria.anyPhone = StringSearchField.and("111")
        criteria.saccountid = NumberSearchField(1)
        val accounts = accountService.findPageableListByCriteria(BasicSearchRequest(criteria)) as List<SimpleAccount>

        assertThat(accounts.size).isEqualTo(2)
        assertThat<SimpleAccount>(accounts).extracting("id", "accountname", "industry").contains(tuple(1, "xyz", "a"), tuple(2, "xyz1", "b"))
    }

    @DataSet
    @Test
    fun testSearchAnyMailField() {
        val criteria = AccountSearchCriteria()
        criteria.anyMail = StringSearchField.and("abc")
        criteria.saccountid = NumberSearchField(1)

        val accounts = accountService.findPageableListByCriteria(BasicSearchRequest(criteria)) as List<SimpleAccount>
        assertThat(accounts.size).isEqualTo(2)
        assertThat<SimpleAccount>(accounts).extracting("id", "accountname", "industry")
                .contains(tuple(2, "xyz1", "b"), tuple(3, "xyz2", "c"))
    }

    @Test
    @DataSet
    fun testRemoveAccounts() {
        accountMapper.removeKeysWithSession(Arrays.asList(1, 2))

        val criteria = AccountSearchCriteria()
        criteria.saccountid = NumberSearchField(1)

        val accounts = accountService.findPageableListByCriteria(BasicSearchRequest(criteria)) as List<SimpleAccount>

        assertThat(accounts.size).isEqualTo(1)
        assertThat<SimpleAccount>(accounts).extracting("id", "accountname", "industry").contains(tuple(3, "xyz2", "c"))
    }

    @Test
    @DataSet
    fun testFindAccountById() {
        val account = accountService.findById(1, 1)
        assertThat(account!!.accountname).isEqualTo("xyz")
    }

    @Test
    @DataSet
    fun testRemoveAccountBySearchCriteria() {
        var criteria = AccountSearchCriteria()
        criteria.industries = SetSearchField("a")
        criteria.saccountid = NumberSearchField(1)

        accountService.removeByCriteria(criteria, 1)

        criteria = AccountSearchCriteria()
        criteria.saccountid = NumberSearchField(1)

        val accounts = accountService.findPageableListByCriteria(BasicSearchRequest(criteria)) as List<SimpleAccount>

        assertThat(accounts.size).isEqualTo(2)
        assertThat<SimpleAccount>(accounts).extracting("id", "accountname", "industry").contains(tuple(2, "xyz1", "b"), tuple(3, "xyz2", "c"))
    }

    @Test
    @DataSet
    fun testUpdateAccount() {
        val account = Account()
        account.id = 1
        account.accountname = "abc"
        account.saccountid = 1
        accountService.updateWithSession(account, "hai79")

        val simpleAccount = accountService.findById(1, 1)
        assertThat(simpleAccount!!.accountname).isEqualTo("abc")
        assertThat(simpleAccount.industry).isEqualTo(null)
    }

    @Test
    @DataSet
    fun testSearchWebsite() {
        val criteria = AccountSearchCriteria()
        criteria.website = StringSearchField.and("http://www.esofthead.com")
        criteria.saccountid = NumberSearchField(1)

        val accounts = accountService.findPageableListByCriteria(BasicSearchRequest(criteria)) as List<SimpleAccount>

        assertThat(accounts.size).isEqualTo(3)
        assertThat<SimpleAccount>(accounts).extracting("id", "accountname", "industry")
                .contains(tuple(1, "xyz", "a"), tuple(2, "xyz1", "b"), tuple(3, "xyz2", "c"))
    }

    @Test
    @DataSet
    fun tesSearchAnyAddress() {
        val criteria = AccountSearchCriteria()
        criteria.anyAddress = StringSearchField.and("123")
        criteria.saccountid = NumberSearchField(1)

        val accounts = accountService.findPageableListByCriteria(BasicSearchRequest(criteria)) as List<SimpleAccount>

        assertThat(accounts.size).isEqualTo(2)
        assertThat<SimpleAccount>(accounts).extracting("id", "accountname", "industry").contains(tuple(1, "xyz", "a"), tuple(2, "xyz1", "b"))
    }

    @Test
    @DataSet
    fun tesSearchAnyCity() {
        val criteria = AccountSearchCriteria()
        criteria.anyCity = StringSearchField.and("ha noi")
        criteria.saccountid = NumberSearchField(1)

        val accounts = accountService.findPageableListByCriteria(BasicSearchRequest(criteria)) as List<SimpleAccount>

        assertThat(accounts.size).isEqualTo(2)
        assertThat<SimpleAccount>(accounts).extracting("id", "accountname", "industry").contains(tuple(1, "xyz", "a"), tuple(2, "xyz1", "b"))
    }

    @Test
    @DataSet
    fun testAssignUser() {
        val criteria = AccountSearchCriteria()
        criteria.assignUser = StringSearchField.and("hai79")
        criteria.saccountid = NumberSearchField(1)

        val accounts = accountService.findPageableListByCriteria(BasicSearchRequest(criteria)) as List<SimpleAccount>

        assertThat(accounts.size).isEqualTo(1)
        assertThat<SimpleAccount>(accounts).extracting("id", "accountname", "industry").contains(tuple(1, "xyz", "a"))
    }

    @Test
    @DataSet
    fun testSearchByAssignUserName() {
        val criteria = AccountSearchCriteria()
        criteria.assignUser = StringSearchField.and("hai79")
        criteria.saccountid = NumberSearchField(1)

        val accounts = accountService.findPageableListByCriteria(BasicSearchRequest(criteria)) as List<SimpleAccount>

        assertThat(accounts.size).isEqualTo(1)
        assertThat<SimpleAccount>(accounts).extracting("id", "accountname", "industry").contains(tuple(1, "xyz", "a"))
    }

    @Test
    @DataSet
    fun testMassUpdate() {
        val updateKeys = Arrays.asList(1, 2, 3)
        val account = Account()
        account.assignuser = "hai79"
        account.industry = "aaa"
        accountService.massUpdateWithSession(account, updateKeys, 1)

        val criteria = AccountSearchCriteria()
        criteria.saccountid = NumberSearchField(1)

        val accounts = accountService.findPageableListByCriteria(BasicSearchRequest(criteria)) as List<SimpleAccount>

        assertThat(accounts.size).isEqualTo(3)
        assertThat<SimpleAccount>(accounts).extracting("id", "accountname", "industry",
                "assignuser").contains(tuple(1, "xyz", "aaa", "hai79"),
                tuple(2, "xyz1", "aaa", "hai79"),
                tuple(3, "xyz2", "aaa", "hai79"))
    }

    @Test
    @DataSet
    fun testQueryAccountWithExtNoValueSearchField() {
        val criteria = AccountSearchCriteria()
        criteria.saccountid = NumberSearchField(1)
        criteria.addExtraField(NoValueSearchField(SearchField.AND, "m_crm_account.accountName is not null"))

        val accounts = accountService.findPageableListByCriteria(BasicSearchRequest(criteria)) as List<SimpleAccount>

        assertThat(accounts.size).isEqualTo(3)
        assertThat<SimpleAccount>(accounts).extracting("id", "accountname", "industry")
                .contains(tuple(1, "xyz", "a"), tuple(2, "xyz1", "b"), tuple(3, "xyz2", "c"))
    }

    @Test
    @DataSet
    fun testQueryAccountWithExtOneValueSearchField() {
        val criteria = AccountSearchCriteria()
        criteria.saccountid = NumberSearchField(1)
        criteria.addExtraField(OneValueSearchField(SearchField.AND, "m_crm_account.accountName = ", "xyz"))

        val accounts = accountService.findPageableListByCriteria(BasicSearchRequest(criteria)) as List<SimpleAccount>
        assertThat(accounts.size).isEqualTo(1)
        assertThat<SimpleAccount>(accounts).extracting("id", "accountname", "industry").contains(tuple(1, "xyz", "a"))
    }

    @Test
    @DataSet
    fun testQueryAccountWithExtCollectionValueSearchField() {
        val criteria = AccountSearchCriteria()
        criteria.saccountid = NumberSearchField(1)
        criteria.addExtraField(CollectionValueSearchField(SearchField.AND, "m_crm_account.industry in ", Arrays.asList("a", "b")))
        val accounts = accountService.findPageableListByCriteria(BasicSearchRequest(criteria)) as List<SimpleAccount>

        assertThat(accounts.size).isEqualTo(2)
        assertThat<SimpleAccount>(accounts).extracting("id", "accountname", "industry").contains(tuple(1, "xyz", "a"), tuple(2, "xyz1", "b"))
    }

    @Test
    @DataSet
    fun testQueryAccountWithCompositeValueSearchField() {
        val criteria = AccountSearchCriteria()
        criteria.saccountid = NumberSearchField(1)

        val compoField = CompositionSearchField(SearchField.AND)
        compoField.addField(OneValueSearchField("", "m_crm_account.city = ", "ha noi"))
        compoField.addField(OneValueSearchField("", "m_crm_account.shippingCity = ", "ha noi"))
        criteria.addExtraField(compoField)

        val accounts = accountService.findPageableListByCriteria(BasicSearchRequest(criteria)) as List<SimpleAccount>

        assertThat(accounts.size).isEqualTo(1)
        assertThat<SimpleAccount>(accounts).extracting("id", "accountname", "industry").contains(tuple(1, "xyz", "a"))
    }
}
