package com.ericsson.isf.model;

public class DesktopInformationModel {
	private String userWiseInstalledVersion;
	private String currentDesktopVersion;
	private boolean status;
	private String signumId;

	public String getUserWiseInstalledVersion() {
		return userWiseInstalledVersion;
	}

	public void setUserWiseInstalledVersion(String userWiseInstalledVersion) {
		this.userWiseInstalledVersion = userWiseInstalledVersion;
	}

	public String getCurrentDesktopVersion() {
		return currentDesktopVersion;
	}

	public void setCurrentDesktopVersion(String currentDesktopVersion) {
		this.currentDesktopVersion = currentDesktopVersion;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getSignumId() {
		return signumId;
	}

	public void setSignumId(String signumId) {
		this.signumId = signumId;
	}

}
