package com.leon.rfq.common;

public final class EnumTypes
{
	public static enum LocationEnum
	{
	    HONG_KONG("Hong Kong"), LONDON("London"), SYDNEY("Sydney"), TOKYO("Tokyo"),
	    PARIS ("Paris"), FRANKFURT("Frankfurt"), NEW_YORK("New York"), MOSCOW("Moscow");
	    
		private final String location;
		
		private LocationEnum(String location)
		{
			this.location = location;
		}
		
		public String getLocation()
		{
			return this.location;
		}
	}
	
	public static enum SideEnum
	{
		BUY("Buy"), SELL("Sell");
		
		private final String side;
		
		private SideEnum(String side)
		{
			this.side = side;
		}
		
		public String getSide()
		{
			return this.side;
		}
	}
	
	public static enum StatusEnum
	{
		PENDING("Pending"), PICKED_UP("Picked up"), PASSED("Passed"), INVALID("Invalid"),
		TRADED_AWAY("Traded away"), TRADED_AWAY_ASK("Traded away ask"), TRADED_AWAY_BID("Traded away bid"),
		FILLED("Filled"),	FILLED_ASK("Filled ask"),	FILLED_BID("Filled bid");
		
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
		SHARES("Shares"), FUTURES("Futures"), OPTIONS("Options");
		
		private final String hedgeType;
		
		private HedgeTypeEnum(String hedgeType)
		{
			this.hedgeType = hedgeType;
		}
		
		public String getHedgeType()
		{
			return this.hedgeType;
		}
	}
	
	public static enum PriceSimulatorRequestEnum
	{
		ADD_UNDERLYING,	REMOVE_UNDERLYING,
		SUSPEND_UNDERLYING,	AWAKEN_UNDERLYING,
		SUSPEND_ALL, AWAKEN_ALL, REMOVE_ALL
	}
	
	public static enum ClientTierEnum
	{
		Top("Top"), Middle("Middle"), Bottom("Bottom");
		
		private final String clientTier;
		
		private ClientTierEnum(String clientTier)
		{
			this.clientTier = clientTier;
		}
		
		public String getClientTier()
		{
			return this.clientTier;
		}
	}
}
