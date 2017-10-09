package com.mycollab.common.service

import com.mycollab.common.domain.MonitorItem
import com.mycollab.module.project.ProjectTypeConstants
import com.mycollab.test.DataSet
import com.mycollab.test.spring.IntegrationServiceTest
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import java.util.ArrayList
import java.util.GregorianCalendar

@RunWith(SpringJUnit4ClassRunner::class)
class MonitorServiceTest : IntegrationServiceTest() {
    @Autowired
    private val monitorItemService: MonitorItemService? = null

    @Test
    @DataSet
    fun testSaveBatchMonitor() {
        val mon1 = MonitorItem()
        mon1.monitorDate = GregorianCalendar().time
        mon1.saccountid = 1
        mon1.type = ProjectTypeConstants.BUG
        mon1.typeid = 1
        mon1.extratypeid = 1
        mon1.user = "hainguyen"
        val items = ArrayList<MonitorItem>()
        items.add(mon1)
        monitorItemService!!.saveMonitorItems(items)
    }
}
