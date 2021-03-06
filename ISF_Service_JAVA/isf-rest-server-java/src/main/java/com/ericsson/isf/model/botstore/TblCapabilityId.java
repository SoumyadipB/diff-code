package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * TblCapabilityId generated by hbm2java
 */
@Embeddable
public class TblCapabilityId implements java.io.Serializable {

	private int capabilityPageId;
	private String capabilityPageGroup;
	private String capabilityPageName;
	private Integer active;
	private String groupTitle;
	private String subMenuTitle;
	private String groupIcon;
	private String subMenuHref;
	private Boolean onClick;
	private String groupHref;
	private Integer priority;
	private Boolean groupOnClick;
	
	public TblCapabilityId() {
	}

	public TblCapabilityId(int capabilityPageId) {
		this.capabilityPageId = capabilityPageId;
	}

	public TblCapabilityId(int capabilityPageId, String capabilityPageGroup, String capabilityPageName, Integer active,
			String groupTitle, String subMenuTitle, String groupIcon, String subMenuHref, Boolean onClick,
			String groupHref, Integer priority, Boolean groupOnClick) {
		this.capabilityPageId = capabilityPageId;
		this.capabilityPageGroup = capabilityPageGroup;
		this.capabilityPageName = capabilityPageName;
		this.active = active;
		this.groupTitle = groupTitle;
		this.subMenuTitle = subMenuTitle;
		this.groupIcon = groupIcon;
		this.subMenuHref = subMenuHref;
		this.onClick = onClick;
		this.groupHref = groupHref;
		this.priority = priority;
		this.groupOnClick = groupOnClick;
	}

	@Column(name = "CapabilityPageID", nullable = false)
	public int getCapabilityPageId() {
		return this.capabilityPageId;
	}

	public void setCapabilityPageId(int capabilityPageId) {
		this.capabilityPageId = capabilityPageId;
	}

	@Column(name = "CapabilityPageGroup", length = 512)
	public String getCapabilityPageGroup() {
		return this.capabilityPageGroup;
	}

	public void setCapabilityPageGroup(String capabilityPageGroup) {
		this.capabilityPageGroup = capabilityPageGroup;
	}

	@Column(name = "CapabilityPageName", length = 512)
	public String getCapabilityPageName() {
		return this.capabilityPageName;
	}

	public void setCapabilityPageName(String capabilityPageName) {
		this.capabilityPageName = capabilityPageName;
	}

	@Column(name = "Active")
	public Integer getActive() {
		return this.active;
	}

	public void setActive(Integer active) {
		this.active = active;
	}

	@Column(name = "GroupTitle", length = 100)
	public String getGroupTitle() {
		return this.groupTitle;
	}

	public void setGroupTitle(String groupTitle) {
		this.groupTitle = groupTitle;
	}

	@Column(name = "SubMenuTitle", length = 100)
	public String getSubMenuTitle() {
		return this.subMenuTitle;
	}

	public void setSubMenuTitle(String subMenuTitle) {
		this.subMenuTitle = subMenuTitle;
	}

	@Column(name = "GroupIcon", length = 100)
	public String getGroupIcon() {
		return this.groupIcon;
	}

	public void setGroupIcon(String groupIcon) {
		this.groupIcon = groupIcon;
	}

	@Column(name = "SubMenuHref", length = 100)
	public String getSubMenuHref() {
		return this.subMenuHref;
	}

	public void setSubMenuHref(String subMenuHref) {
		this.subMenuHref = subMenuHref;
	}

	@Column(name = "OnClick")
	public Boolean getOnClick() {
		return this.onClick;
	}

	public void setOnClick(Boolean onClick) {
		this.onClick = onClick;
	}

	@Column(name = "GroupHref")
	public String getGroupHref() {
		return this.groupHref;
	}

	public void setGroupHref(String groupHref) {
		this.groupHref = groupHref;
	}

	@Column(name = "Priority")
	public Integer getPriority() {
		return this.priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	@Column(name = "GroupOnClick")
	public Boolean getGroupOnClick() {
		return this.groupOnClick;
	}

	public void setGroupOnClick(Boolean groupOnClick) {
		this.groupOnClick = groupOnClick;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TblCapabilityId))
			return false;
		TblCapabilityId castOther = (TblCapabilityId) other;

		return (this.getCapabilityPageId() == castOther.getCapabilityPageId())
				&& ((this.getCapabilityPageGroup() == castOther.getCapabilityPageGroup())
						|| (this.getCapabilityPageGroup() != null && castOther.getCapabilityPageGroup() != null
								&& this.getCapabilityPageGroup().equals(castOther.getCapabilityPageGroup())))
				&& ((this.getCapabilityPageName() == castOther.getCapabilityPageName())
						|| (this.getCapabilityPageName() != null && castOther.getCapabilityPageName() != null
								&& this.getCapabilityPageName().equals(castOther.getCapabilityPageName())))
				&& ((this.getActive() == castOther.getActive()) || (this.getActive() != null
						&& castOther.getActive() != null && this.getActive().equals(castOther.getActive())))
				&& ((this.getGroupTitle() == castOther.getGroupTitle()) || (this.getGroupTitle() != null
						&& castOther.getGroupTitle() != null && this.getGroupTitle().equals(castOther.getGroupTitle())))
				&& ((this.getSubMenuTitle() == castOther.getSubMenuTitle())
						|| (this.getSubMenuTitle() != null && castOther.getSubMenuTitle() != null
								&& this.getSubMenuTitle().equals(castOther.getSubMenuTitle())))
				&& ((this.getGroupIcon() == castOther.getGroupIcon()) || (this.getGroupIcon() != null
						&& castOther.getGroupIcon() != null && this.getGroupIcon().equals(castOther.getGroupIcon())))
				&& ((this.getSubMenuHref() == castOther.getSubMenuHref())
						|| (this.getSubMenuHref() != null && castOther.getSubMenuHref() != null
								&& this.getSubMenuHref().equals(castOther.getSubMenuHref())))
				&& ((this.getOnClick() == castOther.getOnClick()) || (this.getOnClick() != null
						&& castOther.getOnClick() != null && this.getOnClick().equals(castOther.getOnClick())))
				&& ((this.getGroupHref() == castOther.getGroupHref()) || (this.getGroupHref() != null
						&& castOther.getGroupHref() != null && this.getGroupHref().equals(castOther.getGroupHref())))
				&& ((this.getPriority() == castOther.getPriority()) || (this.getPriority() != null
						&& castOther.getPriority() != null && this.getPriority().equals(castOther.getPriority())))
				&& ((this.getGroupOnClick() == castOther.getGroupOnClick())
						|| (this.getGroupOnClick() != null && castOther.getGroupOnClick() != null
								&& this.getGroupOnClick().equals(castOther.getGroupOnClick())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getCapabilityPageId();
		result = 37 * result + (getCapabilityPageGroup() == null ? 0 : this.getCapabilityPageGroup().hashCode());
		result = 37 * result + (getCapabilityPageName() == null ? 0 : this.getCapabilityPageName().hashCode());
		result = 37 * result + (getActive() == null ? 0 : this.getActive().hashCode());
		result = 37 * result + (getGroupTitle() == null ? 0 : this.getGroupTitle().hashCode());
		result = 37 * result + (getSubMenuTitle() == null ? 0 : this.getSubMenuTitle().hashCode());
		result = 37 * result + (getGroupIcon() == null ? 0 : this.getGroupIcon().hashCode());
		result = 37 * result + (getSubMenuHref() == null ? 0 : this.getSubMenuHref().hashCode());
		result = 37 * result + (getOnClick() == null ? 0 : this.getOnClick().hashCode());
		result = 37 * result + (getGroupHref() == null ? 0 : this.getGroupHref().hashCode());
		result = 37 * result + (getPriority() == null ? 0 : this.getPriority().hashCode());
		result = 37 * result + (getGroupOnClick() == null ? 0 : this.getGroupOnClick().hashCode());
		return result;
	}

}
