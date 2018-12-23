package com.cijianyouqing.traveler.util;

public class ConvertUtils {
	
	public static String Obj2Str(Object obj){
		if(obj == null){
			return "";
		}else{
			String str = obj.toString();
			if("null".equalsIgnoreCase(str)){
				str = "";
			}
			return str;
		}
	}
	
	public static Integer Obj2Integer(Object obj){
		Integer integer = null;
		if(obj != null){
			try{
				integer = Integer.valueOf(obj.toString());
			}catch(Exception e){
			}
		}
		return integer;
	}
	
	public static int Obj2Int(Object obj){
		Integer integer = Obj2Integer(obj);
		if(integer == null){
			return 0;
		}else{
			return integer.intValue();
		}
	}
	
	public static Double Obj2Double(Object obj){
		Double value = null;
		if(obj != null){
			try{
				value = Double.valueOf(obj.toString());
			}catch(Exception e){
			}
		}
		return value;
	}
	
	public static double Obj2double(Object obj){
		Double value = Obj2Double(obj);
		if(value == null){
			return 0D;
		}else{
			return value.doubleValue();
		}
	}
	
	public static Float Obj2Float(Object obj){
		Float value = null;
		if(obj != null){
			try{
				value = Float.valueOf(obj.toString());
			}catch(Exception e){
			}
		}
		return value;
	}
	
	public static float Obj2float(Object obj){
		Float value = Obj2Float(obj);
		if(value == null){
			return 0F;
		}else{
			return value.floatValue();
		}
	}
	
	public static Long Obj2Long(Object obj){
		Long value = null;
		if(obj != null){
			try{
				value = Long.valueOf(obj.toString());
			}catch(Exception e){
			}
		}
		return value;
	}
	
	public static long Obj2long(Object obj){
		Long value = Obj2Long(obj);
		if(value == null){
			return 0L;
		}else{
			return value.longValue();
		}
	}
	
	public static boolean Obj2Boolean(Object obj){
		return Obj2Boolean(obj,false);
	}
	
	public static boolean Obj2Boolean(Object obj, boolean defaultValue){
		if(obj == null){
			return defaultValue;
		}else{
			String value = Obj2Str(obj);
			if(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("yes") || value.equals("是")){
				return true;
			}else if(value.equalsIgnoreCase("false") || value.equalsIgnoreCase("no") || value.equals("否")){
				return false;
			}else{
				return defaultValue;
			}
		}
	}
}
