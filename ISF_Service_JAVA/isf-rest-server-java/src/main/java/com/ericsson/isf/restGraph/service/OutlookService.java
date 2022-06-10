package com.ericsson.isf.restGraph.service;

import com.ericsson.isf.restGraph.model.Attachment;
import com.ericsson.isf.restGraph.model.Message;
import com.ericsson.isf.restGraph.model.PagedResult;
import com.ericsson.isf.restGraph.model.RequestMessageBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 
 * @author eakinhm
 *
 */
public interface OutlookService {

	@GET("/v1.0/users/{userPrincipalName}/mailFolders/{folderid}/messages")
	Call<PagedResult<Message>> getMessages(
			@Path("userPrincipalName") String userPrincipalName,
			@Path("folderid") String folderId, 
			@Query("$filter") String filter, 
			@Query("$select") String select);
	
	@GET("/v1.0/users/{userPrincipalName}/mailFolders/{folderid}/messages")
	Call<PagedResult<Message>> getMessages(
			@Path("userPrincipalName") String userPrincipalName,
			@Path("folderid") String folderId,
			@Query("$filter") String filter);

	//@Headers({"Content-Type: application/json","Accept: application/json"})
	@PATCH("/v1.0/users/{userPrincipalName}/messages/{messageId}")
	Call<PagedResult<Message>> updateMessage(
			@Path("userPrincipalName") String userPrincipalName,
			@Path("messageId") String messageId,
			@Body RequestMessageBody body);

	@GET("/v1.0/users/{userPrincipalName}/messages/{messageId}?$expand=microsoft.graph.eventMessage/event")
	Call<Message> getEvent(
			@Path("userPrincipalName") String userPrincipalName,
			@Path("messageId") String messageId);

	@GET("/v1.0/users/{userPrincipalName}/messages/{messageId}/attachments")
	Call<PagedResult<Attachment>> getAttachments(
			@Path("userPrincipalName") String userPrincipalName,
			@Path("messageId") String messageId);

}
