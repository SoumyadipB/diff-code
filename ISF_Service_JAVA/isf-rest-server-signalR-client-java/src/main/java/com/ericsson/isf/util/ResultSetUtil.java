/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.util;

import java.sql.ResultSet;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 *
 * @author ekalakr
 */
public class ResultSetUtil {

    /**
     * Convert a result set into a JSON Array
     *
     * @param resultSet
     * @return a JSONArray
     * @throws Exception
     */
    public static JSONArray convertToJSON(ResultSet resultSet) throws Exception {
        JSONArray jsonArray = new JSONArray();
        while (resultSet.next()) {
            int total_cols = resultSet.getMetaData().getColumnCount();
            JSONObject obj = new JSONObject();
            for (int i = 1; i <= total_cols; i++) {
                if (i % 2 != 0) {
                    obj.put(resultSet.getMetaData().getColumnLabel(i), resultSet.getObject(i));
                }
            }
            jsonArray.add(obj);
        }

        return jsonArray;
    }

    public static JSONArray convertToJSONTable(ResultSet resultSet) throws Exception {
        JSONArray jsonArray = new JSONArray();
        while (resultSet.next()) {
            int total_cols = resultSet.getMetaData().getColumnCount();
            JSONObject obj = new JSONObject();
            for (int i = 1; i <= total_cols; i++) {
                if (resultSet.getObject(i) != null) {
                    obj.put(resultSet.getMetaData().getColumnLabel(i), resultSet.getObject(i).toString());
                } else {
                    obj.put(resultSet.getMetaData().getColumnLabel(i), resultSet.getObject(i));
                }
            }
            jsonArray.add(obj);
        }
        
        return jsonArray;
    }

    /**
     * Convert a result set into a XML List
     *
     * @param resultSet
     * @return a XML String with list elements
     * @throws Exception if something happens
     */
    public static String convertToXML(ResultSet resultSet) throws Exception {
        StringBuilder xmlArray = new StringBuilder("<results>");
        while (resultSet.next()) {
            int total_rows = resultSet.getMetaData().getColumnCount();
            xmlArray.append("<result ");
            for (int i = 0; i < total_rows; i++) {
                xmlArray.append(" ").append(resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase()).append("='").append(resultSet.getObject(i + 1)).append("'");
            }
            xmlArray.append(" />");
        }
        xmlArray.append("</results>");
        return xmlArray.toString();
    }
}
