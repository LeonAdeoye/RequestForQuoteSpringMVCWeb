package com.leon.rfq.utilities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class UtilityMethods
{
	private static final Logger logger = LoggerFactory.getLogger(UtilityMethods.class);
	private final static DateFormat MMddyyyyFormat = new SimpleDateFormat("MM/dd/yyyy");

	public final static String DOTNET_DATE_STRING_FORMAT = "M/d/yyyy hh:mm:ss a";
	public final static String DB_DATE_STRING_FORMAT = "yyyy-MM-dd";

	/**
	 * Converts a string date with a format of MM/dd/yyyy to an instance of java.sql.Date.
	 *
	 * @param  	dateToConvert 				the string to convert into a java.sql.Date instance.
	 * @throws  IllegalArgumentException	if dateToConvert string is empty.
	 * @return 								the java.sql.Date if it successfully converts the string otherwise null.
	 */
	public static java.sql.Date convertToSqlDate(String dateToConvert)
	{
		if((dateToConvert == null) || dateToConvert.isEmpty())
			throw new IllegalArgumentException("dateToConvert");

		try
		{
			java.util.Date parsed = MMddyyyyFormat.parse(dateToConvert);
			return new java.sql.Date(parsed.getTime());
		}
		catch(ParseException pe)
		{
			if(logger.isErrorEnabled())
				logger.error("Failed to convert string " + dateToConvert + " to instance of java.sql.Date. Exception thrown:", pe);
		}
		return null;
	}
	
	/**
	 * Converts a string date with a format of MM/dd/yyyy to an instance of java.util.Date.
	 *
	 * @param  	dateToConvert 				the string to convert into a java.util.Date instance.
	 * @throws  IllegalArgumentException	if dateToConvert string is empty.
	 * @return 								the java.util.Date if it successfully converts the string otherwise null.
	 */
	public static java.util.Date convertToDate(String dateToConvert)
	{
		if((dateToConvert == null) || dateToConvert.isEmpty())
			throw new IllegalArgumentException("dateToConvert");

		try
		{
			return MMddyyyyFormat.parse(dateToConvert);
			
		}
		catch(ParseException pe)
		{
			if(logger.isErrorEnabled())
				logger.error("Failed to convert string " + dateToConvert + " to instance of java.util.Date. Exception thrown:", pe);
		}
		return null;
	}

	/**
	 * Converts a date stored as a string in specific format to a date in another string format.
	 * If the conversion fails due parsing exception then an empty string is returned.
	 * 
	 * @param inputDate						the date in the initial string format.
	 * @param inputFormat					the string format of the date to be converted.
	 * @param outputFormat					the string format of the date to be returned as the result of the conversion.
	 * @return								the string of the date after the conversion to the desired format.
	 * @throws IllegalArgumentException 	if the another of the input parameters are empty.
	 */
	public static String convertStringFormatOfDate(String inputDate, String inputFormat, String outputFormat)
	{
		if((inputDate == null) || inputDate.isEmpty())
			throw new IllegalArgumentException("stringDate");

		if((inputFormat == null) || inputFormat.isEmpty())
			throw new IllegalArgumentException("inputFormat");

		if((outputFormat == null) || outputFormat.isEmpty())
			throw new IllegalArgumentException("outputFormat");

		try
		{
			java.util.Date theDate = new SimpleDateFormat(inputFormat).parse(inputDate);
			return new SimpleDateFormat(outputFormat).format(theDate);
		}
		catch(ParseException pe)
		{
			if(logger.isErrorEnabled())
				logger.error("Failed to convert string [" + inputDate + "] from input string format [" + inputFormat +
						"] to output string format [" + outputFormat + "]. Exception thrown: ", pe);
		}
		return "";
	}

	/**
	 * Calculates the hash code of a double value.
	 * 
	 * @param value							the seed double value.
	 */
	public static int doubleHashCode(double value)
	{
		long bits = Double.doubleToLongBits(value);
		return (int)(bits ^ (bits >>> 32));
	}
	
	/**
	 * Concatenates elements in a structure which implements the Iterable interface using a delimiter.
	 * 
	 * @param iterable 							the structure implementing the Iterable interface.
	 * @param delimiter 						the delimiter to be used for the concatenation
	 * @return 									the concatenated string with each element separated by the delimiter.
	 * @throws NullPointerException				if the Iterable reference is null.
	 * @throws IllegalArgumentException			if the delimiter is not set.
	 */
	public static String join(Iterable<? extends CharSequence> iterable, String delimiter)
	{
		if(iterable == null)
			throw new NullPointerException("iterable");
		
		if(delimiter.isEmpty())
			throw new IllegalArgumentException("delimiter");
	    
		Iterator<? extends CharSequence> iter = iterable.iterator();
	    
	    if (!iter.hasNext())
	    	return "";
	    
	    StringBuilder buffer = new StringBuilder(iter.next());
	    
	    while (iter.hasNext())
	    	buffer.append(delimiter).append(iter.next());
	    
	    return buffer.toString();
	}
}

