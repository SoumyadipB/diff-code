package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * TblDeliveryResponsible generated by hbm2java
 */
@Entity
@Table(name = "TBL_DELIVERY_RESPONSIBLE", schema = "transactionalData")
public class TblDeliveryResponsible implements java.io.Serializable {

	private TblDeliveryResponsibleId id;

	public TblDeliveryResponsible() {
	}

	public TblDeliveryResponsible(TblDeliveryResponsibleId id) {
		this.id = id;
	}

	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "deliveryResponsibleId", column = @Column(name = "DeliveryResponsibleID", nullable = false)),
			@AttributeOverride(name = "projectId", column = @Column(name = "ProjectID")),
			@AttributeOverride(name = "deliveryResponsible", column = @Column(name = "DeliveryResponsible", length = 512)),
			@AttributeOverride(name = "active", column = @Column(name = "Active")),
			@AttributeOverride(name = "createdBy", column = @Column(name = "CreatedBy", length = 512)),
			@AttributeOverride(name = "createdDate", column = @Column(name = "CreatedDate", length = 23)),
			@AttributeOverride(name = "lastModifiedBy", column = @Column(name = "LastModifiedBy", length = 512)),
			@AttributeOverride(name = "lastModifiedDate", column = @Column(name = "LastModifiedDate", length = 23)),
			@AttributeOverride(name = "signumId", column = @Column(name = "SignumID", length = 512)) })
	public TblDeliveryResponsibleId getId() {
		return this.id;
	}

	public void setId(TblDeliveryResponsibleId id) {
		this.id = id;
	}

}