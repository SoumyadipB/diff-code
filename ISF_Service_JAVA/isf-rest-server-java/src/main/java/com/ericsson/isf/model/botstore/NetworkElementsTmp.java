package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * NetworkElementsTmp generated by hbm2java
 */
@Entity
@Table(name = "NetworkElementsTmp", schema = "dbo")
public class NetworkElementsTmp implements java.io.Serializable {

	private NetworkElementsTmpId id;

	public NetworkElementsTmp() {
	}

	public NetworkElementsTmp(NetworkElementsTmpId id) {
		this.id = id;
	}

	@EmbeddedId

	@AttributeOverrides({ @AttributeOverride(name = "market", column = @Column(name = "Market")),
			@AttributeOverride(name = "elementType", column = @Column(name = "ElementType")),
			@AttributeOverride(name = "type", column = @Column(name = "Type")),
			@AttributeOverride(name = "name", column = @Column(name = "Name")),
			@AttributeOverride(name = "sector", column = @Column(name = "Sector")),
			@AttributeOverride(name = "latitude", column = @Column(name = "Latitude")),
			@AttributeOverride(name = "longitude", column = @Column(name = "Longitude")),
			@AttributeOverride(name = "softwareRelease", column = @Column(name = "Software_Release")) })
	public NetworkElementsTmpId getId() {
		return this.id;
	}

	public void setId(NetworkElementsTmpId id) {
		this.id = id;
	}

}
