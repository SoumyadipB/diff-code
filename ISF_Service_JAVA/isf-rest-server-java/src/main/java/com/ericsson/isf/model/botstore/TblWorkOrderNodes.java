package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * TblWorkOrderNodes generated by hbm2java
 */
@Entity
@Table(name = "TBL_WORK_ORDER_NODES", schema = "transactionalData")
public class TblWorkOrderNodes implements java.io.Serializable {

	private TblWorkOrderNodesId id;

	public TblWorkOrderNodes() {
	}

	public TblWorkOrderNodes(TblWorkOrderNodesId id) {
		this.id = id;
	}

	@EmbeddedId

	@AttributeOverrides({ @AttributeOverride(name = "woid", column = @Column(name = "WOID")),
			@AttributeOverride(name = "nodeType", column = @Column(name = "NodeType", length = 512)),
			@AttributeOverride(name = "nodeNames", column = @Column(name = "NodeNames", length = 500)),
			@AttributeOverride(name = "createdBy", column = @Column(name = "CreatedBy", length = 1024)),
			@AttributeOverride(name = "createdDate", column = @Column(name = "CreatedDate", length = 23)),
			@AttributeOverride(name = "lastModifiedBy", column = @Column(name = "LastModifiedBy", length = 1024)),
			@AttributeOverride(name = "lastModifiedDate", column = @Column(name = "LastModifiedDate", length = 23)),
			@AttributeOverride(name = "wnid", column = @Column(name = "WNID", nullable = false)) })
	public TblWorkOrderNodesId getId() {
		return this.id;
	}

	public void setId(TblWorkOrderNodesId id) {
		this.id = id;
	}

}
