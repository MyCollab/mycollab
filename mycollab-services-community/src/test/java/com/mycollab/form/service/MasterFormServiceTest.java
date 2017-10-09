package com.mycollab.form.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mycollab.test.spring.IntegrationServiceTest;

@RunWith(SpringJUnit4ClassRunner.class)
public class MasterFormServiceTest extends IntegrationServiceTest {

	@Autowired
	private MasterFormService masterFormService;

//	@DataSet
	@Test
	public void testGetForm() {
//		DynaForm form = masterFormService.findCustomForm(1, "Account");
//		Assert.assertNotNull(form);
	}
}
