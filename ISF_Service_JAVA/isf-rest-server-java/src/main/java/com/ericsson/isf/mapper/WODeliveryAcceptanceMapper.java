/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.mapper;

import com.ericsson.isf.model.ClosedWODetailsModel;
import com.ericsson.isf.model.ReasonFromWOModel;
import com.ericsson.isf.model.WORelatedDetails;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @author esanpup
 */
public interface WODeliveryAcceptanceMapper {

    public List<String> getAcceptanceRatings();

    public void acceptWorkOrder(@Param("woID") Integer woID, 
                                @Param("rating") String rating,
                                @Param("comment") String comment,
                                @Param("acceptedOrRejectedBy") String acceptedOrRejectedBy, 
                                @Param("lastModifiedBy") String lastModifiedBy);
	
    public void sendWorkOrderForAcceptance(@Param("woID") Integer woID, 
                                @Param("accOrRejBy") String accOrRejBy, 
                                @Param("comment") String comment,
                                @Param("lastModifiedBy") String lastModifiedBy);
    
    public int rejectWorkOrder(@Param("woID") Integer woID,
                                @Param("acceptance") String acceptance,
                                @Param("reason") String reason,
                                @Param("comment") String comment,
                                @Param("accOrRejBy") String accOrRejBy,
                                @Param("lastModifiedBy") String lastModifiedBy);
    
    public List<ClosedWODetailsModel> getclosedWODetails(@Param("projectID") Integer projectID, 
                                @Param("projectName") String projectName,
                                @Param("signumID") String signumID,@Param("role") String role);

    public List<ReasonFromWOModel> getReasonFromWO(@Param("wOID") Integer wOID);

    public WORelatedDetails getStatusReasons(@Param("woID") int woID);

    public String getWOStatus(@Param("woID") int woID);

    public String getDelieveryAcceptance(@Param("woID") int woID);

//    public WORelatedDetails getReopenedReasons(@Param("pWOID") int pWOID);

    public int getWOForParentWOID(@Param("woID") int woID);

	public List<ClosedWODetailsModel> getacceptedWODetails(@Param("projectID") Integer projectID, 
                                @Param("projectName") String projectName,
                                @Param("signumID") String signumID,@Param("role") String role);

//    public List<String> getStandardFailureReasons(@Param("failureStatus") String failureStatus);
    
}
