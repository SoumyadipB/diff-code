package com.ericsson.isf.model;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

/**
 * The Class DataTableRequest.
 *
 * @author edhhklu
 */
public class DataTableRequest {
	
	
	
	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getDraw() {
		return draw;
	}

	public void setDraw(String draw) {
		this.draw = draw;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public boolean isRegex() {
		return regex;
	}

	public void setRegex(boolean regex) {
		this.regex = regex;
	}

	public List<DataTableColumnSpecs> getColumns() {
		return columns;
	}

	public void setColumns(List<DataTableColumnSpecs> columns) {
		this.columns = columns;
	}

	public DataTableColumnSpecs getOrder() {
		return order;
	}

	public void setOrder(DataTableColumnSpecs order) {
		this.order = order;
	}

	public boolean isGlobalSearch() {
		return isGlobalSearch;
	}

	public void setGlobalSearch(boolean isGlobalSearch) {
		this.isGlobalSearch = isGlobalSearch;
	}

	public int getMaxParamsToCheck() {
		return maxParamsToCheck;
	}

	public void setMaxParamsToCheck(int maxParamsToCheck) {
		this.maxParamsToCheck = maxParamsToCheck;
	}
	
	private Integer recordsTotal;
	

	public Integer getRecordsTotal() {
		return recordsTotal;
	}

	public void setRecordsTotal(Integer recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	/** The unique id. */
	private String uniqueId;
	
	/** The draw. */
	private String draw;
	
	/** The start. */
	private Integer start;
	
	/** The length. */
	private Integer length;
	
	/** The search. */
	private String search;
	
	/** The regex. */
	private boolean regex;

	/** The columns. */
	private List<DataTableColumnSpecs> columns;
	
	/** The order. */
	private DataTableColumnSpecs order;
	
	/** The is global search. */
	private boolean isGlobalSearch;

	/**
	 * Instantiates a new data table request.
	 *
	 * @param request the request
	 */
	public DataTableRequest(HttpServletRequest request) {
		prepareDataTableRequest(request);
	}

	/**
	 * Prepare data table request.
	 *
	 * @param request the request
	 */
	private void prepareDataTableRequest(HttpServletRequest request) {
		
		Enumeration<String> parameterNames = request.getParameterNames();
    	
    	if(parameterNames.hasMoreElements()) {
    		if(request.getParameter("recordsTotal")!=null && !"".equals(request.getParameter("recordsTotal"))){
    			this.setRecordsTotal(Integer.parseInt(request.getParameter("recordsTotal")));
    		}
    		this.setStart(Integer.parseInt(request.getParameter("start")));
    		this.setLength(Integer.parseInt(request.getParameter("length")));
    		this.setUniqueId(request.getParameter("_"));
    		this.setDraw(request.getParameter("draw"));
    		String search=request.getParameter("search[value]");
    		if(!"".equals(search)){
    			this.setSearch('%'+search+'%');
    		}
    		this.setRegex(Boolean.valueOf(request.getParameter("search[regex]")));
    		
    		int sortableCol = Integer.parseInt(request.getParameter("order[0][column]"));
    		
    		List<DataTableColumnSpecs> columns = new ArrayList<DataTableColumnSpecs>();
    		
    		if(!(this.getSearch()==null ||"".equals(this.getSearch()))) {
    			this.setGlobalSearch(true);
    		}
    		
    		maxParamsToCheck = getNumberOfColumns(request);
    		
    		for(int i=0; i < maxParamsToCheck; i++) {
    			if(null != request.getParameter("columns["+ i +"][data]") 
    					&& !"null".equalsIgnoreCase(request.getParameter("columns["+ i +"][data]"))  
    					&& (!(request.getParameter("columns["+ i +"][data]")==null) || ("".equals(request.getParameter("columns["+ i +"][data]"))))) {
    				DataTableColumnSpecs colSpec = new DataTableColumnSpecs(request, i);
    				if(i == sortableCol) {
    					this.setOrder(colSpec);
    				}
    				
    				if("".equals(colSpec.getData()) || "0".equals(colSpec.getData())){
    					continue;
    				}
    				columns.add(colSpec);
    				
    				if(!(colSpec.getSearch()==null || "".equals(colSpec.getSearch()))) {
    					this.setGlobalSearch(false);
    				}
    			} 
    		}
    		
    		if(!(columns==null ||"".equals(columns))) {
    			this.setColumns(columns);
    		}
    	}
	}
	
	private int getNumberOfColumns(HttpServletRequest request) {
		Pattern p = Pattern.compile("columns\\[[0-9]+\\]\\[data\\]");  
		@SuppressWarnings("rawtypes")
		Enumeration params = request.getParameterNames(); 
		List<String> lstOfParams = new ArrayList<String>();
		while(params.hasMoreElements()){		
		 String paramName = (String)params.nextElement();
		 Matcher m = p.matcher(paramName);
		 if(m.matches())	{
			 lstOfParams.add(paramName);
		 }
		}
		return lstOfParams.size();
	}
	
	/**
	 * Gets the pagination request.
	 *
	 * @return the pagination request
	 */
	
	
	/** The max params to check. */
	private int maxParamsToCheck = 0;
	
}