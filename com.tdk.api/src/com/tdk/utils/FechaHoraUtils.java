/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author fernando
 */
public class FechaHoraUtils {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public static String formatearFecha(Date date) {
        return dateFormat.format(date);
    }

    public static String formatearHora(Date date) {
        return timeFormat.format(date);
    }

    public static String formatearFechaHora(Date date) {
        return dateTimeFormat.format(date);
    }

    public static Date parsearFecha(String value) {
        try {
            return dateFormat.parse(value);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public static Date parsearFechaHora(String value) {
        try {
            return dateTimeFormat.parse(value);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}
