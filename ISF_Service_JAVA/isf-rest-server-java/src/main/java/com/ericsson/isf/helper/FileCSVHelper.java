package com.ericsson.isf.helper;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.cellprocessor.CellProcessorAdaptor;

import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseDouble;

import org.supercsv.cellprocessor.constraint.IsIncludedIn;
import org.supercsv.cellprocessor.constraint.StrMinMax;
import org.supercsv.cellprocessor.constraint.StrNotNullOrEmpty;
import org.supercsv.cellprocessor.constraint.StrRegEx;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.exception.SuperCsvConstraintViolationException;
import org.supercsv.exception.SuperCsvException;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.ICsvListReader;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.util.CsvContext;
import com.ericsson.isf.util.ApplicationMessages;

/**
 * @author edhhklu
 * Class to includes utilities for various csv related operations
 */



public class FileCSVHelper {
	private static final Logger LOG = LoggerFactory.getLogger(FileCSVHelper.class);
	private static final String INVALID_TEMPLATE = "Invalid file template. Please read 'Excel Entry Guidelines' and try again!";

	
	private static final Set<Object> COMPETENCE_VALID_COMPETENNCE_TYPES = 	new HashSet<Object>() {{
																									    add("DOMAIN COMPETENCE");
																									    add("NICHE AREA COMPETENCE");
																									    add("TOOLS COMPETENCE");
																										    
																									}};
	
	private static final Set<Object> NETWORK_ELEMENTS_VALID_ELEMENT_TYPES = 	new HashSet<Object>() {{
																				    add("CLUSTER");
																				    add("SITE");
																				    add("NODE");
																					    
																				}};
	
	
	private static final Set<Object> COMPETENCE_VALID_DOMAIN = 	new HashSet<Object>() {{
																				    add("BSS");
																				    add("IP & Core");
																				    add("Media and Applications");
																				    add("Radio Access Networks");
																				    add("OSS");
																				    add("Transport");
																					    
																				}};
																				
	private static final Set<Object> COMPETENCE_VALID_VENDOR = 	new HashSet<Object>() {{ addAll(Arrays.asList(new Object[] {"ALU",
																							"Avocent",
																							"Ceragon",
																							"Ericsson",
																							"Extreme",
																							"F5 network",
																							"Huawei",
																							"Juniper",
																							"Multivendor",
																							"NEC",
																							"Nortel",
																							"NSN",
																							"SSR",
																							"ZTE",
																							"Nokia",
																							"Samsung"
																					}));}};
																					
	private static final Set<Object> COMPETENCE_VALID_TECHNOLOGY = 	new HashSet<Object>() {{ addAll(Arrays.asList(new Object[] {"5G",
																																"CDMA",
																																"EVDO",
																																"GSM",
																																"IMS",
																																"IoT",
																																"IP",
																																"LTE",
																																"Microwave",
																																"Mobile Switching",
																																"Multi",
																																"Optical",
																																"Packet Core",
																																"SDN & IP Networking",
																																"User Data Management",
																																"WCDMA",
																																"WIFI",
																																"WIMAX",
																																"Wireline Switching",
																																"VOLTE"
																									}));}};
	
																				
	private static final String [] NETWORK_ELEMENTS_FILE_VALID_HEADER={
																			"Market", 
																			"Domain/Sub-Domain",
																			"Technology",
																			"Vendor",
																			"Network Element Type",
																			"Network Sub-Element Type",
																			"Network Element Name/ID",
																			"Latitude",
																			"Longitude"
																		};
	private static final String [] COMPETENCE_FILE_VALID_HEADER={
																			"Competence Type",
																			"Domain",
																			"SubDomain",
																			"Vendor",
																			"Technology",
																			"Competence ServiceArea",
																			"Competency Grade"
																		};
	
	private static final String [] MASTERDATA_FILE_VALID_HEADER={"S.No",
																	"Id",
																	"WBL Name",
																	"Technology",
																	"Training type",
																	"Competency",
																	"Competency Upgrade",
																	"Domain",
																	"SubDomain"
																};
	
	
	private static final String [] ITM_FILE_VALID_HEADER={"USER_ID",
															"SIGNUM",
															"LNAME",
															"FNAME",
															"ORG_DESC",
															"ORG_ID",
															"ITEM_TYPE",
															"ITEM_ID",
															"ITEM_TITLE",
															"CMPL_STAT_ID",
															"COMPL_DTE",
															"GEO_REGION",
															"COUNTRY",
															"LEGAL_UNIT",
															"FUNCTIONAL_AREA",
															"JOB_ROLE_FAMILY",
															"STUD_EMAIL_ADDRESS",
															"JOB_ROLE",
															"REGION",
															"COST_CENTRE",
															"TOTAL_HRS",
															"GRADE"
															};
	
	private static final String [] BULK_MSS_LEAVE_VALID_HEADER = {"Personnel Number",
																	"Name",
																	"Workdate",
																	"Clock in",
																	"Clock out",
																	"Hours",
																	"Attendance/Absence Type",
																	"Attendance/Absence Type Text",
																	"Network ID",
																	"Receiving Order",
																	"Network ID/ Receiving Order description",
																	"Activity",
																	"Activity Type",
																	"Sender Cost Center",
																	"Short Text",
																	"Line Manager Approval Status & Text",
																	"Line Manager Rejection Reason",
																	"Line Manager Approval Date",
																	"Project Manager Approval Status & Text",
																	"Project Manager Rejection Reason",
																	"Project Manager Approver",
																	"Project Manager Approval Date"};

	private static final String NO_SPACE_REGEX="^\\s*\\S.*$";
	private static final String NODE_NAME_SPACE_ERROR="Name cannot be NULL";
	private static final String HEADER_ERR="header_err";
	private static final String MISC_ERR="misc_err";
	
	/**
	 * This method validates the file uploaded by user for network elements bulk insert. 
	 * Validations include headers validation and values in different columns against predefined set of constraints
	 * for individual elements
	 */
	public static Map<String, String> validateNetworkElementsCsvFile(String filename){
		Map<String,String> messagesCauseMap = new LinkedHashMap<>();
		final CsvPreference comma = new CsvPreference.Builder('"', '\t', "\n").build();
        try(ICsvListReader beanReader = new CsvListReader(new FileReader(filename), CsvPreference.STANDARD_PREFERENCE)) {
                final String[] header = beanReader.getHeader(true);
                
                String headerValidation=validateHeader(NETWORK_ELEMENTS_FILE_VALID_HEADER,header);
	                if(headerValidation!=null){
	                	messagesCauseMap.put(MISC_ERR,headerValidation);
	                	return messagesCauseMap;
	                }
               
                	final CellProcessor[] processors = getValidationProcessorsForNetworElFile(header);
                	while(true) {
                		 try{
	                		if(beanReader.read(processors) == null){
	                			break;
	                		} 
                		 }catch(SuperCsvCellProcessorException e){
                        	processSuperCsvDataException(messagesCauseMap,e);
                        	
                        } catch(SuperCsvException e){
                        	messagesCauseMap.put(HEADER_ERR,ApplicationMessages.ERROR_NETWORK_ELEMENTS_HEADER +" Row no:"+e.getCsvContext().getRowNumber());
                        }
                		//no need to process file as it is just for validation of file
                	}
               
                
        }catch (Exception e) {
        	e.printStackTrace();
        	messagesCauseMap.put(MISC_ERR,e.getMessage());
		}
        
        if(!messagesCauseMap.keySet().isEmpty()){
        	messagesCauseMap.put(MISC_ERR+1, "Note: Only first occurrence of an error is shown above.\nPlease check entire file for similar errors.");
        }
        return messagesCauseMap;
	}
	
	//test method 
	public static Map<String, String> validateNetworkElementsExcelFile(String filename){
		Map<String,String> messagesCauseMap = new LinkedHashMap<>();
        try(ICsvListReader beanReader = new CsvListReader(new FileReader(filename), CsvPreference.EXCEL_PREFERENCE)) {
                final String[] header = beanReader.getHeader(true);
                String headerValidation=validateHeader(NETWORK_ELEMENTS_FILE_VALID_HEADER,header);
	                if(headerValidation!=null){
	                	messagesCauseMap.put(MISC_ERR,headerValidation);
	                	return messagesCauseMap;
	                }
               
                	final CellProcessor[] processors = getValidationProcessorsForNetworElFile(header);
                	while(true) {
                		 try{
	                		if(beanReader.read(processors) == null){
	                			break;
	                		} 
                		 }catch(SuperCsvCellProcessorException e){
                        	processSuperCsvDataException(messagesCauseMap,e);
                        	
                        } catch(SuperCsvException e){
                        	messagesCauseMap.put(HEADER_ERR,ApplicationMessages.ERROR_NETWORK_ELEMENTS_HEADER +" Row no:"+e.getCsvContext().getRowNumber());
                        }
                		//no need to process file as it is just for validation of file
                	}
               
                
        }catch (Exception e) {
        	e.printStackTrace();
        	messagesCauseMap.put(MISC_ERR,e.getMessage());
		}
        
        if(messagesCauseMap.keySet().size()>0){
        	messagesCauseMap.put(MISC_ERR+1, "Note: Only first occurrence of an error is shown above.\nPlease check entire file for similar errors.");
        }
        return messagesCauseMap;
	}
	
	private static void processSuperCsvDataException(Map<String,String> messagesCauseMap,SuperCsvCellProcessorException e){
		String errorProcessorType=e.getProcessor().getClass().toString();
		if(messagesCauseMap.get(errorProcessorType)==null){
			//first time this type of error occured
			messagesCauseMap.put(errorProcessorType, e.getMessage() +"\n  First occurrence - Row no:"+ e.getCsvContext().getRowNumber() +" Column no:"+e.getCsvContext().getColumnNumber());
		}
		
		//.getClass()//e.getProcessor()
    	//return e.getMessage() +"\n"+e.getCsvContext();
	}
	
	/**
	 * This method creates validators for each columns for network elements file
	 * @param header 
	 */
	private static CellProcessor[] getValidationProcessorsForNetworElFile(String[] header){
		
        StrRegEx.registerMessage(NO_SPACE_REGEX,NODE_NAME_SPACE_ERROR);
        CellProcessor[] processors = new CellProcessor[header.length]; 
       
        processors[0]	=	new StrNotNullOrEmpty(); 
        processors[1]	=	new StrNotNullOrEmpty();
    	processors[2]	=  	new StrNotNullOrEmpty(); 
    	processors[3]	=  	new StrNotNullOrEmpty();
    	processors[4]	=  	new StrNotNullOrEmpty();
    	processors[5]	=  	new StrNotNullOrEmpty();
    	processors[6]	=	new StrMinMax(1,100,new StrRegEx(NO_SPACE_REGEX));
    	processors[7]	=	new Optional(new ParseDouble());
    	processors[8]	=	new Optional(new ParseDouble());
    	

    	return processors;
	}
	
	private static CellProcessor[] getValidationProcessorsForCompetenceFile(String[] header, ArrayList<Set<Object>> filters){
        CellProcessor[] processors = new CellProcessor[header.length]; 
        processors[0]	=	new IsIncludedInCaseInsestive(filters.get(4));
    	processors[1]	=  	new IsIncludedInCaseInsestive(filters.get(2)); 
    	processors[2]	=	new IsIncludedInCaseInsestive(filters.get(3));
    	processors[3]	=   new IsIncludedInCaseInsestive(filters.get(0));
    	processors[4]	=	new IsIncludedInCaseInsestive(filters.get(1));
    	processors[5]	=	new StrNotNullOrEmpty();
    	processors[6]	=	new StrNotNullOrEmpty();;

    	return processors;
	}
	
	
	public static Map<String, String> validateCompetenceDataFile(String filename, ArrayList<Set<Object>> filters){
		Map<String,String> messagesCauseMap = new LinkedHashMap<>();
        try(ICsvListReader beanReader = new CsvListReader(new FileReader(filename), CsvPreference.TAB_PREFERENCE)) {
                final String[] header = beanReader.getHeader(true);
                String headerValidation=validateHeader(COMPETENCE_FILE_VALID_HEADER,header);
	                if(headerValidation!=null){
	                	messagesCauseMap.put(MISC_ERR,headerValidation);
	                	return messagesCauseMap;
	                }
	                
                	final CellProcessor[] processors = getValidationProcessorsForCompetenceFile(header, filters);
                	while(true) {
                		 try{
	                		if(beanReader.read(processors) == null){
	                			break;
	                		} 
                		 }catch(SuperCsvCellProcessorException e){
                        	processSuperCsvDataException(messagesCauseMap,e);
                        	
                        } catch(SuperCsvException e){
                        	messagesCauseMap.put(HEADER_ERR,ApplicationMessages.ERROR_NETWORK_ELEMENTS_HEADER +" Row no:"+e.getCsvContext().getRowNumber());
                        }
                		//no need to process file as it is just for validation of file
                	}
               
                
        }catch (Exception e) {
        	e.printStackTrace();
        	messagesCauseMap.put(MISC_ERR,e.getMessage());
		}
        
        if(messagesCauseMap.keySet().size()>0){
        	messagesCauseMap.put(MISC_ERR+1, "Note: Only first occurrence of an error is shown above.\nPlease check entire file for similar errors.");
        }
        return messagesCauseMap;
	}
	
	public static Map<String, String> validateMasterDataFile(String filename){
		Map<String,String> messagesCauseMap = new LinkedHashMap<>();
        try(ICsvListReader beanReader = new CsvListReader(new FileReader(filename), CsvPreference.TAB_PREFERENCE)) {
                final String[] header = beanReader.getHeader(true);
                String headerValidation=validateHeader(MASTERDATA_FILE_VALID_HEADER,header);
	                if(headerValidation!=null){
	                	messagesCauseMap.put(MISC_ERR,headerValidation);
	                	return messagesCauseMap;
	                }
               
                
        }catch (Exception e) {
        	e.printStackTrace();
        	messagesCauseMap.put(MISC_ERR,e.getMessage());
		}
        
        if(messagesCauseMap.keySet().size()>0){
        	messagesCauseMap.put(MISC_ERR+1, "Note: Only first occurrence of an error is shown above.\nPlease check entire file for similar errors.");
        }
        return messagesCauseMap;
	}
	
	public static Map<String, String> validateITMDataFile(String filename){
		Map<String,String> messagesCauseMap = new LinkedHashMap<>();
        try(ICsvListReader beanReader = new CsvListReader(new FileReader(filename), CsvPreference.TAB_PREFERENCE)) {
                final String[] header = beanReader.getHeader(true);
                String headerValidation=validateHeader(ITM_FILE_VALID_HEADER,header);
	                if(headerValidation!=null){
	                	messagesCauseMap.put(MISC_ERR,headerValidation);
	                	return messagesCauseMap;
	                }
               
                
        }catch (Exception e) {
        	e.printStackTrace();
        	messagesCauseMap.put(MISC_ERR,e.getMessage());
		}
        
        if(messagesCauseMap.keySet().size()>0){
        	messagesCauseMap.put(MISC_ERR+1, "Note: Only first occurrence of an error is shown above.\nPlease check entire file for similar errors.");
        }
        return messagesCauseMap;
	}
	
	/**
	 * This method checks file-header against expectedHeader
	 * @return error String in case there is mismatch, if no error return null 
	 */
	private static String validateHeader(String[] expectedHeader,String []fileheader){
		String headerErr="";
		if(expectedHeader.length != fileheader.length) {
			return "Invalid file template";
		}
		for(int i=0;i<expectedHeader.length;i++){
			if(!expectedHeader[i].equalsIgnoreCase(fileheader[i])){
				headerErr+= "\nExpected: "+expectedHeader[i] + " ,found: " +fileheader[i];
			}
		}
		return ("".equals(headerErr))?null:"Mismatch in headers.\n"+headerErr;
	}
	
	
	/**
	 * @author edhhklu
	 * custom class created to add case insensitivity to IsIncludedIn validator of super CSV
	 *
	 */
	static class IsIncludedInCaseInsestive extends IsIncludedIn {
		private final Set<String> possibleValues = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		public IsIncludedInCaseInsestive(final Set<Object> possibleValues) {
			super(possibleValues);
			for(Object o:possibleValues){
				this.possibleValues.add(o.toString());
			}
		}
		
		public Object execute(final Object value, final CsvContext context) {
			validateInputNotNull(value, context);
			
			if( !possibleValues.contains(value) ) {
				throw new SuperCsvConstraintViolationException(String.format(
					"'%s' is not included in the allowed set of values", value), context, this);
			}
			
			return next.execute(value, context);
		}
	}
	
	/**
	 * @author edhhklu
	 * custom class created to add conditional mandatory validator of super CSV
	 *
	 */
	static class MandatoryIfOtherColumnEquals extends CellProcessorAdaptor  {

		  private final int column;
		  private final String constantValue;

		  public MandatoryIfOtherColumnEquals(int column,String constantValue) {
		    super();
		    this.column = column;
		    this.constantValue = constantValue;
		  }

		  @Override
		  public Object execute(Object value, CsvContext context) {

		    List<Object> row = context.getRowSource();
		    if(row.get(column - 1).toString().equalsIgnoreCase(constantValue) && (value==null || "".equals(value))) {
				throw new SuperCsvConstraintViolationException(String.format(
					"Column %s is mandatory for Column %s value: '%s'",context.getColumnNumber(), column,constantValue), context, this);
			}
		    return next.execute(value, context);
		}
	}

	/**
	 * This method validates the file uploaded by LM/PM for MSS Leave data bulk insert. 
	 * Validations include headers validation and values in different columns against predefined set of constraints
	 * for individual elements
	 *
	 * @author ekmbuma
	 *
	 * @param filename
	 * @return Map<String, String>
	 */
	public static Map<String, String> validateBulkMssLeaveFile(String filename){
		Map<String,String> messagesCauseMap = new LinkedHashMap<>();
		final CsvPreference comma = new CsvPreference.Builder('"', '\t', "\n").build();        
		try(ICsvListReader beanReader = new CsvListReader(new FileReader(filename), CsvPreference.STANDARD_PREFERENCE)) {
                final String[] header = beanReader.getHeader(true);
                
                if(header == null) {
                	messagesCauseMap.put(MISC_ERR,"Header in the uploaded file is blank");
                	return messagesCauseMap;
                }
                
                String headerValidation=validateHeader(BULK_MSS_LEAVE_VALID_HEADER,header);
	                if(headerValidation != null){
	                	messagesCauseMap.put(MISC_ERR,headerValidation);
	                	return messagesCauseMap;
	                }
               
                	final CellProcessor[] processors = getValidationProcessorsForBulkMssLeaveFile(header);
                	while(true) {
                		 try{
	                		if(beanReader.read(processors) == null){
	                			break;
	                		}
                		 } catch(SuperCsvCellProcessorException e){
                        	processSuperCsvDataException(messagesCauseMap,e);
                        	
                        } catch(SuperCsvException e){
                        	messagesCauseMap.put(HEADER_ERR,ApplicationMessages.BULK_MSS_LEAVE_UPLOAD_ERROR +" Row no:"+e.getCsvContext().getRowNumber());
                        	break;
                        }
                		//no need to process file as it is just for validation of file
                	}
                
        }catch (Exception e) {
        	e.printStackTrace();
        	messagesCauseMap.put(MISC_ERR,e.getMessage());
		}
        
        if(messagesCauseMap.keySet().size()>0){
        	messagesCauseMap.put(MISC_ERR+1, "Note: Only first occurrence of an error is shown above.\nPlease check entire file for similar errors.");
        }
        return messagesCauseMap;
	}

	/**
	 * This method creates validators for each columns for mss leave upload file
	 * 
	 * @author ekmbuma
	 * 
	 * 
	 * @param header
	 * @return CellProcessor[] 
	 */
	private static CellProcessor[] getValidationProcessorsForBulkMssLeaveFile(String[] header){
		
        StrRegEx.registerMessage(NO_SPACE_REGEX,NODE_NAME_SPACE_ERROR);
        CellProcessor[] processors = new CellProcessor[header.length]; 
        // To read about CellProcessors here is the link
        // https://super-csv.github.io/super-csv/apidocs/allclasses-noframe.html
       
        processors[0]	=	new StrNotNullOrEmpty();                                        // Personnel Number
        processors[1]	=	new Optional();                                                 // Name
    	processors[2]	=  	new StrNotNullOrEmpty();//FmtDate("MM/dd/YYYY");                                      // Workdate
    	processors[3]	=	new Optional();                                                 // Clock in
    	processors[4]	=   new Optional();                                                 // Clock out
    	processors[5]	=	new ParseDouble();                                              // Hours
    	processors[6]	=	new StrNotNullOrEmpty();                                        // Attendance/Absence Type
    	processors[7]	=	new StrNotNullOrEmpty();                                        // Attendance/Absence Type Text
    	processors[8]	=	new Optional();                                                 // Network ID
    	processors[9]	=	new Optional();
    	processors[10]	=	new Optional();                                                 // Network ID/ Receiving Order description
    	processors[11]	=	new Optional();                                                 // Activity
    	processors[12]	=	new Optional();                                                 // Activity Type
    	processors[13]	=	new Optional();                                                 // Sender Cost Center
    	processors[14]	=	new Optional();
    	processors[15]  =   new StrNotNullOrEmpty(); //StrRegEx("^[0-9][0-9][\\s][-][\\s][A-Z]*[a-z]*$");          //Line Manager Approval Status & Text
    	processors[16]	=	new Optional();
    	processors[17]	=	new Optional();                                                 // Line Manager Approval Date
    	processors[18]	=	new Optional();
    	processors[19]	=	new Optional();
    	processors[20]	=	new Optional();
    	processors[21]	=	new Optional();

    	return processors;
	}
	
	public static Map<String, String> validateCNEDBCsvFile(String filename){
		Map<String,String> messagesCauseMap = new LinkedHashMap<>();
		
        try(ICsvListReader beanReader = new CsvListReader(new FileReader(filename), CsvPreference.STANDARD_PREFERENCE)) {
                final String[] header = beanReader.getHeader(true);
                if(header==null) {
                	LOG.info("inside header============"+header);
                	messagesCauseMap.put(MISC_ERR,INVALID_TEMPLATE);
                }
                else {
                	LOG.info("inside else Header is============"+header);
                String headerValidation=validateHeader(NETWORK_ELEMENTS_FILE_VALID_HEADER,header);
                LOG.info("inside headerValidation============"+headerValidation);
	                if(headerValidation!=null){
	                	LOG.info("if headerValidation is null ============");
	                	messagesCauseMap.put(MISC_ERR,headerValidation);
	                	return messagesCauseMap;
	                }
                }
        }catch (Exception e) {
        	
        	LOG.error(e.getMessage());
        	messagesCauseMap.put(MISC_ERR,e.getMessage());
		}
       
        return messagesCauseMap;
	}

}
