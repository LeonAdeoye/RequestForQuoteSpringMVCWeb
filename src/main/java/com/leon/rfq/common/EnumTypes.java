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
		PENDING("Pending"), PICKED_UP("Picked up"), TRADED_AWAY("Traded away"), TRADED_AWAY_ASK("Traded away ask"),
		TRADED_AWAY_BID("Traded away bid"),	FILLED("Filled"),	FILLED_ASK("Filled ask"),	FILLED_BID("Filled bid"),
		INVALID("Invalid"), PASSED("Passed");
		
		private final String description;
		
		private StatusEnum(String description)
		{
			this.description = description;
		}
		
		public String getDescription()
		{
			return this.description;
		}
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
