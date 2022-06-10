package com.ericsson.isf.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ericsson.isf.model.AspAcceptRejectModel;
import com.ericsson.isf.model.AspExplorerModel;
import com.ericsson.isf.model.AspLoginModel;
import com.ericsson.isf.model.AspVendorModel;

/**
*
* @author esudbhu
*/

public interface AspMapper {

	AspVendorModel getVendorDetailsByID(@Param("vendorCode") String vendorCode);
	
	public int getFailedAttempts(@Param("signum") String signum);
	
	public void updateFailedAttempts(@Param("signum") String signum,@Param("failedAttempts")Integer failedAttempts);
	
	public int isResetRequired(@Param("signum") String signum);
	
	public void setResetRequired(@Param("flag") Integer flag,@Param("signum") String signum);
	
	public int isProfileLocked(@Param("signum") String signum);
	
	public void setProfileLocked(@Param("flag") Integer flag,@Param("signum") String signum);
	
	public int isProfileActive(@Param("signum") String signum);
	
	public void setProfileActive(@Param("flag") Integer flag,@Param("signum") String signum);
	
	public Map<String,Date> isExpired(@Param("signum") String signum);
	
	public List<AspExplorerModel> getAspExplorerForManager(@Param("managerSignum") String managerSignum);

	public String getVendorByEmail(@Param("email") String email);

	public void insertUserAccessAsp(@Param("signum") String signum);
	
	public String getAspSignumByEmail(@Param("email") String email);
	
	public void updateAspProfileAccess(@Param("aspAcceptRejectModel") AspAcceptRejectModel aspAcceptRejectModel);
	
	public int getAspProfileIdByName(@Param("name") String name);
	
	public boolean insertAspUserAccessProfile(@Param("signum") String signum, @Param("profileID") int profileID);

	public AspExplorerModel getAspFlags(String signum);
	
	public void resetFlagsWhenForgetPassword(String signum); 
	
	public AspLoginModel getAspDetailsBySignum(@Param("signum") String signum);
	
	public List<AspVendorModel> getAllVendors();
	
	public List<Map<String,Object>> getAspByVendor(@Param("vendorCode") String vendorCode);
	
	public void updateAspLastPasswordChange(@Param("signum") String signum);
	
	public String getVendorByScope(@Param("projectScopeId") String vendorCode);
	
	public String getVendorByWoid(@Param("woid") String vendorCode);

	public boolean updateStatusInEmployeeTable(@Param("signum") String signum,@Param("status") String status);

	public List<AspVendorModel> getAllAspVendorDetails();

	public void insertAspVendorDetails(@Param("input") AspVendorModel input);

	public void updateAspVendorDetails(@Param("input") AspVendorModel input);

	void enableDisableAspVendorDetails(@Param("vendorCode") String vendorCode, @Param("signum") String signum, @Param("i") int i);

	public AspVendorModel getAllAspVendorModelByCode(@Param("vendorCode") String vendorCode);

	public List<AspVendorModel> getAllActiveAspVendors();

	boolean isSignumASPM(@Param("signum")String signum);

}
