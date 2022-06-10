package com.ericsson.isf.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ericsson.isf.exception.ApplicationExceptionHandler;
import com.ericsson.isf.mapper.ResourceRequestMapper;
import com.ericsson.isf.model.CompetenceSubModel;
import com.ericsson.isf.model.DataTableRequest;
import com.ericsson.isf.model.LeavePlanModel;
import com.ericsson.isf.model.ResoucePositionFmModel;
import com.ericsson.isf.model.ResourceCalandarModel;
import com.ericsson.isf.model.ResourceEngagementModel;
import com.ericsson.isf.model.ResourceModel;
import com.ericsson.isf.model.ResourcePositionWorkEffortModel;
import com.ericsson.isf.model.ResourceRequestModel;
import com.ericsson.isf.model.SearchResourceByFilterModel;
import com.ericsson.isf.service.JdbcConnectionFactory;
import com.ericsson.isf.util.ResultSetUtil;

import net.sf.json.JSONArray;

@Repository
public class ResourceRequestDAO {
    
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationExceptionHandler.class);
	
	  @Qualifier("sqlSession")
	    /*Create session from SQLSessionFactory */
	    @Autowired
	    private SqlSessionTemplate sqlSession;
	  
	@Autowired
	private JdbcConnectionFactory jdbcConnectionFactory;

	    public List<HashMap<String, Object>> searchResourcesByFilter(SearchResourceByFilterModel searchResourceByFilterModel) {
	        ResourceRequestMapper resourceRequestMapper = sqlSession.getMapper(ResourceRequestMapper.class);
	        if(searchResourceByFilterModel.getSignum() !=null && !searchResourceByFilterModel.getSignum().equals(""))
	        	return resourceRequestMapper.searchResourcesByEmployeeManager(searchResourceByFilterModel);
	        else if(searchResourceByFilterModel.getManagerSignum() !=null && !searchResourceByFilterModel.getManagerSignum().equals(""))
	        	return resourceRequestMapper.searchResourcesByEmployeeManager(searchResourceByFilterModel);
            else{
            	if (searchResourceByFilterModel.getCompetenceString()==null ||searchResourceByFilterModel.getCompetenceString().length == 0){
            		searchResourceByFilterModel.setCompetenceString(null);
            	}
            	if (searchResourceByFilterModel.getCompetenceLevel()==null ||searchResourceByFilterModel.getCompetenceLevel().length == 0){
            		searchResourceByFilterModel.setCompetenceLevel(null);
            	}
            	return resourceRequestMapper.searchResourcesByFilter(searchResourceByFilterModel);
            }
	    }
	    
	    

	    public List<HashMap<String, Object>> searchResourcesByFilters(SearchResourceByFilterModel searchResourceByFilterModel) {
	    	ResourceRequestMapper resourceRequestMapper = sqlSession.getMapper(ResourceRequestMapper.class);
	    	return resourceRequestMapper.searchResourcesByFilters(searchResourceByFilterModel);
	    }

	    public List<LinkedHashMap<String, Object>> getResourceRequestsByFilter(Integer ProjectID,Integer DomainID,Integer SubDomainID, Integer SubServiceAreaID,Integer TechnologyID,String PositionStatus,String AllocatedResource, String spoc, String marketArea) {
	    	ResourceRequestMapper resourceRequestMapper = sqlSession.getMapper(ResourceRequestMapper.class);

		       return resourceRequestMapper.getResourceRequestsByFilter(ProjectID,DomainID,SubDomainID,SubServiceAreaID,TechnologyID,PositionStatus,AllocatedResource,spoc,marketArea);
	    }


	    public List<ResoucePositionFmModel> getPositionsAndAllocatedResources(Integer rrID, String positionStatus, String spoc, String marketArea, Integer projectID) {
	    	ResourceRequestMapper resourceRequestMapper = sqlSession.getMapper(ResourceRequestMapper.class);
		        return resourceRequestMapper.getPositionsAndAllocatedResources(rrID,positionStatus,spoc,marketArea, projectID);
	    }
	    
	    public List<ResoucePositionFmModel> getAllPositions(Integer rrID, String positionStatus, String spoc, String marketArea, Integer projectID) {
	    	ResourceRequestMapper resourceRequestMapper = sqlSession.getMapper(ResourceRequestMapper.class);
		        return resourceRequestMapper.getAllPositions(rrID,positionStatus,spoc,marketArea, projectID);
	    }
	    
	    ////getCertificateSubDetails
	    public List<HashMap<String,Object>> getCertificateSubDetails(Integer rrID){
	    	ResourceRequestMapper resourceRequestMapper = sqlSession.getMapper(ResourceRequestMapper.class);

	    	return resourceRequestMapper.getCertificateSubDetails(rrID);
	    }
	    //
	    public List<CompetenceSubModel> getCompetenceSubDetails(Integer rrID){
	    	ResourceRequestMapper resourceRequestMapper = sqlSession.getMapper(ResourceRequestMapper.class);

	    	return resourceRequestMapper.getCompetenceSubDetails(rrID);
	    }


	    public List<HashMap<String, Object>> getResourcePositionSubList(ResourcePositionWorkEffortModel rpef,int rrID){
	    	ResourceRequestMapper resourceRequestMapper = sqlSession.getMapper(ResourceRequestMapper.class);

	    	return resourceRequestMapper.getResourcePositionSubList(rpef,rrID);
	    }

	    public List<HashMap<String, Object>> getCompetenceLevel(Integer rrID){
	    	ResourceRequestMapper resourceRequestMapper = sqlSession.getMapper(ResourceRequestMapper.class);

	    	return resourceRequestMapper.getCompetenceLevel(rrID);
	    }

	    public List<ResourceRequestModel> getDemandRequestDetail(int rrID) {
	    	ResourceRequestMapper resourceRequestMapper = sqlSession.getMapper(ResourceRequestMapper.class);
	    	return resourceRequestMapper.getDemandRequestDetail(rrID);
	    }



	    public List<ResourcePositionWorkEffortModel> getResourceRequestWEffort(int rrID) {
	    	ResourceRequestMapper resourceRequestMapper = sqlSession.getMapper(ResourceRequestMapper.class);
	    	return resourceRequestMapper.getResourceRequestWEffort(rrID);
	    }
	    public List<ResourcePositionWorkEffortModel> getResourceRequestWEffortDetails(int rrID) {
	    	ResourceRequestMapper resourceRequestMapper = sqlSession.getMapper(ResourceRequestMapper.class);
	    	return resourceRequestMapper.getResourceRequestWEffort(rrID);
	    }

	    public List<ResourceEngagementModel> getWOResourceLevelDetails(String signumID, List<String> signumForProject, Integer projectID, String startDate, String endDate) {
	    	ResourceRequestMapper resourceRequestMapper = sqlSession.getMapper(ResourceRequestMapper.class);
	        return resourceRequestMapper.getWOResourceLevelDetails(signumID,signumForProject, projectID, startDate, endDate);
	        
	    }

	    public List<Map<String,Object>> getSignumsWorkingInProject(String signumID, Integer projectID, String startDate, String endDate, DataTableRequest dataTableReq,String term ) {

			ResourceRequestMapper resourceRequestMapper = sqlSession.getMapper(ResourceRequestMapper.class);
			return resourceRequestMapper.getSignumsWorkingInProject(signumID, projectID, startDate, endDate, dataTableReq,term);
		}

	    public JSONArray getResourceWOLevelDetails(String signumID) { 
	    	JSONArray resourceLevel1 = new JSONArray();
	    	String sqlQuery;
	    	CallableStatement cstmt = null;
	    	Connection con = jdbcConnectionFactory.getPooledConnection();
	    	try {
	    		sqlQuery = "{call user_sp_getResourceWODetails_ResourceWOLevel_V2(?)}";
	    		cstmt = con.prepareCall(sqlQuery);
	    		cstmt.setString(1, signumID);
	    		boolean results = cstmt.execute();
	    		int rowsAffected = 0;
	    		ResultSet rs = null;
	    		LOG.info("sqlQuery for user_sp_getResourceWODetails_ResourceWOLevel_V2(): " + sqlQuery);
	    		// Protects against lack of SET NOCOUNT in stored prodedure
	    		while (results || rowsAffected != -1) {
	    			if (results) {
	    				rs = cstmt.getResultSet();
	    				break;
	    			} else {
	    				rowsAffected = cstmt.getUpdateCount();
	    			}
	    			results = cstmt.getMoreResults();
	    		}
	    		if (rs != null) {
	    			resourceLevel1 = ResultSetUtil.convertToJSONTable(rs);
	    		}
	    		cstmt.close();
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    	}
	    	finally{
	    		jdbcConnectionFactory.attemptClose(con);
	    	}
	    	return resourceLevel1;
	    }

	    public boolean ModifyDemandRequest(ResourceModel resourceModel) {
	    	ResourceRequestMapper resourceRequestMapper = sqlSession.getMapper(ResourceRequestMapper.class);

	    	int resultResourseRequest= resourceRequestMapper.modifyWorkEffort(resourceModel);
	    	int resultWorkEffort= resourceRequestMapper.modifyDemandRequest(resourceModel.getResourceRequestID(),resourceModel.getJobRoleID(),resourceModel.getJobStageID(),resourceModel.getRemoteCount(),resourceModel.getOnsiteCount(),resourceModel.getRequestType(),resourceModel.getResourceType());

	    	if(resultWorkEffort==0 && resultResourseRequest==0)
	    		return false;
	    	else if(resultWorkEffort==1 && resultResourseRequest==1)
	    		return true;
	    	else 
	    		return false;
	    }


  	public List<Map<String, Object>> getBookedResourceBySignum(String signum,Date startDate,Date endDate) {
  	  ResourceRequestMapper resourceRequestMapper = sqlSession.getMapper(ResourceRequestMapper.class);
	      
  	  return resourceRequestMapper.getBookedResourceBySignum(signum,startDate,endDate);
	}
  	
	public List<ResourceCalandarModel> getResourceCalander(String signum, String startdate, String enddate) {
		ResourceRequestMapper resourceRequestMapper = sqlSession.getMapper(ResourceRequestMapper.class);

		return resourceRequestMapper.getResourceCalander(signum, startdate, enddate);
	}

  	public List<Map<String, Object>> getJobStages() {
  	  ResourceRequestMapper resourceRequestMapper = sqlSession.getMapper(ResourceRequestMapper.class);
	      
  	  return resourceRequestMapper.getJobStages();
	}
  	public List<Map<String, Object>> getBacklogWorkOrders(String signum) {
    	  ResourceRequestMapper resourceRequestMapper = sqlSession.getMapper(ResourceRequestMapper.class);
  	      
    	  return resourceRequestMapper.getBacklogWorkOrders(signum);
  	}
  	
  	public Map<String, Object> getDomainDetailsByPorjectScopeId(String projectScopeDetailID) {
  	  ResourceRequestMapper resourceRequestMapper = sqlSession.getMapper(ResourceRequestMapper.class);
	      
  	  return resourceRequestMapper.getDomainDetailsByPorjectScopeId(projectScopeDetailID);
	}



  	public List<Map<String, Object>> getAvgLoeForWoID( String signumID, Integer projectID, String tsStartDate, String tsEndDate) {
		ResourceRequestMapper resourceRequestMapper = sqlSession.getMapper(ResourceRequestMapper.class);
		
		return resourceRequestMapper.getAvgLoeForWoID( signumID, projectID,  tsStartDate,  tsEndDate);
	}



	public int getFlowchartdefID(int woID) {
		ResourceRequestMapper resourceRequestMapper = sqlSession.getMapper(ResourceRequestMapper.class);

		return resourceRequestMapper.getFlowchartdefID(woID);
	}



	public List<Map<String, Object>> getBacklogWorkOrdersForProject(Integer projectID) {
		ResourceRequestMapper resourceRequestMapper = sqlSession.getMapper(ResourceRequestMapper.class);
		return resourceRequestMapper.getBacklogWorkOrdersForProject(projectID);
	}
	
	public List<Map<String, Object>> getAllPositionsCount(Integer projectID) {
		ResourceRequestMapper resourceRequestMapper = sqlSession.getMapper(ResourceRequestMapper.class);
		return resourceRequestMapper.getAllPositionsCount(projectID);
	}



	public Map<String, Object> getSubServiceAreaPCode(int serviceAreaID) {
		ResourceRequestMapper resourceRequestMapper = sqlSession.getMapper(ResourceRequestMapper.class);
		return resourceRequestMapper.getSubServiceAreaPCode(serviceAreaID);
	}



	public List<Map<String, Object>> getJobRoles() {
		ResourceRequestMapper resourceRequestMapper = sqlSession.getMapper(ResourceRequestMapper.class);
		return resourceRequestMapper.getJobRoles();
	}



	public List<Map<String, Object>> getAllCertifications(String Issuer) {
		ResourceRequestMapper resourceRequestMapper = sqlSession.getMapper(ResourceRequestMapper.class);
		return resourceRequestMapper.getAllCertifications(Issuer);
	}



	public List<Map<String, Object>> getUniqueIssuer() {
		ResourceRequestMapper resourceRequestMapper = sqlSession.getMapper(ResourceRequestMapper.class);
		return resourceRequestMapper.getUniqueIssuer();
	}



	public List<Map<String, Object>> getOnsiteLocations() {
		ResourceRequestMapper resourceRequestMapper = sqlSession.getMapper(ResourceRequestMapper.class);
		return resourceRequestMapper.getOnsiteLocations();
	}



	public List<Map<String, Object>> getPositionStatus() {
		ResourceRequestMapper resourceRequestMapper = sqlSession.getMapper(ResourceRequestMapper.class);
		return resourceRequestMapper.getPositionStatus();
	}



	public List<Map<String, Object>> getFilteredCompetences(String competenceString) {
		ResourceRequestMapper resourceRequestMapper = sqlSession.getMapper(ResourceRequestMapper.class);
		return resourceRequestMapper.getFilteredCompetences(competenceString);
	}



	public List<Map<String, Object>> getBacklogWorkOrdersForProjectWithSignum(Integer projectID, List<String> signumForProject) {
		ResourceRequestMapper resourceRequestMapper = sqlSession.getMapper(ResourceRequestMapper.class);
		return resourceRequestMapper.getBacklogWorkOrdersForProjectWithSignum(projectID,signumForProject);
	}
	
	public  List<Map<String, Object>> getSignumsFilteredForEngEngagement(Integer projectID, String startDate, String endDate, String term) {
		int pageSize = 50;
		int pageNo = 0;
		RowBounds rowBounds = new RowBounds(pageNo * pageSize, pageSize);
		ResourceRequestMapper resourceRequestMapper = sqlSession.getMapper(ResourceRequestMapper.class);
		return resourceRequestMapper.getSignumsFilteredForEngEngagement(projectID,startDate,endDate,term,rowBounds);
	}

	public List<Map<String,Object>> getAllSignumForProject(String term, Integer projectID, String startDate, String endDate) {
		ResourceRequestMapper resourceRequestMapper = sqlSession.getMapper(ResourceRequestMapper.class);
		return resourceRequestMapper.getAllSignumForProject(term, projectID, startDate, endDate);
	}
	
	public List<ResourceEngagementModel> getWOResourceLevelDetailsX(String signumID, Integer projectID, String startDate, String endDate) {
    	ResourceRequestMapper resourceRequestMapper = sqlSession.getMapper(ResourceRequestMapper.class);
        return resourceRequestMapper.getWOResourceLevelDetailsX(signumID, projectID, startDate, endDate);
        
    }
	
	public List<Map<String, Object>> getSignumsWorkingInProjectSecond(String signumID, Integer projectID,
			String startDate, String endDate) {
		ResourceRequestMapper resourceRequestMapper = sqlSession.getMapper(ResourceRequestMapper.class);
        return resourceRequestMapper.getSignumsWorkingInProjectSecond(signumID, projectID, startDate, endDate);
	}



	public List<Map<String, Object>> getEmployeesInProject(Integer projectID, String startDate, String endDate,
			DataTableRequest dataTableReq, String term) {
		ResourceRequestMapper resourceRequestMapper = sqlSession.getMapper(ResourceRequestMapper.class);
        return resourceRequestMapper.getEmployeesInProject(projectID, startDate, endDate,dataTableReq, term);
	}



	public List<Map<String, Object>> getBacklogWOCounts(Integer projectID, List<String> signums) {
		ResourceRequestMapper resourceRequestMapper = sqlSession.getMapper(ResourceRequestMapper.class);
        return resourceRequestMapper.getBacklogWOCounts(projectID, signums);
	}



	public List<Map<String, Object>> getClosedWOCounts(Integer projectID, String signum, String startDate,String endDate) {
		ResourceRequestMapper resourceRequestMapper = sqlSession.getMapper(ResourceRequestMapper.class);
        return resourceRequestMapper.getClosedWOCounts(projectID, signum, startDate,endDate);
	}

	public List<Map<String, Object>> getManualHours(Integer projectID, String signumID, String date) {
		ResourceRequestMapper resourceRequestMapper = sqlSession.getMapper(ResourceRequestMapper.class);
        return resourceRequestMapper.getManualHours(projectID, signumID, date);
	}
	
	public List<Map<String, Object>> getAutomaticHours(Integer projectID, String signumID, String date) {
		ResourceRequestMapper resourceRequestMapper = sqlSession.getMapper(ResourceRequestMapper.class);
        return resourceRequestMapper.getAutomaticHours(projectID, signumID, date);
        		}
        
    public List<Map<String, Object>> getPlannedAssignedWOCount(Integer projectID, String signum, String date) {
    		ResourceRequestMapper resourceRequestMapper = sqlSession.getMapper(ResourceRequestMapper.class);
            return resourceRequestMapper.getPlannedAssignedWOCount(projectID, signum, date);
        }
            
    public List<Map<String, Object>> getInProgressWOcount(Integer projectID, String signum, String date) {
        		ResourceRequestMapper resourceRequestMapper = sqlSession.getMapper(ResourceRequestMapper.class);
                return resourceRequestMapper.getInProgressWOcount(projectID, signum, date);
            }
                
    public List<Map<String, Object>> getProjectAdhocHours(Integer projectID, String signum, String date) {
            		ResourceRequestMapper resourceRequestMapper = sqlSession.getMapper(ResourceRequestMapper.class);
                    return resourceRequestMapper.getProjectAdhocHours(projectID, signum, date);
                }
                
     public List<Map<String, Object>> getInternalAdhocHours(Integer projectID, String signum, String date) {
            		ResourceRequestMapper resourceRequestMapper = sqlSession.getMapper(ResourceRequestMapper.class);
                    return resourceRequestMapper.getInternalAdhocHours(projectID, signum, date);
                }     
     
     public List<LeavePlanModel> getLeaveHours(String signum, String date) {
 		ResourceRequestMapper resourceRequestMapper = sqlSession.getMapper(ResourceRequestMapper.class);
         return resourceRequestMapper.getLeaveHours(signum, date);
 	}    
 	
}
