package com.leon.rfq.common;

public final class EnumTypes
{
	public static enum LocationEnum
	{
	    HONG_KONG, LONDON, SYDNEY, TOKYO, PARIS, FRANKFURT, NEW_YORK
	}
	
	public static enum SideEnum
	{
		BUY, SELL
	}
	
	public static enum StatusEnum
	{
		PENDING, TRADEDAWAY, PICKEDUP, FILLED, INVALID
	}
	
	public static enum HedgeTypeEnum
	{
		SHARES, FUTURES
	}
}
