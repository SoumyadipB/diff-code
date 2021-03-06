package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * TblUserOrganisation generated by hbm2java
 */
@Entity
@Table(name = "TBL_USER_ORGANISATION", schema = "transactionalData")
public class TblUserOrganisation implements java.io.Serializable {

	private TblUserOrganisationId id;

	public TblUserOrganisation() {
	}

	public TblUserOrganisation(TblUserOrganisationId id) {
		this.id = id;
	}

	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "userOrginisationId", column = @Column(name = "UserOrginisationID", nullable = false)),
			@AttributeOverride(name = "userName", column = @Column(name = "UserName", length = 1024)),
			@AttributeOverride(name = "signumId", column = @Column(name = "SignumID", length = 128)),
			@AttributeOverride(name = "organisation", column = @Column(name = "Organisation", length = 512)),
			@AttributeOverride(name = "active", column = @Column(name = "Active")) })
	public TblUserOrganisationId getId() {
		return this.id;
	}

	public void setId(TblUserOrganisationId id) {
		this.id = id;
	}

}
