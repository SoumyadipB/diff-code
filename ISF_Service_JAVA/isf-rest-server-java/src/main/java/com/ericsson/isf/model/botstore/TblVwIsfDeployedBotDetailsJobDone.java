package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * TblVwIsfDeployedBotDetailsJobDone generated by hbm2java
 */
@Entity
@Table(name = "TBL_vw_ISF_Deployed_BOT_Details_JobDone", schema = "refData")
public class TblVwIsfDeployedBotDetailsJobDone implements java.io.Serializable {

	private TblVwIsfDeployedBotDetailsJobDoneId id;

	public TblVwIsfDeployedBotDetailsJobDone() {
	}

	public TblVwIsfDeployedBotDetailsJobDone(TblVwIsfDeployedBotDetailsJobDoneId id) {
		this.id = id;
	}

	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "idbehaviourDetails", column = @Column(name = "idbehaviourDetails", length = 500)),
			@AttributeOverride(name = "idShTasks", column = @Column(name = "idSH_Tasks", length = 500)),
			@AttributeOverride(name = "shBehaviourDetailsName", column = @Column(name = "SH_behaviourDetailsName", length = 500)),
			@AttributeOverride(name = "shTasksname", column = @Column(name = "SH_Tasksname", length = 500)),
			@AttributeOverride(name = "shOutFolder", column = @Column(name = "SH_Out_Folder", length = 500)),
			@AttributeOverride(name = "shActionName", column = @Column(name = "SH_Action_Name", length = 500)),
			@AttributeOverride(name = "shClass", column = @Column(name = "SH_Class", length = 500)),
			@AttributeOverride(name = "shValidation", column = @Column(name = "SH_Validation", length = 500)),
			@AttributeOverride(name = "shType", column = @Column(name = "SH_Type", length = 500)),
			@AttributeOverride(name = "shInitiator", column = @Column(name = "SH_Initiator", length = 500)),
			@AttributeOverride(name = "shDetails", column = @Column(name = "SH_Details", length = 500)),
			@AttributeOverride(name = "shDeployedAt", column = @Column(name = "SH_DeployedAt", length = 500)),
			@AttributeOverride(name = "shBehaviourDetailsStatus", column = @Column(name = "SH_behaviourDetailsStatus", length = 500)),
			@AttributeOverride(name = "shManualTime", column = @Column(name = "SH_ManualTime", length = 500)),
			@AttributeOverride(name = "shShtime", column = @Column(name = "SH_SHTime", length = 500)),
			@AttributeOverride(name = "shSpoc", column = @Column(name = "SH_SPOC", length = 500)),
			@AttributeOverride(name = "shProject", column = @Column(name = "SH_Project", length = 500)),
			@AttributeOverride(name = "shRegion", column = @Column(name = "SH_Region", length = 500)),
			@AttributeOverride(name = "shDeveloper", column = @Column(name = "SH_Developer", length = 500)),
			@AttributeOverride(name = "shCategory", column = @Column(name = "SH_Category", length = 500)),
			@AttributeOverride(name = "shTimeBookingAvailable", column = @Column(name = "SH_TimeBookingAvailable", length = 500)),
			@AttributeOverride(name = "shUseFrequency", column = @Column(name = "SH_UseFrequency", length = 500)),
			@AttributeOverride(name = "shUserCount", column = @Column(name = "SH_UserCount", length = 500)),
			@AttributeOverride(name = "shNdxactivity", column = @Column(name = "SH_NDXActivity", length = 500)),
			@AttributeOverride(name = "shNdxsubActivity", column = @Column(name = "SH_NDXSubActivity", length = 500)),
			@AttributeOverride(name = "shDomain", column = @Column(name = "SH_Domain", length = 500)),
			@AttributeOverride(name = "shJarName", column = @Column(name = "SH_JarName", length = 500)),
			@AttributeOverride(name = "shBotfor", column = @Column(name = "SH_BOTFor", length = 500)),
			@AttributeOverride(name = "shParallelWoexecution", column = @Column(name = "SH_ParallelWOExecution", length = 500)),
			@AttributeOverride(name = "shSubactivityId", column = @Column(name = "SH_Subactivity_ID", length = 500)),
			@AttributeOverride(name = "shProjectName", column = @Column(name = "SH_ProjectName", length = 500)),
			@AttributeOverride(name = "shCodeReuseFactor", column = @Column(name = "SH_CodeReuseFactor", length = 500)),
			@AttributeOverride(name = "shLineOfCode", column = @Column(name = "SH_LineOfCode", length = 500)),
			@AttributeOverride(name = "shUatstatus", column = @Column(name = "SH_UATStatus", length = 500)),
			@AttributeOverride(name = "shUatowner", column = @Column(name = "SH_UATOwner", length = 500)),
			@AttributeOverride(name = "shUatcompletionDate", column = @Column(name = "SH_UATCompletionDate", length = 500)),
			@AttributeOverride(name = "uploadedOn", column = @Column(name = "UploadedON", length = 50)) })
	public TblVwIsfDeployedBotDetailsJobDoneId getId() {
		return this.id;
	}

	public void setId(TblVwIsfDeployedBotDetailsJobDoneId id) {
		this.id = id;
	}

}
