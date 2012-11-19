package com.thorplatform.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class NumericUtils_18_2 implements NumericUtils {
    
    private static final int SCALE = 2;
    private static final int PRECISION = 18;
    private static final MathContext _mathContext = new MathContext(PRECISION, RoundingMode.valueOf(BigDecimal.ROUND_HALF_UP));
    
    private static final BigDecimal CERO = new BigDecimal("0.00");
    
    private static final DecimalFormat decimalFormat3 = new DecimalFormat("##,###,###.###");
    
    public boolean isBigDecimalParsable(String string) {
        try {
            parseBigDecimal(string);
            return true;
        } catch(Throwable t) {
            return false;
        }
    }
    
    public boolean isIntegerParsable(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch(Throwable t) {
            return false;
        }
    }
    
    public BigDecimal parseBigDecimal(String str) {
        return new BigDecimal(str, _mathContext).setScale(SCALE, _mathContext.getRoundingMode());
    }
    
    public BigDecimal safeParseBigDecimal(String str) {
        try {
            return parseBigDecimal(str);
        } catch(Throwable t) {
            return CERO;
        }
    }
    
    public Integer safeParseInteger(String str) {
        try {
            return new Integer(str);
        } catch(Throwable t) {
            return new Integer(0);
        }
    }
    
    public String bigDecimalToString(BigDecimal n) {
        return decimalFormat3.format(n.doubleValue());
    }
    
    public String bigDecimalToString(BigDecimal n, String format) {
        String value = null; 
        try {
            DecimalFormat df = new DecimalFormat(format);
            value = df.format(n.doubleValue());
        } catch (Throwable ex) {
            
        }
        return value;
    }
    
    public BigDecimal bigDecimalToBigDecimal3(BigDecimal n) {
        try {
            String value = bigDecimalToString(n);
            value = value.replaceAll(",",".");
            return new BigDecimal(value);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return n;
    }
}
