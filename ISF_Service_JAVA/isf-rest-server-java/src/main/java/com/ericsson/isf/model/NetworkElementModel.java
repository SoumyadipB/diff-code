/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.List;

/**
 *
 * @author esanpup
 */
public class NetworkElementModel {
    
    private int networkElementID;
    private int projectID;
    private int domainSubDomainID;
    private int technologyID;
    private int vendorID;
    private String elementType;
    private String type;
    private String name;
    private String latitude;
    private String longitude;
    private String Software_Release;
    private String market;
    private List<TechnologyModel> technologyDetails;
    private List<DomainModel> domainDetails;
    private List<VendorModel> vendorDetails;
    private String uploadedON;
    private String uploadedBy;
    private String band;
    private String requestedDomainID;
    private String requestedTechnologyID;
    private String requestedVendorID;
    private int recordsTotal;
    private String sector;

  

	public int getRecordsTotal() {
		return recordsTotal;
	}

	public void setRecordsTotal(int recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	public String getBand() {
		return band;
	}

	public void setBand(String band) {
		this.band = band;
	}

	public String getUploadedON() {
		return uploadedON;
	}

	public void setUploadedON(String uploadedON) {
		this.uploadedON = uploadedON;
	}

	public String getUploadedBy() {
		return uploadedBy;
	}

	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

	public int getNetworkElementID() {
        return networkElementID;
    }

    public void setNetworkElementID(int networkElementID) {
        this.networkElementID = networkElementID;
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public int getDomainSubDomainID() {
        return domainSubDomainID;
    }

    public void setDomainSubDomainID(int domainSubDomainID) {
        this.domainSubDomainID = domainSubDomainID;
    }

    public int getTechnologyID() {
        return technologyID;
    }

    public void setTechnologyID(int technologyID) {
        this.technologyID = technologyID;
    }

    public int getVendorID() {
        return vendorID;
    }

    public void setVendorID(int vendorID) {
        this.vendorID = vendorID;
    }

    public String getElementType() {
        return elementType;
    }

    public void setElementType(String elementType) {
        this.elementType = elementType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }


    public String getSoftware_Release() {
		return Software_Release;
	}

	public void setSoftware_Release(String software_Release) {
		Software_Release = software_Release;
	}

	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public List<TechnologyModel> getTechnologyDetails() {
        return technologyDetails;
    }

    public void setTechnologyDetails(List<TechnologyModel> technologyDetails) {
        this.technologyDetails = technologyDetails;
    }

    public List<DomainModel> getDomainDetails() {
        return domainDetails;
    }

    public void setDomainDetails(List<DomainModel> domainDetails) {
        this.domainDetails = domainDetails;
    }

    public List<VendorModel> getVendorDetails() {
        return vendorDetails;
    }

    public void setVendorDetails(List<VendorModel> vendorDetails) {
        this.vendorDetails = vendorDetails;
    }
    
	public String getRequestedDomainID() {
		return requestedDomainID;
	}

	public void setRequestedDomainID(String requestedDomainID) {
		this.requestedDomainID = requestedDomainID;
	}

	public String getRequestedTechnologyID() {
		return requestedTechnologyID;
	}

	public void setRequestedTechnologyID(String requestedTechnologyID) {
		this.requestedTechnologyID = requestedTechnologyID;
	}

	public String getRequestedVendorID() {
		return requestedVendorID;
	}

	public void setRequestedVendorID(String requestedVendorID) {
		this.requestedVendorID = requestedVendorID;
	}

	@Override
    public String toString() {
        return "NetworkElementModel{" + "networkElementID=" + networkElementID + ", projectID=" + projectID + ", domainSubDomainID=" + domainSubDomainID + ", technologyID=" + technologyID + ", vendorID=" + vendorID + ", elementType=" + elementType + ", type=" + type + ", name=" + name + ", latitude=" + latitude + ", longitude=" + longitude + ", softwareRelease=" + Software_Release + ", market=" + market + ", technologyDetails=" + technologyDetails + ", domainDetails=" + domainDetails + ", vendorDetails=" + vendorDetails + '}';
    }
    
    

        
}
