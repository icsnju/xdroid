package com.nata.xdroid.injector;

public class TextTypeDetector {
	
	public static String detectByInputType(int inputType){
		if (isTextURI(inputType)) {
			return "url";
		}
		if (isTextEmailAddress(inputType)) {
			return "email";
		}
		if (isTextPostalAddress(inputType)) {
			return "zip";
		}
		if (isNumber(inputType)){
			 if (isNumberSigned(inputType))
			 {
				 if (isNumberDecimal(inputType)) {
					 return "numberSignedDecimal";
				 }
				 return "numberSigned";
			 }
			 if (isNumberDecimal(inputType)) {
				 return "numberDecimal";
			 }
			 return "number";
	    }
		if(isDatetime(inputType)){
			return "date";
		}
		if(isPhone(inputType)){
			return "phone";
		}
		return "default";
	}
	
//	public static String detect(int textType,String name,String value,String paramString)
//	  {
//		String text
//		String str2;
//	    if (paramString != null){
//	    	str2 = paramString.toLowerCase();
//	    }
//	      
//	    if ((!isText(textType)) && (!isTextURI(textType)) && (!isTextEmailAddress(textType)) && (!isTextPostalAddress(textType))) {
//	        break ;
//	      }
//	      if (!isTextURI(textType)) {
//	        if (!stringContains(value, new String[] { "http", "www" })) {
//	          if (!stringContains(name, new String[] { "site", "url", "web", "sito" })) {
//	            if (!stringContains(str2, new String[] { "site", "url", "web", "sito" })) {
//	              break;
//	            }
//	          }
//	        }
//	      }
//	      return "url";
//	    if (!isTextEmailAddress(textType)) {
//	      if (!stringContains(value, new String[] { "@" })) {
//	        if (!stringContains(name, new String[] { "e-mail", "email" })) {
//	          if (!stringContains(str2, new String[] { "e-mail", "email" })) {
//	            break label243;
//	          }
//	        }
//	      }
//	    }
//	    return "email";
//	    if (!stringContains(name, new String[] { "isbn" }))
//	    {
//	      if (!stringContains(name, new String[] { "isbn" })) {}
//	    }
//	    else {
//	      return "isbn";
//	    }
//	    if (!stringContains(name, new String[] { "credit", "card" }))
//	    {
//	      if (!stringContains(str2, new String[] { "credit", "card" })) {}
//	    }
//	    else {
//	      return "creditCard";
//	    }
//	    if (!isTextPostalAddress(textType)) {
//	      if (!stringContains(name, new String[] { "postcode", "postal", "zip" })) {
//	        if (!stringContains(str2, new String[] { "postcode", "postal", "zip" })) {
//	          break label428;
//	        }
//	      }
//	    }
//	    return "zip";
//	    if (isNumber(textType))
//	    {
//	      if (isNumberSigned(textType))
//	      {
//	        if (isNumberDecimal(textType)) {
//	          return "numberSignedDecimal";
//	        }
//	        return "numberSigned";
//	      }
//	      if (isNumberDecimal(textType)) {
//	        return "numberDecimal";
//	      }
//	      return "number";
//	    }
//	    return "default";
//	  }
	  
	  public static boolean isDatetime(int paramInt)
	  {
	    return (paramInt & InputType.TYPE_MASK_CLASS) == InputType.TYPE_CLASS_DATETIME;
	  }
	  
	  public static boolean isNumber(int paramInt)
	  {
	    return (paramInt & InputType.TYPE_MASK_CLASS) == InputType.TYPE_CLASS_NUMBER;
	  }
	  
	  public static boolean isNumberDecimal(int paramInt)
	  {
	    return (isNumber(paramInt)) && ((InputType.TYPE_MASK_FLAGS & paramInt) == InputType.TYPE_NUMBER_FLAG_DECIMAL);
	  }
	  
	  public static boolean isNumberSigned(int paramInt)
	  {
	    return (isNumber(paramInt)) && ((InputType.TYPE_MASK_FLAGS & paramInt) == InputType.TYPE_NUMBER_FLAG_SIGNED);
	  }
	  
	  public static boolean isPhone(int paramInt)
	  {
	    return (paramInt & InputType.TYPE_MASK_CLASS) ==InputType.TYPE_CLASS_PHONE;
	  }
	  
	  public static boolean isText(int paramInt)
	  {
	    return (paramInt & InputType.TYPE_MASK_CLASS) == InputType.TYPE_CLASS_TEXT;
	  }
	  
	  public static boolean isTextEmailAddress(int paramInt)
	  {
	    return (isText(paramInt)) && ((paramInt & InputType.TYPE_MASK_VARIATION) == InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
	  }
	  
	  public static boolean isTextMultiline(int paramInt)
	  {
	    return (isText(paramInt)) && ((InputType.TYPE_MASK_FLAGS & paramInt) == InputType.TYPE_TEXT_FLAG_MULTI_LINE);
	  }
	  
	  public static boolean isTextPostalAddress(int paramInt)
	  {
	    return (isText(paramInt)) && ((paramInt & InputType.TYPE_MASK_VARIATION) == InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS);
	  }
	  
	  public static boolean isTextURI(int paramInt)
	  {
	    return (isText(paramInt)) && ((paramInt & InputType.TYPE_MASK_VARIATION) == InputType.TYPE_TEXT_VARIATION_URI);
	  }
	  
	  public static boolean stringContains(String paramString, String... paramVarArgs)
	  {
		if(paramString == null) return false;
	    int length = paramVarArgs.length;
	    for (int index = 0;index < length ; index++)
	    {
	      if (paramString.contains(paramVarArgs[index])) {
	        return true;
	      }
	    }
	    return false;
	  }
}	
