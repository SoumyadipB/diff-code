package com.ericsson.isf.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ericsson.isf.model.CompetenceServiceAreaModel;
import com.ericsson.isf.model.CompetenceTrainingModel;
import com.ericsson.isf.model.DownloadTemplateModel;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.model.UserCompetenceModel;
import com.ericsson.isf.service.CompetenceService;

/**
 * 
 * @author eakinhm
 *
 */
@RestController
@RequestMapping("/competenceController")
public class CompetenceController {

	private static final Logger LOG = LoggerFactory.getLogger(CompetenceController.class);

	@Autowired
	private CompetenceService competenceService;

	@RequestMapping(value = "/getDomain/{competenceTypeID}", method = RequestMethod.GET)
	public List<Map<String, Object>> getDomain(@PathVariable int competenceTypeID) {

		LOG.info("getDomain: Success ");
		return competenceService.getDomain(competenceTypeID);
	}

	@RequestMapping(value = "/getTechnology/{competenceTypeID}/{subdomainID}", method = RequestMethod.GET)
	public List<Map<String, Object>> getTechnology(@PathVariable int competenceTypeID, @PathVariable int subdomainID) {

		LOG.info("getTechnology: Success ");
		return competenceService.getTechnology(competenceTypeID, subdomainID);

	}

	@RequestMapping(value = "/getVendor/{competenceTypeID}/{subdomainID}/{technologyID}", method = RequestMethod.GET)
	public List<Map<String, Object>> getVendor(@PathVariable int competenceTypeID, @PathVariable int subdomainID,
			@PathVariable int technologyID) {

		LOG.info("getVendor: Success ");
		return competenceService.getVendor(competenceTypeID, subdomainID, technologyID);

	}

	@RequestMapping(value = "/getCompetence", method = RequestMethod.GET)
	public List<Map<String, Object>> getCompetence() {

		LOG.info("getCompetence: Success ");
		return competenceService.getCompetence();

	}

	@RequestMapping(value = "/getServiceArea/{competenceTypeID}/{subdomainID}/{technologyID}/{vendorID}", method = RequestMethod.GET)
	public List<Map<String, Object>> getServiceArea(@PathVariable int competenceTypeID, @PathVariable int subdomainID,
			@PathVariable int technologyID, @PathVariable int vendorID) {

		LOG.info("getServiceArea: Success ");
		return competenceService.getServiceArea(competenceTypeID, subdomainID, technologyID, vendorID);

	}

	@RequestMapping(value = "/getBaseline", method = RequestMethod.GET)
	public List<Map<String, Object>> getBaseline() {

		LOG.info("Baseline: Success ");
		return competenceService.getBaseline();

	}

	@RequestMapping(value = "/getAmbition/{flag}/{competenceGradeID}", method = RequestMethod.GET)
	public Response<List<Map<String, Object>>> getAmbition(@PathVariable String flag, @PathVariable int competenceGradeID) {
		  System.out.println(flag+" "+competenceGradeID);
			LOG.info("getAmbition: Success ");
			return competenceService.getAmbition(flag, competenceGradeID);
	}

	@RequestMapping(value = "/getUserCompetenceRow/{systemID}", method = RequestMethod.GET)
	public UserCompetenceModel getUserCompetenceRow(@PathVariable int systemID) {
		return competenceService.getUserCompetenceRow(systemID);
	}

	@RequestMapping(value = "/insertCompetenceData", method = RequestMethod.POST)
	public Response<Void> insertCompetenceData(@RequestBody List<UserCompetenceModel> userCompetenceModel)
			throws ServletException {
		return competenceService.insertCompetenceData(userCompetenceModel);
	}

	@RequestMapping(value = "/getUserCompetenceData", method = RequestMethod.POST)
	public List<Map<String, Object>> getUserCompetenceData(@RequestBody UserCompetenceModel userCompetenceModel,
			@RequestHeader("Role") String role, @RequestHeader("Signum") String signum) throws ServletException {
		return competenceService.getUserCompetenceData(userCompetenceModel, role);
	}

	@RequestMapping(value = "/getWBLData", method = RequestMethod.GET)
	public List<Map<String, Object>> getWBLData() {

		LOG.info("getWBLData: Success ");
		return competenceService.getWBLData();

	}

	@RequestMapping(value = "/insertCompetenceServiceArea", method = RequestMethod.POST)
	public Response<Void> insertCompetenceServiceArea(
			@RequestBody CompetenceServiceAreaModel competenceServiceAreaModel) throws ServletException {
		return this.competenceService.insertCompetenceServiceArea(competenceServiceAreaModel);
	}

	@RequestMapping(value = "/getAllCompetenceServiceArea", method = RequestMethod.GET)
	public List<Map<String, Object>> getAllCompetenceServiceArea() {
		return competenceService.getAllCompetenceServiceArea();
	}

	@RequestMapping(value = "/getCompetenceUpgrade", method = RequestMethod.GET)
	public List<Map<String, Object>> getCompetenceUpgrade() {
		return competenceService.getCompetenceUpgrade();
	}

	@RequestMapping(value = "/insertCompetenceDataBulk/{signum}", method = RequestMethod.POST, consumes = "multipart/form-data")
	public Response<Void> insertCompetenceDataBulk(@RequestPart("file") MultipartFile file,
			@PathVariable("signum") String signum) throws IOException, SQLException {
		Response<Void> responseData = this.competenceService.insertCompetenceDataBulk(file, signum);
		return responseData;
	}

	@RequestMapping(value = "/insertTraining", method = RequestMethod.POST)
	public Response<Void> insertTraining(@RequestBody CompetenceTrainingModel competenceTrainingModel)
			throws ServletException {
		return this.competenceService.insertTraining(competenceTrainingModel);
	}

	@RequestMapping(value = "/getTrainingData/{competenceID}", method = RequestMethod.GET)
	public List<Map<String, Object>> getTrainingData(@PathVariable("competenceID") int competenceID) {
		return competenceService.getTrainingData(competenceID);
	}

	@RequestMapping(value = "/enableDisableCompetence/{competenceID}/{active}", method = RequestMethod.POST)
	public Response<Void> enableDisableCompetence(@PathVariable("competenceID") int competenceID,
			@PathVariable("active") boolean active) throws ServletException {
		return this.competenceService.enableDisableCompetence(competenceID, active);
	}

	@RequestMapping(value = "/insertMasterTrainingBulk/{signum}", method = RequestMethod.POST, consumes = "multipart/form-data")
	public Response<Void> insertMasterTrainingBulk(@RequestPart("file") MultipartFile file,
			@PathVariable("signum") String signum) throws IOException, SQLException {
		Response<Void> responseData = this.competenceService.insertMasterTrainingBulk(file, signum);
		return responseData;
	}

	@RequestMapping(value = "/insertItmTrainingBulk/{signum}", method = RequestMethod.POST, consumes = "multipart/form-data")
	public Response<Void> insertItmTrainingBulk(@RequestPart("file") MultipartFile file,
			@PathVariable("signum") String signum) throws IOException, SQLException {
		Response<Void> responseData = this.competenceService.insertItmTrainingBulk(file, signum);
		return responseData;
	}

	@RequestMapping(value = "/getEngDataForLM", method = RequestMethod.POST)
	public List<Map<String, Object>> getEngDataForLM(@RequestBody UserCompetenceModel userCompetenceModel) {
		return competenceService.getEngDataForLM(userCompetenceModel);
	}

	@RequestMapping(value = "/downloadCompetenceData/{fileName}", method = RequestMethod.GET)
	public DownloadTemplateModel downloadCompetenceData(@PathVariable("fileName") String fileName)
			throws IOException, SQLException {
		return this.competenceService.downloadCompetenceData(fileName);
	}

	@RequestMapping(value = "/getAllDomain", method = RequestMethod.GET)
	public List<Map<String, Object>> getAllDomain() {

		LOG.info("getDomain: Success ");
		return competenceService.getAllDomain();
	}

	@RequestMapping(value = "/getAllTechnology", method = RequestMethod.GET)
	public List<Map<String, Object>> getAllTechnology() {

		LOG.info("getTechnology: Success ");
		return competenceService.getAllTechnology();

	}

	@RequestMapping(value = "/getAllVendor", method = RequestMethod.GET)
	public List<Map<String, Object>> getAllVendor() {

		LOG.info("getVendor: Success ");
		return competenceService.getAllVendor();

	}

	@RequestMapping(value = "/updateCompetenceAssesmentData", method = RequestMethod.POST)
	public Response<Void> updateCompetenceAssesmentData(@RequestBody UserCompetenceModel userCompetenceModel) {

		LOG.info("updateCompetenceAssesmentData: Success ");
		return competenceService.updateCompetenceAssesmentData(userCompetenceModel);
	}

}
