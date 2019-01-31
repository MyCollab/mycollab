package com.mycollab.module.project.service

import com.mycollab.db.arguments.*
import com.mycollab.module.project.domain.SimpleStandupReport
import com.mycollab.module.project.domain.criteria.StandupReportSearchCriteria
import com.mycollab.test.DataSet
import com.mycollab.test.rule.DbUnitInitializerRule
import com.mycollab.test.spring.IntegrationServiceTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDate

@ExtendWith(SpringExtension::class, DbUnitInitializerRule::class)
class StandupReportServiceTest : IntegrationServiceTest() {
    @Autowired
    private lateinit var reportService: StandupReportService

    @Test
    @DataSet
    fun gatherStandupList() {
        val criteria = StandupReportSearchCriteria()
        criteria.projectIds = SetSearchField(1)
        criteria.logBy = StringSearchField.and("hainguyen")
        val d = LocalDate.of(2013, 3, 13);
        criteria.onDate = DateSearchField(d, DateSearchField.EQUAL)
        criteria.saccountid = NumberSearchField(1)
        val reports = reportService.findPageableListByCriteria(BasicSearchRequest(criteria)) as List<SimpleStandupReport>
        assertThat(reports.size).isEqualTo(1)
        assertThat(reports).extracting("id", "logby", "whattoday").contains(tuple(1, "hainguyen", "a"))
    }

    @Test
    @DataSet
    fun testFindUsersNotDoReportYet() {
        val d = LocalDate.of(2013, 3, 13)
        val users = reportService.findUsersNotDoReportYet(1, d, 1)
        assertThat(users.size).isEqualTo(1)
        assertThat(users[0].username).isEqualTo("linhduong")
    }
}
