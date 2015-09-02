package com.leon.rfq.common;

import java.time.format.DateTimeFormatter;

public final class DateUtilities
{
	public final static DateTimeFormatter dateFormatter_MMddyyyy = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	private DateUtilities() {}
}
