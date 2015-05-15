package com.leon.rfq.services;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.stereotype.Service;

@Service
public final class PriceServiceImpl implements PriceService
{

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
