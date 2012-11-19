package com.thorplatform.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SimpleDateTimeUtils implements DateTimeUtils {
    
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    
    public synchronized String formatDate(Date date) {
        return dateFormat.format(date);
    }
    
    public synchronized String formatTime(Date date) {
        return timeFormat.format(date);
    }
    
    public synchronized String formatDateTime(Date date) {
        return dateTimeFormat.format(date);
    }
    
    public synchronized Date parseDate(String value) {
        try {
            return dateFormat.parse(value);
        } catch(Throwable t) {
            throw new RuntimeException(t);
        }
    }
    
    public synchronized Date parseDateTime(String value) {
        try {
            return dateTimeFormat.parse(value);
        } catch(Throwable t) {
            throw new RuntimeException(t);
        }
    }
    
    public synchronized Date ultimaHora(Date date) {
        String dateStr = formatDate(date);
        dateStr = dateStr + " 23:59:59";
        return parseDateTime(dateStr);
    }
    
    public synchronized Date primeraHora(Date date) {
        String dateStr = formatDate(date);
        dateStr = dateStr + " 00:00:00";
        return parseDateTime(dateStr);
    }
    
    public synchronized int countDayDiff(Date dateA, Date dateB) {
        int direfenciaTemp = 0;
        int diferencia = 0;
        Calendar inicial = Calendar.getInstance();
        Calendar posterior = Calendar.getInstance();
        
        if (dateA.compareTo(dateB) < 0) {
            inicial.setTime(dateA);
            posterior.setTime(dateB);
        } else {
            inicial.setTime(dateB);
            posterior.setTime(dateA);
        }
        
        while (inicial.get(Calendar.YEAR) != posterior.get(Calendar.YEAR)) {
            direfenciaTemp = 365 * (posterior.get(Calendar.YEAR) - inicial.get(Calendar.YEAR));
            diferencia += direfenciaTemp;
            
            inicial.add(Calendar.DAY_OF_YEAR, direfenciaTemp);
        }
        
        if (inicial.get(Calendar.DAY_OF_YEAR) != posterior.get(Calendar.DAY_OF_YEAR)) {
            direfenciaTemp = posterior.get(Calendar.DAY_OF_YEAR) - inicial.get(Calendar.DAY_OF_YEAR);
            diferencia += direfenciaTemp;
        }
        
        return diferencia;
    }
    
}
