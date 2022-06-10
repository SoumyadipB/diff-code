/**
 * 
 */
package com.ericsson.isf.constants;


public enum EnvironmentVariable {
	    LOGDIRECTORY("logDirectory"),
	    FILEPATTERN("filePattern"),
	    OPERATION("operation"),
	    TARGETDIRECTORY("targetDirectory"),
	    RETENTIONTIMEINDAYS("retentionTimeInDays"),
	    MOVE("move"),
	    DELETE("delete"),
	    CONVERTEDFILEFORMAT("convertedFileFormat"),
	    DOUBLESLASH("\\"),
	    REMOVESPECIALCHARACTER("[^a-zA-Z0-9]"),
	    CONSTANT("file"),
	    PROPERTIESFILENAME("\\directoryConfig.properties");
	    private String propertyKey;
	 
	    EnvironmentVariable(String propertyKey) {
	        this.propertyKey = propertyKey;
	    }

		public String getPropertyKey() {
			return propertyKey;
		}

		
	 
	    
}
