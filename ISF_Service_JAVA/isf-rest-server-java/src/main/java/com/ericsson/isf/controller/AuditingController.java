package com.ericsson.isf.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ericsson.isf.model.AuditDataModel;
import com.ericsson.isf.model.DownloadTemplateModel;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.service.audit.AuditManagementService;

@RestController
@Validated
@RequestMapping("/auditing")
public class AuditingController {
	
	@Autowired
	AuditManagementService auditManagementService;
	
	private static final Logger LOG = LoggerFactory.getLogger(AuditingController.class);
	
	List<AuditDataModel> listComments=new ArrayList<>();
	@RequestMapping(value = "/addComment", method = RequestMethod.POST)
	public ResponseEntity<Response<AuditDataModel>> addComment(@RequestBody AuditDataModel commentModel) {
		LOG.info("Add Comment:Success");
		return auditManagementService.addComments(commentModel);
	}	
	
	@RequestMapping(value = "/getAuditData", method = RequestMethod.GET)
	public Map<String,Object> getAuditData(@RequestParam(value="pageId",required=true) String pageId,@RequestParam(value="start",required=false) Integer start,@RequestParam(value="length",required=false) Integer length, @RequestParam(value="searchString",required=false) @NotNull(message = "searchString can't be null")  String searchString ){
		return auditManagementService.getAuditDataByPageId(pageId, start, length, searchString);
	}
	
	 @RequestMapping(value="/downloadAuditData", method=RequestMethod.GET)
	    public DownloadTemplateModel downloadAuditData(@RequestParam(value="pageId",required=true) String pageId,@RequestParam(value="start",required=false) Integer start,@RequestParam(value="length",required=false) Integer length, @RequestParam(value="searchString",required=false) String searchString 
	    	) throws IOException, SQLException
	    {
	    return this.auditManagementService.downloadAuditData(pageId, start, length, searchString);
	     }
	 
	@RequestMapping(value = "/addAuditData", method = RequestMethod.POST)
		public AuditDataModel addCommentX(@RequestBody AuditDataModel auditdata) {
			return auditManagementService.addToAudit(auditdata);
	}
}

