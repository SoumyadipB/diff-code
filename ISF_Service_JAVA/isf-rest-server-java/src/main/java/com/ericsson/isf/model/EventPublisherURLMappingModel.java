package com.ericsson.isf.model;

/**
 * This class is a model representation of table
 * refData.TBL_EventPublisherURLMapping
 * 
 * @author eakinhm
 *
 */
public class EventPublisherURLMappingModel {

	private Long sourceID;
	private String sourceUrl;
	private String category;
	private String payload;
	private Integer eventPublisherID;

	public Long getSourceID() {
		return sourceID;
	}

	public void setSourceID(Long sourceID) {
		this.sourceID = sourceID;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public Integer getEventPublisherID() {
		return eventPublisherID;
	}

	public void setEventPublisherID(Integer eventPublisherID) {
		this.eventPublisherID = eventPublisherID;
	}

	@Override
	public String toString() {
		return "EventPublisherURLMappingModel [sourceID=" + sourceID + ", sourceUrl=" + sourceUrl + ", category="
				+ category + ", payload=" + payload + ", eventPublisherID=" + eventPublisherID + "]";
	}

	public EventPublisherURLMappingModel(Long sourceID, String sourceUrl, String category, String payload,
			Integer eventPublisherID) {
		this.sourceID = sourceID;
		this.sourceUrl = sourceUrl;
		this.category = category;
		this.payload = payload;
		this.eventPublisherID = eventPublisherID;
	}

}
