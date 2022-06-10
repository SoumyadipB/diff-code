package com.ericsson.isf.iva.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ericsson.isf.iva.model.IsfResponseModel;
import com.ericsson.isf.iva.model.ServerBotModel;
import com.ericsson.isf.iva.model.StepDetailsModel;
import com.ericsson.isf.iva.model.WorkFlowModel;
import com.ericsson.isf.iva.model.WorkOrderModel;
import com.ericsson.isf.iva.service.IvaManagementService;
import com.ericsson.isf.iva.model.BookingDetailsModel;


@RestController
@RequestMapping("/ivaManagement")
public class IvaManagementController {
	
	@Autowired
    private IvaManagementService ivaManagementService;
	
	/**
	 * this API validates data in serverBotModel and returns POST REST API response from ExternalInterface-ISF
	 * @param serverBotModel
	 * @return
	 * @author EHRMSNG
	 */
	@PostMapping("/startTask")
	public IsfResponseModel<Map<String, Object>> startTask(@RequestParam(value = "serverBotModel") String serverBotModel,
			@RequestHeader(value="signum") String signum){
		return ivaManagementService.startTask(serverBotModel, signum);
    }
	
	@PostMapping("/stopTask")
	public IsfResponseModel<Map<String, Object>> stopTask(@RequestBody ServerBotModel serverBotModel,
			@RequestHeader(value="signum") String signum){
		return ivaManagementService.stopTask(serverBotModel, signum);
    }

	@PostMapping("/updateBookingDetailsStatus")
	public IsfResponseModel<Map<String, Object>> updateBookingDetailsStatus(@RequestBody ServerBotModel serverBotModel,
			@RequestHeader(value="signum") String signum){
        return  ivaManagementService.updateBookingDetailsStatus(serverBotModel, signum);
	}
	
	@PostMapping("/addStepDetailsForFlowChart")
	public IsfResponseModel<Void> addStepDetailsForFlowChart(@RequestBody StepDetailsModel stepDetailsModel,
			@RequestHeader(value="signum") String signum){
		return ivaManagementService.addStepDetailsForFlowChart(stepDetailsModel, signum);
    }
	
	@PostMapping("/checkParallelWorkOrderDetails")
	public IsfResponseModel<Map<String,Object>> checkParallelWorkOrderDetails(@RequestBody ServerBotModel serverBotModel,
			@RequestHeader(value="signum") String signum){
		return ivaManagementService.checkParallelWorkOrderDetails(serverBotModel, signum);
    }
	
    @GetMapping("/getWorkOrders")
    public IsfResponseModel<Map<String, Object>> getWorkOrders(@RequestBody WorkOrderModel workOrderModel
    		,@RequestHeader(value="signum") String signum) {
		return ivaManagementService.getWorkOrders(workOrderModel, signum);
    }
    
	@GetMapping("/testIVA")
    public String testIVA() {
		return "ISF Voice Assistant is Up Now!";
    }
	
	@GetMapping("/getWOWorkFlow/{wOID}/{isQualified}")
	public IsfResponseModel<List<WorkFlowModel>> getWOWorkFlow(@PathVariable("wOID") int wOID,
			@PathVariable("isQualified") boolean isQualified,
			@RequestHeader(value="signum") String signum) throws Exception{
		return ivaManagementService.getWOWorkFlow(wOID, isQualified, signum);
	}
	
	@GetMapping("/getInprogressTask")
    public IsfResponseModel<List<LinkedHashMap<String, Object>>> getInprogressTask(@RequestHeader(value="signum") String signum) {
		return ivaManagementService.getInprogressTask(signum);
    }


	@GetMapping(value = "/getBookingDetailsByBookingId/{bookingId}")
	public IsfResponseModel<BookingDetailsModel> getBookingDetailsByBookingId(@PathVariable("bookingId") int bookingId,
			@RequestHeader(value="signum") String signum) {
		return ivaManagementService.getBookingDetailsByBookingId(bookingId,signum);
	}
	
}
