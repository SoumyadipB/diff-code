/**
* This NetworkElement controller used to add new api of network element.
* @author ekarmuj
* @since   2021-06-18 
*/

package com.ericsson.isf.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ericsson.isf.model.DataTableRequest;
import com.ericsson.isf.model.DownloadTemplateModel;
import com.ericsson.isf.model.MqttModel;
import com.ericsson.isf.model.NetworkElementDataTable;
import com.ericsson.isf.model.NetworkElementFilterModel;
import com.ericsson.isf.model.NetworkElemntViewModel;
import com.ericsson.isf.model.NodeFilterModel;
import com.ericsson.isf.model.NetworkElementCountModel;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.model.WorkOrderNetworkElementModel;
import com.ericsson.isf.service.NetworkElementService;
import com.ericsson.isf.service.NetworkElements;



@RestController
@RequestMapping("/networkElement")
public class NetworkElementController {

	private static final Logger LOG = LoggerFactory.getLogger(NetworkElementController.class);

	@Autowired /* Bind to bean/pojo */
	private NetworkElementService networkElementService;

	/**
	 * 
	 * @param request
	 * @param projectID
	 * @return
	 */
	@PostMapping(value = "/getNetworkElementDetailsv1/{projectID}")
	public ResponseEntity<Response<NetworkElements>> getNetworkElementDetails(HttpServletRequest request,
			@PathVariable("projectID") int projectID,@RequestParam(value="recordsTotal") int recordsTotal) {

		LOG.info("/getNetworkElementDetailsv1:Success");
		return networkElementService.getNetworkElementDetailsv1(projectID, new DataTableRequest(request), recordsTotal);

	}

	
	
	/**
	 * This API is not in Use
	 * @param request
	 * @param projectID
	 * @return
	 */
	@RequestMapping(value = "/getNetworkElementDetailsv2/{projectID}", method = RequestMethod.POST)
	public ResponseEntity<Response<NetworkElements>> getNetworkElementDetailsV(HttpServletRequest request,
			@PathVariable("projectID") int projectID) {

		LOG.info("/getNetworkElementDetailsv1:Success");
		return networkElementService.getNetworkElementDetailsv2(projectID, new DataTableRequest(request));

	}

	/**
	 * 
	 * @param request
	 * @param projectID
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws SQLException
	 */
	@RequestMapping(value = "/downloadNetworkElementv1/{projectID}", method = RequestMethod.GET)
	public DownloadTemplateModel downloadNetworkElement(HttpServletRequest request,
			@PathVariable("projectID") int projectID, HttpServletResponse response) throws IOException, SQLException {

		LOG.info("/downloadNetworkElementv1:Success");
		return networkElementService.downloadNetworkElementv1(projectID, response);
	}
	
	
	/**
	 * @purpose: This API is used for Downloading the Reference Mapping File From Network Element Tab on UI.
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws SQLException
	 */
	@GetMapping(value = "/downloadReferenceMappingFile")
	public DownloadTemplateModel downloadReferenceMappingFile(HttpServletRequest request,
			 HttpServletResponse response) throws IOException {

		LOG.info("/downloadReferenceMappingFile:Success");
		return networkElementService.downloadReferenceMappingFile();
	}
	
	/**
	 * Api name: networkElement/checkStatusofNEUploadForUser
	 * @purpose: This API is used for Checking if there is any other 
	 * NE Upload is InProgress for same CountryCustomerGroup or not.
	 * @param projectID
	 * @return ResponseEntity<Response<String>>
	 */
	
	@GetMapping(value = "/checkStatusofNEUploadForUser")
	public  ResponseEntity<Response<String>> checkStatusofNEUploadForUser(
			@RequestParam("projectID") int projectID
			){
		LOG.info("checkStatusofNEUploadForUser : SUCCESS ");
		return networkElementService.checkStatusofNEUploadForUser(projectID);
	}

	/**
	 * Api name: networkElement/getUploadedFileStatus
	 * Description:  This Api used to get uploaded file status in network element to check if some other 
	 * user already upload network element of same countryCustomerGroupId.
	 * @param projectID
	 * @return ResponseEntity<Response<MqttModel>>
	 */
	
	@GetMapping(value = "/getUploadedFileStatus")
	public ResponseEntity<Response<MqttModel>> getUploadedFileStatus(@RequestParam(value="referenceId", required=true) int referenceId){
		return networkElementService.getUploadedFileStatus(referenceId);
	}
	
	
	/**
	 * Api name: networkElement/validateNetworkElementFile/{projectID}/{uploadedON}/{uploadedBy}
	 * @purpose: This API is Used for Validating Network Element File and Uploading the Success and Failure Link at Storage.
	 * @param ProjectID
	 * @param file
	 * @param uploadedON
	 * @param uploadedBy
	 * @return ResponseEntity<Response<String>>
	 * @throws IOException
	 * @throws SQLException
	 */
	 @PostMapping(value = "/validateNetworkElementFile/{projectID}/{uploadedON}/{uploadedBy}", consumes = "multipart/form-data")
	    public ResponseEntity<Response<String>> validateNetworkElementFile(
	    		@PathVariable("projectID") int projectID,@PathVariable("uploadedON") String uploadedON,@PathVariable("uploadedBy") String uploadedBy,
	            @RequestPart("file") MultipartFile file)  {
	        
		    return  networkElementService.validateNetworkElementFile(projectID, uploadedON, uploadedBy, file);
	        
			
	    }
	 
	/**
	 * Api name: networkElement/saveDeleteNeUploadFinalData
	 * Description: This Api
	 * used to save and delete data from temp table to master table or delete temp
	 * table based on status.
	 * @author ekarmuj
	 * @param projectID
	 * @return ResponseEntity<Response<String>>
	 */

	@PostMapping(value = "/saveDeleteNeUploadFinalData")
	public ResponseEntity<Response<String>> saveDeleteNeUploadFinalData(@RequestParam(value="signum", required=true) String signum,
			@RequestParam(value="projectID", required=true) int projectID,@RequestParam(value="status", required=true) String status,
			@RequestParam(value="neuploadId", required=true) int neuploadId) {
		return networkElementService.saveDeleteNeUploadFinalData(signum,projectID,status,neuploadId);
	}
	
	/**
	 * API Name: networkElement/getNetworkElementNameByTerm Description: This API is
	 * used to Fetch Network Element on 4 letter search with tech and domain
	 * 
	 * @param nodeFilterModel
	 * @return ResponseEntity<Response<List<String>>>
	 */
	@PostMapping(value = "/getNetworkElementNameByTerm")
	public ResponseEntity<Response<List<String>>> getNetworkElementNameByTerm(
			@RequestBody NodeFilterModel nodeFilterModel) {
		return networkElementService.getNetworkElementNameByTerm(nodeFilterModel);
	}
	
	/**
	 * API Name: networkElement/viewNetworkElementDetails
	 * @purpose: This API is used for get Details of Network Element from Temp table with Data Table pagination.
	 * @param request
	 * @param tablename
	 * @return ResponseEntity<Response<NetworkElementDataTable>>
	 */
	
	@PostMapping(value = "/viewNetworkElementDetails")
	public ResponseEntity<Response<NetworkElementDataTable>> viewNetworkElementDetails(HttpServletRequest request,
			@RequestParam("tablename") String tablename,
			@RequestParam("status") String status) {

		return networkElementService.viewNetworkElementDetails(tablename, status, new DataTableRequest(request));

	}
	
	/**
	 * API Name: networkElement/validateCommaSeparatedNetworkElementData
	 * @param nodeFilterModel
	 * @param signum
	 * @return ResponseEntity<Response<String>>
	 */
	
	@PostMapping(value = "/validateCommaSeparatedNetworkElementData")
	public ResponseEntity<Response<String>> validateCommaSeparatedNetworkElementData(
			@RequestBody NetworkElementFilterModel networkElementFilterModel, @RequestHeader("signum") String signum) {
		return networkElementService.validateCommaSeparatedNetworkElementData(networkElementFilterModel, signum);
	}
	


	/**
	 * 
	 * @param networkElemntViewModel
	 * @return
	 */
	@PostMapping(value = "/updateRadioSelectionForNE")
	public ResponseEntity<Response<Void>> updateRadioSelectionForNE(
			@RequestBody NetworkElemntViewModel networkElemntViewModel) {
		return networkElementService.updateRadioSelectionForNE(networkElemntViewModel);
	}
	
	/**
	 * 
	 * @param tablename
	 * @return ResponseEntity<Response<String>>
	 */
	@GetMapping(value = "/checkNetworkElementType")
	public ResponseEntity<Response<WorkOrderNetworkElementModel>> checkNetworkElementType(
			@RequestParam("tablename") String tablename) {
		return networkElementService.checkNetworkElementType(tablename);
	}
	
	/**
	 * 
	 * @param tablename
	 * @return ResponseEntity<Response<Void>>
	 */
	@PostMapping(value = "/deleteTempTableForNE")
	public ResponseEntity<Response<Void>> deleteTempTableForNE(
			@RequestParam("tablename") String tablename) {
		return networkElementService.deleteTempTableForNE(tablename);
	}
	
	/**
	 * Api Name: networkElement/downloadNeData
	 * purpose: This Api used to download network element data based on status like group,valid and Invalid.
	 * @param request
	 * @param status
	 * @param response
	 * @return DownloadTemplateModel
	 * @throws IOException
	 * @throws SQLException
	 * @author ekarmuj
	 */
	
	@RequestMapping(value = "/downloadNeData", method = RequestMethod.GET)
	public DownloadTemplateModel downloadNeDataOnStatus(
			@RequestParam("status") String status,@RequestParam("tablename") String tablename) throws IOException, SQLException {

		LOG.info("/downloadNeData:Success");
		return networkElementService.downloadNeDataOnStatus(status,tablename);
	}
	
	/**
	 * API Name: networkElement/getTotalNECount
	 * 
	 * @author edixshu
	 * @purpose To get the count of NE.
	 */
	@PostMapping(value = "/getTotalNECount")
	public ResponseEntity<Response<Long>> getTotalNetworkElementCount(@RequestBody NetworkElementCountModel networkElementCountModel) {
		 return networkElementService.getTotalNetworkElementCount(networkElementCountModel);
	}
	
	
	/**
	 * API Name: networkElement/resetSelectedRecordsForNE
	 * 
	 * @author elkpain
	 * @purpose Resets Selected Records For NE.
	 * @return Void
	 * @throws IOException
	 */
	@PostMapping(value = "/resetSelectedRecordsForNE")
	public ResponseEntity<Response<Void>> resetSelectedRecordsForNE(
			@RequestBody NetworkElemntViewModel networkElemntViewModel) {
		
		LOG.info("/resetSelectedRecordsForNE:Success");
		return networkElementService.resetSelectedRecordsForNE(networkElemntViewModel);
	}

}
