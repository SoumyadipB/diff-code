package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * TblSubactivityFlowchartDep generated by hbm2java
 */
@Entity
@Table(name = "TBL_SUBACTIVITY_FLOWCHART_DEP", schema = "transactionalData")
public class TblSubactivityFlowchartDep implements java.io.Serializable {

	private TblSubactivityFlowchartDepId id;

	public TblSubactivityFlowchartDep() {
	}

	public TblSubactivityFlowchartDep(TblSubactivityFlowchartDepId id) {
		this.id = id;
	}

	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "subActivityFlowChartDepId", column = @Column(name = "SubActivityFlowChartDepID", nullable = false)),
			@AttributeOverride(name = "subActivityFlowChartStepId", column = @Column(name = "SubActivityFlowChartStepID")),
			@AttributeOverride(name = "subActivityFlowChartDependentStepId", column = @Column(name = "SubActivityFlowChartDependentStepID")),
			@AttributeOverride(name = "dependencyCondition", column = @Column(name = "DependencyCondition")),
			@AttributeOverride(name = "linkJson", column = @Column(name = "LinkJSON")) })
	public TblSubactivityFlowchartDepId getId() {
		return this.id;
	}

	public void setId(TblSubactivityFlowchartDepId id) {
		this.id = id;
	}

}