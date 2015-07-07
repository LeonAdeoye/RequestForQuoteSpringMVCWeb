package com.leon.rfq.services.test;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.concurrent.BlockingQueue;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.leon.rfq.domains.PriceDetailImpl;
import com.leon.rfq.services.PriceService;

@ContextConfiguration(locations = { "classpath: **/applicationContext.xml" })
public class PriceServiceImplTest extends AbstractJUnit4SpringContextTests
{
	@Autowired(required=true)
	private PriceService priceService;
	
	@Resource(name="priceUpdateBlockingQueue")
	private BlockingQueue<PriceDetailImpl> priceUpdateBlockingQueue;
	
	private final PriceDetailImpl zeroPrice = new PriceDetailImpl("ric_without_prices");
	
	private final PriceDetailImpl nonZeroPrice = new PriceDetailImpl("ric_with_prices", BigDecimal.TEN,
			BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN);
	
	@Before
	public void setUp()
	{
	}
	
	@After
    public void cleanUp()
	{
	}
	
	@Test
    public void getLastPrice_NullRic_ThrowsInvalidArgumentException()
	{
		catchException(this.priceService).getLastPrice(null);
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "ric argument is invalid");
	}
	
	@Test
    public void getLastPrice_EmptyStringRic_ThrowsInvalidArgumentException()
	{
		catchException(this.priceService).getLastPrice("");
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "ric argument is invalid");
	}
	
	@Test
    public void getMidPrice_NullRic_ThrowsInvalidArgumentException()
	{
		catchException(this.priceService).getMidPrice(null);
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "ric argument is invalid");
	}
	
	@Test
    public void getMidPrice_EmptyStringRic_ThrowsInvalidArgumentException()
	{
		catchException(this.priceService).getMidPrice("");
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "ric argument is invalid");
	}
	
	@Test
    public void getAskPrice_NullRic_ThrowsInvalidArgumentException()
	{
		catchException(this.priceService).getAskPrice(null);
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "ric argument is invalid");
	}
	
	@Test
    public void getAskPrice_EmptyStringRic_ThrowsInvalidArgumentException()
	{
		catchException(this.priceService).getAskPrice("");
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "ric argument is invalid");
	}
	
	@Test
    public void getBidPrice_NullRic_ThrowsInvalidArgumentException()
	{
		catchException(this.priceService).getBidPrice(null);
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "ric argument is invalid");
	}
	
	@Test
    public void getBidPrice_EmptyStringRic_ThrowsInvalidArgumentException()
	{
		catchException(this.priceService).getBidPrice("");
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "ric argument is invalid");
	}
	
	@Test
    public void getAllPrices_NullRic_ThrowsInvalidArgumentException()
	{
		catchException(this.priceService).getAllPrices(null);
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "ric argument is invalid");
	}
	
	@Test
    public void getAllPrices_EmptyStringRic_ThrowsInvalidArgumentException()
	{
		catchException(this.priceService).getAllPrices("");
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "ric argument is invalid");
	}
	
	//TODO - fail because price server terminates
	
	
	@Test
    public void getAllPrices_ricWithPrices_ValidPricesAreReturned()
	{
		// Arrange
		try
		{
			int count = 0;
			this.priceUpdateBlockingQueue.put(this.nonZeroPrice);
			while(this.priceUpdateBlockingQueue.contains(this.nonZeroPrice))
			{
				Thread.sleep(500);
				if(++count == 10)
					fail();
			}
		}
		catch(InterruptedException ie)
		{
			this.priceService.terminate();
		}
		
		this.priceService.initialize();
		
		// Act & Assert
		assertEquals("Non-zero prices should be returned", this.nonZeroPrice,
				this.priceService.getAllPrices("ric_with_prices"));
		
		this.priceService.terminate();
	}
		
	@Test
	 public void getAllPrices_ricWithoutPrices_ZeroPricesAreReturned()
	{
		// Arrange
		try
		{
			int count = 0;
			this.priceService.initialize();
			this.priceUpdateBlockingQueue.put(this.zeroPrice);
			while(this.priceUpdateBlockingQueue.contains(this.zeroPrice))
			{
				Thread.sleep(500);
				if(++count == 10)
					fail();
			}
		}
		catch(InterruptedException ie)
		{
			this.priceService.terminate();
		}
		
		// Act & Assert
		assertEquals("zero prices should be returned", this.zeroPrice,
				this.priceService.getAllPrices("ric_without_prices"));
		
		this.priceService.terminate();
	}
	
	@Test
	 public void getLastPrice_ricWithoutPrices_ZeroPriceReturned()
	{
		// Arrange
		try
		{
			int count = 0;
			this.priceService.initialize();
			this.priceUpdateBlockingQueue.put(this.zeroPrice);
			while(this.priceUpdateBlockingQueue.contains(this.zeroPrice))
			{
				Thread.sleep(500);
				if(++count == 10)
					fail();
			}
		}
		catch(InterruptedException ie)
		{
			this.priceService.terminate();
		}
		
		// Act & Assert
		assertEquals("Zero last price should be returned", BigDecimal.ZERO,
				this.priceService.getLastPrice("ric_without_prices"));
		
		this.priceService.terminate();
	}
	
	@Test
	 public void getLastPrice_ricWithPrices_ValidPriceReturned()
	{
		// Arrange
		try
		{
			int count = 0;
			this.priceService.initialize();
			this.priceUpdateBlockingQueue.put(this.nonZeroPrice);
			while(this.priceUpdateBlockingQueue.contains(this.nonZeroPrice))
			{
				Thread.sleep(500);
				if(++count == 10)
					fail();
			}
		}
		catch(InterruptedException ie)
		{
			this.priceService.terminate();
		}
		
		// Act & Assert
		assertEquals("Non-zero last price should be returned", BigDecimal.TEN,
				this.priceService.getLastPrice("ric_with_prices"));
		
		this.priceService.terminate();
	}
	
	@Test
	 public void getMidPrice_ricWithoutPrices_ZeroPriceReturned()
	{
		// Arrange
		try
		{
			int count = 0;
			this.priceService.initialize();
			this.priceUpdateBlockingQueue.put(this.zeroPrice);
			while(this.priceUpdateBlockingQueue.contains(this.zeroPrice))
			{
				Thread.sleep(500);
				if(++count == 10)
					fail();
			}
		}
		catch(InterruptedException ie)
		{
			this.priceService.terminate();
		}
		
		// Act & Assert
		assertEquals("Zero mid price should be returned", BigDecimal.ZERO,
				this.priceService.getMidPrice("ric_without_prices"));
		
		this.priceService.terminate();
	}
	
	@Test
	 public void getMidPrice_ricWithPrices_ValidPriceReturned()
	{
		// Arrange
		try
		{
			int count = 0;
			this.priceService.initialize();
			this.priceUpdateBlockingQueue.put(this.nonZeroPrice);
			while(this.priceUpdateBlockingQueue.contains(this.nonZeroPrice))
			{
				Thread.sleep(500);
				if(++count == 10)
					fail();
			}
		}
		catch(InterruptedException ie)
		{
			this.priceService.terminate();
		}
		
		// Act & Assert
		assertEquals("Non-zero mid price should be returned", BigDecimal.TEN,
				this.priceService.getMidPrice("ric_with_prices"));
		
		this.priceService.terminate();
	}
	
	@Test
	 public void getAskPrice_ricWithoutPrices_ZeroPriceReturned()
	{
		// Arrange
		try
		{
			int count = 0;
			this.priceService.initialize();
			this.priceUpdateBlockingQueue.put(this.zeroPrice);
			while(this.priceUpdateBlockingQueue.contains(this.zeroPrice))
			{
				Thread.sleep(500);
				if(++count == 10)
					fail();
			}
		}
		catch(InterruptedException ie)
		{
			this.priceService.terminate();
		}
		
		// Act & Assert
		assertEquals("Zero ask price should be returned", BigDecimal.ZERO,
				this.priceService.getAskPrice("ric_without_prices"));
		
		this.priceService.terminate();
	}
	
	@Test
	@Ignore
	 public void getAskPrice_ricWithPrices_ValidPriceReturned()
	{
		// Arrange
		try
		{
			int count = 0;
			this.priceService.initialize();
			this.priceUpdateBlockingQueue.put(this.nonZeroPrice);
			while(this.priceUpdateBlockingQueue.contains(this.nonZeroPrice))
			{
				Thread.sleep(500);
				if(++count == 10)
					fail();
			}
		}
		catch(InterruptedException ie)
		{
			this.priceService.terminate();
		}
		
		// Act & Assert
		assertEquals("Non-zero ask price should be returned", BigDecimal.TEN,
				this.priceService.getAskPrice("ric_with_prices"));
		
		this.priceService.terminate();
	}
	
	@Test
	 public void getBidPrice_ricWithoutPrices_ZeroPriceReturned()
	{
		// Arrange
		try
		{
			int count = 0;
			this.priceService.initialize();
			this.priceUpdateBlockingQueue.put(this.zeroPrice);
			while(this.priceUpdateBlockingQueue.contains(this.zeroPrice))
			{
				Thread.sleep(500);
				if(++count == 10)
					fail();
			}
		}
		catch(InterruptedException ie)
		{
			this.priceService.terminate();
		}
		
		// Act & Assert
		assertEquals("Zero bid price should be returned", BigDecimal.ZERO,
				this.priceService.getBidPrice("ric_without_prices"));
		
		this.priceService.terminate();
	}
	
	@Test
	 public void getBidPrice_ricWithPrices_ValidPriceReturned()
	{
		// Arrange
		try
		{
			int count = 0;
			this.priceService.initialize();
			this.priceUpdateBlockingQueue.put(this.nonZeroPrice);
			while(this.priceUpdateBlockingQueue.contains(this.nonZeroPrice))
			{
				Thread.sleep(500);
				if(++count == 10)
					fail();
			}
		}
		catch(InterruptedException ie)
		{
			this.priceService.terminate();
		}
		
		// Act & Assert
		assertEquals("Non-zero bid price should be returned", BigDecimal.TEN,
				this.priceService.getBidPrice("ric_with_prices"));
		
		this.priceService.terminate();
	}
	
}
