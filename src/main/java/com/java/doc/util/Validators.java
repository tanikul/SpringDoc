package com.java.doc.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

public final class Validators {
	private static final String VALUE_TRUE = "true";
	private static final String VALUE_FALSE = "false";
	
	private Validators() {}
	
    public static boolean isNull(Object obj) {
        return ((null == obj) ? true : false);
    }

    public static boolean isNotNull(Object obj) {
        return !isNull(obj);
    }

	public static boolean isEmpty(Object value) {
		if (isNull(value)) {
			return true;
		} else if (value instanceof String) {
			return ((String) value).isEmpty();
		} else if (value instanceof Collection<?>) {
			return ((Collection<?>) value).isEmpty();
		} else if (value instanceof Map<?, ?>) {
			return ((Map<?, ?>) value).isEmpty();
		} else if (value.getClass().isArray()) {
			return Array.getLength(value) == 0;
		} else {
			return value.toString() == null || value.toString().isEmpty();
		}
	}
	
    public static boolean isNotEmpty(Object value) {
        return !isEmpty(value);
    }

 	public static boolean isBooleanVal(String val) {
		if (isEmpty(val)) {
			return false;
		}

		return (VALUE_TRUE.equalsIgnoreCase(val) || VALUE_FALSE.equalsIgnoreCase(val));
	}

    public static boolean isNotBooleanVal(String val) {
    	return !isBooleanVal(val);
    }
    
    public static boolean isInteger(String val) {
		if (isEmpty(val)) {
    		return false;
    	}
		
    	try {
    		Integer.valueOf(val);
    		return true;
    	} catch (NumberFormatException ex) {}
    	return false;
	}
    
    public static boolean isNotInteger(String val) {
    	return !isInteger(val);
    }

}
