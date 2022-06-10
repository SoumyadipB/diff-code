package com.ericsson.isf.test;
import static org.junit.Assert.*;

import java.security.NoSuchAlgorithmException;
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

import com.ericsson.isf.config.DataSourceConfig;
import com.ericsson.isf.config.PropertiesConfig;
import com.ericsson.isf.config.WebConfig;
import com.ericsson.isf.model.AspExplorerModel;
import com.ericsson.isf.model.AspLoginModel;
import com.ericsson.isf.model.AspVendorModel;
import com.ericsson.isf.service.AccessManagementService;
import com.ericsson.isf.service.AspManagementService;
import com.ericsson.isf.test.config.DataSourceConfigTest;
import com.ericsson.isf.test.config.WebConfigTest;
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
		WebConfigTest.class})
@Transactional
@Ignore
public class AspManagementTest {

	@Autowired
    private AspManagementService aspManagementService;
	
	@Autowired
    private AccessManagementService accessManagementService;
	
	
	AspVendorModel testVendor;
	
	@Before
	public void setUp(){
		testVendor=aspManagementService.getAllVendors().get(0);
	}
	
	private AspLoginModel getTestRegisterationData(){
		AspLoginModel registerAspRequest=new AspLoginModel();
		registerAspRequest.setVendorCode(testVendor.getVendorCode());
		registerAspRequest.setEmail("test@email.com");
		registerAspRequest.setFirstName("firstName");
		registerAspRequest.setLastName("lastName");
		registerAspRequest.setContactNumber("123");
		return registerAspRequest;
	}
	
	
	/**
	 * Test to check is user registration is successful with minimum fields
	 */
	@Test
	public void aspRegisterSuccessTest() throws NoSuchAlgorithmException{
		AspLoginModel testUser = getTestRegisterationData();
		Map<String, Object> response=accessManagementService.aspIsfRegistraion(testUser);
		System.out.println(response);
		assertTrue((boolean)response.get("isSuccess"));
	}
	
	/**
	 * Test for user registration fail if duplicate user is registered
	 */
	@Test
	public void aspRegisterDuplicateFailureTest() throws NoSuchAlgorithmException{
		AspLoginModel testUser = getTestRegisterationData();
		Map<String, Object> response=accessManagementService.aspIsfRegistraion(testUser);
		
		//try to register again
		response=accessManagementService.aspIsfRegistraion(testUser);
		assertFalse((boolean)response.get("isSuccess"));
	}
	
	/**
	 * test to check if registered user is returned by team explorer
	 */
	@Test
	public void aspRegisterUserAvailableInTeamExplorer() throws NoSuchAlgorithmException{
		AspLoginModel testUser = getTestRegisterationData();
		accessManagementService.aspIsfRegistraion(testUser);
		AspExplorerModel aspDetails = null;
		List<AspExplorerModel> asplist = aspManagementService.getAspExplorerForManager(testVendor.getManagerSignum());
		for(AspExplorerModel a:asplist){
			if(a.getEmail().equalsIgnoreCase(testUser.getEmail())){
				aspDetails=a;
				break;
			}
		}
		assertNotNull(aspDetails);
	}
	
	
	/**
	 * test to check if registered user's status is pending
	 */
	@Test
	public void getAspExplorerForManager_defaultStatus() throws NoSuchAlgorithmException{
		AspLoginModel testUser = getTestRegisterationData();
		accessManagementService.aspIsfRegistraion(testUser);
		AspExplorerModel aspDetails = null;
		List<AspExplorerModel> asplist = aspManagementService.getAspExplorerForManager(testVendor.getManagerSignum());
		for(AspExplorerModel a:asplist){
			if(a.getEmail().equalsIgnoreCase(testUser.getEmail())){
				aspDetails=a;
				break;
			}
		}
		assertEquals("PENDING",aspDetails.getStatus());
	}

}
