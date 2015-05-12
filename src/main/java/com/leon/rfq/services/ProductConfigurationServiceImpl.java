package com.leon.rfq.services;

import org.springframework.stereotype.Service;

import com.leon.rfq.domains.RequestEnums.LocationEnum;

@Service
public class ProductConfigurationServiceImpl implements	ProductConfigurationService
{
	@Override
	public LocationEnum getLocation()
	{
		// TODO Auto-generated method stub
		return LocationEnum.TOKYO;
	}

}
