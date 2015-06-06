package com.leon.rfq.services.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.leon.rfq.common.EnumTypes.LocationEnum;
import com.leon.rfq.repositories.BankHolidayDao;
import com.leon.rfq.repositories.BankHolidayDaoImpl;
import com.leon.rfq.services.BankHolidayService;

@ContextConfiguration(locations = { "classpath: **/applicationContext.xml" })
public class BankHolidayServiceImplTest extends AbstractJUnit4SpringContextTests
{
	@Autowired(required=true)
	private BankHolidayService bankHolidayService;
	
	@Before
	public void setUp()
	{
		assertNotNull("autowired bankHolidayDaoImpl should not be null", this.bankHolidayService);
	}
		
	@Test
    public void getHolidaysInLocation_ValidLocation_DaoMethodCalled()
	{
		// Arrange
		BankHolidayDao bankHolidayDaoMock = mock(BankHolidayDaoImpl.class);
		this.bankHolidayService.setBankHolidayDao(bankHolidayDaoMock);
		// Act
		this.bankHolidayService.getHolidaysInLocation(LocationEnum.TOKYO);
		// Assert
		verify(bankHolidayDaoMock).getAll(LocationEnum.TOKYO);
	}
	
	@Test
    public void calculateAllDaysToExpiry_ValidDates_CorrectResultReturned()
	{
		// Arrange
		assertEquals("Should calculate correct number of days between two dates", this.bankHolidayService.calculateAllDaysToExpiry(LocalDate.now(), LocalDate.now().plusDays(10)), 10);
	}
	
	@Test
    public void calculateAllDaysToExpiryFromToday_ValidDates_CorrectResultReturned()
	{
		// Arrange
		assertEquals("Should calculate correct number of days between today and end date", this.bankHolidayService.calculateAllDaysToExpiryFromToday(LocalDate.now().plusDays(10)), 10);
	}
}
