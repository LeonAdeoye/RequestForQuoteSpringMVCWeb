package com.leon.rfq.services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

import org.springframework.stereotype.Service;

import com.leon.rfq.domains.PriceDetailImpl;

@Service
public final class PriceServiceImpl implements PriceService
{
	Map<String, PriceDetailImpl> prices = new ConcurrentSkipListMap<>();
	
	@Override
	public BigDecimal getLatestPrice(String ric)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getClosePrice(String ric)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getOpenPrice(String ric)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getMidPrice(String ric)
	{
		// TODO Auto-generated method stub
		return new BigDecimal("90");
	}

	@Override
	public BigDecimal getClosePrice(String ric, Date referenceDate)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getOpenPrice(String ric, Date referenceDate)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getAskPrice(String ric)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getBidPrice(String ric)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
