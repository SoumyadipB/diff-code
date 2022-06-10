package com.ericsson.isf.test.controller;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.ericsson.isf.controller.WOExecutionController;
import com.ericsson.isf.test.config.DataSourceConfigTest;
import com.ericsson.isf.test.config.HibernateConfigurationTest;
import com.ericsson.isf.test.config.WebConfigTest;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { WebConfigTest.class, DataSourceConfigTest.class,
		HibernateConfigurationTest.class })
@WebAppConfiguration
@Transactional
@Ignore
public class TestWOExecutionController {

	@Autowired
	private WOExecutionController woExecutionController;
	
	private MockMvc mockMvc;

	private static final String CONTEXT_URL = "/isf-rest-server-java";
	private static final String URL = "/isf-rest-server-java/woExecution/";

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(woExecutionController).build();
	}
	
	@Test
	@Ignore
	public void getTaskRelatedDetailsTest() throws Exception {
		
		 /*mockMvc.perform(get("/todo/{id}", 1L))
         .andExpect(status().isOk())
         .andExpect(view().name("todo/view"))
         .andExpect(forwardedUrl("/WEB-INF/jsp/todo/view.jsp"))
         .andExpect(model().attribute("todo", hasProperty("id", is(1L))))
         .andExpect(model().attribute("todo", hasProperty("description", is("Lorem ipsum"))))
         .andExpect(model().attribute("todo", hasProperty("title", is("Foo"))));
		 */
		 
		mockMvc.perform(MockMvcRequestBuilders
				.get(URL +"/getTaskRelatedDetails/{subActivityID}/{projectID}/{taskID}", "17428","11365","135582")
				.contextPath(CONTEXT_URL))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
}
