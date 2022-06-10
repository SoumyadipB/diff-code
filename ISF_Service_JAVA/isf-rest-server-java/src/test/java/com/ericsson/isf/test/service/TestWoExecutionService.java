package com.ericsson.isf.test.service;

import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.ericsson.isf.model.AspVendorModel;
import com.ericsson.isf.model.TaskModel;
import com.ericsson.isf.model.botstore.ServerBotModel;
import com.ericsson.isf.service.WOExecutionService;
import com.ericsson.isf.test.config.DataSourceConfigTest;
import com.ericsson.isf.test.config.HibernateConfigurationTest;
import com.ericsson.isf.test.config.WebConfigTest;
import com.ericsson.isf.util.AppUtil;

/**
 * WOExecution service test class
 * 
 * @author eakinhm
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { WebConfigTest.class, DataSourceConfigTest.class,
		HibernateConfigurationTest.class })
@WebAppConfiguration
@Transactional
@Ignore
public class TestWoExecutionService {

	@Autowired
	private WOExecutionService woExecutionService;

	AspVendorModel testVendor;

	@Before
	public void setUp() {

		System.out.println("test");
	}

	@Test
	@Ignore
	public void getTaskRelatedDetailsTest() {
		
		List<TaskModel> taskModel=woExecutionService.getTaskRelatedDetails("135582","17428","11365");
				
		System.out.println(taskModel);
		assertNotNull("taskModel is null",taskModel);
	}
	
	@Test
	@Ignore
	public void startTaskTest() throws Exception {
		
		ServerBotModel serverBotModel = new ServerBotModel();
		serverBotModel.setwOID(1114969);
		serverBotModel.setStepID("1a257055-7529-42ed-b258-c13879651c1b");
		serverBotModel.setTaskID(7456);
		serverBotModel.setSubActivityFlowChartDefID(11279);
		serverBotModel.setSignumID("EAKINHM");
		serverBotModel.setDecisionValue("");
		serverBotModel.setExecutionType("Manual");
		serverBotModel.setBotPlatform(null);
		serverBotModel.setType("");
		serverBotModel.setOutputUpload("YES");
		serverBotModel.setBookingType("BOOKING");
		serverBotModel.setRefferer("ui");
		
		
		String serverBotModelStr = AppUtil.convertClassObjectToJson(serverBotModel);
		Map<String, Object> startTaskResponse=woExecutionService.startTask(null,serverBotModel,null);
				
		System.out.println(startTaskResponse);
		assertNotNull("startTaskResponse is null",startTaskResponse);
	}
	
	
	
}
