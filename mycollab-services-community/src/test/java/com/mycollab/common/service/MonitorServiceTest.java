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
package com.mycollab.common.service;

import com.mycollab.common.domain.MonitorItem;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.test.DataSet;
import com.mycollab.test.service.IntergrationServiceTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
public class MonitorServiceTest extends IntergrationServiceTest {
    @Autowired
    private MonitorItemService monitorItemService;

    @Test
    @DataSet
    public void testSaveBatchMonitor() {
        MonitorItem mon1 = new MonitorItem();
        mon1.setMonitorDate(new GregorianCalendar().getTime());
        mon1.setSaccountid(1);
        mon1.setType(ProjectTypeConstants.BUG);
        mon1.setTypeid(1);
        mon1.setExtratypeid(1);
        mon1.setUser("hainguyen");
        List<MonitorItem> items = new ArrayList<>();
        items.add(mon1);
        monitorItemService.saveMonitorItems(items);
    }
}
