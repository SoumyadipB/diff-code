package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Loe generated by hbm2java
 */
@Entity
@Table(name = "LOE", schema = "dbo")
public class Loe implements java.io.Serializable {

	private LoeId id;

	public Loe() {
	}

	public Loe(LoeId id) {
		this.id = id;
	}

	@EmbeddedId

	@AttributeOverrides({ @AttributeOverride(name = "fcstepId", column = @Column(name = "FCStepID", nullable = false)),
			@AttributeOverride(name = "loeInMinutes", column = @Column(name = "LOE__in_minutes_", nullable = false, precision = 53, scale = 0)) })
	public LoeId getId() {
		return this.id;
	}

	public void setId(LoeId id) {
		this.id = id;
	}

}
