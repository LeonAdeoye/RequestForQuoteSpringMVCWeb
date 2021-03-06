package com.leon.rfq.common;

public final class RegexConstants
{
	public static final String REQUEST_PATTERN = "^([+-]?[1-9]*[C|c|P|p]{1}){1}([-+]{1}[1-9]*[C|c|P|p]{1})* ([\\d]+){1}(,{1}[\\d]+)* "
    		+ "[\\d]{1,2}(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)20[\\d]{2}(,{1}[\\d]{1,2}(Jan|Feb|Mar"
    		+ "|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)20[\\d]{2})* (\\w){4,7}\\.[A-Z]{1,2}$";
	
	public static final String EUROPEAN_OPTION_PATTERN = "^([+-]?[1-9]*[CP]{1}){1}([-+]{1}[1-9]*[CP]{1})*";
	
	public static final String DETAIL_PATTERN = "^(?<side>[+-])?(?<quantity>[1-9])?(?<type>[C|c|P|p]{1})+";
	
	public static final String LEG_PATTERN = "^(?<leg>[+-]?[1-9]?[C|c|P|p]{1})+";
}
