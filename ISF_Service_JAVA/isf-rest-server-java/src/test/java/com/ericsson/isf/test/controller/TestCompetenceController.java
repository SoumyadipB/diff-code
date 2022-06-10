package com.ericsson.isf.test.controller;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.ericsson.isf.controller.CompetenceController;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.model.UserCompetenceModel;
import com.ericsson.isf.service.CompetenceService;
import com.ericsson.isf.test.util.CompetenceUtil;
import com.ericsson.isf.test.util.TestUtil;
import com.ericsson.isf.util.CompetenceStatusEnum;

public class TestCompetenceController {

	@InjectMocks
	private CompetenceController competenceController;

	private MockMvc mockMvc;

	@Mock
	private CompetenceService competenceServiceMock;

	private static final String CONTEXT_URL = "/isf-rest-server-java";
	private static final String URL = "/isf-rest-server-java/competenceController/";

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(competenceController).build();
	}

	@Test
	public void testGetUserCompetenceData() throws Exception {

		List<Map<String, Object>> expectedResponse = CompetenceUtil
				.getExpectedResponseForGetUserCompetenceData(CompetenceStatusEnum.INITIATED.getDisplayStatus());
		UserCompetenceModel reqUserCompetenceModel = new UserCompetenceModel();

		when(competenceServiceMock.getUserCompetenceData(reqUserCompetenceModel, CompetenceUtil.CHANGED_BY_LM)).thenReturn(expectedResponse);

		mockMvc.perform(MockMvcRequestBuilders.post(URL + "getUserCompetenceData/").contextPath(CONTEXT_URL)
				.accept(MediaType.APPLICATION_JSON_UTF8).contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJson(reqUserCompetenceModel)).header("Role", CompetenceUtil.CHANGED_BY_LM)
				.header("Signum", "signum")).andExpect(MockMvcResultMatchers.status().isOk());

	}

	@SuppressWarnings("serial")
	@Test
	public void testInsertCompetenceData() throws Exception {

		Response<Void> expectedResponse = new Response<Void>();
		expectedResponse.addFormMessage(String.format(CompetenceStatusEnum.INITIATED.getSuccessMsg(), 1));

		List<UserCompetenceModel> userCompetenceModel = new ArrayList<UserCompetenceModel>() {
			{
				add(new UserCompetenceModel());
			}
		};

		when(competenceServiceMock.insertCompetenceData(userCompetenceModel)).thenReturn(expectedResponse);

		mockMvc.perform(MockMvcRequestBuilders.post(URL + "insertCompetenceData/").contextPath(CONTEXT_URL)
				.accept(MediaType.APPLICATION_JSON_UTF8).contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJson(userCompetenceModel)))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

}
