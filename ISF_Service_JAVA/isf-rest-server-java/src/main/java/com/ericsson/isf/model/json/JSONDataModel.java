package com.ericsson.isf.model.json;

import java.util.List;
import java.util.Map;

public class JSONDataModel {
	private List<Scanner> scanners;
	private List<Match> matches;
	private List<Source> sources;
	private List<Condition> conditions;
	private Connector connector;
	private ConnectorNotification notification;
	private Map<Source, List<Target>> map;
	private Map<String, List<String>> childMap;
	private List<String> keyList;
	public Map<String, List<String>> getChildMap() {
		return childMap;
	}
	public void setChildMap(Map<String, List<String>> childMap) {
		this.childMap = childMap;
	}
	public List<String> getKeyList() {
		return keyList;
	}
	public void setKeyList(List<String> keyList) {
		this.keyList = keyList;
	}
	public List<Scanner> getScanners() {
		return scanners;
	}
	public void setScanners(List<Scanner> scanners) {
		this.scanners = scanners;
	}
	public List<Match> getMatches() {
		return matches;
	}
	public void setMatches(List<Match> matches) {
		this.matches = matches;
	}
	public List<Source> getSources() {
		return sources;
	}
	public void setSources(List<Source> sources) {
		this.sources = sources;
	}
	public List<Condition> getConditions() {
		return conditions;
	}
	public void setConditions(List<Condition> conditions) {
		this.conditions = conditions;
	}
	public Connector getConnector() {
		return connector;
	}
	public void setConnector(Connector connector) {
		this.connector = connector;
	}
	public ConnectorNotification getNotification() {
		return notification;
	}
	public void setNotification(ConnectorNotification notification) {
		this.notification = notification;
	}
	public void setMap(Map<Source, List<Target>> map) {
		this.map=map;
		
	}
	public Map<Source, List<Target>> getMap() {
		return map;
	}

	
}
