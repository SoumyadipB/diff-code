package com.ericsson.isf.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ericsson.isf.mapper.AspMapper;
import com.ericsson.isf.model.AspAcceptRejectModel;
import com.ericsson.isf.model.AspExplorerModel;
import com.ericsson.isf.model.AspLoginModel;
import com.ericsson.isf.model.AspVendorModel;

/**
*
* @author esudbhu
*/

@Repository
public class AspManagementDao {
	
	 @Qualifier("sqlSession")
	    /*Create session from SQLSessionFactory */
	    @Autowired
	    private SqlSessionTemplate sqlSession;
	 

	public AspVendorModel getVendorDetailsByID(String vendorCode) {
		 AspMapper aspMapper= sqlSession.getMapper(AspMapper.class);
		return aspMapper.getVendorDetailsByID(vendorCode);
	}
	
	public String getAspSignumByEmail(String email) {
		 AspMapper aspMapper= sqlSession.getMapper(AspMapper.class);
		return aspMapper.getAspSignumByEmail(email);	
	}
	
	public int getFailedAttempts(String signum) {
		AspMapper aspMapper= sqlSession.getMapper(AspMapper.class);
		return aspMapper.getFailedAttempts(signum);
	}
	
	public void updateFailedAttempts(String signum,Integer failedAttempts) {
		AspMapper aspMapper= sqlSession.getMapper(AspMapper.class);
		aspMapper.updateFailedAttempts(signum, failedAttempts);
	}
	
	public int isResetRequired(String signum) {
		AspMapper aspMapper= sqlSession.getMapper(AspMapper.class);
		return aspMapper.isResetRequired(signum);
	}
	
	public void setResetRequired(Integer flag,String signum) {
		AspMapper aspMapper= sqlSession.getMapper(AspMapper.class);
		aspMapper.setResetRequired(flag, signum);
	}
	
	public int isProfileLocked(String signum) {
		AspMapper aspMapper= sqlSession.getMapper(AspMapper.class);
		return aspMapper.isProfileLocked(signum);
	}
	
	public void setProfileLocked(Integer flag,String signum) {
		AspMapper aspMapper= sqlSession.getMapper(AspMapper.class);
		aspMapper.setProfileLocked(flag, signum);
	}
	
	public int isProfileActive(String signum) {
		AspMapper aspMapper= sqlSession.getMapper(AspMapper.class);
		return aspMapper.isProfileActive(signum);
	}
	
	public void setProfileActive(Integer flag,String signum) {
		AspMapper aspMapper= sqlSession.getMapper(AspMapper.class);
		aspMapper.setProfileActive(flag, signum);
	}
	
	public Map<String,Date> isExpired(String signum){
		AspMapper aspMapper= sqlSession.getMapper(AspMapper.class);
		return aspMapper.isExpired(signum);
	}
	
	public List<AspExplorerModel> getAspExplorerForManager(String managerSignum) {
		AspMapper aspMapper= sqlSession.getMapper(AspMapper.class);
		return aspMapper.getAspExplorerForManager(managerSignum);
	}
	
	public String getVendorByEmail(String email){
		AspMapper aspMapper= sqlSession.getMapper(AspMapper.class);
		return aspMapper.getVendorByEmail(email);
	}
	
	public void insertUserAccessAsp(String signum) {
		AspMapper aspMapper= sqlSession.getMapper(AspMapper.class);
		aspMapper.insertUserAccessAsp(signum);
	}
	
	public boolean insertAspUserAccessProfile(String signum,int profileId) {
		AspMapper aspMapper= sqlSession.getMapper(AspMapper.class);
		return aspMapper.insertAspUserAccessProfile(signum,profileId);
	}
	
	public int getAspProfileIdByName(String name) {
		AspMapper aspMapper= sqlSession.getMapper(AspMapper.class);
		return aspMapper.getAspProfileIdByName(name);
	}
	
	public void updateAspProfileAccess(AspAcceptRejectModel aspAcceptRejectModel) {
		AspMapper aspMapper= sqlSession.getMapper(AspMapper.class);
		aspMapper.updateAspProfileAccess(aspAcceptRejectModel);
	}
	public AspExplorerModel getAspFlags(String signum){
		AspMapper aspMapper= sqlSession.getMapper(AspMapper.class);
		return aspMapper.getAspFlags(signum);
	}
	public void resetFlagsWhenForgetPassword(String signum) {
		AspMapper aspMapper= sqlSession.getMapper(AspMapper.class);
		aspMapper.resetFlagsWhenForgetPassword(signum);
	}
	public AspLoginModel getAspDetailsBySignum(String signum){
		AspMapper aspMapper= sqlSession.getMapper(AspMapper.class);
		return aspMapper.getAspDetailsBySignum(signum);
	}
	public List<AspVendorModel> getAllVendors(){
		AspMapper aspMapper = sqlSession.getMapper(AspMapper.class);
		return aspMapper.getAllVendors();
	}
	public List<Map<String,Object>> getAspByVendor(String vendorCode){
		AspMapper aspMapper = sqlSession.getMapper(AspMapper.class);
		return aspMapper.getAspByVendor(vendorCode);
	}
	public void updateAspLastPasswordChange(String signum){
		AspMapper aspMapper = sqlSession.getMapper(AspMapper.class);
		aspMapper.updateAspLastPasswordChange(signum);
	}
	public String getVendorByScope(String projectScopeId){
		AspMapper aspMapper = sqlSession.getMapper(AspMapper.class);
		return aspMapper.getVendorByScope(projectScopeId);
	}
	
	public String getVendorByWoid(String woId){
		AspMapper aspMapper = sqlSession.getMapper(AspMapper.class);
		return aspMapper.getVendorByWoid(woId);
	}
	
	public boolean updateStatusInEmployeeTable(String signum,String status){
		AspMapper aspMapper = sqlSession.getMapper(AspMapper.class);
		return aspMapper.updateStatusInEmployeeTable(signum,status);
	}

	public List<AspVendorModel> getAllAspVendorDetails() {
		AspMapper aspMapper = sqlSession.getMapper(AspMapper.class);
		return aspMapper.getAllAspVendorDetails();
	}

	public void insertAspVendorDetails(AspVendorModel input) {
		AspMapper aspMapper = sqlSession.getMapper(AspMapper.class);
		aspMapper.insertAspVendorDetails(input);
	}

	public void updateAspVendorDetails(AspVendorModel input) {
		AspMapper aspMapper = sqlSession.getMapper(AspMapper.class);
		aspMapper.updateAspVendorDetails(input);
		
	}
	
	public AspVendorModel getAllAspVendorModelByCode(String vendorCode) {
		AspMapper aspMapper = sqlSession.getMapper(AspMapper.class);
		return aspMapper.getAllAspVendorModelByCode(vendorCode);
	}

	public void enableDisableAspVendorDetails(String vendorCode, String signum, int i) {
		AspMapper aspMapper = sqlSession.getMapper(AspMapper.class);
		aspMapper.enableDisableAspVendorDetails(vendorCode,signum,i);		
	}

	public List<AspVendorModel> getAllActiveAspVendors() {
		AspMapper aspMapper = sqlSession.getMapper(AspMapper.class);
		return aspMapper.getAllActiveAspVendors();
	}

	public boolean isSignumASPM(String signum) {
		AspMapper aspMapper = sqlSession.getMapper(AspMapper.class);
		return aspMapper.isSignumASPM(signum);
	}

}
