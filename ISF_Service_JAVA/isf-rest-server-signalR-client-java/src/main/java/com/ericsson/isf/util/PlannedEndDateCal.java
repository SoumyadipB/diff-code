/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author eabhmoj
 */
public class PlannedEndDateCal {
     static int fromHour = 8;
    static int fromMinute = 0;
   // static double toHour = 17;
    static int toMinute = 0;
    static long maxResponseTime = 16;
    
    public static Date calculateEndDateV21(Date startDt, double hours) {
       
        if(hours == 0) {
            return startDt;
        }
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(startDt);
        
        int hourPart = endDate.get(Calendar.HOUR_OF_DAY);
        
        double toHour = hourPart+9;
        int minutePart = endDate.get(Calendar.MINUTE);
        
        int secondPart = endDate.get(Calendar.SECOND);
        
        double hourOfDay = hourPart + ((double)minutePart / 60) + ((double)secondPart / 3600);
        
        
        
        if(toHour > hourOfDay) {
            
            double addHours = toHour - hourOfDay;
            int tempHourPart = (int) addHours;
            
            int tempMinutePart = (int) (addHours * 60) % 60;
            
            int tempSecondPart = (int) (addHours * (60*60)) % 60;
            
            
            endDate.add(Calendar.HOUR, tempHourPart);
            endDate.add(Calendar.MINUTE, tempMinutePart);
            endDate.add(Calendar.SECOND, tempSecondPart);
            
            hours = hours - (toHour - hourOfDay);
            
        }
        
        while(hours > 0) {
            
            
            
            
            if(hours >= 9) {
                
                endDate.add(Calendar.DATE, 1);
                hours = hours - 9;
                
            } else {
                
                endDate.add(Calendar.DATE, 1);
                endDate.set(Calendar.HOUR_OF_DAY, hourPart);
                endDate.set(Calendar.MINUTE, 0);
                endDate.set(Calendar.SECOND, 0);
                
                int tempHourPart = (int) hours;
                
                int tempMinutePart = (int) (hours * 60) % 60;
                
                int tempSecondPart = (int) (hours * (60*60)) % 60;
                

                endDate.add(Calendar.HOUR, tempHourPart);
                endDate.add(Calendar.MINUTE, tempMinutePart);
                endDate.add(Calendar.SECOND, tempSecondPart);
                
                hours = 0;
            }
            int dayOfWeek = endDate.get(Calendar.DAY_OF_WEEK);
            if(dayOfWeek == Calendar.SATURDAY) hours += 9;
            if(dayOfWeek == Calendar.SUNDAY) hours += 9;
        }
        
        return endDate.getTime();
    }
    
    public static Date calculateEndDateV2(Date startDt, double hours)
    {
        if(hours == 0) {
            return startDt;
        }
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(startDt);
        int hourPart = endDate.get(Calendar.HOUR_OF_DAY);
        
        int minutePart = endDate.get(Calendar.MINUTE);
        
        int secondPart = endDate.get(Calendar.SECOND);
        
        double hourOfDay = hourPart + ((double)minutePart / 60) + ((double)secondPart / 3600);
        
        endDate.add(Calendar.MINUTE, (int)( hours * 60));     
            
        return endDate.getTime();
    }
            

    
    public static void main(String args[]) {
        Date currentDate = convertStringToDate("2017-09-19 18:00:42", "yyyy-MM-dd HH:mm:ss");
        Date endDate = calculateEndDateV2(currentDate, 10);
        
        
    }

    public static Date convertStringToDate(String dateStr, String format) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            Date date = formatter.parse(dateStr);
            return date;
        } catch(Exception ex) {
            return new Date();
        }
    }

    public static String convertDateToString(Date date, String format) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            String dateStr = formatter.format(date);
            return dateStr;
        } catch(Exception ex) { }
        return StringUtils.EMPTY;
    }

    public static String getDayOfWeekStr(Calendar start) {
        switch(start.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY: return "Sunday";
            case Calendar.MONDAY: return "Monday";
            case Calendar.TUESDAY: return "Tuesday";
            case Calendar.WEDNESDAY: return "Wednesday";
            case Calendar.THURSDAY: return "Thursday";
            case Calendar.FRIDAY: return "Friday";
            case Calendar.SATURDAY: return "Saturday";
            default: return "";
        }
    }
    
    public static Date getPlannedStartDate(Date startDate, int hour, int day) {
        
        Calendar calculatedStartDate = Calendar.getInstance();
        calculatedStartDate.setTime(startDate);
        calculatedStartDate.add(Calendar.HOUR, hour);
        calculatedStartDate.add(Calendar.DATE, day-1);
        return calculatedStartDate.getTime();
    }
    
    public static Date calculateDateByAddingDayAndHour(Date startDate, int days,int hours) {
    	
    	Calendar calculatedEndDate = Calendar.getInstance();
    	calculatedEndDate.setTime(startDate);
    	calculatedEndDate.add(Calendar.DATE, days);
    	calculatedEndDate.add(Calendar.HOUR, hours);
		return calculatedEndDate.getTime();
    	
    }
    
    /**
     * Add slaHrs to PlannedStartDate to get PlannedEndDate
     * 
     * @param startDate
     * @param hours
     * @return
     */
    public static Date calculatePlannedEndDate(Date startDate,int hours) {
    	Calendar calculatedEndDate = Calendar.getInstance();
    	calculatedEndDate.setTime(startDate);
    	calculatedEndDate.add(Calendar.HOUR_OF_DAY, hours);
		return calculatedEndDate.getTime();
    	
    }
}
