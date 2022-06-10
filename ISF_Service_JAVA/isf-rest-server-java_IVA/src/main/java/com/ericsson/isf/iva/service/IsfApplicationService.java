package com.ericsson.isf.iva.service;


import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ericsson.isf.iva.model.BookingDetailsModel;
import com.ericsson.isf.iva.model.IsfResponseModel;
import com.ericsson.isf.iva.model.ServerBotModel;
import com.ericsson.isf.iva.model.StepDetailsModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IsfApplicationService {

	@Headers("apiName: /ivaManagement/startTask")
	@POST("externalInterface/startTask")
	Call<IsfResponseModel<Map<String, Object>>> startTask(@Body ServerBotModel serverBotModel );
	
	@Headers("apiName: /ivaManagement/stopTask")
	@POST("externalInterface/stopTask")
	Call<IsfResponseModel<Map<String, Object>>> stopTask(@Body ServerBotModel serverBotModel );
	
	@Headers("apiName: /ivaManagement/updateBookingDetailsStatus")
	@POST("externalInterface/updateBookingDetailsStatus/{wOID}/{signumID}/{taskID}/{bookingID}/{status}/{reason}/{stepid}/{flowChartDefID}/{refferer}")
	Call<IsfResponseModel<Map<String, Object>>> updateBookingDetailsStatus(@Path("wOID") int wOID,
			@Path("signumID") String signumID, @Path("taskID") int taskID,
			@Path("bookingID") int bookingID, @Path("status") String status,
			@Path("reason") String reason, @Path("stepid") String stepid,
			@Path("flowChartDefID") int flowChartDefID, @Path("refferer") String refferer);
	
	@Headers("apiName: /ivaManagement/addStepDetailsForFlowChart")
	@POST("externalInterface/addStepDetailsForFlowChart")
	Call<IsfResponseModel<Void>> addStepDetailsForFlowChart(@Body StepDetailsModel stepDetailsModel );
	
	@Headers("apiName: /ivaManagement/checkParallelWorkOrderDetails")
	@GET("externalInterface/checkParallelWorkOrderDetails/{signumID}/{isApproved}/{executionType}/{woid}/{taskid}/"
			+ "{projectID}/{versionNO}/{subActivityFlowChartDefID}/{stepID}")
	Call<Map<String, Object>> checkParallelWorkOrderDetails(@Path("signumID") String signumID,
			@Path("isApproved")  boolean isApproved, @Path("executionType") String executionType,
			@Path("woid") int woid,@Path("taskid") int taskid,
			@Path("projectID") int projectID,@Path("versionNO") int versionNO,@Path("subActivityFlowChartDefID") int subActivityFlowChartDefID,
			@Path("stepID") String stepID);
	
	@GET("woExecution/getWorkOrders")
	Call<Map<String, Object>> getWorkOrders(@Query("signumID") String signumID, @Query("status") String status,
			@Query("startDate") String startDate,@Query("endDate") String endDate);
	
	@GET("woManagement/getInprogressTask/{signum}")
	Call<IsfResponseModel<List<LinkedHashMap<String, Object>>>> getInprogressTask(@Path("signum") String signum);
	
	@GET("externalInterface/getBookingDetailsByBookingId/{bookingId}")
	Call<IsfResponseModel<BookingDetailsModel>> getBookingDetailsByBookingId(@Path("bookingId") int bookingId);
}
