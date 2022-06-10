package com.ericsson.isf.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PdbDataModel {
	
	    public String _id;
	    @JsonProperty("CIRCLE") 
	    public String cIRCLE;
	    @JsonProperty("NOMINAL_AOP") 
	    public String nOMINAL_AOP;
	    @JsonProperty("NOMINAL_QUARTER") 
	    public String nOMINAL_QUARTER;
	    @JsonProperty("TECH_ID") 
	    public String tECH_ID;
	    @JsonProperty("SITE_ID_2G") 
	    public String sITE_ID_2G;
	    @JsonProperty("ENODEB_ID") 
	    public String eNODEB_ID;
	    @JsonProperty("CELL_ID") 
	    public String cELL_ID;
	    @JsonProperty("BAND") 
	    public String bAND;
	    @JsonProperty("PROJECT") 
	    public String pROJECT;
	    @JsonProperty("BTS_TYPE") 
	    public String bTS_TYPE;
	    @JsonProperty("TOWN_CATEGORY") 
	    public String tOWN_CATEGORY;
	    @JsonProperty("SITE_TYPE") 
	    public String sITE_TYPE;
	    @JsonProperty("BUSINESS_PRIORITY") 
	    public String bUSINESS_PRIORITY;
	    @JsonProperty("CTA") 
	    public String cTA;
	    @JsonProperty("USO") 
	    public String uSO;
	    @JsonProperty("OEM_NAME") 
	    public String oEM_NAME;
	    @JsonProperty("TOCO_NAME") 
	    public String tOCO_NAME;
	    @JsonProperty("TOCO_ID") 
	    public String tOCO_ID;
	    @JsonProperty("TYPE_OF_TENANCY") 
	    public String tYPE_OF_TENANCY;
	    @JsonProperty("NOMINAL_TYPE") 
	    public String nOMINAL_TYPE;
	    @JsonProperty("TOWER_TYPE") 
	    public String tOWER_TYPE;
	    @JsonProperty("PRODUCT_TYPE") 
	    public String pRODUCT_TYPE;
	    @JsonProperty("EB_AVAILABILITY_FROM_SP") 
	    public String eB_AVAILABILITY_FROM_SP;
	    @JsonProperty("DG_REQUIRED") 
	    public String dG_REQUIRED;
	    @JsonProperty("LAT") 
	    public String lAT;
	    @JsonProperty("LONG") 
	    public String lONG;
	    @JsonProperty("SR_NO") 
	    public String sR_NO;
	    @JsonProperty("SR_DATE") 
	    public String sR_DATE;
	    @JsonProperty("SP_DATE") 
	    public String sP_DATE;
	    @JsonProperty("SO_DATE") 
	    public String sO_DATE;
	    @JsonProperty("LOCATOR_ID") 
	    public String lOCATOR_ID;
	    @JsonProperty("RFAI_REJECTION_DATE") 
	    public Object rFAI_REJECTION_DATE;
	    @JsonProperty("RFAI_REGENERATION_DATE") 
	    public Object rFAI_REGENERATION_DATE;
	    @JsonProperty("E_E_MEDIA_READY_DATE") 
	    public Object e_E_MEDIA_READY_DATE;
	    @JsonProperty("POST_MEDIA_ISSUE_OPEN_DATE") 
	    public Object pOST_MEDIA_ISSUE_OPEN_DATE;
	    @JsonProperty("POST_MEDIA_ISSUE_CLOSE_DATE") 
	    public Object pOST_MEDIA_ISSUE_CLOSE_DATE;
	    @JsonProperty("SACFA_APPLICATION_ID") 
	    public String sACFA_APPLICATION_ID;
	    @JsonProperty("WPC_ACCEPTANCE_DATE") 
	    public Object wPC_ACCEPTANCE_DATE;
	    @JsonProperty("WPC_ACCEPTANCE_ID") 
	    public String wPC_ACCEPTANCE_ID;
	    @JsonProperty("PERFORMANCE_AT_ACCEPTANCE_DATE") 
	    public Object pERFORMANCE_AT_ACCEPTANCE_DATE;
	    @JsonProperty("TX_MEDIA_ON") 
	    public String tX_MEDIA_ON;
	    @JsonProperty("PRI_CATEGORY") 
	    public Object pRI_CATEGORY;
	    @JsonProperty("CURRENT_STATUS_OF_SITE") 
	    public String cURRENT_STATUS_OF_SITE;
	    @JsonProperty("MW_FAR_END_ID") 
	    public String mW_FAR_END_ID;
	    @JsonProperty("MW_FAR_END_TOCO_TYPE") 
	    public String mW_FAR_END_TOCO_TYPE;
	    @JsonProperty("MATERIAL_RETURNED_DATE") 
	    public Date mATERIAL_RETURNED_DATE;
	    @JsonProperty("PRE_MS1_PRI1_OPENING_DATE") 
	    public Date pRE_MS1_PRI1_OPENING_DATE;
	    @JsonProperty("PRE_MS1_PRI1_CLOSING_DATE") 
	    public Date pRE_MS1_PRI1_CLOSING_DATE;
	    @JsonProperty("PRE_MS1_PRI2_OPENING_DATE") 
	    public Date pRE_MS1_PRI2_OPENING_DATE;
	    @JsonProperty("PRE_MS1_PRI2_CLOSING_DATE") 
	    public Date pRE_MS1_PRI2_CLOSING_DATE;
	    @JsonProperty("PRE_MS1_PRI3_OPENING_DATE") 
	    public Date pRE_MS1_PRI3_OPENING_DATE;
	    @JsonProperty("PRE_MS1_PRI3_CLOSING_DATE") 
	    public Date pRE_MS1_PRI3_CLOSING_DATE;
	    @JsonProperty("PRE_MS1_PRI4_OPENING_DATE") 
	    public Date pRE_MS1_PRI4_OPENING_DATE;
	    @JsonProperty("PRE_MS1_PRI4_CLOSING_DATE") 
	    public Date pRE_MS1_PRI4_CLOSING_DATE;
	    @JsonProperty("PRE_MS1_PRI5_OPENING_DATE") 
	    public Date pRE_MS1_PRI5_OPENING_DATE;
	    @JsonProperty("PRE_MS1_PRI5_CLOSING_DATE") 
	    public Date pRE_MS1_PRI5_CLOSING_DATE;
	    @JsonProperty("PRE_MS1_PRI6_OPENING_DATE") 
	    public Date pRE_MS1_PRI6_OPENING_DATE;
	    @JsonProperty("PRE_MS1_PRI6_CLOSING_DATE") 
	    public Date pRE_MS1_PRI6_CLOSING_DATE;
	    @JsonProperty("PRE_MS1_PRI7_OPENING_DATE") 
	    public Date pRE_MS1_PRI7_OPENING_DATE;
	    @JsonProperty("PRE_MS1_PRI7_CLOSING_DATE") 
	    public Date pRE_MS1_PRI7_CLOSING_DATE;
	    @JsonProperty("PRE_MS1_PRI8_OPENING_DATE") 
	    public Date pRE_MS1_PRI8_OPENING_DATE;
	    @JsonProperty("PRE_MS1_PRI8_CLOSING_DATE") 
	    public Date pRE_MS1_PRI8_CLOSING_DATE;
	    @JsonProperty("PRE_MS1_PRI9_OPENING_DATE") 
	    public Date pRE_MS1_PRI9_OPENING_DATE;
	    @JsonProperty("PRE_MS1_PRI9_CLOSING_DATE") 
	    public Date pRE_MS1_PRI9_CLOSING_DATE;
	    @JsonProperty("PRE_MS1_PRI10_OPENING_DATE") 
	    public Date pRE_MS1_PRI10_OPENING_DATE;
	    @JsonProperty("PRE_MS1_PRI10_CLOSING_DATE") 
	    public Date pRE_MS1_PRI10_CLOSING_DATE;
	    @JsonProperty("POST_MS1_PRI1_OPENING_DATE") 
	    public Date pOST_MS1_PRI1_OPENING_DATE;
	    @JsonProperty("POST_MS1_PRI1_CLOSING_DATE") 
	    public Date pOST_MS1_PRI1_CLOSING_DATE;
	    @JsonProperty("POST_MS1_PRI2_OPENING_DATE") 
	    public Date pOST_MS1_PRI2_OPENING_DATE;
	    @JsonProperty("POST_MS1_PRI2_CLOSING_DATE") 
	    public Date pOST_MS1_PRI2_CLOSING_DATE;
	    @JsonProperty("POST_MS1_PRI3_OPENING_DATE") 
	    public Date pOST_MS1_PRI3_OPENING_DATE;
	    @JsonProperty("POST_MS1_PRI3_CLOSING_DATE") 
	    public Date pOST_MS1_PRI3_CLOSING_DATE;
	    @JsonProperty("POST_MS1_PRI4_OPENING_DATE") 
	    public Date pOST_MS1_PRI4_OPENING_DATE;
	    @JsonProperty("POST_MS1_PRI4_CLOSING_DATE") 
	    public Date pOST_MS1_PRI4_CLOSING_DATE;
	    @JsonProperty("POST_MS1_PRI5_OPENING_DATE") 
	    public Date pOST_MS1_PRI5_OPENING_DATE;
	    @JsonProperty("POST_MS1_PRI5_CLOSING_DATE") 
	    public Date pOST_MS1_PRI5_CLOSING_DATE;
	    @JsonProperty("POST_MS1_PRI6_OPENING_DATE") 
	    public Date pOST_MS1_PRI6_OPENING_DATE;
	    @JsonProperty("POST_MS1_PRI6_CLOSING_DATE") 
	    public Date pOST_MS1_PRI6_CLOSING_DATE;
	    @JsonProperty("POST_MS1_PRI7_OPENING_DATE") 
	    public Date pOST_MS1_PRI7_OPENING_DATE;
	    @JsonProperty("POST_MS1_PRI7_CLOSING_DATE") 
	    public Date pOST_MS1_PRI7_CLOSING_DATE;
	    @JsonProperty("POST_MS1_PRI8_OPENING_DATE") 
	    public Date pOST_MS1_PRI8_OPENING_DATE;
	    @JsonProperty("POST_MS1_PRI8_CLOSING_DATE") 
	    public Date pOST_MS1_PRI8_CLOSING_DATE;
	    @JsonProperty("POST_MS1_PRI9_OPENING_DATE") 
	    public Date pOST_MS1_PRI9_OPENING_DATE;
	    @JsonProperty("POST_MS1_PRI9_CLOSING_DATE") 
	    public Date pOST_MS1_PRI9_CLOSING_DATE;
	    @JsonProperty("POST_MS1_PRI10_OPENING_DATE") 
	    public Date pOST_MS1_PRI10_OPENING_DATE;
	    @JsonProperty("POST_MS1_PRI10_CLOSING_DATE") 
	    public Date pOST_MS1_PRI10_CLOSING_DATE;
	    @JsonProperty("SR_REJECTION_BY_TOCO_DATE") 
	    public Date sR_REJECTION_BY_TOCO_DATE;
	    @JsonProperty("SO_WITHDRAWN_BY_AIRTEL_DATE") 
	    public Date sO_WITHDRAWN_BY_AIRTEL_DATE;
	    @JsonProperty("SO_REJECTION_BY_TOCO_DATE") 
	    public Date sO_REJECTION_BY_TOCO_DATE;
	    @JsonProperty("SP_REJECTION_BY_AIRTEL_DATE") 
	    public Date sP_REJECTION_BY_AIRTEL_DATE;
	    @JsonProperty("CGI_ID") 
	    public String cGI_ID;
	    @JsonProperty("RFAI_DATE") 
	    public Date rFAI_DATE;
	    @JsonProperty("E_E_MEDIA_STATUS") 
	    public String e_E_MEDIA_STATUS;
	    @JsonProperty("E_E_MEDIA_STATUS_REMARKS") 
	    public String e_E_MEDIA_STATUS_REMARKS;
	    @JsonProperty("POST_RFAI_SURVEY_DATE_BASELINE") 
	    public Date pOST_RFAI_SURVEY_DATE_BASELINE;
	    @JsonProperty("POST_RFAI_SURVEY_DATE_PLAN") 
	    public Date pOST_RFAI_SURVEY_DATE_PLAN;
	    @JsonProperty("POST_RFAI_SURVEY_DATE_ACTUAL") 
	    public Date pOST_RFAI_SURVEY_DATE_ACTUAL;
	    @JsonProperty("POST_RFAI_SURVEY_REMARKS") 
	    public String pOST_RFAI_SURVEY_REMARKS;
	    @JsonProperty("RFAI_ACCEPTANCE_STATUS") 
	    public String rFAI_ACCEPTANCE_STATUS;
	    @JsonProperty("RFAI_ACCEPTANCE_STATUS_DATE_BASELINE") 
	    public Date rFAI_ACCEPTANCE_STATUS_DATE_BASELINE;
	    @JsonProperty("RFAI_ACCEPTANCE_STATUS_DATE_PLAN") 
	    public Date rFAI_ACCEPTANCE_STATUS_DATE_PLAN;
	    @JsonProperty("RFAI_ACCEPTANCE_STATUS_DATE_ACTUAL") 
	    public Date rFAI_ACCEPTANCE_STATUS_DATE_ACTUAL;
	    @JsonProperty("RFAI_ACCEPTANCE_STATUS_VERSION_1") 
	    public String rFAI_ACCEPTANCE_STATUS_VERSION_1;
	    @JsonProperty("RFAI_ACCEPTANCE_STATUS_DATE_PLAN_VERSION_1") 
	    public Date rFAI_ACCEPTANCE_STATUS_DATE_PLAN_VERSION_1;
	    @JsonProperty("RFAI_ACCEPTANCE_STATUS_DATE_ACTUAL_VERSION_1") 
	    public Date rFAI_ACCEPTANCE_STATUS_DATE_ACTUAL_VERSION_1;
	    @JsonProperty("RFAI_ACCEPTANCE_STATUS_VERSION_1_REMARKS") 
	    public String rFAI_ACCEPTANCE_STATUS_VERSION_1_REMARKS;
	    @JsonProperty("RFAI_ACCEPTANCE_STATUS_VERSION_2") 
	    public String rFAI_ACCEPTANCE_STATUS_VERSION_2;
	    @JsonProperty("RFAI_ACCEPTANCE_STATUS_DATE_PLAN_VERSION_2") 
	    public Date rFAI_ACCEPTANCE_STATUS_DATE_PLAN_VERSION_2;
	    @JsonProperty("RFAI_ACCEPTANCE_STATUS_DATE_ACTUAL_VERSION_2") 
	    public Date rFAI_ACCEPTANCE_STATUS_DATE_ACTUAL_VERSION_2;
	    @JsonProperty("RFAI_ACCEPTANCE_STATUS_VERSION_2_REMARKS") 
	    public String rFAI_ACCEPTANCE_STATUS_VERSION_2_REMARKS;
	    @JsonProperty("MO_PUNCHING_DATE_BASELINE") 
	    public Date mO_PUNCHING_DATE_BASELINE;
	    @JsonProperty("MO_PUNCHING_DATE_PLAN") 
	    public Date mO_PUNCHING_DATE_PLAN;
	    @JsonProperty("MO_PUNCHING_DATE_ACTUAL") 
	    public Date mO_PUNCHING_DATE_ACTUAL;
	    @JsonProperty("MO_PUNCHING_DATE_PLAN_VERSION_1") 
	    public Object mO_PUNCHING_DATE_PLAN_VERSION_1;
	    @JsonProperty("MO_PUNCHING_DATE_PLAN_VERSION_2") 
	    public Object mO_PUNCHING_DATE_PLAN_VERSION_2;
	    @JsonProperty("MO_PUNCHING_DATE_PLAN_VERSION_3") 
	    public Object mO_PUNCHING_DATE_PLAN_VERSION_3;
	    @JsonProperty("MO_PUNCHING_DATE_ACTUAL_VERSION_1") 
	    public Object mO_PUNCHING_DATE_ACTUAL_VERSION_1;
	    @JsonProperty("MO_PUNCHING_DATE_ACTUAL_VERSION_2") 
	    public Object mO_PUNCHING_DATE_ACTUAL_VERSION_2;
	    @JsonProperty("MO_PUNCHING_DATE_ACTUAL_VERSION_3") 
	    public Object mO_PUNCHING_DATE_ACTUAL_VERSION_3;
	    @JsonProperty("MO_PUNCHING_VERSION_1_REMARKS") 
	    public Object mO_PUNCHING_VERSION_1_REMARKS;
	    @JsonProperty("MO_PUNCHING_VERSION_2_REMARKS") 
	    public Object mO_PUNCHING_VERSION_2_REMARKS;
	    @JsonProperty("MO_PUNCHING_VERSION_3_REMARKS") 
	    public Object mO_PUNCHING_VERSION_3_REMARKS;
	    @JsonProperty("MATERIAL_DISPATCH_DATE_BASELINE") 
	    public Object mATERIAL_DISPATCH_DATE_BASELINE;
	    @JsonProperty("MATERIAL_DISPATCH_DATE_PLAN") 
	    public Object mATERIAL_DISPATCH_DATE_PLAN;
	    @JsonProperty("MATERIAL_DISPATCH_DATE_ACTUAL") 
	    public Object mATERIAL_DISPATCH_DATE_ACTUAL;
	    @JsonProperty("MATERIAL_DISPATCH_DATE_PLAN_VERSION_1") 
	    public Object mATERIAL_DISPATCH_DATE_PLAN_VERSION_1;
	    @JsonProperty("MATERIAL_DISPATCH_DATE_PLAN_VERSION_2") 
	    public Object mATERIAL_DISPATCH_DATE_PLAN_VERSION_2;
	    @JsonProperty("MATERIAL_DISPATCH_DATE_PLAN_VERSION_3") 
	    public Object mATERIAL_DISPATCH_DATE_PLAN_VERSION_3;
	    @JsonProperty("MATERIAL_DISPATCH_DATE_ACTUAL_VERSION_1") 
	    public Object mATERIAL_DISPATCH_DATE_ACTUAL_VERSION_1;
	    @JsonProperty("MATERIAL_DISPATCH_DATE_ACTUAL_VERSION_2") 
	    public Object mATERIAL_DISPATCH_DATE_ACTUAL_VERSION_2;
	    @JsonProperty("MATERIAL_DISPATCH_DATE_ACTUAL_VERSION_3") 
	    public Object mATERIAL_DISPATCH_DATE_ACTUAL_VERSION_3;
	    @JsonProperty("MATERIAL_DISPATCH_VERSION_1_REMARKS") 
	    public Object mATERIAL_DISPATCH_VERSION_1_REMARKS;
	    @JsonProperty("MATERIAL_DISPATCH_VERSION_2_REMARKS") 
	    public Object mATERIAL_DISPATCH_VERSION_2_REMARKS;
	    @JsonProperty("MATERIAL_DISPATCH_VERSION_3_REMARKS") 
	    public Object mATERIAL_DISPATCH_VERSION_3_REMARKS;
	    @JsonProperty("MATERIAL_DELIVERY_DATE_BASELINE") 
	    public Object mATERIAL_DELIVERY_DATE_BASELINE;
	    @JsonProperty("MATERIAL_DELIVERY_DATE_PLAN") 
	    public Object mATERIAL_DELIVERY_DATE_PLAN;
	    @JsonProperty("MATERIAL_DELIVERY_DATE_ACTUAL") 
	    public Object mATERIAL_DELIVERY_DATE_ACTUAL;
	    @JsonProperty("MATERIAL_DELIVERY_DATE_PLAN_VERSION_1") 
	    public Object mATERIAL_DELIVERY_DATE_PLAN_VERSION_1;
	    @JsonProperty("MATERIAL_DELIVERY_DATE_PLAN_VERSION_2") 
	    public Object mATERIAL_DELIVERY_DATE_PLAN_VERSION_2;
	    @JsonProperty("MATERIAL_DELIVERY_DATE_PLAN_VERSION_3") 
	    public Object mATERIAL_DELIVERY_DATE_PLAN_VERSION_3;
	    @JsonProperty("MATERIAL_DELIVERY_DATE_ACTUAL_VERSION_1") 
	    public Object mATERIAL_DELIVERY_DATE_ACTUAL_VERSION_1;
	    @JsonProperty("MATERIAL_DELIVERY_DATE_ACTUAL_VERSION_2") 
	    public Object mATERIAL_DELIVERY_DATE_ACTUAL_VERSION_2;
	    @JsonProperty("MATERIAL_DELIVERY_DATE_ACTUAL_VERSION_3") 
	    public Object mATERIAL_DELIVERY_DATE_ACTUAL_VERSION_3;
	    @JsonProperty("MATERIAL_DELIVERY_VERSION_1_REMARKS") 
	    public Object mATERIAL_DELIVERY_VERSION_1_REMARKS;
	    @JsonProperty("MATERIAL_DELIVERY_VERSION_2_REMARKS") 
	    public Object mATERIAL_DELIVERY_VERSION_2_REMARKS;
	    @JsonProperty("MATERIAL_DELIVERY_VERSION_3_REMARKS") 
	    public Object mATERIAL_DELIVERY_VERSION_3_REMARKS;
	    @JsonProperty("INSTALLATION_START_DATE_BASELINE") 
	    public Object iNSTALLATION_START_DATE_BASELINE;
	    @JsonProperty("INSTALLATION_START_DATE_PLAN") 
	    public Object iNSTALLATION_START_DATE_PLAN;
	    @JsonProperty("INSTALLATION_START_DATE_ACTUAL") 
	    public Object iNSTALLATION_START_DATE_ACTUAL;
	    @JsonProperty("INSTALLATION_START_REMARKS") 
	    public Object iNSTALLATION_START_REMARKS;
	    @JsonProperty("INSTALLATION_END_DATE_BASELINE") 
	    public Object iNSTALLATION_END_DATE_BASELINE;
	    @JsonProperty("INSTALLATION_END_DATE_PLAN") 
	    public Object iNSTALLATION_END_DATE_PLAN;
	    @JsonProperty("INSTALLATION_END_DATE_ACTUAL") 
	    public Object iNSTALLATION_END_DATE_ACTUAL;
	    @JsonProperty("INSTALLATION_END_REMARKS") 
	    public Object iNSTALLATION_END_REMARKS;
	    @JsonProperty("SACFA_ACK_NO") 
	    public Object sACFA_ACK_NO;
	    @JsonProperty("SACFA_COMMENTS") 
	    public Object sACFA_COMMENTS;
	    @JsonProperty("SACFA_WITH_DEVIATION_APPROVAL") 
	    public Object sACFA_WITH_DEVIATION_APPROVAL;
	    @JsonProperty("WPC_ACCEPTANCE_NUMBER") 
	    public Object wPC_ACCEPTANCE_NUMBER;
	    @JsonProperty("FINAL_SACFA_APPROVAL_DATE") 
	    public Object fINAL_SACFA_APPROVAL_DATE;
	    @JsonProperty("INTEGRATION_DATE_BASELINE") 
	    public Object iNTEGRATION_DATE_BASELINE;
	    @JsonProperty("INTEGRATION_DATE_PLAN") 
	    public Object iNTEGRATION_DATE_PLAN;
	    @JsonProperty("INTEGRATION_DATE_ACTUAL") 
	    public Object iNTEGRATION_DATE_ACTUAL;
	    @JsonProperty("INTEGRATION_REMARKS") 
	    public Object iNTEGRATION_REMARKS;
	    @JsonProperty("RSA_BASELINE") 
	    public Object rSA_BASELINE;
	    @JsonProperty("RSA_PLAN") 
	    public Object rSA_PLAN;
	    @JsonProperty("RSA_ACTUAL") 
	    public Object rSA_ACTUAL;
	    @JsonProperty("RSA_REMARKS") 
	    public Object rSA_REMARKS;
	    @JsonProperty("EMF_SUBMISSION_DATE_BASELINE") 
	    public Object eMF_SUBMISSION_DATE_BASELINE;
	    @JsonProperty("EMF_SUBMISSION_DATE_PLAN") 
	    public Object eMF_SUBMISSION_DATE_PLAN;
	    @JsonProperty("EMF_SUBMISSION_DATE_ACTUAL") 
	    public Object eMF_SUBMISSION_DATE_ACTUAL;
	    @JsonProperty("EMF_SUBMISSION_REMARKS") 
	    public Object eMF_SUBMISSION_REMARKS;
	    @JsonProperty("SCFT_COMPLETION_DATE_BASELINE") 
	    public Object sCFT_COMPLETION_DATE_BASELINE;
	    @JsonProperty("SCFT_COMPLETION_DATE_PLAN") 
	    public Object sCFT_COMPLETION_DATE_PLAN;
	    @JsonProperty("SCFT_COMPLETION_DATE_ACTUAL") 
	    public Object sCFT_COMPLETION_DATE_ACTUAL;
	    @JsonProperty("SCFT_COMPLETION_REMARKS") 
	    public Object sCFT_COMPLETION_REMARKS;
	    @JsonProperty("OA_DATE_BASELINE") 
	    public Object oA_DATE_BASELINE;
	    @JsonProperty("OA_DATE_PLAN") 
	    public Object oA_DATE_PLAN;
	    @JsonProperty("OA_DATE_ACTUAL") 
	    public Object oA_DATE_ACTUAL;
	    @JsonProperty("OA_REMARKS") 
	    public String oA_REMARKS;
	    @JsonProperty("AT_CUSTOMER_SIGNOFF_BASELINE") 
	    public Object aT_CUSTOMER_SIGNOFF_BASELINE;
	    @JsonProperty("AT_CUSTOMER_SIGNOFF_PLAN") 
	    public Object aT_CUSTOMER_SIGNOFF_PLAN;
	    @JsonProperty("AT_CUSTOMER_SIGNOFF_ACTUAL") 
	    public Object aT_CUSTOMER_SIGNOFF_ACTUAL;
	    @JsonProperty("AT_CUSTOMER_SIGNOFF_REMARKS") 
	    public Object aT_CUSTOMER_SIGNOFF_REMARKS;
	    @JsonProperty("INVOICING_IC_BASELINE") 
	    public Object iNVOICING_IC_BASELINE;
	    @JsonProperty("INVOICING_IC_PLAN") 
	    public Object iNVOICING_IC_PLAN;
	    @JsonProperty("INVOICING_IC_ACTUAL") 
	    public Object iNVOICING_IC_ACTUAL;
	    @JsonProperty("INVOICING_IC_REMARKS") 
	    public Object iNVOICING_IC_REMARKS;
	    @JsonProperty("PHYSICAL_AT_ACCEPTANCE_DATE_BASELINE") 
	    public Object pHYSICAL_AT_ACCEPTANCE_DATE_BASELINE;
	    @JsonProperty("PHYSICAL_AT_ACCEPTANCE_DATE_PLAN") 
	    public Object pHYSICAL_AT_ACCEPTANCE_DATE_PLAN;
	    @JsonProperty("PHYSICAL_AT_ACCEPTANCE_DATE_ACTUAL") 
	    public Object pHYSICAL_AT_ACCEPTANCE_DATE_ACTUAL;
	    @JsonProperty("PHYSICAL_AT_ACCEPTANCE_REMARKS") 
	    public Object pHYSICAL_AT_ACCEPTANCE_REMARKS;
	    @JsonProperty("MAPA_INCLUSION_DATE_BASELINE") 
	    public Object mAPA_INCLUSION_DATE_BASELINE;
	    @JsonProperty("MAPA_INCLUSION_DATE_PLAN") 
	    public Object mAPA_INCLUSION_DATE_PLAN;
	    @JsonProperty("MAPA_INCLUSION_DATE_ACTUAL") 
	    public Object mAPA_INCLUSION_DATE_ACTUAL;
	    @JsonProperty("MAPA_INCLUSION_REMARKS") 
	    public String mAPA_INCLUSION_REMARKS;
	    @JsonProperty("SCFT_AT_SIGNOFF_BASELINE") 
	    public Object sCFT_AT_SIGNOFF_BASELINE;
	    @JsonProperty("SCFT_AT_SIGNOFF_PLAN") 
	    public Object sCFT_AT_SIGNOFF_PLAN;
	    @JsonProperty("SCFT_AT_SIGNOFF_ACTUAL") 
	    public Object sCFT_AT_SIGNOFF_ACTUAL;
	    @JsonProperty("SCFT_AT_SIGNOFF_REMARKS") 
	    public Object sCFT_AT_SIGNOFF_REMARKS;
	    @JsonProperty("CAT_OSS_KPI_BASELINE") 
	    public Object cAT_OSS_KPI_BASELINE;
	    @JsonProperty("CAT_OSS_KPI_PLAN") 
	    public Object cAT_OSS_KPI_PLAN;
	    @JsonProperty("CAT_OSS_KPI_ACTUAL") 
	    public Object cAT_OSS_KPI_ACTUAL;
	    @JsonProperty("CAT_AT_SIGNOFF_BASELINE") 
	    public Object cAT_AT_SIGNOFF_BASELINE;
	    @JsonProperty("CAT_AT_SIGNOFF_PLAN") 
	    public Object cAT_AT_SIGNOFF_PLAN;
	    @JsonProperty("CAT_AT_SIGNOFF_ACTUAL") 
	    public Object cAT_AT_SIGNOFF_ACTUAL;
	    @JsonProperty("SOA_PUSH_DATE") 
	    public Object sOA_PUSH_DATE;
	    @JsonProperty("SACFA_APPLIED_DATE") 
	    public Object sACFA_APPLIED_DATE;
	    @JsonProperty("SOFT_AT_ACCEPTANCE_DATE") 
	    public Object sOFT_AT_ACCEPTANCE_DATE;
	    @JsonProperty("DEVIATION_APPROVAL_MAIL_RX_DATE") 
	    public Object dEVIATION_APPROVAL_MAIL_RX_DATE;
	    @JsonProperty("TXN_CATEGORY") 
	    public Object tXN_CATEGORY;
	    @JsonProperty("RMO_NO") 
	    public Object rMO_NO;
	    @JsonProperty("RMO_DATE") 
	    public Object rMO_DATE;
	    @JsonProperty("MATERIAL_RETURNED") 
	    public Object mATERIAL_RETURNED;
	    @JsonProperty("SR_SP_Ageing") 
	    public Object sR_SP_Ageing;
	    @JsonProperty("SP_SO_Ageing") 
	    public Object sP_SO_Ageing;
	    @JsonProperty("S0_RFAI_Ageing") 
	    public Object s0_RFAI_Ageing;
	    @JsonProperty("OPEN_SR_Ageing") 
	    public Object oPEN_SR_Ageing;
	    @JsonProperty("MO_RFAI_Ageing") 
	    public Object mO_RFAI_Ageing;
	    @JsonProperty("MO_MD_Ageing") 
	    public Object mO_MD_Ageing;
	    @JsonProperty("MOS_MD_Ageing") 
	    public Object mOS_MD_Ageing;
	    @JsonProperty("IC_Completion_Ageing") 
	    public Object iC_Completion_Ageing;
	    @JsonProperty("SECTOR") 
	    public String sECTOR;
	    @JsonProperty("CELL_NAME") 
	    public String cELL_NAME;
	    @JsonProperty("Site_Name") 
	    public String site_Name;
	    @JsonProperty("Town_Name") 
	    public String town_Name;
	    @JsonProperty("Site_Address") 
	    public String site_Address;
	    @JsonProperty("Tehsil") 
	    public String tehsil;
	    @JsonProperty("District") 
	    public String district;
	    @JsonProperty("Zone") 
	    public String zone;
	    @JsonProperty("Tech_2G_4G") 
	    public String tech_2G_4G;
	    @JsonProperty("WPC_2G") 
	    public String wPC_2G;
	    @JsonProperty("WPC_FDD") 
	    public String wPC_FDD;
	    @JsonProperty("POP") 
	    public Object pOP;
	    @JsonProperty("PCM_Path_2G") 
	    public Object pCM_Path_2G;
	    @JsonProperty("VLAN_2G") 
	    public Object vLAN_2G;
	    @JsonProperty("VLAN_4G") 
	    public Object vLAN_4G;
	    @JsonProperty("CDD_Status") 
	    public Object cDD_Status;
	    @JsonProperty("VLAN_Tagging_2G") 
	    public Object vLAN_Tagging_2G;
	    @JsonProperty("VLAN_Tagging_4G") 
	    public Object vLAN_Tagging_4G;
	    @JsonProperty("Material_Request") 
	    public Object material_Request;
	    @JsonProperty("MO_Number") 
	    public Object mO_Number;
	    @JsonProperty("I_and_C_status") 
	    public Object i_and_C_status;
	    @JsonProperty("Installation_Remark") 
	    public Object installation_Remark;
	    @JsonProperty("Integration_Date_2G") 
	    public Object integration_Date_2G;
	    @JsonProperty("Integration_Remarks_2G") 
	    public Object integration_Remarks_2G;
	    @JsonProperty("EMF_date_2G") 
	    public Object eMF_date_2G;
	    @JsonProperty("EMF_Remarks_2G") 
	    public Object eMF_Remarks_2G;
	    @JsonProperty("SCFT_offered_date_2G") 
	    public Object sCFT_offered_date_2G;
	    @JsonProperty("SCFT_Date_2G") 
	    public Object sCFT_Date_2G;
	    @JsonProperty("SCFT_Remarks_2G") 
	    public Object sCFT_Remarks_2G;
	    @JsonProperty("On_Air_Date_2G") 
	    public Object on_Air_Date_2G;
	    @JsonProperty("On_Air_Remarks_2G") 
	    public String on_Air_Remarks_2G;
	    @JsonProperty("SoftAT_Status_2G") 
	    public Object softAT_Status_2G;
	    @JsonProperty("Integration_Date_FDD") 
	    public Object integration_Date_FDD;
	    @JsonProperty("Integration_Remarks_FDD") 
	    public Object integration_Remarks_FDD;
	    @JsonProperty("EMF_Date_FDD") 
	    public Object eMF_Date_FDD;
	    @JsonProperty("EMF_Remarks_FDD") 
	    public Object eMF_Remarks_FDD;
	    @JsonProperty("SCFT_offered_date_FDD") 
	    public Object sCFT_offered_date_FDD;
	    @JsonProperty("SCFT_Date_FDD") 
	    public Object sCFT_Date_FDD;
	    @JsonProperty("SCFT_remarks_FDD") 
	    public Object sCFT_remarks_FDD;
	    @JsonProperty("On_AirDate_FDD") 
	    public Object on_AirDate_FDD;
	    @JsonProperty("On_Air_Remarks_FDD") 
	    public String on_Air_Remarks_FDD;
	    @JsonProperty("Soft_AT_Status_FDD") 
	    public Object soft_AT_Status_FDD;
	    @JsonProperty("FDD_2G_Integrated_Date") 
	    public Object fDD_2G_Integrated_Date;
	    @JsonProperty("FDD_2G_EMF_Date") 
	    public Object fDD_2G_EMF_Date;
	    @JsonProperty("FDD_2G_On_Aired_Date") 
	    public Object fDD_2G_On_Aired_Date;
	    @JsonProperty("Final_Installation_Status") 
	    public Object final_Installation_Status;
	    @JsonProperty("Final_Remarks") 
	    public Object final_Remarks;
	    @JsonProperty("Major_Category") 
	    public Object major_Category;
	    @JsonProperty("Responsibility") 
	    public Object responsibility;
	    @JsonProperty("L900_date") 
	    public Object l900_date;
	    @JsonProperty("FME_Name") 
	    public Object fME_Name;
	    @JsonProperty("ASP_Name") 
	    public Object aSP_Name;
	    @JsonProperty("ASP_PO_Number") 
	    public Object aSP_PO_Number;
	    @JsonProperty("ASP_PO_Date") 
	    public Object aSP_PO_Date;
	    @JsonProperty("WCC_Submission_Date") 
	    public Object wCC_Submission_Date;
	    @JsonProperty("Vendor_GRN_Number") 
	    public Object vendor_GRN_Number;
	    @JsonProperty("Vendor_GRN_Date") 
	    public Object vendor_GRN_Date;
	    @JsonProperty("DT_Vendor_Name") 
	    public Object dT_Vendor_Name;
	    @JsonProperty("DT_Vendor_PO_Number") 
	    public Object dT_Vendor_PO_Number;
	    @JsonProperty("DT_Vendor_PO_Date") 
	    public Object dT_Vendor_PO_Date;
	    @JsonProperty("DT_Vendor_WCC_Submission_Date") 
	    public Object dT_Vendor_WCC_Submission_Date;
	    @JsonProperty("DT_Vendor_GRN_Number") 
	    public Object dT_Vendor_GRN_Number;
	    @JsonProperty("DT_Vendor_GRN_Date") 
	    public Object dT_Vendor_GRN_Date;
	    @JsonProperty("Customer_PO_Number") 
	    public Object customer_PO_Number;
	    @JsonProperty("Customer_PO_Date") 
	    public Object customer_PO_Date;
	    @JsonProperty("OSS_Snapshot_Signoff_Actual_End_Date") 
	    public Object oSS_Snapshot_Signoff_Actual_End_Date;
	    @JsonProperty("MS1_Invoie_date") 
	    public Object mS1_Invoie_date;
	    @JsonProperty("MS2_Acceptance_Date") 
	    public Object mS2_Acceptance_Date;
	    @JsonProperty("MS2_invoice_date") 
	    public Object mS2_invoice_date;
	    @JsonProperty("CAT_AT_Signoff_Actual_End_Date") 
	    public Object cAT_AT_Signoff_Actual_End_Date;
	    @JsonProperty("CAT_AT_Invoice_date") 
	    public Object cAT_AT_Invoice_date;
	    @JsonProperty("SCFT_Report_Acceptance_Actual_End_Date") 
	    public Object sCFT_Report_Acceptance_Actual_End_Date;
	    @JsonProperty("CAT_Report_Acceptance_Actual_End_Date") 
	    public Object cAT_Report_Acceptance_Actual_End_Date;
	    @JsonProperty("WCC_submitted_NDO_SCFT") 
	    public Object wCC_submitted_NDO_SCFT;
	    @JsonProperty("WCC_approved_NDO_SCFT") 
	    public Object wCC_approved_NDO_SCFT;
	    public String created_on;
	    public String updated_on;
	    public String _etag;
	    


}
