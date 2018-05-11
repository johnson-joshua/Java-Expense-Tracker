/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 *
 * @author johnsonjm
 */
public class Helper {

    public static boolean validateStringWithSpaces(String stringToValidate){
	boolean isValid = false;
	//Strings should only contain text, no numbers
	if(Pattern.matches("[a-zA-Z ]+", stringToValidate)) //regex pattern allows spaces in string
	{ 
	    isValid = true;
	} 
	else 
	{ 
	    isValid = false;
	} 

	return isValid;
    }

    public static boolean validateStringNoSpaces(String stringToValidate){
	boolean isValid = false;
	//Strings should only contain text, no numbers, no spaces
	if(Pattern.matches("[a-zA-Z]+", stringToValidate)) 
	{ 
	    isValid = true;
	} 
	else 
	{ 
	    isValid = false;
	} 

	return isValid;
    }

    public static boolean validatePass(String passToValidate){
	boolean isValid = false;
	//4 - 10 characters, caps count, special character required
	String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
	if(Pattern.matches(regex , passToValidate))
	{ 
	    isValid = true;
	} 
	else 
	{ 
	    isValid = false;
	} 

	return isValid;
    }

    public static boolean validateAmount(String amountToValidate){
	//Should only allow numbers or numbers followed by . and 2 digits
	boolean isValid = false;
	if(Pattern.matches("^\\d+\\.\\d{2}$", amountToValidate) || Pattern.matches("^\\d+$", amountToValidate)){
	    return true;
	}
	return isValid;
    }

    public static LocalDate setEndDateIfNull(LocalDate startDate){
	if(startDate == null){
	    return LocalDate.now();
	}
	else{
	    int defaultEndYear = startDate.getYear() + 10; //If end date is left empty, end date will be 10 years past start date
	    String sDay;
	    int endDay = startDate.getDayOfMonth();
	    if(String.valueOf(endDay).length() == 1){ //day is listed as single digit (1-9), add 0 in front of number
		sDay = "0"+String.valueOf(endDay);
	    }
	    else{
		sDay = String.valueOf(endDay);

	    }
	    String sYear = String.valueOf(defaultEndYear);
	    int intMonth = startDate.getMonthValue();
	    String sMonth = generateMonthNumbers(String.valueOf(intMonth));
	    LocalDate newEndDate = LocalDate.parse(sYear+"-"+sMonth+"-"+sDay);
	    return newEndDate;
	}
    }
    //For use with HashMaps as Key Value
    public static String generateMonthNames(String monthNumber) {
	String monName = null;
	switch(monthNumber) {
	case "01":
	case "1":
	case "Jan": monName = "Jan";	break;
	case "02":
	case "2":
	case "Feb": monName = "Feb";	break;
	case "03":
	case "3":
	case "Mar": monName = "Mar";	break;
	case "04":
	case "4":
	case "Apr": monName = "Apr";	break;
	case "05":
	case "5":
	case "May": monName = "May";	break;
	case "06":
	case "6":
	case "Jun": monName = "Jun";	break;
	case "07":	
	case "7":
	case "Jul": monName = "Jul";	break;
	case "08":
	case "8":
	case "Aug": monName = "Aug";	break;
	case "09":	
	case "9":
	case "Sep": monName = "Sep";	break;
	case "10":
	case "Oct": monName = "Oct";	break;
	case "11":
	case "Nov": monName = "Nov";	break;
	case "12":	
	case "Dec": monName = "Dec";	break;
	default:	break;
	}
	return monName;
    }//end generateMonthNames()
    //For use with date class - 0=Jan 11=Dec
    public static String generateMonthNumbers(String monthName) {
	String monNum = "00";
	switch(monthName) {
	case "01":
	case "1":
	case "Jan":      monNum = "01";	break;
	case "02":
	case "2":
	case "Feb":      monNum = "02";	break;
	case "03":
	case "3":
	case "Mar":      monNum = "03";	break;
	case "04":
	case "4":
	case "Apr":      monNum = "04";	break;
	case "05":
	case "5":
	case "May":	 monNum = "05";	break;
	case "06":
	case "6":
	case "Jun":	 monNum = "06";	break;
	case "07":
	case "7":
	case "Jul":	 monNum = "07";	break;
	case "08":
	case "8":
	case "Aug":      monNum = "08";	break;
	case "09":
	case "9":
	case "Sep":	 monNum = "09";	break;
	case "10":
	case "Oct":      monNum = "10";	break;
	case "11":
	case "Nov":      monNum = "11";	break;
	case "12":
	case "Dec":	 monNum = "12";	break;
	default:	 break;
	}
	return monNum;
    }//end generateMonthNumbers()

    public static LocalDate dateToLocalDate(Date input){
	Date date = input;
	LocalDate localDate = null;
	if(date == null){
	    return null;
	}
	String sDate = input.toString();
	try{
	    localDate = LocalDate.parse(sDate);
	    return localDate;
	}
	catch(Exception e){
	    String[] dateTokens = date.toString().split(" ");
	    String stringDate = dateTokens[5]+"-"+generateMonthNumbers(dateTokens[1])+"-"+dateTokens[2];
	    localDate = LocalDate.parse(stringDate);
	}
	return localDate;
    }

    public static int convertTypeToInt(String type) {
	if(type.equalsIgnoreCase("Income")){
	    return 1;}
	else{
	    return 2;
	}    
    }
}
