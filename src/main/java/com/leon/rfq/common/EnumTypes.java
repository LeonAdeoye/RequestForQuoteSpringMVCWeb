package com.leon.rfq.common;

public final class EnumTypes
{
	public static enum LocationEnum
	{
	    HONG_KONG, LONDON, SYDNEY, TOKYO,
	    PARIS, FRANKFURT, NEW_YORK
	}
	
	public static enum SideEnum
	{
		BUY, SELL
	}
	
	public static enum StatusEnum
	{
		PENDING, PICKED_UP, TRADED_AWAY, TRADED_AWAY_ASK, TRADED_AWAY_BID,
		FILLED,	FILLED_ASK,	FILLED_BID,	INVALID, PASSED
	}
	
	public static enum HedgeTypeEnum
	{
		SHARES, FUTURES
	}
	
	public static enum PriceSimulatorRequestEnum
	{
		ADD_UNDERLYING,	REMOVE_UNDERLYING,
		SUSPEND_UNDERLYING,	AWAKEN_UNDERLYING,
		SUSPEND_ALL, AWAKEN_ALL, REMOVE_ALL
	}
	
	public static enum ClientTierEnum
	{
		Top, Middle, Bottom
	}
}
