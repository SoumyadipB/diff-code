package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * TblIsfdesktopUpdates generated by hbm2java
 */
@Entity
@Table(name = "TBL_ISFDesktopUpdates", schema = "refData")
public class TblIsfdesktopUpdates implements java.io.Serializable {

	private TblIsfdesktopUpdatesId id;

	public TblIsfdesktopUpdates() {
	}

	public TblIsfdesktopUpdates(TblIsfdesktopUpdatesId id) {
		this.id = id;
	}

	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "isfdesktopUpdatesId", column = @Column(name = "ISFDesktopUpdatesID", nullable = false)),
			@AttributeOverride(name = "updateType", column = @Column(name = "UpdateType", length = 10)),
			@AttributeOverride(name = "version", column = @Column(name = "Version", length = 20)),
			@AttributeOverride(name = "updatedOn", column = @Column(name = "UpdatedOn", length = 23)),
			@AttributeOverride(name = "active", column = @Column(name = "Active")) })
	public TblIsfdesktopUpdatesId getId() {
		return this.id;
	}

	public void setId(TblIsfdesktopUpdatesId id) {
		this.id = id;
	}

}
