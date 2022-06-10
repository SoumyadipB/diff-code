package com.ericsson.isf.model;

public class CountryModel {
	private int CountryID;
	private String CountryName;
	private String Description;
	private int MarketAreaID;

	public int getCountryID() {
		return CountryID;
	}
	public void setCountryID(int countryID) {
		CountryID = countryID;
	}
	public String getCountryName() {
		return CountryName;
	}
	public void setCountryName(String countryName) {
		CountryName = countryName;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public int getMarketAreaID() {
		return MarketAreaID;
	}
	public void setMarketAreaID(int marketAreaID) {
		MarketAreaID = marketAreaID;
	}
}
