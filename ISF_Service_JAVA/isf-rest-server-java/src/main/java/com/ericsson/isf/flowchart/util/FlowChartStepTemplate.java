/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.flowchart.util;

/**
 *
 * @author ekarath
 */
public class FlowChartStepTemplate {
    
    public static String OPERATION_STEP_JSON_WITH_TASK= "{\n" +
											            "\"type\":\"ericsson.Manual\",\n" +
											            "\"icons\":{\n" +
											            "\n" +
											            " },\n" +
											            "\"name\":\"%STEP_VALUE%\",\n" +
											            "\"outputUpload\":\"%OUTPUT_UPLOAD%\",\n" +
											            "\"position\":{\n" +
											            "\"x\":%POSITION_X%,\n" +
											            "\"y\":%POSITION_Y%\n" +
											            "},\n" +
											            "\"size\":{\n" +
											            "\"width\":210,\n" +
											            "\"height\":145\n" +
											            "},\n" +
											            "\"angle\":0,\n" +
											            "\"id\":\"%STEP_ID%\",\n" +
											            "\"z\":9,\n" +
											            "\"action\":\"%ACTION%\",\n" +
											            "\"tool\":\"%TOOL_DATA%\",\n" +  
											            "\"responsible\":\"%RESP%\",\n" + 
											            "\"attrs\":{\n" +
											            "\"body\":{\n" +
											            "\"stroke\":\"#31d0c6\",\n" +
											            "\"fill\":\"white\",\n" +
											            "\"strokeDasharray\":\"0\"\n" +
											            "},\n" +
											            "\"header\":{\n" +
											            "\"height\":20,\n" +
											            "\"stroke\":\"#31d0c6\",\n" +
											            "\"fill\":\"#31d0c6\",\n" +
											            "\"strokeDasharray\":\"0\"\n" +
											            "},\n" +
											            "\"headerText\":{\n" +
											            "\"refY\":10,\n" +
											            "\"fontSize\":15,\n" +
											            "\"fill\":\"#f6f6f6\",\n" +
											            "\"text\":\"Manual (%RESP%)\",\n" +
											            "\"fontFamily\":\"Roboto Condensed\",\n" +
											            "\"fontWeight\":\"Normal\",\n" +
											            "\"strokeWidth\":0\n" +
											            "},\n" +
											            "\"bodyText\":{\n" +
											            "\"fontSize\":11,\n" +
											            "\"text\":\"%STEP_NAME%\",\n" +
											            "\"fill\":\"black\",\n" +
											            "\"fontFamily\":\"Roboto Condensed\",\n" +
											            "\"fontWeight\":\"Normal\",\n" +
											            "\"strokeWidth\":0\n" +
											            "},\n" +
											            "\"footer\":{\n" +
											            "\"stroke\":\"#31d0c6\",\n" +
											            "\"fill\":\"#31d0c6\",\n" +
											            "\"strokeDasharray\":\"0\",\n" +
											            "\"refY\":\"100%\",\n" +
											            "\"refY2\":-30\n" +
											            "},\n" +
											            "\"root\":{\n" +
											            "\"dataTooltipPosition\":\"right\",\n" +
											            "\"dataTooltipPositionSelector\":\".joint-stencil\"\n" +
											            "},\n" +
											            "\"task\":{\n" +
											            "\"avgEstdEffort\":\"%AGV_ESTD_EFFORT%\",\n" +
											            "\"bookingID\":\"%BOOKING_ID%\",\n" +
											            "\"executionType\":\"Manual\",\n" +
											            "\"reason\":\"%REASON%\",\n" +
											            "\"status\":\"%STATUS%\",\n" +
											            "\"taskID\":\"%TASK_ID%\",\n" +
											            "\"taskName\":\"%TASK_NAME%\",\n" +
											            "\"toolID\":\"%TOOL_ID%\",\n" +
											            "\"toolName\":\"%TOOL_NAME%\"\n" +
											            "},\n" +
											            "\"tool\":{\n" +
											            "\"RPAID\":\"%RPATOOL_ID%\",\n" +
											            "\"RPAName\":\"%RPATOOL_NAME%\"\n" +
											            "}\n" +
											            "}\n" +
											            "}";

    public static String AUTOMATED_JSON_WITH_TASK = "{\r\n" + 
										    		"\"type\":\"ericsson.Automatic\",\r\n" + 
										    		"\"name\":\"%STEP_VALUE%\",\r\n" + 
										    		"\"outputUpload\":\"%OUTPUT_UPLOAD%\",\r\n" +
										    		"\"cascadeInput\":\"%CASCADE_INPUT%\",\r\n"+
										    		"\"position\":{\r\n" + 
										    		"\"x\":%POSITION_X%,\r\n" + 
										    		"\"y\":%POSITION_Y%\r\n" + 
										    		"},\r\n" + 
										    		"\"size\":{\r\n" + 
										    		"\"width\":210,\r\n" + 
										    		"\"height\":145\r\n" + 
										    		"},\r\n" + 
										    		"\"angle\":0,\r\n" + 
										    		"\"id\":\"%STEP_ID%\",\r\n" + 
										    		"\"z\":10,\r\n" + 
										    		"\"action\":\"%TASK_DATA%\",\r\n" + 
										    		"\"tool\":\"%TOOL_DATA%\",\r\n" + 
										    		"\"rpaid\":\"%RPA_DATA%\",\r\n" + 
										            "\"responsible\":\"%RESP%\",\n" + 										    		
										    		"\"attrs\":{\r\n" + 
										    		"\"body\":{\r\n" + 
										    		"\"stroke\":\"#f39b17\",\r\n" + 
										    		"\"fill\":\"white\",\r\n" + 
										    		"\"strokeDasharray\":\"0\"\r\n" + 
										    		"},\r\n" + 
										    		"\"header\":{\r\n" + 
										    		"\"height\":20,\r\n" + 
										    		"\"stroke\":\"#f39b17\",\r\n" + 
										    		"\"fill\":\"#f39b17\",\r\n" + 
										    		"\"strokeDasharray\":\"0\"\r\n" + 
										    		"},\r\n" + 
										    		"\"headerText\":{\r\n" + 
										    		"\"refY\":10,\r\n" + 
										    		"\"fontSize\":15,\r\n" + 
										    		"\"fill\":\"#f6f6f6\",\r\n" + 
										    		"\"text\":\"Automatic (%RESP%)\",\r\n" + 
										    		"\"fontFamily\":\"Roboto Condensed\",\r\n" + 
										    		"\"fontWeight\":\"Normal\",\r\n" + 
										    		"\"strokeWidth\":0\r\n" + 
										    		"},\r\n" + 
										    		"\"bodyText\":{\r\n" + 
										    		"\"refY2\":0,\r\n" + 
										    		"\"fontSize\":11,\r\n" + 
										    		"\"fill\":\"black\",\r\n" + 
										    		"\"text\":\"%STEP_NAME%\",\r\n" + 
										    		"\"fontFamily\":\"Roboto Condensed\",\r\n" + 
										    		"\"fontWeight\":\"Normal\",\r\n" + 
										    		"\"strokeWidth\":0\r\n" + 
										    		"},\r\n" + 
										    		"\"footer\":{\r\n" + 
										    		"\"stroke\":\"#f39b17\",\r\n" + 
										    		"\"fill\":\"#f39b17\",\r\n" + 
										    		"\"strokeDasharray\":\"0\",\r\n" + 
										    		"\"refY\":\"100%\",\r\n" + 
										    		"\"refY2\":-30\r\n" + 
										    		"},\r\n" + 
										    		"\"root\":{\r\n" + 
										    		"\"dataTooltipPosition\":\"left\",\r\n" + 
										    		"\"dataTooltipPositionSelector\":\".joint-stencil\"\r\n" + 
										    		"},\r\n" + 
										    		"\"task\":{\r\n" + 
										    		"\"avgEstdEffort\":\"%AGV_ESTD_EFFORT%\",\r\n" + 
										    		"\"bookingID\":\"%BOOKING_ID%\",\r\n" + 
										    		"\"executionType\":\"Automatic\",\r\n" + 
										    		"\"reason\":\"%REASON%\",\r\n" + 
										    		"\"status\":\"%STATUS%\",\r\n" + 
										    		"\"taskID\":\"%TASK_ID%\",\r\n" + 
										    		"\"taskName\":\"%TASK_NAME%\",\r\n" + 
										    		"\"toolID\":\"%TOOL_ID%\",\r\n" + 
										    		"\"toolName\":\"%TOOL_NAME%\",\r\n" +
										    		"\"outputLink\":\"%OUTPUT_LINK%\"\n" +
										    		"},\r\n" + 
										    		"\"tool\":{\r\n" + 
										    		"\"RPAID\":\"%RPATOOL_ID%\",\r\n" + 
										    		"\"RPAName\":\"%RPATOOL_NAME%\"\r\n" + 
										    		"}\r\n" + 
										    		"}\r\n" + 
										    		"}";
										    
      public static String DECISION_STEP_JSON=   "{\n" +
									              "\"type\":\"ericsson.Decision\",\n" +
									              "\"position\":{\n" +
									              "\"x\":%POSITION_X%,\n" +
									              "\"y\":%POSITION_Y%\n" +
									              "},\n" +
									              "\"size\":{\n" +
									              "\"width\":170,\n" +
									              "\"height\":80\n" +
									              "},\n" +
									              "\"angle\":0,\n" +
									              "\"id\":\"%STEP_ID%\",\n" +
									              "\"z\":5,\n" +
									              "\"attrs\":{\n" +
									              "\"body\":{\n" +
									              "\"refPoints\":\"50,0 100,50 50,100 0,50\",\n" +
									              "\"stroke\":\"#7c7ac7\",\n" +
									              "\"fill\":\"white\",\n" +
									              "\"strokeDasharray\":\"0\"\n" +
									              "},\n" +
									              "\"label\":{\n" +
									              "\"fontSize\":11,\n" +
									              "\"fill\":\"black\",\n" +
									              "\"text\":\"%STEP_NAME%\",\n" +
									              "\"fontFamily\":\"Roboto Condensed\",\n" +
									              "\"fontWeight\":\"Normal\",\n" +
									              "\"strokeWidth\":0\n" +
									              "},\n" +
									              "\"root\":{\n" +
									              "\"dataTooltipPosition\":\"left\",\n" +
									              "\"dataTooltipPositionSelector\":\".joint-stencil\"\n" +
									              "}\n" +
									              "}\n" +
									              "}";
     
    public static String START_STEP_JSON =     "{\r\n" + 
									    		"\"type\":\"ericsson.StartStep\",\r\n" + 
									    		"\"position\":{\r\n" + 
									    		"\"x\":%POSITION_X%,\r\n" + 
									    		"\"y\":%POSITION_Y%\r\n" + 
									    		"},\r\n" + 
									    		"\"size\":{\r\n" + 
									    		"\"width\":60,\r\n" + 
									    		"\"height\":60\r\n" + 
									    		"},\r\n" + 
									    		"\"angle\":0,\r\n" + 
									    		"\"preserveAspectRatio\":true,\r\n" + 
									    		"\"id\":\"%STEP_ID%\",\r\n" + 
									    		"\"z\":2,\r\n" + 
									    		"\"attrs\":{\r\n" + 
									    		"\"circle\":{\r\n" + 
									    		"\"fill\":\"green\",\r\n" + 
									    		"\"cx\":30,\r\n" + 
									    		"\"cy\":30,\r\n" + 
									    		"\"stroke-width\":0\r\n" + 
									    		"},\r\n" + 
									    		"\"text\":{\r\n" + 
									    		"\"x\":17,\r\n" + 
									    		"\"y\":35,\r\n" + 
									    		"\"text\":\"Start\",\r\n" + 
									    		"\"fill\":\"white\",\r\n" + 
									    		"\"font-family\":\"Roboto Condensed\",\r\n" + 
									    		"\"font-weight\":\"Normal\",\r\n" + 
									    		"\"font-size\":15,\r\n" + 
									    		"\"stroke-width\":0\r\n" + 
									    		"},\r\n" + 
									    		"\"root\":{\r\n" + 
									    		"\"dataTooltipPosition\":\"left\",\r\n" + 
									    		"\"dataTooltipPositionSelector\":\".joint-stencil\"\r\n" + 
									    		"}\r\n" + 
									    		"}\r\n" + 
									    		"}";
    
    public static String STOP_STEP_JSON =      	"{\r\n" + 
									    		"\"type\":\"ericsson.EndStep\",\r\n" + 
									    		"\"position\":{\r\n" + 
									    		"\"x\":%POSITION_X%,\r\n" + 
									    		"\"y\":%POSITION_Y%\r\n" + 
									    		"},\r\n" + 
									    		"\"size\":{\r\n" + 
									    		"\"width\":60,\r\n" + 
									    		"\"height\":60\r\n" + 
									    		"},\r\n" + 
									    		"\"angle\":0,\r\n" + 
									    		"\"preserveAspectRatio\":true,\r\n" + 
									    		"\"id\":\"%STEP_ID%\",\r\n" + 
									    		"\"z\":13,\r\n" + 
									    		"\"attrs\":{\r\n" + 
									    		"\"circle\":{\r\n" + 
									    		"\"fill\":\"red\",\r\n" + 
									    		"\"cx\":30,\r\n" + 
									    		"\"cy\":30,\r\n" + 
									    		"\"stroke-width\":0\r\n" + 
									    		"},\r\n" + 
									    		"\"text\":{\r\n" + 
									    		"\"x\":17,\r\n" + 
									    		"\"y\":35,\r\n" + 
									    		"\"text\":\"Stop\",\r\n" + 
									    		"\"fill\":\"white\",\r\n" + 
									    		"\"font-family\":\"Roboto Condensed\",\r\n" + 
									    		"\"font-weight\":\"Normal\",\r\n" + 
									    		"\"font-size\":15,\r\n" + 
									    		"\"stroke-width\":0\r\n" + 
									    		"},\r\n" + 
									    		"\"root\":{\r\n" + 
									    		"\"dataTooltipPosition\":\"left\",\r\n" + 
									    		"\"dataTooltipPositionSelector\":\".joint-stencil\"\r\n" + 
									    		"}\r\n" + 
									    		"}\r\n" + 
									    		"}";
        
   public static String APP_LINK_JSON =             	"{\r\n" + 
												   		"\"type\":\"app.Link\",\r\n" + 
												   		"\"router\":{\r\n" + 
												   		"\"name\":\"normal\"\r\n" + 
												   		"},\r\n" + 
												   		"\"connector\":{\r\n" + 
												   		"\"name\":\"rounded\"\r\n" + 
												   		"},\r\n" + 
												   		"\"labels\":[\r\n" + 
												   		"\r\n" + 
												   		"],\r\n" + 
												   		"\"source\":{\r\n" + 
												   		"\"id\":\"%SRC_STEP_ID%\"\r\n" + 
												   		"},\r\n" + 
												   		"\"target\":{\r\n" + 
												   		"\"id\":\"%TAR_STEP_ID%\"\r\n" + 
												   		"},\r\n" + 
												   		"\"id\":\"%LINK_ID%\",\r\n" + 
												   		"\"z\":15,\r\n" + 
												   		"\"attrs\":{\r\n" + 
												   		" \r\n" + 
												   		"}\r\n" + 
												   		"}";
    
    public static  String APP_LINK_JSON_WITH_LABLE =        "{\r\n" + 
												    		"\"type\":\"app.Link\",\r\n" + 
												    		"\"router\":{\r\n" + 
												    		"\"name\":\"normal\"\r\n" + 
												    		"},\r\n" + 
												    		"\"connector\":{\r\n" + 
												    		"\"name\":\"rounded\"\r\n" + 
												    		"},\r\n" + 
												    		"\"labels\":[\r\n" + 
												    		"{\r\n" + 
												    		"\"attrs\":{\r\n" + 
												    		"\"text\":{\r\n" + 
												    		"\"text\":\"%LINK_TEXT%\"\r\n" + 
												    		"}\r\n" + 
												    		"}\r\n" + 
												    		"}\r\n" + 
												    		"],\r\n" + 
												    		"\"source\":{\r\n" + 
												    		"\"id\":\"%SRC_STEP_ID%\"\r\n" + 
												    		"},\r\n" + 
												    		"\"target\":{\r\n" + 
												    		"\"id\":\"%TAR_STEP_ID%\"\r\n" + 
												    		"},\r\n" + 
												    		"\"id\":\"%LINK_ID%\",\r\n" + 
												    		"\"z\":12,\r\n" + 
												    		"\"attrs\":{\r\n" + 
												    		"\r\n" + 
												    		"}\r\n" + 
												    		"}";
    
}
